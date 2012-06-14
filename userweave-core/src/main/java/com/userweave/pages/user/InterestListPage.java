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

import javax.mail.MessagingException;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.domain.User;
import com.userweave.domain.service.UserService;
import com.userweave.domain.service.mail.MailService;
import com.userweave.pages.base.BaseUserWeavePage;
import com.userweave.pages.components.button.DefaultButton;

/**
 * 
 * @author opr
 *
 */
public class InterestListPage extends WebPage {
	@SpringBean
	private MailService mailService;
	
	@SpringBean
	private UserService userService;
	
	private String email;
	
	public InterestListPage(final ModalWindow modal) {
		
		final Component feedback;
		add(feedback = new FeedbackPanel("feedback").setOutputMarkupId(true));
		
		Form form = new Form("form", new CompoundPropertyModel(this));
		
		add(form);
		
		form.add(new TextField("email").setRequired(true));
		
		form.add(new DefaultButton("submitButton", new Model("submit"), form)
		{
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form form) {
				target.addComponent(feedback);
				sendInterestMail(modal, target);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form form) {
				target.addComponent(feedback);
			}
		
		});
	}
	
	@Override
	public void renderHead(IHeaderResponse response)
	{
		response.renderCSSReference(new PackageResourceReference(
				BaseUserWeavePage.class, "configBase.css"));
	}
	
	private void sendInterestMail(ModalWindow modal, AjaxRequestTarget target)
	{
		if(!RegisterUser.checkMail(email)) 
		{
			error("email_incorrect_pattern");
			return;
		} 
		else 
		{
			final User userx = userService.findByEmail(email);
			if(userx != null) 
			{
				error("user_already_exists");
				return;
			}
		}
		
		String message = "Interessent: " + email;
		
		try {
			mailService.sendMail(email, "Interest Mail", "Danke für ihr Interesse", "info@user-weave.net");
			
			modal.close(target);
		} 
		catch (MessagingException e) 
		{
			error("Konnte Mail nicht versenden");
		}
	}
}
