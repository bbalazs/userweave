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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.application.OnFinishCallback;
import com.userweave.dao.StudyDependendDao;
import com.userweave.domain.TestResultEntityBase;
import com.userweave.module.methoden.questionnaire.dao.QuestionnaireConfigurationDao;
import com.userweave.module.methoden.questionnaire.dao.QuestionnaireResultDao;
import com.userweave.module.methoden.questionnaire.domain.QuestionnaireConfigurationEntity;
import com.userweave.module.methoden.questionnaire.domain.QuestionnaireResult;
import com.userweave.module.methoden.questionnaire.domain.question.Question;
import com.userweave.module.methoden.questionnaire.page.QuestionConfigurationPanelFactory;
import com.userweave.module.methoden.questionnaire.page.QuestionnaireSurveyContext;
import com.userweave.module.methoden.questionnaire.page.SubmitCallbackPanel;
import com.userweave.pages.test.singleSurveyTestUI.SingleFormSurveyUI;


@SuppressWarnings("serial")
public class QuestionnaireSurveyUI extends SingleFormSurveyUI<QuestionnaireConfigurationEntity> {

	@SpringBean
	private QuestionnaireConfigurationDao configurationDao;

	@SpringBean
	private QuestionnaireResultDao questionnaireResultDao;

	private final QuestionnaireSurveyContext context;
	
	private final List<SubmitCallbackPanel> questionSubmitCallbacks  = new ArrayList<SubmitCallbackPanel>();

	public QuestionnaireSurveyUI(
		String id, int configurationId, 
		int surveyExecutionId, 
		OnFinishCallback onFinishCallback,
		Locale locale) 
	{
		super(id, configurationId, surveyExecutionId, onFinishCallback, locale);	
	
		QuestionnaireConfigurationEntity configuration = getConfiguration();
		
		
		RepeatingView questionList = new RepeatingView("questions");
		getForm().add(questionList);
		
		List<Question> questions = configuration.getQuestions();
		
		context = new QuestionnaireSurveyContext(surveyExecutionId) {

			@Override
			public QuestionnaireResult getOrCreateQuestionnaireResult() {
				
				QuestionnaireResult result = null;
				if(getQuestionnaireResultId() == null) {
					result = questionnaireResultDao.findByConfigurationAndSurveyExecution(getConfiguration(), getSurveyExecution());
				} else {
					 result = questionnaireResultDao.findById(getQuestionnaireResultId());
				}
				if(result == null) {
					result = new QuestionnaireResult();
				}
				if(saveScopeToResult(result)) {
					questionnaireResultDao.save(result);
				}
				setQuestionnaireResultId(result.getId());

				return result;
			}

			@Override
			public void finishResult(QuestionnaireResult result) {
				questionnaireResultDao.save(result);
			}
			
		};
		for (Question question : questions) {
			// no slidalbe tab panel needed on survey
			SubmitCallbackPanel questionSurveyPanel = 
				new QuestionConfigurationPanelFactory()
					.getSurveyComponent(questionList.newChildId(), question, context, getLocale());
			
			if (questionSurveyPanel != null) {
				questionList.add(questionSurveyPanel);
				questionSubmitCallbacks.add(questionSurveyPanel);
			}			
		}
		
	}
	
	@Override
	protected void onSubmit() {
		//getSurveyTimer().setEndTimeNow();
		
		for (SubmitCallback submitCallback : questionSubmitCallbacks) {
			submitCallback.onSubmit();			
		}
		finish();
	}

	@Override
	protected StudyDependendDao<QuestionnaireConfigurationEntity> getConfigurationDao() {
		return configurationDao;
	}

	@Override
	protected QuestionnaireResult createOrLoadResult() {
		return context.getOrCreateQuestionnaireResult();
	}

	@Override
	protected void finishResult(TestResultEntityBase<QuestionnaireConfigurationEntity> aResult) {
		QuestionnaireResult result = (QuestionnaireResult) aResult;
		
		context.finishResult(result);
	}
	
	@Override
	public void renderHead(IHeaderResponse response)
	{
		super.renderHead(response);
		
		response.renderJavaScriptReference(
			new JavaScriptResourceReference(
					QuestionnaireSurveyUI.class, "questionnaire_survey.js"));
	}
}