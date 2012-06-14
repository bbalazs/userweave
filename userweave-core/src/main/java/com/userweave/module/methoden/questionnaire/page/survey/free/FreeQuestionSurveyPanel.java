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
package com.userweave.module.methoden.questionnaire.page.survey.free;


import java.util.Locale;

import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.components.validator.ValidationStyleBehavior;
import com.userweave.dao.BaseDao;
import com.userweave.module.methoden.questionnaire.dao.FreeQuestionDao;
import com.userweave.module.methoden.questionnaire.domain.answer.FreeNumberAnswer;
import com.userweave.module.methoden.questionnaire.domain.answer.FreeTextAnswer;
import com.userweave.module.methoden.questionnaire.domain.question.FreeQuestion;
import com.userweave.module.methoden.questionnaire.domain.question.FreeQuestion.AnswerType;
import com.userweave.module.methoden.questionnaire.page.QuestionnaireSurveyContext;
import com.userweave.module.methoden.questionnaire.page.survey.AnswerPanel;

/**
 * @author oma
 */
@SuppressWarnings("serial")
public class FreeQuestionSurveyPanel extends AnswerPanel<FreeQuestion>  {

	@SpringBean
	private FreeQuestionDao freeQuestionDao;
	
	private String answerString;
	
	private Integer answerNumber;
	
	
	public FreeQuestionSurveyPanel(
		String id, FreeQuestion question, QuestionnaireSurveyContext context, Locale locale) {
		super(id, question, context, locale);
		
		final AnswerType answerType = question.getAnswerType();
				
		add( 
			new TextField("answerNumber", new PropertyModel(this, "answerNumber"), Integer.class) {
				@Override
				public boolean isVisible() {
					return answerType == AnswerType.NUMBER;
				}
			}
			.add(new ValidationStyleBehavior())
		);
		
		add( new TextField("answerShortText", new PropertyModel(this, "answerString"), String.class) {
			@Override
			public boolean isVisible() {
				return answerType == AnswerType.SHORT_TEXT;
			}
		});
		
		add(new TextArea("answerLongText", new PropertyModel(this, "answerString")) {
			@Override
			public boolean isVisible() {
				return answerType == AnswerType.LONG_TEXT;
			}
		});
	}

	@Override
	protected BaseDao<FreeQuestion> getQuestionDao() {
		return freeQuestionDao;
	}

	public void onSubmit() {
		FreeQuestion question = getQuestion();
		if (question.getAnswerType() == AnswerType.NUMBER) {
			if (answerNumber == null) {
				return;
			}
			FreeNumberAnswer answer = (FreeNumberAnswer) loadAnswer();
			if(answer == null) {
				answer = new FreeNumberAnswer();
			}
			answer.setNumber(answerNumber);
			saveAnswer(answer);
		} else {
			if (answerString == null) {
				return;
			}
			FreeTextAnswer answer = (FreeTextAnswer) loadAnswer();
			if(answer == null) {
				answer = new FreeTextAnswer();
			}
			answer.setText(answerString);
			saveAnswer(answer);
		}
	}

	@Override
	protected void initAnswer() {
		if (getQuestion().getAnswerType() == AnswerType.NUMBER) {
			FreeNumberAnswer answer = (FreeNumberAnswer) loadAnswer();
			if(answer != null) {
				answerNumber = answer.getNumber();
			}
		} else {
			FreeTextAnswer answer = (FreeTextAnswer) loadAnswer();
			if(answer != null) {
				answerString = answer.getText();
			}
		}
	}
}

