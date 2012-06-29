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
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.PackageResourceReference;

import com.userweave.components.customModalWindow.CustomModalWindow;
import com.userweave.pages.base.BaseUserWeavePage;
import com.userweave.pages.test.jquery.JQuery;

public class ChangeNameNotImplementedPage extends WebPage 
{
	private static final long serialVersionUID = 1L;

	public ChangeNameNotImplementedPage(final CustomModalWindow window) 
	{
		add(new AjaxLink<Void>("ok") 
		{
			private static final long serialVersionUID = 1L;

			@Override
            public void onClick(AjaxRequestTarget target) 
			{
            	window.close(target);
            }
        });
	}

	@Override
	public void renderHead(IHeaderResponse response)
	{
		response.renderCSSReference(new PackageResourceReference(
				BaseUserWeavePage.class, "configBase.css"));
	
		response.renderJavaScriptReference(JQuery.getLatest());
		
		response.renderJavaScriptReference(
				new JavaScriptResourceReference(
					BaseUserWeavePage.class,
					"hover.js"));
	}
}
