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
package com.userweave.module.methoden.questionnaire.page.report.question.freeanswer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.math.stat.descriptive.SummaryStatistics;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.domain.util.FormatUtils;
import com.userweave.module.methoden.questionnaire.dao.AnswerDao;
import com.userweave.module.methoden.questionnaire.domain.answer.FreeAnwer;
import com.userweave.module.methoden.questionnaire.domain.answer.FreeNumberAnswer;
import com.userweave.module.methoden.questionnaire.domain.answer.FreeTextAnswer;
import com.userweave.module.methoden.questionnaire.domain.question.FreeQuestion;
import com.userweave.module.methoden.questionnaire.page.report.QuestionReportPanel;

@SuppressWarnings("serial")
public class FreeQuestionReportPanel extends QuestionReportPanel {

	@SpringBean
	private AnswerDao answerDao;

	public FreeQuestionReportPanel(
		String id, Locale locale, FreeQuestion question, com.userweave.pages.configuration.report.FilterFunctorCallback filterFunctorCallback) {
		super(id, locale, question);
				
		List<FreeAnwer> answers = answerDao.getValidAnswersForQuestion(question, filterFunctorCallback.getFilterFunctor());
		
		List<FreeAnwer> notEmptyAnswers = new ArrayList<FreeAnwer>();
		
		SummaryStatistics summaryStatistics = SummaryStatistics.newInstance();
		
		boolean isNumber = false;
		if (answers != null) {
			for (FreeAnwer freeAnwer : answers) {
				String answerString = getAnswerString(freeAnwer);				
				if (answerString != null && !answerString.isEmpty()) {
					notEmptyAnswers.add(freeAnwer);
					
					if (isFreeNumberAnswer(freeAnwer)) {
						double number = ((FreeNumberAnswer)freeAnwer).getNumber();
						summaryStatistics.addValue(number);
						isNumber = true;
					}
				}
			}
		}
		
		boolean isVisible = question.getAnswerType() == FreeQuestion.AnswerType.NUMBER; 
		
		addNumber("mean", summaryStatistics.getMean(), isNumber, isVisible);
		
		addNumber("stdDeviation", summaryStatistics.getStandardDeviation(), isNumber, isVisible);
			
		setTotalCount(notEmptyAnswers.size());
		
		add(
			new ListView("answers", notEmptyAnswers) {
				@Override
				protected void populateItem(ListItem item) {
					item.add(new Label("answer", getAnswerString((FreeAnwer) item.getModelObject())));						
				}
			}
		);		
	}

	private void addNumber(
		String id, double number, final boolean isNumberFinal, boolean isVisible) {
		
		WebMarkupContainer row = new WebMarkupContainer(id + "Row") {
			@Override
			public boolean isVisible() {
				return isNumberFinal;
			}
		};
		
		row.add(new Label(id, Double.toString(FormatUtils.round(number, 2))));
		
		row.setVisible(isVisible);
		
		add(row);
	}

		
	private String getAnswerString(FreeAnwer answer) {
		if (isFreeNumberAnswer(answer)) {
			return Integer.toString(((FreeNumberAnswer) answer).getNumber());
		} else if (FreeTextAnswer.class.isAssignableFrom(answer.getClass())) {
			return ((FreeTextAnswer) answer).getText();
		}
		return "";
	}


	private boolean isFreeNumberAnswer(FreeAnwer answer) {
		return FreeNumberAnswer.class.isAssignableFrom(answer.getClass());
	}
}
