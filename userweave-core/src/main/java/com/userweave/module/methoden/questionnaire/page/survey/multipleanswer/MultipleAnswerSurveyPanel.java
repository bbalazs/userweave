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
package com.userweave.module.methoden.questionnaire.page.survey.multipleanswer;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.components.model.LocalizedChoiceRenderer;
import com.userweave.dao.BaseDao;
import com.userweave.domain.LocalizedString;
import com.userweave.module.methoden.questionnaire.dao.QuestionWithMultiplePossibleAnswersDao;
import com.userweave.module.methoden.questionnaire.domain.QuestionWithMultiplePossibleAnswers;
import com.userweave.module.methoden.questionnaire.domain.answer.MultipleAnswersAnwer;
import com.userweave.module.methoden.questionnaire.domain.question.MultipleAnswersQuestion;
import com.userweave.module.methoden.questionnaire.page.QuestionnaireSurveyContext;
import com.userweave.module.methoden.questionnaire.page.survey.AnswerPanel;
import com.userweave.pages.components.twoColumnPanel.multiColumnCheckboxMultipleChoice.MultiColumnCheckboxMultipleChoice;

/**
 * @author oma
 */
public class MultipleAnswerSurveyPanel 
	extends AnswerPanel<QuestionWithMultiplePossibleAnswers>  
{
	private static final long serialVersionUID = 1L;

	@SpringBean
	private QuestionWithMultiplePossibleAnswersDao questionDao;
	
	private final List<LocalizedString> answers = new ArrayList<LocalizedString>();
	
	public MultipleAnswerSurveyPanel(
		String id, MultipleAnswersQuestion question, QuestionnaireSurveyContext context, Locale locale) {
		super(id, question, context, locale);

		
		List localizedAnswers = getQuestion().getPossibleAnswers();
		sortPossibleAnswers(localizedAnswers);

		add(
			new MultiColumnCheckboxMultipleChoice(
				"possibleAnswers", 
				new PropertyModel(this, "answers"),
				localizedAnswers,
				new LocalizedChoiceRenderer(getLocale())
			)
		);
	}

	@Override
	protected MultipleAnswersQuestion getQuestion() {
		return (MultipleAnswersQuestion) getDefaultModelObject();
	}

	
	@Override
	protected void initAnswer() {
		MultipleAnswersAnwer answer = (MultipleAnswersAnwer) loadAnswer();
		if(answer != null) {
			answers.addAll(answer.getAnswers());
		}
	}

	public void onSubmit() {
		
		MultipleAnswersAnwer answer = (MultipleAnswersAnwer) loadAnswer();
		if(answer == null) {
			answer = new MultipleAnswersAnwer();
		}
		answer.setAnswers(answers);
		
		saveAnswer(answer);
	}
	
	@Override
	protected BaseDao<QuestionWithMultiplePossibleAnswers> getQuestionDao() {
		return questionDao;
	}


}

