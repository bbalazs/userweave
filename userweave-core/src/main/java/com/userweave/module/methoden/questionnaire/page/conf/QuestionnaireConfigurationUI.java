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
package com.userweave.module.methoden.questionnaire.page.conf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.components.callback.EventHandler;
import com.userweave.components.navigation.breadcrumb.StateChangeTrigger;
import com.userweave.dao.BaseDao;
import com.userweave.dao.StudyDependendDao;
import com.userweave.domain.Study;
import com.userweave.domain.StudyState;
import com.userweave.module.methoden.questionnaire.dao.QuestionDao;
import com.userweave.module.methoden.questionnaire.dao.QuestionnaireConfigurationDao;
import com.userweave.module.methoden.questionnaire.domain.QuestionnaireConfigurationEntity;
import com.userweave.module.methoden.questionnaire.domain.question.Question;
import com.userweave.module.methoden.questionnaire.page.QuestionConfigurationPanelFactory;
import com.userweave.module.methoden.questionnaire.page.report.QuestionnaireReportPanel;
import com.userweave.pages.components.slidableajaxtabpanel.ChangeTabsCallback;
import com.userweave.pages.components.slidableajaxtabpanel.SlidableAjaxTabbedPanelWithAddTab;
import com.userweave.pages.components.slidableajaxtabpanel.SlidableTabbedPanel;
import com.userweave.pages.configuration.base.ModuleConfigurationReportPanel;
import com.userweave.pages.configuration.module.ModuleConfigurationBaseUI;
import com.userweave.pages.configuration.question.QuestionConfigurationPanel;

/**
 * Configuration and report UI for questionnaire method.
 * Displays a list of questions as a slidable tabbed panel.
 * 
 * @author opr
 */
public class QuestionnaireConfigurationUI  
	extends ModuleConfigurationReportPanel<QuestionnaireConfigurationEntity>
{
	private static final long serialVersionUID = 1L;
	
	@SpringBean
	private QuestionnaireConfigurationDao configurationDao;
	
	@SpringBean
	private QuestionDao questionDao;
	
	@Override
	protected StudyDependendDao<QuestionnaireConfigurationEntity> getBaseDao() 
	{		
		return configurationDao;
	}
	
	@Override
	protected StudyDependendDao<QuestionnaireConfigurationEntity> getConfigurationDao() 
	{
		return configurationDao;
	}
	
	/**
	 * Tabbed panel to display a list of questions.
	 */
	private SlidableAjaxTabbedPanelWithAddTab tabPanel;
	
	/**
	 * Event Handler to listen to an filter add event.
	 */
	private final EventHandler addFilterCallback;
	
	/**
	 * Default constructor.
	 * 
	 * @param id
	 * 		Markup id of component.
	 * @param configurationId
	 * 		Configuration entity.
	 * @param trigger
	 * 		Publischer for the ui state change event.
	 * @param callback
	 * 		Callback to fire, if a question on the tabbed panel 
	 * 		is selected.
	 * @param addFilterCallback
	 * 		Listener for the add filter event.
	 */
	public QuestionnaireConfigurationUI(
			String id, 
			final Integer configurationId, 
			final StateChangeTrigger trigger,
			ChangeTabsCallback callback,
			EventHandler addFilterCallback)
	{
		super(id,  trigger, configurationId, callback);
		
		this.addFilterCallback = addFilterCallback;
		
		
		setOutputMarkupId(true);
		
		
		tabPanel = getTabbedPanel(trigger, null);
		
		tabPanel.setOutputMarkupId(true);
		
		add(tabPanel);
	}

	/**
	 * Factorx method to create a slidable tabbed panel.
	 * 
	 * @param trigger
	 * 		Publischer for the ui state change event.
	 * @param preSelectedTab
	 * 		The tab to preselect.
	 * 
	 * @return
	 * 		A sub class of a slidable tabbed panel
	 */
	private SlidableAjaxTabbedPanelWithAddTab getTabbedPanel(
			final StateChangeTrigger trigger, Integer preSelectedTab)
	{		
		SlidableAjaxTabbedPanelWithAddTab newTabbedPanel = 
			new SlidableAjaxTabbedPanelWithAddTab(
				"questions", 
				getQuestionTabs(trigger), 
				trigger, 
				getChangeTabsCallback(trigger),
				SlidableTabbedPanel.QUESTION_TABBED_PANEL)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Panel getAddTabPanel(String componentId, ChangeTabsCallback callback)
			{	
				return new AddQuestionPanel(
					componentId, 
					new QuestionConfigurationPanelFactory().getQuestionTypes(),
					callback,
					QuestionnaireConfigurationUI.this.getDefaultModel());
			}

			@Override
			protected boolean isAddTabActive()
			{
				return getConfiguration()
							.getStudy().getState() == StudyState.INIT;
			}
			
			@Override
			protected String getTabContainerCssClass()
			{
				return "questionTabs tab-row";
			}
			
			@Override
			public boolean isVisible()
			{
				boolean studyState = getStudy().getState() != StudyState.RUNNING;
				boolean triggerState = trigger.getState() != UiState.REPORT;
				
				return studyState || triggerState;
			}
		};
		
		if(preSelectedTab != null)
		{
			newTabbedPanel.setSelectedTab(preSelectedTab);
		}
		
		// trigger slidable functions
		AjaxRequestTarget target = AjaxRequestTarget.get();
		
		if(target != null)
		{
			target.appendJavaScript(newTabbedPanel.getTriggerScripts());
		}
		
		return newTabbedPanel;
	}
	
	/**
	 * Factory method to create the ITabs to display in the
	 * above slidable tabbed panel.
	 * 
	 * @param trigger
	 * 		Publischer for the ui state change event.
	 * @return
	 * 		List of ITabs
	 */
	private List<ITab> getQuestionTabs(final StateChangeTrigger trigger)
	{
		List<ITab> tabs = new ArrayList<ITab>();
		
		List<Question> questions = getConfiguration().getQuestions();
		
		// questions are null after moduleCreation
		if(questions != null)
		{
			Collections.sort(questions, new Comparator<Question>()
			{
				@Override
				public int compare(Question o1, Question o2)
				{
					return o1.getPosition().compareTo(o2.getPosition());
				}
			});
			
			for(final Question question : questions)
			{
				tabs.add(new AbstractTab(getTabModel(getStudy(), question))
				{
					private static final long serialVersionUID = 1L;

					@Override
					public Panel getPanel(String panelId)
					{
						return new QuestionConfigurationPanel(
							panelId, 
							getConfiguration(), 
							question,
							getChangeTabsCallback(trigger),
							addFilterCallback,
							trigger
						);
					}
				});
			}
		}
		
		return tabs;
	}
	
	/**
	 * Fix for speedup:
	 * If study is running or finished, a tabbed panel can't be changed
	 * anymore, so the model can be changed to a simple model.
	 *  
	 * @param study
	 * @param module
	 * @return
	 */
	private IModel getTabModel(final Study study, final Question question)
	{
		IModel tabModel;
		
		if(study.getState() == StudyState.FINISHED || 
		   study.getState() == StudyState.RUNNING)
		{
			tabModel = new Model(question.getName().getValue(getLocale()));
		}
		else
		{
			tabModel = new LoadableDetachableModel()
			{
				private static final long serialVersionUID = 1L;
				
				@Override
				protected Object load()
				{
					return questionDao.findById(question.getId()).getName().getValue(getLocale());
				}
			};
		}
		
		return tabModel;
	}
	
	/**
	 * Convinient method to replace the current tabbed
	 * panel with a new one.
	 * 
	 * @param target
	 * 		Ajax request target
	 * @param trigger
	 * 		Publischer for the ui state change event.
	 * @param preSelectedTab
	 * 		The tab to preselect.
	 */
	private void replaceTabbedPanel(
		AjaxRequestTarget target, StateChangeTrigger trigger, Integer preSelectedTab)
	{
		SlidableAjaxTabbedPanelWithAddTab replacement =
			getTabbedPanel(trigger, preSelectedTab);
		
		tabPanel.replaceWith(replacement);
		tabPanel = replacement;
		
		target.addComponent(tabPanel);
	}
	
	@Override
	protected Study getStudy()
	{
		return getEntity().getStudy();
	}

	@Override
	protected Component getConfigurationComponent(String id)
	{
		ModuleConfigurationBaseUI<QuestionnaireConfigurationEntity> baseUi = 
			new ModuleConfigurationBaseUI<QuestionnaireConfigurationEntity>(id, getConfiguration().getId())
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected StudyDependendDao<QuestionnaireConfigurationEntity> getConfigurationDao()
				{
					return configurationDao;
				}

				@Override
				protected BaseDao<QuestionnaireConfigurationEntity> getBaseDao()
				{
					return configurationDao;
				}

				@Override
				protected IModel getTypeModel()
				{
					return new StringResourceModel(
						"questionnaire_type", QuestionnaireConfigurationUI.this, null);
				}
			};
		
		return baseUi;
	}

	@Override
	protected Component getReportComponent(String id, StateChangeTrigger trigger)
	{
		return new QuestionnaireReportPanel(id, getConfiguration(), trigger.getFilterFunctorCallback());
	}
	
	/**
	 * Callback to listen to state changes in the tabbed panel.
	 * 
	 * @param trigger
	 * 		Publischer for the ui state change event.
	 * @return
	 * 		Callback object to execute methods, if an event 
	 * 		in the tabbed panel occurs.
	 */
	private ChangeTabsCallback getChangeTabsCallback(final StateChangeTrigger trigger)
	{
		return new ChangeTabsCallback()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void fireChange(AjaxRequestTarget target, Integer preSelectedTab)
			{
				replaceTabbedPanel(target, trigger, preSelectedTab);
			}

			@Override
			public void fireAppend(AjaxRequestTarget target)
			{				
				target.addComponent(tabPanel.get("tabs-container"));
			}
			
			@Override
			public int getSizeOfTabList()
			{
				return tabPanel.getTabs().size();
			}
		};
	}
	
	@Override
	public void onFilter(AjaxRequestTarget target, StateChangeTrigger trigger)
	{
		super.onFilter(target, trigger);
		
		// delegate on filter event to selected tab		
		Component panel = tabPanel.get(TabbedPanel.TAB_PANEL_ID);
		
		if(panel != null && panel instanceof QuestionConfigurationPanel)
		{
			((QuestionConfigurationPanel) panel).onFilter(target, trigger);
		}
	}
}
