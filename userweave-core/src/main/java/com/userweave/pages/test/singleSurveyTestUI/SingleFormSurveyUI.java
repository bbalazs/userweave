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
package com.userweave.pages.test.singleSurveyTestUI;

import java.util.Locale;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.resolver.IComponentResolver;

import com.userweave.application.OnFinishCallback;
import com.userweave.domain.ModuleConfigurationEntityBase;
import com.userweave.pages.test.NextButton;
import com.userweave.pages.test.SurveyUI;

public abstract class SingleFormSurveyUI<T extends ModuleConfigurationEntityBase<?>> 
	extends SurveyUI<T> 
	implements IComponentResolver
{
	private static final long serialVersionUID = 1L;

	/**
	 * The wrapping form for all attached subelements.
	 */
	private final Form<Void> form;
	
	protected Form<Void> getForm() 
	{
		return form;
	}
	
	/**
	 * Markup id for form component.
	 */
	private static final String FORM_MARKUP_ID = "submitForm";
	
	/**
	 * Default constructor.
	 * 
	 * @param id
	 * 		Component markup id.
	 * @param configurationId
	 * 		Id of configuration to load on request.
	 * @param surveyExecutionId
	 * 		Id of current survey execution.
	 * @param onFinishCallback
	 * 		Callback to fire on finish event.
	 * @param locale
	 * 		The locale to display strings in.
	 */
	public SingleFormSurveyUI(
		String id, 
		int configurationId, 
		int surveyExecutionId, 
		OnFinishCallback onFinishCallback,
		Locale locale) 
	{
		super(id, configurationId, surveyExecutionId, onFinishCallback, locale);
		
		form = new Form<Void>(FORM_MARKUP_ID) 
		{
			private static final long serialVersionUID = 1L;
				
			@Override
			public void onSubmit() 
			{				
				SingleFormSurveyUI.this.onSubmit();
			}							
		};
		
		form.add(getNextButton());
		
		super.add(form);
	}
	
	/**
	 * Constructs the submit button for the form.
	 * 
	 * @return
	 */
	protected Component getNextButton()
	{
		return new NextButton("nextButton")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected String getImageColor()
			{
				return getColorNextButton();
			}
		};
	}
	
	/**
	 * @Override 
	 * 		isTransparentResolver doesn't exist anymore, so we add every
	 * 		component directly to the form.	
	 */
	@Override
	public MarkupContainer add(Component... childs)
	{
		for(Component c : childs)
		{
			getForm().add(c);
		}
		
		return this;
	}
	
	/**
	 * Since isTransparentResolver() has disaperade in wicket 1.5,
	 * we need to resolve manualy. 
	 */
	@Override
	public Component resolve(MarkupContainer container, MarkupStream markupStream, ComponentTag tag)
	{
		if(tag.getId().equals(FORM_MARKUP_ID))
		{
			return getForm();
		}
		
		Component resolvedComponent = getParent().get(tag.getId());
		if (resolvedComponent != null && getPage().wasRendered(resolvedComponent))
		{
			return null;
		}
		return resolvedComponent;
	}
	
	/**
	 * Handle the submit event here.
	 */
	protected abstract void onSubmit();
}
