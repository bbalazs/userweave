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


import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.application.OnFinishCallbackWithReturnValue;
import com.userweave.application.UserWeaveSession;
import com.userweave.domain.User;
import com.userweave.domain.service.UserService;
import com.userweave.pages.base.BaseUserWeavePage;
import com.userweave.pages.components.button.DefaultButton;
import com.userweave.pages.login.SigninPage;

@SuppressWarnings("serial")
public class ChangePassword extends BaseUserWeavePage {
	
	@SpringBean
	private UserService userService;
	private final OnFinishCallbackWithReturnValue<Page> onFinishCallback;
	
	public ChangePassword(OnFinishCallbackWithReturnValue<Page> onFinishCallbackWithLoginCheck) {
		this.onFinishCallback = onFinishCallbackWithLoginCheck;
		add(new ChangePasswordForm("passwordForm"));
		add(new FeedbackPanel("feedback").setOutputMarkupId(true));
	}

	@Override
	public void renderHead(IHeaderResponse response)
	{
		response.renderCSSReference(new PackageResourceReference(
				SigninPage.class, "signin.css"));
	}
	
	private class ChangePasswordForm extends StatelessForm {

		private final String oldpassword=null;
		private String password;
		private String password2;

		public ChangePasswordForm(String id) {
			super(id);

			setModel(new CompoundPropertyModel(this));
			PasswordTextField oldPasswordTextField = new PasswordTextField("oldpassword");
			Label oldPasswordLabel = new Label("old_password_label",
					new StringResourceModel("old_password", this, null));
			
			boolean isOldPasswordVisible = 
				UserWeaveSession.get().getUser() != null && 
				UserWeaveSession.get().getUser().getPassword()!=null;
			
			oldPasswordLabel.setVisibilityAllowed(isOldPasswordVisible);
			add(oldPasswordLabel);
			
			oldPasswordTextField.setVisibilityAllowed(isOldPasswordVisible);
			add(oldPasswordTextField.setRequired(true));
			
			add(new WebMarkupContainer("old_password_label_spacer")
					.add(new SimpleAttributeModifier("class", "old_password_spacer"))
					.setVisibilityAllowed(isOldPasswordVisible));
			add(new WebMarkupContainer("old_password_input_spacer")
					.add(new SimpleAttributeModifier("class", "old_password_spacer"))
					.setVisibilityAllowed(isOldPasswordVisible));
			
			
			PasswordTextField passwordTextField = new PasswordTextField("password");
			add(passwordTextField.setRequired(true));
			
			PasswordTextField repeatedPasswordTextField = new PasswordTextField("password2");
			add(repeatedPasswordTextField.setRequired(true));

			add(new EqualPasswordInputValidator(passwordTextField, repeatedPasswordTextField));

			add(new DefaultButton("saveButton", new StringResourceModel("submit", this, null), this) {

				@Override
				protected void onError(AjaxRequestTarget target, Form form) {
					target.add(ChangePassword.this.get("feedback"));
				}
			});
			
		}
		
		@Override
		public final void onSubmit() {
			changePassword(oldpassword,password,password2);
			if(onFinishCallback != null) {
				onFinishCallback.onFinish(ChangePassword.this);
			}
		}

	}
	
	private boolean changePassword(String oldPassword, String password, String password2) {
		User user = UserWeaveSession.get().getUser();
		if(oldPassword != null) {
			if(!userService.validatePassword(user, oldPassword)) {
				error("Old Password is wrong. Cancelled Password change.");
				return false;
			}
			if(oldPassword.equals(password)) {
				error("Old password and new password do not differ. Cancelled password change.");
				return false;
			}
		}
		if(password.length() < 3) {
			error("new passwords are too short. Please choose new ones.");
			return false;
		}
		userService.changePassword(user, password);
		return true;
	
	}
}
