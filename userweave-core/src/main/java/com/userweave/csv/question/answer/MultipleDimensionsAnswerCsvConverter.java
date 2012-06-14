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
import com.userweave.csv.table.AntipdePairCsvColumn;
import com.userweave.csv.table.ICsvColumn;
import com.userweave.csv.table.question.MultipleDimensionsColumnGroup;
import com.userweave.module.methoden.questionnaire.domain.answer.MultipleDimensionsAnswer;
import com.userweave.module.methoden.questionnaire.domain.answer.SingleDimensionAnswer;
import com.userweave.module.methoden.questionnaire.domain.question.AntipodePair;

/**
 * Converter for multiple dimensions answers. Each column
 * contains the given rating for the antipode pair.
 * 
 * @author opr
 *
 * @important the antipodePair has to be the 
 * 			  headline of the column!
 */
public class MultipleDimensionsAnswerCsvConverter extends
		AbstractAnswerCsvConverter<MultipleDimensionsAnswer>
{	
	public static void convertToCsvRow(
			MultipleDimensionsColumnGroup colGroup,
			MultipleDimensionsAnswer entity, 
			int rowIndex,
			Locale locale,
			Date finishedExecutionDate)
	{
		List<SingleDimensionAnswer> ratings = entity.getRatings();
		
		if(finishedExecutionDate != null &&
		   (ratings == null || ratings.isEmpty()))
		{
			// module is executed, but no answer was given
			colGroup.getFirst().addRow(new IntegerCsvCell(0), rowIndex);
		}
		else
		{
			for(SingleDimensionAnswer rating : ratings)
			{
				AntipodePair pair = rating.getAntipodePair();
				
				ICsvColumn col = colGroup.findByAntipodePair(pair);
			
				if(col == null)
				{
					String title = pair.getAntipode1().getValue(locale) + 
								   " : " +
					   			   pair.getAntipode2().getValue(locale);
		
					col = new AntipdePairCsvColumn(title, pair);
					
					colGroup.addColumn(col);
				}
				
				Integer ratingVal = rating.getRating() == null ? -1 : rating.getRating() + 1;
				
				col.addRow(	new IntegerCsvCell(ratingVal), rowIndex);
			}
		}
	}
}
