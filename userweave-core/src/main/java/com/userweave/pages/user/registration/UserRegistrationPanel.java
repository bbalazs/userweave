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
package com.userweave.pages.user.registration;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.application.UserWeaveSession;
import com.userweave.components.customModalWindow.CustomModalWindow;
import com.userweave.dao.ProjectInvitationDao;
import com.userweave.dao.ProjectUserRoleJoinDao;
import com.userweave.dao.RoleDao;
import com.userweave.dao.UserDao;
import com.userweave.domain.User;
import com.userweave.pages.user.verification.TermsOfUsePage;
import com.userweave.presentation.model.UserModel;

@SuppressWarnings("serial")
public abstract class UserRegistrationPanel extends Panel {

	@SpringBean
	private UserDao userDao;

	@SpringBean
	private ProjectUserRoleJoinDao purjDao;
	
	@SpringBean
	private ProjectInvitationDao projectInvitationDao;
	
	@SpringBean
	private RoleDao roleDao;
	
	public UserRegistrationPanel(String id, User user) {
		super(id, new UserModel(user));
		
		setOutputMarkupId(true);
		
		initPanel();
	}

	private void initPanel() {
		
		final CustomModalWindow agbModalWindow = new CustomModalWindow("agbModalWindow");
		
		agbModalWindow.setInitialHeight(550);
		agbModalWindow.setInitialWidth(600);
		
		agbModalWindow.setPageCreator(new ModalWindow.PageCreator() {
            public Page createPage() {
                return new TermsOfUsePage(agbModalWindow);
            }
        });
		
		add(agbModalWindow);
		
		add(new UserRegistrationForm("userForm", getDefaultModel(), agbModalWindow) {

				@Override
				public void onSave(AjaxRequestTarget target) {
					final User user = getUser();
					userDao.save(user);
					
//					List<ProjectInvitation> invitations = 
//						projectInvitationDao.findByEmail(user.getEmail());
//					
//					for(ProjectInvitation invitation : invitations)
//					{
//						ProjectUserRoleJoin newJoin = 
//							purjDao.createJoin(
//									invitation.getProject(), 
//									user, 
//									roleDao.findByName(invitation.getRole().getRoleName()));
//						
//						purjDao.save(newJoin);
//						
//						projectInvitationDao.delete(invitation);
//					
//					}
					
					UserWeaveSession.get().setLocaleForUser();
					UserRegistrationPanel.this.onSave(user, target);
				}
			}
		);
	}
	
	private User getUser() {
		return (User) getDefaultModelObject();
	}

	public abstract void onSave(User user, AjaxRequestTarget target);
}
