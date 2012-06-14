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
package com.userweave.module.methoden.questionnaire.page.report.question.dimensions;

import java.util.List;
import java.util.Locale;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.userweave.components.model.LocalizedPropertyModel;
import com.userweave.dao.filter.FilterFunctor;
import com.userweave.module.methoden.questionnaire.dao.AnswerDao;
import com.userweave.module.methoden.questionnaire.domain.answer.SingleDimensionAnswer;
import com.userweave.module.methoden.questionnaire.domain.question.AntipodePair;
import com.userweave.module.methoden.questionnaire.domain.question.DimensionsQuestion;
import com.userweave.module.methoden.questionnaire.domain.question.Question;
import com.userweave.module.methoden.questionnaire.page.report.question.AnswerStatistics;
import com.userweave.module.methoden.questionnaire.page.report.question.AntipodeRatingPage;
import com.userweave.module.methoden.questionnaire.page.report.question.RatingAndDimensionsReportPanel;
import com.userweave.module.methoden.questionnaire.page.report.question.multiplerating.MultipleRatingReportPanel;
import com.userweave.pages.configuration.report.FilterFunctorCallback;

/**
 * @author oma
 */
public class DimensionsReportPanel extends RatingAndDimensionsReportPanel<Integer> 
{
	private static final long serialVersionUID = 1L;
	
	private final static Logger logger = LoggerFactory.getLogger(MultipleRatingReportPanel.class);
	
	@SpringBean
	private AnswerDao answerDao;
	
	private final AnswerStatistics<Integer> ratingStatistics;
	
	private final Long numberOfAnswers;
	
	public DimensionsReportPanel(
		String id, final Locale locale, 
		final DimensionsQuestion question, 
		FilterFunctorCallback filterFunctorCallback) 
	{
		super(id, locale, question);
		
		ratingStatistics = new AnswerStatistics<Integer>();	
		
		FilterFunctor filterFunctor = filterFunctorCallback.getFilterFunctor();
		
		long startTime = System.currentTimeMillis();
		List<SingleDimensionAnswer> answers = 
			answerDao.getValidAnswersForQuestion(question, filterFunctor);
		long overallTime = System.currentTimeMillis()-startTime;
		logger.info("OVERALLTIME: "+overallTime+ " milliseconds");
		
		numberOfAnswers = 
			answerDao.getGroupedValidAnswersForDimensionQuestion(
				question, filterFunctor);
			
		if (answers != null)
		{
			for (SingleDimensionAnswer singleDimensionAnswer : answers)
			{
				AntipodePair antipodePair = singleDimensionAnswer
						.getAntipodePair();
				Integer rating = singleDimensionAnswer.getRating();
				ratingStatistics.addRating(antipodePair.getId(), rating);
			}
			
			setTotalCount(numberOfAnswers.intValue());
		}
				
		add(
			new PropertyListView<AntipodePair>("objects", question.getAntipodePairs())
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void populateItem(ListItem<AntipodePair> item) {
					
					final AntipodePair antipodePair = item.getModelObject();

					int answerCount = ratingStatistics.getTotalCount(antipodePair.getId());
					
					item.add(new Label("answerCount", Integer.toString(answerCount)));
										
					item.add(new Label("antipode1", new LocalizedPropertyModel(item.getModel(), "antipode1", getStudyLocale())));
					
					item.add(new Label("antipode2", new LocalizedPropertyModel(item.getModel(), "antipode2", getStudyLocale())));
					
					addAntipodeRatingReport(
						question,
						ratingStatistics,
						item,
						antipodePair.getId());
				}
			}
		);
		
	}

	@Override
	protected boolean isOneColumnHeadline()
	{
		return false;
	}
	
	@Override
	protected Page getDetailsPage(
		final ModalWindow window, final Question q, final Integer object, final ListItem item)
	{			
			return new AntipodeRatingPage(
				getValueModel(ratingStatistics.getRatingMean(object) + 1), 
				getValueModel(ratingStatistics.getRatingStandardDeviation(object)),
				ratingStatistics.getTotalCount(object))
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onAccept(AjaxRequestTarget target)
				{
					window.close(target);
				}
				
				@Override
				protected void onDecline(AjaxRequestTarget target)
				{
					window.close(target);
				}
				
				@Override
				protected Component getRatingReport(String componentId)
				{
					DimensionsQuestion question = (DimensionsQuestion) q;
					
					final int numberOfRatingSteps = 
						question.getNumberOfRatingSteps() != null ? 
								question.getNumberOfRatingSteps() : 
								0;
					
					final AntipodePair antipodePair = 
						(AntipodePair) item.getModelObject();
					
					Boolean noAnswerOption = question.getShowNoAnswerOption();
					
					if(noAnswerOption == null)
					{
						noAnswerOption = new Boolean(false);
					}
					
					return createRatingReport(
							componentId, 
							antipodePair,
							createRatingListModel(
								ratingStatistics, 
								numberOfAnswers.intValue(), 
								numberOfRatingSteps, 
								object, 
								noAnswerOption.booleanValue()));
				}
			};
	}
	
	@Override
	protected IModel<String> getModalWindowTitle(ListItem item)
	{
		AntipodePair pair = (AntipodePair) item.getModelObject();
		
		return new StringResourceModel(
				"details_for", this, null, 
				new Object[] { pair.getAntipode1().getValue(getStudyLocale()) + 
							   " - " +
							   pair.getAntipode2().getValue(getStudyLocale())});
	}

}

