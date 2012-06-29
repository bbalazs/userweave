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
package com.userweave.domain.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.userweave.dao.InvoiceDao;
import com.userweave.domain.Study;
import com.userweave.domain.service.InvoiceService;
import com.userweave.domain.service.InvoiceServiceException;
import com.userweave.domain.service.pdf.PdfService;
import com.userweave.domain.service.pdf.PdfServiceException;
import com.userweave.domain.service.pdf.PdfService.Receipient;
import com.userweave.domain.service.pdf.impl.PdfServiceImpl;
import com.userweave.domain.util.InvoiceNumberProvider;
import com.userweave.domain.util.PurchaseTaxProvider;
import com.userweave.domain.util.xml.InvoiceCreator;

@Service
public class InvoiceServiceImpl implements InvoiceService {

	// FIXME: spring annotation an PdfServiceImpl anfuegen!!
	//@Resource
	//private PdfService pdfService;
	private final PdfService pdfService = new PdfServiceImpl();
	
	@Autowired
	private InvoiceDao invoiceDao;
	
	/**
	 * Create Invoice from Consideration of assigned study and save resulting invoice in given study
	 * @param study
	 * @throws PdfServiceException 
	 */
	@Override
	public byte[] createInvoicePDF(Study study) throws PdfServiceException {
		
		InvoiceCreator invoiceCreator = new InvoiceCreator();
		
		Receipient receipient = invoiceCreator.evaluateReceipient(study);
		String invoiceXML = invoiceCreator.toXML(study);
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		InputStream xmlMarkup = new ByteArrayInputStream(invoiceXML.getBytes());
		invoiceXML = null; // let the garbage collector remove the generated xml, the copy is in the input stream now
		
		pdfService.createInvoice(xmlMarkup, receipient, out);
		
		return out.toByteArray();
	}
	

	public void createInvoice(Study study) throws InvoiceServiceException, PdfServiceException {
		
		InvoiceCreator invoiceCreator = new InvoiceCreator();
		Receipient receipient = invoiceCreator.evaluateReceipient(study);
		
		DateTime now = new DateTime();
		study.getConsideration().getInvoice().setDate(now);
		String prefix = InvoiceNumberProvider.getNextNumberPrefix();
		int next = invoiceDao.findNextInvoiceNumber(prefix);
		study.getConsideration().getInvoice().setNumber( InvoiceNumberProvider.getNextNumber(next) );
		
		if(study.getConsideration().getInvoice().getPurchaseTaxPercent() == null 
				|| study.getConsideration().getInvoice().getPurchaseTaxPercent() <= 0) {
			study.getConsideration().getInvoice().setPurchaseTaxPercent(PurchaseTaxProvider.getGermanPurchaseTax());	
		}
		
		if(study.getConsideration().getGrossAmount() == null 
			|| study.getConsideration().getInvoice().getPurchaseTaxPercent() == null 
			|| study.getConsideration().getInvoice().getPurchaseTaxPercent() <= 0) {
			study.getConsideration().setGrossAmount(0d);
//			throw new InvoiceServiceException(t)
		}
		
		if (study.getConsideration().getInvoice().getPurchaseTaxPercent() == null 
				|| study.getConsideration().getInvoice().getPurchaseTaxPercent() <= 0) {
			study.getConsideration().getInvoice().setPurchaseTaxPercent(0d);
//			throw new InvoiceServiceException(t)
		}
		
		// Berechne aus Consideration Butto-Betrag den Netto-Betrag fuer die Rechnung
		// Umsatzsteuerformel: 
		// Brutto = Netto * (Steuersatz / 100 + 1)
		// Netto = (Brutto * 100) / (Steuersatz + 100)
		if ( receipient == Receipient.GERMAN_ALL || receipient == Receipient.OTHER_PRIVATE) {
			if (study.getConsideration().getGrossAmount() > 0d 
					&& study.getConsideration().getInvoice().getPurchaseTaxPercent() > 0d) {
				
				
				// save gross
				study.getConsideration().getInvoice().setGrossAmount(
						study.getConsideration().getGrossAmount()
					);
				
				// save net
				study.getConsideration().getInvoice().setNetAmount( 
						(study.getConsideration().getGrossAmount() * 100) / 
						(study.getConsideration().getInvoice().getPurchaseTaxPercent() + 100)
					);
				
				// save rounded net
				study.getConsideration().getInvoice().setNetAmount(
						Math.round( study.getConsideration().getInvoice().getNetAmount() * 100. ) / 100.
					);
			}
			else {
				study.getConsideration().getInvoice().setNetAmount(0d);
			}
		}
		// nimm Consideration Brutto-Betrag als Netto-Betrag fuer die Rechnung
		else if (receipient == Receipient.OTHER_COMPANY) {
			study.getConsideration().getInvoice().setNetAmount(study.getConsideration().getGrossAmount());
		}
		
		study.getConsideration().getInvoice().setOwner(study.getOwner());
		
		// Erzeuge PDF
		study.getConsideration().getInvoice().setGeneratedPdf( 
				this.createInvoicePDF(study)
			);
	}
}
