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
package com.userweave.ajax.form;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;

import com.userweave.ajax.IAjaxLocalizedStringUpdater;
import com.userweave.ajax.IAjaxUpdater;
import com.userweave.components.model.LocalizedModel;
import com.userweave.components.model.LocalizedPropertyModel;
import com.userweave.domain.LocalizedString;

public class AjaxBehaviorFactory
{
	/**
	 * Needed to save From components like textarea and textfields.
	 * 
	 * @param event 
	 * 		The javascript event trigger (mostly 'onblur' or 'onchange')
	 * @param ajaxEnabledComponent
	 * 		Component that makes an udate thorugh ajax behaviors.
	 * 
	 * @return
	 */
	public static AjaxFormComponentUpdatingBehavior getUpdateBehavior(
			final String event, final IAjaxUpdater ajaxEnabledComponent)
	{
		return new AjaxFormComponentUpdatingBehavior(event) 
		{	
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) 
			{
				ajaxEnabledComponent.onUpdate(target);
			}
			
			/**
			 * Disable the overlay, if the input field saves.
			 */
			@Override
			protected IAjaxCallDecorator getAjaxCallDecorator() 
			{	
				return  AjaxBehaviorFactory.getCallDecorator();
			}
		};
	}
	
	/**
	 * Creates a updating behavior to update a localized string.
	 * 
	 * @param event
	 * 		Event, that triggers the behavior.
	 * @param localeString
	 * 		Localized string to save.
	 * @return
	 */
	public static AjaxFormComponentUpdatingBehavior getUpdateBehaviorForLocalizedString(
			final String event, 
			final LocalizedString localeString, 
			final IAjaxLocalizedStringUpdater ajaxEnabledComponent) 
	{
		return new AjaxFormComponentUpdatingBehavior(event)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) 
			{
				ajaxEnabledComponent.onUpdate(target, localeString);
			}
			
			@Override
			protected IAjaxCallDecorator getAjaxCallDecorator() 
			{	
				return  AjaxBehaviorFactory.getCallDecorator();
			}
		};
	}
	
	public static AjaxFormComponentUpdatingBehavior getUpdateBehaviorForLocalizedString(
			final String event, 
			final LocalizedPropertyModel localeStringModel, 
			final IAjaxLocalizedStringUpdater ajaxEnabledComponent) 
	{
		return new AjaxFormComponentUpdatingBehavior(event)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) 
			{
				ajaxEnabledComponent.onUpdate(target, localeStringModel.getLocalizedString());
			}
			
			@Override
			protected IAjaxCallDecorator getAjaxCallDecorator() 
			{	
				return  AjaxBehaviorFactory.getCallDecorator();
			}
		};
	}
	
	public static AjaxFormComponentUpdatingBehavior getUpdateBehaviorForLocalizedString(
			final String event, 
			final LocalizedModel localeStringModel, 
			final IAjaxLocalizedStringUpdater ajaxEnabledComponent) 
	{
		return new AjaxFormComponentUpdatingBehavior(event)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) 
			{
				ajaxEnabledComponent.onUpdate(target, localeStringModel.getLocalizedString());
			}
			
			@Override
			protected IAjaxCallDecorator getAjaxCallDecorator() 
			{	
				return  AjaxBehaviorFactory.getCallDecorator();
			}
		};
	}
	
	/**
	 * Creates a call cecorator that disables the modal overlay 
	 * on event.
	 * @return
	 */
	public static IAjaxCallDecorator getCallDecorator()
	{
		IAjaxCallDecorator decorator = new IAjaxCallDecorator() 
		{
			private static final long serialVersionUID = 1L;

			@Override
			public CharSequence decorateOnFailureScript(
					Component component, CharSequence script) {
				
				return "disableModalOverlay();" + 
						script + 
						"enableModalOverlay();";
			}

			@Override
			public CharSequence decorateOnSuccessScript(
					Component component, CharSequence script) {
				
				return "disableModalOverlay();" + 
					   script + 
					   "enableModalOverlay();";
			}

			@Override
			public CharSequence decorateScript(
					Component component, CharSequence script) 
			{
				return "disableModalOverlay();" + 
					   script + 
					   "enableModalOverlay();";
			}
		};
		
		return decorator;
	}
}
