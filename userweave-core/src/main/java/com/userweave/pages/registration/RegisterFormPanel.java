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
package com.userweave.pages.registration;

import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.components.MailMessageProviderImpl;
import com.userweave.dao.UserDao;
import com.userweave.domain.User;
import com.userweave.domain.service.SecurityTokenService;
import com.userweave.domain.service.UserService;

public abstract class RegisterFormPanel extends Panel {

	@SpringBean
	protected UserService userService;
	
	@SpringBean
	protected UserDao userDao;
	
	@SpringBean
	protected SecurityTokenService securityTokenService;
	
	private String forename, lastname, email, password;
	
	private boolean checked;


	public RegisterFormPanel(String id) {
		super(id);
		
		checked = false;
		
		 // Add a FeedbackPanel for displaying our messages
        FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");

		
        setDefaultModel(new CompoundPropertyModel(RegisterFormPanel.this));
		
		Form<Void> form = new Form<Void>("form")
		{
			@Override
			protected void onSubmit() 
			{
				User user = userService.createUser(email, getSession().getLocale());
				
				user.setForename(forename);
				
				user.setSurname(lastname);
				
				user.setPasswordMD5(password);
								
				user.setVerified(true);
				
				userDao.save(user);
				
				userService.sendRegisterMail(email, getSession().getLocale(),new MailMessageProviderImpl(this) 
				{
					
					@Override
					public String getMailSubject() {
						return new StringResourceModel("mail_subject", RegisterFormPanel.this, null).getString();
					}
					
					@Override
					public String getMailMessage(final String urlStr) {
						return new StringResourceModel("mail_message", RegisterFormPanel.this, null, new Object[] {
							email,
							urlStr
						}).getString();
					}
				});
				
				onAfterSubmit();
			}
		};
		
		add(form);
		
		form.add(new TextField<String>("forename").setRequired(true).setLabel(new Model(new StringResourceModel("name", RegisterFormPanel.this, null).getString())));

		form.add(new TextField<String>("lastname").setRequired(true).setLabel(new Model(new StringResourceModel("surname", RegisterFormPanel.this, null).getString())));

		form.add(new TextField<String>("email").setRequired(true).setLabel(new Model(new StringResourceModel("mail", RegisterFormPanel.this, null).getString())));		
		
		form.add(new PasswordTextField("password").setLabel(new Model(new StringResourceModel("password", RegisterFormPanel.this, null).getString())));	
		
		form.add(new CheckBox("checked").setRequired(true));
		
		form.add(feedbackPanel);
		
	}
	
	
	protected abstract void onAfterSubmit();
}
