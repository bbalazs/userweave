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
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;

import com.userweave.components.customModalWindow.BaseModalWindowPage;

/**
 * Message dialog to display the "set to public" message.
 * Contains a checkbox to accept the rules for setting a 
 * project to public.
 * 
 * @author opr
 */
public abstract class SetToPublicWebPage extends BaseModalWindowPage
{
	/**
	 * Determines, if a user accepts the agreement.
	 */
	private final boolean checked;
	
	/**
	 * The name of the project.
	 */
	private final String projectName;
	
	/**
	 * Default constructor.
	 * 
	 * @param window
	 * 		Modal window this page is attached to.
	 */
	public SetToPublicWebPage(ModalWindow window, String projectName)
	{
		super(window);
		
		setOutputMarkupId(true);
		
		checked = false;
		
		this.projectName = projectName;
		
		addToForm(new AjaxCheckBox("accept", new PropertyModel(this, "checked"))
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				target.addComponent(getAcceptLink().setVisible(checked));
			}
		});
		
		addToForm(new Label(
			"paragraph_two", 
			new StringResourceModel(
					"paragraph_two", 
					SetToPublicWebPage.this,
					null,
					new Object[] {projectName})));
		
		addToForm(new Label(
				"paragraph_three", 
				new StringResourceModel(
						"paragraph_three", 
						SetToPublicWebPage.this,
						null,
						new Object[] {projectName})));
		
		addToForm(new Label(
				"paragraph_four", 
				new StringResourceModel(
						"paragraph_four", 
						SetToPublicWebPage.this,
						null,
						new Object[] {projectName})));
		
		
		/*
		 * Replace to make label visible. 
		 */
		WebMarkupContainer replacement = getAcceptButton("accept", window);
		replacement.add(new Label("accept_label", getAcceptLabel()));
		
		getAcceptLink().replaceWith(replacement);
		
		setAcceptLink(replacement);
		
	}
	
	@Override
	protected WebMarkupContainer getAcceptButton(String componentId,
			final ModalWindow window)
	{
		AjaxLink acceptLink = new AjaxLink(componentId)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				onAccept(target);
				window.close(target);
			}
		};
	
		acceptLink.setVisible(false);
		acceptLink.setOutputMarkupId(true);
		acceptLink.setOutputMarkupPlaceholderTag(true);
		
		return acceptLink;
	}
	
	@Override
	protected IModel getAcceptLabel()
	{
		return new StringResourceModel(
			"accept_button_label", 
			SetToPublicWebPage.this, 
			null,
			new Object[] {SetToPublicWebPage.this.projectName});
	}
	
	/**
	 * Delegate event handling back to calling window.
	 * 
	 * @param target
	 * 		The ajax request target.
	 */
	protected abstract void onAccept(AjaxRequestTarget target);
}
