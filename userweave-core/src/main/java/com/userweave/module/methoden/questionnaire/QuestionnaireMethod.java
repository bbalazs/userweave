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
package com.userweave.module.methoden.questionnaire;

import java.util.Locale;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.panel.Panel;
import org.springframework.beans.factory.annotation.Autowired;

import com.userweave.application.OnFinishCallback;
import com.userweave.components.callback.EventHandler;
import com.userweave.components.navigation.breadcrumb.StateChangeTrigger;
import com.userweave.dao.StudyDependendDao;
import com.userweave.dao.TestResultDao;
import com.userweave.module.AbstractModule;
import com.userweave.module.ModuleConfiguration;
import com.userweave.module.methoden.questionnaire.dao.QuestionnaireConfigurationDao;
import com.userweave.module.methoden.questionnaire.dao.QuestionnaireResultDao;
import com.userweave.module.methoden.questionnaire.domain.QuestionnaireConfigurationEntity;
import com.userweave.module.methoden.questionnaire.page.QuestionConfigurationPanelFactory;
import com.userweave.module.methoden.questionnaire.page.conf.QuestionnaireConfigurationUI;
import com.userweave.module.methoden.questionnaire.page.grouping.QuestionnaireGroupingPanel;
import com.userweave.module.methoden.questionnaire.page.survey.QuestionnaireSurveyUI;
import com.userweave.pages.components.slidableajaxtabpanel.ChangeTabsCallback;
import com.userweave.pages.test.SurveyUI;

public class QuestionnaireMethod extends AbstractModule<QuestionnaireConfigurationEntity> {

	public QuestionnaireMethod() {
		init("Questionnaire", "some Questions...");
	}
	
	public static final String moduleId = "questionnaireMethod";
	
	@Override
	public String getModuleId() {
		return moduleId;
	}
	
	@Autowired
	private QuestionnaireConfigurationDao configurationDao;
	
	@Override
	protected StudyDependendDao<QuestionnaireConfigurationEntity> getConfigurationDao() {
		return configurationDao;
	}
	
	@Autowired
	private QuestionnaireResultDao resultDao;

	@Override
	protected TestResultDao getTestResultDao() {
		return resultDao;
	}
	
	public SurveyUI getTestUI(String id, int surveyExecutionId, QuestionnaireConfigurationEntity configuration, OnFinishCallback onFinishCallback,
			Locale locale) {
		return new QuestionnaireSurveyUI(id, configuration.getId(), surveyExecutionId, onFinishCallback, locale);
	}

	@Override
	public Panel getConfigurationUI(
			String id, 
			QuestionnaireConfigurationEntity configuration, 
			StateChangeTrigger trigger,
			ChangeTabsCallback callback,
			EventHandler addFilterCallback)
	{
		return new QuestionnaireConfigurationUI(
			id, configuration.getId(), trigger, callback, addFilterCallback); 
	}

	/**
	 * Get configuration ui for specific question.
	 * 
	 * @param id
	 * @param questionType
	 * @param questionId
	 * @param configuration
	 * @param callback
	 * @return
	 */
	public Component getConfigurationUI(
			String id, 
			String questionType,
			int questionId,
			ModuleConfiguration configuration, 
			EventHandler callback,
			final ChangeTabsCallback changeTabsCallback)
	{
		// show configuration for single question 
		return new QuestionConfigurationPanelFactory()
			.getQuestionConfigurationComponent(
				id, 
				questionType, 
				questionId, 
				configuration.getId(), 
				configuration.getStudy().getLocale(),
				callback,
				changeTabsCallback
			);
	}
		
//	@Override
//	public Component getReportUI(
//			String id, QuestionnaireConfigurationEntity configuration, FilterFunctorCallback callback) {
//		return new QuestionnaireReportPanel(id, configuration, callback);
//	}

	@Override
	public Component getGroupingUI(String id, QuestionnaireConfigurationEntity configuration) {
		return new QuestionnaireGroupingPanel(id, configuration);
	}
	
	@Override
	public boolean hasGroupingUI() {
		return true;
	}
	
	@Override
	protected QuestionnaireConfigurationEntity createConfiguration() {
		return new QuestionnaireConfigurationEntity();
	}

}