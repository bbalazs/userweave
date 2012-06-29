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
package com.userweave.dao.filter;

import com.userweave.dao.SurveyExecutionDependentQuery;
import com.userweave.domain.ModuleReachedGroup;

import de.userprompt.utils_userweave.query.model.PropertyCondition;
import de.userprompt.utils_userweave.query.model.QueryObject;

/**
 * Filter for module reached. This filter meens, that only 
 * results a considered, that reached at least the given module.
 * 
 * @author opr
 *
 */
public class ModuleReachedFilterFunctor extends StudyFilterFunctor<ModuleReachedGroup>
{
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 * 
	 * @param group
	 */
	public ModuleReachedFilterFunctor(ModuleReachedGroup group) 
	{
		super(group);
	}

	@Override
	public QueryObject apply(SurveyExecutionDependentQuery query) 
	{
		ModuleReachedGroup group = getGroup();
		
		if(group != null)
		{
			// join with survey execution to find reached modules.
			query.connectToSurveyExecution("surveyexecution", "survey_exec", "id");
			
			query.addAndCondition(
				PropertyCondition.greaterOrEqual(
						"survey_exec.modulesExecuted", getGroup().getModulePosition() + 1));
		}
		
		return query;
	}

}
