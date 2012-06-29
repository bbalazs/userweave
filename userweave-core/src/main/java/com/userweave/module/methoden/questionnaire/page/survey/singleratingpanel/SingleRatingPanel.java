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
package com.userweave.module.methoden.questionnaire.page.survey.singleratingpanel;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Displays a row with radio choices for rating questions
 * (matrix and sem. diff.).
 * 
 * @author opr
 *
 */
public class SingleRatingPanel extends Panel 
{
	private static final long serialVersionUID = 1L;

	/**
	 * If no answer can be given, this is the index for this
	 * answer.
	 */
	public static final Integer INDEX_OF_NOANSWER_OPTION = -1;
	
	/**
	 * Radio group containing the radio input fields.
	 */
	private RadioGroup<Integer> group;

	protected WebMarkupContainer getRadioGroup() 
	{
		return group;
	}
	
	/**
	 * Repeater for the radio input fields.
	 */
	private RepeatingView choices;
	
	/**
	 * Container for the no answer option. May be null.
	 */
	RepeatingView containerForNoAnswerOption = null;

	/**
	 * Constructor for a question which does not contain a container
	 * for the no answer option.
	 * 
	 * @param id
	 * 		Component markup id
	 * @param numberOfRatingSteps
	 * 		Number of radio input fields to display.
	 * @param showNoAnswerOption
	 * 		Display the no answer container? Valid only, if the container
	 * 		is not null.
	 * @param ratingModel
	 * 		Model to get/set the rating given for this question.
	 */
	public SingleRatingPanel(
		String id, Integer numberOfRatingSteps, 
		final boolean showNoAnswerOption, 
		final IModel<Integer> ratingModel) 
	{
		super(id);
		
		init(numberOfRatingSteps, showNoAnswerOption, ratingModel);
	}
	
	/**
	 * Constructor for a question which does contain a container
	 * for the no answer option.
	 * 
	 * @param id
	 * 		Component markup id
	 * @param numberOfRatingSteps
	 * 		Number of radio input fields to display.
	 * @param showNoAnswerOption
	 * 		Display the no answer container? Valid only, if the container
	 * 		is not null.
	 * @param containerForNoAnswerOption
	 * 		Container element, which holds the radio choiche for the 
	 * 		no answer option.
	 * @param ratingModel
	 * 		Model to get/set the rating given for this question.
	 */
	public SingleRatingPanel(
		String id, Integer numberOfRatingSteps, 
		final boolean showNoAnswerOption, 
		RepeatingView containerForNoAnswerOption, final IModel<Integer> ratingModel) 
	{
		super(id);
		
		this.containerForNoAnswerOption = containerForNoAnswerOption;
		
		init(numberOfRatingSteps, showNoAnswerOption, ratingModel);
	}

	/**
	 * Initializes this component.
	 * 
	 * @param numberOfRatingSteps
	 * @param showNoAnswerOption
	 * @param ratingModel
	 */
	private void init(
		Integer numberOfRatingSteps, 
		final boolean showNoAnswerOption, 
		final IModel<Integer> ratingModel) 
	{
		group = new RadioGroup<Integer>("group", new Model<Integer>() 
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Integer getObject() 
			{
				return ratingModel.getObject();
			}

			@Override
			public void setObject(Integer object) 
			{	
				ratingModel.setObject(object);
			}			
	   	});
		
	   super.add(group);
	   
	   choices = new RepeatingView("choices");
	   group.add(choices);

	   if (containerForNoAnswerOption != null) 
	   {
		   group.add(containerForNoAnswerOption);
		   containerForNoAnswerOption.setVisible(showNoAnswerOption);
		   String id = Integer.toString(numberOfRatingSteps);
		   containerForNoAnswerOption.add(createRadioChoice(id, INDEX_OF_NOANSWER_OPTION));
	   }
	}

	/**
	 * ATENTION: must be called in child components.
	 * 
	 * Add radio choice elements for each rating step.
	 * 
	 * @param numberOfRatingSteps
	 */
	protected void addRadioChoices(Integer numberOfRatingSteps)
	{
		for (int index = 0; index < numberOfRatingSteps; index++) 
		{
			   String id = Integer.toString(index);
			   addRadioChoice(id, index);
		}
	}
	
	/**
	 * Adds a radio input field to the choices repeater.
	 * 
	 * @param id
	 * 		Component markup id.
	 * @param index
	 * 		Index of choice.
	 */
	protected void addRadioChoice(String id, int index)
	{
		 choices.add(createRadioChoice(id, index));
	}
	
	/**
	 * Factory method to create a radio choice element.
	 * 
	 * @param id
	 * 		Component markup id.
	 * @param index
	 * 		Index of choice.
	 * @return
	 *		A container which contains a radio choice panel.
	 */
	private WebMarkupContainer createRadioChoice(String id, int index) 
	{
		WebMarkupContainer container = new WebMarkupContainer(id);	   
	    container.add(new RadioChoicePanel("content", index));
	    
	    return container;
	}
	
	/**
	 * Add all child elements to the radio group.
	 */
	@Override
	public MarkupContainer add(Component... childs)
	{
		return getRadioGroup().add(childs);
	}
}
