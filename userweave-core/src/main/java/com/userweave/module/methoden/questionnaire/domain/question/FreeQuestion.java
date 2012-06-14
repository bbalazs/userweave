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
import javax.persistence.Enumerated;
import javax.persistence.Transient;

@Entity
public class FreeQuestion extends Question {

	private static final long serialVersionUID = 3675431497107496494L;

	public static final String TYPE = "Free Question";
	
	@Override
	@Transient
	public String getType() {
		return TYPE;
	}
	
	public enum AnswerType {
		SHORT_TEXT("Short text"), LONG_TEXT("Long text"), NUMBER("Number");
		private final String text;
		
		private AnswerType(String text) {
			this.text = text;
		}
		
		@Override
		public String toString() {
			return text;
		}
	};
	
	private AnswerType answerType;

	public AnswerType getAnswerType() {
		return answerType;
	}

	@Enumerated
	public void setAnswerType(AnswerType answerType) {
		this.answerType = answerType;
	}

	@Override
	@Transient
	public FreeQuestion copy() {
		FreeQuestion clone = new FreeQuestion();
		super.copy(clone);
		clone.setAnswerType(answerType);

		return clone;
	}
	
}
