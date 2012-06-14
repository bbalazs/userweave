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

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import com.userweave.components.callback.EventHandler;
import com.userweave.pages.components.customizedtabpanel.CustomizedTabPanel;

@Deprecated
public class QuestionConfigurationTabPanel extends CustomizedTabPanel
{

	private final int studyId;
	
	private final int moduleConfigurationId;
	
	private final int questionId;
	
	private final String questionType;
	
	public QuestionConfigurationTabPanel(
			String id, 
			int studyId, 
			int moduleConfigurationId, 
			int questionId,
			String questionType,
			EventHandler callback)
	{
		super(id, callback);
		
		this.studyId = studyId;
		this.moduleConfigurationId = moduleConfigurationId;
		this.questionId = questionId;
		this.questionType = questionType;
	}

	@Override
	protected List<ITab> getTabList(final EventHandler... callback)
	{
		List<ITab> tabs = new ArrayList<ITab>();
		
		tabs.add(new AbstractTab(new Model("Question"))
		{
			
			@Override
			public Panel getPanel(String panelId)
			{
//				return new QuestionConfigurationPanel(
//						panelId, studyId, moduleConfigurationId, questionId, questionType, callback, null,0);
			
				return null;
			}
		});
		
		return tabs;
	}

}
