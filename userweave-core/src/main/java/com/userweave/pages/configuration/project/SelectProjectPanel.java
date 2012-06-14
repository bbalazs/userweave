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
package com.userweave.pages.configuration.project;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.ajax.IAjaxUpdater;
import com.userweave.ajax.form.AjaxBehaviorFactory;
import com.userweave.application.UserWeaveSession;
import com.userweave.application.images.ImageResource;
import com.userweave.components.authorization.AuthOnlyTextArea;
import com.userweave.components.callback.EventHandler;
import com.userweave.dao.ProjectDao;
import com.userweave.domain.Project;
import com.userweave.domain.Role;
import com.userweave.domain.User;
import com.userweave.pages.components.customizedtabpanel.CustomizedTabPanel;
import com.userweave.pages.configuration.editentitypanel.EditProjectEntityPanel;
import com.userweave.pages.configuration.project.userpanel.UserPanel;
import com.userweave.pages.configuration.study.selection.StudySelectionPanel;

/**
 * Displays a tab panel for "studies" and
 * active users in this project.
 * 
 * @author opr
 *
 */
public class SelectProjectPanel extends CustomizedTabPanel implements IAjaxUpdater
{
	private static final long serialVersionUID = 1L;
	
	@SpringBean
	private ProjectDao projectDao;
	
	/**
	 * The project description text.
	 */
	private String description;
	
	/**
	 * Tab index of user tab.
	 */
	public static final int ACTIVE_USERS_TAB = 1;
	
	private class AdminOnlyTextArea extends AuthOnlyTextArea
	{
		private static final long serialVersionUID = 1L;

		public AdminOnlyTextArea(String id, IModel model)
		{
			super(id, model, null);
		}
		
		@Override
		protected boolean authorizedOnTag()
		{
			User user = UserWeaveSession.get().getUser();
			return super.authorizedOnTag() && user.hasRole(Role.PROJECT_ADMIN);
		}
	}
	
	/**
	 * Default constructor.
	 * 
	 * @param id
	 * 		Component id.
	 * @param callbacks
	 * 		Callbacks to fire, if a link is clicked. 
	 * 		[0] Project event handler
	 * 		[1] Study event handler
	 */
	public SelectProjectPanel(String id, final EventHandler... callbacks)
	{
		super(id, callbacks);
		
		addActions(callbacks[0]);
		
		addProjectLogo();
		
		addProjectDescription();
	}
	
	/**
	 * Constructor to preselect a tab.
	 * 
	 * @param id
	 * 		Component id.
	 *
	 * @param selectedTab
	 * 		Tab to preselect.
	 *
	 * @param callbacks
	 * 		Callbacks to fire, if a link is clicked. 
	 * 		[0] Project event handler
	 * 		[1] Study event handler
	 */
	public SelectProjectPanel(String id, int selectedTab, final EventHandler... callbacks)
	{
		super(id, selectedTab, callbacks);
		
		addActions(callbacks[0]);
		
		addProjectLogo();
		
		addProjectDescription();
	}
	
	private void addActions(EventHandler projectCallback)
	{
		LoadableDetachableModel projectModel = new LoadableDetachableModel()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected Object load()
			{
				return projectDao.findById(
					UserWeaveSession.get().getProject().getId());
			}
		};
		
		add(new EditProjectEntityPanel("actions", projectModel, projectCallback));
	}
	
	private void addProjectLogo()
	{
		Project project = UserWeaveSession.get().getProject();
		
		Image logo = ImageResource.createImage("projectLogo", project.getLogo());
		
		add(logo);
	}
	
	private void addProjectDescription()
	{
		description = UserWeaveSession.get().getProject().getDescription();
		
		final AdminOnlyTextArea textArea = 
			new AdminOnlyTextArea(
					"projectDescr", 
					new PropertyModel(SelectProjectPanel.this, "description"));
		
		textArea.setOutputMarkupId(true);
		
		add(textArea);
		
		textArea.add(AjaxBehaviorFactory.getUpdateBehavior("onchange", SelectProjectPanel.this));
	}
	
	@Override
	public void onUpdate(AjaxRequestTarget target)
	{
		Project project = UserWeaveSession.get().getProject();

		project.setDescription(description);

		projectDao.save(project);

		target.add(SelectProjectPanel.this.get("projectDescr"));
	}
	
	@Override
	protected List<ITab> getTabList(final EventHandler... callbacks) 
	{
		List<ITab> tabs = new ArrayList<ITab>();
		
		final Project project = UserWeaveSession.get().getProject();
		
		tabs.add(new AbstractTab(new ResourceModel("studies"))
		{
			@Override
			public Panel getPanel(String panelId) 
			{
				return new StudySelectionPanel(
						panelId, project, callbacks);
			}
		});
		
		tabs.add(new AbstractTab(new ResourceModel("activeUsers"))
		{
			@Override
			public Panel getPanel(String panelId) 
			{
				return new UserPanel(panelId, project, callbacks[0]);
			}
		});
		
		tabs.add(new AbstractTab(new ResourceModel("configuration"))
		{
			@Override
			public Panel getPanel(String panelId) 
			{
				return new ProjectConfigurationPanel(panelId, project.getId());
			}
		});
		
		return tabs;
	}
}
