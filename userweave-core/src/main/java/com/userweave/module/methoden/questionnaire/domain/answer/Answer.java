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
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForceDiscriminator;
import org.hibernate.annotations.Index;

import com.userweave.domain.EntityBase;
import com.userweave.module.methoden.questionnaire.domain.QuestionnaireResult;
import com.userweave.module.methoden.questionnaire.domain.question.Question;

@Entity
@Table(name="questionnaire_answer")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@org.hibernate.annotations.Table(
		appliesTo = "questionnaire_answer", 
		indexes = { @Index(name="questionnaire_answer_question", columnNames={"question_id","answer_id"} ) } )
@ForceDiscriminator // this annotation resloves an error, if you join with a sub class answer.
public abstract class Answer<T extends Question> extends EntityBase {

	private static final long serialVersionUID = -1358182194983078021L;

	private T question;

	@ManyToOne
	public T getQuestion() {
		return question;
	}

	public void setQuestion(T question) {
		this.question = question;
	}	

	private QuestionnaireResult questionnaireResult;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="result_id")
	public QuestionnaireResult getQuestionnaireResult() {
		return questionnaireResult;
	}

	public void setQuestionnaireResult(QuestionnaireResult questionnaireResult) {
		this.questionnaireResult = questionnaireResult;		
	}

}
