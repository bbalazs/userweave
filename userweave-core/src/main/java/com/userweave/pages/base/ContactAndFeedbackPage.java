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
package com.userweave.pages.base;

import java.util.Arrays;

import javax.mail.MessagingException;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.userweave.application.UserWeaveSession;
import com.userweave.components.customModalWindow.BaseModalWindowPage;
import com.userweave.domain.User;
import com.userweave.domain.service.mail.MailService;

/**
 * Web page to give feedback for the product.
 * 
 * @author opr
 */
public class ContactAndFeedbackPage extends BaseModalWindowPage
{
	@SpringBean
	private MailService mailservice;
	
	/**
	 * Type of feedback, that can be given.
	 * 
	 * @author opr
	 */
	private enum FeedbackType { 
		FEEDBACK, INQUIRY, PRAISE, CRITICISM, REPORT_ERROR, COMPLAINT, OTHER } 
	
	/**
	 * Selected feedback type
	 */
	private FeedbackType feedbacktype;
	
	/**
	 * Written feedback message
	 */
	private String feedbackMessage;
	
	/**
	 * Feedback panel to display errors.
	 */
	private FeedbackPanel feedback;
	
	/**
	 * Default construcor.
	 * 
	 * @param window
	 * 		Modal window this page belongs to.
	 */
	public ContactAndFeedbackPage(ModalWindow window)
	{
		super(window);
		
		feedbacktype = FeedbackType.FEEDBACK;
		
		User user = UserWeaveSession.get().getUser();
		
		addToForm(new Label(
			"feedbackMessage", 
			new StringResourceModel(
					"feedbackMessage", 
					this, null, 
					new Object[] {user.getForename(), user.getSurname()})));
		
		addToForm(new DropDownChoice(
			"typeOfFeedback", 
			new PropertyModel(ContactAndFeedbackPage.this, "feedbacktype"), 
			Arrays.asList(FeedbackType.values()), 
			new IChoiceRenderer()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public String getIdValue(Object object, int index)
				{
					return ((FeedbackType) object).toString() + index;
				}
				
				@Override
				public Object getDisplayValue(Object object)
				{
					return new StringResourceModel(
						((FeedbackType) object).toString(), 
						ContactAndFeedbackPage.this, null).getObject();
				}
			}));
		
		addToForm(new TextArea(
				"messageTextArea", 
				new PropertyModel(ContactAndFeedbackPage.this, "feedbackMessage")));
		
		feedback = new FeedbackPanel("feedback");
		feedback.setOutputMarkupId(true);
		
		addToForm(feedback);
	}
	
	@Override
	protected WebMarkupContainer getAcceptButton(String componentId,
			final ModalWindow window)
	{
		final AjaxSubmitLink link = new AjaxSubmitLink(componentId, getForm())
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form form)
			{
				User user = UserWeaveSession.get().getUser();
				
				String subject = new StringResourceModel(
					feedbacktype.toString(), ContactAndFeedbackPage.this, null).getString();
				
				try
				{
					DateTimeFormatter fmt = DateTimeFormat.forPattern("dd.MM.yyyy");
					
					DateTimeFormatter localeFmt = fmt.withLocale(user.getLocale());
					
					DateTimeFormatter timeFmt = DateTimeFormat.forPattern("HH:mm");
					
					DateTime dateTime = new DateTime();
					
					String mailMessage = new StringResourceModel(
						"mailMessage", 
						ContactAndFeedbackPage.this, 
						null,
						new Object[] {  
							user.getForename(),
							user.getSurname(),
							dateTime.toString(localeFmt),
							dateTime.toString(timeFmt),
							subject,
							feedbackMessage
						}).getString();
					
					String subjectForMail = new StringResourceModel(
							"mailSubject", 
							ContactAndFeedbackPage.this, 
							null).getString();
					
					mailservice.sendMail(
						user.getEmail(), subjectForMail, mailMessage, "info@userweave.net", true);
					
					window.close(target);
				}
				catch (MessagingException e)
				{
					error(new StringResourceModel(
						"mailMessageError", ContactAndFeedbackPage.this, null).getString());
				
					target.add(feedback);
				}
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form)
			{
			}
		};
		
		return link;
	}
	
	@Override
	protected IModel getAcceptLabel()
	{
		return new StringResourceModel("send", this, null);
	}
}
