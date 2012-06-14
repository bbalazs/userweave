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
import com.userweave.module.methoden.questionnaire.domain.group.MultipleRatingGroup;

import de.userprompt.utils_userweave.query.model.Join;
import de.userprompt.utils_userweave.query.model.PropertyCondition;
import de.userprompt.utils_userweave.query.model.QueryObject;

/**
 * Filter functor for multiple rating answers.
 * 
 * @author opr
 */
public class MultipleRatingFilterFunctor 
	extends RatingFilterFunctor<MultipleRatingGroup>
{
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 * 
	 * @param multipleRatingGroup
	 */
	public MultipleRatingFilterFunctor(MultipleRatingGroup multipleRatingGroup) 
	{
		super(MultipleRatingGroup.class, multipleRatingGroup);
	}

	@Override
	public QueryObject apply(SurveyExecutionDependentQuery query) 
	{		
		String tableAlias = getUniqueAlias("r_sra");
		
		query.connectToSurveyExecution(
			"results_singleratinganswer", tableAlias, "surveyexecution_id");
		
		// join to get rating of single dimensions rating
		String joinAlias = getUniqueAlias("q_sra");
		
		query.addLeftJoin(new Join(
			"questionnaire_singleratinganswer", joinAlias, 
			tableAlias + ".singleratinganswer_id", joinAlias + ".id"));
		
//		query.addQueryEntity("MultipleRatingAnswer", answer);
//		
//		String singleRatingAnswer =  getUniqueAlias("singleRatingAnswer");
//		query.addLeftJoin(answer + ".ratings", singleRatingAnswer);
//		
//		String ratingTerm = getUniqueAlias("ratingTerm");
//		query.addLeftJoin(singleRatingAnswer + ".ratingTerm", ratingTerm);
				
		MultipleRatingGroup group = getGroup();
		
		//connectToSurveyExecution(query, answer);
		
		query.addAndCondition(
			PropertyCondition.equals(
					tableAlias + ".ratingterm_id", group.getRatingTerm().getId()));
		
		applyRatingFilter(query, joinAlias + ".rating", group);

		return query;
	}
}
