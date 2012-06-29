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

import java.util.List;

import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import com.userweave.application.UserWeaveSession;
import com.userweave.domain.Group;
import com.userweave.domain.Study;
import com.userweave.pages.configuration.report.GroupFilterPanelList;

/**
 * Panel to hold the filter options for a study.
 * 
 * @author opr
 */
public abstract class StudyFilterPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	/**
	 * Panel, which holds the filter logic.
	 */
	private final GroupFilterPanelList groupFilterList;
	
	/**
	 * Default constructor.
	 * 
	 * @param id
	 * 		Component markup id.
	 * @param studyModel
	 * 		Model which holds a study entity.
	 */
	public StudyFilterPanel(String id, IModel<?> studyModel)
	{
		super(id, studyModel);
		
		groupFilterList = newGroupFilterPanelList();
		
		groupFilterList.setOutputMarkupId(true);
		
		add(groupFilterList);
	}

	/**
	 * Creates the panel which holds the filter logic.
	 * 
	 * @return
	 */
	private GroupFilterPanelList newGroupFilterPanelList()
	{
		return new GroupFilterPanelList(
			"filter",
			((UserWeaveSession) Session.get()).getSelectedGroups(getStudy().getId()))
		{
			private static final long serialVersionUID = 1L;
	
			@Override
			protected void onFilter(AjaxRequestTarget target)
			{
				StudyFilterPanel.this.onFilter(target);
			}
	
			@Override
			protected Study getStudy()
			{
				return StudyFilterPanel.this.getStudy();
			}
		};
	}
	
	/**
	 * Shortcut to study entity in the model.
	 * 
	 * @return
	 * 		A study entity.
	 */
	private Study getStudy()
	{
		return (Study) getDefaultModelObject();
	}
	
	/**
	 * If a filter gets removed, this method informs the all filters,
	 * that their model has maybe been changed.
	 * 
	 * @param target
	 * 		AjaxRequestTarget to append the filter list panel.
	 */
	protected void triggerModelChange(AjaxRequestTarget target)
	{
		groupFilterList.triggerModelChangeOfChoices();
		target.add(groupFilterList);
	}
	
	/**
	 * Returns a list of selected groups (i.e., the list of 
	 * selected filters).
	 * 
	 * @return
	 */
	public List<Group> getSelectedGroups()
	{
		return groupFilterList.getSelectedGroups();
	}
	
	/**
	 * Callback method, if filter event occurs.
	 * 
	 * @param target
	 * 		AjaxRequestTarget.
	 */
	protected abstract void onFilter(AjaxRequestTarget target);
}
