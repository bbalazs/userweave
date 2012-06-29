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

import com.userweave.module.methoden.questionnaire.domain.question.FreeQuestion;
import com.userweave.module.methoden.questionnaire.domain.question.Question;

@Entity
public class FreeQuestionNumberGroup extends RatingGroup {

	private static final long serialVersionUID = -3363126181101375832L;

	@Override
	@Transient
	public Question getQuestion() {
		return getFreeQuestion();
	};
	
	private FreeQuestion freeQuestion;
	
	@ManyToOne
	public FreeQuestion getFreeQuestion() {
		return freeQuestion;
	}

	public void setFreeQuestion(FreeQuestion dimensionsQuestion) {
		this.freeQuestion = dimensionsQuestion;
	}

	@Override
	@Transient
	public FreeQuestionNumberGroup copy(Question cloneQuestion) {
		FreeQuestionNumberGroup clone = new FreeQuestionNumberGroup();
		super.copy(clone);

		clone.setFreeQuestion((FreeQuestion) cloneQuestion);

		return clone;
	}
}
