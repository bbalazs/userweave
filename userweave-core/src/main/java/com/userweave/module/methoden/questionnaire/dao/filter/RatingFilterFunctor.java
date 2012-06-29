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

import com.userweave.module.methoden.questionnaire.domain.group.RatingGroup;

import de.userprompt.utils_userweave.query.model.ObjectCondition;
import de.userprompt.utils_userweave.query.model.PropertyCondition;
import de.userprompt.utils_userweave.query.model.QueryObject;

public abstract class RatingFilterFunctor<T extends RatingGroup>  
	extends QuestionnaireFilterFunctorBase<T> 
{
	private static final long serialVersionUID = 1L;

	public RatingFilterFunctor(Class<T> clazz, T group) {
		super(clazz, group);
	}
	
	protected void applyRatingFilter(
		QueryObject query, String ratingAnswerAlias, RatingGroup ratingGroup) 
	{
		if (ratingGroup.getMissing() == null || !ratingGroup.getMissing())
		{
			if (ratingGroup.getLowerBound() != null)
			{
				onLowerBound(query, ratingAnswerAlias, ratingGroup);
			}

			if (ratingGroup.getUpperBound() != null)
			{
				onUpperBound(query, ratingAnswerAlias, ratingGroup);
			}
		}
		else
		{
			query.addAndCondition(ObjectCondition.isNull(ratingAnswerAlias));
		}
	}

	/**
	 * @param query
	 * @param ratingAnswerAlias
	 * @param ratingGroup
	 */
	protected void onUpperBound(QueryObject query, String ratingAnswerAlias,
			RatingGroup ratingGroup)
	{
		query.addAndCondition(PropertyCondition.lessOrEqual(
				ratingAnswerAlias, ratingGroup.getUpperBound() - 1));
	}

	/**
	 * @param query
	 * @param ratingAnswerAlias
	 * @param ratingGroup
	 */
	protected void onLowerBound(QueryObject query, String ratingAnswerAlias,
			RatingGroup ratingGroup)
	{
		query.addAndCondition(PropertyCondition.greaterOrEqual(
				ratingAnswerAlias, ratingGroup.getLowerBound() - 1));
	}

}
