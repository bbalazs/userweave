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
/**
 * 
 */
package com.userweave.components.customModalWindow;

import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

/**
 * Extends the wicket modal window class
 * for styles purposes only.
 * 
 * @author opr
 *
 */
public class CustomModalWindow extends ModalWindow 
{
	private static final long serialVersionUID = 1L;

	public static int defaultWidth = 472;
	
	public static int defaultHeight = 350;
	
	public final static String CSS_CLASS_CUSTOM = "w_custom";
	
	private static final ResourceReference CSS = 
		new PackageResourceReference(CustomModalWindow.class, "res/customModal.css");
	
	public CustomModalWindow(String id)
	{
		super(id);
		
		setCssClassName(CustomModalWindow.CSS_CLASS_CUSTOM);
		setInitialHeight(CustomModalWindow.defaultHeight);
		setInitialWidth(CustomModalWindow.defaultWidth);
	}
	
	@Override
	public void renderHead(final IHeaderResponse response)
	{
		super.renderHead(response);
		
		response.renderCSSReference(CSS);
	}
	
}
