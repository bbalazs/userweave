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
package com.userweave.pages.configuration.study.details.popups.registration;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.resource.PackageResourceReference;

import com.userweave.pages.base.BaseUserWeavePage;
import com.userweave.pages.test.jquery.JQuery;

public abstract class AskForContinueRegistrationPage extends WebPage {

	private AjaxLink yes;
	
	public AskForContinueRegistrationPage(final ModalWindow window)
	{
		add(yes = new AjaxLink("yes") 
		{
            @Override
            public void onClick(AjaxRequestTarget target) {
            	window.close(target);
            	onOk(target);
            }
        });
	}
	
	@Override
	public void renderHead(IHeaderResponse response)
	{
		response.renderCSSReference(new PackageResourceReference(
				BaseUserWeavePage.class, "configBase.css"));
		
		response.renderJavaScriptReference(JQuery.getLatest());
		
		response.renderJavaScriptReference(new PackageResourceReference(
				BaseUserWeavePage.class,"hover.js"));
	}
	
	protected abstract void onOk(AjaxRequestTarget target);
}
