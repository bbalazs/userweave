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
package com.userweave.pages.components.studypanel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.application.UserWeaveSession;
import com.userweave.components.callback.EventHandler;
import com.userweave.components.callback.IEntityEvent;
import com.userweave.components.navigation.breadcrumb.StateChangeTrigger;
import com.userweave.dao.EmptyFilterFunctor;
import com.userweave.dao.filter.CompoundFilterFunctor;
import com.userweave.dao.filter.FilterFunctor;
import com.userweave.dao.filter.ModuleReachedFilterFunctor;
import com.userweave.dao.filter.StudyLocalesFilterFunctor;
import com.userweave.domain.Group;
import com.userweave.domain.ModuleReachedGroup;
import com.userweave.domain.Study;
import com.userweave.domain.StudyLocalesGroup;
import com.userweave.domain.StudyState;
import com.userweave.domain.service.ModuleService;
import com.userweave.domain.service.StudyService;
import com.userweave.module.Module;
import com.userweave.module.ModuleConfiguration;
import com.userweave.pages.components.slidableajaxtabpanel.ChangeTabsCallback;
import com.userweave.pages.components.slidableajaxtabpanel.SlidableTabbedPanel;
import com.userweave.pages.components.slidableajaxtabpanel.StudiesSlidableAjaxTabbedPanel;
import com.userweave.pages.configuration.base.ConfigurationReportPanel;
import com.userweave.pages.configuration.base.StudyConfigurationReportPanel;
import com.userweave.pages.configuration.editentitypanel.BaseFunctionEditEntityPanel;
import com.userweave.pages.configuration.editentitypanel.EditStudyEntityPanel;
import com.userweave.pages.configuration.report.FilterFunctorCallback;
import com.userweave.pages.configuration.report.StudyFilteredReportPanel;
import com.userweave.pages.test.DisplaySurveyUI;

/**
 * Panel to configure and evaluate a study.
 * 
 * @author opr
 */
public class StudyPanel extends StudyConfigurationReportPanel
{
	private static final long serialVersionUID = 1L;
	
	@SpringBean
	private ModuleService moduleService;
	
	@SpringBean
	private StudyService studyService;
	
	/**
	 * Tab Panel to hold the methods of this study.
	 */
	private SlidableTabbedPanel tabbedPanel;
	
	/**
	 * Object to signal the change between the report
	 * ui and config ui.
	 */
	private final StateChangeTrigger trigger;
	
	/**
	 * Default constructor.
	 * 
	 * @param id
	 * 		Markup id of component.
	 * @param studyId
	 * 		Id of study to configure/evaluate.
	 * @param callback
	 * 		Callback to fire on on event.
	 */
	public StudyPanel(String id, final int studyId, EventHandler callback)
	{
		super(id, studyId);
		
		UiState state = 
			getStudy().getState() == StudyState.INIT ? 
				UiState.CONFIG :
				UiState.REPORT;
		
		trigger = new StateChangeTrigger(state);
		
		trigger.register(this);
		
		initView(trigger, callback);
		
		initTabPanel(trigger, callback);
		
		// disabled and moved to 4p.1
		//addUrlToSurvey();
	}
	
	private void addUrlToSurvey()
	{
		PageParameters parameters = new PageParameters();
		
		if(studyService.isAtLeastOneAdminRegistered(getStudy().getParentProject()))
		{
			parameters.set(0, getStudy().getHashCode());
		}
		
		String url =  RequestCycle.get().getUrlRenderer().renderFullUrl(
				Url.parse(urlFor(DisplaySurveyUI.class,null).toString()));

		
		Label link = new Label("url", new Model<String>(url));
		
		WebMarkupContainer container = 
			new WebMarkupContainer("urlContainer");
		
		container.setVisible(getStudy().getState() == StudyState.RUNNING);
		
		container.add(link);
		
		add(container);
	}
	
	/**
	 * Initializes the tab panel on this component.
	 * 
	 * @param stateChangeTrigger
	 * 		Trigger to signal state change.
	 */
	private void initTabPanel(
			final StateChangeTrigger stateChangeTrigger, 
			final EventHandler callback)
	{
		tabbedPanel = getTabbedPanel(callback, stateChangeTrigger, null);
				
		add(tabbedPanel);
	}
	
	/**
	 * Creates a new Tabbed Panel for studies.
	 * 
	 * @param callback
	 * @param stateChangeTrigger
	 * @return
	 */
	private StudiesSlidableAjaxTabbedPanel getTabbedPanel(
			final EventHandler callback,
			final StateChangeTrigger stateChangeTrigger, 
			Integer preSelectedTab)
	{
		StudiesSlidableAjaxTabbedPanel newTabbedPanel = 
			new StudiesSlidableAjaxTabbedPanel(
				"tabs", 
				getTabs(getStudy(), callback, stateChangeTrigger),  
				stateChangeTrigger,
				getChangeTabsCallback(callback, stateChangeTrigger))
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected Study getStudy()
			{
				return StudyPanel.this.getStudy();
			}
			
			@Override
			protected Component getResultTabPanel(String id)
			{
				return new StudyFilteredReportPanel(
						id, StudyPanel.this.getDefaultModel(), 
						callback, getAddFilterCallback(stateChangeTrigger),
						getCallback());
			}
			
			@Override
			protected String getTabContainerCssClass()
			{
				return "studyTabs tab-row";
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
	 * Creates a list of tabs to display. Each tab is a wrapper
	 * for a specific module.
	 * 
	 * @param study
	 * 		Study to load modules from
	 * @param callback
	 * 		Callback to fire on event.
	 * @return
	 * 		List of ITabs.
	 */
	@SuppressWarnings("rawtypes")
	private List<ITab> getTabs(
			final Study study, 
			final EventHandler callback,
			final StateChangeTrigger stateChangeTrigger)
	{
		stateChangeTrigger.setFilterFunctorCallback(getCallback());
		
		List<ITab> tabs = new ArrayList<ITab>();
		
		List<ModuleConfiguration> modules = 
			moduleService.getModuleConfigurationsForStudy(getStudy());
		
		for(final ModuleConfiguration<?> module : modules)
		{	
			tabs.add(new AbstractTab(getTabModel(study, module))
			{
				private static final long serialVersionUID = 1L;

				@Override
				public Panel getPanel(String panelId)
				{	
					ModuleConfiguration conf = 
						moduleService.getModuleConfigurationForStudy(study, module.getId());
					
					// return an error panel, if module has been deleted.
					if(conf == null)
					{
						return new ModuleDeletedErrorPanel(panelId);
					}
					
					@SuppressWarnings("unchecked")
					Module<ModuleConfiguration> modConf = conf.getModule();
					
					return modConf.getConfigurationUI(
						panelId, 
						module, 
						stateChangeTrigger, 
						getChangeTabsCallback(callback, stateChangeTrigger),
						getAddFilterCallback(stateChangeTrigger));
				}
			});
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
	private IModel getTabModel(final Study study, final ModuleConfiguration<?> module)
	{
		IModel tabModel;
		
		if(study.getState() == StudyState.FINISHED || 
		   study.getState() == StudyState.RUNNING)
		{
			tabModel = new Model(module.getName());
		}
		else
		{
			tabModel = new LoadableDetachableModel()
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected Object load()
				{
					ModuleConfiguration conf = 
						moduleService.getModuleConfigurationForStudy(study, module.getId());
					
					// Create error tab, if another user has
					// deleted the configutation
					if(conf == null)
					{
						return new StringResourceModel(
								"error", 
								StudyPanel.this, 
								null).getString();
					}
					
					return conf.getName();
				}	
			};
		}
		return tabModel;
	}
	
	private ChangeTabsCallback getChangeTabsCallback(
			final EventHandler callback, final StateChangeTrigger trigger)
	{
		return new ChangeTabsCallback()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public int getSizeOfTabList()
			{
				return tabbedPanel.getTabs().size();
			}
			
			@Override
			public void fireChange(AjaxRequestTarget target, Integer preSelectedTab)
			{
				replaceTabbedPanel(target, callback, trigger, preSelectedTab);
			}
			
			@Override
			public void fireAppend(AjaxRequestTarget target)
			{	
				target.add(tabbedPanel);
				
				hideSlidableArrows(target);
			}
		};
	}
	
	/**
	 * Hides the slidable arrows on the slidable tabbed panels.
	 * @param target
	 */
	private void hideSlidableArrows(AjaxRequestTarget target)
	{
		// hide slidable arrows
		target.appendJavaScript("computeTabWidth('.studyTabs');");
		target.appendJavaScript("computeTabWidth('.questionTabs');");
	}
	
	/**
	 * Method to replace the tabbed panel with a fresh
	 * one.
	 * 
	 * @param target
	 * 		AjaxRequestTarget
	 */
	private void replaceTabbedPanel(
			AjaxRequestTarget target, 
			final EventHandler callback,
			final StateChangeTrigger stateChangeTrigger,
			final Integer preSelectedTab)
	{
		StudiesSlidableAjaxTabbedPanel replacement = 
			getTabbedPanel(callback, stateChangeTrigger, preSelectedTab);
			
		tabbedPanel.replaceWith(replacement);
		tabbedPanel = replacement;
		
		target.add(tabbedPanel);
	}
	
	/**
	 * Callback to delegate filters to the method
	 * statistics.
	 * 
	 * @return
	 */
	private FilterFunctorCallback getCallback()
	{
		return new FilterFunctorCallback()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public FilterFunctor getFilterFunctor()
			{
				return StudyPanel.this.getFilterFunctor();
			}
		};
	}
	
	/**
	 * Creates the overall filter rule for
	 * method statistics.
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	protected FilterFunctor getFilterFunctor() 
	{
		List<Group> selectedGroups = getSelectedGroups();
		
		if (selectedGroups != null && !selectedGroups.isEmpty()) 
		{
			CompoundFilterFunctor compoundFilterFunctor = new CompoundFilterFunctor();
			
			List<ModuleConfiguration> activeModuleConfigurations = 
				getStudy().getActiveModuleConfigurations(moduleService.getModules());

			for(Group group : selectedGroups) 
			{
				if(group instanceof StudyLocalesGroup) 
				{
					compoundFilterFunctor.addFilterFunctor(new StudyLocalesFilterFunctor((StudyLocalesGroup) group));
				} 
				else if (group instanceof ModuleReachedGroup) 
				{
					compoundFilterFunctor.addFilterFunctor(new ModuleReachedFilterFunctor((ModuleReachedGroup) group));
				} 
			}
			// collect filter-functors for groups from module-configurations 
			for (ModuleConfiguration moduleConfiguration : activeModuleConfigurations) 
			{
				List groups = moduleConfiguration.getGroups();
				
				for (Group group : selectedGroups) 
				{
					if (group != null) 
					{					
						if (groups.contains(group)) 
						{
							compoundFilterFunctor.addFilterFunctor(moduleConfiguration.getFilterFunctorForGroup(group));
						}
					}
				}
			}
			
			return compoundFilterFunctor;
		} 
		else 
		{
			return new EmptyFilterFunctor();
		}	
	}
	
	/**
	 * Selected filter.
	 * 
	 * @return
	 */
	private List<Group> getSelectedGroups() 
	{
		Component component = getStateDependComponent();
		
		if(component instanceof StudyFilterPanel)
		{
			return ((StudyFilterPanel) component).getSelectedGroups();
		}
		
		return new ArrayList<Group>(0);
	}
	
	@Override
	protected BaseFunctionEditEntityPanel getActionComponent(String id, UiState state, EventHandler callback)
	{
		return new EditStudyEntityPanel(id, (IModel<Study>)getDefaultModel(), callback, trigger)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onFilterWindowClose(AjaxRequestTarget target)
			{
				// update all filter list
				StudyFilterPanel component = (StudyFilterPanel) getStateDependComponent();

				component.triggerModelChange(target);
				
				// reset selected filters
				List<Group> list = Collections.emptyList();
				
				((UserWeaveSession) Session.get()).setSelectedGroups(getStudy().getId(), list);
				
				replaceStudyFilterPanel(target, trigger);
				
				// hide arrows
				hideSlidableArrows(target);
				
				onFilter(target, trigger);
			}
		};
	}

	@Override
	protected Component getConfigurationComponent(String id)
	{
		return new StudyDescriptionConfigPanel(id, getStudy().getId());
	}

	@Override
	protected Component getReportComponent(String id, final StateChangeTrigger trigger)
	{
		if(getStudy().getState() == StudyState.RUNNING)
		{
			return getConfigurationComponent(id);
		}
		
		return new StudyFilterPanel(id, getDefaultModel())
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onFilter(AjaxRequestTarget target)
			{
				StudyPanel.this.onFilter(target, trigger);
			}
		};
	}

	/**
	 * Callback to fire, if a new filter has to be added to
	 * the filter list.
	 * 
	 * @return
	 */
	private EventHandler getAddFilterCallback(final StateChangeTrigger trigger)
	{
		return new EventHandler()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean onEvent(IEntityEvent event)
			{
				hideSlidableArrows(event.getTarget());
				return replaceStudyFilterPanel(event.getTarget(), trigger);
			}
		};
	}
	
	private boolean replaceStudyFilterPanel(AjaxRequestTarget target, final StateChangeTrigger trigger)
	{
		Component filterPanel = getStateDependComponent();
		
		if(filterPanel != null && filterPanel instanceof StudyFilterPanel)
		{
			StudyFilterPanel replacement = new StudyFilterPanel(
					STATE_DEPEND_COMPONENT_ID, getDefaultModel())
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onFilter(AjaxRequestTarget target)
				{
					StudyPanel.this.onFilter(target, trigger);
				}
			};
			
			replacement.setOutputMarkupId(true);
			
			filterPanel.replaceWith(replacement);
			setStateDependComponent(replacement);
			
			target.add(this);
			target.add(getStateDependComponent());
			
			return true;
		}
		
		return false;
	}
	
	@Override
	public void onFilter(AjaxRequestTarget target, StateChangeTrigger trigger)
	{
		// delegate on filter event to selected tab		
		Component panel = tabbedPanel.get(TabbedPanel.TAB_PANEL_ID);
		
		if(panel != null)
		{
			if(panel instanceof StudyFilteredReportPanel)
			{	
				// selected tab is configuration tab
				((StudyFilteredReportPanel) panel).onFilter(target);
			}
			else if(panel instanceof ConfigurationReportPanel)
			{
				// selected tab is method tab
				((ConfigurationReportPanel) panel).onFilter(target, trigger);
			}
		}
	}
}
