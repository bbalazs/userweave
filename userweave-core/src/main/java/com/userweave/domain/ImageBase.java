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
package com.userweave.domain;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Transient;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class ImageBase extends EntityBase {

	private static final long serialVersionUID = -1833014838636584326L;

	private byte[] imageData;

	private String mimeType;

	private String clientFileName;

	public byte[] getImageData() {
		return imageData;
	}

	public void setImageData(byte[] imageData) {
		this.imageData = imageData;
	}
	
	public String getMimeType() {
		return mimeType;
	}
	
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	
	public String getClientFileName() {
		return clientFileName;
	}
	
	public void setClientFileName(String clientFileName) {
		this.clientFileName = clientFileName;
	}

	private Integer version;
	
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
	@Transient
	public void incrementVersion() {
		if(version == null) {
			version =1;
		} else {
			version++;
		}
	}

	@Override
	public String toString() {
		return getClientFileName();
	}
	
	@Transient 
	public ImageBase copy() {
		ImageBase clone = new ImageBase();
		return copy(clone);
	}
	
	@Transient
	protected ImageBase copy(ImageBase clone) {
		clone.setClientFileName(clientFileName);
		clone.setImageData(imageData);
		clone.setMimeType(mimeType);
		
		// version is loosely coupled with the unique id. 
        // Perhaps we can skip the copying here?
		clone.setVersion(version);
		
		return clone;
	}

	@Transient
	public Dimension getDimension()
	{
		BufferedImage img = null;
		try
		{
			img = ImageIO.read(new ByteArrayInputStream(getImageData()));
		}
		catch(IOException e) { return null; }
		
		return new Dimension(img.getWidth(null),
							 img.getHeight(null));
	}
	
}
