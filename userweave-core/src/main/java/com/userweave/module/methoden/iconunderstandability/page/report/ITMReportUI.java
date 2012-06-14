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

import java.util.List;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

import com.userweave.module.methoden.iconunderstandability.domain.ITMTestResult;
import com.userweave.module.methoden.iconunderstandability.domain.IconTermMatchingConfigurationEntity;

/**
 * @author oma
 */
@SuppressWarnings("serial")
public class ITMReportUI extends Panel {

	public ITMReportUI(String id, IconTermMatchingConfigurationEntity configuration) {
		super(id);

		add(new Label("conf", configuration.getName()));

		List<ITMTestResult> results = configuration.getValidResults();
		addTermIconCategorization(results);
		addIconTermCategorization(results);
	}

	private void addTermIconCategorization(List<ITMTestResult> results) {

		final ObjectCategorization iconTermCategorization = new IconMatchingReportHelperImpl(results).getIconTermCategorization();
		
		int maxNumberOfCategorizations = results != null ? results.size() : 0;

		add(
			new ITMReportPanel("reportPanel_iconTerm", iconTermCategorization, maxNumberOfCategorizations,
				new CategoryObjectDisplay<String>() {

					public void displayCategory(MarkupContainer item, String wicketId, String category) {
						item.add(new Label(wicketId, category));
					}

					public void displayObject(MarkupContainer item, String wicketId, ReportModel<String> reportModel) {
						item.add(new ImageWrapper(wicketId, Integer.decode(reportModel.getObject())));
					}

					public void displayMissingObject(MarkupContainer item, String wicketId) {
						item.add(new WrappedMissingImage(wicketId));
					}
				}
			)
		);
	}

	private void addIconTermCategorization(List<ITMTestResult> results) {

		final ObjectCategorization termIconCategorization = new IconMatchingReportHelperImpl(results).getTermIconCategorization();
	

		int maxNumberOfCategorizations = results != null ? results.size() : 0;

		add(
			new ITMReportPanel("reportPanel_termIcon", termIconCategorization, maxNumberOfCategorizations,
				new CategoryObjectDisplay<String>() {

					public void displayCategory(MarkupContainer item, String wicketId, String category) {
						item.add(new ImageWrapper(wicketId, Integer.decode(category)));
					}

					public void displayObject(MarkupContainer item, String wicketId, ReportModel<String> reportModel) {
						item.add(new Label(wicketId, reportModel.getObject()));
					}

					public void displayMissingObject(MarkupContainer item, String wicketId) {
						item.add(new Label(wicketId, "-"));
					}
				}
			)
		);
	}
}

