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
package com.userweave.csv.question.answer;

import java.util.Date;
import java.util.Locale;

import com.userweave.csv.IntegerCsvCell;
import com.userweave.csv.table.CsvColumn;
import com.userweave.domain.LocalizedString;
import com.userweave.module.methoden.questionnaire.domain.answer.AnswerToSingleAnswerQuestion;

/**
 * Converter for single answers. A single answer is a
 * one element so we need only one column here.
 * 
 * @author opr
 */
public class SingleAnswerCsvConverter 
	extends AbstractAnswerCsvConverter<AnswerToSingleAnswerQuestion>
{
	public static void convertToCsvRow(
			CsvColumn col,
			AnswerToSingleAnswerQuestion entity, 
			int rowIndex, Locale locale, Date finishedExecutionDate)
	{	
		LocalizedString answer = entity.getAnswer();
		IntegerCsvCell cell = null;
		
		if(answer == null)
		{
			cell = new IntegerCsvCell(0);
		}
		else
		{
			Integer pos = answer.getPosition();
			
			if(pos == null)
			{
				cell = new IntegerCsvCell(-2);
			}
			else
			{
				cell = new IntegerCsvCell(pos + 1);
			}
		}
		
		col.addRow(cell, rowIndex);
		
//		/*
//		 * Check, if an answer was given, if not, we need
//		 * to set at least one cell to 0 to fill the 
//		 * remaining cells with 0.
//		 * 
//		 * @see SingleAnswerCsvColumnGroup.getRow()
//		 */
//		if(answer == null)
//		{
//			// achieve above by setting the fist column to 0
//			answer = entity.getQuestion().getPossibleAnswers().get(0);
//			cell = new IntegerCsvCell(0);
//		}
		
//		ICsvColumn col = colGroup.findColumByString(answer);
//		
//		if(col == null)
//		{
//			col = new MultipleAnswersCsvColumn(answer.getValue(locale), answer);
//			
//			colGroup.addColumn(col);
//		}
		
//		col.addRow(cell != null ? cell : new IntegerCsvCell(1), rowIndex);
	}

}
