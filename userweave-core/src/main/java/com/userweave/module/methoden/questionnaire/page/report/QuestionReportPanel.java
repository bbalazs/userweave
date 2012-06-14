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
package com.userweave.module.methoden.questionnaire.page.report;

import java.util.Locale;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import com.userweave.components.model.LocalizedPropertyModel;
import com.userweave.module.methoden.questionnaire.domain.question.Question;

/**
 * Base class for a report of a question. Displays the
 * question type, the stimulus and the number of valid
 * answers.
 * 
 * @author oma
 */
public abstract class QuestionReportPanel extends Panel 
{
	private static final long serialVersionUID = 1L;

	/**
	 * Locale to display results in.
	 */
	private final Locale studyLocale;
	
	protected Locale getStudyLocale() 
	{
		return studyLocale;
	}
	
	/**
	 * Number of valid answers.
	 */
	private int totalCount;
	
	public void setTotalCount(int totalCount) 
	{
		this.totalCount = totalCount;
	}

	protected int getTotalCount() 
	{
		return totalCount;
	}
	
	/**
	 * Default constructor.
	 * 
	 * @param id
	 * 		Component markup id.
	 * @param studyLocale
	 * 		Locale of the study this question belongs to.
	 * @param question
	 * 		The question entity to evaluate.
	 */
	public QuestionReportPanel(String id, Locale studyLocale, Question question) {
		super(id);
		
		// pavkovic 2009.06.12: we want to use the system 
		// and not the study locale at report panel
		this.studyLocale = getLocale();
		
		add(new Label(
			"questionText", 
			new LocalizedPropertyModel(question, "text", getStudyLocale())));
		
		add(
			new Label("totalCount", 
				new Model<Integer>() 
				{
					private static final long serialVersionUID = 1L;

					@Override
					public Integer getObject()
					{
						return getTotalCount();
					}
				}
			)
		);		
	}
}

