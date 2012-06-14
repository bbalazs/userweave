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

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.application.UserWeaveSession;
import com.userweave.components.customModalWindow.CustomModalWindow;
import com.userweave.dao.ProjectUserRoleJoinDao;
import com.userweave.dao.RoleDao;
import com.userweave.dao.StudyDao;
import com.userweave.domain.Project;
import com.userweave.domain.ProjectUserRoleJoin;
import com.userweave.domain.Role;
import com.userweave.domain.StudyState;
import com.userweave.domain.User;
import com.userweave.pages.configuration.ConfigurationPage;

/**
 * Panel to display a list of users and their respective rights
 * in this project. Other users can also write e-mails to and 
 * delete users.
 * 
 * @author opr
 */
public abstract class UserListPanel extends Panel
{	
	private static final long serialVersionUID = 1L;

	/**
	 * Fragment to display a label with the role
	 * of a user.
	 * 
	 * @author opr
	 */
	private class LabelFragment extends Fragment
	{
		private static final long serialVersionUID = 1L;
		
		/**
		 * Default constructor.
		 * 
		 * @param id
		 * 		Markup id.
		 * @param role
		 * 		Rolenae to display.
		 */
		public LabelFragment(String id, String role)
		{
			super(id, "labelFragment", UserListPanel.this);
		
			add(new Label("role", new StringResourceModel(role, UserListPanel.this, null)));
		}
	}
	
	/**
	 * Fragment to display a list choice to change a users
	 * role in the given project.
	 * Acitve only, when user is project adimin or particiant.
	 * 
	 * @author opr
	 */
	private class ListChoiceFragment extends Fragment
	{
		private static final long serialVersionUID = 1L;
		
		/**
		 * The selected role for a user.
		 */
		private Role role;

		public void setRole(Role role)
		{
			this.role = role;
		}

		/**
		 * Default constructor.
		 * 
		 * @param id
		 * 		Markup id.
		 * @param roles
		 * 		List of roles to display in the list choice.
		 * @param join
		 * 		The current join of the given project, the current
		 * 		proccesed user and his role in the given project.
		 * @param user
		 * 		Current session user.
		 */
		public ListChoiceFragment(
				String id, List<Role> roles, 
				final ProjectUserRoleJoin join, 
				final User user)
		{
			super(id, "listChoiceFragment", UserListPanel.this);
			
			setRole(join.getRole());
			
			
			Form form = new Form("form");
			
			add(form);
			
			final DropDownChoice choices = new DropDownChoice(
					"roleChoice", 
					new PropertyModel(ListChoiceFragment.this, "role"), 
					roles,
					new LocalizedRoleChoiceRenderer(UserListPanel.this));
			
			choices.add(new AjaxFormComponentUpdatingBehavior("onchange") 
			{	
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target) 
				{
					// check, if user still has his role.
					// needed, because change events from 
					// other users aren't delegated to the 
					// users session
					List<Role> userRoles = // userRoles are currently one element lists
						roleDao.getRolesByUserAndProject(user, join.getProject());
					
					if(user.isAdmin() || // super admin can change everything
					   (userRoles != null && 
					    !userRoles.isEmpty() && 
					    user.hasRole(userRoles.get(0).getRoleName())))
					{
						ProjectUserRoleJoin newJoin = 
							purjDao.createJoin(join.getProject(), join.getUser(), role);
						
						purjDao.save(newJoin);
						
						purjDao.delete(join);
						
						// reload user privileges, if the user changed
						// his rights.
						if(join.getUser().equals(user) && !user.isAdmin())
						{
							UserWeaveSession.get().getUser();
						}
						
						onAfterRoleChange(target);
					}
					else
					{
						error("NOT SAME ROLE ANYMORE!!!");
						
						target.addComponent(feedback);
					}			
				}
			});
			
			form.add(choices);
		}
	}
	
	/**
	 * A choice renderer to translate role names in a 
	 * specific locale.
	 * 
	 * @author opr
	 */
	private class LocalizedRoleChoiceRenderer implements IChoiceRenderer
	{
		private static final long serialVersionUID = 1L;
	
		/**
		 * Parent component reference to find translation
		 * of the given object.
		 */
		UserListPanel parent;
		
		/**
		 * Default constructor.
		 * 
		 * @param parent
		 * 		Parent component to lookup translation.
		 */
		public LocalizedRoleChoiceRenderer(UserListPanel parent)
		{
			this.parent = parent;
		}
		
		@Override
		public Object getDisplayValue(Object object) 
		{
			StringResourceModel srm = 
				new StringResourceModel(((Role) object).getRoleName(), parent, null);
			
			return srm.getObject();
		}

		@Override
		public String getIdValue(Object object, int index) 
		{
			return ((Role) object).getRoleName();
		}
	}
	
	@SpringBean
	private ProjectUserRoleJoinDao purjDao;
	
	@SpringBean
	private RoleDao roleDao;
	
	@SpringBean
	private StudyDao studyDao;
	
	private boolean isDeleted = false;
	
	private final FeedbackPanel feedback;
	
	/**
	 * Default constructor
	 * 
	 * @param id
	 * 		Markup id of component
	 * @param projectUserModel
	 * 		Purj list model
	 * @param selectedProject
	 * 		The current selected project
	 */
	public UserListPanel(
		String id, List<ProjectUserRoleJoin> projectUserList, 
		final Project selectedProject, String resourceKey) 
	{
		super(id);
		
		Label noUserLabel = new Label(
			"noUserLabel", new StringResourceModel(resourceKey, this, null));
		
		noUserLabel.setVisible(projectUserList == null || projectUserList.isEmpty());
		
		add(noUserLabel);
		
		add(new ListView("userList", projectUserList)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem item)
			{
				ProjectUserRoleJoin join = (ProjectUserRoleJoin) item.getModelObject();
				
				User user = join.getUser();
				
				// display name and email address of user
				String userName = user.getForename() + " " + user.getSurname();
				
				item.add(new Label("userName", userName));
				//item.add(new Label("userEmail", user.getEmail()));
				
				// add mailto link
				item.add(new ExternalLink("mailto", "mailto:" + user.getEmail()));
				
				
				// flag to indicate if the row in this list contains a
				// user, who has a project admin role.
				final boolean joinEqualsProjectAdmin = 
					join.getRole().getRoleName().equals(Role.PROJECT_ADMIN);
				
				final List<ProjectUserRoleJoin> projectAdmins = 
					purjDao.getProjectAdmins(selectedProject);
				
				final CustomModalWindow deleteModal = getDeleteModal("deleteModal", join);
				
				final CustomModalWindow unableToDeleteUserModal = 
					getUnableToDeleteModal("unableToDeleteModal");
				
				AjaxLink deleteLink = 
					getDeleteLink(
						deleteModal, unableToDeleteUserModal,
						joinEqualsProjectAdmin, projectAdmins.size(),
						selectedProject);
				
				item.add(deleteLink);
				item.add(deleteModal);
				item.add(unableToDeleteUserModal);
				
				
				// workflow logic
				User currentUser = UserWeaveSession.get().getUser();
				
				if(currentUser.hasRole(Role.PROJECT_ADMIN))
				{
					/*
					 * A project admin can delete everybody but himself,
					 * if he is the only project admin. Also he can change
					 * everybodies role, except his own, if he is the only 
					 * project admin.
					 */
					
					if(!joinEqualsProjectAdmin)
					{
						/*
						 * The rendered row is a user, who is not 
						 * a project admin.
						 */
						item.add(
							new ListChoiceFragment(
									"roleFragment", 
									roleDao.findAll(), 
									join,
									currentUser));
					}
					else
					{
						/*
						 * The rendered row is a user, who is a 
						 * project admin. Ensure, that at least
						 * two admins are there before the active
						 * user change the role of the row user.
						 */
						if(projectAdmins.size() > 1)
						{
							item.add(
								new ListChoiceFragment(
										"roleFragment", 
										roleDao.findAll(), 
										join,
										currentUser));
						}
						else
						{
							/*
							 * there is only one project admin and the
							 * current user can't change his role nor 
							 * delete him from this project.
							 */
							item.add(new LabelFragment(
								"roleFragment", join.getRole().getRoleName()));
							
							deleteLink.setVisible(false);
						}
					}
				}
				else if(currentUser.hasRole(Role.PROJECT_PARTICIPANT) &&
						!joinEqualsProjectAdmin) // <- participant can't change an admin
				{	
					/*
					 * A project participant can change roles of and 
					 * delete guests and other participants.
					 */
					
					List<Role> roles = roleDao.findAll();
					List<Role> newRoles = new ArrayList<Role>(roles.size() - 1);
					
					/*
					 *  Populate a list choice with all roles equal 
					 *  or lower to the current users role.
					 */
					for(Role r : roles)
					{
						if(!(r.getRoleName().compareTo(Role.PROJECT_ADMIN) == 0))
						{
							newRoles.add(r);
						}
					}
					
					// @see: #1313
					if(join.getUser().equals(currentUser))
					{
						item.add(
							new ListChoiceFragment(
									"roleFragment", 
									newRoles, 
									join,
									currentUser));
					}
					else
					{
						item.add(new LabelFragment("roleFragment", join.getRole().getRoleName()));
					}
					// a project participant can only delete
					// project guests or himself
					deleteLink.setVisible(
							//join.getRole().getRoleName().equals(Role.PROJECT_GUEST) || @see: #1313 
							join.getUser().equals(currentUser));
				}
				else
				{
					item.add(new LabelFragment("roleFragment", join.getRole().getRoleName()));
					
					// guests can always delete themselfs
					deleteLink.setVisible(join.getUser().equals(currentUser)); 
				}
			}
		});
		
		feedback = new FeedbackPanel("feedback");
		
		feedback.setOutputMarkupId(true);
		
		add(feedback);
	}
	
	private void addDeleteLinkToRow()
	{
		
	}
	
	private CustomModalWindow getUnableToDeleteModal(String id)
	{
		final CustomModalWindow unableToDeleteModal = new CustomModalWindow(id);
		
		unableToDeleteModal.setPageCreator(new ModalWindow.PageCreator()
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			public Page createPage()
			{
				return new UnableToDeleteWebPage(unableToDeleteModal);
			}
		});
		
		return unableToDeleteModal;
	}
	
	private CustomModalWindow getDeleteModal(String id, final ProjectUserRoleJoin join)
	{
		final CustomModalWindow delete = new CustomModalWindow(id);
		
		delete.setTitle(new StringResourceModel("deleteUser", this, null));
		
		delete.setPageCreator(new ModalWindow.PageCreator()
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			public Page createPage()
			{
				return new DeleteUserWebPage(join.getUser(), join.getProject(), delete)
				{
					@Override
					protected void onDelete(AjaxRequestTarget target)
					{
						deleteUser(join);
						
						isDeleted = true;
						
						delete.close(target);
					}
				};
			}
		});
		
		delete.setWindowClosedCallback(new ModalWindow.WindowClosedCallback()
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClose(AjaxRequestTarget target)
			{
				if(isDeleted)
				{
					if(join.getUser().equals(UserWeaveSession.get().getUser()))
					{
						UserWeaveSession.get().setProjectId(null);
					
						setResponsePage(ConfigurationPage.class);
					}
					else
					{
						onAfterDelete(target);
					}
					
					isDeleted = false;
				}
			}	
		});
		
		return delete;
	}
	
	private AjaxLink getDeleteLink(
		final ModalWindow deleteModal, 
		final ModalWindow unableToDeleteUserModal,
		final boolean joinEqualsProjectAdmin,
		final int numberOfProjectAdmins,
		final Project project)
	{
		AjaxLink deleteLink = new AjaxLink("delete")
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick(AjaxRequestTarget target)
			{
				// A user, who is project admin and the only one
				// who is registered, can't delete himself from
				// the project as long as not all studies are either
				// finished or in init state. See #974
//				if(joinEqualsProjectAdmin &&
//				   !isAtLeastOneOtherAdminRegistered(project, user) &&
//				   runningStudiesExistsInProject(project))
				
				// an admin can only be deleted, if either there
				// are other admins or no running studies exist in
				// project.
				if(joinEqualsProjectAdmin && 
				   numberOfProjectAdmins <= 1 && 
				   runningStudiesExistsInProject(project))
				{
					unableToDeleteUserModal.show(target);
				}
				else
				{
					deleteModal.show(target);
				}
			}	
		};
		
		return deleteLink;
	}
	
	// temporary not in use, because registered is not needed.
//	private boolean isAtLeastOneOtherAdminRegistered(final Project project, final User user)
//	{
//		List<ProjectUserRoleJoin> projectAdmins = 
//			purjDao.getProjectAdmins(project);
//		
//		boolean atLeastOneOtherAdminIsRegistered = false;
//		
//		for(ProjectUserRoleJoin projectAdmin : projectAdmins)
//		{
//			if(projectAdmin.getUser().isRegistered() && 
//			   !projectAdmin.getUser().equals(user))
//			{
//				atLeastOneOtherAdminIsRegistered = true;
//				break;
//			}
//		}
//		
//		return atLeastOneOtherAdminIsRegistered;
//	}
	
	private void deleteUser(ProjectUserRoleJoin join)
	{
		purjDao.delete(join);
	}
	
	private boolean runningStudiesExistsInProject(Project project)
	{
		return studyDao.findByProjectAndState(project, false, StudyState.RUNNING).size() > 0;
	}

	protected abstract void onAfterDelete(AjaxRequestTarget target);
	
	protected abstract void onAfterRoleChange(AjaxRequestTarget target);
}
