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

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;

import com.userweave.ajax.form.AjaxBehaviorFactory;
import com.userweave.domain.User;
import com.userweave.pages.components.address.AddressForRegistrationPanel;
import com.userweave.pages.components.callnumber.CallnumberPanel;
import com.userweave.pages.components.servicePanel.ServicePanel;
import com.userweave.pages.components.servicePanel.ServicePanel.ServicePanelType;
import com.userweave.presentation.model.UserModel;


@SuppressWarnings("serial")
public class UserEditContactPanel extends UserEditBasePanel {
	
	public UserEditContactPanel(String id, UserModel userModel) {
		super(id, userModel);
		
		add(new ServicePanel("servicePanel", ServicePanelType.OWN_HOSTING));
		add(new ServicePanel("servicePanel2", ServicePanelType.ADVANCE_PRODUCT));
		
		getForm().add(new TextField("email").setRequired(true).setEnabled(getUser().getEmail() == null));

		CallnumberPanel callnumber = 
			new CallnumberPanel("callnumberPanel", new PropertyModel(getDefaultModel(), "callnumber"));
		
		callnumber.addAjaxUpdateBehaviorToPhone(AjaxBehaviorFactory.getUpdateBehavior("onblur", UserEditContactPanel.this));
		
		AddressForRegistrationPanel address = 
			new AddressForRegistrationPanel("addressPanel", new PropertyModel(getDefaultModel(), "address"));
		
		address.enableAjaxUpdateBehavior(this);
		
		getForm().add(callnumber);
		getForm().add(address);
	}

	
	@Override
	protected void onSubmit(AjaxRequestTarget target) {
		User user = getUser();
		if(user.getEmail() != null && !user.getEmail().equals(user.getEmail().toLowerCase())) {
			user.setEmail(user.getEmail().toLowerCase());
		}
		super.onSubmit(target);
	}
}
