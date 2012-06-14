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
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

import com.userweave.components.customModalWindow.BaseModalWindowPage;

/**
 * Confirmation page for deletion of an entity.
 * 
 * @author opr
 */
public abstract class DeleteEntityPage extends BaseModalWindowPage
{
	/**
	 * Default constructor
	 * 
	 * @param modal
	 * 		Window to close after confirmation.
	 */
	public DeleteEntityPage(final ModalWindow modal, String entityName)
	{
		super(modal);
		
		addToForm(new Label("message", 
			new StringResourceModel(
				"deleteMessage", this, null,
				new Object[] { entityName })));
	}
	
	@Override
	protected WebMarkupContainer getAcceptButton(String componentId,
			final ModalWindow window)
	{
		return new AjaxLink(componentId)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				window.close(target);
				
				DeleteEntityPage.this.onDelete(target);
			}
		};
	}
	
	@Override
	protected IModel getAcceptLabel()
	{
		return new StringResourceModel("deleteButtonLabel", this, null);
	}
	
	/**
	 * Callback function to delete an entity.
	 * 
	 * @param target
	 * 		AjaxRequestTarget
	 * @param entity
	 * 		Entity to delete.
	 */
	protected abstract void onDelete(AjaxRequestTarget target);
}
