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

import java.util.Iterator;
import java.util.List;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.application.UserWeaveSession;
import com.userweave.components.callback.EntityEvent;
import com.userweave.components.callback.EventHandler;
import com.userweave.components.customModalWindow.CustomModalWindow;
import com.userweave.dao.ProjectDao;
import com.userweave.dao.ProjectUserRoleJoinDao;
import com.userweave.domain.Project;
import com.userweave.domain.ProjectUserRoleJoin;
import com.userweave.domain.User;
import com.userweave.domain.service.ProjectService;
import com.userweave.pages.components.button.AddLink;
import com.userweave.pages.configuration.project.EditProjectPage;

/**
 * Extends the ProjectListViewPanel with the 
 * "add project" function.
 * 
 * @important This panel overrides the markup of its parent!
 * 
 * @author opr
 */
public class MyProjectListViewPanel extends ProjectListViewPanel<List<Project>>
{
	private static final long serialVersionUID = 1L;

	@SpringBean
	private ProjectDao projectDao;
	
	@SpringBean
	private ProjectService projectService;
	
	@SpringBean
	private ProjectUserRoleJoinDao purjDao;
	
	/**
	 * Dialog for creating new projects.
	 */
	private final ModalWindow newProjectModalWindow;
	
	/**
	 * Number of projects the user has.
	 */
	private final int numberOfProjects;
	
	/**
	 * Default Constructor.
	 * 
	 * @param id
	 * 		Component markup id.
	 * @param projects
	 * 		List of projects to display
	 * @param callback
	 * 		Callback for each link.
	 * @param parentProject
	 * 		If this component is attached to a SelectProjectPanel
	 * 		and the panel displays a project, which is a child
	 * 		project, this parameter is contains the reference to
	 * 		the parent project. Else it is null.   
	 */
	public MyProjectListViewPanel(
			String id, 
			IModel<List<Project>> projectsModel,
			EventHandler callback,
			Project parentProject) 
	{
		super(id, projectsModel, callback);
		
		setOutputMarkupId(true);
		
		this.numberOfProjects = getProjects().size();
		
		newProjectModalWindow = 
			createNewProjectModalWindow(parentProject, callback);
		
		add(newProjectModalWindow);
		
		add(new AddLink("addProject", 
				new StringResourceModel(
					"addProject", MyProjectListViewPanel.this, null))
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) 
			{
				newProjectModalWindow.show(target);
			}	
		});
	}
	
	@Override
	protected void populateItem(EventHandler callback, ListItem<Project> item)
	{
		super.populateItem(callback, item);
		
		final Project project = item.getModelObject();
		final User user = UserWeaveSession.get().getUser();
		
		item.add(createMoveUp(project, user, item.getIndex()));
		item.add(createMoveDown(project, user, item.getIndex()));
	}
	
	/**
	 * Create a dialog to create a new project.
	 * 
	 * @return
	 * 		A CustomModalWindow
	 */
	private ModalWindow createNewProjectModalWindow(
		final Project parentProject, final EventHandler callback)
	{
		final ModalWindow projectModalWindow = 
			new CustomModalWindow("createNewProjectModalWindow");
		
		projectModalWindow.setTitle(
			new StringResourceModel(
					"newProject", 
					MyProjectListViewPanel.this, 
					null));
				
		projectModalWindow.setPageCreator(new ModalWindow.PageCreator()
		{	
			private static final long serialVersionUID = 1L;

			@Override
			public Page createPage()
			{				
				return new EditProjectPage(projectModalWindow)
				{
					private static final long serialVersionUID = 1L;

					@Override
					protected void onFinish(
						AjaxRequestTarget target, String projectName)
					{
						Project project = 
							projectService.createPreConfiguredProject(projectName);
						
						/**
						 * @see #933. User can add subproject, so there must be
						 * getUser(project) called
						 */
						projectDao.finalizeProject(
							project, UserWeaveSession.get().getUser());
						
						UserWeaveSession.get().setCreatedEntityId(project.getId());
					}
					
				};
			}
		});
		
		projectModalWindow.setWindowClosedCallback(new ModalWindow.WindowClosedCallback()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClose(AjaxRequestTarget target)
			{
				Integer projectId = UserWeaveSession.get().getCreatedEntityId();
				
				if(projectId != null)
				{
					EntityEvent.Selected(
							target, 
							projectDao.findById(projectId)).fire(callback);
				
					projectId = null;
				}
			}
		});
		
		return projectModalWindow;
	}
	
	/**
	 * Factory method to create an ajax link to move the
	 * given project in the ordering 'up';
	 * 
	 * @param project
	 * 		Project to move.
	 * @param user
	 * 		User to find joins to. 
	 * @param rowIndex
	 * 		The index of the row that is currently populated.
	 * @return
	 * 		An ajax link.
	 */
	private AjaxLink<Void> createMoveUp(
		final Project project, final User user, final int rowIndex)
	{
		return new AjaxLink<Void>("moveUp")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				moveUp(project, user);
				
				target.add(MyProjectListViewPanel.this);
			}
			
			@Override
			public boolean isEnabled()
			{
				return rowIndex > 0;
			}
		};
	}
	
	/**
	 * Factory method to create an ajax link to move the
	 * given project in the ordering 'down';
	 * 
	 * @param project
	 * 		Project to move.
	 * @param user
	 * 		User to find joins to. 
	 * @param rowIndex
	 * 		The index of the row that is currently populated.
	 * @return
	 * 		An ajax link.
	 */
	private AjaxLink<Void> createMoveDown(
		final Project project, final User user, final int rowIndex)
	{
		return new AjaxLink<Void>("moveDown")
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick(AjaxRequestTarget target)
			{
				moveDown(project, user);
				
				target.add(MyProjectListViewPanel.this);
			}
			
			@Override
			public boolean isEnabled()
			{
				if(numberOfProjects <= PROJECT_ROWS)
				{
					return rowIndex < (numberOfProjects - 1);
				}
				
				return rowIndex < (PROJECT_ROWS - 1);
			}
		};
	}
	
	/**
	 * Moves a project up in the list of users projects.
	 * 
	 * @param project
	 * 		Project to move.
	 * @param user
	 * 		User to find joins to. 
	 */
	private void moveUp(final Project project, final User user)
	{
		List<ProjectUserRoleJoin> joins = purjDao.getJoinsByUser(user);
		
		Iterator<ProjectUserRoleJoin> i = joins.iterator();
		
		// previous join
		ProjectUserRoleJoin prev = null;
		
		while(i.hasNext())
		{
			ProjectUserRoleJoin join = i.next();
			
			if(join.getProject().equals(project))
			{
				if(prev != null)
				{
					swapJoins(join, prev);
				}
				
				break;
			}
			
			prev = join;
		}
	}
	
	/**
	 * Moves a project down in the list of users projects.
	 * 
	 * @param project
	 * 		Project to move.
	 * @param user
	 * 		User to find joins to. 
	 */
	private void moveDown(final Project project, final User user)
	{
		List<ProjectUserRoleJoin> joins = purjDao.getJoinsByUser(user);
		
		Iterator<ProjectUserRoleJoin> i = joins.iterator();
		
		while(i.hasNext())
		{
			ProjectUserRoleJoin join = i.next();
			
			if(join.getProject().equals(project))
			{
				if(i.hasNext())
				{
					ProjectUserRoleJoin next = i.next();
				
					swapJoins(join, next);
				}
	
				break;
			}
		}
	}
	
	/**
	 * Swap two joins' positions.
	 * 
	 * @param o1
	 * @param o2
	 */
	private void swapJoins(ProjectUserRoleJoin o1, ProjectUserRoleJoin o2)
	{
		Integer index = o1.getPosition();
		o1.setPosition(o2.getPosition());
		o2.setPosition(index);
		
		purjDao.save(o1);
		purjDao.save(o2);
	}
}
