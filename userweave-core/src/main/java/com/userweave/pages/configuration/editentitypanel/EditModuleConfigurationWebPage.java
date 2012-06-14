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
package com.userweave.pages.configuration.editentitypanel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

import com.userweave.components.customModalWindow.BaseModalWindowPage;
import com.userweave.module.ModuleConfiguration;

public class EditModuleConfigurationWebPage extends BaseModalWindowPage
{
	public EditModuleConfigurationWebPage(IModel entityModel, final ModalWindow window)
	{
		super(window);
		
		setDefaultModel(entityModel);
		
		addToForm(new TextField("name"));
	}
	
	@Override
	protected WebMarkupContainer getAcceptButton(String componentId,
			final ModalWindow window)
	{
		return new AjaxButton(componentId, getForm())
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form form)
			{
				((ModuleConfiguration)EditModuleConfigurationWebPage.this.getDefaultModelObject()).save();
				window.close(target);
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form)
			{
			}
		};
	}
	
	@Override
	protected IModel getAcceptLabel()
	{
		return new StringResourceModel("save", this, null);
	}
}
