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
/**
 * 
 */
package com.userweave.csv;

/**
 * Abstract idea of a csv cell. Transforms a given object
 * to a csv string, which results often in multiple columns
 * in the result .csv. 
 * 
 * @author opr
 *
 */
public abstract class AbstractCsvCell<T>
{
	/**
	 * Object to hold in the cell.
	 */
	private final T object;
	
	public T getObject()
	{
		return object;
	}
	
	/**
	 * Constructor.
	 * 
	 * @param object
	 * 		Object the cell should hold.
	 */
	public AbstractCsvCell(T object)
	{
		this.object = object;
	}
	
	/**
	 * Transforms this cell to a csv compatible string
	 * 
	 * @return
	 * 		A string.
	 */
	public String transformToCsv()
	{
		StringBuilder csvString = new StringBuilder();
		
		csvString.append(toCsvString(object));
		
		return csvString.toString();
	}
	
	/**
	 * Convert the object of this cell to a string.
	 * 
	 * @param object
	 * 		Object of this cell.
	 * @return
	 * 		A string representation of the cells object.
	 */
	protected abstract String toCsvString(T object);
}
