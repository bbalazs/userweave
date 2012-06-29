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
package com.userweave.module.methoden.questionnaire.page.conf;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.panel.Panel;

import com.userweave.components.customModalWindow.CustomModalWindow;
import com.userweave.pages.components.button.AddLink;
import com.userweave.pages.components.button.AuthOnlyAddLink;

/**
 * @author oma
 */
@SuppressWarnings("serial")
public abstract class AddButtonWithModalWindow extends Panel 
{
	private CustomModalWindow addModalWindow;

	public AddButtonWithModalWindow(String id) 
	{
		super(id);
	
		add(new AuthOnlyAddLink("add", AddLink.ADD_QUESTION)
		{
			@Override
			protected void onClick(AjaxRequestTarget target)
			{
				addModalWindow.show(target);
			}
		});
		
		add(addModalWindow = createAddModalWindow());
		//addModalWindow.setInitialHeight(330);
	}

	private CustomModalWindow createAddModalWindow() 
	{
		final CustomModalWindow modalWindow = new CustomModalWindow("addModalWindow");
		
		//modalWindow.setPageMapName("modal-1");

		//modalWindow.setInitialHeight(310);
		
        modalWindow.setPageCreator(new ModalWindow.PageCreator() {
            public Page createPage() {
               return AddButtonWithModalWindow.this.createPage(modalWindow);
            }
        });
        
        modalWindow.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
            public void onClose(AjaxRequestTarget target) { 
            	AddButtonWithModalWindow.this.onClose(target);
            }
        });

        
        modalWindow.setCloseButtonCallback(new ModalWindow.CloseButtonCallback() {
            public boolean onCloseButtonClicked(AjaxRequestTarget target) {
                return true;
            }
        });

      return modalWindow;
	}
	
	protected abstract Page createPage(CustomModalWindow modalWindow);
	
	protected abstract void onClose(AjaxRequestTarget target);
}

