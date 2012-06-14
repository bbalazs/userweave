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
package com.userweave.module.methoden.questionnaire.domain.question;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;

import com.userweave.domain.LocalizedString;
import com.userweave.domain.OrderedEntityBase;

@Entity
public class MultipleRatingQuestion extends Question implements RatingQuestion{

	private static final long serialVersionUID = -4788531469628246578L;

	public static final String TYPE = "Matrix";
	
	@Override
	@Transient
	public String getType() {
		return TYPE;
	}
	
	private Integer numberOfRatingSteps;
	
	private Boolean showNoAnswerOption;

	public Integer getNumberOfRatingSteps() {
		return numberOfRatingSteps;
	}

	public Boolean getShowNoAnswerOption() {
		return showNoAnswerOption;
	}

	public void setShowNoAnswerOption(Boolean showNoAnswerOption) {
		this.showNoAnswerOption = showNoAnswerOption;
	}

	public void setNumberOfRatingSteps(Integer numberOfRatingSteps) {
		this.numberOfRatingSteps = numberOfRatingSteps;
	}

	private AntipodePair antipodePair;
	
	@OneToOne
	@Cascade(value={org.hibernate.annotations.CascadeType.ALL})
	public AntipodePair getAntipodePair() {
		return antipodePair;
	}

	public void setAntipodePair(AntipodePair antipodePair) {
		this.antipodePair = antipodePair;
	}


	private List<RatingTerm> ratingTerms = new ArrayList<RatingTerm>();
	
	@OneToMany
	@JoinColumn(name="question_id")
	@OrderBy(value="position")
	@Cascade(value={org.hibernate.annotations.CascadeType.ALL})
	public List<RatingTerm> getRatingTerms() {
		return ratingTerms;
	}

	public void setRatingTerms(List<RatingTerm> ratingTerms) {
		this.ratingTerms = ratingTerms;
	}

	public void addToRatingTerms(LocalizedString answerText) {
		ratingTerms.add(new RatingTerm(answerText));
		OrderedEntityBase.renumberPositions(ratingTerms);
	}
	
	public void removeFromRatingTerms(LocalizedString answerText) {
		for (RatingTerm simpleAnswer : ratingTerms) {
			if (simpleAnswer.getText() != null && simpleAnswer.getText().equals(answerText)) {
				ratingTerms.remove(simpleAnswer);
				OrderedEntityBase.renumberPositions(ratingTerms);
				break;
			}
		}		
	}
	
	@Override
	@Transient
	public List<LocalizedString> getLocalizedStrings() {
		List<LocalizedString> localizedStrings = super.getLocalizedStrings();

		for (RatingTerm ratingTerm : getRatingTerms()) {
			localizedStrings.addAll(ratingTerm.getLocalizedStrings());
		}
		
		localizedStrings.addAll(antipodePair.getLocalizedStrings());
		
		return localizedStrings;
	}

	@Override
	@Transient
	public MultipleRatingQuestion copy() {
		MultipleRatingQuestion clone = new MultipleRatingQuestion();
		super.copy(clone);
		clone.setAntipodePair(antipodePair.copy());
		clone.setNumberOfRatingSteps(numberOfRatingSteps);
		
		if(ratingTerms != null) {
			List<RatingTerm> cloneRatingTerms = new ArrayList<RatingTerm>();
			for(RatingTerm ratingTerm : ratingTerms) {
				cloneRatingTerms.add(ratingTerm.copy());
			}
			clone.setRatingTerms(cloneRatingTerms);
		}
		
		clone.setShowNoAnswerOption(showNoAnswerOption);

		return clone;
	}

}
