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
package com.userweave.csv.writer;

import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * Simple writer to write a csv to an output stream.
 * 
 * @author opr
 *
 */
public class CsvWriter
{
	/**
	 * Printwriter for the output stream
	 */
	private final PrintWriter out;
	
	/**
	 * Flag for first item of row.
	 */
	private boolean first = true;

	/**
	 * Default constructor.
	 * 
	 * @param os
	 * 		Output stream to write to.
	 */
	public CsvWriter(OutputStream os)
	{
		out = new PrintWriter(os);
	}

	/**
	 * Write a row to the output stream.
	 * 
	 * @param row
	 * 		Row to write to output stream
	 */
	public void writeRow(StringBuilder row)
	{
		out.append(row.toString());
		
		endLine();
	}

	public void endLine()
	{
		out.append("\r\n");
		first = true;
	}

	public void flush()
	{
		out.flush();
	}

	public void close()
	{
		out.close();
	}
}

