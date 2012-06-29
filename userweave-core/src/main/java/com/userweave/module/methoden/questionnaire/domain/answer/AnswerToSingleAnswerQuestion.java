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
import javax.persistence.OneToOne;

import com.userweave.domain.LocalizedString;
import com.userweave.module.methoden.questionnaire.domain.question.SingleAnswerQuestion;

@Entity
public class AnswerToSingleAnswerQuestion extends Answer<SingleAnswerQuestion> {

	private static final long serialVersionUID = 3670282545324001866L;

	private LocalizedString answer;

	@OneToOne
	public LocalizedString getAnswer() {
		return answer;
	}

	public void setAnswer(LocalizedString answer) {
		this.answer = answer;
	}
	
	
}
