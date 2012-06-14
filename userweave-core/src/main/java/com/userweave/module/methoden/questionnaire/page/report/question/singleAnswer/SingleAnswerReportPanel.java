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

import java.util.List;
import java.util.Locale;

import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.domain.LocalizedString;
import com.userweave.module.methoden.questionnaire.dao.AnswerDao;
import com.userweave.module.methoden.questionnaire.domain.answer.AnswerToSingleAnswerQuestion;
import com.userweave.module.methoden.questionnaire.domain.question.SingleAnswerQuestion;
import com.userweave.module.methoden.questionnaire.page.report.question.multipleanswers.QuestionWithMultiplePossibleAnswersReportPanel;
import com.userweave.pages.configuration.report.FilterFunctorCallback;

@SuppressWarnings("serial")
public class SingleAnswerReportPanel extends QuestionWithMultiplePossibleAnswersReportPanel {

	@SpringBean
	private AnswerDao answerDao;

	private final SingleAnswerStatisticsComputer statistics;

	public SingleAnswerReportPanel(
		String id, 
		Locale locale, 
		SingleAnswerQuestion question, 
		FilterFunctorCallback filterFunctorCallback) 
	{
		super(id, locale, question);
		
		List<AnswerToSingleAnswerQuestion> answers = 
			answerDao.getValidAnswersForQuestion(
					question, filterFunctorCallback.getFilterFunctor());
		
		statistics = new SingleAnswerStatisticsComputer(answers);
		
		setTotalCount(statistics.getTotalAnswerCount());
	}

	@Override
	protected int getTotalAnswerCount() 
	{		
		return statistics.getTotalAnswerCount();
	}
	
	@Override
	protected int getAbsoluteCount(LocalizedString possibleAnswer)
	{
		return statistics.getAbsoluteCount(possibleAnswer);
	}
	
	@Override
	protected Double getRealtiveCount(LocalizedString possibleAnswer)
	{
		return statistics.getRelativeCount(possibleAnswer);
	}
}
