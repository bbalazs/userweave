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

public interface ICsvColumnGroup
{
	/**
	 * Returns the number of columns in this group.
	 * @return
	 */
	public int getNumberOfCols();
	
	/**
	 * Creates the first row in the ouptut csv.
	 * @return
	 */
	public String getHeadlinesAsCsv(String prefix);
	
	/**
	 * Creates the second row in the output csv.
	 * @return
	 */
	//public String getSubHeadlinesAsCsv();
	
	/**
	 * Get the row at index <i>index</i>
	 * 
	 * @param index
	 * 		Index of row.
	 * @return
	 */
	public String getRow(int index);
	
	/**
	 * Creates the third row in the output csv.
	 * @return
	 */
	//public String getAdditionalAsCsv();
	
	/**
	 * Adds a column to this column group.
	 * @param column
	 */
	public void addColumn(ICsvColumn column);
	
	/**
	 * Adds a column group to this column group.
	 * @param column
	 */
	public void addColumnGroup(ICsvColumnGroup group);
}
