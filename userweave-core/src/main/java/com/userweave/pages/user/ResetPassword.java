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
package com.userweave.pages.user;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.resource.PackageResourceReference;

import com.userweave.components.MailMessageProviderImpl;
import com.userweave.domain.User;
import com.userweave.pages.login.SigninPage;

public class ResetPassword extends RegisterUser {

	public ResetPassword() {
	}

	@Override
	protected void init(String code) {
		
		registerForm = new RegisterUserForm("registerForm", false);
		
		registerPanel = new ResetPasswordPanel("registerPanel", registerForm);
		
		add(component = registerPanel);
		
		registerPanel.add(registerForm);
		
	}

	@Override
	public void renderHead(IHeaderResponse response)
	{
		response.renderCSSReference(new PackageResourceReference(
				SigninPage.class, "signin.css"));
	}
	
	@Override
	protected boolean register(final String email, final String code) {
		// code check is done in SigninPage
		return register(email);
	}
	
	protected boolean register(String email) {
		if(!checkMail(email)) {
			error(new StringResourceModel(
					"email_incorect_pattern", this, null).getString());
			return false;
		} else {
			final User user = userService.findByEmail(email);
			if(user == null) {
				error(new StringResourceModel(
						"user_not_exists", this, null).getString());
				return false;
			} else {
				userService.sendResetPasswordMail(user,new MailMessageProviderImpl(this) {
					
					@Override
					public String getMailSubject() {
						return new StringResourceModel(
								"subject_reset", ResetPassword.this, null).getString();
					}
					
					@Override
					public String getMailMessage(String urlStr) {
						return 
							new StringResourceModel(
									"new_password_first_part", ResetPassword.this, null).getString() +
							
							" " + user.getEmail() + " " +
							
							new StringResourceModel(
									"new_password_second_part", ResetPassword.this, null).getString() + 
							"\n\n" + 
							new StringResourceModel(
									"reset_password", ResetPassword.this, null).getString() +
							"\n" +
							urlStr + 
							"\n\n" +
							new StringResourceModel(
									"link_notice", ResetPassword.this, null).getString() +
							"\n\n" +
							new StringResourceModel(
									"dont_want", ResetPassword.this, null).getString() + 
							"\n\n" +
							new StringResourceModel(
									"remaining_questions", ResetPassword.this, null).getString() + 
							"\n\n" +
							new StringResourceModel(
									"thank_you", ResetPassword.this, null).getString() + 
							"\n" +
							new StringResourceModel(
									"team", ResetPassword.this, null).getString();
					}
				});
				
				Component newComponent =new ResetPasswordFinishedPanel("registerPanel"); 
				component.replaceWith(newComponent);
				component = newComponent;
				
				return true;
			}
		}
	}
}