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

import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

public class CreatePDF {

	public static void main(String[] args) {
		if(args.length == 0) {
			args = new String[] { "one.pdf", "two.pdf", "out.pdf" };
		}
	    if( args.length== 3 ) {
	        try {
	          // the document we're watermarking
	          PdfReader document= new PdfReader( args[0] );
	          int num_pages= document.getNumberOfPages();

	          // the watermark (or letterhead, etc.)
	          PdfReader mark= new PdfReader( args[1] );
	          Rectangle mark_page_size= mark.getPageSize( 1 );

	          // the output document
	          PdfStamper writer=
	            new PdfStamper( document,
	                            new FileOutputStream( args[2] ) );

	          // create a PdfTemplate from the first page of mark
	          // (PdfImportedPage is derived from PdfTemplate)
	          PdfImportedPage mark_page=
	            writer.getImportedPage( mark, 1 );

	          for( int ii= 0; ii< num_pages; ) {
	            // iterate over document's pages, adding mark_page as
	            // a layer 'underneath' the page content; scale mark_page
	            // and move it so it fits within the document's page;
	            // if document's page is cropped, then this scale might
	            // not be small enough

	            ++ii;
	            Rectangle doc_page_size= document.getPageSize( ii );
	            float h_scale= doc_page_size.getWidth() / mark_page_size.getWidth();
	            float v_scale= doc_page_size.getHeight() / mark_page_size.getHeight();
	            float mark_scale= (h_scale< v_scale) ? h_scale :  v_scale;

	            float h_trans= (float)((doc_page_size.getWidth()-
	                                    mark_page_size.getWidth()* mark_scale) / 2.0);
	            float v_trans= (float)((doc_page_size.getHeight()-
	                                    mark_page_size.getHeight()* mark_scale) / 2.0);

	            PdfContentByte contentByte= writer.getUnderContent( ii );
	            contentByte.addTemplate( mark_page,
	                                     mark_scale, 0,
	                                     0, mark_scale,
	                                     h_trans, v_trans );
	          }

	          writer.close();
	        }
	        catch( Exception ee ) {
	          ee.printStackTrace();
	        }
	      }
	      else { // input error
	        System.err.println("arguments: in_document in_watermark out_pdf_fn");
	      }

	}
}
