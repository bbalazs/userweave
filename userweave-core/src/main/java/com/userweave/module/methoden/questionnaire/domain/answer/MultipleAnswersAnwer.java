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

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;

import org.hibernate.annotations.Cascade;

import com.userweave.domain.LocalizedString;
import com.userweave.module.methoden.questionnaire.domain.question.MultipleAnswersQuestion;

@Entity
public class MultipleAnswersAnwer extends Answer<MultipleAnswersQuestion> {

	private static final long serialVersionUID = -1904232223162686543L;

	private List<LocalizedString> answers = new ArrayList<LocalizedString>();

	@ManyToMany
	@Cascade(value={org.hibernate.annotations.CascadeType.ALL})
	@OrderBy(value="position")
	public List<LocalizedString> getAnswers() {
		return answers;
	}

	public void setAnswers(List<LocalizedString> answers) {
		this.answers = answers;
	}
	
	public void addToAnswers(LocalizedString answer) {
		answers.add(answer);
	}
}
