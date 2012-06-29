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
package com.userweave.components.localerenderer;

import java.util.Locale;

import org.apache.wicket.markup.html.form.IChoiceRenderer;

import com.userweave.utils.LocalizationUtils;

@SuppressWarnings("serial")
public class LocaleChoiceRenderer implements IChoiceRenderer {

	/**
	 * selfTranslate means that the language shall be displayed in it's own language
	 */
	private final Locale refLocale;
	
	public LocaleChoiceRenderer(Locale locale) {
		this.refLocale = locale;	
	}
	
	@Override
	public Object getDisplayValue(Object object) {
		return getDisplayValue(object, refLocale);
	}

	@Override
	public String getIdValue(Object object, int index) {
		return Integer.toString(index);
	}
	
	public static String getDisplayValue(Object object, Locale refLocale) {
		Locale locale = (Locale) object;
		return LocalizationUtils.getDisplayLanguage(locale, refLocale);
	}

}
