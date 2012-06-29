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

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.model.StringResourceModel;

import com.userweave.domain.Country;

@SuppressWarnings("serial")
public class LocalizedCountryChoiceRenderer extends ChoiceRenderer {
	Component parent;
	
	public LocalizedCountryChoiceRenderer(Component parent) {
		this.parent = parent;
	}
	
	@Override
	public Object getDisplayValue(Object object) {
		Country country = (Country) object;
		StringResourceModel srm = new StringResourceModel(country.name(), parent, null);
		return srm.getObject();
	}

	@Override
	public String getIdValue(Object object, int index) {
		//return ((User.Gender) object).toString();
		return super.getIdValue(object, index);
	}
	
}
