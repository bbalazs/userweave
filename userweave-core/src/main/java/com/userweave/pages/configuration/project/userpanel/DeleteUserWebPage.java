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
package com.userweave.pages.configuration.project.userpanel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

import com.userweave.components.customModalWindow.BaseModalWindowPage;
import com.userweave.domain.Project;
import com.userweave.domain.User;

public abstract class DeleteUserWebPage extends BaseModalWindowPage
{
	public DeleteUserWebPage(final User user, final Project project, final ModalWindow window)
	{
		super(window);
		
		addToForm(new Label("deleteUser",
				new StringResourceModel(
					"deleteUser_text", 
					this, null, 
					new Object[] 
					{
						user.getForename(),
						user.getSurname(),
						project.getName()
					})));
	}
	
	@Override
	protected WebMarkupContainer getAcceptButton(String componentId,
			ModalWindow window)
	{
		return new AjaxLink(componentId)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				onDelete(target);
			}	
		};
	}
	
	@Override
	protected IModel getAcceptLabel()
	{
		return new StringResourceModel("deleteUser", this, null);
	}
	
	protected abstract void onDelete(AjaxRequestTarget target);
}
