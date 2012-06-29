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
package com.userweave.module.methoden.iconunderstandability.page.report;

import java.sql.Blob;

import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.image.resource.BlobImageResource;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.hibernate.lob.BlobImpl;

import com.userweave.module.methoden.iconunderstandability.dao.ITMImageDao;

/**
 * @author oma
 */
@SuppressWarnings("serial")
public class ImageWrapper extends Panel {
	
	@SpringBean
	private ITMImageDao itmImageDao;
	
	public ImageWrapper(String id, final int imageId) {
		super(id);
		
		add( new Image("image", 
				new BlobImageResource() {

					@Override
					protected Blob getBlob() {
						return new BlobImpl(itmImageDao.findById(imageId).getImageData());
					}
				}
			)
		);
	}
}

