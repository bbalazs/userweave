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
package com.userweave.pages.components.ajaxindicator;

import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.resource.PackageResourceReference;

/**
 * Simple panel to display an overlay with a 'loading flower'
 * after an ajax request has been made.
 * 
 * @author ipavkovic
 */
public class AjaxIndicator extends Panel 
{
	private static final long serialVersionUID = 1L;

	public AjaxIndicator(String markupId) 
	{
		super(markupId);
		
		add(new Image("flower", new PackageResourceReference(
				AjaxIndicator.class, "res/flower.gif")));
	}

	@Override
	public void renderHead(IHeaderResponse response)
	{
		// add javascript libraries
		response.renderJavaScriptReference(new PackageResourceReference(
				AjaxIndicator.class, "res/AjaxIndicator.js"));
		
		response.renderJavaScriptReference(new PackageResourceReference(
				AjaxIndicator.class, "res/initUpdateThread.js"));
		
		response.renderCSSReference(new PackageResourceReference(
				AjaxIndicator.class,"res/AjaxIndicator.css"));	
	}
}

