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
package com.userweave.module.methoden.iconunderstandability.page.conf.terms;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.userweave.domain.Role;

/**
 * Panel that contains a file upload field to upload a 
 * list of terms from a text file.
 * 
 * @author opr
 *
 */
@AuthorizeAction(action = Action.RENDER, 
				 roles = {Role.PROJECT_ADMIN, Role.PROJECT_PARTICIPANT})
public class ITMTermsUpload extends Panel 
{
	private static final long serialVersionUID = 1L;

	private final static Logger logger = LoggerFactory.getLogger(ITMTermsUpload.class);
	
	/**
	 * The input field wrapped by this panel.
	 */
	private FileUploadField fileUploadField;
	
	/**
	 * Default constructor.
	 * 
	 * @param id
	 * 		Component markup id.
	 */
	public ITMTermsUpload(String id) 
	{
		super(id);
	
		init();
	}
	
	/**
	 * Initializes this panel. Adds the file upload field.
	 */
	private void init() 
	{
		IModel<List<FileUpload>> model = Model.ofList(new ArrayList());
		
		add(fileUploadField = 
			new FileUploadField("imageFile", model));
		
		fileUploadField.setRequired(true);
	}
	
	public List<String> getUploadedTerms() 
	{
		FileUpload fileUpload = fileUploadField.getFileUpload();
		
		if (isTxtFile(fileUpload)) {
			return extractUploadedTermsFromTxtFile(fileUpload);
		} 
		else {
			warn(new StringResourceModel("notSupportedFileType", this, null).getString());
			return Collections.emptyList();				
		}	
	}
	
	private List<String> extractUploadedTermsFromTxtFile(FileUpload fileUpload) {
		List<String> uploadedTerms = new ArrayList<String>();

		File tempFile=null;
		try {
			tempFile = fileUpload.writeToTempFile();
			tempFile.deleteOnExit();

			Scanner scanner = new Scanner(tempFile);
		    try {
		    	//first use a Scanner to get each line
		    	while ( scanner.hasNextLine() ){
			    	String curLine = scanner.nextLine();
			    	
			    	if (!curLine.trim().equals("")) {
			    		uploadedTerms.add(curLine);
			    		//use a second Scanner to parse the content of each line 
//			    		Scanner lineScanner = new Scanner(curLine);
//			    		while ( lineScanner.hasNext() ){
//		    		    	String token = lineScanner.next();
//			    	    	if (token != null) {
//								uploadedTerms.add(token);
//		
//								if (logger.isDebugEnabled()) {
//										logger.debug("token is : " + token.trim());
//								}
//							}
//		    	    	}
//		    	    	// (no need for finally here, since String is source)
//		    	    	lineScanner.close();
			    	}
		    	}
		    }
		    finally {
		      //ensure the underlying stream is always closed
		      scanner.close();
		    }

			
			return uploadedTerms;
		} catch (IOException e) {
			error(new StringResourceModel("unexpectedErrorDuringExtractionOfFile", this, null).getString());
			if (logger.isErrorEnabled()) {
				logger.error(new StringResourceModel("unexpectedErrorDuringExtractionOfFile", this, null).getString(), e);
			}
			return Collections.emptyList();	
		} finally {
			if(tempFile != null) {
				tempFile.delete();
			}
		}

	}
	
	private boolean isTxtFile(FileUpload fileUpload) {
		return fileUpload.getContentType().equals("text/plain");
	}
}