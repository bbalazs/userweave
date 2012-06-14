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
package com.userweave.components.imageUpload;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.resource.DynamicImageResource;

import com.userweave.components.authorization.AuthOnlyAjaxLink;
import com.userweave.components.customModalWindow.CustomModalWindow;
import com.userweave.components.image.uploadpage.IconUploadPage;
import com.userweave.domain.StudyState;
import com.userweave.module.methoden.iconunderstandability.page.conf.images.ImageUploadCallback;
import com.userweave.pages.configuration.EnabledInStudyState;

/**
 * Panel to upload an icon for study or project configuration.
 * 
 * @author opr
 *
 */
public class IconUploadPanel extends Panel 
{
	private static final long serialVersionUID = 1L;
	
	@EnabledInStudyState(states={StudyState.INIT})
	private abstract class IconActionLink extends AuthOnlyAjaxLink
	{
		private static final long serialVersionUID = 1L;

		public IconActionLink(String id)
		{
			super(id);
		}
		
		public IconActionLink(String id, ToolTipType type)
		{
			super(id, type);
		}
	}
	/**
	 * Icon upload modal window.
	 */
	private CustomModalWindow iconUpload;
	
	/**
	 * Callback to fire on after image upload.
	 */
	private final ImageUploadCallback imageUpload;
	
	private NonCachingImage image;
	
	/**
	 * Default constructor.
	 * 
	 * @param id
	 * @param imageUpload
	 */
	public IconUploadPanel(String id, ImageUploadCallback imageUpload) 
	{
		super(id);
		
		this.imageUpload = imageUpload;
		
		init(0, 0);
	}

	/**
	 * Constructor to set the image dimensions.
	 * 
	 * @param id
	 * @param width
	 * @param heigth
	 * @param imageUpload
	 */
	public IconUploadPanel(String id, int width, int heigth, ImageUploadCallback imageUpload) 
	{
		super(id);
	
		this.imageUpload = imageUpload;
		
		init(width, heigth);
	}
	
	private void init(final int width, final int height)
	{
		setOutputMarkupId(true);
		
		addIconUploadModal(width, height);
		
		add(new IconActionLink("upload")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				iconUpload.show(target);
			}	
		});
		
		add(new IconActionLink("delete")
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick(AjaxRequestTarget target) 
			{	
				imageUpload.remove();
				target.add(IconUploadPanel.this);
				
				replaceImage(target, width, height);
			}
		});
		
		
		addImage(width, height);
	}
	
	private void addIconUploadModal(final int width, final int height)
	{
		iconUpload = new CustomModalWindow("iconUploadModal");
		
		add(iconUpload);
		
		iconUpload.setTitle(new StringResourceModel("uploadFile", IconUploadPanel.this, null));
		
		iconUpload.setPageCreator(new ModalWindow.PageCreator()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page createPage()
			{
				return new IconUploadPage(iconUpload)
				{
					private static final long serialVersionUID = 1L;

					@Override
					protected void onSubmit(FileUploadField fileUploadField) throws Exception
					{
						FileUpload fileUpload = 
							fileUploadField.getFileUpload();
						
						if (fileUpload != null) 
						{
							imageUpload.saveFileUpload(fileUpload);
						}
					}
				};
			}
		});
		
		iconUpload.setWindowClosedCallback(new ModalWindow.WindowClosedCallback()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClose(AjaxRequestTarget target)
			{
				target.add(IconUploadPanel.this);
				
				replaceImage(target, width, height);
			}
		});
	}
	
	private void addImage(int width, int height)
	{
		add(image = getImage(width, height));
	}
	
	private void replaceImage(AjaxRequestTarget target, int width, int height)
	{
		NonCachingImage replacement = getImage(width, height);
		
		image.replaceWith(replacement);
		
		image = replacement;
		
		target.add(image);
	}
	
	private NonCachingImage getImage(int width, int height)
	{
		NonCachingImage image = new NonCachingImage("image", new DynamicImageResource()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected byte[] getImageData(Attributes attributes)
			{
				byte[] imageData = imageUpload.getImageData();
				if (imageData != null)
				{
					return imageData;
				}
				else
				{
					return new byte[] {};
				}
			}
		});

		if ((height > 0) && (width > 0))
		{
			image.add(new AttributeModifier("style", new Model<String>(
					"width: " + 
					Integer.toString(width) + "px;" + "height: " +
					Integer.toString(height) + "px;")));
		}
		
		image.setOutputMarkupId(true);
		
		return image;
	}
}