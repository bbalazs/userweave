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

import com.userweave.module.methoden.questionnaire.domain.question.AntipodePair;
import com.userweave.module.methoden.questionnaire.domain.question.DimensionsQuestion;

@Entity
public class MultipleDimensionsAnswer extends Answer<DimensionsQuestion>  {

	private static final long serialVersionUID = -1904232223162686543L;

	private List<SingleDimensionAnswer> ratings = new ArrayList<SingleDimensionAnswer>();

	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="multipledimensionsanswer_id")
	public List<SingleDimensionAnswer> getRatings() {
		return ratings;
	}

	public void setRatings(List<SingleDimensionAnswer> ratings) {
		this.ratings = ratings;
	}
	
	public void addToRatings(SingleDimensionAnswer rating) {
		ratings.add(rating);
	}
	
	@Transient
	public SingleDimensionAnswer getRating(AntipodePair antipodePair) {
		for(SingleDimensionAnswer answer: getRatings()) {
			if(answer.getAntipodePair().equals(antipodePair)) {
				return answer;
			}
		}
		return null;
	}
	
}
