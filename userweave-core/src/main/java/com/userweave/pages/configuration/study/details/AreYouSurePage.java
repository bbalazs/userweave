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
package com.userweave.pages.configuration.study.details;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.StringResourceModel;

import com.userweave.components.customModalWindow.BaseModalWindowPage;
import com.userweave.domain.StudyState;

public abstract class AreYouSurePage extends BaseModalWindowPage 
{	
	final StudyState state;
	
	public AreYouSurePage(final ModalWindow window, final StudyState state)
	{
		super(window);
		
		this.state = state;
		
		if(state == StudyState.INIT)
		{
			window.setTitle(new StringResourceModel("init_need", this, null));
			
			addToForm(new Label("sure", new StringResourceModel("activate_sure",this,null)));
			addToForm(new Label("testing", new StringResourceModel("activate_testing",this,null)));
		}
		else
		{
			window.setTitle(new StringResourceModel("running_need", this, null));
			
			addToForm(new Label("sure", new StringResourceModel("close_sure",this,null)));
			addToForm(new Label("testing", new StringResourceModel("close_testing",this,null)));
		}
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
            	onOk(target);
            }
        };
	}
	
	protected abstract void onOk(AjaxRequestTarget target);
}
