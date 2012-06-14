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
package com.userweave.module.methoden.questionnaire.page.report.question;

import java.util.Locale;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.userweave.components.authorization.AuthOnlyAjaxLink;
import com.userweave.components.customModalWindow.CustomModalWindow;
import com.userweave.domain.util.FormatUtils;
import com.userweave.module.methoden.questionnaire.domain.question.AntipodePair;
import com.userweave.module.methoden.questionnaire.domain.question.Question;
import com.userweave.module.methoden.questionnaire.page.report.QuestionReportPanel;
import com.userweave.module.methoden.questionnaire.page.report.question.multiplerating.AntipodeRatingReport;
import com.userweave.module.methoden.questionnaire.page.report.question.multiplerating.AntipodeRatingReport.AntipodeRatingListModel;
import com.userweave.module.methoden.questionnaire.page.report.question.multiplerating.AntipodeRatingReport.AntipodeRatingModel;

/**
 * Panel to display a report for rating and dimension questions.
 * 
 * @author oma
 * @author opr
 */
public abstract class RatingAndDimensionsReportPanel<T> extends QuestionReportPanel
{
	private static final long serialVersionUID = 1L;

	/**
	 * Fragment to display a one column headline for 
	 * answer list (used by rating terms / rating questions).
	 * 
	 * @author opr
	 *
	 */
	protected class OneColumnFragment extends Fragment
	{
		private static final long serialVersionUID = 1L;
		
		/**
		 * Default constructor.
		 * 
		 * @param id
		 * 		Component markup id.
		 */
		public OneColumnFragment(String id)
		{
			super(id, "oneColumnFragment", RatingAndDimensionsReportPanel.this);
		}
	}
	
	/**
	 * Fragment to display a two column headline for 
	 * answer list (used by antipode pairs / dimension questions).
	 * 
	 * @author opr
	 *
	 */
	protected class TwoColumnFragment extends Fragment
	{
		private static final long serialVersionUID = 1L;
		
		/**
		 * Default constructor.
		 * 
		 * @param id
		 * 		Component markup id.
		 */
		public TwoColumnFragment(String id)
		{
			super(id, "twoColumnFragment", RatingAndDimensionsReportPanel.this);
		}
	}
	
	/**
	 * Returns a fragment to display either two or one columns for
	 * the table headline.
	 * 
	 * @param isOneColumn
	 * 		If true, the table needs only one column for the headline.
	 * @param id
	 * 		Component markup id.
	 * @return
	 */
	private Fragment getHeadlineColumnFragment(boolean isOneColumn, String id)
	{
		if(isOneColumn)
		{
			return new OneColumnFragment(id);
		}
		
		return new TwoColumnFragment(id);
	}
	
	/**
	 * Default constructor.
	 * 
	 * @param id
	 * 		Component markup id.
	 * @param locale
	 * 		Locale to display strings in.
	 * @param question
	 * 		Question to create report for.
	 */
	public RatingAndDimensionsReportPanel(String id, Locale locale, Question question) 
	{
		super(id, locale, question);
		
		setOutputMarkupId(true);
		
		add(getHeadlineColumnFragment(isOneColumnHeadline(), "eventScale"));
		
		// append table sorter
		AjaxRequestTarget target = AjaxRequestTarget.get();
		
		triggerTableSorter(target);
	}
	
	private void triggerTableSorter(AjaxRequestTarget target)
	{
		if(target != null)
			target.appendJavaScript("$(\"#ratingTable\").tablesorter();");
	}
	
	public void addAntipodeRatingReport(
		Question q, AnswerStatistics<T> ratingStatistics, ListItem item, T object) 
	{
		Label mean = new Label("meanRating", getValueModel(ratingStatistics.getRatingMean(object) + 1))
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Locale getLocale()
			{
				// workaround for i18n, because the table sorter 
				// can't sort double values in german, because they
				// contain a , instead of an .
				return Locale.ENGLISH;
			}
		};
		
		item.add(mean);

		
		Label std = new Label("stdDeviation", getValueModel(ratingStatistics.getRatingStandardDeviation(object)))
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Locale getLocale()
			{
				return Locale.ENGLISH;
			}
		};
		
		item.add(std);
		
		
		final ModalWindow window = createDetailsModal(q, object, item);
		
		window.setTitle(getModalWindowTitle(item));
		
		item.add(window);
		
		item.add(new AuthOnlyAjaxLink("showDetails")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				window.show(target);
			}
		});
	}

	protected AntipodeRatingListModel createRatingListModel(
		final AnswerStatistics<T> ratingStatistics, final int numberOfAnswers, 
		final int numberOfRatingSteps, final T object, boolean showNoAnswerOption) 
	{
		
		AntipodeRatingListModel rv = new AntipodeRatingListModel(numberOfRatingSteps);
		
		for (int rating = 0; rating < numberOfRatingSteps; rating++) {
			
			int absoluteCount = ratingStatistics.getCount(object, rating);								
			double relativeCount = absoluteCount / new Double(numberOfAnswers);								
			
			rv.addToRatingModels(
				new AntipodeRatingModel(
					rating,
					absoluteCount,
					relativeCount
				)
			);
		}
		
		if (showNoAnswerOption) {
			int missing = ratingStatistics.getMissing(object);
			double missingRelativeCount = missing / new Double(numberOfAnswers);
			
			rv.addToRatingModels(
				new AntipodeRatingModel(
					null,
					missing,
					missingRelativeCount
				)
			);
		}

		return rv;
	}				

	protected Model getValueModel(Double value) {
		Model model;
		if (value != null) {
			model = new Model(FormatUtils.round(value, 2));
		} else {
			model = new Model("n. a.");
		}
		return model;
	}

	/**
	 * Factory method to create a Dialog to display details
	 * of a specific question.
	 * 
	 * @return
	 * 		A modal window.
	 */
	private ModalWindow createDetailsModal(
		final Question question, final T object, final ListItem item)
	{
		final CustomModalWindow window = new CustomModalWindow("showDetailsModal");
		
		window.setPageCreator(new ModalWindow.PageCreator()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page createPage()
			{
				return getDetailsPage(window, question, object, item);
			}
		});
		
		window.setWindowClosedCallback(new ModalWindow.WindowClosedCallback()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClose(AjaxRequestTarget target)
			{
				target.add(RatingAndDimensionsReportPanel.this);
				triggerTableSorter(target);
			}
		});
		
		return window;
	}
	
	/**
	 * Creates an AntipodeRatingReport for a specific
	 * antipode pair.
	 * 
	 * @param antipodePair
	 * 		Pair of antipodes.
	 * @param model
	 * 		Model to get details for the report.
	 */
	protected AntipodeRatingReport createRatingReport(
		String id, AntipodePair antipodePair, AntipodeRatingListModel model)
	{
		return new AntipodeRatingReport(id, getStudyLocale(), antipodePair, model);
	}
	
	protected abstract boolean isOneColumnHeadline();
	
	protected abstract IModel<String> getModalWindowTitle(ListItem item);
	
	/**
	 * Get the web page for the details dialog
	 * 
	 * @param window
	 * 		Modal window the page will be attached to.
	 * 
	 * @return
	 * 		A WebPage to display specific details.
	 */
	protected abstract Page getDetailsPage(
		ModalWindow window, Question q, T object, ListItem item);
}

