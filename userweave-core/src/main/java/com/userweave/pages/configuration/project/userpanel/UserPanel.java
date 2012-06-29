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
package com.userweave.pages.configuration.project.userpanel;

import java.util.List;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.application.UserWeaveSession;
import com.userweave.components.ToolTipAjaxLink;
import com.userweave.components.IToolTipComponent.ToolTipType;
import com.userweave.components.callback.EntityEvent;
import com.userweave.components.callback.EventHandler;
import com.userweave.components.customModalWindow.CustomModalWindow;
import com.userweave.dao.ProjectInvitationDao;
import com.userweave.dao.ProjectUserRoleJoinDao;
import com.userweave.dao.RoleDao;
import com.userweave.domain.Project;
import com.userweave.domain.ProjectInvitation;
import com.userweave.domain.ProjectUserRoleJoin;
import com.userweave.domain.Role;
import com.userweave.domain.User;
import com.userweave.domain.service.ProjectInvitationService;
import com.userweave.pages.configuration.project.invitation.InviteUserToProjectPage;

/**
 * Panel to display a list of active users and 
 * their roles in a given project.
 * 
 * @author opr
 */
public class UserPanel extends Panel 
{
	private static final long serialVersionUID = 1L;

	@SpringBean
	private ProjectUserRoleJoinDao purjDao;
	
	@SpringBean
	private ProjectInvitationDao projectInvitationDao;
	
	@SpringBean
	private ProjectInvitationService projectInvitationService;	
	
	@SpringBean
	private RoleDao roleDao;
	
	/**
	 * Invite user dialog.
	 */
	private CustomModalWindow inviteUser;
	
	/**
	 * List of open invitations.
	 */
	private ListView invitationsList;
	
	/**
	 * Panel to display a list of admins of the given
	 * project.
	 */
	private UserListPanel admins;

	/**
	 * Panel to display a list of participants respective
	 * a list of guests of the given project.
	 */
	private UserListPanel participants, guests;
	
	/**
	 * Label to display, if no invitations are there.
	 */
	private Label noInvitations;
	
	/**
	 * Authorized version of an ajax link. 
	 * 
	 * @author opr
	 */
	@AuthorizeAction(action = Action.RENDER, 
					 roles = {Role.PROJECT_ADMIN, Role.PROJECT_PARTICIPANT})
	private abstract class AuthLink extends AjaxLink
	{
		private static final long serialVersionUID = 1L;

		public AuthLink(final String id)
		{
			super(id);
		}
	}
	
	
	/**
	 * Default constructor.
	 * 
	 * @param id
	 * 		Component Markup id.
	 * @param project
	 * 		Project to laod users for.
	 */
	public UserPanel(String id, final Project project, EventHandler projectCallback)
	{
		super(id);
		
		setOutputMarkupId(true);
		
		admins = createUserListPanel(
			"adminList", purjDao.getProjectAdmins(project), project, "noAdmins");
		
		participants = createUserListPanel(
			"participantsList", purjDao.getProjectParticipants(project), project, "noParticipants");
		
		guests = createUserListPanel(
			"guestList", purjDao.getProjectGuests(project), project, "noGuests");
		
		add(admins);
		add(participants);
		add(guests);

		add(inviteUser = getInviteUserModal(project));
		
		add(new AuthLink("inviteUser")
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick(AjaxRequestTarget target)
			{
				inviteUser.show(target);
			}
		});
		
		addPendingInvitations(project);
		
		addWriteMessageLinkAndDialog(project);
		
		addBecomingGuestLink(project, projectCallback);
	}
	
	/**
	 * Creates a link and a window to write a message to 
	 * all project users.
	 * 
	 * @param project
	 * 		The project, the users belonging to.
	 */
	private void addWriteMessageLinkAndDialog(final Project project)
	{
		final ModalWindow window = createMessageModal(project);
		
		add(window);
		
		add(new AjaxLink("writeMessageLink")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				window.show(target);
			}
		});
	}
	
	/**
	 * Factory method to create a dialog to write all users
	 * in this project a message.
	 * 
	 * @param project
	 * 		
	 * @return
	 */
	private ModalWindow createMessageModal(final Project project)
	{
		final CustomModalWindow window = new CustomModalWindow("writeMessageModal");
		
		window.setTitle(new StringResourceModel("writeMessage", UserPanel.this, null));
		
		window.setPageCreator(new ModalWindow.PageCreator()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page createPage()
			{
				return new WriteMessagePage(window, project);
			}
		});
		
		return window;
	}
	
	/**
	 * A user, who is not a member of this project can become a member
	 * by clicking this link.
	 * 
	 * @see: #1290
	 * 
	 * @param project
	 * 		Project to become guest in.
	 * @param projectCallback
	 * 		Callback to fire a select event.
	 */
	private void addBecomingGuestLink(
		final Project project, final EventHandler projectCallback)
	{
		ToolTipAjaxLink link = new ToolTipAjaxLink("becomingGuest", ToolTipType.IMPOSSIBLE)
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick(AjaxRequestTarget target)
			{
				ProjectUserRoleJoin newJoin = 
					purjDao.createJoin(
						project,
						UserWeaveSession.get().getUser(),
						roleDao.findByName(Role.PROJECT_GUEST));
				
				purjDao.save(newJoin);
			
				EntityEvent.Selected(target, project).fire(projectCallback);
			}
			
			
			@Override
			public boolean isEnabled()
			{
				Roles userRoles = 
					UserWeaveSession.get().getUser().getCurrentProjectRoles();
				
				return super.isEnabled() && (userRoles == null || userRoles.isEmpty());
			}
		};
		
		add(link);
	}
	
	/**
	 * List of pending not yet accepted invitations.
	 * 
	 * @param project
	 * 		Project to load invitations for.
	 */
	private void addPendingInvitations(final Project project)
	{
		IModel listModel = new LoadableDetachableModel()
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			protected Object load()
			{
				return projectInvitationDao.findByProject(project);
			}
		};
			
		invitationsList = new ListView("pendingInvitations", listModel)
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(ListItem item)
			{
				final ProjectInvitation invitation = 
					(ProjectInvitation) item.getModelObject();
				
				if(invitation.getAddressee() == null)
				{
					// user is not yet member of the platform
					item.add(new Label("addressee.email", invitation.getEmail()));
				}
				else
				{
					item.add(new Label("addressee.email", invitation.getAddressee().getEmail()));
				}
				
				item.add(new Label(
					"role", 
					new StringResourceModel(
						invitation.getRole().getRoleName(), UserPanel.this, null)));
				
				item.add(new AjaxLink("deleteInvitation")
				{
					private static final long serialVersionUID = 1L;
					
					@Override
					public void onClick(AjaxRequestTarget target)
					{	
						projectInvitationService.declineProjectInvitation(invitation.getId());
						
						invitationsList.modelChanged();
						
						target.addComponent(UserPanel.this);
						replaceNoInvitations(target);
					}
					
					@Override
					public boolean isVisible()
					{
						User currentUser = UserWeaveSession.get().getUser();
						
						return invitation.getAddresser().equals(currentUser) ||
							   currentUser.hasRole(Role.PROJECT_ADMIN);
					}
				});
			}
		};
		
		invitationsList.setOutputMarkupId(true);
		
		noInvitations = new Label("noInvitations", new StringResourceModel("noInvitations", this, null));
		
		noInvitations.setVisible(invitationsList.getList().isEmpty());
		
		noInvitations.setOutputMarkupId(true);
		
		add(noInvitations);
		
		add(invitationsList);
		
	}

	private UserListPanel createUserListPanel(
		String id, List<ProjectUserRoleJoin> list, final Project project, String resourceKey)
	{
		UserListPanel panel = 
			new UserListPanel(id, list, project, resourceKey)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onAfterRoleChange(AjaxRequestTarget target)
			{
				updateUserPanels(target, project);
			}
			
			@Override
			protected void onAfterDelete(AjaxRequestTarget target)
			{
				updateUserPanels(target, project);
			}
		};
		
		panel.setOutputMarkupId(true);
	
		return panel;
	}
	
	
	
	private void updateUserPanels(AjaxRequestTarget target, Project project)
	{
		replaceAdmins(target, createUserListPanel(
			"adminList", purjDao.getProjectAdmins(project), project, "noAdmins"));
		
		replaceParticipants(target, createUserListPanel(
				"participantsList", purjDao.getProjectParticipants(project), project, "noParticipants"));
		
		replaceGuests(target, createUserListPanel(
				"guestList", purjDao.getProjectGuests(project), project, "noGuests"));
	}
	
	private void replaceNoInvitations(AjaxRequestTarget target)
	{
		Label replacement = new Label(
			"noInvitations", new StringResourceModel("noInvitations", this, null));
		
		replacement.setOutputMarkupId(true);
		
		replacement.setVisible(invitationsList.getList().isEmpty());
		
		noInvitations.replaceWith(replacement);
		
		noInvitations = replacement;
		
		target.addComponent(noInvitations);
	}
	
	private void replaceAdmins(AjaxRequestTarget target, UserListPanel replacement)
	{
		admins.replaceWith(replacement);
		admins = replacement;
		
		target.addComponent(admins);
	}
	
	private void replaceParticipants(AjaxRequestTarget target, UserListPanel replacement)
	{
		participants.replaceWith(replacement);
		participants = replacement;
		
		target.addComponent(participants);
	}
	
	private void replaceGuests(AjaxRequestTarget target, UserListPanel replacement)
	{
		guests.replaceWith(replacement);
		guests = replacement;
		
		target.addComponent(guests);
	}
	
	/**
	 * Creates an Invitation dialog.
	 * 
	 * @return
	 * 		A CustomModalWindow.
	 */
	private CustomModalWindow getInviteUserModal(final Project project)
	{
		final CustomModalWindow invite = new CustomModalWindow("inviteUserModal");
		
		invite.setTitle(new StringResourceModel("inviteUser", this, null));
		
		invite.setPageCreator(new ModalWindow.PageCreator()
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			public Page createPage()
			{
				return new InviteUserToProjectPage(
						UserWeaveSession.get().getUser(), project, invite);
			}
		});
		
		invite.setWindowClosedCallback(new ModalWindow.WindowClosedCallback()
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClose(AjaxRequestTarget target)
			{
				target.addComponent(UserPanel.this);
				replaceNoInvitations(target);
			}
		});
		
		return invite;
	}
}
