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
package com.userweave.pages.user.verification;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.application.UserWeaveSession;
import com.userweave.components.customModalWindow.CustomModalWindow;
import com.userweave.dao.UserDao;
import com.userweave.domain.User;
import com.userweave.domain.service.UserService;
import com.userweave.presentation.model.UserModel;

public abstract class UserVerificationPanel extends Panel 
{
	private static final long serialVersionUID = 1L;

	@SpringBean
	private UserDao userDao;
	
	@SpringBean
	private UserService userService;
	
	public UserVerificationPanel(String id, User user) 
	{
		super(id,new UserModel(user));
		
		setOutputMarkupId(true);
		
		initPanel();
	}

	private void initPanel() 
	{	
		ModalWindow agbModalWindow = createAgbModal();
		
		add(agbModalWindow);
		
		add(new UserVerificationForm("userForm", getDefaultModel(), agbModalWindow) 
		{
			private static final long serialVersionUID = 1L;

				@Override
				public void onSave(AjaxRequestTarget target) 
				{
					final User user = getUser();
					userDao.save(user);
				
					// locale data may have changed
					UserWeaveSession.get().setLocaleForUser();
					
					// user is verified *here* so we copy all demo studies into his account
					// FIXME: move this call into a background job?
					userService.copyStudiesOfDefaultUser(user);
					UserVerificationPanel.this.onSave(user, target);
				}
			}
		);
	}

	/**
	 * @return
	 */
	protected ModalWindow createAgbModal()
	{
		final ModalWindow agbModalWindow = new CustomModalWindow("agbModalWindow");
		
		agbModalWindow.setInitialHeight(550);
		agbModalWindow.setInitialWidth(600);
		
		agbModalWindow.setPageCreator(new ModalWindow.PageCreator() {
            public Page createPage() {
                return new TermsOfUsePage(agbModalWindow);
            }
        });
		
		return agbModalWindow;
	}
	
	private User getUser() {
		return (User) getDefaultModelObject();
	}

	public abstract void onSave(User user, AjaxRequestTarget target);
}
