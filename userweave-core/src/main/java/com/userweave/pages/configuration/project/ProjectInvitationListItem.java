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

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.domain.ProjectInvitation;
import com.userweave.domain.service.ProjectInvitationService;

public abstract class ProjectInvitationListItem extends Panel
{
	private static final long serialVersionUID = 1L;
	
	@SpringBean
	private ProjectInvitationService projectInvitationService;
	
	public ProjectInvitationListItem(String id, final ProjectInvitation invitation)
	{
		super(id);
		
		add(new Label("projectName", invitation.getProject().getName()));
		
		AbstractLink accept = new AjaxLink("accept") 
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) 
			{
				projectInvitationService.acceptProjectInvitation(invitation.getId());
				
				ProjectInvitationListItem.this.onClick(target, true);
			}			
		};
		
		AjaxLink decline = new AjaxLink("decline")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				projectInvitationService.declineProjectInvitation(invitation.getId());
				
				ProjectInvitationListItem.this.onClick(target, false);
			}
			
		};
		
		add(decline);
		add(accept);
	}
	
	public abstract void onClick(AjaxRequestTarget target, boolean accept);
}
