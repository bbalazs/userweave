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
package com.userweave.module.methoden.questionnaire.page.survey.multiplerating;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;

import com.userweave.module.methoden.questionnaire.page.survey.singleratingpanel.SingleRatingPanel;

/**
 * Panel to display choices for a multiple rating question.
 * 
 * @author oma
 */
public class MultiRatingSingleRatingPanel extends SingleRatingPanel 
{
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 * 
	 * @param id
	 * @param numberOfRatingSteps
	 * @param showNoAnswerOption
	 * @param term
	 * @param ratingModel
	 */
	public MultiRatingSingleRatingPanel(
		String id, Integer numberOfRatingSteps, 
		boolean showNoAnswerOption, 
		String term, IModel<Integer> ratingModel) 
	{
		super(id, numberOfRatingSteps, showNoAnswerOption, new RepeatingView("noAnswerOption"), ratingModel);	
		
		add(new Label("term", term));

		addRadioChoices(numberOfRatingSteps);
		 
	}

}

