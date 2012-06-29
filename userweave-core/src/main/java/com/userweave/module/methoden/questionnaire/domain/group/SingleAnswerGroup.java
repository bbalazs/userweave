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

import com.userweave.module.methoden.questionnaire.domain.question.Question;
import com.userweave.module.methoden.questionnaire.domain.question.SingleAnswerQuestion;

@Entity
public class SingleAnswerGroup extends MultiplePossibleAnswersGroup {

	private static final long serialVersionUID = -5877147976324470666L;

	@Override
	@Transient
	public SingleAnswerQuestion getQuestion() {
		return getSingleAnswerQuestion();
	};
	
	private SingleAnswerQuestion question;
	
	@ManyToOne
	public SingleAnswerQuestion getSingleAnswerQuestion() {
		return question;
	}

	public void setSingleAnswerQuestion(SingleAnswerQuestion question) {
		this.question = question;
	}
	
	@Override
	@Transient
	public SingleAnswerGroup copy(Question cloneQuestion) {
		SingleAnswerGroup clone = new SingleAnswerGroup();
		
		super.copy(clone, (SingleAnswerQuestion) cloneQuestion);
		
		clone.setSingleAnswerQuestion((SingleAnswerQuestion) cloneQuestion);

		return clone;
	}

}
