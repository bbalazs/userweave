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
package com.userweave.components.customModalWindow;

import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

import com.userweave.pages.test.jquery.JQuery;

/**
 * Implements a javascript scrollable page.
 * 
 * @author opr
 *
 */
public class ScrollablePopupPage extends WebPage 
{
	public ScrollablePopupPage()
	{
		super();
	}
	
	@Override
	public void renderHead(IHeaderResponse response)
	{
		response.renderJavaScriptReference(JQuery.getLatest());
		
		response.renderJavaScriptReference(
				new JavaScriptResourceReference(
						ScrollablePopupPage.class, "res/jquery.mousewheel.min.js"));
		
		response.renderJavaScriptReference(
				new JavaScriptResourceReference(
						ScrollablePopupPage.class, "res/jScrollPane.js"));
		
		response.renderJavaScriptReference(
				new JavaScriptResourceReference(
						ScrollablePopupPage.class, "res/initScrollPane.js"));
	}
}
