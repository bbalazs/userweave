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

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

import com.userweave.domain.LocalizedString;
import com.userweave.domain.OrderedEntityBase;

@Entity
public class DimensionsQuestion extends Question implements RatingQuestion {

	private static final long serialVersionUID = 3675431497107496494L;

	public static final String TYPE = "Semantic differential";
	
	@Override
	@Transient
	public String getType() {
		return TYPE;
	}
	
	private Boolean showNoAnswerOption;
	
	
	public Boolean getShowNoAnswerOption() {
//		if(showNoAnswerOption != null)
			return showNoAnswerOption;
//		else
//			return false;
	}

	public void setShowNoAnswerOption(Boolean showNoAnswerOption) {
		this.showNoAnswerOption = showNoAnswerOption;
	}

	private List<AntipodePair> antipodePairs = new ArrayList<AntipodePair>();

	@OneToMany(cascade={CascadeType.ALL})
	@JoinColumn(name="quesion_id")
	@OrderBy(value="position")
	public List<AntipodePair> getAntipodePairs() {
		return antipodePairs;
	}

	public void setAntipodePairs(List<AntipodePair> antipodePairs) {
		this.antipodePairs = antipodePairs;
	}
	
	public void addToAntipodePairs(AntipodePair antipodePair) {
		antipodePairs.add(antipodePair);	
		OrderedEntityBase.renumberPositions(antipodePairs);
	}
	
	public void removeFromAntipodePairs(AntipodePair antipodePair) {
		antipodePairs.remove(antipodePair);
		OrderedEntityBase.renumberPositions(antipodePairs);
	}


	private Integer numberOfRatingSteps = 0;

	public Integer getNumberOfRatingSteps() {
		return numberOfRatingSteps;
	}

	public void setNumberOfRatingSteps(Integer numberOfRatingSteps) {
		this.numberOfRatingSteps = numberOfRatingSteps;
	}
	
	@Override
	@Transient
	public List<LocalizedString> getLocalizedStrings() {
		List<LocalizedString> localizedStrings = super.getLocalizedStrings();

		for (AntipodePair pair : getAntipodePairs()) {
			localizedStrings.addAll(pair.getLocalizedStrings());
		}
		
		return localizedStrings;
	}

	@Override
	@Transient
	public Question copy() {
		DimensionsQuestion clone = new DimensionsQuestion();
		super.copy(clone);

		if(antipodePairs != null) {
			List<AntipodePair> cloneAntipodePairs = new ArrayList<AntipodePair>();
			for(AntipodePair antipodePair : antipodePairs) {
				cloneAntipodePairs.add(antipodePair.copy());
			}
			clone.setAntipodePairs(cloneAntipodePairs);
		}
		
		clone.setNumberOfRatingSteps(numberOfRatingSteps);
		clone.setShowNoAnswerOption(showNoAnswerOption);
		clone.setRandomizeAntipodePosition(randomizeAntipodePosition);
		
		return clone;
	}

	private boolean randomizeAntipodePosition;

	public boolean getRandomizeAntipodePosition()
	{
		return randomizeAntipodePosition;
	}

	public void setRandomizeAntipodePosition(boolean randomizeAntipodePosition)
	{
		this.randomizeAntipodePosition = randomizeAntipodePosition;
	}
	
}
