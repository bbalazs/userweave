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
package com.userweave.pages.configuration.report;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.application.UserWeaveSession;
import com.userweave.domain.Group;
import com.userweave.domain.Study;
import com.userweave.domain.service.ModuleService;

/**
 * Panel, which contains the filter logic for studies. This panel
 * initialy contains one drop down to select a filter. If a filter
 * is selected, this panel adds a new drop down (as long as there
 * are filters) to select an additional filter.
 * 
 * @author oma
 * @author opr
 */
public abstract class GroupFilterPanelList extends Panel 
{
	private static final long serialVersionUID = 1L;

	@SpringBean
	private ModuleService moduleService; 
	
	/**
	 * List of currently selected filters.
	 */
	private final List<GroupFilterPanel> groupFilterPanelList = 
		new ArrayList<GroupFilterPanel>();

	/**
	 * Repeater for the drop downs.
	 */
	private final RepeatingView groupFilterRepeater;

	/**
	 * Container for the repeating view (a repeating view can't
	 * be added to an ajay request target).
	 */
	private final WebMarkupContainer groupFilterContainer;
	
	/**
	 * Default constructor.
	 * 
	 * @param id
	 * 		Component markup id.
	 * @param groups
	 * 		List of selected groups.
	 */
	public GroupFilterPanelList(String id, List<Group> groups) 
	{
		super(id);
		
		groupFilterContainer = new WebMarkupContainer("groupFilterContainer");
		
		groupFilterContainer.setOutputMarkupId(true);
		
		add(groupFilterContainer);
		
		
		groupFilterRepeater = new RepeatingView("groupFilterRepeater");
		
		groupFilterContainer.add(groupFilterRepeater);
		
		/*
		 * For each group add a GroupFilterPanel with a predefined
		 * filter. If there is no selected filter, add a simple
		 * not preselected drop down.
		 */
		if (groups == null || groups.isEmpty()) 
		{
			addGroupFilter(null);
		} 
		else 
		{
			for (Group group : groups) 
			{
				addGroupFilter(group);
			}
		}
	}

	/**
	 * Adds a GroupFilterPanel (i.e., a panel which contains a 
	 * drop down) to this component. 
	 * 
	 * @param group
	 * 		The filter to preselect in the GroupFilterPanel (may be null).
	 */
	private void addGroupFilter(Group group) 
	{	
		final GroupFilterPanel groupFilter = 
			new GroupFilterPanel(groupFilterRepeater.newChildId(), group) 
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onAdd(AjaxRequestTarget target) 
			{
				addGroupFilter(null);
				
//				target.add(groupFilterContainer);
//				
//				updateSelectedGroups(getSelectedGroups());
			}

			@Override
			protected void onRemove(AjaxRequestTarget target) 
			{
				if(isLast(this))
				{
					this.setSelectedGroup(null);
				}
				else
				{
					removeGroupFilter(this, target);
				}
				
				target.add(groupFilterContainer);
				
				updateSelectedGroups(getSelectedGroups());
				
				onFilter(target);
			}
			
			@Override
			protected void onSelect(AjaxRequestTarget target) 
			{
				target.add(groupFilterContainer);
				
				updateSelectedGroups(getSelectedGroups());
				
				onFilter(target);
			}

			@Override
			protected boolean addButtonIsVisible(GroupFilterPanel panel) 
			{
				return isLast(panel) && !allGroupsSelected();
			}

			private boolean allGroupsSelected() 
			{
				return getSelectedGroups().size() == getGroups().size();
			}

			private boolean isLast(GroupFilterPanel panel) 
			{
				return groupFilterPanelList.indexOf(panel) == groupFilterPanelList.size() - 1;
			}

			@Override
			protected List<Group> getSelectedGroups() 
			{
				return GroupFilterPanelList.this.getSelectedGroups();
			}
			
			@Override
			protected List<Group> getGroups() 
			{				
				return getStudy().getGroups(moduleService.getModules());				
			}
			
		};
	
		groupFilterPanelList.add(groupFilter);
		
		groupFilterRepeater.add(groupFilter);
	}
	
	/**
	 * Removes a filter from the filter list.
	 * 
	 * @param groupFilter
	 * 		Filter to remove.
	 * @param target
	 * 		AjaxRequestTarget.
	 */
	private void removeGroupFilter(GroupFilterPanel groupFilter, AjaxRequestTarget target) 
	{		
		groupFilterPanelList.remove(groupFilter);
		groupFilterRepeater.remove(groupFilter);
		
		onFilter(target);
		
		if (groupFilterPanelList.isEmpty()) 
		{
			addGroupFilter(null);
		}
	}

	/**
	 * Collects each selected filter on thos panel.
	 * 
	 * @return
	 * 		List of selected filters.
	 */
	public List<Group> getSelectedGroups() 
	{
		List<Group> selectedGroups = new ArrayList<Group>();
		
		for (GroupFilterPanel groupFilterPanel : groupFilterPanelList) 
		{
			selectedGroups.add(groupFilterPanel.getSelectedGroup());
		}		

		return selectedGroups;
	}
	
	/**
	 * Propagates a model change signal to each group filter panel.
	 */
	public void triggerModelChangeOfChoices()
	{
		for(GroupFilterPanel filter : groupFilterPanelList)
		{
			filter.getChoices().modelChanged();
		}
	}
	
	/**
	 * Updates the selected list of filters.
	 * 
	 * @param groups
	 * 		List of filters.
	 */
	private void updateSelectedGroups(List<Group> groups)
	{
		((UserWeaveSession) Session.get())
			.setSelectedGroups(getStudy().getId(), groups);
	}
	
	/**
	 * Gets the study this panel depends on.
	 * 
	 * @return
	 * 		A study entity.
	 */
	protected abstract Study getStudy();
	
	/**
	 * Callback for filter event.
	 * 
	 * @param target
	 * 		AjaxRequestTarget.
	 */
	protected abstract void onFilter(AjaxRequestTarget target);
}

