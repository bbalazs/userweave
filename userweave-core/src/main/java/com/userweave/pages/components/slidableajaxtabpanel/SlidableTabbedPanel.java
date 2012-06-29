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
import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.LoopItem;

/**
 * A simple ajax tabbed panel with slidable functions. That is, only a 
 * specific number of tabs are visible (depending on the size of the tab
 * title). The remaining tabs will become visible, if the tab bar is 
 * slided to the left or right.
 * 
 * @author opr
 *
 */
public class SlidableTabbedPanel extends AjaxTabbedPanel
{
	private static final long serialVersionUID = 1L;

	public static final String QUESTION_TABBED_PANEL = "questionTabs";
	
	public static final String STUDY_TABBED_PANEL = "studyTabs";
	
	public static final String ICON_TABBED_PANEL = "icontestTabs";
	
	protected final String whichTabbedPanel;
	
	public SlidableTabbedPanel(String id, List<ITab> tabs)
	{
		super(id, tabs);
		
		this.whichTabbedPanel = STUDY_TABBED_PANEL;
		
		this.get("tabs-container").setOutputMarkupId(true);
	}

	public SlidableTabbedPanel(String id, List<ITab> tabs, String whichTabbedPanel)
	{
		super(id, tabs);
		
		this.whichTabbedPanel = whichTabbedPanel;
	
		this.get("tabs-container").setOutputMarkupId(true);
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
				
				int index = getIndex();
				
				cssClass += " tab" + index;

				if (index == getSelectedTab())
				{
					cssClass += " selected";
				}
				if (index == getTabs().size() - 1)
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
	
	@Override
	protected void onAjaxUpdate(AjaxRequestTarget target)
	{
		target.appendJavaScript(getTriggerScripts());
	}
	
	public String getTriggerScripts()
	{
		return "bindMouseEvents('" + whichTabbedPanel + "');" +
			   "slideTo('." + whichTabbedPanel + "', " + getSelectedTab() + ");";
	}
	
	protected void addToTabbedPanel(Component component)
	{
		WebMarkupContainer tabsContainer = 
			(WebMarkupContainer) SlidableTabbedPanel.this.get("tabs-container");
		
		if(tabsContainer != null)
		{
			tabsContainer.add(component);
		}
	}
}
