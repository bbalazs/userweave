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

import com.userweave.dao.SurveyExecutionDependentQuery;
import com.userweave.module.methoden.questionnaire.domain.group.FreeQuestionTextGroup;
import com.userweave.module.methoden.questionnaire.domain.question.FreeQuestion.AnswerType;

import de.userprompt.utils_userweave.query.model.PropertyCondition;
import de.userprompt.utils_userweave.query.model.QueryObject;

/**
 * Filter functor for free text answers.
 * 
 * @author opr
 */
public class FreeQuestionTextFilterFunctor 
	extends QuestionnaireFilterFunctorBase<FreeQuestionTextGroup> 
{
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 * 
	 * @param group
	 */
	public FreeQuestionTextFilterFunctor(FreeQuestionTextGroup group) 
	{
		super(FreeQuestionTextGroup.class, group);
	}

	@Override
	public QueryObject apply(SurveyExecutionDependentQuery queryObject) 
	{
		FreeQuestionTextGroup group = getGroup();	
		
		if (group.getFreeQuestion().getAnswerType().equals(AnswerType.LONG_TEXT) || 
			group.getFreeQuestion().getAnswerType().equals(AnswerType.SHORT_TEXT))
		{
			String tableAlias = getUniqueAlias("r_fta");
			
			queryObject.connectToSurveyExecution(
				"results_freetextanswer", tableAlias, "surveyexecution_id");
			
			if(getGroup().getQuestion() != null) 
			{
				queryObject.addAndCondition(
					PropertyCondition.equals(
							tableAlias + ".question_id", getGroup().getQuestion().getId()));
			}
			
			if((group.getChoice() != null) && 
			   (group.getChoice().compareTo("") != 0) &&
			   (group.getFilterText() != null) &&
			   (group.getFilterText() != ""))
			{
				if(group.getChoice().compareTo("Equals") == 0)
				{
					queryObject.addAndCondition(
						PropertyCondition.equals(
							tableAlias + ".text", group.getFilterText()));
				}
				else if(group.getChoice().compareTo("Not Equals") == 0)
				{
					queryObject.addAndCondition(
						PropertyCondition.notEquals(
								tableAlias + ".text", group.getFilterText()));
				}
				else if(group.getChoice().compareTo("Contains") == 0)
				{
					queryObject.addAndCondition(
						PropertyCondition.ilike(
								tableAlias + ".text", group.getFilterText()));
				}
				else if(group.getChoice().compareTo("Not Contains") == 0)
				{
					queryObject.addAndCondition(
						PropertyCondition.notIlike(
							tableAlias + ".text", group.getFilterText()));
				}
			}
		}
		
		return queryObject;
	}

}
