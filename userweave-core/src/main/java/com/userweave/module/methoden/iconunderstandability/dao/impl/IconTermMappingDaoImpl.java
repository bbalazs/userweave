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

import java.util.List;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.userweave.dao.SurveyExecutionDependentQuery;
import com.userweave.dao.filter.FilterFunctor;
import com.userweave.dao.impl.BaseDaoImpl;
import com.userweave.module.methoden.iconunderstandability.dao.IconTermMappingDao;
import com.userweave.module.methoden.iconunderstandability.domain.IconTermMatchingConfigurationEntity;
import com.userweave.module.methoden.iconunderstandability.page.survey.IconTermMapping;

import de.userprompt.utils_userweave.query.model.Join;
import de.userprompt.utils_userweave.query.model.ObjectCondition;
import de.userprompt.utils_userweave.query.model.PropertyCondition;
import de.userprompt.utils_userweave.query.model.QueryEntity;
import de.userprompt.utils_userweave.query.template.QueryTemplate;

@Repository
@Transactional
public class IconTermMappingDaoImpl extends BaseDaoImpl<IconTermMapping> implements IconTermMappingDao {

	@Override
	public Class<IconTermMapping> getPersistentClass() {
		return IconTermMapping.class;
	}

	private SurveyExecutionDependentQuery createValidResultQuery(
		IconTermMatchingConfigurationEntity configuration, FilterFunctor filterFunctor) 
	{
		SurveyExecutionDependentQuery query = new SurveyExecutionDependentQuery("se", "id");		
		
		// query for ITMTestResult
		QueryEntity result = new QueryEntity("itm_" + getEntityName(), "mapping");
		
		query.setResult("{mapping.*}");
		
		query.addQueryEntity(result);

		query.addLeftJoin(new Join(
				"itm_result", "result", "mapping.result_id", "result.id"));

		// Join with survey execution table
		query.addLeftJoin(new Join(
			"surveyexecution", "se", "result.surveyexecution_id", "se.id"));

		query.addLeftJoin(new Join(
			"itm_configuration","configuration", "result.configuration_id", "configuration.id"));
		
		query.addLeftJoin(new Join(
			"itm_term","terms", "configuration.id", "terms.configuration_id"));	
		
		// only results for this configuration
		query.addAndCondition(PropertyCondition.equals("result.configuration_id", configuration.getId()));
		
		query.addAndCondition(ObjectCondition.equals("terms.value_id","mapping.term_id"));
			
		filterFunctor.apply(query);
		/**
		select count(*)
			  from itm_icontermmapping mapping
			  left join itm_result result on mapping.result_id = result.id
			  left join surveyexecution se on result.surveyexecution_id = se.id
          left join itm_configuration configuration on result.configuration_id = configuration.id
			  left join itm_term term on configuration.id = term.configuration_id
			 where se.state >=2
			   and result.configuration_id = 260052
           and term.value_id = mapping.term_id
	 */
		
		return query;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<IconTermMapping> findValidResults(IconTermMatchingConfigurationEntity configuration, FilterFunctor filterFunctor) {
		
		//TODO query.setResult(result);
		SurveyExecutionDependentQuery query = createValidResultQuery(configuration, filterFunctor);
		
		SQLQuery q = new QueryTemplate(query).createSqlQuery((getCurrentSession()));
		q.addEntity("mapping", getPersistentClass());
		
		return q.list();
		
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> findValidResultsFast(IconTermMatchingConfigurationEntity configuration, FilterFunctor filterFunctor) {
		SurveyExecutionDependentQuery query = createValidResultQuery(configuration, filterFunctor);
		query.setResult("mapping.term_id, mapping.image_id, mapping.executiontime, se.id");
		
		return new QueryTemplate(query).createSqlQuery(getCurrentSession()).list();
			
	}

}
