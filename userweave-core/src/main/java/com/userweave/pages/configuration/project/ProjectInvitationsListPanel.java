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

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.dao.ProjectInvitationDao;
import com.userweave.domain.ProjectInvitation;
import com.userweave.domain.User;
import com.userweave.pages.configuration.ConfigurationPage;

/**
 * Displays a list of invitations to accept.
 * 
 * @author opr
 */
public abstract class ProjectInvitationsListPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	@SpringBean
	private ProjectInvitationDao projectInvitationDao;
	
	/**
	 * Label to display a no invitations message.
	 */
	private Label noInvitations;
	
	/**
	 * Default constructor.
	 * 
	 * @param id
	 * 		Component id.
	 * @param recipant
	 * 		User to load messages for.
	 */
	public ProjectInvitationsListPanel(String id, final User recipant)
	{
		super(id);
		
		setOutputMarkupId(true);
		
		setDefaultModel(new LoadableDetachableModel()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected Object load()
			{
				return projectInvitationDao.findByRecipant(recipant);
			}
			
		});
		
		addNoInvitaitonsLabel();
		
		final RepeatingView rv = new RepeatingView("rv");
		
		add(rv);
		
		for(final ProjectInvitation invitation : (List<ProjectInvitation>) getDefaultModelObject())
		{	
			noInvitations.setVisible(false);
			
			rv.add(new ProjectInvitationListItem(rv.newChildId(), invitation)
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick(AjaxRequestTarget target, boolean accept)
				{
					rv.remove(this);
					
					onRemoveInvitation(target);
					
					if(rv.size() == 0)
					{
						noInvitations.setVisible(true);
						target.addComponent(noInvitations);
					}
					
					if(accept)
					{
						setResponsePage(new ConfigurationPage(invitation.getProject()));
					}
					else
					{
						target.addComponent(ProjectInvitationsListPanel.this);
					}
				}
			});
		}
	}
	
	/**
	 * Create a no inivtation message label.
	 */
	private void addNoInvitaitonsLabel()
	{
		noInvitations = new Label("noInvitationsAvailable", 
					new StringResourceModel("noInvitationsAvailable", this, null));
		
		noInvitations.setOutputMarkupId(true);
		
		add(noInvitations);
	}
	
	protected abstract void onRemoveInvitation(AjaxRequestTarget target);
}
