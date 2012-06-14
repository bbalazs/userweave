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

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;

import com.userweave.components.callback.EventHandler;
import com.userweave.components.customModalWindow.CustomModalWindow;
import com.userweave.domain.EntityBase;

/**
 * Extends the base functions with a copy function.
 * 
 * @author opr
 *
 */
public abstract class CopyEntityPanel<T extends EntityBase> extends BaseFunctionEditEntityPanel<T>
{
	private static final long serialVersionUID = 1L;

	/**
	 * Dialogs for copy.
	 */
	private CustomModalWindow copyModal;
	
	/**
	 * Default constructor.
	 * 
	 * @param id
	 * 		Component id.
	 * @param entity
	 * 		Entity to copy.
	 * @param callback
	 * 		Callback to fire on event.
	 */
	public CopyEntityPanel(String id, IModel<T> entityModel, EventHandler callback)
	{
		super(id, entityModel, callback);
		
		add(copyModal = getCopyModal(callback));
		
		AjaxLink<Void> copy = new AjaxLink<Void>("copy")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				copyModal.show(target);
			}
		};
		
		add(copy);
	}

	/**
	 * Creates a confirmation dialog to copy an entity.
	 * 
	 * @param entity
	 * 		Entity to copy.
	 * @return
	 * 		A CustomModalWindow.
	 */
	private CustomModalWindow getCopyModal(final EventHandler callback)
	{
		final CustomModalWindow copy = new CustomModalWindow("copyModal");
		
		copy.setTitle(getCopyModalTitle());
		
		copy.setInitialWidth(900);
		
		copy.setPageCreator(new ModalWindow.PageCreator()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page createPage()
			{
				return getCopyWebPage(callback, copy);
			}
		});
		
		copy.setWindowClosedCallback(new ModalWindow.WindowClosedCallback()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClose(AjaxRequestTarget target)
			{
				CopyEntityPanel.this.onClose(target);
			}
		});
		
		return copy;
	}
	
	protected void onClose(AjaxRequestTarget target) 
	{
		// do nothing, if window close callback is fired.
		// Override to change this behavior.
	}
	
	protected abstract IModel<String> getCopyModalTitle();
	
	
	/**
	 * Get a specific WebPage for copying the entity.
	 * 
	 * @return
	 */
	protected abstract WebPage getCopyWebPage(EventHandler callback, ModalWindow window);
}
