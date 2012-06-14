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

import java.util.ArrayList;
import java.util.List;

import com.userweave.csv.AbstractCsvCell;
import com.userweave.csv.IntegerCsvCell;

public class IdCsvColumn implements ICsvColumn
{
	private final List<Integer> indexes;
	
	String title;
	
	public IdCsvColumn()
	{
		indexes = new ArrayList<Integer>();
		
		title = null;
	}
	
	public IdCsvColumn(String title)
	{
		indexes = new ArrayList<Integer>();
	
		this.title = title;
	}
	
	@Override
	public void addRow(AbstractCsvCell cell)
	{
		if(cell instanceof IntegerCsvCell)
		{
			indexes.add((Integer) cell.getObject());
		}
	}
	
	public int getRowById(Integer surveyExecutionId)
	{
		return indexes.indexOf(surveyExecutionId);
	}

	public Integer getSurvexExecIdByIndex(int index)
	{
		IntegerCsvCell cell = (IntegerCsvCell)getCellAtIndex(index);
		return cell.getObject();
	}

	@Override
	public String getTitle()
	{
		return null;
	}

	@Override
	public void addRow(AbstractCsvCell cell, int rowIndex){}

	@Override
	public AbstractCsvCell getCellAtIndex(int index)
	{
		return new IntegerCsvCell(indexes.get(index));
	}
}
