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

import java.util.List;

import com.userweave.csv.AbstractCsvCell;
import com.userweave.csv.EmptyCsvCell;
import com.userweave.module.methoden.questionnaire.domain.answer.Answer;

/**
 * @author opr
 *
 */
public abstract class AbstractAnswerCsvConverter<T extends Answer<?>> 
{
	protected void fillSubHeadlinesWithEmpty(List<AbstractCsvCell<?>> subHeadlines, int size)
	{
		if(subHeadlines == null)
		{
			return;
		}
		
		int i = size;
		
		while(i > 0)
		{
			i--;
			subHeadlines.add(new EmptyCsvCell());
		}
	}
}
