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
package com.userweave.module.methoden.questionnaire.page;

import java.io.Serializable;

import com.userweave.module.methoden.questionnaire.domain.QuestionnaireResult;


@SuppressWarnings("serial")
public abstract class QuestionnaireSurveyContext implements Serializable {

	private final Integer surveyExecutionId;
	
	public QuestionnaireSurveyContext(Integer surveyExecutionId) {
		this.surveyExecutionId = surveyExecutionId;
	}
	
	public Integer getSurveyExecutionId() {
		return surveyExecutionId;
		
	}

	private Integer questionnaireResultId;

	public abstract QuestionnaireResult getOrCreateQuestionnaireResult();
	
	public abstract void finishResult(QuestionnaireResult result);

	protected Integer getQuestionnaireResultId() {
		return questionnaireResultId;
	}
	
	protected void setQuestionnaireResultId(Integer questionnaireResultId) {
		this.questionnaireResultId = questionnaireResultId;
	}
	
}
