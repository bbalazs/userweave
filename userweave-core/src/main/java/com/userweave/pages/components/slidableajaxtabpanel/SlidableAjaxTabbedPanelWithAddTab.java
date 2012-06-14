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
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.userweave.application.UserWeaveSession;
import com.userweave.components.callback.EventHandler;
import com.userweave.components.navigation.breadcrumb.StateChangeTrigger;
import com.userweave.domain.Role;
import com.userweave.pages.configuration.base.IConfigReportStateChanger;

/**
 * Slidable tab panel with an extra add tab.
 * Used to generate a panel for adding new
 * tabs to the underlying tab list.
 * 
 * @author opr
 *
 */
public abstract class SlidableAjaxTabbedPanelWithAddTab
	extends SlidableTabbedPanel implements IConfigReportStateChanger
{
	private static final long serialVersionUID = 1L;

	private abstract class AddLink extends AjaxFallbackLink
	{
		private static final long serialVersionUID = 1L;

		public AddLink(String id) 
		{
			super(id);
		}
	}
	
	public SlidableAjaxTabbedPanelWithAddTab(
			String id, List<ITab> tabs, StateChangeTrigger trigger, ChangeTabsCallback callback)
	{
		super(id, tabs);
		
		init(trigger, callback);
	}
	
	public SlidableAjaxTabbedPanelWithAddTab(
			String id, 
			List<ITab> tabs, 
			StateChangeTrigger trigger, 
			ChangeTabsCallback callback,
			String whichTabbedPanel)
	{
		super(id, tabs, whichTabbedPanel);
		
		init(trigger, callback);
	}
	
	/**
	 * Initialize the add tab.
	 */
	private void init(StateChangeTrigger trigger, ChangeTabsCallback callback)
	{
		trigger.register(this);
		
		addAddTab(getTabs().size(), callback);
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	protected void addAddTab(int numberOfTabs, final ChangeTabsCallback callback)
	{
		int position = numberOfTabs > 0 ? numberOfTabs : 0;
		
		if(!addAddTabAtLastPosition())
		{
			position = position > 0 ? position - 1 : 0;
		}
		
		List tabs = getTabs();
		
		tabs.add(position, createAddTab(callback));
	}
	
	private ITab createAddTab(final ChangeTabsCallback callback)
	{
		return new AddTab()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Panel getPanel(final String panelId)
			{
				return getAddTabPanel(panelId, callback);
			}
		};
	}
	
	/**
	 * Override, because the add tab needs a special link.
	 */
	@Override
	protected WebMarkupContainer newLink(String linkId, int index)
	{
		// add addLink to last or last but one position
		if((addAddTabAtLastPosition()  && (index == getTabs().size() - 1)) || 
		   (!addAddTabAtLastPosition() && (index == getTabs().size() - 2)))
		{
			WebMarkupContainer link = newAddLink(linkId, index);
			link.add(new AttributeAppender(
				"class", new Model<String>("addLink addTab"), " "));
		
			return link;
		}
		
		return super.newLink(linkId, index);
	}
	
	/**
	 * Override, because the add tab title is a icon.
	 */
	@Override
	protected Component newTitle(String titleId, IModel<?> titleModel, int index)
	{
		if((addAddTabAtLastPosition()  && (index == getTabs().size() - 1)) || 
		   (!addAddTabAtLastPosition() && (index == getTabs().size() - 2)))
		{
			return getAddTitle(titleId, titleModel, index);
		}
		
		return super.newTitle(titleId, titleModel, index);
	}
	
	protected WebMarkupContainer newAddLink(String linkId, final int index)
	{
		return new AddLink(linkId)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				setSelectedTab(index);
				
				if (target != null)
				{
					target.add(SlidableAjaxTabbedPanelWithAddTab.this);
				}
				
				onAjaxUpdate(target);
			}
			
			@Override
			public boolean isEnabled()
			{
				return isAddTabActive() && 
					   ! UserWeaveSession.get().getUser().hasRole(Role.PROJECT_GUEST);
			}
		};
	}
	
	protected Component getAddTitle(String titleId, IModel<?> titleModel, int index)
	{
		Label addTitle = new Label(titleId, new Model<String>(""));
		addTitle.add(new AttributeAppender("class", titleModel, " "));
		
		return addTitle;
	}
	
	@Override
	public void onStateChange(UiState state, AjaxRequestTarget target, EventHandler callback, StateChangeTrigger trigger)
	{
		IConfigReportStateChanger panel = 
			(IConfigReportStateChanger) SlidableAjaxTabbedPanelWithAddTab.this.get(TAB_PANEL_ID);
	
		this.setVisible(this.determineVisibility());
		
		target.add(this);
		
		if(panel != null)
		{
			panel.onStateChange(state, target, callback, trigger);
		}
	}
	
	protected boolean addAddTabAtLastPosition()
	{
		return true;
	}
	
	/**
	 * Returns a panel for the tabbed panel to display.
	 * 
	 * @param componentId
	 * 		Id for component.
	 * @return
	 */
	public abstract Panel getAddTabPanel(String componentId, ChangeTabsCallback callback);
	
	protected abstract boolean isAddTabActive();
}
