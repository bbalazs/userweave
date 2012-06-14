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
package com.userweave.pages.configuration.project.projectbrowser;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.navigation.paging.IPagingLabelProvider;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigation;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import com.userweave.application.images.ImageResource;
import com.userweave.components.callback.EntityEvent;
import com.userweave.components.callback.EventHandler;
import com.userweave.domain.Project;

/**
 * Displays a pageable list of projects.
 * 
 * @author opr
 */
public class ProjectListViewPanel<T extends List<Project>> extends Panel
{
	private static final long serialVersionUID = 1L;

	/**
	 * Number of projects to display on each page.
	 */
	public static final int PROJECT_ROWS = 10;
	
	/**
	 * Default constructor.
	 * 
	 * @param id
	 * 		Component markup id.
	 * @param projects
	 * 		List of projects to display.
	 * @param callback
	 * 		Callback to fire on event.
	 */
	public ProjectListViewPanel(
		String id, IModel<List<Project>> projectsModel, final EventHandler callback)
	{
		super(id, projectsModel);
		
		PageableListView<Project> publicProjects = 
			new PageableListView<Project>("publicProjects", projectsModel, PROJECT_ROWS)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Project> item)
			{
				ProjectListViewPanel.this.populateItem(callback, item);
			}
		};
		
		PagingNavigator nav = new PagingNavigator("navigator", publicProjects)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected PagingNavigation newNavigation(final String id, IPageable pageable,
					IPagingLabelProvider labelProvider)
			{
				PagingNavigation nav = super.newNavigation(id, pageable, labelProvider);
				
				nav.setViewSize(8);
				
				return nav;
			}
		};
		
		add(publicProjects);
		add(nav);
	}
	
	/**
	 * Populates the current row of the PageableListView.
	 * 
	 * @param callback
	 * 		Callback to fire on event.
	 * @param item
	 * 		The current row to populate.
	 */
	protected void populateItem(final EventHandler callback,
			ListItem<Project> item)
	{
		final Project project = item.getModelObject();
		
		AbstractLink select = getProjectSelectLink("select", project, callback);
		
		select.add(new Label("projectDescription", project.getDescription()));
		select.add(new Label("projectName", project.getName()));
		
		item.add(select);
		
		
		AbstractLink logoSelect = getProjectSelectLink("logoSelect", project, callback);
		
		logoSelect.add(ImageResource.createImage("logo", project.getLogo()));
	
		item.add(logoSelect);
	}
	
	/**
	 * Factory method for a link to select a project.
	 * 
	 * @param id
	 * 		Component markup id.
	 * @param project
	 * 		Project to open.
	 * @param callback
	 * 		Callback to fire on project select.
	 * @return
	 */
	private AjaxLink<Void> getProjectSelectLink(
		String id, final Project project, final EventHandler callback)
	{
		return new AjaxLink<Void>(id)
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick(AjaxRequestTarget target)
			{
				EntityEvent.Selected(target, project).fire(callback);
			}
		};
	}
	
	/**
	 * Shortcut to get porjets form the model.
	 * 
	 * @return
	 * 		A list of projects
	 */
	protected List<Project> getProjects()
	{
		return (List<Project>) getDefaultModelObject();
	}
}
