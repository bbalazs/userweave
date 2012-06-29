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
package com.userweave.pages.configuration.project.userpanel;

import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.application.UserWeaveSession;
import com.userweave.components.customModalWindow.BaseModalWindowPage;
import com.userweave.dao.ProjectUserRoleJoinDao;
import com.userweave.domain.Project;
import com.userweave.domain.ProjectUserRoleJoin;
import com.userweave.domain.User;
import com.userweave.domain.service.mail.MailService;

public class WriteMessagePage extends BaseModalWindowPage
{
	@SpringBean
	private ProjectUserRoleJoinDao purjDao;
	
	@SpringBean
	private MailService mailService;
	
	private String subject, message;
	
	private final Project project;
	
	private final FeedbackPanel feedback;
	
	public WriteMessagePage(ModalWindow window, final Project project)
	{
		super(window);
		
		this.project = project;
		
		addToForm(new Label(
			"writeMessage", 
			new StringResourceModel(
					"writeMessage", 
					WriteMessagePage.this, 
					null, 
					new Object[] {project.getName()})));
		
		addToForm(new TextField(
			"subject", new PropertyModel(WriteMessagePage.this, "subject")));
		
		addToForm(new TextArea(
			"yourMessage", new PropertyModel(WriteMessagePage.this, "message")));
	
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
				
				List<ProjectUserRoleJoin> joins = purjDao.getJoinsByProject(project);
				
				List<String> recipients = new ArrayList<String>();
				
				for(ProjectUserRoleJoin join : joins)
				{
					if(join.getUser().getId().equals(user.getId()))
					{
						continue;
					}
					
					recipients.add(join.getUser().getEmail());
				}
				
				String mailSubject = new StringResourceModel(
					"mailSubject", 
					WriteMessagePage.this, 
					null,
					new Object[] {project.getName()}).getString();
				
				String mailMessage = new StringResourceModel(
						"mailMessage", 
						WriteMessagePage.this, 
						null, 
						new Object[] 
						{
							user.getForename(),
							user.getSurname(),
							project.getName(),
							subject,
							message
						}).getString();
				
				try
				{
					mailService.sendMails(
						recipients, mailSubject, mailMessage, user.getEmail());
					
					window.close(target);
				}
				catch(MessagingException e)
				{
					error(new StringResourceModel(
							"could_not_send_mail_to", 
							WriteMessagePage.this, null)); 
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
		return new StringResourceModel("send", WriteMessagePage.this, null);
	}
}
