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

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Index;

import com.userweave.domain.TestResultEntityBase;

@Entity
@Table(name="questionnaire_result")
@org.hibernate.annotations.Table(
		appliesTo = "questionnaire_result", 
		indexes = { @Index(name="questionnaire_result_configuration", columnNames={"configuration_id"} ) } )
public class QuestionnaireResult extends TestResultEntityBase<QuestionnaireConfigurationEntity> {

	private static final long serialVersionUID = -4943259584782140681L;
	
	/*
	answers are reachable (and deleteable) via question!
	
	private List<Answer> answers = new ArrayList<Answer>();
	
	@OneToMany(mappedBy="questionnaireResult")
	@Cascade(value={org.hibernate.annotations.CascadeType.PERSIST})
	private List<Answer> getAnswers() {
		return answers;
	}

	private void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}
	*/
}
