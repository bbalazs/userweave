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
package com.userweave.module.methoden.questionnaire.page.report.question.multiplerating;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import com.userweave.domain.util.FormatUtils;
import com.userweave.module.methoden.questionnaire.domain.question.AntipodePair;
import com.userweave.utils.LocalizationUtils;

/**
 * @author oma
 */
@SuppressWarnings("serial")
public class AntipodeRatingReport extends Panel {
	
	public static class AntipodeRatingListModel implements Serializable {

		public int getNumberOfRatingSteps() {
			return numberOfRatingSteps;
		}

		private final int numberOfRatingSteps;
	
		private final List<AntipodeRatingModel> ratingModels = new ArrayList<AntipodeRatingModel>();

		public AntipodeRatingListModel(int numberOfRatingSteps) {
			super();			
			this.numberOfRatingSteps = numberOfRatingSteps;
		}
		
		public List<AntipodeRatingModel> getRatingModels() {
			return ratingModels;
		}
		
		public void addToRatingModels(AntipodeRatingModel ratingModel) {
			ratingModels.add(ratingModel);
		}
	}
	
	public static class AntipodeRatingModel implements Serializable {
		
		private final Integer rating;
		private final int absoluteCount;
		private final double relativeCount;
		
		public AntipodeRatingModel(Integer rating, int absoluteCount, double relativeCount) {
			super();
			this.rating = rating;
			this.absoluteCount = absoluteCount;
			this.relativeCount = relativeCount;
		}
		
		public Integer getRating() {
			return rating;
		}
		
		public int getAbsoluteCount() {
			return absoluteCount;
		}
		
		public double getRelativeCount() {
			return relativeCount;
		}
	}

	public AntipodeRatingReport(
		String id, Locale locale, AntipodePair antipodePair, AntipodeRatingListModel ratingListModel) 
	{
		
		super(id);

		final String antipode1 = LocalizationUtils.getValue(antipodePair.getAntipode1(), locale);
		
		final String antipode2 = LocalizationUtils.getValue(antipodePair.getAntipode2(), locale);
		
		final int numberOfRatingSteps = ratingListModel.getNumberOfRatingSteps();
		
		add(
			new ListView("ratings", ratingListModel.getRatingModels()) {

				@Override
				protected void populateItem(ListItem item) {
				
					AntipodeRatingModel ratingModel = (AntipodeRatingModel) item.getModelObject();					
					Integer rating = ratingModel.getRating();
					
					String antipode = "";
					if (rating != null) {
						if (rating == 0) {
							antipode = antipode1;
						} else if (rating == numberOfRatingSteps-1) {
							antipode = antipode2;
						}

						item.add(new Label("rating", new Model(rating + 1)));
						item.add(new Label("antipode", new Model(antipode)));
					} else {
						item.add(new Label("rating", "x"));
						item.add(new Label("antipode", "missing"));
					}
					item.add(new Label("absolute", new Model(ratingModel.getAbsoluteCount())));								
																	
					item.add(new Label("relative", new Model(FormatUtils.formatAsPercent(ratingModel.getRelativeCount()))));
					
				}
		
			}
		);
	}




}

