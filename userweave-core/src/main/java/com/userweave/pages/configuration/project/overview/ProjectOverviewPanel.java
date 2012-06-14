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
package com.userweave.pages.configuration.project.overview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.application.UserWeaveSession;
import com.userweave.components.callback.EventHandler;
import com.userweave.dao.ProjectDao;
import com.userweave.dao.ProjectInvitationDao;
import com.userweave.domain.Project;
import com.userweave.domain.ProjectInvitation;
import com.userweave.domain.User;
import com.userweave.pages.components.customizedtabpanel.CustomizedTabPanel;
import com.userweave.pages.configuration.project.ProjectInvitationsPanel;
import com.userweave.pages.configuration.project.projectbrowser.MyProjectListViewPanel;
import com.userweave.pages.configuration.project.projectbrowser.ProjectListViewPanel;

/**
 * Displays a Tab Panel for "my projects", 
 * "public projects" and "invitations"
 * 
 * @author opr
 */
public class ProjectOverviewPanel extends CustomizedTabPanel 
{
	private static final long serialVersionUID = 1L;

	/*
	 * FIXME: Adjust indices, if a new tab is added to the tab list.
	 */
	public static final int MY_ACCOUNT_TAB = 0;
	
	private int INVITATION_TAB_INDEX;
	
	private int numberOfInvitations;
	
	private IModel titleModel;
	
	@SpringBean
	private ProjectInvitationDao projectInvitationDao;
	
	@SpringBean
	private ProjectDao projectDao;
	
	/**
	 * Default constructor.
	 * 
	 * @param id
	 * 		Markup id.
	 * @param callback
	 * 		Callback for links in the tabs.
	 */
	public ProjectOverviewPanel(String id, final EventHandler callback) 
	{
		super(id, callback);
	}
	
	/**
	 * Constructor to preselect a tab.
	 * 
	 * @param id
	 * 		Markup id.
	 * @param callback
	 * 		Callback for links in the tabs.
	 * @param selectedTab
	 * 		Tab to preselect
	 */
	public ProjectOverviewPanel(String id, final EventHandler callback, int selectedTab) 
	{
		super(id, callback);
	}
	
	/**
	 * Creates a tab panel with "my projects", "public projects" 
	 * and if necessary an "invitations" tab. Portal admins have
	 * also an administration tab.
	 */
	@Override
	protected List<ITab> getTabList(final EventHandler... callback)
	{
		final User user = getUser();
		
		List<ITab> tabs = new ArrayList<ITab>();
		
		tabs.add(new AbstractTab(new ResourceModel("projects"))
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Panel getPanel(String panelId) 
			{
				IModel<List<Project>> projectsModel = 
					new LoadableDetachableModel<List<Project>>()
				{
					private static final long serialVersionUID = 1L;

					@Override
					protected List<Project> load()
					{
						return projectDao.findByUser(
							user, UserWeaveSession.get().isAdmin());
					}
				};
				
				return new MyProjectListViewPanel(
						panelId,
						projectsModel,
						callback[0],
						null);
			}
		});
		
		tabs.add(new AbstractTab(new ResourceModel("projectBrowser"))
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			public Panel getPanel(String panelId) 
			{
				List<Project> publicProjects = projectDao.findPublicProjects();
				Collections.shuffle(publicProjects);
			
				return new ProjectListViewPanel(
						panelId,
						Model.ofList(publicProjects),
						callback[0]);
			}
		});
		
		if(user.isAdmin())
		{
			tabs.add(
				new AbstractTab(
					new StringResourceModel("admin_projects", ProjectOverviewPanel.this, null))
			{
				private static final long serialVersionUID = 1L;

				
				@Override
				public Panel getPanel(String panelId)
				{
					return new ProjectListViewPanel(
							panelId,
							Model.ofList(projectDao.findAll()),
							callback[0]);
				}
			});
		}
		
		List<ProjectInvitation> invitations = 
			projectInvitationDao.findByRecipant(user);
		
		numberOfInvitations = invitations.size();
		
		// display a tab for invitations only if 
		// there are invitations
		if(numberOfInvitations > 0)
		{	
			INVITATION_TAB_INDEX = tabs.size();
			
			titleModel = new LoadableDetachableModel()
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected Object load()
				{
					return new StringResourceModel(
							"invitations", 
							ProjectOverviewPanel.this, 
							null, 
							new Object[] {numberOfInvitations}).getString();
				}
			};
			
			tabs.add(new AbstractTab(titleModel)
			{
				private static final long serialVersionUID = 1L;

				@Override
				public Panel getPanel(String panelId) 
				{
					return new ProjectInvitationsPanel(panelId)
					{
						private static final long serialVersionUID = 1L;

						@Override
						protected void onRemoveInvitation(
								AjaxRequestTarget target)
						{
							numberOfInvitations--;
							
							target.addComponent(ProjectOverviewPanel.this.get("tabs"));
						}
						
					};
				}
			});
		}
		else
		{
			INVITATION_TAB_INDEX = -1;
		}
		
		
		return tabs;
	}
	
	/**
	 * Convenient method to get the active user and
	 * to reset the current selected project.
	 * 
	 * @return
	 * 		The active user.
	 */
	private User getUser()
	{
		UserWeaveSession.get().setProjectId(null);
		return UserWeaveSession.get().getUser();
	}
	
	@Override
	protected void onAjaxUpdate(AjaxRequestTarget target, final EventHandler... callback)
	{
		if(INVITATION_TAB_INDEX != -1)
		{
			int selectedTab = getSelectedTab();
			
			if(numberOfInvitations == 0 && selectedTab != INVITATION_TAB_INDEX)
			{
				replaceTabbedPanel(target, callback);
			}
		}
	}
}
