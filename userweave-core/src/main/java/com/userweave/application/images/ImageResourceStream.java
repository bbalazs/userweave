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
package com.userweave.application.images;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.wicket.util.resource.AbstractResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;

import com.userweave.domain.ImageBase;

@SuppressWarnings("serial")
public class ImageResourceStream extends AbstractResourceStream {

	private final ImageBase image;
	
	/** Resource stream */
	private transient InputStream inputStream;

	public ImageResourceStream(ImageBase image) {
		this.image = image;
	}
	
	
	@Override
	public void close() throws IOException {
		if(inputStream != null) {
			inputStream.close();
			inputStream = null;
		}
		
	}

	@Override
	public InputStream getInputStream() throws ResourceStreamNotFoundException {
		if (inputStream == null) {
			if(image == null || image.getImageData() == null) {
				throw new ResourceStreamNotFoundException("Image not found");
			}
			inputStream = new ByteArrayInputStream(image.getImageData());
		}

		return inputStream;
	}

}
