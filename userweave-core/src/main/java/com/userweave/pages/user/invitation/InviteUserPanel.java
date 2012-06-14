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
package com.userweave.pages.user.invitation;

import java.util.List;
import java.util.Locale;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.application.UserWeaveSession;
import com.userweave.components.MailMessageProviderImpl;
import com.userweave.components.localerenderer.LocaleChoiceRenderer;
import com.userweave.domain.Invitation;
import com.userweave.domain.User;
import com.userweave.domain.service.UserService;
import com.userweave.pages.base.BaseUserWeavePage;
import com.userweave.pages.test.jquery.JQuery;
import com.userweave.pages.user.RegisterUser;
import com.userweave.utils.LocalizationUtils;

/**
 * @author ipavkovic
 */
@SuppressWarnings("serial")
public abstract class InviteUserPanel extends Panel {

	private String email;
	
	private Locale locale;

	private Component feedbackPanel;
	
	@SpringBean
	private UserService userService;
	
	public InviteUserPanel(String id) 
	{
		super(id);
		
		add(feedbackPanel = new FeedbackPanel("feedback").setOutputMarkupId(true));
		
		Form form = new Form("form", new CompoundPropertyModel(this));

		add(form);

		List<Invitation> invitations = UserWeaveSession.get().getUser().getInvitations();
		
		email = new StringResourceModel("label_email_textfield", this, null).getString();
		
		form.add(new TextField("email").setRequired(true));
		
		form.add(new RadioChoice("locale", 
				LocalizationUtils.getSupportedConfigurationFrontendLocales(), 
				new LocaleChoiceRenderer(getLocale())).setSuffix("<span>&nbsp;&nbsp;</span>"));
		
		form.add(new AjaxButton("submit") {
			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form form) {
				target.add(feedbackPanel);
				invite(target);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form form) {
				target.add(feedbackPanel);
			}
		});
	}

	public void invite(AjaxRequestTarget target) {
		if(email != null) {
			email = email.toLowerCase();
		}
		
		if(!RegisterUser.checkMail(email)) {
			// FIXME: I18N
			error("email_incorrect_pattern");
			return;
		} else {
			
			final User userx = userService.findByEmail(email);
			if(userx != null) {
				// FIXME: I18N
				error("user_already_exists");
				return;
			}
		}
		
		userService.sendInvitationMail(UserWeaveSession.get().getUser(), email, locale, new MailMessageProviderImpl(this) {
			
			@Override
			public String getUrl(Component component, String token, Locale locale) {
				
				PageParameters parameters = new PageParameters();
				parameters.set(0, token);
				parameters.set(1, LocalizationUtils.getLocaleShort(locale)); 

				//return component.urlFor(Session.get().getDefaultPageMap(), RegisterUser.class, parameters).toString();
				return component.urlFor(RegisterUser.class, parameters).toString();
			}
			
			@Override
			public String getMailSubject() {
				User user = UserWeaveSession.get().getUser();
				
				return new StringResourceModel("subject_invite", InviteUserPanel.this, null, 
						new Object[] {
							user.getForename(),
							user.getSurname()
						}).getString();
			}
			
			@Override
			public String getMailMessage(String urlStr) {
				User user = UserWeaveSession.get().getUser();
				
				return new StringResourceModel("mail_invite", InviteUserPanel.this, null, new Object[] {
						user.getForename(),
						user.getSurname(),
						urlStr
					}).getString();
			}
		});
		
		onInvite(target);
	}

	@Override
	public void renderHead(IHeaderResponse response)
	{
		response.renderJavaScriptReference(JQuery.getLatest());
		
		response.renderJavaScriptReference(
				new JavaScriptResourceReference(
					BaseUserWeavePage.class,
					"hover.js"));
	}
	
	protected abstract void onInvite(AjaxRequestTarget target);
	
}

