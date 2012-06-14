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
package com.userweave.components.valueListPanel.imageValueListPanel;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.DynamicImageResource;

/**
 * Panel to display an icon on a tile in the 
 * icon usability configurations view.
 * 
 * @author oma
 */
public class ImageDisplayPanel extends Panel 
{
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor
	 * 
	 * @param id
	 * 		Component markup id.
	 * @param byteArray
	 * 		Image data.
	 * @param width
	 * 		If larger than 0, the image will be scaled on this width,
	 * 		if the height value is also larger than 0.
	 * @param height
	 * 		If larger than 0, the image will be scaled on this height,
	 * 		if the width value is also larger than 0.
	 */
	public ImageDisplayPanel(
		String id, final ByteArray byteArray, 
		final int width, final int height) 
	{
		super(id);
		
		NonCachingImage image = 
			new NonCachingImage("image", 
				new DynamicImageResource() 
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected byte[] getImageData(Attributes attributes)
				{
					return byteArray.getByteArray();
				}
			});
		
		add(image);
		
		// show image in original size if width and height equals 0
		if(width != 0 && height != 0)
		{
			image.add(new AttributeAppender(
				"style", 
				new Model<String>(
						"width:" + Integer.toString(width) + "px;" +
						"height:" + Integer.toString(height) + "px;"),
				";"));
		}
	}
}

