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
import com.userweave.module.methoden.questionnaire.domain.question.AntipodePair;

@Entity
@Table(name="questionnaire_singledimensionanswer")
public class SingleDimensionAnswer extends EntityBase {

	private static final long serialVersionUID = -1973154908977168450L;
	
	private AntipodePair antipodePair;
	
	public SingleDimensionAnswer() {};
	
	public SingleDimensionAnswer(AntipodePair antipodePair, Integer rating) {
		this.antipodePair = antipodePair;
		this.rating = rating;
	};

	@ManyToOne
	public AntipodePair getAntipodePair() {
		return antipodePair;
	}

	public void setAntipodePair(AntipodePair antipodePair) {
		this.antipodePair = antipodePair;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	private Integer rating;
	
	
	private MultipleDimensionsAnswer multipleDimensionsAnswer;

	@ManyToOne(fetch = FetchType.LAZY)
	public MultipleDimensionsAnswer getMultipleDimensionsAnswer()
	{
		return multipleDimensionsAnswer;
	}

	public void setMultipleDimensionsAnswer(
			MultipleDimensionsAnswer multipleDimensionsAnswer)
	{
		this.multipleDimensionsAnswer = multipleDimensionsAnswer;
	}
}
