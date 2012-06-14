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

/**
 * Basic implementation of the ICsvColumnGroup inteface.
 * 
 * @author opr
 * @see ICsvColumnGroup
 */
public abstract class CsvColumnGroup implements ICsvColumnGroup
{
	private final List<ICsvColumn> columns;
	
	public List<ICsvColumn> getColumns()
	{
		return columns;
	}

	private final List<ICsvColumnGroup> columnGroups;
	
	public List<ICsvColumnGroup> getColumnGroups()
	{
		return columnGroups;
	}
	
	private final String title;
	
	public String getTitle()
	{
		return title;
	}

	public CsvColumnGroup(String title)
	{
		columns = new ArrayList<ICsvColumn>();
		
		columnGroups = new ArrayList<ICsvColumnGroup>();
		
		this.title = title;
	}
	
	@Override
	public void addColumn(ICsvColumn column)
	{
		columns.add(column);
	}
	
	public ICsvColumn getColumn(int index)
	{
		return columns.get(index);
	}
	
	public void addColumnGroup(ICsvColumnGroup group)
	{
		columnGroups.add(group);
	}
	
	@Override
	public int getNumberOfCols()
	{
		int size = columns.size();
		
		for(ICsvColumnGroup group : columnGroups)
		{
			size += group.getNumberOfCols();
		}
		
		return size;
	}
}
