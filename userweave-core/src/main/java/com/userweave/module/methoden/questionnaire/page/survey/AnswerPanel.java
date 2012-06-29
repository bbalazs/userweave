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
package com.userweave.module.methoden.questionnaire.page.survey;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.components.model.LocalizedPropertyModel;
import com.userweave.dao.BaseDao;
import com.userweave.dao.SurveyExecutionDao;
import com.userweave.domain.SurveyExecution;
import com.userweave.domain.util.LocalizedAnswer;
import com.userweave.domain.util.LocalizedAnswerComparator;
import com.userweave.module.methoden.questionnaire.dao.AnswerDao;
import com.userweave.module.methoden.questionnaire.domain.QuestionnaireResult;
import com.userweave.module.methoden.questionnaire.domain.answer.Answer;
import com.userweave.module.methoden.questionnaire.domain.question.Question;
import com.userweave.module.methoden.questionnaire.domain.question.Question.OrderType;
import com.userweave.module.methoden.questionnaire.page.QuestionnaireSurveyContext;
import com.userweave.module.methoden.questionnaire.page.SubmitCallbackPanel;

/**
 * @author oma
 */
@SuppressWarnings("serial")
public abstract class AnswerPanel<T extends Question> extends SubmitCallbackPanel {

	private final QuestionnaireSurveyContext context;

	@SpringBean
	private AnswerDao answerDao;

	@SpringBean
	private SurveyExecutionDao surveyExecutionDao;

	private Locale selectedLocale;
	
	@Override
	public Locale getLocale()
	{
		return selectedLocale;
	}
	
	public AnswerPanel(String id, T question, QuestionnaireSurveyContext context, Locale locale) 
	{
		super(id);
		
		this.selectedLocale = locale;
		
		this.context = context;
		
		final Integer questionId = question.getId();
		
		LoadableDetachableModel questionModel = new LoadableDetachableModel() {
			@Override
			protected Object load() {
				return getQuestionDao().findById(questionId);
			}					
		};
		
		setDefaultModel(new CompoundPropertyModel(questionModel));
		
		AnswerFeedbackPanel feedback = new AnswerFeedbackPanel("feedback");
		
		feedback.setFilter(
			new IFeedbackMessageFilter() {

				@Override
				public boolean accept(FeedbackMessage message) {
					return AnswerPanel.this.contains(message.getReporter(), true);
				}
				
			}
		);
			
		add(feedback);
		
		add(new Label("text", new LocalizedPropertyModel(questionModel, "text", getLocale())));
		
	}

	@Override
	protected void onBeforeRender() {
		// pavkovic 2009.07.14: here we restore the answer state on browser back button.
		initAnswer();
		super.onBeforeRender();
	}


	protected abstract BaseDao<?> getQuestionDao();
	
	@SuppressWarnings("unchecked")
	protected T getQuestion() {
		return (T) getDefaultModelObject();
	}
	
	private SurveyExecution getSurveyExecution() {
		return surveyExecutionDao.findById(context.getSurveyExecutionId());
	}
	
	private QuestionnaireResult getOrCreateQuestionnaireResult() {
		return context.getOrCreateQuestionnaireResult();
	}

	/**
	 * initAnswer is called to ensure that already stored results are displayed on the answer panel
	 */
	protected abstract void initAnswer();
	
	protected Answer loadAnswer() {
		return answerDao.findByQuestionAndSurveyExecution(getQuestion(), getSurveyExecution());
	}
	
	protected void saveAnswer(Answer answer) {
		answer.setQuestionnaireResult(getOrCreateQuestionnaireResult());
		answer.setQuestion(getQuestion());
		answerDao.save(answer);
    }
	
	protected final void sortPossibleAnswers(List<LocalizedAnswer> list) {
		if (getQuestion().getOrderType() == OrderType.ALPHABETICAL) {
			Collections.sort(list, new LocalizedAnswerComparator(getLocale()));
		}
		if (getQuestion().getOrderType() == OrderType.RANDOM) {
			Collections.shuffle(list);
		}
	}
}

