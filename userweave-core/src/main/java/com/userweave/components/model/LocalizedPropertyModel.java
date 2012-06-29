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

import org.apache.wicket.model.PropertyModel;

import com.userweave.domain.LocalizedString;
import com.userweave.utils.LocalizationUtils;

/**
 * Model to wrap a localized string, which is part of
 * an entity.
 * 
 * @author opr
 */
@SuppressWarnings("rawtypes")
public class LocalizedPropertyModel extends PropertyModel 
{
	private static final long serialVersionUID = 1L;

	/**
	 * The locale to resolve the localized string,
	 * if getObject / setObject is called.
	 */
	private final Locale locale;
	
	/**
	 * Default constructor.
	 * 
	 * @param modelObject
	 * 		The entity model to wrap.
	 * @param expression
	 * 		The attribute of the entity to reolve.
	 * @param locale
	 * 		The locale to reolve the localized string to.
	 */
	public LocalizedPropertyModel(
		final Object modelObject, final String expression, Locale locale) 
	{
		super(modelObject, expression);
		
		this.locale = locale;
	}
	
	/**
	 * Return a string form the LocalizedString object,
	 * that is determined by the given locale.
	 */
	@Override
	public Object getObject() 
	{		
		LocalizedString localizedString = getLocalizedString();
		
		return LocalizationUtils.getValue(localizedString, locale);
	}
	
	/**
	 * Set a string in the underlying LocalizedString, depending
	 * on the given locale.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void setObject(Object object) 
	{
		LocalizedString localizedString = getLocalizedString();
		
		if (localizedString == null) 
		{
			localizedString = new LocalizedString();
			super.setObject(localizedString);
		} 	
		
		localizedString.setValue(
			(String) object, 
			locale != null ? locale : LocalizationUtils.getDefaultLocale());
	}

	/**
	 * Get the underling model object.
	 * 
	 * @return
	 * 		A LocalizedString.
	 */
	public LocalizedString getLocalizedString() 
	{
		return (LocalizedString) super.getObject();
	}
}
