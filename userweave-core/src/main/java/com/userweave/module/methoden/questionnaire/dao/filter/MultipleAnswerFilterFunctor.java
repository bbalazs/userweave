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
import com.userweave.module.methoden.questionnaire.domain.group.MultipleAnswersGroup;

import de.userprompt.utils_userweave.query.model.Join;
import de.userprompt.utils_userweave.query.model.PropertyCondition;
import de.userprompt.utils_userweave.query.model.QueryObject;

/**
 * Filter functor for multiple rating (matrix) answers.
 * 
 * @author opr
 *
 */
public class MultipleAnswerFilterFunctor 
	extends QuestionnaireFilterFunctorBase<MultipleAnswersGroup> 
{	
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 * 
	 * @param multipleAnswersGroup		
	 */
	public MultipleAnswerFilterFunctor(MultipleAnswersGroup multipleAnswersGroup) 
	{
		super(MultipleAnswersGroup.class, multipleAnswersGroup);
	}

	@Override
	public QueryObject apply(SurveyExecutionDependentQuery query) 
	{	
		String tableAlias = getUniqueAlias("r_ma");
		
		query.connectToSurveyExecution(
			"results_multipleanswer", tableAlias, "surveyexecution_id");
		
		//String multipleAnswersAnswer = getUniqueAlias("answer");
		
		// multiple-answer 
		//query.addQueryEntity(new QueryEntity("MultipleAnswersAnwer", multipleAnswersAnswer));

		// connect multiple-answer to survey execution
		//connectToSurveyExecution(query, multipleAnswersAnswer);

		if(getGroup().getOperator() != null && 
		   getGroup().getOperator().equals(MultipleAnswersGroup.Operator.AND)) 
		{
			applyAndCondition(query, tableAlias);
		} 
		else 
		{
			applyOrCondition(query, tableAlias);
		}
		
		return query;
	}

	/**
	 * And condition means, that all answers in the group have to be
	 * in the list of given answers for an answer object. 
	 *  
	 * @param query
	 * @param tableAlias
	 */
	private void applyAndCondition(QueryObject query, String tableAlias) 
	{
		List<LocalizedString> answers = getGroup().getAnswers();
		
		if(answers == null | answers.isEmpty())
		{
			return;
		}
		
		for (LocalizedString localizedString : answers) 
		{
			String uniqueAlias = getUniqueAlias("qals");
				
			query.addLeftJoin(new Join(
				"questionnaire_answer_localized_string", 
				uniqueAlias,
				tableAlias + ".multipleanswer_id", 
				uniqueAlias + ".questionnaire_answer_id"));
			
			query.addAndCondition(
					PropertyCondition.equals(
						uniqueAlias + ".answers_id", 
						localizedString.getId()));
		}
		
		// WHERE
//		query.addAndCondition(
//				PropertyCondition.in(
//					qalsAlias + ".answers_id", ids));
		
		// GROUP BY
////		query.setGroupBy(qalsAlias + ".questionnaire_answer_id");
//		query.setGroupBy("qa.id");
//		// HAVING
//		query.setHaving("count(" + qalsAlias + 
//						".questionnaire_answer_id) = " + 
//						ids.size());
		
//			// single answer from multiple-answer
//			String singleAnswer = multipleAnswersAnswer + "_answer" + answerIndex;
//			
//			query.addQueryEntity(new QueryEntity("LocalizedString", singleAnswer));
//			
//			query.addAndCondition(ObjectCondition.in(singleAnswer, multipleAnswersAnswer + ".answers"));
//			
//			// connect single answer to answers in group
//			query.addAndCondition(PropertyCondition.equals(singleAnswer, localizedString));			
//			
//			answerIndex++;
//		}
	}
	
	/**
	 * Or condition means, that at least one answer in the group has to
	 * be in the answer set of the answer object.
	 * 
	 * @param query
	 * @param tableAlias
	 */
	private void applyOrCondition(QueryObject query, String tableAlias) 
	{
		String qalsAlias = getUniqueAlias("qals");
		
		// join with localized strings for answer
		query.addLeftJoin(new Join(
				"questionnaire_answer_localized_string", qalsAlias,
				tableAlias + ".multipleanswer_id", 
				qalsAlias + ".questionnaire_answer_id"));
		
		List<LocalizedString> answers = getGroup().getAnswers();
		
		List<Integer> ids = new LinkedList<Integer>();
		
		for (LocalizedString localizedString : answers) 
		{
			ids.add(localizedString.getId());
		}
		
		// WHERE
		query.addAndCondition(
				PropertyCondition.in(
					qalsAlias + ".answers_id", ids));
		
		// GROUP BY
		query.setGroupBy(qalsAlias + ".questionnaire_answer_id");
		
//		// single answer from multiple-answer
//		String singleAnswer = multipleAnswersAnswer + "_answer";
//		query.addQueryEntity(new QueryEntity("LocalizedString", singleAnswer));
//		
//		query.addAndCondition(ObjectCondition.in(singleAnswer, multipleAnswersAnswer + ".answers"));
//
//		// connect single answer to answers in group
//		query.addAndCondition(PropertyCondition.in(singleAnswer, getGroup().getAnswers()));
	}
}
