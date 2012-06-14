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
package com.userweave.pages.components.userfeedback;

import javax.mail.MessagingException;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.application.UserWeaveSession;
import com.userweave.domain.User;
import com.userweave.domain.service.mail.MailService;
import com.userweave.pages.base.BaseUserWeavePage;
import com.userweave.pages.components.button.DefaultButton;
import com.userweave.pages.test.jquery.JQuery;

/**
 * 
 * @author opr
 *
 */
public class UserFeedbackPage extends WebPage {
	
	@SpringBean
	private MailService mailService;
	
	public UserFeedbackPage(final ModalWindow window) {
		
		
		final User user = getUser();
		
		final Model commentModel = new Model<String>() 
		{
			String object;
  
			@Override
			public String getObject() 
			{
			    return object;
			}

			@Override
			public void setObject(String object) 
			{
			    this.object = object;
			}
		};
		
		final RadioGroup feedbackSelectionType = new RadioGroup("feedbackSelectionType",new Model());
		
		feedbackSelectionType.add(new Radio("question", 
				new StringResourceModel("question", this, null)));
		feedbackSelectionType.add(new Radio("praise", 
				new StringResourceModel("praise", this, null)));
		feedbackSelectionType.add(new Radio("critique", 
				new StringResourceModel("critique", this, null)));
		feedbackSelectionType.add(new Radio("comment", 
				new StringResourceModel("comment", this, null)));
		
		Form form = new Form("form")
		{
			@Override
			protected void onSubmit()
			{
				String message = new StringResourceModel("salutation", this, null).getObject().toString() +  " " 
		    	 + user.getForename() + " " + user.getSurname()  + ",\n\n";
				message += new StringResourceModel("kind_of_feedback", this, null).getObject().toString() +" "+feedbackSelectionType.getModel().getObject().toString()+ ".\n\n";
				message += new StringResourceModel("feedback", this, null).getObject().toString() +" "+ commentModel.getObject() + "\n\n";
				message += new StringResourceModel("communicate", this, null).getObject().toString()+ "\n\n";
				message += new StringResourceModel("discharge", this, null).getObject().toString();
					try {
						mailService.sendMail(user.getEmail(), new StringResourceModel("feedback_mail_subject", this, null).getObject().toString() , message, "feedback@userweave.net", true);
					} catch (MessagingException e) {
						e.printStackTrace();
					}
			}
		};
		
		add(form);		
		
		form.add(feedbackSelectionType);		
		
		form.add(new TextArea ("textarea", commentModel));
		
		form.add(new DefaultButton("button", new StringResourceModel("submit_feedback_panel", UserFeedbackPage.this, null), form){
			   @Override
			   protected void onSubmit(AjaxRequestTarget target, Form form)
			   {
			    window.close(target);
			   }
			   
			  });
	}
	
	@Override
	public void renderHead(IHeaderResponse response)
	{
		response.renderCSSReference(new PackageResourceReference(
				BaseUserWeavePage.class, "configBase.css"));
	
		response.renderJavaScriptReference(JQuery.getLatest());
		
		response.renderJavaScriptReference(
				new JavaScriptResourceReference(
					BaseUserWeavePage.class,
					"hover.js"));
	}
	
	public User getUser()
	{
		return UserWeaveSession.get().getUser();
	}
	
}
