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
import com.userweave.module.methoden.questionnaire.domain.group.FreeQuestionNumberGroup;
import com.userweave.module.methoden.questionnaire.domain.group.RatingGroup;
import com.userweave.module.methoden.questionnaire.domain.question.FreeQuestion.AnswerType;

import de.userprompt.utils_userweave.query.model.PropertyCondition;
import de.userprompt.utils_userweave.query.model.QueryObject;

/**
 * Filter functor for free number answers.
 * 
 * @author opr
 */
public class FreeQuestionNumberFilterFunctor 
	extends RatingFilterFunctor<FreeQuestionNumberGroup>
{
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 * 
	 * @param freeQuestionNumberGroup
	 */
	public FreeQuestionNumberFilterFunctor(FreeQuestionNumberGroup freeQuestionNumberGroup) 
	{
		super(FreeQuestionNumberGroup.class, freeQuestionNumberGroup);
	}

	@Override
	public QueryObject apply(SurveyExecutionDependentQuery query) 
	{
		FreeQuestionNumberGroup group = getGroup();
		
		if (group.getFreeQuestion().getAnswerType().equals(AnswerType.NUMBER)) 
		{
			String tableAlias = getUniqueAlias("r_fna");
			
			query.connectToSurveyExecution(
				"results_freenumberanswer", tableAlias, "surveyexecution_id");
			
			query.addAndCondition(PropertyCondition.equals(
				tableAlias + ".question_id",
				group.getFreeQuestion().getId()));
			
//			String answer =  getUniqueAlias("answer");					
//			query.addQueryEntity("FreeNumberAnswer", answer);
			
			//connectToSurveyExecution(query, answer);
					
			applyRatingFilter(query, tableAlias + ".number", group);
		}
		
		return query;
	}
	
	/**
	 * Override, because we need the given value, not the val - 1.
	 */
	@Override
	protected void onUpperBound(QueryObject query, String ratingAnswerAlias,
			RatingGroup ratingGroup)
	{
		query.addAndCondition(PropertyCondition.lessOrEqual(
				ratingAnswerAlias, ratingGroup.getUpperBound()));
	}

	/**
 	 * Override, because we need the given value, not the val - 1.
 	 */
	@Override
	protected void onLowerBound(QueryObject query, String ratingAnswerAlias,
			RatingGroup ratingGroup)
	{
		query.addAndCondition(PropertyCondition.greaterOrEqual(
				ratingAnswerAlias, ratingGroup.getLowerBound()));
	}

}
