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
package com.userweave.module.methoden.questionnaire.dao.filter;

import java.util.LinkedList;
import java.util.List;

import com.userweave.dao.SurveyExecutionDependentQuery;
import com.userweave.domain.LocalizedString;
import com.userweave.module.methoden.questionnaire.domain.group.SingleAnswerGroup;

import de.userprompt.utils_userweave.query.model.PropertyCondition;
import de.userprompt.utils_userweave.query.model.QueryObject;

/**
 * Filter functor for single answers.
 * 
 * @author opr
 */
public class SingleAnswerFilterFunctor 
	extends QuestionnaireFilterFunctorBase<SingleAnswerGroup> 
{
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 * 
	 * @param singleAnswerGroup
	 */
	public SingleAnswerFilterFunctor(SingleAnswerGroup singleAnswerGroup) 
	{
		super(SingleAnswerGroup.class, singleAnswerGroup);
	}

	@Override
	public QueryObject apply(SurveyExecutionDependentQuery query) 
	{		
		String tableAlias = getUniqueAlias("r_sa");
		
		query.connectToSurveyExecution(
				"results_singleanswer", tableAlias, "surveyexecution_id");
		
		if(getGroup().getAnswers() != null && 
		   !getGroup().getAnswers().isEmpty())
		{
			List<Integer> ids = new LinkedList<Integer>();
			
			for(LocalizedString answer : getGroup().getAnswers())
			{
				ids.add(answer.getId());
			}
			
			query.addAndCondition(
				PropertyCondition.in(tableAlias + ".answer_id", ids));
		}
		
		
//		query.addQueryEntity(new QueryEntity("AnswerToSingleAnswerQuestion", answer));
//
//		//connectToSurveyExecution(query, answer);
//		
//		if( getGroup().getAnswers() != null && !getGroup().getAnswers().isEmpty())
//		{
//			query.addAndCondition(PropertyCondition.in(answer + ".answer", getGroup().getAnswers()));
//		}
		
		return query;
	}
}
