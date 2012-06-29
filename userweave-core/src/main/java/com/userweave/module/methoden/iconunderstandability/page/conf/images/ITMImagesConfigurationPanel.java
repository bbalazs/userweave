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
package com.userweave.module.methoden.iconunderstandability.page.conf.images;


import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.imageio.ImageIO;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.components.image.ImageListPanel;
import com.userweave.components.valueListPanel.ValueListPanel.ValueListController;
import com.userweave.components.valueListPanel.imageValueListPanel.ByteArray;
import com.userweave.dao.StudyDependendDao;
import com.userweave.module.methoden.iconunderstandability.dao.IconTermMatchingConfigurationDao;
import com.userweave.module.methoden.iconunderstandability.domain.ITMImage;
import com.userweave.module.methoden.iconunderstandability.domain.IconTermMatchingConfigurationEntity;
import com.userweave.pages.configuration.DisableComponentIfStudyStateNotInitVisitor;
import com.userweave.pages.configuration.DisableLinkVisitor;
import com.userweave.pages.configuration.module.ModuleConfigurationPanelBase;

/**
 * Configuration view for the icons of an icon usability test. 
 * 
 * @author oma
 */
public class ITMImagesConfigurationPanel 
	extends ModuleConfigurationPanelBase<IconTermMatchingConfigurationEntity> 
	implements ValueListController<ByteArray> 
{
	private static final long serialVersionUID = 1L;

	@SpringBean
	private IconTermMatchingConfigurationDao dao;

	/**
	 * Default constructor.
	 * 
	 * @param id
	 * 		Component markup id.
	 * @param configurationId
	 * 		Id of icon usability configuration.
	 * @param studyLocale
	 * 		Locale of underlying study.
	 */
	public ITMImagesConfigurationPanel(
		String id, final Integer configurationId, Locale studyLocale) 
	{
		super(id, configurationId, studyLocale);
	
		add(new ImageListPanel("imageListPanel", this));
	}
	
	@Override
	protected void onBeforeRender() 
	{
		IModel studyStateModel = new PropertyModel(getDefaultModel(), "study.state");
		visitChildren(new DisableLinkVisitor(studyStateModel));
		visitChildren(new DisableComponentIfStudyStateNotInitVisitor(studyStateModel));
		super.onBeforeRender();
	}
	
	@Override
	protected StudyDependendDao<IconTermMatchingConfigurationEntity> getConfigurationDao() {
		return dao;
	}
	
	public void changeValue(ByteArray oldValue, ByteArray newValue) {
	}

	@Override
	public List<ByteArray> getValues() {
		List<ByteArray> rv = new ArrayList<ByteArray>();
		
		List<ITMImage> images = getConfiguration().getImages();
		if (images != null) {
			for (ITMImage image : images) {
				rv.add(new ByteArray(image.getId(), image.getImageData()));
			}
		}		
		return rv;
	}

	@Override
	public String validate(ByteArray value) {
		return null;
	}
	
	/**
	 * @see #1246. 
	 * 		Uploaded icons can be of arbitrary size (max. 64px x 64px)
	 * 		Bounds checking occures in ImageListPanel, so we only have 
	 * 		to add the byte array.
	 */
	@Override
	public void addValue(ByteArray value) 
	{
		// do bounds checking of image
		//Dimension d = getConfiguration().getDimiensionOfIcons();
		
		BufferedImage img = null;
		try
		{
			img = ImageIO.read(new ByteArrayInputStream(value.getByteArray()));
			
			addImageToConfiguration(value);
			
//			if(d == null ||
//			  (img.getHeight() == d.getHeight() &&
//			   img.getWidth() == d.getWidth()))
//			{
//				addImageToConfiguration(value);
//			}
//			else
//			{
//				warn(new StringResourceModel("bad_image", this, null).getString());
//			}
		}
		catch(IOException e)
		{
			warn( new StringResourceModel("broken_image", this, null).getString() );
		}
	}
	
	@Override
	public void removeValue(ByteArray value) {
		removeImageFromConfiguration(value);				
	}

	@Override
	public Dimension getLargestDimension()
	{
		return getConfiguration().getDimiensionOfIcons();
	}
	
	private void addImageToConfiguration(ByteArray bytes) {		
		ITMImage image = new ITMImage();
		image.setImageData(bytes.getByteArray());
		image.incrementVersion();
		getConfiguration().addToImages(image);
		saveConfiguration();		
		
		bytes.setId(image.getId());
	}

	private void removeImageFromConfiguration(ByteArray bytes) {
		getConfiguration().removeFromImages(bytes.getId());
		saveConfiguration();
	}

}

