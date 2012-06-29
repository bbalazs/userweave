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
package com.userweave.csv;

import java.util.Locale;

import com.userweave.csv.table.CsvSurveyExecutionColumnGroup;
import com.userweave.csv.table.ICsvColumnGroup;

/**
 * Simple interface to convert an entity for a given
 * survey execution to a csv row.
 * 
 * @author opr
 */
public interface ICsvConverter<T>
{
	public ICsvColumnGroup convertToCsv(
		T entity, 
		CsvSurveyExecutionColumnGroup idGroup,
		Locale locale);
}
