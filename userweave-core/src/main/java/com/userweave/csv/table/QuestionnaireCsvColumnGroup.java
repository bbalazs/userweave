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

public class QuestionnaireCsvColumnGroup extends CsvColumnGroup
{
	public QuestionnaireCsvColumnGroup(String title)
	{
		super(title);
	}

	@Override
	public String getHeadlinesAsCsv(String prefix)
	{
		StringBuilder csvOuput = new StringBuilder();
		
		for(ICsvColumnGroup group : getColumnGroups())
		{
			csvOuput.append(group.getHeadlinesAsCsv(getTitle()));
		}
		
		return csvOuput.toString();
	}

	@Override
	public void addColumn(ICsvColumn column)
	{
		// do nothing
	}

	@Override
	public void addColumnGroup(ICsvColumnGroup group)
	{
		super.addColumnGroup(group);
	}

	@Override
	public String getRow(int index)
	{
		List<ICsvColumnGroup> groups = getColumnGroups();
		
		StringBuilder csvOutput = new StringBuilder();
		
		for(ICsvColumnGroup group : groups)
		{
			csvOutput.append(group.getRow(index));
		}
		
		return csvOutput.toString();
	}

}
