/*******************************************************************************
 * This file is part of UserWeave.
 *
 *     UserWeave is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     UserWeave is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with UserWeave.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2012 User Prompt GmbH | Psychologic IT Expertise
 *******************************************************************************/
package com.userweave.dao.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import org.hibernate.SQLQuery;
import org.springframework.transaction.annotation.Transactional;

import com.userweave.dao.SurveyExecutionDependentQuery;
import com.userweave.dao.TestResultDao;
import com.userweave.dao.filter.FilterFunctor;
import com.userweave.domain.ModuleConfigurationEntityBase;
import com.userweave.domain.ModuleConfigurationWithResultsEntity;
import com.userweave.domain.SurveyExecution;
import com.userweave.domain.SurveyExecutionState;
import com.userweave.domain.TestResultEntityBase;
import com.userweave.domain.service.GeneralStatistics;
import com.userweave.domain.service.SurveyStatisticsService;
import com.userweave.domain.service.impl.GeneralStatisticsImpl;

import de.userprompt.utils_userweave.query.model.Join;
import de.userprompt.utils_userweave.query.model.PropertyCondition;
import de.userprompt.utils_userweave.query.model.QueryEntity;
import de.userprompt.utils_userweave.query.template.QueryTemplate;

@Transactional
public abstract class TestResultDaoImpl<T extends TestResultEntityBase<U>, U extends ModuleConfigurationEntityBase> extends BaseDaoImpl<T> implements TestResultDao<T, U> {

	@SuppressWarnings("unchecked")
	@Override
	public T findByConfigurationAndSurveyExecution(ModuleConfigurationWithResultsEntity configuration, SurveyExecution surveyExecution) {
		String queryString = "from  " + getEntityName() + 
			" where configuration = :configuration and surveyExecution = :surveyexecution";
		return (T) getCurrentSession()
			.createQuery(queryString)
			.setParameter("configuration", configuration)
			.setParameter("surveyexecution", surveyExecution)
			.uniqueResult();
	}

	@Override
	public SurveyExecutionDependentQuery createQuery(
		ModuleConfigurationWithResultsEntity configuration, FilterFunctor filterFunctor) 
	{
		SurveyExecutionDependentQuery query = 
			new SurveyExecutionDependentQuery("se", "id");		
	
		// needed, because we use the 'normal' results and not
		// results_tablename to compute end results.
		query.addAndCondition(
				// FINISHED > STARTED -> result contains both states.
				PropertyCondition.greaterOrEqual( 
					"se.state", 
					SurveyExecutionState.STARTED.ordinal()));
		
		// SELECT
		query.setResult("{result.*}");
		
		// FROM
		QueryEntity result = new QueryEntity(getEntityResultName(), "result");
		query.addQueryEntity(result);
		
		// Join with survey execution table
		query.addLeftJoin(new Join(
			"surveyexecution", "se", "result.surveyexecution_id", "se.id"));
	
		// only results for this configuration
		query.addAndCondition(PropertyCondition.equals("result.configuration_id", configuration.getId()));

		if(filterFunctor != null)
		{
			filterFunctor.apply(query);
		}
		
		return query;
		
	}
	
	/**
	 * Gets called by rrtReportPanel
	 */
	@Override
	public List<T> findValidResults(
		ModuleConfigurationWithResultsEntity configuration, 
		FilterFunctor filterFunctor) 
	{
		SurveyExecutionDependentQuery query = createQuery(configuration, filterFunctor);		
		
		if(query.getHasGroupBy())
		{
			query.setGroupBy(
				"result.id, " + 
				"result.configuration_id, " +
				"result.executionFinished, " +
				"result.executionStarted, " +
				"result.executionTime, " +
				"result.surveyExecution_id");
		}
		
		SQLQuery q = new QueryTemplate(query).createSqlQuery(getCurrentSession());
		
		q.addEntity("result", getPersistentClass());
		
		return q.list();
	}
	
	@Override
	public GeneralStatistics findValidResultStatistics(
		ModuleConfigurationWithResultsEntity configuration,FilterFunctor filterFunctor) 
	{
		SurveyExecutionDependentQuery notFinishedCountQuery = createQuery(configuration, filterFunctor);		

		notFinishedCountQuery.setResult("count(*)");
		notFinishedCountQuery.addAndCondition(PropertyCondition.isNull("result.executiontime"));
		
		Long notFinishedCount = 0L; 
		
		// group by clause does not count overall number of rows, but
		// returns rows with count values!
		if(notFinishedCountQuery.getHasGroupBy())
		{
			List result = new QueryTemplate(notFinishedCountQuery)
			.createSqlQuery(getCurrentSession()).list();
			
			if(result != null && ! result.isEmpty())
			{
				notFinishedCount = ((Integer)result.size()).longValue();
			}
		}
		else
		{
			BigInteger notFinishedCountBig = 
				(BigInteger) new QueryTemplate(notFinishedCountQuery)
				.createSqlQuery(getCurrentSession()).uniqueResult();
			
			if(notFinishedCountBig != null)
			{
				notFinishedCount = notFinishedCountBig.longValue();
			}
		}
		
		SurveyExecutionDependentQuery query = createQuery(configuration, filterFunctor);
		
		query.addAndCondition(
				PropertyCondition.less(
						"result.executiontime",
						new Long(SurveyStatisticsService.UNBELIEVABLE_LONG)));
		
		Long finishedCount;
		Double average, deviation;
		Integer started;
		
		// if group by applies, we need to calculate the values by hand, since
		// the result ist not a single row!
		if(query.getHasGroupBy())
		{
			query.setResult("count(result.id), avg(result.executiontime)");
			
			List<Object[]> results = 
				new QueryTemplate(query).createSqlQuery(getCurrentSession()).list();
			
			finishedCount = ((Integer)results.size()).longValue();
			average = computeAverage(results);
			deviation = computeDeviation(average, results);
		}
		else
		{
			query.setResult("count(*), avg(result.executiontime), stddev(result.executiontime)");
			
			List<Object[]> results = 
				new QueryTemplate(query).createSqlQuery(getCurrentSession()).list();
			
			Object[] result = results.get(0);
			finishedCount   = result[0] == null ? 0 : ((BigInteger) result[0]).longValue();
			average     	= result[1] == null ? 0 :((BigDecimal) result[1]).doubleValue();
			deviation   	= result[2] == null ? 0 :((BigDecimal) result[2]).doubleValue();
		}

		started = notFinishedCount.intValue() + finishedCount.intValue();
		
		return new GeneralStatisticsImpl(started, started, finishedCount.intValue(), 
				average.longValue()/1000, deviation/1000);
	}

	@Override
	public int getValidResultCount(U configuration, FilterFunctor filterFunctor) 
	{
		SurveyExecutionDependentQuery query = new SurveyExecutionDependentQuery("se", "id");		

		// query for number of questionnaire results
		QueryEntity result = new QueryEntity(getEntityResultName(), "result");
		query.addQueryEntity(result);
		query.setResult("count(se.id)");
		
		// result must match configuration
		query.addAndCondition(PropertyCondition.equals("result.configuration_id", configuration.getId()));
		
		// join on survey execution for filtering
		query.addLeftJoin(new Join(
				"surveyexecution", "se", "result.surveyexecution_id", "se.id"));

		// only count started or completed surveys
		query.addAndCondition(
			PropertyCondition.greaterOrEqual(
				"se.state", 
				SurveyExecutionState.STARTED.ordinal()));

		if (filterFunctor != null) 
		{
			filterFunctor.apply(query);
		}
		
		if(query.getHasGroupBy())
		{
			query.setGroupBy("se.id");
			
			SQLQuery q = new QueryTemplate(query).createSqlQuery(getCurrentSession());
			
			return q.list().size();
		}
		else
		{
			SQLQuery q = new QueryTemplate(query).createSqlQuery(getCurrentSession());
			
			return ((BigInteger)q.uniqueResult()).intValue();
		}
	}
	
	/**
	 * Computes the sum of a list of execution times
	 * 
	 * @param results
	 * @return
	 */
	protected Long sum(List<Object[]> results)
	{
		if(results == null || results.size() == 0)
		{
			return 0L;
		}
		else
		{
			Long sum = 0L;
			
			for(Object[] result : results)
			{
				sum += ((BigDecimal) result[1]).longValue();
			}
			
			return sum;
		}
	}
	
	/**
	 * Compute the average/mean of a list of execution times.
	 * 
	 * @param results
	 * @return
	 */
	protected Double computeAverage(List<Object[]> results)
	{
		Long sum = sum(results);
        
		double mean = 0;

        if (sum > 0) 
        {
            mean = sum / (results.size() * 1.0);
        }
        
        return mean;
	}
	
	/**
	 * Computes the standard deviation of a list of execution times.
	 * 
	 * @see http://mindprod.com/jgloss/sd.html, sdKnuth()
	 * 
	 * @param mean
	 * @param results
	 * @return
	 */
	protected Double computeDeviation(Double mean, List<Object[]> results)
	{
		final int n = results.size();
	    
		if ( n < 2 )
	    {
			return new Double(0);
	    }
	    
		double avg = mean;
	    double sum = 0;
	    
	    for ( int i = 1; i < n; i++ )
	    {
	    	Long data = ((BigDecimal) results.get(i)[1]).longValue();
	    	
	         double newavg = avg + ( data - avg ) / ( i + 1 );
	         sum += ( data - avg ) * ( data  - newavg ) ;
	         avg = newavg;
	    }
	
	    return Math.sqrt( sum / ( n - 1 ) );
	}
	
	
	protected abstract String getEntityResultName();
}
