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
import java.util.Locale;

import com.userweave.csv.AbstractCsvCell;
import com.userweave.csv.IntegerCsvCell;
import com.userweave.csv.StringCsvCell;
import com.userweave.domain.SurveyExecution;

public class CsvSurveyExecutionColumnGroup extends CsvColumnGroup
{
	private final IdCsvColumn surveyIds;
	
	private final CsvColumn locales;
	
	public CsvSurveyExecutionColumnGroup(
		List<SurveyExecution> finishedSurveyExecutions, String title)
	{
		super(title);
		
		surveyIds = new IdCsvColumn("participants");
		
		locales = new CsvColumn("locales");
		
		for(SurveyExecution execution : finishedSurveyExecutions)
		{
			surveyIds.addRow(new IntegerCsvCell(execution.getId()));
			
			Locale locale = execution.getLocale();
			
			locales.addRow(new StringCsvCell(
				locale == null ? 
					execution.getStudy().getLocale().toString() : 
					locale.toString()));
		}
		
		addColumn(surveyIds);
		addColumn(locales);
	}
	
	public int findIndexOfId(Integer id)
	{
		return surveyIds.getRowById(id);
	}

	public Integer getSurveyIdByIndex(int index)
	{
		return surveyIds.getSurvexExecIdByIndex(index);
	}

	@Override
	public String getHeadlinesAsCsv(String prefix)
	{
		return new String("\"participants\";\"locale\";");
	}

	@Override
	public String getRow(int index)
	{
		AbstractCsvCell cell = surveyIds.getCellAtIndex(index);
		AbstractCsvCell localeCell = locales.getCellAtIndex(index); 
		return (index + 1) + ";" + localeCell.transformToCsv() + ";";
	}
}
