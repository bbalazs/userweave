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
package com.userweave.pages.configuration.question;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.components.callback.EventHandler;
import com.userweave.components.navigation.breadcrumb.StateChangeTrigger;
import com.userweave.dao.StudyDao;
import com.userweave.domain.Study;
import com.userweave.domain.service.ModuleService;
import com.userweave.module.ModuleConfiguration;
import com.userweave.module.methoden.questionnaire.QuestionnaireMethod;
import com.userweave.module.methoden.questionnaire.dao.QuestionDao;
import com.userweave.module.methoden.questionnaire.domain.QuestionnaireConfigurationEntity;
import com.userweave.module.methoden.questionnaire.domain.question.Question;
import com.userweave.module.methoden.questionnaire.page.QuestionConfigurationPanelFactory;
import com.userweave.module.methoden.questionnaire.page.report.QuestionReportPanel;
import com.userweave.pages.components.slidableajaxtabpanel.ChangeTabsCallback;
import com.userweave.pages.configuration.base.IConfigReportStateChanger;
import com.userweave.pages.configuration.editentitypanel.EditQuestionEntityPanel;

public class QuestionConfigurationPanel extends Panel implements IConfigReportStateChanger
{
	private static final long serialVersionUID = 1L;

	@SpringBean
	private ModuleService moduleService;

	@SpringBean
	private StudyDao studyDao;
	
	@SpringBean
	private QuestionDao questionDao;
	
	private final int questionId, moduleConfigurationId, studyId, moduleIndex;
	
	private final String questionType;
	
	private Component content, actions;
	
	private final ChangeTabsCallback changeTabsCallback;
	
	public QuestionConfigurationPanel(
			String id, 
			QuestionnaireConfigurationEntity configuration,
			Question question,
			final ChangeTabsCallback changeTabsCallback,
			final EventHandler addFilterCallback,
			StateChangeTrigger trigger)
	{
		super(id); 
		
		this.changeTabsCallback = changeTabsCallback;
		
		this.questionId = question.getId();
		
		this.moduleConfigurationId = configuration.getId();
		
		this.studyId = configuration.getStudy().getId();
		
		this.moduleIndex = question.getPosition();
		
		this.questionType = question.getType();
		
		add(actions = getActions(addFilterCallback, questionId, trigger, changeTabsCallback));
		
		actions.setOutputMarkupId(true);
		
		add(content = createDefaultModuleConfigurationPanel(
				moduleConfigurationId, studyId, null, 
				changeTabsCallback, moduleIndex, trigger.getState(), trigger));
	
		content.setOutputMarkupId(true);
	}

	private Component createDefaultModuleConfigurationPanel(
			int moduleConfigurationId, 
			int studyId, 
			EventHandler callback,
			final ChangeTabsCallback changeTabsCallback,
			final int moduleIndex, 
			UiState uiState,
			StateChangeTrigger trigger)
	{
		ModuleConfiguration moduleConfiguration = getModuleConfiguration(moduleConfigurationId, studyId);		
		
		QuestionnaireMethod question = (QuestionnaireMethod)moduleConfiguration.getModule();
		
		if(uiState == UiState.CONFIG)
		{
			return question.getConfigurationUI(
				"module", 
				this.questionType, 
				this.questionId,
				moduleConfiguration,
				callback,
				changeTabsCallback);
		}
		else
		{
			return new QuestionConfigurationPanelFactory().getReportComponent(
					"module", 
					moduleConfiguration.getStudy().getLocale(), 
					questionDao.findById(questionId), 
					trigger.getFilterFunctorCallback());
		}
	}
	
	private ModuleConfiguration getModuleConfiguration(int moduleConfigurationId, int studyId) {
		Study study = studyDao.findById(studyId);
		
		ModuleConfiguration moduleConfiguration = moduleService.getModuleConfigurationForStudy(study, moduleConfigurationId);
		return moduleConfiguration;
	}

	@Override
	public void onStateChange(UiState state, AjaxRequestTarget target,
			EventHandler callback, StateChangeTrigger trigger)
	{
		Component replacement = 
				createDefaultModuleConfigurationPanel(
						moduleConfigurationId, studyId, callback, changeTabsCallback, moduleIndex, state, trigger);
		
		content.replaceWith(replacement);
		content = replacement;
		
		content.setOutputMarkupId(true);
		
		target.addComponent(content);
		target.addComponent(actions);
	}
	
	private Component getActions(
		EventHandler addFilterCallback, 
		final int questionId, 
		StateChangeTrigger trigger,
		final ChangeTabsCallback changeTabsCallback)
	{
		IModel<Question> questionModel = new LoadableDetachableModel<Question>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected Question load()
			{
				return questionDao.findById(questionId);
			}
		};
		
		return new EditQuestionEntityPanel(
			"actions", questionModel, trigger, addFilterCallback, changeTabsCallback, moduleIndex)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected boolean moveUpIsEnabled(int moduleIndex)
			{
				return moduleIndex > 0 && 
					   moduleIndex < changeTabsCallback.getSizeOfTabList();
			}

			@Override
			protected boolean moveDownIsEnabled(int moduleIndex)
			{
				return moduleIndex >= 0 && 
					   moduleIndex < changeTabsCallback.getSizeOfTabList() - 2;
			}

			@Override
			protected void moveModuleUp(AjaxRequestTarget target)
			{
				changeTabsCallback.fireChange(target, getEntity().getPosition());
			}

			@Override
			protected void moveModuleDown(AjaxRequestTarget target)
			{
				changeTabsCallback.fireChange(target, getEntity().getPosition());
			}

			@Override
			protected void onEdit(AjaxRequestTarget target)
			{
				changeTabsCallback.fireAppend(target);
			}

			@Override
			protected void onAfterDelete(AjaxRequestTarget target,
					EventHandler callback, Integer positionBeforeDeletion)
			{
				if(positionBeforeDeletion != null)
				{
					int preSelectedTab = 
						positionBeforeDeletion -1 > 0 ? 
							positionBeforeDeletion -1 : 
							0;
					
					changeTabsCallback.fireChange(target, preSelectedTab);
				}
				else
				{
					changeTabsCallback.fireChange(target, null);
				}
			}
			
		};
	}
	
	public void onFilter(AjaxRequestTarget target, StateChangeTrigger trigger)
	{
		if(content instanceof QuestionReportPanel)
		{
			Component replacement = 
				createDefaultModuleConfigurationPanel(
						moduleConfigurationId, studyId, null, 
						changeTabsCallback, moduleIndex, trigger.getState(), trigger);
			
			replacement.setOutputMarkupId(true);
			
			content.replaceWith(replacement);
			content = replacement;
			
			target.addComponent(content);
		}
	}
}
