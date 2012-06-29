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

import java.util.List;

import org.hibernate.SQLQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.userweave.dao.SurveyExecutionDao;
import com.userweave.dao.SurveyExecutionDependentQuery;
import com.userweave.dao.filter.FilterFunctor;
import com.userweave.domain.Study;
import com.userweave.domain.SurveyExecution;
import com.userweave.domain.SurveyExecutionState;

import de.userprompt.utils_userweave.query.model.PropertyCondition;
import de.userprompt.utils_userweave.query.model.QueryEntity;
import de.userprompt.utils_userweave.query.template.QueryTemplate;

@Repository
@Transactional
public class SurveyExecutionDaoImpl extends StudyDependendDaoImpl<SurveyExecution> implements SurveyExecutionDao {
	private final static Logger logger = LoggerFactory.getLogger(SurveyExecutionDaoImpl.class);
	
	@Override
	public Class<SurveyExecution> getPersistentClass() {
		return SurveyExecution.class;
	}

	public SurveyExecution findByHashcode(String hashCode) {
		return (SurveyExecution) getCurrentSession()
			.createQuery("from " + getEntityName() + " where hashcode = :hashcode")
			.setParameter("hashcode", hashCode)
			.uniqueResult();
	}

	@Override
	public List<SurveyExecution> findByStudy(Study study, FilterFunctor filterFunctor) {
		SurveyExecutionDependentQuery query = new SurveyExecutionDependentQuery("se", "id");		
		
		// query for ITMTestResult
		QueryEntity result = new QueryEntity(getEntityName(), "se");
		query.setResult("{se.*}");
		
		query.addQueryEntity(result);
		
		// only results for this configuration
		query.addAndCondition(PropertyCondition.equals("se.study_id", study.getId()));

		if(filterFunctor != null)
		{
			filterFunctor.apply(query);
		}
	
		SQLQuery q = new QueryTemplate(query).createSqlQuery((getCurrentSession()));
		q.addEntity("se", getPersistentClass());
		
		long startTime = System.currentTimeMillis();
		List<SurveyExecution> rv = q.list();
		long overallTime = System.currentTimeMillis()-startTime;
		
		logger.info("EXECUTION TIME: "+overallTime+" milliseconds ("+query.toString()+")");
		return rv;
	}
	
	@Override
	public List<SurveyExecution> findFinishedByStudy(Study study,
			FilterFunctor filterFunctor)
	{
		SurveyExecutionDependentQuery query = new SurveyExecutionDependentQuery("se", "id");		
		
		QueryEntity result = new QueryEntity(getEntityName(), "se");
		query.setResult("{se.*}");
		query.addQueryEntity(result);
		
		query.addAndCondition(
			PropertyCondition.equals("se.state", SurveyExecutionState.COMPLETED));

		SQLQuery q = new QueryTemplate(query).createSqlQuery((getCurrentSession()));
		q.addEntity("se", getPersistentClass());
		
		List<SurveyExecution> rv = q.list();
		
		return rv;
	}
}

