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

import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.Panel;

import com.userweave.domain.Role;

@SuppressWarnings("serial")
@AuthorizeAction(action = Action.RENDER, roles = {Role.PROJECT_ADMIN, Role.PROJECT_PARTICIPANT})
@Deprecated
public class ImageUpload extends Panel {

//	private final static Logger logger = LoggerFactory.getLogger(ImageUpload.class);
	
	
	private FileUploadField fileUploadField;
	
	public ImageUpload(String id) {
		super(id);
		init();
	}
		
	private void init() {
		add(fileUploadField = new FileUploadField("imageFile"));
		fileUploadField.setRequired(true);
	}
	
//	public List<ByteArray> getImageByteArrays() {
//		
//		List<ByteArray> rv = new ArrayList<ByteArray>();
//		for(UploadedImage image: getUploadedImages()) {
//			rv.add(image.toByteArray());
//		}
//		return rv;
//	}
	
//	public List<UploadedImage> getUploadedImages() {
//		FileUpload fileUpload = fileUploadField.getFileUpload();
//		
//		if (isZipFile(fileUpload)) {
//			return extractUploadedImagesFromZipFile(fileUpload);
//		} else {		
//			if (isImage(fileUpload.getClientFileName())) {
//				return Collections.singletonList(new UploadedImage(null, fileUpload.getBytes(), fileUpload.getClientFileName()));
//			} else {
//				warn(new StringResourceModel("notSupportedImage", this, null).getString());
//				return Collections.emptyList();				
//			}
//		}	
//	}
	
//	private List<UploadedImage> extractUploadedImagesFromZipFile(FileUpload fileUpload) {
//		List<UploadedImage> uploadedImages = new ArrayList<UploadedImage>();
//
//		File tempFile=null;
//		try {
//			tempFile = fileUpload.writeToTempFile();
//			tempFile.deleteOnExit();
//
//			ZipFile zipFile = new ZipFile(tempFile);
//			for(Enumeration<ZipEntry> e = getEntries(zipFile); e.hasMoreElements(); ) {
//				ZipEntry zipEntry = e.nextElement();
//				if (isImage(zipEntry.getName())) {
//					InputStream inputStream = zipFile.getInputStream(zipEntry);
//					uploadedImages.add(new UploadedImage(null, readByteArray(inputStream), zipEntry.getName()));
//					inputStream.close();
//				}
//				else
//				{
//					warn(new StringResourceModel("notSupportedImageInZipFile", this, null, new Object[] { zipEntry.getName()
//							
//					}).getString());
//				}
//			}				
//			return uploadedImages;
//		} catch (IOException e) {
//			error(new StringResourceModel("unexpectedErrorDuringExtractionOfZipFile", this, null).getString());
//			if (logger.isErrorEnabled()) {
//				logger.error(new StringResourceModel("unexpectedErrorDuringExtractionOfZipFile", this, null).getString(), e);
//			}
//			return Collections.emptyList();	
//		} finally {
//			if(tempFile != null) {
//				tempFile.delete();
//			}
//		}
//
//	}

//	@SuppressWarnings("unchecked")
//	private Enumeration<ZipEntry> getEntries(ZipFile zipFile) {
//		return zipFile.getEntries();
//	}

//	private boolean isImage(String name) {
//		String suffix = name.substring(name.length() - 4, name.length()).toLowerCase();		
//		return supportedImages.contains(suffix);	
//	}

//	private byte[] readByteArray(InputStream inputStream) throws IOException {
//		ByteArrayOutputStream output = new ByteArrayOutputStream();
//		
//		byte[] buf = new byte[1000];
//		
//		int len = 0;
//		while( ( len = inputStream.read(buf) ) != - 1 ) {
//			output.write( buf, 0, len );				
//		}
//						
//		output.close();
//		
//		return output.toByteArray();
//	}
	
//	private boolean isZipFile(FileUpload fileUpload) {
//		return fileUpload.getContentType().equals("application/zip");
//	}
	
}