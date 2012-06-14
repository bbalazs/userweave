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

import java.util.Locale;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;

import com.userweave.components.model.LocalizedModel;
import com.userweave.domain.LocalizedString;
import com.userweave.domain.util.FormatUtils;
import com.userweave.module.methoden.questionnaire.domain.QuestionWithMultiplePossibleAnswers;
import com.userweave.module.methoden.questionnaire.page.report.QuestionReportPanel;

@SuppressWarnings("serial")
public abstract class QuestionWithMultiplePossibleAnswersReportPanel extends QuestionReportPanel {

	public QuestionWithMultiplePossibleAnswersReportPanel(
		String id, Locale locale, QuestionWithMultiplePossibleAnswers question) 
	{
	
		super(id, locale, question);
		
		// append table sorter
		AjaxRequestTarget target = AjaxRequestTarget.get();
		
		target.appendJavaScript("$(\"#ratingTable\").tablesorter();");
		
		add(
			new ListView("answers", question.getPossibleAnswers()) {
				@Override
				protected void populateItem(ListItem item) 
				{
					LocalizedString possibleAnswer = (LocalizedString) item.getModelObject();

					int absoluteCount = getAbsoluteCount(possibleAnswer);
					double relativeCount = getRealtiveCount(possibleAnswer);
					
					item.add(new Label(
						"possibleAnswer", new LocalizedModel(possibleAnswer, getStudyLocale())));
					item.add(new Label("absoluteCount", Integer.toString(absoluteCount)));
					item.add(new Label("relativeCount", FormatUtils.formatAsPercent(relativeCount)));					
				}
			}
		);
	}
	
	/**
	 * number of single answers aggregated over multiple-answers
	 * @return
	 */
	protected abstract int getTotalAnswerCount();

	protected abstract int getAbsoluteCount(LocalizedString possibleAnswer);
	
	protected abstract Double getRealtiveCount(LocalizedString possibleAnswer);
}
