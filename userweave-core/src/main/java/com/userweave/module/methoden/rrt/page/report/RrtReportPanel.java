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
package com.userweave.module.methoden.rrt.page.report;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.components.model.LocalizedPropertyModel;
import com.userweave.module.methoden.rrt.dao.RrtResultDao;
import com.userweave.module.methoden.rrt.domain.RrtConfigurationEntity;
import com.userweave.module.methoden.rrt.domain.RrtTerm;
import com.userweave.pages.configuration.report.FilterFunctorCallback;

public class RrtReportPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	@SpringBean
	private RrtResultDao resultDao;
	
	private Map<Integer, List<Double>> termId2Positions;

	public RrtReportPanel(
		String id, 
		final boolean showDetails, 
		final RrtConfigurationEntity configuration,
		final FilterFunctorCallback filterFunctorCallback)
	{
		super(id);
		
		termId2Positions = new HashMap<Integer, List<Double>>();
		
		WebMarkupContainer container = new WebMarkupContainer("container");
		
		add(container);
		
		if (showDetails)
		{
			// pavkovic 2009.06.12: we want to use the system and not the study
			// locale at report panel
			// final Locale studyLocale = getConfiguration().getStudy().getLocale();
			final Locale studyLocale = getLocale();
			
			addResultCount(configuration, filterFunctorCallback, container);

			
			List<Object[]> results = 
				findValidResults(configuration, filterFunctorCallback);

			for (Object[] result : results)
			{
//				List<OrderedTerm> orderedTerms = result.getOrderedTerms();
//				if (orderedTerms != null)
//					for (OrderedTerm orderedTerm : orderedTerms)
//					{
//						addOrderedTermToReport(orderedTerm);
//					}
				
				if(result != null && result[0] != null && result[1] != null)
				{
					addOrderedTermToReport((Integer)result[0], (Integer)result[1]);
				}
			}
			
			final Map<Integer, MeanAndStdDeviation> termId2Mean = computeMeansForTerms(termId2Positions);

			List<RrtTerm> terms = configuration.getTerms();

			Collections.sort(terms, new Comparator<RrtTerm>()
			{

				// sort by mean and standard-deviation
				@Override
				public int compare(RrtTerm term1, RrtTerm term2)
				{
					final MeanAndStdDeviation meanAndStdDev1 = getMeanAndStdDevForTerm(
							termId2Mean, term1);
					final MeanAndStdDeviation meanAndStdDev2 = getMeanAndStdDevForTerm(
							termId2Mean, term2);

					Double mean1 = meanAndStdDev1 == null ? 0 : meanAndStdDev1
							.getMean();
					Double mean2 = meanAndStdDev2 == null ? 0 : meanAndStdDev2
							.getMean();
					int meanComparison = Double.compare(mean1, mean2);
					if (meanComparison != 0)
					{
						return meanComparison;
					}
					else
					{
						Double std1 = meanAndStdDev1 == null ? 0
								: meanAndStdDev1.getStdDeviation();
						Double std2 = meanAndStdDev2 == null ? 0
								: meanAndStdDev2.getStdDeviation();
						return Double.compare(std1, std2);
					}

				}

			});

			container.add(new ListView("results", terms)
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void populateItem(ListItem item)
				{
					RrtTerm term = (RrtTerm) item.getModelObject();

					item.add(new Label("term", new LocalizedPropertyModel(term,
							"value", studyLocale)));

					final MeanAndStdDeviation mean = getMeanAndStdDevForTerm(
							termId2Mean, term);

					if (mean != null)
					{
						String meanString = Double.toString(mean.getMean());
						item.add(new Label("mean", formatNumber(meanString)));

						String stdDeviationString = Double.toString(mean
								.getStdDeviation());
						item.add(new Label("stdDeviation",
								formatNumber(stdDeviationString)));
					}
					else
					{
						item.add(new Label("mean", "--"));
						item.add(new Label("stdDeviation", "--"));
					}
				}

				private String formatNumber(String stdDeviationString)
				{
					int indexPoint = stdDeviationString.indexOf('.');

					int digitsAfterPoint = 3;
					int lastIndex = indexPoint + digitsAfterPoint > stdDeviationString
							.length() ? stdDeviationString.length()
							: indexPoint + digitsAfterPoint;
					stdDeviationString = stdDeviationString.substring(0,
							lastIndex);
					return stdDeviationString;
				}
			});

		}
		else
		{
			container.setVisible(false);
		}
	}

	/**
	 * Adds the valid result counter to the display.
	 * 
	 * @param configuration
	 * 		Configuration to load results from.
	 * @param filterFunctorCallback
	 * 		Filter to filter results with.
	 * @param container
	 * 		Markup container to add result label.
	 */
	private void addResultCount(
			final RrtConfigurationEntity configuration,
			final FilterFunctorCallback filterFunctorCallback,
			WebMarkupContainer container)
	{
		final int resultCount = resultDao.getValidResultCount(
				configuration, filterFunctorCallback.getFilterFunctor());

		container.add(new Label("resultCount", Integer.toString(resultCount)));
	}
	
	private Map<Integer, MeanAndStdDeviation> computeMeansForTerms(Map<Integer, List<Double>> termId2Positions) {
		Map<Integer, MeanAndStdDeviation> rv = new HashMap<Integer, MeanAndStdDeviation>();
		for (Integer termId: termId2Positions.keySet()) {
			List<Double> positions = termId2Positions.get(termId);
			
			MeanAndStdDeviation item = new MeanAndStdDeviation(positions);
			rv.put(termId, item);
		}
		return rv;
	}
	
	protected void addOrderedTermToReport(Integer position, Integer termId) 
	{
//		Integer position = orderedTerm.getPosition()+1;
//		Integer termId = orderedTerm.getTerm().getId();

		Integer pos = position + 1;
		
		getPositionsForTerm(termId).add(new Double(pos.doubleValue()));	
	}
	
	private List<Double> getPositionsForTerm(Integer termId) 
	{
		List<Double> positions = termId2Positions.get(termId);
		if (positions == null) {
			positions = new ArrayList<Double>();
			termId2Positions.put(termId, positions);
		}
		return positions;
	}
	
	private MeanAndStdDeviation getMeanAndStdDevForTerm(
		final Map<Integer, MeanAndStdDeviation> termId2Mean, RrtTerm term) 
	{
		return termId2Mean.get(term.getId());
	}
	
	protected List<Object[]> findValidResults(
			final RrtConfigurationEntity configuration,
			final FilterFunctorCallback filterFunctorCallback) 
	{
		return resultDao.findValidResultsForRrtConf(
				configuration, filterFunctorCallback.getFilterFunctor()); 
	}
}
