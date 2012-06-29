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
package com.userweave.pages.api;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.application.UserWeaveSession;
import com.userweave.components.MailMessageProviderImpl;
import com.userweave.domain.User;
import com.userweave.domain.service.UserService;
import com.userweave.pages.login.LoginPage;
import com.userweave.pages.user.RegisterFinishedPanel;
import com.userweave.pages.user.RegisterUser;
import com.userweave.pages.user.ResetPassword;
import com.userweave.utils.LocalizationUtils;

public class LoginRegisterPanel extends Panel {
	
	@SpringBean
	private UserService userService;
	
	private String username;
	
	private String password;
	
	private String email;
	
	public LoginRegisterPanel(String id)
	{
		super(id);
		
		add(new FeedbackPanel("feedback"));
		
		add(new BookmarkablePageLink("forgot_password",ResetPassword.class));
		
		Form registerForm = new Form("register_form")
		{
			@Override
			protected void onSubmit()
			{
				register(email);
			}
		};
		
		registerForm.add(new TextField("register_text", new PropertyModel(this, "email")).setRequired(true));
		
		add(registerForm);
		
		Form loginForm = new Form("login_form")
		{
			@Override
			protected void onSubmit()
			{
				doSignIn(username, password);
			}
		};
		
		loginForm.add(new TextField("login_text", new PropertyModel(this, "username")).setRequired(true));
		
		loginForm.add(new PasswordTextField("password", new PropertyModel(this, "password")).setRequired(true));
		
		add(loginForm);
	}
	
	private boolean doSignIn(String username, String password) {
		User user = signInAndErrorOnFailure(username, password);
		if(user!=null) {
			signInAndContinue(user, false);
		}
		return user != null;
	}
	
	private void signInAndContinue(User user, boolean code) {
		UserWeaveSession.get().setUserId(user.getId());
		
		if(code) 
		{
			setResponsePage(LoginPage.class);
		} 
		else 
		{
			stopInterception(this.getPage());
		}
	}
	
	private User signInAndErrorOnFailure(String username, String password) {
		if(username == null ||username.trim().equals("") || 
				password == null || password.trim().equals("")) {
			error(new StringResourceModel("too_few", this, null).getString());
			
			return null;
		}
		
		User user = userService.findByEmail(username);
		
		StringResourceModel srm = new StringResourceModel(
				"unknown_user_password", this, null);
		StringResourceModel retype = new StringResourceModel(
				"retype", this, null);
		
		if(user == null) {
			error(srm.getString());
			error(retype.getString());
			return null;
		}

		if (!userService.validatePassword(user, password)) {
			error(srm.getString());
			error(retype.getString());
			return null;
		}
		
		if(user.isDeactivated()) {
			error(new StringResourceModel("account_disabled", this, null).getString());
			return null;
		}
			
		return user;
	}
	
	protected static void stopInterception(Page page) {
		// throw back responsibility to AuthorizationStrategy
		if (!page.continueToOriginalDestination()) {                                            
			page.setResponsePage(page.getApplication().getHomePage());                           
		}
	}
	
	protected boolean register(final String email) {
		if(!RegisterUser.checkMail(email)) {
			error(new StringResourceModel(
					"email_incorrect_pattern", this, null).getString());
			return false;
		} else {
			final User userx = userService.findByEmail(email);
			if(userx != null) {
				error(new StringResourceModel("user_already_exists", this, null).getString());
				return false;
			} else {	
				userService.sendRegisterMail(email, LocalizationUtils.getRegistrationLocaleShort(getSession().getLocale()), new MailMessageProviderImpl(this) {
						
						@Override
						public String getMailSubject() {
							return new StringResourceModel("subject_register", LoginRegisterPanel.this, null).getString();
						}
						
						@Override
						public String getMailMessage(final String urlStr) {
							return new StringResourceModel("subject_mail", LoginRegisterPanel.this, null, new Object[] {
								email,
								urlStr
							}).getString();
						}
					});
				
					
					Component newComponent =new RegisterFinishedPanel(this.getId()); 
					this.replaceWith(newComponent);
					
					return true;
			}
		}
	}
}
