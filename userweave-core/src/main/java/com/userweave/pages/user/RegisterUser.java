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

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.components.MailMessageProviderImpl;
import com.userweave.domain.SecurityToken;
import com.userweave.domain.User;
import com.userweave.domain.SecurityToken.SecurityTokenType;
import com.userweave.domain.service.SecurityTokenService;
import com.userweave.domain.service.UserService;
import com.userweave.pages.base.BaseUserWeavePage;
import com.userweave.pages.components.button.DefaultButton;
import com.userweave.pages.login.SigninPage;
import com.userweave.pages.test.DisplaySurveyUI;
import com.userweave.utils.LocalizationUtils;

public class RegisterUser extends BaseUserWeavePage 
{
	private static final long serialVersionUID = 1L;

	@SpringBean
	protected UserService userService;

	@SpringBean
	protected SecurityTokenService securityTokenService;

	protected RegisterUserForm<Void> registerForm;
	
	protected RegisterPanel registerPanel;
	
	protected Component component;
	
	
	public RegisterUser(PageParameters parameters) 
	{
		String verifyCode = parameters.get(0).toString();
		
		Locale locale = DisplaySurveyUI.getLocale(parameters);
		if(locale != null) {
			Session.get().setLocale(locale);
		}
		
		init(verifyCode);
	}

	public RegisterUser() 
	{
		init(null);
	}
	
	protected void init(String code) {
		
		registerForm = new RegisterUserForm<Void>("registerForm", true);
		
		registerPanel = new RegisterPanel("registerPanel", registerForm);
		
		add(component = registerPanel);
		
		registerPanel.add(registerForm);
		
		registerForm.code = code;
		SecurityToken token = validateToken(code);
		if(token != null && token.getInvitation() != null) {
			registerForm.email = token.getInvitation().getEmail();
		}
	}
	
	private SecurityToken validateToken(String code) {
		if(code == null) {
			return null;
		}
		return securityTokenService.validateToken(code, SecurityTokenType.INVITE_USER);
	}

	protected class RegisterUserForm<T> extends StatelessForm<T> 
	{
		private static final long serialVersionUID = 1L;

		private String email;
		
		private String code;
		
		public RegisterUserForm(String id, boolean codeNeeded) 
		{
			super(id);
			
			setModel(new CompoundPropertyModel(this));
			
			add(new TextField<String>("email").setRequired(true));
			add(new TextField<String>("code").setRequired(codeNeeded).setVisible(codeNeeded));
			add(new DefaultButton(
					"saveButton", 
					new StringResourceModel("button_text", this, null), 
					this) 
			{
				private static final long serialVersionUID = 1L;
				
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form form) 
				{
					if (register(email, code))
					{
						target.add(component);
					}
					else
					{
						target.add(registerPanel.get("feedback"));
					}
				}
				
				@Override
				protected void onError(AjaxRequestTarget target, Form form) 
				{
					target.add(registerPanel.get("feedback"));
				}
				
			});
		}

	}

	@Override
	public void renderHead(IHeaderResponse response)
	{
		response.renderCSSReference(new PackageResourceReference(
				SigninPage.class, "signin.css"));
	}
	
	public static boolean checkMail(String email) {
		if(email == null) {
			return false;
		}
		//	from   http://www.mkyong.com/java/how-to-validate-email-address-in-java-regular-expression/
		Pattern mail = Pattern.compile( "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*((\\.[A-Za-z]{2,}){1}$)" );
		Matcher m = mail.matcher(email);
		return m.matches();
	}
	
	protected boolean register(final String email, final String code) {
		SecurityToken token = validateToken(code);
		if(token == null) {
			error(new StringResourceModel("illegal_token", this, null).getString());
			return false;
		} else {
			return register(email, token);
		}
	}

	protected boolean register(final String email, SecurityToken token) {
		if(!checkMail(email)) {
			error(new StringResourceModel(
					"email_incorrect_pattern", this, null).getString());
			return false;
		} else {
			final User userx = userService.findByEmail(email);
			if(userx != null) {
				error(new StringResourceModel("user_already_exists", this, null).getString());
				return false;
			} else {	
				userService.sendRegisterMail(
					email, 
					LocalizationUtils.getRegistrationLocaleShort(getSession().getLocale()), 
					new MailMessageProviderImpl(this) 
				{
						
						@Override
						public String getMailSubject() {
							return new StringResourceModel("subject_register", RegisterUser.this, null).getString();
						}
						
						@Override
						public String getMailMessage(final String urlStr) {
							return new StringResourceModel("subject_mail", RegisterUser.this, null, new Object[] {
								email,
								urlStr
							}).getString();
						}
					});
				
					
					Component newComponent =new RegisterFinishedPanel("registerPanel"); 
					component.replaceWith(newComponent);
					component = newComponent;
					
					securityTokenService.deleteToken(token);
					
					return true;
			}
		}
	}
}
