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
import java.util.List;
import java.util.Locale;

import com.userweave.csv.IntegerCsvCell;
import com.userweave.csv.table.CsvColumn;
import com.userweave.csv.table.ICsvColumn;
import com.userweave.csv.table.question.MultipleRatingColumGroup;
import com.userweave.module.methoden.questionnaire.domain.answer.MultipleRatingAnswer;
import com.userweave.module.methoden.questionnaire.domain.answer.SingleRatingAnswer;

/**
 * Converter for multiple rating answers. Each column
 * contains the given rating for the rating term.
 * 
 * @author opr
 *
 * @important the rating term has to be the 
 * 			  headline of the column!
 */
public class MultipleRatingAnswerCsvConverter extends
		AbstractAnswerCsvConverter<MultipleRatingAnswer>
{
	public static void convertToCsvRow(
			MultipleRatingColumGroup colGroup,
			MultipleRatingAnswer entity, 
			int rowIndex,
			Locale locale,
			Date finishedExecutionDate)
	{
		List<SingleRatingAnswer> ratings = entity.getRatings();
		
		if(finishedExecutionDate != null &&
		   (ratings == null || ratings.isEmpty()))
		{
			// module is executed, but no answer was given
			colGroup.getFirst().addRow(new IntegerCsvCell(0), rowIndex);
		}
		else
		{
			for(SingleRatingAnswer rating : ratings)
			{
				ICsvColumn col = colGroup.getColumnByRatingTerm(
					rating.getRatingTerm().getText().getValue(locale));
			
				if(col == null)
				{
					col = new CsvColumn(rating.getRatingTerm().getText().getValue(locale));
					
					colGroup.addColumn(col);
				}
				
				Integer ratingVal = rating.getRating() == null ? -1 : rating.getRating() + 1;
				
				col.addRow(new IntegerCsvCell(ratingVal), rowIndex);
			}
		}
	}
}
