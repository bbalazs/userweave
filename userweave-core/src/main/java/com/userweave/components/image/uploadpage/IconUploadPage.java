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
package com.userweave.components.image.uploadpage;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

import com.userweave.components.customModalWindow.BaseModalWindowPage;

/**
 * Web page for modal windows to upload an image file.
 * 
 * @author opr
 *
 */
public abstract class IconUploadPage extends BaseModalWindowPage
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Display panel for the file upload.
	 * 
	 * @author opr
	 */
	private class FileUploadFragment extends Fragment
	{
		private static final long serialVersionUID = 1L;

		/**
		 * The fle upload field for uploading an image.
		 */
		private FileUploadField fileUploadField;
		
		public FileUploadField getFileUploadField()
		{
			return fileUploadField;
		}
		
		/**
		 * Default constructor.
		 * 
		 * @param id
		 * 		Component markup id.
		 */
		public FileUploadFragment(String id)
		{
			super(id, "fileUpload", getForm());
			
			add(fileUploadField = new FileUploadField("imageFile"));
		}
	}
	
	/**
	 * Message panel to display a success message after successful
	 * file upload.
	 * 
	 * @author opr
	 *
	 */
	private class MessageFragment extends Fragment
	{
		private static final long serialVersionUID = 1L;

		/**
		 * Default constructor.
		 * 
		 * @param id
		 * 		Component markup id.
		 */
		public MessageFragment(String id)
		{
			super(id, "fileUploadMessage", getForm());
		}
	}
	
	/**
	 * The displayed content.
	 */
	private Component content;
	
	/**
	 * Reference to the submit button. Wll be invisible after 
	 * successful fileupload.
	 */
	private WebMarkupContainer submitButton;

	/**
	 * Feedbackpanel for error messages.
	 */
	private final FeedbackPanel feedback;
	
	/**
	 * Default constructor
	 * 
	 * @param window
	 * 		Modal window this page is attached to.
	 */
	public IconUploadPage(ModalWindow window)
	{
		super(window);
		
		addToForm(content = new FileUploadFragment("content"));
		
		feedback = new FeedbackPanel("feedback");
		
		addToForm(feedback);
	}

	@Override
	protected WebMarkupContainer getAcceptButton(String componentId,
			final ModalWindow window)
	{	
		return submitButton = new SubmitLink(componentId, getForm())
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit()
			{
				try
				{
					IconUploadPage.this.onSubmit(
						((FileUploadFragment) content).getFileUploadField());
					
					replaceAfterUpload();
					super.onSubmit();
				}
				catch (Exception e)
				{
					warn(e.getMessage());
				}
			}
			
		};
	}
	
	/**
	 * Replaces the displayed content and hides the submit button.
	 */
	private void replaceAfterUpload()
	{
		Fragment replacement = new MessageFragment("content");
		
		content.replaceWith(replacement);
		
		content = replacement;
		
		submitButton.setVisible(false);
	}
	
	@Override
	protected IModel<String> getAcceptLabel()
	{
		return new StringResourceModel("upload", this, null);
	}
	
	@Override
	protected IModel<String> getDeclineLabel()
	{
		return new StringResourceModel("close", this, null);
	}
	
	/**
	 * Processes the file upload.
	 * 
	 * @param fileUploadField
	 * 		The field containing the submitted data.
	 * 
	 * @throws Exception
	 * 		On error, throw an exception to display a warn
	 * 		message on the feedback panel.
	 */
	protected abstract void onSubmit(FileUploadField fileUploadField) throws Exception;
}
