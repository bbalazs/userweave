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

import com.userweave.module.methoden.questionnaire.domain.question.AntipodePair;
import com.userweave.module.methoden.questionnaire.domain.question.DimensionsQuestion;
import com.userweave.module.methoden.questionnaire.domain.question.Question;

@Entity
public class DimensionsGroup extends RatingGroup {

	private static final long serialVersionUID = 8525658509833210877L;

	@Override
	@Transient
	public Question getQuestion() {
		return getDimensionsQuestion();
	};
	
	private DimensionsQuestion dimensionsQuestion;
	
	@ManyToOne
	public DimensionsQuestion getDimensionsQuestion() {
		return dimensionsQuestion;
	}

	public void setDimensionsQuestion(DimensionsQuestion dimensionsQuestion) {
		this.dimensionsQuestion = dimensionsQuestion;
	}

	private AntipodePair antipodePair;
	
	@ManyToOne
	public AntipodePair getAntipodePair() {
		return antipodePair;
	}

	public void setAntipodePair(AntipodePair ratingTerm) {
		this.antipodePair = ratingTerm;
	}

	@Transient
	@Override
	public DimensionsGroup copy(Question cloneQuestion) {
		DimensionsGroup clone = new DimensionsGroup();
		super.copy(clone);
		
		clone.setDimensionsQuestion((DimensionsQuestion) cloneQuestion);

		if(antipodePair != null) {
			int idx = dimensionsQuestion.getAntipodePairs().indexOf(antipodePair);
			clone.setAntipodePair(clone.getDimensionsQuestion().getAntipodePairs().get(idx));
		}
		
		return clone;
	}
}
