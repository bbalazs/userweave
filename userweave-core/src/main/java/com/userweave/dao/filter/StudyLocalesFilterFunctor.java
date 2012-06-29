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
import com.userweave.domain.StudyLocalesGroup;

import de.userprompt.utils_userweave.query.model.PropertyCondition;
import de.userprompt.utils_userweave.query.model.QueryObject;

public class StudyLocalesFilterFunctor extends StudyFilterFunctor<StudyLocalesGroup> 
{
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 * 
	 * @param group
	 */
	public StudyLocalesFilterFunctor(StudyLocalesGroup group) 
	{
		super(group);
	}
		
	@Override
	public QueryObject apply(SurveyExecutionDependentQuery query) 
	{ 
		// join with survey execution to find reached modules.
		query.connectToSurveyExecution("surveyexecution", "survey_exec", "id");
		
		query.addAndCondition(
			PropertyCondition.in("survey_exec.locale", getGroup().getLocales()));
		
		return query;
	}
}
