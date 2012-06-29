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
package com.userweave.pages.configuration.project.invitation;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.components.customModalWindow.BaseModalWindowPage;
import com.userweave.dao.ProjectDao;
import com.userweave.dao.ProjectInvitationDao;
import com.userweave.dao.RoleDao;
import com.userweave.dao.UserDao;
import com.userweave.domain.Project;
import com.userweave.domain.ProjectInvitation;
import com.userweave.domain.User;
import com.userweave.pages.user.RegisterUser;

public class InviteUserToProjectPage extends BaseModalWindowPage
{
	private static final long serialVersionUID = 1L;

	@SpringBean
	private UserDao userDao;
	
	@SpringBean
	private RoleDao roleDao;
	
	@SpringBean
	private ProjectInvitationDao projectInvitationDao;
	
	@SpringBean
	private ProjectDao projectDao;
	
	private final Project project;
	
	private final User user;
	
	private final Component displayComponent;
	
	public InviteUserToProjectPage(
			final User user, final Project project, final ModalWindow window)
	{	
		super(window);
		
		this.project = project;
		
		this.user = user;
		
		displayComponent = new InvitationFormPanel("invitationPanel");
		
		displayComponent.setOutputMarkupId(true);
		
		addToForm(displayComponent);
	}
	
	@Override
	protected WebMarkupContainer getAcceptButton(String componentId,
			final ModalWindow window)
	{
		final AjaxSubmitLink link = new AjaxSubmitLink(componentId, getForm())
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onError(AjaxRequestTarget target, Form form)
			{
				// add the feedback panel of the invitation form panel.
				target.addComponent(
					((InvitationFormPanel) displayComponent).getFeedbackPanel());
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form form)
			{
				InvitationFormPanel panel = (InvitationFormPanel) displayComponent;
				
				String addressee = panel.getInvitaitonAddressee().toLowerCase();
				
				User recipant = userDao.findByEmail(addressee);
				
				if(recipant == null) // user is not a registered user
				{
					if(verifyEmailAddress(addressee))
					{
						if(project == null)
						{
							triggerError("projectDoesNotExists", panel, target);
						}
						else if(userHasBeenInvited(addressee, project))
						{
							triggerError("userAlreadyInvited", panel, target);
						}
						else // send new invitation
						{
							projectInvitationDao.sendInvitation(
								addressee,
								user, 
								project, 
								roleDao.findByName(panel.getRole().getRoleName()),
								panel.getSelectedLocale(),
								InviteUserToProjectPage.this);
							
							
							window.close(target);
							
							//replaceDisplayComponentAndHideLink(target);
						}
					}
					else // email address incorect
					{
						triggerError("emailAddressIncorrectPattern", panel, target);
					}
				}
				else
				{	
					if(project == null)
					{
						triggerError("projectDoesNotExists", panel, target);
					}
					else
					{
						if(isUserAlreadyInProject(recipant, project))
						{
							triggerError("userAlreadyInProject", panel, target);
						}
						else if(userHasBeenInvited(recipant, project))
						{
							triggerError("userAlreadyInvited", panel, target);
						}
						else
						{
							projectInvitationDao.sendInvitation(
								user, recipant, project, 
								roleDao.findByName(panel.getRole().getRoleName()));
							
							window.close(target);
							
							//replaceDisplayComponentAndHideLink(target);
						}
					}
					
				}
			}
		};
		
		link.setOutputMarkupId(true);
		
		return link;
	}
	
//	private void replaceDisplayComponentAndHideLink(AjaxRequestTarget target)
//	{
//		getAcceptLink().setVisible(false);
//		
//		target.addComponent(getAcceptLink());
//		
//		Component replacement = new InvitationSendMessagePanel("invitationPanel");
//		
//		displayComponent.replaceWith(replacement);
//		
//		displayComponent = replacement;
//		
//		replacement.setOutputMarkupId(true);
//		
//		target.addComponent(displayComponent);
//	}
	
	/**
	 * Displays the error message given by the key resource.
	 * 
	 * @param key
	 * 		Key for the StringResourceModel.
	 * @param panel
	 * 		Panel, wehere the feedback panel resides.
	 * @param target
	 * 		Ajax request target.
	 */
	private void triggerError(
		String key, InvitationFormPanel panel, AjaxRequestTarget target)
	{
		error(new StringResourceModel(
				key, InviteUserToProjectPage.this, null).getString());
		
		target.addComponent(panel.getFeedbackPanel());
	}
	
	@Override
	protected IModel getAcceptLabel()
	{
		return new StringResourceModel("invite", this, null);
	}
	
	@Override
	protected IModel getDeclineLabel()
	{
		return new StringResourceModel("closeWindow", this, null);
	}
	
	private boolean verifyEmailAddress(String invitaitonAddressee)
	{
		if(!RegisterUser.checkMail(invitaitonAddressee))
		{
			return false;
		}
		
		return true;
	}
	
	private boolean isUserAlreadyInProject(User user, Project project)
	{
		return projectDao.findByUser(user, false).contains(project);
	}
	
	private boolean userHasBeenInvited(User recipant, Project project)
	{
		List<ProjectInvitation> list = 
			projectInvitationDao.findByRecipantAndProject(recipant, project);
		
		return ! list.isEmpty();
	}
	
	private boolean userHasBeenInvited(String email, Project project)
	{
		List<ProjectInvitation> list =
			projectInvitationDao.findByEmailAndProject(email, project);
		
		return ! list.isEmpty();
	}
}
