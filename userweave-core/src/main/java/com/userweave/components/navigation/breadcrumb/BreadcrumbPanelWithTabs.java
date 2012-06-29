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
package com.userweave.components.navigation.breadcrumb;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;

import com.userweave.components.callback.EventHandler;
import com.userweave.domain.EntityBase;
import com.userweave.pages.configuration.base.IConfigReportStateChanger.UiState;

/**
 * Extends the breadcrumb panel with two tabs
 * to switch between study configuration and
 * study results.
 *  
 * @author opr
 *
 */
@Deprecated
public class BreadcrumbPanelWithTabs extends BreadcrumbPanel
{
	private static final long serialVersionUID = 1L;
	
	private final StateChangeTrigger stateChangeTrigger;
	
	public BreadcrumbPanelWithTabs(
			String id, 
			final EntityBase entity, 
			final EventHandler projectCallback,
			final EventHandler studyCallback,
			final UiState initialUiState)
	{
		super(id, entity, projectCallback);
		
		stateChangeTrigger = new StateChangeTrigger(initialUiState);
		
		addStudyTabs(entity, studyCallback);
	}

	/**
	 * Study tabs development and results.
	 * 
	 * @param entity
	 * 		The entity of this panel. Needed to determine the 
	 * 		visibility of the tabs.
	 */
	private void addStudyTabs(final EntityBase entity, final EventHandler studyCallback)
	{
		add(new AjaxLink("studyDevelTab")
		{
			@Override
			public void onClick(AjaxRequestTarget target)
			{
				stateChangeTrigger.triggerChange(UiState.CONFIG, target, studyCallback);
				
				//StudyEvent.Selected(target, entity, true).fire(studyCallback);
			}
		});
		
		add(new AjaxLink("studyResultsTab")
		{
			@Override
			public void onClick(AjaxRequestTarget target)
			{
				stateChangeTrigger.triggerChange(UiState.REPORT, target, studyCallback);
				
				//StudyEvent.Selected(target, entity, false).fire(studyCallback);
			}
		});
	}
	
	public StateChangeTrigger getTrigger()
	{
		return stateChangeTrigger;
	}
}
