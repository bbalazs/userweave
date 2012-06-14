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
package com.userweave.csv.table;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.util.resource.AbstractStringResourceStream;
import org.apache.wicket.util.resource.IResourceStream;

/**
 * Class to represent a csv table.
 * 
 * @author opr
 */
public class CsvTable
{
	/**
	 * A csv table consists of a list of
	 * column groups.
	 */
	List<ICsvColumnGroup> table;
	
	public CsvTable()
	{
		table = new ArrayList<ICsvColumnGroup>();
	}
	
	public void addColGroup(ICsvColumnGroup group)
	{
		table.add(group);
	}
	
	//public void transformToCsv(CsvWriter writer, int numberOfSurveyExecutions)
	public IResourceStream transformToCsv(final int numberOfSurveyExecutions)
	{
		IResourceStream stream = new AbstractStringResourceStream()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected Charset getCharset()
			{
				return Charset.forName("UTF-8");
			}
			
			@Override
			public String getContentType()
			{
				return "text/csv";
			}
			
			@Override
			protected String getString()
			{
				return createCsvString(numberOfSurveyExecutions);
			}
		};
		
		return stream;
	}
	
	private String createCsvString(int numberOfSurveyExecutions)
	{
		StringBuilder output = new StringBuilder();
		
		// create headline
		for(ICsvColumnGroup group : table)
		{
			output.append(group.getHeadlinesAsCsv(""));
		}
		
		output.append("\n");
		
		// create content rows
		for(int i = 0; i < numberOfSurveyExecutions; i++)
		{
			for(ICsvColumnGroup group : table)
			{
				output.append(group.getRow(i));
			}
			
			output.append("\n");
		}
		
		return output.toString();
	}
}
