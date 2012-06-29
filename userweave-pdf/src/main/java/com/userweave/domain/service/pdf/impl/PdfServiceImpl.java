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
package com.userweave.domain.service.pdf.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.transform.TransformerException;


import org.apache.fop.apps.FOPException;

import com.lowagie.text.DocumentException;
import com.userweave.domain.service.pdf.FopUtils;
import com.userweave.domain.service.pdf.ItextUtils;
import com.userweave.domain.service.pdf.PdfService;
import com.userweave.domain.service.pdf.PdfServiceException;

public class PdfServiceImpl implements PdfService {

	public void createInvoice(InputStream xmlMarkup, Receipient receipient, OutputStream out) throws PdfServiceException {
		try { 
			
			ByteArrayOutputStream fopout = new ByteArrayOutputStream();
			new FopUtils().createPDF(xmlMarkup, receipient, fopout);
			fopout.close();
			
			InputStream generatedPDF = new ByteArrayInputStream(fopout.toByteArray());
			fopout = null; // let the garbage collector remove the generated pdf, the copy is in the input stream now
			
			new ItextUtils().createPDF(generatedPDF, receipient, out);
			out.flush();
			out.close();
			
		} catch (FOPException e) {
			throw new PdfServiceException(e);
		} catch (IOException e) {
			throw new PdfServiceException(e);
		} catch (TransformerException e) {
			throw new PdfServiceException(e);
		} catch (DocumentException e) {
			throw new PdfServiceException(e);
		}
	}

	
}