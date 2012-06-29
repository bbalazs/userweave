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
package com.userweave.pages.configuration.editentitypanel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.WindowClosedCallback;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.joda.time.DateTime;

import com.userweave.components.authorization.AuthOnlyAjaxLink;
import com.userweave.components.callback.EntityEvent;
import com.userweave.components.callback.EventHandler;
import com.userweave.dao.ProjectDao;
import com.userweave.domain.Project;
import com.userweave.domain.Role;
import com.userweave.domain.StudyState;
import com.userweave.pages.configuration.project.EditProjectPage;

/**
 * Panel to edit a project entity.
 * 
 * @author opr
 */
public class EditProjectEntityPanel extends BaseFunctionEditEntityPanel<Project>
{
	private static final long serialVersionUID = 1L;

	@AuthorizeAction(action = Action.RENDER, roles = Role.PROJECT_ADMIN)
	private abstract class ProjectAdminLink extends AuthOnlyAjaxLink
	{
		private static final long serialVersionUID = 1L;

		public ProjectAdminLink(String id)
		{
			super(id);
		}	
	}
	
	@SpringBean
	private ProjectDao projectDao;
	
	private boolean projectHasBeenEdited = false;
	
	/**
	 * Default constructor.
	 * 
	 * @param id
	 * 		Component id.
	 * @param entity
	 * 		Project entity.
	 * @param callback
	 * 		Callback to fire on event.
	 */
	public EditProjectEntityPanel(String id, IModel<Project> entityModel,
			EventHandler callback)
	{
		super(id, entityModel, callback);
	}
	
	private Project getProject()
	{
		return getEntity();
	}
	
	@Override
	protected AuthOnlyAjaxLink getDeleteLink(String id)
	{
		ProjectAdminLink link = new ProjectAdminLink(id)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				deleteModal.show(target);
			}
		};
		
		link.add(new Label("deleteLabel", new StringResourceModel("delete", this, null)));
		
		return link;
	}
	
	@Override
	protected AuthOnlyAjaxLink getEditLink(String id)
	{
		return new ProjectAdminLink(id)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				editModal.show(target);
			}
		};
	}
	
	@Override
	protected WebPage getEditWebPage(final EventHandler callback, ModalWindow window)
	{
		return new EditProjectPage(window, getProject().getName())
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onFinish(AjaxRequestTarget target, String newProjectName)
			{
				Project project = getProject();
				project.setName(newProjectName);
				
				projectDao.save(project);
				projectHasBeenEdited = true;
			}
			
			@Override
			protected IModel<String> getAcceptLabel()
			{
				return new StringResourceModel("save", EditProjectEntityPanel.this, null);
			}
		};
	}

	@Override
	protected WindowClosedCallback getEditWindowClosedCallback(final EventHandler callback)
	{
		return new ModalWindow.WindowClosedCallback()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClose(AjaxRequestTarget target)
			{
				if(projectHasBeenEdited)
				{
					EntityEvent.Updated(target, getProject()).fire(callback);
				}
			}	
		};
	}
	
	@Override
	protected void onDelete(AjaxRequestTarget target)
	{
		Project project = getProject();
		project.setDeletedAt(new DateTime());
		
		projectDao.save(project);
	}

	/**
	 * Return the study state init, because
	 * project functions are always enabled.
	 */
	@Override
	protected IModel<StudyState> getStudyStateModel()
	{
		return new Model<StudyState>(StudyState.INIT);
	}

	@Override
	protected String getEntityName()
	{
		return getProject().getName();
	}
	
	/**
	 * @param positionBeforeDeletion
	 * 		Ignore this param.
	 */
	@Override
	protected void onAfterDelete(AjaxRequestTarget target, final EventHandler callback, Integer positionBeforeDeletion)
	{
		EntityEvent.Purged(target, getProject()).fire(callback);
	}
}
