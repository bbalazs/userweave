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

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.form.AbstractTextComponent;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;

import com.userweave.ajax.form.AjaxBehaviorFactory;
import com.userweave.domain.EntityBase;
import com.userweave.domain.LocalizedString;
import com.userweave.domain.Study;
import com.userweave.utils.LocalizationUtils;

public class StudyLocalePanel extends AbstractLocalizationPanelWithHeadline
{
	private static final long serialVersionUID = 1L;

	public StudyLocalePanel(String id, List<Locale> locales, Study study, Locale studyLocale)
	{
		super(id, locales, study.getLocalizedStrings(), study, studyLocale);
	}

	@Override
	protected IModel getEntityName(EntityBase entity)
	{
		return new StringResourceModel(
			"study", 
			this, 
			null, new Object[] {((Study)entity).getName()});
	}
	
	@Override
	protected IModel getLabelForConfigurationRow(int index, int size)
	{
		// the only localization string is the description
		return new StringResourceModel("description", this, null);
	}

	@Override
	protected AbstractTextComponent getTextComponentForLocaleConfiguration(
			String id, LocalizedString localeString, int index, Locale locale)
	{
		// the only localization string is the description		
		TextArea textArea = new TextArea(
			id, 
			new PropertyModel(
				localeString, 
				"value" + LocalizationUtils.getIsoLangCode(locale).toUpperCase())); 
			
		textArea.add(AjaxBehaviorFactory.getUpdateBehaviorForLocalizedString(
				"onblur", localeString, StudyLocalePanel.this));
		
		textArea.add(new AttributeModifier("row", true, new Model(8)));
		textArea.add(new AttributeModifier("col", true, new Model(35)));
			
		return textArea;
	}
}
