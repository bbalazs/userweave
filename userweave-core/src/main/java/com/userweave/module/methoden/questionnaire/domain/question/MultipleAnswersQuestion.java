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

import javax.persistence.Entity;
import javax.persistence.Transient;

import com.userweave.module.methoden.questionnaire.domain.QuestionWithMultiplePossibleAnswers;

@Entity
public class MultipleAnswersQuestion extends QuestionWithMultiplePossibleAnswers {

	private static final long serialVersionUID = 7371325395509337058L;
	
	public static final String TYPE = "Multiple Answers";
	
	@Override
	@Transient
	public String getType() {
		return TYPE;
	}

	@Override
	@Transient
	public MultipleAnswersQuestion copy() {
		MultipleAnswersQuestion clone = new MultipleAnswersQuestion();
		super.copy(clone);
		return clone;
	}
}
