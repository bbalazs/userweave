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
package com.userweave.module.methoden.questionnaire.page.conf.question.dimensions;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.ajax.form.AjaxBehaviorFactory;
import com.userweave.components.authorization.AuthOnlyDropDownChoice;
import com.userweave.domain.OrderedEntityBase;
import com.userweave.domain.StudyState;
import com.userweave.module.methoden.questionnaire.dao.QuestionDao;
import com.userweave.module.methoden.questionnaire.domain.question.AntipodePair;
import com.userweave.module.methoden.questionnaire.domain.question.DimensionsQuestion;
import com.userweave.module.methoden.questionnaire.page.conf.question.QuestionConfigurationPanel;

/**
 * @author oma
 */
public class DimensionsQuestionConfigurationPanel 
	extends QuestionConfigurationPanel<DimensionsQuestion> 
{	
	private static final long serialVersionUID = 1L;

	@SpringBean
	private QuestionDao questionDao;
	
	public DimensionsQuestionConfigurationPanel(
		String id, final int configurationId, Integer theQuestionId, Locale studyLocale) 
	{
		super(id, configurationId, theQuestionId, DimensionsQuestion.TYPE, studyLocale);
		
		addNoAnswerChoice();
		
		addRatingStepsDropDown(configurationId);
		
		addAntipodeReorderableListPanel(studyLocale);
		
		addRandomAntipodePostitionChoice();
	}

	private void addRandomAntipodePostitionChoice()
	{
		AuthOnlyDropDownChoice randomPos = 
			new AuthOnlyDropDownChoice(
				"randomPos",
				new PropertyModel(getDefaultModel(), "randomizeAntipodePosition"),
				Arrays.asList(new Boolean[] {true, false}),
				new IChoiceRenderer()
				{
					private static final long serialVersionUID = 1L;

					@Override
					public String getIdValue(Object object, int index)
					{
						return object.toString() + index;
					}
					
					@Override
					public Object getDisplayValue(Object object)
					{
						if(((Boolean) object))
						{
							return new StringResourceModel(
								"randomizedPosition", 
								DimensionsQuestionConfigurationPanel.this,
								null).getObject();
						}
						
						return new StringResourceModel(
								"fixedPosition", 
								DimensionsQuestionConfigurationPanel.this,
								null).getObject();
					}
				});
		
		randomPos.add(AjaxBehaviorFactory.getUpdateBehavior(
				"onchange", DimensionsQuestionConfigurationPanel.this));
		
		randomPos.setOutputMarkupId(true);
		
		getQuestionForm().add(randomPos);
	}
	
	/**
	 * Adds a reorderable list panel for antipodes.
	 * 
	 * @param studyLocale
	 * 		Locale of study.
	 */
	private void addAntipodeReorderableListPanel(Locale studyLocale)
	{
		getQuestionForm().add(
			new AntipodePairReorderableListPanel(
				"antipodeList", studyIsInState(StudyState.INIT), studyLocale) 
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void delete(AntipodePair objectToDelete, List<AntipodePair> objects) {
				getQuestion().removeFromAntipodePairs(objectToDelete);
				questionDao.save(getQuestion());				
			}

			@Override
			protected List<AntipodePair> getDisplayObjects() {
				return getQuestion().getAntipodePairs();
			}

			@Override
			protected void moveDown(AntipodePair orderedObject,List<AntipodePair> objects) {
				OrderedEntityBase.moveDown(objects,orderedObject);
				questionDao.save(getQuestion());				
			}

			@Override
			protected void moveUp(AntipodePair orderedObject, List<AntipodePair> objects) {
				OrderedEntityBase.moveUp(objects,orderedObject);
				questionDao.save(getQuestion());				
			}
			
			@Override
			protected boolean addIsVisible() {
				return isEditable();
			}

			@Override
			public AntipodePair append() 
			{
				AntipodePair antipodePair = new AntipodePair();	
				
				getQuestion().addToAntipodePairs(antipodePair);
				
				questionDao.save(getQuestion());
				
				return antipodePair;
			}
		});
	}

	/**
	 * Add a drop down choice to display the number of rating steps for
	 * this question.
	 * 
	 * @param configurationId
	 * 		Id of question configuration.
	 */
	private void addRatingStepsDropDown(final int configurationId)
	{
		AuthOnlyDropDownChoice dropdown = new AuthOnlyDropDownChoice(
				"numberOfRatingSteps", 					
				Arrays.asList(new Integer[] {4, 5, 6, 7, 8, 9})
			);
		
		dropdown.setRequired(true);
		
		dropdown.setOutputMarkupId(true);
		
		
		Boolean showNoAnswer = getQuestion().getShowNoAnswerOption();
		
		// may be null, if question has been created
		if(showNoAnswer == null)
		{
			getQuestion().setShowNoAnswerOption(false);
			addQuestionToConfigurationAndSave(configurationId);
		}
		
		getQuestionForm().add(dropdown);
		
		dropdown.add(AjaxBehaviorFactory.getUpdateBehavior(
			"onchange", DimensionsQuestionConfigurationPanel.this));
	}

	/**
	 * Dropdown to select if the "no answer" choice should be shown.
	 */
	private void addNoAnswerChoice()
	{
		AuthOnlyDropDownChoice noAnswer = 
			new AuthOnlyDropDownChoice(
				"showNoAnswerOption",
				new PropertyModel(getDefaultModel(), "showNoAnswerOption"),
				Arrays.asList(new Boolean[] {true, false}),
				new IChoiceRenderer()
				{
					private static final long serialVersionUID = 1L;

					@Override
					public String getIdValue(Object object, int index)
					{
						return object.toString() + index;
					}
					
					@Override
					public Object getDisplayValue(Object object)
					{
						if(((Boolean) object))
						{
							return new StringResourceModel(
								"showNoAnswer", 
								DimensionsQuestionConfigurationPanel.this,
								null).getObject();
						}
						
						return new StringResourceModel(
								"showNoAnswerReset", 
								DimensionsQuestionConfigurationPanel.this,
								null).getObject();
					}
				});
		
		noAnswer.add(AjaxBehaviorFactory.getUpdateBehavior(
				"onchange", DimensionsQuestionConfigurationPanel.this));
		
//		noAnswer.add(new AjaxFormComponentUpdatingBehavior("onchange") 
//		{	
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			protected void onUpdate(AjaxRequestTarget target) 
//			{
//				getQuestion().setShowNoAnswerOption(showNoAnswer);
//				
//				target.addComponent(DimensionsQuestionConfigurationPanel.this.get("feedbackPanel"));				
//				addQuestionToConfigurationAndSave(configurationId);
//			}
//		});
		
		noAnswer.setOutputMarkupId(true);
		
		getQuestionForm().add(noAnswer);
	}
	
	@Override
	protected QuestionDao getQuestionDao() {
		return questionDao;
	}
	
	@Override
	protected IModel getTypeModel()
	{
		return new StringResourceModel("dimensions_type", this, null);
	}
}

