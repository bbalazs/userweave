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

import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValueConversionException;

import com.userweave.dao.ImageDao;
import com.userweave.domain.ImageBase;

public class ImageResource //extends WebResource 
	extends DynamicImageResource
{
	
	@SpringBean
	private ImageDao imageDao;
	
	public final static String IMAGE_RESOURCE = "MyWellKonwonwblablaUNique!";
	
//	@Override
//	public IResourceStream getResourceStream() {
//		if(imageDao == null) {
//			Injector.get().inject(this);
//		}
//		
//		ImageBase image = null;
//		try {
//			Integer id = getParameters().getInt("0");
//			image = imageDao.findById(id);
//		} catch(StringValueConversionException e) {
//			// TODO: warn here? who knows
//		}
//		
//		return new ImageResourceStream(image);
//	}


	public static ResourceReference getResourceReference() 
	{
		return new PackageResourceReference(IMAGE_RESOURCE);
	}

	public static Image createImage(String id, ImageBase image) 
	{	
		//Map<String, Object> map = new HashMap<String, Object>();
		PageParameters parameters = new PageParameters();
		
		Integer imageid = 0;
		if(image != null && image.getId() != null) 
		{
			imageid = image.getId();
		}
		
		parameters.set(0, imageid);
		
		if(image != null && image.getVersion() != null) 
		{
			parameters.set(1, image.getVersion());
		}
		
		
		return new Image(id, ImageResource.getResourceReference(), parameters);	
	}


	@Override
	protected byte[] getImageData(Attributes attributes)
	{
		if(imageDao == null) 
		{
			Injector.get().inject(this);
		}
		
		ImageBase image = null;
		
		try 
		{
			Integer id = attributes.getParameters().get(0).toInteger();
			image = imageDao.findById(id);
		} 
		catch(StringValueConversionException e) 
		{
			// TODO: warn here? who knows
		}
		
		return 	image.getImageData();
	}


}
