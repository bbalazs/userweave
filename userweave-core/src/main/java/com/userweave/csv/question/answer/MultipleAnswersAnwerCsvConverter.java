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
package com.userweave.csv.question.answer;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.userweave.csv.IntegerCsvCell;
import com.userweave.csv.table.ICsvColumn;
import com.userweave.csv.table.MultipleAnswersCsvColumn;
import com.userweave.csv.table.question.MultipleAnswersCsvColumnGroup;
import com.userweave.domain.LocalizedString;
import com.userweave.module.methoden.questionnaire.domain.answer.MultipleAnswersAnwer;

/**
 * Transforms a multiple answers question answer to 
 * a list of cells. Each cell contains the id of 
 * the given localized string answer, which are child
 * elements of the questions answer.
 * 
 * @author opr
 *
 */
public class MultipleAnswersAnwerCsvConverter extends
		AbstractAnswerCsvConverter<MultipleAnswersAnwer>
{	
	public static void convertToCsvRow(
			MultipleAnswersCsvColumnGroup colGroup,
			MultipleAnswersAnwer entity, 
			int rowIndex,
			Locale locale,
			Date finishedExecutionDate)
	{
		List<LocalizedString> answers = entity.getAnswers();
		
		if(finishedExecutionDate != null &&
		   (answers == null || answers.isEmpty()))
		{
			// module is executed, but no answer was given
			colGroup.getFirst().addRow(new IntegerCsvCell(0), rowIndex);
		}
		else
		{
			for(LocalizedString answer : answers)
			{
				ICsvColumn col = colGroup.findColumByString(answer);
				
				if(col == null)
				{
					col = new MultipleAnswersCsvColumn(answer.getValue(locale), answer);
					
					colGroup.addColumn(col);
				}
				
				col.addRow(new IntegerCsvCell(1), rowIndex);
			}
		}
	}
}
