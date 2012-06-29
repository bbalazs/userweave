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
package com.userweave.module.methoden.iconunderstandability.dao.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.userweave.dao.SurveyExecutionDependentQuery;
import com.userweave.dao.filter.FilterFunctor;
import com.userweave.dao.impl.TestResultDaoImpl;
import com.userweave.domain.ModuleConfigurationWithResultsEntity;
import com.userweave.domain.SurveyExecutionState;
import com.userweave.domain.service.GeneralStatistics;
import com.userweave.domain.service.SurveyStatisticsService;
import com.userweave.domain.service.impl.GeneralStatisticsImpl;
import com.userweave.module.methoden.iconunderstandability.dao.ITMTestResultDao;
import com.userweave.module.methoden.iconunderstandability.domain.ITMTestResult;
import com.userweave.module.methoden.iconunderstandability.domain.IconTermMatchingConfigurationEntity;

import de.userprompt.utils_userweave.query.model.Join;
import de.userprompt.utils_userweave.query.model.PropertyCondition;
import de.userprompt.utils_userweave.query.template.QueryTemplate;

@Repository
@Transactional
public class ITMTestResultDaoImpl extends TestResultDaoImpl<ITMTestResult, IconTermMatchingConfigurationEntity> implements ITMTestResultDao {

	@Override
	public Class<ITMTestResult> getPersistentClass() {
		return ITMTestResult.class;
	}

	@Override
	public String getEntityResultName()
	{
		return "itm_result";
	}
	
	public List<Object[]> findAllValidExecutionTimesAndIconCount() {

		String query = 
			new StringBuilder()
				.append("select mapping.executionTime, (select count(*) from configuration.images)")
				.append("from IconTermMatchingConfigurationEntity configuration ")
				.append("join configuration.results result " )
				.append("join result.iconTermMappings mapping ")
				.append("where ") 
				.append(" result.surveyExecution.state > :state") // this is a shortcut for state in (STARTED, COMPLETED)
				.toString();

		List<Object[]> result = 
			getCurrentSession()
			.createQuery(query)
			.setParameter("state", SurveyExecutionState.STARTED)
			.list();
		
		return result;
	}
	
	/**
	 * Create gerneral statistics for a given term.
	 * 
	 * @param configuration
	 * @param filterFunctor
	 * @param termId
	 * @return
	 */
	@Override
	public GeneralStatistics findValidResultStatisticsForTerm(
			ModuleConfigurationWithResultsEntity configuration,
			FilterFunctor filterFunctor,
			Integer termValueId)
	{
		SurveyExecutionDependentQuery notFinishedCountQuery = createQuery(configuration, filterFunctor);		

		notFinishedCountQuery.addLeftJoin(
			new Join("itm_icontermmapping", "itmmapping", 
					 "itmmapping.result_id", "result.id"));
		
		notFinishedCountQuery.addAndCondition(PropertyCondition.equals("itmmapping.term_id", termValueId));
		
		notFinishedCountQuery.setResult("count(*)");
		notFinishedCountQuery.addAndCondition(PropertyCondition.isNull("itmmapping.executiontime"));
		
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
		
		query.addLeftJoin(
				new Join("itm_icontermmapping", "itmmapping", 
						 "itmmapping.result_id", "result.id"));
			
		query.addAndCondition(PropertyCondition.equals("itmmapping.term_id", termValueId));
		
		query.addAndCondition(
				PropertyCondition.less(
						"itmmapping.executiontime",
						new Long(SurveyStatisticsService.UNBELIEVABLE_LONG)));
		
		Long finishedCount;
		Double average, deviation;
		Integer started;
		
		// if group by applies, we need to calculate the values by hand, since
		// the result ist not a single row!
		if(query.getHasGroupBy())
		{
			query.setResult("count(result.id), avg(itmmapping.executiontime)");
			
			List<Object[]> results = 
				new QueryTemplate(query).createSqlQuery(getCurrentSession()).list();
			
			finishedCount = ((Integer)results.size()).longValue();
			average = computeAverage(results);
			deviation = computeDeviation(average, results);
		}
		else
		{
			query.setResult("count(*), avg(itmmapping.executiontime), stddev(itmmapping.executiontime)");
			
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

}
