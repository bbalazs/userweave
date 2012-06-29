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
package com.userweave.module.methoden.questionnaire.page.survey.singleanswer;


import java.util.List;
import java.util.Locale;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.components.model.LocalizedChoiceRenderer;
import com.userweave.dao.BaseDao;
import com.userweave.dao.LocalizedStringDao;
import com.userweave.domain.LocalizedString;
import com.userweave.module.methoden.questionnaire.dao.QuestionWithMultiplePossibleAnswersDao;
import com.userweave.module.methoden.questionnaire.domain.QuestionWithMultiplePossibleAnswers;
import com.userweave.module.methoden.questionnaire.domain.answer.AnswerToSingleAnswerQuestion;
import com.userweave.module.methoden.questionnaire.domain.question.SingleAnswerQuestion;
import com.userweave.module.methoden.questionnaire.domain.question.SingleAnswerQuestion.DisplayType;
import com.userweave.module.methoden.questionnaire.page.QuestionnaireSurveyContext;
import com.userweave.module.methoden.questionnaire.page.survey.AnswerPanel;
import com.userweave.pages.components.twoColumnPanel.multiColumnRadioChoicePanel.TwoColumnRadioChoicePanel;

/**
 * @author oma
 */
@SuppressWarnings("serial")
public class SingleAnswerSurveyPanel extends AnswerPanel<QuestionWithMultiplePossibleAnswers>  {

	@SpringBean
	private QuestionWithMultiplePossibleAnswersDao questionDao;
	
	@SpringBean
	private LocalizedStringDao localizedStringDao;

	private LocalizedString answer;
	
	public SingleAnswerSurveyPanel(String id, SingleAnswerQuestion question, QuestionnaireSurveyContext context, Locale locale) {
		super(id, question, context, locale);
		
		List localizedAnswers = getQuestion().getPossibleAnswers();
		sortPossibleAnswers(localizedAnswers);
		
		add(
			new DropDownChoice(
				"possibleAnswersDropDown", 
				new MyPropertyModel(SingleAnswerSurveyPanel.this, "answer"),
				localizedAnswers,
				new LocalizedChoiceRenderer(getLocale())
			) {
				@Override
				public boolean isVisible() {
					return displayAsDropDownChoice();
				}
			}
		);
		
		add(
			new TwoColumnRadioChoicePanel(
				"possibleAnswersRadioChoice",
				new MyPropertyModel(SingleAnswerSurveyPanel.this, "answer"),
				localizedAnswers,
				new LocalizedChoiceRenderer(getLocale())
			) {
				@Override
				public boolean isVisible() {
					return !displayAsDropDownChoice();
				}
			}
		);
	}

	public boolean displayAsDropDownChoice() {
		return getQuestion().getDisplayType() == DisplayType.DROP_DOWN;
	}
	
	@Override
	protected SingleAnswerQuestion getQuestion() {
		return (SingleAnswerQuestion) getDefaultModelObject();
	}

	@Override
	public void initAnswer() {
		AnswerToSingleAnswerQuestion answerToQuestion = (AnswerToSingleAnswerQuestion) loadAnswer();	
		if(answerToQuestion != null) {
			answer = answerToQuestion.getAnswer();
		}
	}

	public void onSubmit() 
	{
		// load the entity or create an empty one. This is
		// necessary to create 0 values in a csv export.
		AnswerToSingleAnswerQuestion answer = (AnswerToSingleAnswerQuestion) loadAnswer();
		
		if(answer == null) 
		{
			answer = new AnswerToSingleAnswerQuestion();
		}
		
		if(getAnswer() != null) 
		{
			answer.setAnswer(getAnswer());
		}
		
		saveAnswer(answer);
	}

	private LocalizedString getAnswer() {
		if(answer != null) {
			// is dirty, so we reload the answer instance
			answer = localizedStringDao.findById(answer.getId());
		}
		
		return answer;
	}

	@Override
	protected BaseDao<QuestionWithMultiplePossibleAnswers> getQuestionDao() {
		return questionDao;
	}

	private class MyPropertyModel extends PropertyModel {

		public MyPropertyModel(Object modelObject, String expression) {
			super(modelObject, expression);
		}

		@Override
		public void setObject(Object object) {
			super.setObject(object);
		}
	}
}

