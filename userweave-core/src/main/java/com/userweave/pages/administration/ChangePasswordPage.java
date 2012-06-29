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
package com.userweave.pages.administration;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.application.UserWeaveSession;
import com.userweave.components.customModalWindow.BaseModalWindowPage;
import com.userweave.domain.User;
import com.userweave.domain.service.UserService;

public class ChangePasswordPage extends BaseModalWindowPage
{
	@SpringBean
	private UserService userService;
	
	private String oldPasswordString, newPasswordString;
	//, newPasswordRetypeString;
	
	final FeedbackPanel feedback;
	
	
	public ChangePasswordPage(final ModalWindow window)
	{
		super(window);
		
		feedback = new FeedbackPanel("feedback");
		
		feedback.setOutputMarkupId(true);
		
		addToForm(feedback);
		
		
		PasswordTextField oldPassword = 
			new PasswordTextField(
					"oldPassword",
					new PropertyModel(ChangePasswordPage.this, "oldPasswordString"));
		
		addToForm(oldPassword);
		
		PasswordTextField newPassword = 
			new PasswordTextField(
					"newPassword",
					new PropertyModel(ChangePasswordPage.this, "newPasswordString"));
		
		addToForm(newPassword);
		
//		PasswordTextField newPasswordRetype = 
//			new PasswordTextField(
//					"newPasswordRetype",
//					new PropertyModel(ChangePasswordPage.this, "newPasswordRetypeString"));
//		
//		addToForm(newPasswordRetype);
		
	}
	
	@Override
	protected WebMarkupContainer getAcceptButton(String componentId,
			final ModalWindow window)
	{
		return new AjaxButton(componentId, getForm())
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form form)
			{
				if(changePassword(oldPasswordString, newPasswordString))
				{
					window.close(target);
				}
				else
				{
					target.add(feedback);
				}
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form)
			{
			}
		};
	}
	
	@Override
	protected IModel getAcceptLabel()
	{
		return new StringResourceModel("changePasswd", this, null);
	}
	
	private boolean changePassword(String oldPassword, String password) 
	{
		User user = UserWeaveSession.get().getUser();
		
		if(oldPassword != null) 
		{
			if(! userService.validatePassword(user, oldPassword)) 
			{
				error(new StringResourceModel(
						"wrongOldPassword", 
						ChangePasswordPage.this, 
						null).getString());
			
				return false;
			}
			
			if(oldPassword.equals(password)) 
			{
				error(new StringResourceModel(
						"passwordsNotDiffer", 
						ChangePasswordPage.this, 
						null).getString());
			
				return false;
			}
		}
		else
		{
			return false;
		}
//		
//		if(! password.equals(password2)) 
//		{
//			error(new StringResourceModel(
//				"notSamePassword", 
//				ChangePasswordPage.this, 
//				null).getString());
//			
//			return false;
//		}
		
		if(password.length() < 3) 
		{
			error(new StringResourceModel(
					"passwordToShort", 
					ChangePasswordPage.this, 
					null).getString());
			
			return false;
		}
		
		userService.changePassword(user, password);
		
		return true;
	}
}
