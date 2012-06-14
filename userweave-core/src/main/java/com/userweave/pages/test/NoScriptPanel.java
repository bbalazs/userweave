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
package com.userweave.pages.test;

import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.html.panel.Panel;

import com.userweave.application.images.ImageResource;
import com.userweave.domain.ImageBase;
import com.userweave.pages.imprint.ImprintPanel;

/**
 * Dummy class to deliver the noscript content
 * 
 * @author opr
 */
public class NoScriptPanel extends Panel 
{
	private static final long serialVersionUID = 1L;

	private static final String NOSCRIPT_LOGO = "noscript_logo";
	
	private Image noscript_logo;
	
	public NoScriptPanel(String id)
	{
		super(id);
		
		add(noscript_logo = new NonCachingImage(NOSCRIPT_LOGO));
		
		add(new ImprintPanel("imprintPanel_noscript"));
	}
	
	public void replaceNoScriptLogo(ImageBase image)
	{
		Image newNoscriptLogo = ImageResource.createImage(NOSCRIPT_LOGO, image);
		
		noscript_logo.replaceWith(newNoscriptLogo);
		noscript_logo = newNoscriptLogo;
	}
}
