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
package com.userweave.module.methoden.iconunderstandability.page.report;

import java.util.ArrayList;
import java.util.Comparator;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

/**
 * @author oma
 */
@SuppressWarnings("serial")
public class ITMReportPanel extends Panel {


	/**
	 * 
	 * @param id
	 * @param objectCategorization holding categorization of objects into categories
	 * @param categorizationCount maximum number of categorizations 
	 * @param displayObjectFunctor
	 */
	public ITMReportPanel(String id, ObjectCategorization objectCategorization, int categorizationCount, CategoryObjectDisplay displayObjectFunctor) {
		super(id);
		addTermReports(objectCategorization, categorizationCount, displayObjectFunctor);
	}

	private void addTermReports(final ObjectCategorization objectCategorization, final int categorizationCount, final CategoryObjectDisplay displayObject) {
		ArrayList terms = new ArrayList(objectCategorization.getCategories());
			add(
			new ListView("categories", terms) {

				@Override
				protected void populateItem(ListItem item) {
					final String category = (String) item.getModelObject();
					
					displayObject.displayCategory(item, "category", category);	
					
					final int objectCountForCategorySum = objectCategorization.getObjectCountForCategory(category);
					
					// how many times the object was not categorized 
					final int missing = categorizationCount - objectCountForCategorySum;
					
					item.add(new Label("missing", Integer.toString(missing)));	

					item.add(new Label("sum", Integer.toString(objectCountForCategorySum)));					

					// create list of report models
					ArrayList<ReportModel> reportModels = new ArrayList<ReportModel>();
					for (String object : objectCategorization.getObjectsForCategory(category)) {
						reportModels.add(new ReportModel(object, objectCategorization.getCountForCategoryAndObject(category, object)));						
					}

					// add line for missing-categorizations
					if (missing > 0) {
						reportModels.add(new ReportModel(null, missing));
					}

					// sort by count
					java.util.Collections.sort(reportModels, new Comparator<ReportModel>() {
						public int compare(ReportModel reportModel1, ReportModel reportModel2) {						
							return new Integer(reportModel2.getCount()).compareTo(reportModel1.getCount());
						}});
					
					item.add(
						new ListView("objects", reportModels) {

							@Override
							protected void populateItem(ListItem item) {
								
								final ReportModel reportModel = (ReportModel) item.getModelObject();

								// display object on  the left
								if (reportModel.getObject() != null) {
									displayObject.displayObject(item, "object", reportModel);
								} else {
									displayObject.displayMissingObject(item, "object");
								}
								
								// percent this object was classified for current category 
								String percent = Double.toString(reportModel.getCount() * 100 / categorizationCount);
								addBar(item, percent, reportModel.getObject() != null);

								item.add(new Label("percent", percent));
									
								String iconCount = Integer.toString(reportModel.getCount());
								item.add(new Label("count", iconCount));			
							}

							private void addBar(ListItem item, String percent, boolean missing) {
								Label bar = new Label("bar", "");
								if (missing) {
									bar.add(new AttributeModifier("class", true, new Model("color")));
								} else {
									bar.add(new AttributeModifier("class", true, new Model("color-missing")));
								}
								bar.add(new AttributeModifier("style", true, new Model("width: " + percent + "%")));
								item.add(bar);
							}					
						}
					);
				}
				
			}
		);
	}
}

