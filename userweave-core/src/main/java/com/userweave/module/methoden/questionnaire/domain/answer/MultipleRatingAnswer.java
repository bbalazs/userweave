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
package com.userweave.module.methoden.questionnaire.domain.answer;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.userweave.module.methoden.questionnaire.domain.question.MultipleRatingQuestion;
import com.userweave.module.methoden.questionnaire.domain.question.RatingTerm;

@Entity
public class MultipleRatingAnswer extends Answer<MultipleRatingQuestion>  {

	private static final long serialVersionUID = -1904232223162686543L;

	private List<SingleRatingAnswer> ratings = new ArrayList<SingleRatingAnswer>();
	
	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="multipleratinganswer_id")
	public List<SingleRatingAnswer> getRatings() {
		return ratings;
	}

	public void setRatings(List<SingleRatingAnswer> ratings) {
		this.ratings = ratings;
	}
	
	public void addToRatings(SingleRatingAnswer rating) {
		ratings.add(rating);
	}
	
	@Transient
	public SingleRatingAnswer getRating(RatingTerm rating) {
		for(SingleRatingAnswer answer: getRatings()) {
			if(answer.getRatingTerm().equals(rating)) {
				return answer;
			}
		}
		return null;
	}
	
}
