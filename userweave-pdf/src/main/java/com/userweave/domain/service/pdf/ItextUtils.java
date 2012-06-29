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

import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.userweave.domain.service.pdf.PdfService.Receipient;

public class ItextUtils {
	
	public OutputStream createPDF(InputStream generatedPDF, InputStream stampPDF, OutputStream out) throws IOException, DocumentException {
		    
		// the document we're watermarking
        PdfReader document = new PdfReader( generatedPDF );
        int num_pages = document.getNumberOfPages();

        // the watermark (or letterhead, etc.)
        PdfReader mark = new PdfReader( stampPDF );
        Rectangle mark_page_size = mark.getPageSize( 1 );

        // the output document
        PdfStamper writer = new PdfStamper( document, out );

        // create a PdfTemplate from the first page of mark
        // (PdfImportedPage is derived from PdfTemplate)
        PdfImportedPage mark_page = writer.getImportedPage( mark, 1 );

        for( int ii = 0; ii< num_pages; ) {
          // iterate over document's pages, adding mark_page as
          // a layer 'underneath' the page content; scale mark_page
          // and move it so it fits within the document's page;
          // if document's page is cropped, then this scale might
          // not be small enough

          ++ii;
          Rectangle doc_page_size = document.getPageSize( ii );
          float h_scale = doc_page_size.getWidth() / mark_page_size.getWidth();
          float v_scale = doc_page_size.getHeight() / mark_page_size.getHeight();
          float mark_scale = (h_scale< v_scale) ? h_scale :  v_scale;

          float h_trans = (float)((doc_page_size.getWidth()-
                                   mark_page_size.getWidth()* mark_scale) / 2.0);
          float v_trans = (float)((doc_page_size.getHeight()-
                                   mark_page_size.getHeight()* mark_scale) / 2.0);

          PdfContentByte contentByte = writer.getUnderContent( ii );
          contentByte.addTemplate( mark_page,
                                   mark_scale, 0,
                                   0, mark_scale,
                                   h_trans, v_trans );
        }

        writer.close();
        
        return out;
		
/*		
		
		
		
		
		    PdfReader reader = new PdfReader(origPDF);
		    
		    int n = reader.getNumberOfPages();
		    
		    Document document = new Document(reader.getPageSizeWithRotation(1));
		    PdfWriter writer = PdfWriter.getInstance(document, outfile);
		    writer.setEncryption(PdfWriter.STRENGTH40BITS, "pdf", null,
		      PdfWriter.AllowCopy);
		    document.open();
		    PdfContentByte cb = writer.getDirectContent();
		    PdfImportedPage page;
		    int rotation;
		    int i = 0;
		    while (i < n) {
		      i++;
		      document.setPageSize(reader.getPageSizeWithRotation(i));
		      document.newPage();
		      page = writer.getImportedPage(reader, i);
		      rotation = reader.getPageRotation(i);
		      if (rotation == 90 || rotation == 270) {
		        cb.addTemplate(page, 0, -1f, 1f, 0, 0,
		        reader.getPageSizeWithRotation(i).height());
		      } else {
		        cb.addTemplate(page, 1f, 0, 0, 1f, 0, 0);
		      }
		      System.out.println("Processed page " + i);
		    }
		    document.close();
		  } catch( Exception e) {
		    e.printStackTrace();
		  }
		*/

		}

	
	public OutputStream createPDF(InputStream origPDF, Receipient receipient, OutputStream out) throws IOException, DocumentException {
		
		String pdfFile = null;
		switch (receipient) {
		case GERMAN_ALL:
			pdfFile = "invoice_german_background.pdf";
			break;
		case OTHER_COMPANY:
			pdfFile = "invoice_german_background.pdf";
			break;
		case OTHER_PRIVATE:
			pdfFile = "invoice_german_background.pdf";
		default:
			break;
		}
		if(pdfFile == null) {
			throw new IOException("receipient illegal: "+receipient);
		}
		
		InputStream stampPDF = this.getClass().getClassLoader().getResourceAsStream(pdfFile);
		if(stampPDF == null) {
			throw new IOException("File not found: : "+pdfFile);
		}
		
		return createPDF(origPDF, stampPDF, out);
	}

}
