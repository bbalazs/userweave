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
package com.userweave.components.model;

import java.util.Locale;

import org.apache.wicket.markup.html.form.ChoiceRenderer;

import com.userweave.domain.LocalizedString;
import com.userweave.utils.LocalizationUtils;

@SuppressWarnings("serial")
public class LocalizedChoiceRenderer extends ChoiceRenderer {

	private final Locale locale;
	
	public LocalizedChoiceRenderer(Locale locale) {
		this.locale = locale;
	}
	
	public LocalizedChoiceRenderer(String displayExpression, Locale locale) {
		super(displayExpression);
		this.locale = locale;
	}
	
	@Override
	public Object getDisplayValue(Object object) {
		Object displayValue = super.getDisplayValue(object);
		
		LocalizedString localizedString = (LocalizedString) (displayValue);
		return LocalizationUtils.getValue(localizedString, locale);
	}

}
