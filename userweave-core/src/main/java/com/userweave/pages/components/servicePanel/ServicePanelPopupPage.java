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
package com.userweave.pages.components.servicePanel;

import javax.mail.MessagingException;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.application.UserWeaveSession;
import com.userweave.components.customModalWindow.CustomModalWindow;
import com.userweave.dao.UserDao;
import com.userweave.domain.User;
import com.userweave.domain.service.mail.MailService;
import com.userweave.pages.base.BaseUserWeavePage;
import com.userweave.pages.components.button.DefaultButton;
import com.userweave.pages.components.callnumber.CallnumberPanel;
import com.userweave.presentation.model.UserModel;

@SuppressWarnings("serial")
public class ServicePanelPopupPage extends WebPage {
	private Form form;
	
	private CallnumberPanel CallnumberPanel;

	@SpringBean
	private MailService mailService;
	
	@SpringBean 
	private UserDao userDao;

	public ServicePanelPopupPage(final CustomModalWindow window, final IModel serviceText, final IModel linkText) {
		
		final UserModel userModel = new UserModel(UserWeaveSession.get().getUser());	
		
		setDefaultModel(new CompoundPropertyModel(userModel));
		
		final Model commentModel = new Model<String>() 
		{
			String object;
  
			@Override
			public String getObject() 
			{
			    if(object == null) 
			    {
			      return new StringResourceModel(
			    		  "comment", ServicePanelPopupPage.this, null).getString();
			    }
			    return object;
			}

			@Override
			public void setObject(String object) 
			{
			    this.object = object;
			}
		};
		
		

		Radio email;
		
		Radio phone;
		
		final RadioGroup group = new RadioGroup("group", new Model());
		
		group.setRequired(false);

		group.add(email = new Radio("radio_email", new Model("E-Mail")));		

		group.add(new Label("email"));

		group.add(phone = new Radio("radio_phone", new StringResourceModel("phone", this, null)));		

		add(new FeedbackPanel("feedback").setOutputMarkupId(true));

		add(new Label("serviceText", serviceText));

		add(new Label("linkText", linkText));
		
		add(form = new Form("form")
		{
			{			
				
				add(new TextArea("comment", commentModel));
				
				add(CallnumberPanel = new CallnumberPanel("callnumberPanel", 
						new PropertyModel(ServicePanelPopupPage.this.getDefaultModel(), 
								"callnumber")));
				

				add(new DefaultButton("submit", 
						new StringResourceModel("submit_service_panel", ServicePanelPopupPage.this, null), 
						this)
			    {
			     @Override
			     protected void onSubmit(AjaxRequestTarget target, Form form) {
			    	
					window.close(target);
					
			     }
			     
			     @Override
			     protected void onError(AjaxRequestTarget target, Form form) {
			    	 target.addComponent(ServicePanelPopupPage.this.get("feedback"));
			     }
			    });
			}
			
			@Override
			protected void onSubmit()
			{
								
				User user = userModel.getObject();
				
				user.getCallnumber().setPhone(CallnumberPanel.getEnteredPhoneNumber());
				
				userDao.save(user);
				
				String message = new StringResourceModel("salutation", this, null).getObject().toString() +  " " 
		    	 + user.getForename() + " " + user.getSurname()  + ",\n\n";
		 		message += "" + serviceText.getObject().toString() + "\n\n";
		 		message += "" + linkText.getObject().toString() + "\n\n";
		 		message += new StringResourceModel("your_comment", this, null).getObject().toString() +"\n\n";
		 		message += "" + commentModel.getObject() + "\n\n";
		 		message += new StringResourceModel("how_to_communicate", this, null).getObject().toString() + " " +
		 					group.getModel().getObject() + " " +
		 					new StringResourceModel("how_to_communicate2", this, null).getObject().toString() + "\n\n";
		 		message += new StringResourceModel("service_data", this, null).getObject().toString() +"\n\n" + "Email:" + " " + 
		 		user.getEmail() + "\n\n" + 
		 		new StringResourceModel("phone", ServicePanelPopupPage.this, null).getString() + ": " + 
		 		user.getCallnumber().getPhone() + "\n\n";
		 		
		 		message += new StringResourceModel("discharge", this, null).getObject().toString();

					try {
						mailService.sendMail(user.getEmail(), new StringResourceModel("service_mail_subject", this, null).getObject().toString() , message, "service@userweave.net", true);
					} catch (MessagingException e) {
						e.printStackTrace();
					}
			}
		});

		form.add(group);
	}

	@Override
	public void renderHead(IHeaderResponse response)
	{
		response.renderCSSReference(new PackageResourceReference(
				BaseUserWeavePage.class, "configBase.css"));
	
		response.renderCSSReference(new PackageResourceReference(
				ServicePanelPopupPage.class, "res/PopupPage.css"));
	}
	
	public User getUser()
	{
		return UserWeaveSession.get().getUser();
	}
	
}