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

import java.util.List;
import java.util.Vector;

import com.userweave.csv.AbstractCsvCell;
import com.userweave.csv.EmptyCsvCell;

/**
 * Class to represent a column in a csv table.
 * 
 * @author opr
 */
public class CsvColumn implements ICsvColumn
{
	List<AbstractCsvCell> rows;
	
	String title;
	
	public CsvColumn(String title)
	{
		rows = new Vector<AbstractCsvCell>(50, 10);
	
		this.title = title;
	}
	
	@Override
	public void addRow(AbstractCsvCell cell)
	{
		rows.add(cell);
	}

	@Override
	public String getTitle()
	{
		return title;
	}

	@Override
	public void addRow(
		AbstractCsvCell cell, int rowIndex)
	{
		if(rowIndex >= rows.size())
		{
			fillWithEmptyCells(rowIndex);
		}
		
		// because of above operation, the element must
		// be set, not added.
		rows.set(rowIndex, cell);
	}
	
	@Override
	public AbstractCsvCell getCellAtIndex(int index)
	{
		if(index >= rows.size())
		{
			return new EmptyCsvCell();
		}
		
		return rows.get(index);
	}
	
	private void fillWithEmptyCells(int rowIndex)
	{
		// fill with empty cells
		while(rows.size() <= rowIndex)
		{
			rows.add(new EmptyCsvCell());
		}
	}
}
