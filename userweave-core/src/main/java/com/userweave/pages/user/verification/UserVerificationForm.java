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
package com.userweave.pages.user.verification;

import java.util.Arrays;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;

import com.userweave.components.localerenderer.LocaleChoiceRenderer;
import com.userweave.domain.User;
import com.userweave.pages.components.button.DefaultButton;
import com.userweave.utils.LocalizationUtils;

@SuppressWarnings("serial")
public abstract class UserVerificationForm extends StatelessForm {
	
	private final WebMarkupContainer container;
	
	private final Boolean isReceiveNews = false;
	
	public UserVerificationForm(String id, IModel userModel, final ModalWindow agbModalWindow) {
		super(id);
		
		setModel(new CompoundPropertyModel(userModel));
		
		AjaxCheckBox acb = new AjaxCheckBox("verified")
		{
			@Override
			protected void onUpdate(AjaxRequestTarget target) 
			{
				toggleVisibility(this, target);				
			}
		};
		
		add(acb);
		
		
		add(new AjaxLink("agb")
		{

			@Override
			public void onClick(AjaxRequestTarget target) 
			{
				agbModalWindow.show(target);	
			}
			
		});
		
		container = new WebMarkupContainer("container");
		add(container);
		
		container.setVisible(false);
		
		container.setOutputMarkupPlaceholderTag(true);
		
		container.add(new TextField("surname").setRequired(true));					
		container.add(new TextField("forename").setRequired(true));	
		
		container.add(createReceiveNewsChoice());	
		
		container.add(new DefaultButton("saveButton", new StringResourceModel("save", this, null), this) {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form form) {
				UserVerificationForm.this.onSave(target);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form form) {
				target.addComponent(UserVerificationForm.this.get("feedback"));
				
			}
		});
		
		addLocaleChoice();
		
		add(new FeedbackPanel("feedback").setOutputMarkupId(true));
	}
	
	private DropDownChoice createReceiveNewsChoice()
	{
		DropDownChoice choice = new DropDownChoice(
			"receiveNews",
			new PropertyModel(UserVerificationForm.this, "isReceiveNews"),
			Arrays.asList(new Boolean[] { true, false }),
			new IChoiceRenderer()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public String getIdValue(Object object, int index)
				{
					return object.toString() + index;
				}

				@Override
				public Object getDisplayValue(Object object)
				{
					if (((Boolean) object))
					{
						return new StringResourceModel("isReceivingNews",
							UserVerificationForm.this, null).getObject();
					}

					return new StringResourceModel("isNotReceivingNews",
							UserVerificationForm.this, null).getObject();
				}
			});
		
		choice.add(new AjaxFormComponentUpdatingBehavior("onchange")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				getUser().setReceiveNews(isReceiveNews);
//				
//				UserVerificationForm.this.onSave(target);
			}
		});
		
		return choice;
	}
	
	private void addLocaleChoice() {
		
		container.add( 
				new DropDownChoice("locale",			
						LocalizationUtils.getSupportedConfigurationFrontendLocales(), 
						new LocaleChoiceRenderer(null)
				)
			);
	}

	protected User getUser() {
		return (User) getModelObject();
		
	}
	
	private void toggleVisibility(CheckBox checkBox, AjaxRequestTarget target) {
		boolean visible = checkBox.getModelObject().equals(Boolean.TRUE);
		toggleVisibility(visible);
		
		if(target != null) {
			target.addComponent(this);
		}
	}
		
	private void toggleVisibility(boolean b)
	{
		container.setVisible(b);
	}
	
	public abstract void onSave(AjaxRequestTarget target);
	
}
