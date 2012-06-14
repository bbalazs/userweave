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

import java.io.Serializable;
import java.util.Locale;

import org.apache.wicket.model.Model;

import com.userweave.domain.LocalizedString;
import com.userweave.utils.LocalizationUtils;

/**
 * Model to wrap a localized String. If getObject() is called,
 * this model returns the value of the localized string and 
 * not the localized string itself. This is needed, because 
 * calling objects are mostly text fields, which displays 
 * a string.
 * 
 * @author opr
 *
 */
@SuppressWarnings("rawtypes")
public class LocalizedModel extends Model 
{
	private static final long serialVersionUID = 1L;

	/**
	 * The locale, in which the returnig string is 
	 * displayed.
	 */
	private final Locale locale;
	
	/**
	 * Default constructor.
	 * 
	 * @param object
	 * 		Object to wrap.
	 * @param locale
	 * 		Locale for the strings.
	 */
	@SuppressWarnings("unchecked")
	public LocalizedModel(Serializable object, Locale locale) 
	{
		super(object); // calls this.setObject(). See comment there.
		
		this.locale = locale;
	}
	
	/**
	 * Returns the String, defined by the given locale, instead of
	 * the LocalizedString object.
	 */
	@Override
	public Serializable getObject() 
	{             
		LocalizedString localizedString = getLocalizedString();
		return LocalizationUtils.getValue(localizedString, locale);
	}

	/**
	 * Sets the object of this model.
	 * 
	 * @param object.
	 * 		Can be subclass of either LocalizedString or String. In 
	 * 		the former case, we need to call super.setObject(), since
	 * 		this is the object we want to store. In the later case,
	 * 		we have to set the LocalizedString value.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void setObject(Serializable object) 
	{
		if(object instanceof LocalizedString)
		{
			super.setObject(object);
			return;
		}
		
		LocalizedString localizedString = getLocalizedString();
		
		if (localizedString == null) 
		{
			localizedString = new LocalizedString();
			super.setObject(localizedString);
		}
		
		localizedString.setValue((String) object, locale != null ? locale : LocalizationUtils.getDefaultLocale());
	}
	
	public LocalizedString getLocalizedString() {
		return (LocalizedString) super.getObject();
	}
}
