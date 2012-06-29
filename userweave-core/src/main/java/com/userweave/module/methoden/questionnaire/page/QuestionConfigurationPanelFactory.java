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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.Component;

import com.userweave.components.callback.EventHandler;
import com.userweave.module.methoden.questionnaire.domain.question.AntipodePair;
import com.userweave.module.methoden.questionnaire.domain.question.DimensionsQuestion;
import com.userweave.module.methoden.questionnaire.domain.question.FreeQuestion;
import com.userweave.module.methoden.questionnaire.domain.question.MultipleAnswersQuestion;
import com.userweave.module.methoden.questionnaire.domain.question.MultipleRatingQuestion;
import com.userweave.module.methoden.questionnaire.domain.question.Question;
import com.userweave.module.methoden.questionnaire.domain.question.SingleAnswerQuestion;
import com.userweave.module.methoden.questionnaire.domain.question.SingleAnswerQuestion.DisplayType;
import com.userweave.module.methoden.questionnaire.page.conf.question.dimensions.DimensionsQuestionConfUI;
import com.userweave.module.methoden.questionnaire.page.conf.question.free.FreeQuestionConfUI;
import com.userweave.module.methoden.questionnaire.page.conf.question.multipleanswers.MultiplePossibleAnswersConfUI;
import com.userweave.module.methoden.questionnaire.page.conf.question.multiplerating.MultipleRatingConfUI;
import com.userweave.module.methoden.questionnaire.page.conf.question.singleanswer.SingleAnswerConfUI;
import com.userweave.module.methoden.questionnaire.page.report.question.dimensions.DimensionsReportPanel;
import com.userweave.module.methoden.questionnaire.page.report.question.freeanswer.FreeQuestionReportPanel;
import com.userweave.module.methoden.questionnaire.page.report.question.multipleanswers.MultipleAnswerReportPanel;
import com.userweave.module.methoden.questionnaire.page.report.question.multiplerating.MultipleRatingReportPanel;
import com.userweave.module.methoden.questionnaire.page.report.question.singleAnswer.SingleAnswerReportPanel;
import com.userweave.module.methoden.questionnaire.page.survey.dimensions.DimensionsQuestionSurveyPanel;
import com.userweave.module.methoden.questionnaire.page.survey.free.FreeQuestionSurveyPanel;
import com.userweave.module.methoden.questionnaire.page.survey.multipleanswer.MultipleAnswerSurveyPanel;
import com.userweave.module.methoden.questionnaire.page.survey.multiplerating.MultRatingSurveyPanel;
import com.userweave.module.methoden.questionnaire.page.survey.singleanswer.SingleAnswerSurveyPanel;
import com.userweave.pages.components.slidableajaxtabpanel.ChangeTabsCallback;
import com.userweave.pages.configuration.report.FilterFunctorCallback;

@SuppressWarnings("serial")
public class QuestionConfigurationPanelFactory implements Serializable{
	
	private interface QuestionPanelCreator extends Serializable {
		public boolean appliesForType(String type);
		
		public Component createConfigurationPanel(
				String id, 
				String questionType, 
				Integer questionId, 
				int configurationId, 
				Locale studyLocale, 
				EventHandler callback,
				ChangeTabsCallback changeTabsCallback);
		
		public Component createReportPanel(String id, Locale locale, Question question, FilterFunctorCallback filterFunctorCallback);
		
		public SubmitCallbackPanel createSurveyPanel(String id, Question question, QuestionnaireSurveyContext context, Locale locale);
		
		public Question createQuestion(String type);

		public String getType();		
	};
	
	private abstract class QuestionPanelCreatorImpl implements QuestionPanelCreator {

		private final String type;
				
		public QuestionPanelCreatorImpl(String type) {
			this.type = type;
		}
		
		public boolean appliesForType(String type) {
			return this.type.equals(type);
		}		

		public String getType() {
			return type;
		}
		
		public Component createReportPanel(String id, Question question) {
			return null;
		}
	}
	
	private final List<QuestionPanelCreator> questionPanelCreators = new ArrayList<QuestionPanelCreator>();
	
	protected  void register(QuestionPanelCreator creator) {
		questionPanelCreators.add(creator);		
	}
	
	public QuestionConfigurationPanelFactory() {
		register(
			new QuestionPanelCreatorImpl(MultipleAnswersQuestion.TYPE) {

				public Component createConfigurationPanel(
						String id, 
						String questionType, 
						Integer questionId, 
						int configurationId, 
						Locale studyLocale, 
						EventHandler callback,
						ChangeTabsCallback changeTabsCallback) 
				{
					return new MultiplePossibleAnswersConfUI(
							id, 
							configurationId, 
							questionId, 
							studyLocale, 
							callback, 
							changeTabsCallback);
				}

				public SubmitCallbackPanel createSurveyPanel(String id, Question question, QuestionnaireSurveyContext context, Locale locale) {
					return new MultipleAnswerSurveyPanel(id, (MultipleAnswersQuestion) question, context, locale);
				}
				
				@Override
				public Component createReportPanel(String id, Locale locale, Question question, FilterFunctorCallback filterFunctorCallback) {
					return new MultipleAnswerReportPanel(id, locale,  (MultipleAnswersQuestion) question, filterFunctorCallback);
				}

				public Question createQuestion(String type) {
					return new MultipleAnswersQuestion();
				}
			}
		);
		
		register(
			new QuestionPanelCreatorImpl(SingleAnswerQuestion.TYPE) {

				public Component createConfigurationPanel(String id, String questionType, Integer questionId, int configurationId, Locale studyLocale, EventHandler callback, ChangeTabsCallback changeTabsCallback) {
					return new SingleAnswerConfUI(id, configurationId, questionId, studyLocale, callback, changeTabsCallback);
				}

				public SubmitCallbackPanel createSurveyPanel(String id, Question question, QuestionnaireSurveyContext context, Locale locale) {
					return new SingleAnswerSurveyPanel(id, (SingleAnswerQuestion) question, context, locale);
				}
				
				@Override
				public Component createReportPanel(String id, Locale locale, Question question, FilterFunctorCallback filterFunctorCallback) {
					return new SingleAnswerReportPanel(id, locale,  (SingleAnswerQuestion) question, filterFunctorCallback);
				}

				public Question createQuestion(String type) {
					SingleAnswerQuestion singleAnswerQuestion = new SingleAnswerQuestion();
					singleAnswerQuestion.setDisplayType(DisplayType.RADIOBUTTONS);
					return singleAnswerQuestion;
				}
			}
		);		
		
		register(
			new QuestionPanelCreatorImpl(FreeQuestion.TYPE) {

				public Component createConfigurationPanel(String id, String questionType, Integer questionId, int configurationId, Locale studyLocale, EventHandler callback, ChangeTabsCallback changeTabsCallback) {
					return new FreeQuestionConfUI(id, configurationId, questionId, studyLocale, callback, changeTabsCallback);
				}

				public SubmitCallbackPanel createSurveyPanel(String id, Question question, QuestionnaireSurveyContext context, Locale locale) {
					return new FreeQuestionSurveyPanel(id, (FreeQuestion) question, context, locale);
				}
				
				@Override
				public Component createReportPanel(String id, Locale locale, Question question, FilterFunctorCallback filterFunctorCallback) {
					return new FreeQuestionReportPanel(id,  locale, (FreeQuestion) question, filterFunctorCallback);
				}

				public Question createQuestion(String type) {
					FreeQuestion question = new FreeQuestion();
					question.setAnswerType(FreeQuestion.AnswerType.SHORT_TEXT);
					return question;
				}
			}
		);
		
		register(
			new QuestionPanelCreatorImpl(MultipleRatingQuestion.TYPE) {

				public Component createConfigurationPanel(String id, String questionType, Integer questionId, int configurationId, Locale studyLocale, EventHandler callback, ChangeTabsCallback changeTabsCallback) {
					return new MultipleRatingConfUI(id, configurationId, questionId, studyLocale, callback, changeTabsCallback);
				}

				public SubmitCallbackPanel createSurveyPanel(String id, Question question, QuestionnaireSurveyContext context, Locale locale) {
					return new MultRatingSurveyPanel(id, (MultipleRatingQuestion) question, context, locale);
				}
				
				@Override
				public Component createReportPanel(String id, Locale locale, Question question, FilterFunctorCallback filterFunctorCallback) {
					return new MultipleRatingReportPanel(id, locale, (MultipleRatingQuestion) question, filterFunctorCallback);
				}

				public Question createQuestion(String type) {
					MultipleRatingQuestion question = new MultipleRatingQuestion();
					question.setNumberOfRatingSteps(5);
					question.setAntipodePair(new AntipodePair());
					return question;
				}
			}
		);
		
		register(
			new QuestionPanelCreatorImpl(DimensionsQuestion.TYPE) {

				public Component createConfigurationPanel(
						String id, String questionType, Integer questionId, 
						int configurationId, Locale studyLocale, EventHandler callback,
						ChangeTabsCallback changeTabsCallback) {
					return new DimensionsQuestionConfUI(id, configurationId, questionId, studyLocale, callback, changeTabsCallback);	           
				}

				public SubmitCallbackPanel createSurveyPanel(String id, Question question, QuestionnaireSurveyContext context, Locale locale) {
					return new DimensionsQuestionSurveyPanel(id, (DimensionsQuestion) question, context, locale);
				}
				
				@Override
				public Component createReportPanel(String id, Locale locale, Question question, FilterFunctorCallback filterFunctorCallback) {
					return new DimensionsReportPanel(id,  locale, (DimensionsQuestion) question, filterFunctorCallback);
				}

				public Question createQuestion(String type) {
					DimensionsQuestion dimensionsQuestion = new DimensionsQuestion();
					dimensionsQuestion.setNumberOfRatingSteps(5);
					dimensionsQuestion.setRandomizeAntipodePosition(false);
					return dimensionsQuestion;
				}
			}
		);

	}
	
	public List<String> getQuestionTypes() {
		List<String> rv = new ArrayList<String>();
		for (QuestionPanelCreator creator : questionPanelCreators) {
			rv.add(creator.getType());
		}
		return rv;
	}
	
	public Component getQuestionConfigurationComponent(
			String id, String questionType, 
			int configurationId, Locale studyLocale, 
			EventHandler callback,
			ChangeTabsCallback changeTabsCallback) 
	{
		return getConfComponent(id, questionType, null, configurationId, studyLocale, callback, changeTabsCallback);
	}
	
	public Component getQuestionConfigurationComponent(
			String id, String questionType, 
			final Integer questionId, int configurationId, 
			Locale studyLocale, EventHandler callback,
			ChangeTabsCallback changeTabsCallback) 
	{		
		return getConfComponent(id, questionType, questionId, configurationId, studyLocale, callback, changeTabsCallback);
	}
	
	private Component getConfComponent(
			String id, String questionType, 
			Integer questionId, int configurationId, 
			Locale studyLocale, EventHandler callback,
			final ChangeTabsCallback changeTabsCallback) 
	{
		for (QuestionPanelCreator creator : questionPanelCreators) {
			if (creator.appliesForType(questionType)) {
				return creator.createConfigurationPanel(id, questionType, questionId, configurationId, studyLocale, callback, changeTabsCallback);
			}
		}
		return null;
	}
	
	public SubmitCallbackPanel getSurveyComponent(
		String id, Question question, QuestionnaireSurveyContext context, Locale locale) 
	{
		for (QuestionPanelCreator creator : questionPanelCreators) {
			if (creator.appliesForType(question.getType())) {
				return creator.createSurveyPanel(id, question, context, locale);
			}
		}
		return null;	
	}
	
	public Question createQuestion(String type) {
		for (QuestionPanelCreator creator : questionPanelCreators) {
			if (creator.appliesForType(type)) {
				return creator.createQuestion(type);
			}
		}
		return null;	
	}
	
	public Component getReportComponent(String id, Locale locale, Question question, FilterFunctorCallback filterFunctorCallback) {
		for (QuestionPanelCreator creator : questionPanelCreators) {
			if (creator.appliesForType(question.getType())) {
				return creator.createReportPanel(id, locale, question, filterFunctorCallback);
			}
		}
		return null;	
	}

}
