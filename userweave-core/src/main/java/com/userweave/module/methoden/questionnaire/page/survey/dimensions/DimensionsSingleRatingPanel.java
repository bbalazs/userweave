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
package com.userweave.module.methoden.questionnaire.page.survey.dimensions;

import java.util.Locale;
import java.util.Random;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;

import com.userweave.components.model.LocalizedPropertyModel;
import com.userweave.module.methoden.questionnaire.domain.question.AntipodePair;
import com.userweave.module.methoden.questionnaire.page.survey.singleratingpanel.SingleRatingPanel;

/**
 * @author oma
 */
public class DimensionsSingleRatingPanel extends SingleRatingPanel 
{
	private static final long serialVersionUID = 1L;

	/**
	 * Flag to signal, if the order of antipodes should be
	 * as given or reversed (that is, antipode1 - antipode2 
	 * or antipode2 - antipode1)
	 */
	private boolean maintainOrder = true;
	
	/**
	 * Default constructor.
	 * 
	 * @param id
	 * @param numberOfRatingStepsWithoutNoAnswerOption
	 * @param showNoAnswerOption
	 * @param randomizeAntipodePosition
	 * @param antipodePairModel
	 * @param ratingModel
	 * @param locale
	 */
	public DimensionsSingleRatingPanel(
		String id, 
		Integer numberOfRatingStepsWithoutNoAnswerOption, 
		boolean showNoAnswerOption, 
		boolean randomizeAntipodePosition,
		IModel<AntipodePair> antipodePairModel, 
		IModel<Integer> ratingModel, 
		Locale locale) 
	{
		super(id, numberOfRatingStepsWithoutNoAnswerOption, 
			 showNoAnswerOption, new RepeatingView("noAnswerOption"), 
			 ratingModel);			

		// determine position of antipodes
		if(randomizeAntipodePosition)
		{
			maintainOrder = new Random().nextBoolean();
		}
		
		if(maintainOrder)
		{
			add(new Label("antipode1", new LocalizedPropertyModel(antipodePairModel, "antipode1", locale)));
			add(new Label("antipode2", new LocalizedPropertyModel(antipodePairModel, "antipode2", locale)));
		}
		else
		{
			add(new Label("antipode1", new LocalizedPropertyModel(antipodePairModel, "antipode2", locale)));
			add(new Label("antipode2", new LocalizedPropertyModel(antipodePairModel, "antipode1", locale)));
		}
		
		addRadioChoices(numberOfRatingStepsWithoutNoAnswerOption);
	}
	
	/**
	 * Override to reverse ordering of antipodes, if choosen so.
	 */
	@Override
	protected void addRadioChoices(Integer numberOfRatingSteps)
	{
		if(maintainOrder)
		{
			super.addRadioChoices(numberOfRatingSteps);
		}
		else
		{
			// reverse order of rating
			for (int index = numberOfRatingSteps - 1; index >= 0; index--) 
			{
				   String id = Integer.toString(index);
				   addRadioChoice(id, index);
			}
		}
	}
}

