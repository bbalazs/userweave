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
package com.userweave.module.methoden.questionnaire.domain.group;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.userweave.module.methoden.questionnaire.domain.question.MultipleRatingQuestion;
import com.userweave.module.methoden.questionnaire.domain.question.Question;
import com.userweave.module.methoden.questionnaire.domain.question.RatingTerm;

@Entity
public class MultipleRatingGroup extends RatingGroup {

	private static final long serialVersionUID = -6805450298632781869L;

	@Override
	@Transient
	public Question getQuestion() {
		return getMultipleRatingQuestion();
	};
	
	private MultipleRatingQuestion multipleRatingQuestion;

	@ManyToOne
	public MultipleRatingQuestion getMultipleRatingQuestion() {
		return multipleRatingQuestion;
	}

	public void setMultipleRatingQuestion(MultipleRatingQuestion multipleRatingQuestion) {
		this.multipleRatingQuestion = multipleRatingQuestion;
	}
	
	
	private RatingTerm ratingTerm;
	
	@ManyToOne
	public RatingTerm getRatingTerm() {
		return ratingTerm;
	}

	public void setRatingTerm(RatingTerm ratingTerm) {
		this.ratingTerm = ratingTerm;
	}

	@Override
	@Transient
	public MultipleRatingGroup copy(Question cloneQuestion) {
		MultipleRatingGroup clone = new MultipleRatingGroup();
		super.copy(clone);

		clone.setMultipleRatingQuestion((MultipleRatingQuestion) cloneQuestion);

		if(ratingTerm != null) {
			int idx = multipleRatingQuestion.getRatingTerms().indexOf(ratingTerm);
			clone.setRatingTerm(clone.getMultipleRatingQuestion().getRatingTerms().get(idx));
		}		

		return clone;
	}	
}
