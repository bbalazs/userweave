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
package com.userweave.pages.components.callnumber;

import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

/**
 * @author oma
 */
@SuppressWarnings("serial")
public class CallnumberPanel extends Panel {

	private final TextField phone;
	
	public CallnumberPanel(String id, IModel callnumberModel) {
		super(id);	
		setDefaultModel(new CompoundPropertyModel(callnumberModel));			
		
		phone = new TextField("phone");
		phone.setRequired(true);
		
		add(phone);				
	}
	
	public String getEnteredPhoneNumber()
	{
		return phone.getConvertedInput().toString();
	}
	
	/**
	 * enables ajax functionality to update the phone
	 * number via ajax
	 */
	public void addAjaxUpdateBehaviorToPhone(AjaxFormComponentUpdatingBehavior behavior)
	{
		phone.add(behavior);
	}
}

