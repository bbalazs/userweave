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
package com.userweave.pages.test;

import java.util.List;
import java.util.Locale;

import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

import com.userweave.components.localerenderer.LocaleChoiceRenderer;
import com.userweave.pages.components.twoColumnPanel.multiColumnRadioChoicePanel.TwoColumnRadioChoicePanel;
import com.userweave.pages.test.jquery.JQuery;
import com.userweave.utils.LocalizationUtils;

/**
 * @author oma
 */
@SuppressWarnings("serial")
public abstract class SelectLocalePanel extends Panel {
	
	private final Locale selectedLocale;

	public SelectLocalePanel(String id, List<Locale> locales) {
		super(id);
		
		Locale browserLocale = LocalizationUtils.mapLocale(getSession().getLocale());
		
		if (browserLocale != null && locales.contains(browserLocale)) {
			selectedLocale = browserLocale;
		} else { 
			selectedLocale = locales.get(0);
		}
		
		Form form = new Form("form") {
			@Override
			protected void onSubmit() {
				onLocaleSelected(selectedLocale);
			}
		};
		
		add(form);
	
		form.add(new TwoColumnRadioChoicePanel(
				"locale",
				new PropertyModel(this, "selectedLocale"),
				locales,
				new LocaleChoiceRenderer(null))
		);
		
		form.add(new NextButton("nextButton"){
			@Override
			protected String getImageColor()
			{
				return getColorForNextButton();
			}
		});
	}
	
	@Override
	public void renderHead(IHeaderResponse response)
	{
		super.renderHead(response);
		
		JQuery.addSurveyUiJavaScript(getPage(), response);
	}
	
	protected abstract void onLocaleSelected(Locale selectedLocale);
	
	protected abstract String getColorForNextButton();

}

