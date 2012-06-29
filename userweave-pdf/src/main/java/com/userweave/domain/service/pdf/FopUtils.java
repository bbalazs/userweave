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
package com.userweave.domain.service.pdf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;

import com.userweave.domain.service.pdf.PdfService.Receipient;

public class FopUtils {

	public OutputStream createPDF(InputStream foMarkup, OutputStream out) throws IOException, FOPException, TransformerException {
		return createPDF(foMarkup, (InputStream) null, out);
	}
	
	/**
	 * function to render pdf. fop is not threadsafe so we must use synchronized here.
	 * 
	 * @param xmlMarkup
	 * @param xsltMarkup
	 * @param out
	 * @return
	 * @throws IOException
	 * @throws FOPException
	 * @throws TransformerException
	 */
	public synchronized OutputStream createPDF(InputStream xmlMarkup, InputStream xsltMarkup, OutputStream out) throws IOException, FOPException, TransformerException {
		
		FopFactory fopFactory = FopFactory.newInstance();

		FOUserAgent foUserAgent = fopFactory.newFOUserAgent();

		Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);

		// Setup XSLT
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer;
		if(xsltMarkup == null) {
			transformer = factory.newTransformer();
		} else {
			transformer = factory.newTransformer(new StreamSource(xsltMarkup));
		}

		// Set the value of a <param> in the stylesheet
		// transformer.setParameter("versionParam", "2.0");

		// Setup input for XSLT transformation
		Source src = new StreamSource(xmlMarkup);

		// Resulting SAX events (the generated FO) must be piped through to FOP
		Result res = new SAXResult(fop.getDefaultHandler());

		// Start XSLT transformation and FOP processing
		transformer.transform(src, res);

		// Result processing 
		// FormattingResults foResults = fop.getResults();

		return out;
	}

	public OutputStream createPDF(InputStream xmlMarkup, Receipient receipient, OutputStream out) throws IOException, FOPException, TransformerException {
		String xsltFile = null;
		switch (receipient) {
		case GERMAN_ALL:
			xsltFile = "invoice_german_all.xsl";
			break;
		case OTHER_COMPANY:
			xsltFile = "invoice_other_company.xsl";
			break;
		case OTHER_PRIVATE:
			xsltFile = "invoice_other_private.xsl";
		default:
			break;
		}
		if(xsltFile == null) {
			throw new IOException("receipient illegal: "+receipient);
		}
		
		InputStream xsltMarkup = this.getClass().getClassLoader().getResourceAsStream(xsltFile);
		if(xsltMarkup == null) {
			throw new IOException("File not found: : "+xsltFile);
		}
		
		return createPDF(xmlMarkup, xsltMarkup, out);
		
	}

}
