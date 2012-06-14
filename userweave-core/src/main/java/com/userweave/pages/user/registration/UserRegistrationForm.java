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
package com.userweave.pages.user.registration;

import java.util.Arrays;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;

import com.userweave.components.localerenderer.LocaleChoiceRenderer;
import com.userweave.domain.User;
import com.userweave.domain.User.BusinessRole;
import com.userweave.pages.components.address.AddressForRegistrationPanel;
import com.userweave.pages.components.button.DefaultButton;
import com.userweave.pages.components.callnumber.CallnumberPanel;
import com.userweave.pages.user.configuration.LocalizedBusinessRoleChoiceRenderer;
import com.userweave.pages.user.configuration.LocalizedPositionChoiceRenderer;
import com.userweave.utils.LocalizationUtils;

@SuppressWarnings("serial")
public abstract class UserRegistrationForm extends StatelessForm {
	
	private final WebMarkupContainer companyContainer;
	private final TextField company;

	
	public UserRegistrationForm(String id, IModel userModel, final ModalWindow agbModalWindow) {
		super(id);
		
		setModel(new CompoundPropertyModel(userModel));
		
		WebMarkupContainer container = new WebMarkupContainer("container");
		add(container);
		
		companyContainer = new WebMarkupContainer("companyContainer");
		container.add(companyContainer);
		companyContainer.setOutputMarkupPlaceholderTag(true);
		// set initial visibility
		companyContainer.setVisible(getUser().getBusinessRole() == BusinessRole.Company);
		
		//add(new TextField("email").setRequired(true).setEnabled(getUser().getEmail() == null));

		add(new CheckBox("verified").setEnabled(false));
		
		//add(new ExternalLink("agb",new Model("http://usability-methods.com/en/termsofuse") , new StringResourceModel("agb", this, null)));
		
		add(new AjaxLink("agb") {

			@Override
			public void onClick(AjaxRequestTarget target)  {
				agbModalWindow.show(target);	
			}
			
		});
		
		container.add(new TextField("surname").setRequired(true));					
		container.add(new TextField("forename").setRequired(true));			
		
		container.add(new DropDownChoice("locale",			
					LocalizationUtils.getSupportedConfigurationFrontendLocales(), 
					new LocaleChoiceRenderer(null)));
		
		container.add(new RadioChoice("gender", Arrays.asList(User.Gender.values()),
				new LocalizedGenderChoiceRenderer(this)).setSuffix(""));
		
		container.add(new CallnumberPanel("callnumberPanel", new PropertyModel(getModel(), "callnumber")));

		RadioChoice businessRole = new RadioChoice("businessRole", Arrays.asList(User.BusinessRole.values()),
				new LocalizedBusinessRoleChoiceRenderer(this)).setSuffix("");
		businessRole.add(new IValidator() {
			
			@Override
			public void validate(IValidatable validatable) {
				Object o = validatable.getValue();
				
				User.BusinessRole val = (User.BusinessRole) o;
				
				if (val == BusinessRole.Company) {
					company.setRequired(true);
				}
				else {
					company.setRequired(false);
				}
			}
		});

		businessRole.add( new AjaxFormChoiceComponentUpdatingBehavior() {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				boolean actsAsCompany = getUser().getBusinessRole() == BusinessRole.Company;				
				companyContainer.setVisible(actsAsCompany);
				target.addComponent(companyContainer);
			} 
		});
		container.add(businessRole);
		
		
		companyContainer.add(new DropDownChoice("position", Arrays.asList(User.Position.values()),
				new LocalizedPositionChoiceRenderer(this)));
		
		companyContainer.add(new TextField("employment"));
		
		companyContainer.add(company = new TextField("company"));				
		//add(new TextField("education"));					
		companyContainer.add(new TextField("companyUrl"));								
		companyContainer.add(new TextField("VATIN"));
		
		container.add(new AddressForRegistrationPanel("addressPanel", new PropertyModel(getModel(), "address")));
		
		
		container.add(new CheckBox("receiveNews"));	
		//add(new CheckBox("verified").setRequired(true).setEnabled(!getUser().isVerified()));
		
		container.add(new DefaultButton("saveButton", 
				new StringResourceModel("save", this, null), this)
		{
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form form)
			{
				UserRegistrationForm.this.onSave(target);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form form) {
				target.addComponent(UserRegistrationForm.this.get("feedback"));
				
			}
		});
		
		add(new FeedbackPanel("feedback").setOutputMarkupId(true));
	}
	

	public User getUser() {
		return (User) getModelObject();		
	}
	
	private class LocalizedGenderChoiceRenderer implements IChoiceRenderer
	{
		private final Component parent;
		public LocalizedGenderChoiceRenderer(Component parent)
		{
			this.parent = parent;
		}
		
		@Override
		public Object getDisplayValue(Object object) {
			StringResourceModel srm = new StringResourceModel(
					((User.Gender) object).name().toLowerCase(), parent, null);
			
			return srm.getObject();
		}

		@Override
		public String getIdValue(Object object, int index) {
			return ((User.Gender) object).toString();
		}
	}
	
	public abstract void onSave(AjaxRequestTarget target);
	
}
