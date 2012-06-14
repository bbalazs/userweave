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
package com.userweave.pages.configuration.study.localization;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import com.userweave.components.customModalWindow.BaseModalWindowPage;
import com.userweave.components.localerenderer.LocaleChoiceRenderer;
import com.userweave.domain.Study;
import com.userweave.utils.LocalizationUtils;

public abstract class AddLocalePage extends BaseModalWindowPage
{
	private List<Locale> locales;
	
	private FeedbackPanel feedback;
	
	public AddLocalePage(ModalWindow window, IModel studyModel)
	{
		super(window);
		
		setDefaultModel(studyModel);
		
		ListMultipleChoice choices = new ListMultipleChoice(
				"locales", 
				new PropertyModel(AddLocalePage.this, "locales"), 
				getAdditionalLocales(), new LocaleChoiceRenderer(getLocale()));
		
		choices.setMaxRows(5);
		
		addToForm(choices);
		
		addToForm(feedback = new FeedbackPanel("feedback"));
	}

	private Study getStudy()
	{
		return (Study) getDefaultModelObject();
	}
	
	private List<Locale> getAdditionalLocales() 
	{
		List<Locale> additionalLocales = new ArrayList<Locale>();
		additionalLocales.addAll(LocalizationUtils.getSupportedStudyLocales());
		additionalLocales.removeAll(getStudy().getSupportedLocales());
		return additionalLocales;
	}
	
	@Override
	protected WebMarkupContainer getAcceptButton(String componentId,
			final ModalWindow window)
	{
		return new AjaxSubmitLink(componentId, getForm())
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form form)
			{
				onAddLocale(target, locales);
				window.close(target);
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, Form form)
			{
				target.addComponent(feedback);
			}
		};
	}
	
	protected abstract void onAddLocale(AjaxRequestTarget target, List<Locale> locales);
}
