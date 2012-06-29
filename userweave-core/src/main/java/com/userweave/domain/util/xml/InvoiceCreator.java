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
package com.userweave.domain.util.xml;

import java.text.DecimalFormat;
import java.util.Locale;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.userweave.domain.Country;
import com.userweave.domain.Study;
import com.userweave.domain.User.BusinessRole;
import com.userweave.domain.service.pdf.PdfService.Receipient;
import com.userweave.domain.util.EUProvider;

public class InvoiceCreator {

	public Receipient evaluateReceipient(Study study) {
		Receipient receipientRV;
		
		/* Receipient.GERMAN_ALL (default)
		 * Diese Rechnung würde an alle Personen und Firmen ausgeliefert werden,
		 * die angeben innerhalb von Deutschland ansässig zu sein.
		 */
		receipientRV = Receipient.GERMAN_ALL;
				
		if (study.getOwner() != null && study.getOwner().getBusinessRole() != null
				&& study.getOwner().getAddress() != null 
				&& study.getOwner().getAddress().getCountry() != null) {
			
			
			if (study.getOwner().getAddress().getCountry() != Country.GERMANY) {	
				// 1.1.2010 00:00:00
				DateTime is2010 = new DateTime( 2010, 1, 1, 0, 0, 0 , 0);
				System.out.println("2010:" + is2010.toCalendar(Locale.GERMAN));
				
				// skip equal case
				if (is2010.isEqualNow()) {
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				/* Receipient.OTHER_PRIVATE
				 * Diese Rechnung würde an alle Privatpersonen gehen, die nicht in 
				 * Deutschland ansässig sind und ab dem 1.1.2010 auch an alle Unternehmen, 
				 * die innerhalb der EU sitzen.
				 */								
				if (study.getOwner().getBusinessRole() == BusinessRole.Individual 
						|| 
						( is2010.isBeforeNow() 
								&& study.getOwner().getBusinessRole() == BusinessRole.Company 
								&& EUProvider.isInEU(study.getOwner().getAddress().getCountry()) )) 
				{
					receipientRV = Receipient.OTHER_PRIVATE;
				}
				/* Receipient.OTHER_COMPANY
				 * Diese Rechnung würde an alle Unternehmen gehen, die außerhalb Deutschlands 
				 * sitzen (ab 1.1.2010 an alle Unternehmen außerhalb der EU)		
				 */	
				else if (study.getOwner().getBusinessRole() == BusinessRole.Company 
						|| 
						( is2010.isBeforeNow() 
								&& study.getOwner().getBusinessRole() == BusinessRole.Company 
								&& !EUProvider.isInEU(study.getOwner().getAddress().getCountry()) )) 
				{			
					receipientRV = Receipient.OTHER_COMPANY;
				}
			}
		} 
		else {
			//FIXME: Question: should throw Exception???
		}
		
		return receipientRV;
	}
	
	/**
	 * Create XML Representation of Invoice of given Study
	 * @param study
	 * @return
	 */
	public String toXML(Study study) {
		
		DecimalFormat df_us =
			  (DecimalFormat)DecimalFormat.getInstance(Locale.US);
		df_us.applyPattern( "#0.00" );
		
		DecimalFormat df_de =
			  (DecimalFormat)DecimalFormat.getInstance(Locale.GERMAN);
		df_de.applyPattern( "#0.00" );
		
		Document document = DocumentHelper.createDocument();
		Element invoice = document.addElement( "invoice" );
		
		Element receipient = invoice.addElement("receipient");
		
		Element details = invoice.addElement("details");

		if (study.getOwner().getCompany() != null) {
			receipient.addElement("company").addText( study.getOwner().getCompany() );
		}
		else {
			receipient.addElement("company").addText("");
		}
		
		receipient.addElement("surname").addText( study.getOwner().getSurname() );
		
		receipient.addElement("forename").addText( study.getOwner().getForename() );

		Element address = receipient.addElement("address");
		if(study.getOwner().getAddress() != null) {
			address.addElement("street").addText(study.getOwner().getAddress().getStreet());
			address.addElement("housenumber").addText(study.getOwner().getAddress().getHouseNumber());
			address.addElement("postcode").addText(study.getOwner().getAddress().getPostcode());
			address.addElement("city").addText(study.getOwner().getAddress().getCity());
			address.addElement("country").addText(study.getOwner().getAddress().getCountry().getName());
		}

		details.addElement("number").addText(study.getConsideration().getInvoice().getNumber());
		
		DateTime invoiceDate = study.getConsideration().getDate();
		DateTimeFormatter dateDTF = DateTimeFormat.longDate();
	    DateTimeFormatter usFmt = dateDTF.withLocale(Locale.US);
	    String dateString = invoiceDate.toString(usFmt);
	    
		if (evaluateReceipient(study) == Receipient.GERMAN_ALL) { 
		    DateTimeFormatter deFmt = dateDTF.withLocale(Locale.GERMAN);
		    dateString = invoiceDate.toString(deFmt);		
		}
		
		details.addElement("date").addText(dateString);
			
		details.addElement("currency").addText( study.getConsideration().getCurrency().getCurrencyCode() );
		
		Double gross = study.getConsideration().getGrossAmount();
		if(study.getConsideration().getGrossAmount() != null) {
			
			String grossString = df_us.format( study.getConsideration().getGrossAmount() );
			
			if (evaluateReceipient(study) == Receipient.GERMAN_ALL) { 
				grossString = df_de.format(study.getConsideration().getGrossAmount());
			}
				
			details.addElement("gross").addText( grossString );
		} else {
			details.addElement("gross").addText(new Integer(0).toString());
		}
		
		if(study.getConsideration().getInvoice().getPurchaseTaxPercent() != null && study.getConsideration().getInvoice().getPurchaseTaxPercent() > 0d) {
	
			String netString = df_us.format( study.getConsideration().getInvoice().getNetAmount() );
			if (evaluateReceipient(study) == Receipient.GERMAN_ALL) { 
				netString = df_de.format(study.getConsideration().getInvoice().getNetAmount() );
			}
			details.addElement("net").addText( netString ); 
			
			
			
			Double tax =  new Double(study.getConsideration().getGrossAmount()-study.getConsideration().getInvoice().getNetAmount());
			tax = Math.round( tax * 100. ) / 100.;
			String taxString = df_us.format( tax );
			if (evaluateReceipient(study) == Receipient.GERMAN_ALL) { 
				netString = df_de.format( tax );
			}
			details.addElement("tax").addText( taxString );
			
			
			
			String taxRateString = df_us.format( study.getConsideration().getInvoice().getPurchaseTaxPercent() );
			if (evaluateReceipient(study) == Receipient.GERMAN_ALL) { 
				netString = df_de.format( study.getConsideration().getInvoice().getPurchaseTaxPercent() );
			}
			details.addElement("taxrate").addText( taxRateString );
		} else {
			details.addElement("net").addText(new Integer(0).toString()); 
			details.addElement("tax").addText(new Integer(0).toString()); 
			
			details.addElement("taxrate").addText(study.getConsideration().getInvoice().getPurchaseTaxPercent().toString() );
		}
		
		return document.asXML();
	}

}