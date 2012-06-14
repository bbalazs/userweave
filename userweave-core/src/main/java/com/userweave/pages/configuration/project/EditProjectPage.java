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
package com.userweave.pages.configuration.project;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;

import com.userweave.components.customModalWindow.BaseModalWindowPage;
import com.userweave.module.methoden.questionnaire.page.conf.question.feedbackl.CustomFeedbackPanel;

/**
 * Simple web page to edit/create a project name.
 * 
 * @author opr
 */
public abstract class EditProjectPage extends BaseModalWindowPage
{
	private static final long serialVersionUID = 1L;

	/**
	 * Feedback panel to add on error.
	 */
	private FeedbackPanel feedbackPanel;
	
	/**
	 * The name of the project.
	 */
	private String name;
	
	/**
	 * Constructor to create a new project name.
	 * 
	 * @param modalWindow
	 * 		Window this page is attached to.
	 */
	public EditProjectPage(final ModalWindow modalWindow)
	{
		super(modalWindow);
		
		addFeedbackPanel();
		
		addToForm(
			new TextField(
				"name",
				new PropertyModel(EditProjectPage.this, "name")).setRequired(true));
	}
	
	/**
	 * Constructor to edit a project name.
	 * 
	 * @param modalWindow
	 * 		Window this page is attached to.
	 * @param name
	 * 		The project name to edit.
	 */
	public EditProjectPage(final ModalWindow modalWindow, String name)
	{
		super(modalWindow);
		
		this.name = name;

		addFeedbackPanel();
		
		addToForm(
			new TextField(
				"name",
				new PropertyModel(EditProjectPage.this, "name")).setRequired(true));
	}
	
	/**
	 * Adds a feedback panel to this page.
	 */
	private void addFeedbackPanel()
	{
		feedbackPanel = new CustomFeedbackPanel("feedback");
		
		feedbackPanel.setOutputMarkupId(true);
		
		addToForm(feedbackPanel);
	}
	
	@Override
	protected WebMarkupContainer getAcceptButton(
		String componentId, final ModalWindow window)
	{
		return new AjaxButton(componentId, getForm()) 
		{   
			private static final long serialVersionUID = 1L;

			@Override
			protected void onError(AjaxRequestTarget target, Form form) {
				target.add(feedbackPanel);
			}
			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form form) 
			{
				onFinish(target, name);		
				window.close(target);		
			}
        };
	}
	
	@Override
	protected IModel getAcceptLabel()
	{
		return new StringResourceModel("createProject", this, null);
	}
	
	@Override
	protected IModel getDeclineLabel()
	{
		return new StringResourceModel("decline", this, null);
	}
	
	protected abstract void onFinish(AjaxRequestTarget target, String projectName);
}
