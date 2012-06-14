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

import com.userweave.module.methoden.iconunderstandability.domain.ITMTestResult;
import com.userweave.module.methoden.iconunderstandability.domain.IconTermMatchingConfigurationEntity;
import com.userweave.module.methoden.iconunderstandability.page.survey.IconTermMapping;
import com.userweave.utils.LocalizationUtils;

public class IconMatchingReportHelperImpl implements IconMatchingReportHelper {
	
	private final List<ITMTestResult> results;
	
	private ObjectCategorization iconTermCategorization = null; 
	public IconMatchingReportHelperImpl(List<ITMTestResult> results) {
		this.results = results;
	}
	
	public IconMatchingReportHelperImpl(IconTermMatchingConfigurationEntity configuration) {
		this.results = configuration.getResults();
	}

	public int getMaxNumberOfCategorizations() {
		return results != null ? results.size() : 0;
	}
	
	
	public int getMissingValuesForTerm(String term) {
		return getMaxNumberOfCategorizations() - getIconTermCategorization().getObjectCountForCategory(term);
	}
	
	
	public ObjectCategorization  getIconTermCategorization() {
		
		if (iconTermCategorization == null) {
			iconTermCategorization = new ObjectCategorization();
		
			if (results != null) {
				for (ITMTestResult testResult : results) {
					for (IconTermMapping iconTermMapping : testResult.getIconTermMappings()) {
						if (iconTermMapping.hasTermAndIcon()) {
							iconTermCategorization.addObjectForCategory(LocalizationUtils.getValue(iconTermMapping.getTerm()), Integer.toString(iconTermMapping.getIcon().getId()));
						}
					}
				}
			}
		}
		return iconTermCategorization;
	}


	public ObjectCategorization getTermIconCategorization() {
		final ObjectCategorization termIconCategorization = new ObjectCategorization();

		if (results != null) {
			for (ITMTestResult testResult : results) {
				for (IconTermMapping iconTermMapping : testResult.getIconTermMappings()) {
					if (iconTermMapping.hasTermAndIcon()) {
						termIconCategorization.addObjectForCategory(Integer.toString(iconTermMapping.getIcon().getId()), LocalizationUtils.getValue(iconTermMapping.getTerm()));
					}
				}
			}
		}
		
		return termIconCategorization;
	}
	
	
	public double getExecutionTimeMeanForTerm(String term) {
		return term.length();
	}
}
