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
package com.userweave.module.methoden.rrt.dao;

import java.util.List;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.userweave.dao.SurveyExecutionDependentQuery;
import com.userweave.dao.filter.FilterFunctor;
import com.userweave.dao.impl.TestResultDaoImpl;
import com.userweave.module.methoden.rrt.domain.RrtConfigurationEntity;
import com.userweave.module.methoden.rrt.domain.RrtResult;

import de.userprompt.utils_userweave.query.model.Join;
import de.userprompt.utils_userweave.query.template.QueryTemplate;

@Repository
@Transactional
public class RrtResultDaoImpl extends TestResultDaoImpl<RrtResult, RrtConfigurationEntity> implements RrtResultDao {
	
	@Override
	public Class<RrtResult> getPersistentClass() {
		return RrtResult.class;
	}

	@Override
	public String getEntityResultName()
	{
		return "rrt_result";
	}
	
	@Override
	public List<Object[]> findValidResultsForRrtConf(
			RrtConfigurationEntity conf, FilterFunctor filterFunctor)
	{
		SurveyExecutionDependentQuery query = createQuery(conf, filterFunctor);		
		
		query.addLeftJoin(
			new Join("rrt_orderedterm", "rrtterm", 
					 "result.id", "rrtterm.result_id"));
		
		query.setResult("rrtterm.position, rrtterm.termid");
		
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
		
		return q.list();
	}
}
