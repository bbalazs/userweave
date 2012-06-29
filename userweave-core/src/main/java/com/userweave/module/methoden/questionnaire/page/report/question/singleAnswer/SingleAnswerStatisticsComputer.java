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
package com.userweave.module.methoden.questionnaire.page.report.question.singleAnswer;

import java.io.Serializable;
import java.util.List;

import com.userweave.domain.LocalizedString;
import com.userweave.module.methoden.questionnaire.domain.answer.AnswerToSingleAnswerQuestion;
import com.userweave.module.methoden.questionnaire.page.report.question.AnswerCounter;

public class SingleAnswerStatisticsComputer implements Serializable
{
	private static final long serialVersionUID = 1L;

	private final AnswerCounter<LocalizedString> valueCounter;
	
	public SingleAnswerStatisticsComputer(List<AnswerToSingleAnswerQuestion> answers)
	{
		valueCounter = new AnswerCounter<LocalizedString>();
		
		if (answers != null)
		{
			for (AnswerToSingleAnswerQuestion answer : answers)
			{
				if (answer.getAnswer() != null)
				{
					valueCounter.addAnswer(answer.getAnswer());
				}
			}
		}
	}
	
	public int getTotalAnswerCount() 
	{		
		return valueCounter.getTotalCount();		
	}
	
	/**
	 * Get the absolute count to a given answer
	 * 
	 * @param possibleAnswer
	 * 		Answer to get absolute from.
	 * 
	 * @return
	 */
	public int getAbsoluteCount(LocalizedString possibleAnswer)
	{
		return valueCounter.getCount(possibleAnswer);
	}
	
	/**
	 * Get the relative count to a given anwser.
	 * Computed as absoluteCount / totalCount
	 * 
	 * @param possibleAnswer
	 * 		Answer to get relative from.
	 * @return
	 */
	public Double getRelativeCount(LocalizedString possibleAnswer)
	{	
		int totalAnswerCount = valueCounter.getTotalCount();
		
		if (totalAnswerCount != 0) 
		{
			return new Double(getAbsoluteCount(possibleAnswer)) / totalAnswerCount;
		}
		
		return new Double(totalAnswerCount);
	}
}
