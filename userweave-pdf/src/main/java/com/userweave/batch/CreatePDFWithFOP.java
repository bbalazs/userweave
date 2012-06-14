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
package com.userweave.batch;

import java.io.FileOutputStream;
import java.io.InputStream;

import com.userweave.domain.service.pdf.FopUtils;
import com.userweave.domain.service.pdf.PdfService;
import com.userweave.domain.service.pdf.PdfService.Receipient;
import com.userweave.domain.service.pdf.impl.PdfServiceImpl;

public class CreatePDFWithFOP {

	public static void main(String[] args) throws Exception {
		{
			InputStream xmlMarkup = ClassLoader.getSystemClassLoader().getResourceAsStream("invoice.xml");

			FileOutputStream out = new FileOutputStream("invoice_german_all.pdf");

			createPDF(xmlMarkup, PdfService.Receipient.GERMAN_ALL, out);
			out.flush();
			out.close();
			xmlMarkup.close();
		}

		{ 
			InputStream xmlMarkup = ClassLoader.getSystemClassLoader().getResourceAsStream("invoice.xml");
			FileOutputStream out = new FileOutputStream("invoice_other_company.pdf");

			new FopUtils().createPDF(xmlMarkup, PdfService.Receipient.OTHER_COMPANY, out);
			out.flush();
			out.close();
			xmlMarkup.close();

		}
		
		{ 
			InputStream xmlMarkup = ClassLoader.getSystemClassLoader().getResourceAsStream("invoice.xml");
			FileOutputStream out = new FileOutputStream("invoice_other_private.pdf");

			new FopUtils().createPDF(xmlMarkup, PdfService.Receipient.OTHER_PRIVATE, out);
			out.flush();
			out.close();
			xmlMarkup.close();

		}
	}

	private static void createPDF(InputStream xmlMarkup, Receipient receipient, FileOutputStream out) throws Exception {
		 //new FopUtils().createPDF(xmlMarkup, receipient, out);
		new PdfServiceImpl().createInvoice(xmlMarkup, receipient, out);
	}

}