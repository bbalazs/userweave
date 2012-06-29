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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

import com.userweave.domain.LocalizedString;
import com.userweave.domain.OrderedEntityBase;
import com.userweave.module.methoden.questionnaire.domain.QuestionWithMultiplePossibleAnswers;

@Entity
public abstract class MultiplePossibleAnswersGroup extends QuestionnaireGroup {

	private static final long serialVersionUID = -1967027065169309989L;

	@Transient
	@Override
	public abstract QuestionWithMultiplePossibleAnswers getQuestion();
	
	private List<LocalizedString> answers = new ArrayList<LocalizedString>();

	@ManyToMany
	@OrderBy(value="position")
	public List<LocalizedString> getAnswers() {
		return answers;
	}

	public void setAnswers(List<LocalizedString> answers) {
		this.answers = answers;
	}
	
	@Transient
	public MultiplePossibleAnswersGroup copy(MultiplePossibleAnswersGroup clone, QuestionWithMultiplePossibleAnswers cloneQuestion) {
		super.copy(clone);

		if(answers != null) {
		
			OrderedEntityBase.renumberPositions(answers);

			List<LocalizedString> cloneAnswers = new ArrayList<LocalizedString>();
			for(LocalizedString answer : answers ) {
				int idx = getQuestion().getPossibleAnswers().indexOf(answer);
				cloneAnswers.add(cloneQuestion.getPossibleAnswers().get(idx));
			}
			clone.setAnswers(cloneAnswers);
		}
		
		return clone;
	}
	
}
