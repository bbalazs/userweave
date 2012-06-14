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

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.userweave.domain.EntityBase;
import com.userweave.module.methoden.questionnaire.domain.question.RatingTerm;

@Entity
@Table(name="questionnaire_singleratinganswer")
public class SingleRatingAnswer extends EntityBase {

	private static final long serialVersionUID = -1973154908977168450L;
	
	private RatingTerm ratingTerm;
	
	public SingleRatingAnswer() {};
	
	public SingleRatingAnswer(RatingTerm ratingTerm, Integer rating) {
		this.ratingTerm = ratingTerm;
		this.rating = rating;
	};

	@ManyToOne
	public RatingTerm getRatingTerm() {
		return ratingTerm;
	}

	public void setRatingTerm(RatingTerm ratingTerm) {
		this.ratingTerm = ratingTerm;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	private Integer rating;
	
	private MultipleRatingAnswer multipleRatingAnswer;

	@ManyToOne(fetch = FetchType.LAZY)
	public MultipleRatingAnswer getMultipleRatingAnswer()
	{
		return multipleRatingAnswer;
	}
	
	public void setMultipleRatingAnswer(MultipleRatingAnswer multipleRatingAnswer)
	{
		this.multipleRatingAnswer = multipleRatingAnswer;
	}
}
