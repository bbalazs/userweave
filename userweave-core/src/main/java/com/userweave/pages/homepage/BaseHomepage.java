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
package com.userweave.pages.homepage;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.StatelessLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.application.UserWeaveSession;
import com.userweave.domain.User;
import com.userweave.domain.SecurityToken.SecurityTokenType;
import com.userweave.domain.service.UserService;
import com.userweave.pages.base.BaseUserWeavePage;
import com.userweave.pages.login.LoginPage;
import com.userweave.pages.registration.Registerpage;
import com.userweave.pages.user.ResetPassword;
import com.userweave.utils.LocalizationUtils;

/**
 * Base page for the public accessable pages like 
 * start.html, imprint.html, etc.
 * 
 * @author opr
 *
 */
public class BaseHomepage extends WebPage 
{		
	private static final long serialVersionUID = 1L;
	
	@SpringBean
	private UserService userService;
	
	public BaseHomepage()
	{	
		add(new BookmarkablePageLink<Void>("base_start", getApplication().getHomePage()));
	

		init();

		
		add(new BookmarkablePageLink<Void>("base_imprint", ImprintPage.class));
			
		add(new StatelessLink<Void>("switchToGerman")
		{
			private static final long serialVersionUID = 1L;
			
			@Override 
			public void onClick() {
				getSession().setLocale(LocalizationUtils.getSupportedConfigurationFrontendLocale("de"));
			}
		});
		
		add(new StatelessLink<Void>("switchToEnglish")
		{
			private static final long serialVersionUID = 1L;

			@Override 
			public void onClick() 
			{
				getSession().setLocale(LocalizationUtils.getSupportedConfigurationFrontendLocale("en"));
			}
		});

		WebMarkupContainer metaDescription = new WebMarkupContainer("metaDescription");
		add(metaDescription);
		metaDescription.add(new AttributeModifier("content", new StringResourceModel("metaDescription", this, null)));
	}

	@Override
	public void renderHead(IHeaderResponse response)
	{
		response.renderCSSReference(new PackageResourceReference(
				BaseUserWeavePage.class, "configBase.css"));
		
		response.renderCSSReference(new PackageResourceReference(
				BaseHomepage.class, "whiteFrame.css"));
	}
	
	
	
	private void init() 
	{
			add(new SignInForm("signInForm"));
			
			add(new FeedbackPanel("feedback").setOutputMarkupId(true));
			
			add(new BookmarkablePageLink<Void>("forgot_password",ResetPassword.class));
			//add(new BookmarkablePageLink<Void>("memberLink", BecomeMember.class));
			add(new BookmarkablePageLink<Void>("register",Registerpage.class).setEnabled(true));

		
	}

	protected static void stopInterception(Page page) 
	{
		// throw back responsibility to AuthorizationStrategy
		if (!page.continueToOriginalDestination()) 
		{	
			page.setResponsePage(page.getApplication().getHomePage());                           
		}
	}
	
	/**
	 * Simple stateless form. Contains a password and a text field
	 * to enter user authentication data.
	 * 
	 * @author opr
	 *
	 * @param <T>
	 */
	private class SignInForm<T> extends StatelessForm<T>
	{
		private static final long serialVersionUID = 1L;

		private String username;
	
		private String password;

		public SignInForm(String id) 
		{
			super(id);
			
			setModel(new CompoundPropertyModel(this));
			
			add(new TextField("username"));
			add(new PasswordTextField("password"));
		}
		
		@Override
		public final void onSubmit() 
		{
			doSignIn(username, password);
		}
	}
	
	private boolean doSignIn(String username, String password) 
	{
		User user = signInAndErrorOnFailure(username, password);
		if(user!=null) {
			signInAndContinue(user, false);
		}
		return user != null;
	}
	
	private void signInAndContinue(User user, boolean code) 
	{
		UserWeaveSession.get().setUserId(user.getId());
		
		if(code) {
			setResponsePage(LoginPage.class);
		} else {
			stopInterception(this);
		}
	}

	private boolean doSignIn(String verifyCode) {
		User user = signInAndErrorOnFailure(verifyCode);
		if(user!=null) {
			signInAndContinue(user,true);
		}
		return user != null;
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
	private User signInAndErrorOnFailure(String token) {
		if(token==null) {
			return null;
		}

		User user = userService.validateToken(token, SecurityTokenType.RESET_PASSWORD, SecurityTokenType.VERIFY_USER);
		if(user==null) {
			// FIXME: i18n
			error ("Invalid security token.");
			return null;
		}
		
		return user;
	}
}
