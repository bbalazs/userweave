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

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.userweave.components.localerenderer.LocaleChoiceRenderer;

@SuppressWarnings("serial")
public class LocaleModel extends AbstractReadOnlyModel {
		
	private final IModel model;
	private final Locale refLocale;
	
	public LocaleModel(Locale object, Locale refLocale) {
		this(new Model(object), refLocale);
	}
	
	public LocaleModel(IModel model, Locale refLocale) {
		super();
		this.model = model;
		this.refLocale = refLocale;
	}

	@Override
	public Object getObject() {
		Object locale = model.getObject();
		if (locale instanceof Locale) {
			return LocaleChoiceRenderer.getDisplayValue(locale, refLocale);
		} else {
			return null;
		}
	}

}
