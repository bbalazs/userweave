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
package com.userweave.pages.components.customizedtabpanel;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import com.userweave.components.callback.EventHandler;

/**
 * Base class for tab panels.
 * 
 * @author opr
 *
 */
public abstract class CustomizedTabPanel extends Panel 
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * The underlying ajax tabbed panel.
	 */
	private SpecialAjaxTabbedPanel tabbedPanel;
	
	/**
	 * Default constructor.
	 * 
	 * @param id
	 * 		Markup id.
	 * @param callback
	 * 		The callback for links in tabs.
	 */
	@SuppressWarnings("serial")
	public CustomizedTabPanel(String id, final EventHandler... callbacks) 
	{
		super(id);
		
		add(tabbedPanel = new SpecialAjaxTabbedPanel("tabs", getTabList(callbacks))
		{
			@Override
			protected void onAjaxUpdate(AjaxRequestTarget target)
			{
				CustomizedTabPanel.this.onAjaxUpdate(target, callbacks);
			}
		});
	}
	
	/**
	 * Constructor to preselect a tab
	 * 
	 * @param id
	 * 		Markup id.
	 * 
	 * @param selectedTab
	 * 		Tab to preselect.
	 * 
	 * @param callback
	 * 		List of callbacks to fire on event
	 * 		[0] Project event handler
	 * 		[1] Study event handler
	 */
	@SuppressWarnings("serial")
	public CustomizedTabPanel(String id, int selectedTab, final EventHandler... callbacks) 
	{
		super(id);
		
		tabbedPanel = new SpecialAjaxTabbedPanel("tabs", getTabList(callbacks))
		{
			@Override
			protected void onAjaxUpdate(AjaxRequestTarget target)
			{
				CustomizedTabPanel.this.onAjaxUpdate(target, callbacks);
			}
		};
		
		tabbedPanel.setSelectedTab(selectedTab);
		
		add(tabbedPanel);
	}
	
	/**
	 * Constructor for setting a model in this panel.
	 * 
	 * @param id
	 * 		Component id.
	 * @param model
	 * 		Model to to back this panel.
	 * @param callback
	 * 		The callbacks for links in tabs.
	 */
	@SuppressWarnings("serial")
	public CustomizedTabPanel(String id, final IModel model, final EventHandler... callbacks) 
	{
		super(id, model);
		
		add(tabbedPanel = new SpecialAjaxTabbedPanel("tabs", getTabList(callbacks))
		{
			@Override
			protected void onAjaxUpdate(AjaxRequestTarget target)
			{
				CustomizedTabPanel.this.onAjaxUpdate(target, callbacks);
			}
		});
	}
	
	/**
	 * Get the current selected tab on this panel
	 * 
	 * @return
	 * 		Index of current selected tab.
	 */
	protected int getSelectedTab()
	{
		return ((TabbedPanel) CustomizedTabPanel.this.get("tabs")).getSelectedTab();
	}
	
	/**
	 * Get the list of tabs of this panel.
	 * 
	 * @return
	 * 		List of ITabs.
	 */
	protected List<? extends ITab> getTabs()
	{
		return ((TabbedPanel) CustomizedTabPanel.this.get("tabs")).getTabs();
	}
	
	/**
	 * Replaces the current tab panel with a fresh one.
	 * 
	 * @param target
	 * 		Ajax request target
	 * @param callback
	 * 		List of callbacks to fire on event
	 * 		[0] Project event handler
	 * 		[1] Study event handler
	 */
	protected void replaceTabbedPanel(AjaxRequestTarget target, final EventHandler... callbacks)
	{
		@SuppressWarnings("serial")
		SpecialAjaxTabbedPanel replacement = 
			new SpecialAjaxTabbedPanel("tabs", getTabList(callbacks))
			{
				@Override
				protected void onAjaxUpdate(AjaxRequestTarget target)
				{
					CustomizedTabPanel.this.onAjaxUpdate(target, callbacks);
				}
			};
		
		tabbedPanel.replaceWith(replacement);
		tabbedPanel = replacement;
		
		target.add(tabbedPanel);
	}
	
	/**
	 * Triggered by the ajax tab panel this panel consits of
	 * on after ajax request
	 * 
	 * @param target
	 * 		Ajax request target
	 * @param callback
	 * 		List of callbacks to fire on event
	 * 		[0] Project event handler
	 * 		[1] Study event handler
	 */
	protected void onAjaxUpdate(AjaxRequestTarget target, final EventHandler... callbacks){}
	
	/**
	 * Tabs to display on this panel
	 * 
	 * @param callback
	 * 		List of callbacks to fire on event
	 * 		[0] Project event handler
	 * 		[1] Study event handler
	 * @return
	 * 		List of ITabs.
	 */
	protected abstract List<ITab> getTabList(final EventHandler... callbacks);
}
