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



import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.components.model.LocalizedPropertyModel;
import com.userweave.dao.BaseDao;
import com.userweave.module.methoden.questionnaire.dao.MultipleRatingQuestionDao;
import com.userweave.module.methoden.questionnaire.domain.answer.MultipleRatingAnswer;
import com.userweave.module.methoden.questionnaire.domain.answer.SingleRatingAnswer;
import com.userweave.module.methoden.questionnaire.domain.question.MultipleRatingQuestion;
import com.userweave.module.methoden.questionnaire.domain.question.RatingTerm;
import com.userweave.module.methoden.questionnaire.page.QuestionnaireSurveyContext;
import com.userweave.module.methoden.questionnaire.page.survey.AnswerPanel;
import com.userweave.module.methoden.questionnaire.page.survey.singleratingpanel.SingleRatingPanel;
import com.userweave.utils.LocalizationUtils;

/**
 * @author oma
 */
@SuppressWarnings("serial")
public class MultRatingSurveyPanel extends AnswerPanel<MultipleRatingQuestion>  {

	@SpringBean
	private MultipleRatingQuestionDao questionDao;
	
	private final int numberOfRatingSteps;
	
	//private MultipleRatingAnswer answer;

	private Boolean showNoAnswerOption;

	
	public MultRatingSurveyPanel(
		String id, MultipleRatingQuestion question, QuestionnaireSurveyContext console, Locale locale) 
	{
		super(id, question, console, locale);
		
		add(new Label("antipode1", new LocalizedPropertyModel(getDefaultModel(), "antipodePair.antipode1", getLocale())));
		add(new Label("antipode2", new LocalizedPropertyModel(getDefaultModel(), "antipodePair.antipode2", getLocale())));
	
		numberOfRatingSteps = question.getNumberOfRatingSteps();
		
		showNoAnswerOption = question.getShowNoAnswerOption();
		if (showNoAnswerOption == null) {
			showNoAnswerOption = false;
		}

		add(new WebMarkupContainer("notStatedHeaderLeft").setVisible(showNoAnswerOption));
		
		
		IModel model = new PropertyModel(getDefaultModel(), "ratingTerms") {
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
		
		RepeatingView ratingTermsView = new RepeatingView("ratingTerms", model);
		
		add(ratingTermsView);
		
		for(final RatingTerm ratingTerm : (List<RatingTerm>)ratingTermsView.getDefaultModelObject())
		{
			WebMarkupContainer container = new WebMarkupContainer(ratingTermsView.newChildId());
			
			ratingTermsView.add(container);
			
			container.add(new MultiRatingSingleRatingPanel(
					"singleRatingPanel", numberOfRatingSteps,
					showNoAnswerOption, LocalizationUtils.getValue(
							ratingTerm.getText(), getLocale()), 
					new Model<Integer>()
					{
						@Override
						public Integer getObject()
						{
							return getRating(ratingTerm);
						}

						@Override
						public void setObject(Integer rating)
						{
							setRating(ratingTerm, rating);
						}
					}));
		}
		
//		add(
//			new ListView("ratingTerms", model) {
//				
//				 private boolean notApplicable;
//				 
//				 @Override
//				protected void populateItem(ListItem item) {
//					 final RatingTerm ratingTerm = (RatingTerm) item.getModelObject();
//					 
//					 item.add(
//						 new MultiRatingSingleRatingPanel(
//								 "singleRatingPanel", 
//								 numberOfRatingSteps, 
//								 showNoAnswerOption, 
//								 LocalizationUtils.getValue(ratingTerm.getText(), getLocale()),
//							new Model() {
//							 	@Override
//							 	public Object getObject() {
//							 		return getRating(ratingTerm);
//							 	}
//							 	
//								@Override
//								public void setObject(Object rating) {									
//									setRating(ratingTerm, (Integer) rating);
//								}
//							} 
//						) 
//					);							
//				 }
//			}
//		);
		
		add(new WebMarkupContainer("notStatedFooterLeft").setVisible(showNoAnswerOption));
	}


	
	@Override
	protected BaseDao<MultipleRatingQuestion> getQuestionDao() {
		return questionDao;
	}

	Map<RatingTerm, Integer> ratings = new HashMap<RatingTerm,Integer>();
	
	private Integer getRating(RatingTerm ratingTerm) {
		return ratings.get(ratingTerm);
	}
	
	private void setRating(RatingTerm ratingTerm, Integer rating) {
		ratings.put(ratingTerm, rating);
	}

	public void onSubmit() {
		MultipleRatingAnswer answer = (MultipleRatingAnswer) loadAnswer();
		if(answer == null) {
			answer = new MultipleRatingAnswer();
		}
		// here we assume that ratings has at least all elements from answer.getRatings
		// if not we must delete answer.ratings not in ratings
		for(RatingTerm ratingTerm : ratings.keySet()) {
			SingleRatingAnswer ratingAnswer = answer.getRating(ratingTerm);
			if(ratingAnswer == null) {
				ratingAnswer = new SingleRatingAnswer();
				ratingAnswer.setRatingTerm(ratingTerm);
				
				answer.addToRatings(ratingAnswer);
			}

			Integer rating = ratings.get(ratingTerm);
			if(rating == SingleRatingPanel.INDEX_OF_NOANSWER_OPTION) {
				rating = null;
			}
			
			ratingAnswer.setRating(rating);
		}
		saveAnswer(answer);
	}

	@Override
	protected void initAnswer() {
		MultipleRatingAnswer answer = (MultipleRatingAnswer) loadAnswer();
		if(answer != null) {
			for(SingleRatingAnswer ratingTerm : answer.getRatings()) {
				Integer rating = ratingTerm.getRating();
				if(rating == null) {
					rating = SingleRatingPanel.INDEX_OF_NOANSWER_OPTION;
				}
				setRating(ratingTerm.getRatingTerm(), rating);
			}
		}		
	}
}

