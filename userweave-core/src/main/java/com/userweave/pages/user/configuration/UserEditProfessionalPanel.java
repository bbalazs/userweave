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
package com.userweave.pages.user.configuration;

import java.util.Arrays;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;

import com.userweave.ajax.form.AjaxBehaviorFactory;
import com.userweave.domain.User;
import com.userweave.domain.User.BusinessRole;
import com.userweave.pages.components.servicePanel.ServicePanel;
import com.userweave.pages.components.servicePanel.ServicePanel.ServicePanelType;
import com.userweave.presentation.model.UserModel;


@SuppressWarnings("serial")
public class UserEditProfessionalPanel extends UserEditBasePanel {

	private final WebMarkupContainer companyContainer;
	private final TextField company;
	
	public UserEditProfessionalPanel(String id, UserModel userModel) {
		super(id, userModel);
		
		add(new ServicePanel("servicePanel", ServicePanelType.OWN_HOSTING));
		add(new ServicePanel("servicePanel2", ServicePanelType.ADVANCE_PRODUCT));
		
		companyContainer = new WebMarkupContainer("companyContainer");
		getForm().add(companyContainer);
		companyContainer.setOutputMarkupPlaceholderTag(true);
		
		company = new TextField("company");
		
		company.add(AjaxBehaviorFactory.getUpdateBehavior("onblur", UserEditProfessionalPanel.this));
		
		companyContainer.add(company);
		
		DropDownChoice position = 
			new DropDownChoice("position", Arrays.asList(User.Position.values()),
					new LocalizedPositionChoiceRenderer(this));
		
		position.add(AjaxBehaviorFactory.getUpdateBehavior("onchange", UserEditProfessionalPanel.this));
		
		companyContainer.add(position);
		
		TextField url = new TextField("companyUrl");
		
		url.add(AjaxBehaviorFactory.getUpdateBehavior("onblur", UserEditProfessionalPanel.this));
		
		companyContainer.add(url);
		
		TextField vatin = new TextField("VATIN");
		
		vatin.add(AjaxBehaviorFactory.getUpdateBehavior("onblur", UserEditProfessionalPanel.this));
		
		companyContainer.add(vatin);
		
		TextField employment = new TextField("employment");
		
		employment.add(AjaxBehaviorFactory.getUpdateBehavior("onblur", UserEditProfessionalPanel.this));
		
		companyContainer.add(employment);
		
		// set initial visibility
		companyContainer.setVisible(getUser().getBusinessRole() == BusinessRole.Company);
		company.setRequired(((User)getDefaultModelObject()).getBusinessRole() == BusinessRole.Company);
		
		
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
				company.setRequired(actsAsCompany);
				target.addComponent(companyContainer);
				onSubmit(target);
			} 
		});
		
		
		getForm().add(businessRole);
	}

}
