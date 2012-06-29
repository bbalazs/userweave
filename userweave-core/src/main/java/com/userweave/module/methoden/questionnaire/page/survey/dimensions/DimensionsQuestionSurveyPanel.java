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

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.dao.BaseDao;
import com.userweave.module.methoden.questionnaire.dao.DimensionsQuestionDao;
import com.userweave.module.methoden.questionnaire.domain.answer.MultipleDimensionsAnswer;
import com.userweave.module.methoden.questionnaire.domain.answer.SingleDimensionAnswer;
import com.userweave.module.methoden.questionnaire.domain.question.AntipodePair;
import com.userweave.module.methoden.questionnaire.domain.question.DimensionsQuestion;
import com.userweave.module.methoden.questionnaire.page.QuestionnaireSurveyContext;
import com.userweave.module.methoden.questionnaire.page.survey.AnswerPanel;
import com.userweave.module.methoden.questionnaire.page.survey.singleratingpanel.SingleRatingPanel;

/**
 * @author oma
 */
@SuppressWarnings("serial")
public class DimensionsQuestionSurveyPanel extends AnswerPanel<DimensionsQuestion>  {

	@SpringBean
	private DimensionsQuestionDao questionDao;
	
	private Boolean showNoAnswerOption;
	
	public DimensionsQuestionSurveyPanel(
		String id, DimensionsQuestion question, QuestionnaireSurveyContext context, Locale locale) {
		super(id, question, context, locale);
		
		final int numberOfRatingSteps = question.getNumberOfRatingSteps();
		
		showNoAnswerOption = question.getShowNoAnswerOption();
		if (showNoAnswerOption == null) {
			showNoAnswerOption = false;
		}
		
		add(new WebMarkupContainer("notStatedHeaderLeft").setVisible(showNoAnswerOption));
		
		IModel model = new PropertyModel(getDefaultModel(), "antipodePairs") {
			private boolean sorted = false;
			@Override
			public Object getObject() {
				List objects = (List) super.getObject();
				if (!sorted) {
					sortPossibleAnswers(objects);
					sorted = true;
				}
				return objects;
			}	
		};
		
		RepeatingView view = new RepeatingView("antipodePairs", model);
		
		add(view);
		
		for(final AntipodePair antipodePair : (List<AntipodePair>)view.getDefaultModelObject())
		{
			WebMarkupContainer container = new WebMarkupContainer(view.newChildId());
			
			view.add(container);
			
			container.add(new DimensionsSingleRatingPanel(
					"singleRatingPanel", 
					numberOfRatingSteps, 
					showNoAnswerOption,
					question.getRandomizeAntipodePosition(),
					new Model(antipodePair),
					new Model<Integer>() 
					{
					 	@Override
					 	public Integer getObject() {
					 		return getRating(antipodePair);
					 	}

					 	@Override
						public void setObject(Integer rating) {									
							setRating(antipodePair, rating);
						}
					},
					DimensionsQuestionSurveyPanel.this.getLocale()
				));
		}
		
		
//		add(
//			new PropertyListView("antipodePairs", model) {
//				
//				 @Override
//				protected void populateItem(ListItem item) {
//					 final AntipodePair antipodePair = (AntipodePair) item.getModelObject();
//					 					
//					 item.add(
//						 new DimensionsSingleRatingPanel(
//							"singleRatingPanel", 
//							numberOfRatingSteps, 
//							showNoAnswerOption, 
//							item.getModel(),
//							new Model() {
//							 	@Override
//							 	public Object getObject() {
//							 		return getRating(antipodePair);
//							 	}
//
//							 	@Override
//								public void setObject(Object rating) {									
//									setRating(antipodePair, (Integer) rating);
//								}
//							},
//							DimensionsQuestionSurveyPanel.this.getLocale()
//						)
//					);			
//				 }		 				
//			}
//		);				
		
		add(new WebMarkupContainer("notStatedFooterLeft").setVisible(showNoAnswerOption));
	}
	
	private final Map<AntipodePair, Integer> ratings = new HashMap<AntipodePair, Integer>();
	
	private Integer getRating(AntipodePair antipodePair) {
		return ratings.get(antipodePair);
	}

	private void setRating(AntipodePair antipodePair, Integer rating) {
		ratings.put(antipodePair, rating);
	}

	public void onSubmit() {
		MultipleDimensionsAnswer answer = (MultipleDimensionsAnswer) loadAnswer();
		if(answer == null) {
			answer = new MultipleDimensionsAnswer();
		}
		
		// here we assume that ratings has at least all elements from answer.getRatings
		// if not we must delete answer.ratings not in ratings
		for(AntipodePair antipodePair: ratings.keySet()) {
			SingleDimensionAnswer ratingAnswer = answer.getRating(antipodePair);
			if(ratingAnswer == null) {
				ratingAnswer = new SingleDimensionAnswer();
				ratingAnswer.setAntipodePair(antipodePair);
				
				answer.addToRatings(ratingAnswer);
			}

			Integer rating = ratings.get(antipodePair);
			if(rating == SingleRatingPanel.INDEX_OF_NOANSWER_OPTION) {
				rating = null;
			}
			ratingAnswer.setRating(rating);
		}

		saveAnswer(answer);
	}
	
	@Override
	protected BaseDao<DimensionsQuestion> getQuestionDao() {		
		return questionDao;
	}

	@Override
	protected void initAnswer() {
		MultipleDimensionsAnswer answer = (MultipleDimensionsAnswer) loadAnswer();
		if(answer != null) {
			for(SingleDimensionAnswer ratingTerm : answer.getRatings()) {
				Integer rating = ratingTerm.getRating();
				if(rating == null) {
					rating = SingleRatingPanel.INDEX_OF_NOANSWER_OPTION;
				}
				setRating(ratingTerm.getAntipodePair(), rating);
			}
		}
	}
}

