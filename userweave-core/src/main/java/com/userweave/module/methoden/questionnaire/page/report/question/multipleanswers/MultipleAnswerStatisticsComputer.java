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
package com.userweave.module.methoden.questionnaire.page.report.question.multipleanswers;

import java.io.Serializable;
import java.util.List;

import com.userweave.domain.LocalizedString;
import com.userweave.module.methoden.questionnaire.domain.answer.MultipleAnswersAnwer;
import com.userweave.module.methoden.questionnaire.page.report.question.AnswerCounter;

/**
 * Simple class to wrap statistics computing
 * for multiple answer question.
 * 
 * @author opr
 *
 */
public class MultipleAnswerStatisticsComputer implements Serializable
{
	private static final long serialVersionUID = 1L;

	/**
	 * Number of total answers given
	 */
	private int totalAnswerCount = 0;
	
	public int getTotalAnswerCount() { return totalAnswerCount; }

	/**
	 * Map of given answer with frequency of occurrence
	 */
	private final AnswerCounter<LocalizedString> valueCounter;
	
	/**
	 * Constructor
	 * 
	 * @param answers
	 * 		List of answers to evaluate.
	 */
	public MultipleAnswerStatisticsComputer(List<MultipleAnswersAnwer> answers)
	{
		valueCounter = new AnswerCounter<LocalizedString>();
		
		if (answers != null)
		{
			totalAnswerCount = 0;

			for (MultipleAnswersAnwer answer : answers)
			{
				if (answer.getAnswers() != null
						&& !answer.getAnswers().isEmpty())
				{
					totalAnswerCount++;

					for (LocalizedString answerString : answer.getAnswers())
					{
						valueCounter.addAnswer(answerString);
					}
				}
			}
		}
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
		if (totalAnswerCount != 0) 
		{
			return new Double(getAbsoluteCount(possibleAnswer)) / totalAnswerCount;
		}
		
		return new Double(totalAnswerCount);
	}
}
