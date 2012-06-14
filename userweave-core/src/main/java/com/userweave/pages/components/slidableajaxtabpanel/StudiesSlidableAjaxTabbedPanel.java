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
package com.userweave.pages.components.slidableajaxtabpanel;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.LoopItem;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.components.callback.EventHandler;
import com.userweave.components.navigation.breadcrumb.StateChangeTrigger;
import com.userweave.domain.Role;
import com.userweave.domain.Study;
import com.userweave.domain.StudyState;
import com.userweave.domain.service.ModuleService;
import com.userweave.pages.components.slidableajaxtabpanel.addmethodpanel.AddMethodPanel;
import com.userweave.pages.configuration.base.IConfigReportStateChanger;

/**
 * Slidable panel which holds the first and last module
 * of a study on a constant place (before and after the 
 * second and last but one tab).
 * 
 * @author opr
 *
 */
public abstract class StudiesSlidableAjaxTabbedPanel 
	extends SlidableAjaxTabbedPanelWithAddTab
{
	private static final long serialVersionUID = 1L;
	
	@AuthorizeAction(action = Action.RENDER, 
					 roles = {Role.PROJECT_ADMIN, Role.PROJECT_PARTICIPANT})
	private abstract class AuthOnlyLink extends AjaxLink
	{
		private static final long serialVersionUID = 1L;

		public AuthOnlyLink(String id)
		{
			super(id);
		}
	}
	
	private boolean isTabPanelActive = true;
	
	@SpringBean
	private ModuleService moduleService;
	
	/**
	 * Tab to either display the configuration
	 * or report statistics.
	 */
	private AjaxLink configTab;
	
	private final AttributeAppender configTabSelected = 
		new AttributeAppender("class", true, new Model("configTabSelected"), " ");
	
	/**
	 * Markup id of above configTab.
	 */
	private static final String CONFIG_TAB_ID = "configTab";
	
	/**
	 * Constructor to display a fixed number of tabs.
	 * 
	 * @param id
	 * 		Markup id.
	 * @param tabs
	 * 		List of tabs to display.
	 * @param trigger
	 * 		Trigger object to change this view to
	 * 		another ui state.
	 */
	public StudiesSlidableAjaxTabbedPanel(
			String id, List<ITab> tabs, StateChangeTrigger trigger, ChangeTabsCallback callback)
	{
		super(id, tabs, trigger, callback);
		
		init(trigger);
	}

	/**
	 * Initialize this panel with a special tab.
	 * 
	 * @param trigger
	 * 		The state change trigger for the config
	 * 		and result view.
	 */
	private void init(StateChangeTrigger trigger)
	{
		if(trigger.getState() == UiState.CONFIG)
		{
			addConfigTab();
		}
		else
		{
			addResultTab();
			
			if(getStudy().getState() == StudyState.RUNNING)
			{
				displayResultTab();
			}
		}
		
		setOutputMarkupId(true);
		
		trigger.register(this);
	}
	
	/**
	 * Creates a configuration tab outside of the
	 * normal tab panel. Its visibility is determined
	 * by the study state.
	 */
	protected void addConfigTab()
	{
		addToTabbedPanel(configTab = getConfigLink());
		
		configTab.setOutputMarkupId(true);
	}
	
	@Override
	protected LoopItem newTabContainer(final int tabIndex)
	{
		return new LoopItem(tabIndex)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onComponentTag(ComponentTag tag)
			{
				super.onComponentTag(tag);
				String cssClass = tag.getAttribute("class");
				if (cssClass == null)
				{
					cssClass = " ";
				}
				cssClass += " tab" + getIndex();

				if (getIndex() == getSelectedTab() && isTabPanelActive)
				{
					cssClass += " selected";
				}
				if (getIndex() == getTabs().size() - 1)
				{
					cssClass += " last";
				}
				tag.put("class", cssClass.trim());
				
				if(whichTabbedPanel.equals(STUDY_TABBED_PANEL))
				{
					tag.put("style", "z-index:" + (getTabs().size() - tabIndex) + 
									 ";left:-" + (tabIndex * 9) + "px;");
			
				}
			}

		};
	}
	
	/**
	 * Creates an ajax link to show an
	 * extra tab independent from the
	 * tabbed panel to display configuration.
	 * 
	 * @return
	 */
	protected AjaxLink getConfigLink()
	{
		AjaxLink link = new AjaxLink(CONFIG_TAB_ID)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				if (StudiesSlidableAjaxTabbedPanel.this.get(TAB_PANEL_ID) == null)
				{
					StudiesSlidableAjaxTabbedPanel.this.add(getConfigTabPanel(TAB_PANEL_ID));
				}
				else
				{
					StudiesSlidableAjaxTabbedPanel.this.replace(getConfigTabPanel(TAB_PANEL_ID));
				}
				
				isTabPanelActive = false;
				
				this.add(configTabSelected);
				
				target.addComponent(this);
				target.addComponent(StudiesSlidableAjaxTabbedPanel.this);
				
				target.appendJavaScript(getTriggerScripts());
			}
		};
		
		link.add(
			new Label(
				"configTabLabel", 
				new StringResourceModel(
						"config_label", 
						StudiesSlidableAjaxTabbedPanel.this,
						null)));
		
		link.setOutputMarkupId(true);
		
		return link;
	}
	
	/**
	 * If study is not in init state, replace
	 * the config tab with a result tab.
	 */
	protected void addResultTab()
	{
		addToTabbedPanel(configTab = getResultLink());
		
		configTab.setOutputMarkupId(true);
	}
	
	/**
	 * Creates an ajax link to show an
	 * extra tab independent from the
	 * tabbed panel to display results.
	 * 
	 * @return
	 */
	protected AjaxLink getResultLink()
	{
		AjaxLink link = new AjaxLink(CONFIG_TAB_ID)
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick(AjaxRequestTarget target)
			{	
				displayResultTab();
				
				target.addComponent(this);
				target.addComponent(StudiesSlidableAjaxTabbedPanel.this);
				
				target.appendJavaScript(getTriggerScripts());
			}	
		};
		
		link.add(
				new Label(
					"configTabLabel", 
					new StringResourceModel(
							"result_label", 
							StudiesSlidableAjaxTabbedPanel.this,
							null)));
			
		return link;
	}
	
	/**
	 * Displays the result tab instead of the normal tabbed
	 * panel content.
	 */
	private void displayResultTab()
	{
		isTabPanelActive = false;
		
		configTab.add(configTabSelected);
		
		setSelectedTab(0);
		
		if (StudiesSlidableAjaxTabbedPanel.this.get(TAB_PANEL_ID) == null)
		{
			StudiesSlidableAjaxTabbedPanel.this.add(getResultTabPanel(TAB_PANEL_ID));
		}
		else
		{
			StudiesSlidableAjaxTabbedPanel.this.replace(getResultTabPanel(TAB_PANEL_ID));
		}
	}
	
	/**
	 * Returns a configuration panel for the config tab.
	 * 
	 * @param id
	 * 		Markup id.
	 * @return
	 */
	protected Component getConfigTabPanel(String id)
	{
		return new StudyConfigurationWrapperPanel(id, getStudy().getId());
	}
	
	@Override
	protected boolean isAddTabActive()
	{
		return getStudy().getState() == StudyState.INIT;
	}
	
	@Override
	public void onStateChange(UiState state, AjaxRequestTarget target, EventHandler callback, StateChangeTrigger trigger)
	{
		AjaxLink replacement;
		
		if(state == UiState.CONFIG)
		{
			replacement = getConfigLink();
		}
		else
		{
			replacement = getResultLink();
		}
		
		configTab.replaceWith(replacement);
		configTab = replacement;
		
		target.addComponent(configTab);
		
		Panel currentPanel = 
			(Panel) StudiesSlidableAjaxTabbedPanel.this.get(TAB_PANEL_ID);
		
		// check, if we are in config/result view. These panels
		// aren't instances of IConfigReportStateChanger
		if(currentPanel instanceof IConfigReportStateChanger)
		{
			super.onStateChange(state, target, callback, trigger);
		}
		else
		{
			if(state == UiState.CONFIG)
			{
				StudiesSlidableAjaxTabbedPanel.this.replace(getConfigTabPanel(TAB_PANEL_ID));
			}
			else
			{
				StudiesSlidableAjaxTabbedPanel.this.replace(getResultTabPanel(TAB_PANEL_ID));
			}
			
			replacement.add(configTabSelected);
			
			target.addComponent(StudiesSlidableAjaxTabbedPanel.this);
		}
		
	}
	
	@Override
	public Panel getAddTabPanel(String componentId, ChangeTabsCallback callback)
	{
		// workaround for serialization
		IModel choices = new LoadableDetachableModel()
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			protected Object load()
			{
				return moduleService.getActiveModules();
			}
		};
		
		return new AddMethodPanel(
				componentId, getStudy().getId(), choices, callback);
			
	}
	
	@Override
	protected void onAjaxUpdate(AjaxRequestTarget target)
	{
		if(configTab.getBehaviors().contains(configTabSelected))
		{
			configTab.remove(configTabSelected);
		}
		
		isTabPanelActive = true;
		
		super.onAjaxUpdate(target);
	}
	
	@Override
	protected boolean addAddTabAtLastPosition()
	{
		return false;
	}
	
	/**
	 * Get the study for this tab panel.
	 * 
	 * @return
	 * 		A Study.
	 */
	protected abstract Study getStudy();
	
	/**
	 * Returns a panel for the result tab.
	 * 
	 * @param id
	 * 		Markup id.
	 * @return
	 */
	protected abstract Component getResultTabPanel(String id);
}
