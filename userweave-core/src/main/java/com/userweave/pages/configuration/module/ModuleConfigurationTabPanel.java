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
package com.userweave.pages.configuration.module;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;

import com.userweave.components.callback.EventHandler;
import com.userweave.pages.components.customizedtabpanel.CustomizedTabPanel;

@Deprecated
public class ModuleConfigurationTabPanel extends CustomizedTabPanel
{
	private static final long serialVersionUID = 1L;

	private final Integer studyId;
	
	private final Integer moduleConfigurationId;
	
	public ModuleConfigurationTabPanel(
			String id, 
			Integer studyId,
			Integer moduleConfigurationId,
			EventHandler callback)
	{
		super(id, callback);
		
		this.studyId = studyId;
		
		this.moduleConfigurationId = moduleConfigurationId;
	}

	@Override
	protected List<ITab> getTabList(final EventHandler... callback)
	{
		List<ITab> tabs = new ArrayList<ITab>();
		
		tabs.add(new AbstractTab(new ResourceModel("content"))
		{
			@Override
			public Panel getPanel(String panelId)
			{
//				return new ModuleConfigurationPanel(
//						panelId, 
//						studyId, 
//						moduleConfigurationId, 
//						callback,
//						null,0, null);
				
				return null;
			}
		});
		
		return tabs;
	}

}
