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
package com.userweave.components.image;

import java.awt.Dimension;
import java.awt.Image;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.swing.ImageIcon;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.userweave.components.authorization.AuthOnlyAjaxLink;
import com.userweave.components.customModalWindow.CustomModalWindow;
import com.userweave.components.image.uploadpage.IconUploadPage;
import com.userweave.components.valueListPanel.ValueListPanel.ValueListController;
import com.userweave.components.valueListPanel.imageValueListPanel.ByteArray;
import com.userweave.components.valueListPanel.imageValueListPanel.ImageDisplayPanel;
import com.userweave.components.valueListPanel.imageValueListPanel.UploadedImage;

/**
 * @author oma
 */
public class ImageListPanel extends Panel 
{
	private static final long serialVersionUID = 1L;

	private final static Logger logger = LoggerFactory.getLogger(ImageListPanel.class);
	
	private final int MAX_WIDTH = 64;
	private final int MAX_HEIGHT = 64;
	
	private Component imageContainer;
	
	/**
	 * @bugfix: support for jpeg images (#906).
	 * 		Because jpeg has four characters, extend three character
	 * 		suffix with "."
	 */
	private static final List<String> supportedImages = 
		Arrays.asList(new String[] {".gif", ".jpg", "jpeg", ".png"});
	
	private CustomModalWindow uploadModal;
	
	public ImageListPanel(
		String id, final ValueListController<ByteArray> controller) 
	{
		super(id);
		
		addUploadModal(controller);
		
		add(new AuthOnlyAjaxLink("upload")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				uploadModal.show(target);
			}
		});
		
		add(imageContainer = createImageContainer(controller));
		
		imageContainer.setOutputMarkupId(true);
	}

	private WebMarkupContainer createImageContainer(
		final ValueListController<ByteArray> controller) 
	{
		final WebMarkupContainer imageContainer = new WebMarkupContainer("imageContainer");
		
		imageContainer.setOutputMarkupId(true);
		
		final RepeatingView images = new RepeatingView("repeater");
		
		for (final ByteArray byteArray : controller.getValues()) 
		{	
			final WebMarkupContainer image = new WebMarkupContainer(images.newChildId());
			
			ImageDisplayPanel imagePanel = new ImageDisplayPanel("image", byteArray, 0, 0);
			
			imagePanel.setDefaultModel(new Model<ByteArray>(byteArray));
			
			Dimension d = controller.getLargestDimension();
			
			imagePanel.add(
				new AttributeAppender(
					"style",  
					new Model<String>("width:" + d.width + "px;height:" + d.height + "px"), 
					";"));
			
			image.add(imagePanel);				
			
			image.add(
				new AuthOnlyAjaxLink("delete") 
				{
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						controller.removeValue(byteArray);	
						
						images.remove(image);
						
						target.add(imageContainer);
					}					
				}
			);
			
			images.add(image);
		}
		
		imageContainer.add(images);
		
		return imageContainer;
	}
	
//	private void replaceImageContainer()
//	{
//		
//	}
	
	private void addUploadModal(final ValueListController<ByteArray> controller)
	{
		uploadModal = new CustomModalWindow("uploadModal");
		
		add(uploadModal);
		
		uploadModal.setTitle(new StringResourceModel("uploadIcons", ImageListPanel.this, null));
		
		uploadModal.setPageCreator(new ModalWindow.PageCreator()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page createPage()
			{
				return new IconUploadPage(uploadModal)
				{
					private static final long serialVersionUID = 1L;

					@Override
					protected void onSubmit(FileUploadField fileUploadField) throws Exception
					{
						List<UploadedImage> input = getUploadedImages(fileUploadField);
						
						for (UploadedImage t : input) 
						{	
							Image inImage = new ImageIcon(t.getByteArray()).getImage();
					        
					        if (inImage.getWidth(null) < 1 || 
					        	inImage.getWidth(null) > MAX_WIDTH || 
					        	inImage.getHeight(null) < 1 || 
					        	inImage.getHeight(null) > MAX_HEIGHT)
					        {
					        	String imageCutAcrossMaxSizeMessageFormat = 
									new StringResourceModel("icon_cut_across_max_size", ImageListPanel.this, null).getString();
					        	
					    		String message = String.format(
					    				imageCutAcrossMaxSizeMessageFormat, 
					    				t.getFileName(),
					    				MAX_WIDTH,
					    				MAX_HEIGHT
					    			);
							
					    		throw new Exception(message);
//					    		warn( message );
					        }
					        else 
					        {
					        	controller.addValue(t);
					        }
						}
					}
				};
			}
		});
		
		uploadModal.setWindowClosedCallback(new ModalWindow.WindowClosedCallback()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClose(AjaxRequestTarget target)
			{
				Component replacement = createImageContainer(controller);
				
				imageContainer.replaceWith(replacement);
				
				imageContainer = replacement;	
				
				target.add(imageContainer);
			}
		});
	}
	
	private List<UploadedImage> getUploadedImages(FileUploadField fileUploadField) 
	{
		FileUpload fileUpload = fileUploadField.getFileUpload();
		
		if (isZipFile(fileUpload)) 
		{
			return extractUploadedImagesFromZipFile(fileUpload);
		} 
		else 
		{		
			if (isImage(fileUpload.getClientFileName())) {
				return Collections.singletonList(new UploadedImage(null, fileUpload.getBytes(), fileUpload.getClientFileName()));
			} else {
				warn(new StringResourceModel("notSupportedImage", this, null).getString());
				return Collections.emptyList();				
			}
		}	
	}
	
	private boolean isZipFile(FileUpload fileUpload) 
	{
		return fileUpload.getContentType().equals("application/zip");
	}
	
	@SuppressWarnings("unchecked")
	private Enumeration<ZipEntry> getEntries(ZipFile zipFile) 
	{
		return zipFile.getEntries();
	}
	
	private boolean isImage(String name) 
	{
		String suffix = name.substring(
				name.length() - 4, name.length()).toLowerCase();		
		
		return supportedImages.contains(suffix);	
	}
	
	private byte[] readByteArray(InputStream inputStream) throws IOException 
	{
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		
		byte[] buf = new byte[1000];
		
		int len = 0;
		while( ( len = inputStream.read(buf) ) != - 1 ) {
			output.write( buf, 0, len );				
		}
						
		output.close();
		
		return output.toByteArray();
	}
	
	private List<UploadedImage> extractUploadedImagesFromZipFile(FileUpload fileUpload) 
	{
		List<UploadedImage> uploadedImages = new ArrayList<UploadedImage>();

		File tempFile = null;
		try {
			tempFile = fileUpload.writeToTempFile();
			tempFile.deleteOnExit();

			ZipFile zipFile = new ZipFile(tempFile);
			for(Enumeration<ZipEntry> e = getEntries(zipFile); e.hasMoreElements(); ) {
				ZipEntry zipEntry = e.nextElement();
				if (isImage(zipEntry.getName())) {
					InputStream inputStream = zipFile.getInputStream(zipEntry);
					uploadedImages.add(
						new UploadedImage(
							null, readByteArray(inputStream), zipEntry.getName()));
					inputStream.close();
				}
				else
				{
					warn(new StringResourceModel("notSupportedImageInZipFile", this, null, new Object[] { zipEntry.getName()
							
					}).getString());
				}
			}				
			return uploadedImages;
		} catch (IOException e) {
			error(new StringResourceModel("unexpectedErrorDuringExtractionOfZipFile", this, null).getString());
			if (logger.isErrorEnabled()) {
				logger.error(new StringResourceModel("unexpectedErrorDuringExtractionOfZipFile", this, null).getString(), e);
			}
			return Collections.emptyList();	
		} finally {
			if(tempFile != null) {
				tempFile.delete();
			}
		}
	}
}

