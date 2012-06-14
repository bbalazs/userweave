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
package com.userweave.pages.components.address;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

import com.userweave.ajax.form.AjaxBehaviorFactory;
import com.userweave.pages.user.configuration.UserEditBasePanel;

/**
 * @author oma
 */
@SuppressWarnings("serial")
public class AddressPanel extends Panel {

	private final TextField street, houseNumber, postCode;
	private final TextField city;
	
	private final DropDownChoice country;
	
	public AddressPanel(String id, IModel addressModel) {
		super(id);	
		setDefaultModel(new CompoundPropertyModel(addressModel));		
		
		street = new TextField("street");
		street.setRequired(true);
		
		houseNumber = new TextField("houseNumber");
		houseNumber.setRequired(true);
		
		postCode = new TextField("postcode");
		postCode.setRequired(true);
		
		city = new TextField("city");
		city.setRequired(true);
		
		country = new DropDownChoice("country", 
				new LocalizedCountryModel(this), 
				new LocalizedCountryChoiceRenderer(this));
		
		country.setRequired(true);
		
		add(street);	
		add(houseNumber);
		add(postCode);
		add(city);			
		add(country);		
	}
	
	public void enableAjaxUpdateBehavior(UserEditBasePanel panel)
	{
		street.add(AjaxBehaviorFactory.getUpdateBehavior("onblur", panel));
		houseNumber.add(AjaxBehaviorFactory.getUpdateBehavior("onblur", panel));
		postCode.add(AjaxBehaviorFactory.getUpdateBehavior("onblur", panel));
		city.add(AjaxBehaviorFactory.getUpdateBehavior("onblur", panel));
		country.add(AjaxBehaviorFactory.getUpdateBehavior("onchange", panel));
	}
}

