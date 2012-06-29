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

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.model.Model;

import com.userweave.components.valueListPanel.ValueListPanel;


/**
 * @author oma
 */
@SuppressWarnings("serial")
@Deprecated
public class ImageValueListPanel extends ValueListPanel<ByteArray> {

	public ImageValueListPanel(String id, ValueListController<ByteArray> cb) {
		super(id, cb);	
	}
	
	private ImageUpload imageUpload;
	
	@Override
	protected Component getInputComponent(String id) {
		imageUpload = new ImageUpload(id);
		return imageUpload; 			
	}
	
	@Override
	protected List<ByteArray> getInput() {	
		//return imageUpload.getImageByteArrays();
		return null;
	}



	@Override
	protected void clearInputComponent() {		
	}
	
	@Override
	protected Component getListComponent(String id, ByteArray value) {
		return new ImageDisplayPanel(id, value, 32, 32).setDefaultModel(new Model(value));
	}
}

