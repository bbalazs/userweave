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
package com.userweave.module.methoden.questionnaire.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;

import com.userweave.domain.LocalizedString;
import com.userweave.domain.OrderedEntityBase;
import com.userweave.module.methoden.questionnaire.domain.question.Question;

@Entity
public abstract class QuestionWithMultiplePossibleAnswers extends Question {
	
	private static final long serialVersionUID = 6634753233293553439L;
	
	private List<LocalizedString> possibleAnswers = new ArrayList<LocalizedString>();
	
	@OneToMany
	@Cascade(value={org.hibernate.annotations.CascadeType.ALL})
	@OrderBy(value="position")
	public List<LocalizedString> getPossibleAnswers() {
		return possibleAnswers;
	}

	public void setPossibleAnswers(List<LocalizedString> possibleAnswers) {
		this.possibleAnswers = possibleAnswers;
	}

	public void addToPossibleAnswers(LocalizedString answerText) {
		possibleAnswers.add(answerText);
		OrderedEntityBase.renumberPositions(possibleAnswers);
	}
	
	public void removeFromPossibleAnswers(LocalizedString answerText) {
		possibleAnswers.remove(answerText);
		OrderedEntityBase.renumberPositions(possibleAnswers);
	}
	
	@Override
	@Transient
	public List<LocalizedString> getLocalizedStrings() {
		List<LocalizedString> rv = super.getLocalizedStrings();
		rv.addAll(getPossibleAnswers());
		return rv;
	}
	
	@Transient
	protected QuestionWithMultiplePossibleAnswers copy(QuestionWithMultiplePossibleAnswers clone) {
		super.copy(clone);

		if(possibleAnswers != null) {

			OrderedEntityBase.renumberPositions(possibleAnswers);
			
			List<LocalizedString> clonePossibleAnswers = new ArrayList<LocalizedString>();
			for(LocalizedString possibleAnswer : possibleAnswers) {
				clonePossibleAnswers.add(possibleAnswer.copy());
			}
			clone.setPossibleAnswers(clonePossibleAnswers);
		}
		
		return clone;
	}
}
