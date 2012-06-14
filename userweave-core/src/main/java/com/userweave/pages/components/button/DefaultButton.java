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
package com.userweave.pages.components.button;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

/**
 * a simple designed button for
 * mostly save actions.
 * 
 * @author opr
 *
 */
@SuppressWarnings("serial")
@Deprecated
public class DefaultButton extends Panel {

	private final Button button;
	
	public DefaultButton(String id, IModel buttonTextModel, Form form ) {
		super(id);
		
		add(button = new IndicatingAjaxButton("save", form) {

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form form) {
				DefaultButton.this.onSubmit(target, form);
			}
					
			@Override
			protected void onError(AjaxRequestTarget target, Form form)	{
				DefaultButton.this.onError(target, form);	
			}
		});

		if(buttonTextModel == null) {
			button.setModel(new StringResourceModel("save", this, null));
		} else {
			button.setModel(buttonTextModel);
		}
	}
	
	public DefaultButton(String id, Form form) {
		this(id, null, form);
	}
	
		
	protected void onSubmit(AjaxRequestTarget target, Form form) {
		
	}
	
	protected void onError(AjaxRequestTarget target, Form form) {
		
	}
	
	public Button getButton()
	{
		return button;
	}
	
}
