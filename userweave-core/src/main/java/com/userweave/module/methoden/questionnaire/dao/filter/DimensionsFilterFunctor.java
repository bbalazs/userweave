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
import com.userweave.module.methoden.questionnaire.domain.group.DimensionsGroup;

import de.userprompt.utils_userweave.query.model.Join;
import de.userprompt.utils_userweave.query.model.PropertyCondition;
import de.userprompt.utils_userweave.query.model.QueryObject;

/**
 * Filter functor for dimensions answers
 * 
 * @author opr
 */
public class DimensionsFilterFunctor extends RatingFilterFunctor<DimensionsGroup>
{
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 * 
	 * @param dimensionsGroup
	 */
	public DimensionsFilterFunctor(DimensionsGroup dimensionsGroup) 
	{
		super(DimensionsGroup.class, dimensionsGroup);
	}

	@Override
	public QueryObject apply(SurveyExecutionDependentQuery query) 
	{	
		String tableAlias =  getUniqueAlias("r_sda");		
		
		query.connectToSurveyExecution(
			"results_singledimensionanswer", tableAlias, "surveyexecution_id");
		
		// join to get rating of single dimensions rating
		String joinAlias = getUniqueAlias("q_sda");
		
		query.addLeftJoin(new Join(
			"questionnaire_singledimensionanswer", joinAlias, 
			tableAlias + ".singledimensionanswer_id", joinAlias + ".id"));
		
		DimensionsGroup group = getGroup();
		
		query.addAndCondition(
			PropertyCondition.equals(
				tableAlias + ".antipodepair_id", group.getAntipodePair().getId()));
		
		applyRatingFilter(query, joinAlias + ".rating", group);
		
		//query.addQueryEntity("MultipleDimensionsAnswer", answer);
		
//		String singleDimensionAnswer =  getUniqueAlias("singleDimensionAnswer");
//		query.addLeftJoin(answer + ".ratings", singleDimensionAnswer);
//		
//		String antipodePair = getUniqueAlias("antipodePair");
//		query.addLeftJoin(singleDimensionAnswer + ".antipodePair", antipodePair);
//				
//		DimensionsGroup group = getGroup();
//		
//		//connectToSurveyExecution(query, answer);
//		
//		query.addAndCondition(PropertyCondition.equals(antipodePair, group.getAntipodePair()));
//		
//		applyRatingFilter(query, singleDimensionAnswer + ".rating", group);
		
		return query;
	}

}
