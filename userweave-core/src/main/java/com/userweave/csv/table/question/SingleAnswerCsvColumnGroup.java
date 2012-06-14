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
package com.userweave.csv.table.question;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

import com.userweave.csv.AbstractCsvCell;
import com.userweave.csv.EmptyCsvCell;
import com.userweave.csv.table.ICsvColumn;
import com.userweave.csv.table.ICsvColumnGroup;
import com.userweave.csv.table.MultipleAnswersCsvColumn;
import com.userweave.domain.LocalizedString;
import com.userweave.module.methoden.questionnaire.domain.question.SingleAnswerQuestion;

@Deprecated
public class SingleAnswerCsvColumnGroup implements ICsvColumnGroup
{
	private final LinkedHashMap<LocalizedString, ICsvColumn> colGroup;
	
	private int numberOfCols;
	
	private final String title;
	
	public String getTitle()
	{
		return title;
	}
	
	public SingleAnswerCsvColumnGroup(
		SingleAnswerQuestion question, Locale locale)
	{
		numberOfCols = 0;
		
		this.title = question.getName().getValue(locale);
		
		List<LocalizedString> answers = question.getPossibleAnswers();
		
		colGroup = new LinkedHashMap<LocalizedString, ICsvColumn>(answers.size());
		
		for(LocalizedString answer : answers)
		{	
			addColumn(new MultipleAnswersCsvColumn(
					answer.getValue(locale), answer));
		}
	}

	@Override
	public void addColumn(ICsvColumn column)
	{
		if(column instanceof MultipleAnswersCsvColumn)
		{
			colGroup.put(((MultipleAnswersCsvColumn) column).getString(), column);
			
			numberOfCols++;
		}
	}
	
	public ICsvColumn findColumByString(LocalizedString string)
	{
		if(colGroup.containsKey(string))
		{
			return colGroup.get(string);
		}
		
		return null;
	}
	
	@Override
	public String getHeadlinesAsCsv(String prefix)
	{
		Iterator<Entry<LocalizedString, ICsvColumn>> i = colGroup.entrySet().iterator();
		
		StringBuilder csvOutput = new StringBuilder();
		
		while(i.hasNext())
		{
			Entry<LocalizedString, ICsvColumn> entry = i.next();
		
			csvOutput.append("\"" + prefix + " | " + 
							 getTitle() + " | " + 
							 entry.getValue().getTitle() + "\";");
		}
		
		return csvOutput.toString();
	}

	@Override
	public String getRow(int index)
	{
		Iterator<Entry<LocalizedString, ICsvColumn>> i = colGroup.entrySet().iterator();
		
		StringBuilder csvOutput = new StringBuilder();
		
		/*
		 * If a row contains only empty cells, then
		 * the output is a row of empty cells. If at
		 * least one cell is not an empty cell, that
		 * is, if a answer was given, the row gets
		 * filled up with 0's.
		 */
		List<AbstractCsvCell> row = new ArrayList<AbstractCsvCell>();
		
		boolean isEmptyRow = true;
		
		while(i.hasNext())
		{
			Entry<LocalizedString, ICsvColumn> entry = i.next();
			
			AbstractCsvCell cell = entry.getValue().getCellAtIndex(index);
		
			if(cell != null)
			{
				if(!(cell instanceof EmptyCsvCell))
				{
					isEmptyRow = false;
				}
				
				row.add(cell);
			}
			else
			{
				row.add(new EmptyCsvCell());
			}
		}
		
		// create csv row
		createRow(row, isEmptyRow, csvOutput);
		
		return csvOutput.toString();
	}

	private void createRow(
		List<AbstractCsvCell> row, boolean isEmptyRow, StringBuilder csvOutput)
	{
		for(AbstractCsvCell cell : row)
		{
			if(isEmptyRow)
			{
				csvOutput.append(";");
			}
			else
			{
				if(cell instanceof EmptyCsvCell)
				{
					csvOutput.append("0;");
				}
				else
				{
					csvOutput.append(cell.transformToCsv() + ";");
				}
			}
		}
	}
	
	@Override
	public int getNumberOfCols()
	{
		return numberOfCols;
	}

	@Override
	public void addColumnGroup(ICsvColumnGroup group)
	{
		// do nothing
	}

}
