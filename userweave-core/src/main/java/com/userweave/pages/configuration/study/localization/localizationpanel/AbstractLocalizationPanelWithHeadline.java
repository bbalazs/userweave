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
package com.userweave.pages.configuration.study.localization.localizationpanel;

import java.util.List;
import java.util.Locale;

import org.apache.wicket.markup.html.form.AbstractTextComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;

import com.userweave.ajax.form.AjaxBehaviorFactory;
import com.userweave.domain.EntityBase;
import com.userweave.domain.LocalizedString;
import com.userweave.utils.LocalizationUtils;

public abstract class AbstractLocalizationPanelWithHeadline extends AbstractLocalizationPanel
{
	private static final long serialVersionUID = 1L;
	
	public AbstractLocalizationPanelWithHeadline(
			String id, List<Locale> locales, 
			List<LocalizedString> localizedStrings, 
			EntityBase entity, Locale studyLocale)
	{
		super(id, locales, localizedStrings, entity, studyLocale);
	}
	
	@Override
	protected IModel getLabelForRow(int index, int size)
	{
		// in studies and moduleConfigurations is
		// the first localized string the headline
		if(index == 0)
		{
			return new StringResourceModel("headline", this, null);
		}
		
		return getLabelForConfigurationRow(index, size);
	}
	
	@Override
	protected AbstractTextComponent getTextComponentForLocale(String id,
			final LocalizedString localeString, int index, Locale locale)
	{
		// in studies and moduleConfigurations is
		// the first localized string the headline
		if(index == 0)
		{
			TextField textField = new TextField(
					id, 
					new PropertyModel(localeString, "value" + LocalizationUtils.getIsoLangCode(locale).toUpperCase())); 
			
			textField.add(AjaxBehaviorFactory.getUpdateBehaviorForLocalizedString(
					"onblur", localeString, AbstractLocalizationPanelWithHeadline.this));
			
			return textField;
		}
		
		return getTextComponentForLocaleConfiguration(id, localeString, index, locale);
	}
	
	protected abstract IModel getLabelForConfigurationRow(int index, int size);
	
	protected abstract AbstractTextComponent getTextComponentForLocaleConfiguration(String id,
			final LocalizedString localeString, int index, Locale locale);
}
