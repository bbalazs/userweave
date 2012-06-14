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
package com.userweave.module.methoden.iconunderstandability.page.report.bmi;

import com.userweave.module.methoden.iconunderstandability.domain.report.TermReport;

@SuppressWarnings("serial")
public class IndicatorBarListExplanationPanel extends BarListExplanationPanel {

	public IndicatorBarListExplanationPanel(String id, TermReport termReport) {
		super(id, termReport.getIndicator());
	
		double deviationFromMeanProcessingTime = termReport.getDeviationFromMeanProcessingTime();
		if (deviationFromMeanProcessingTime == 0) {
			deviationFromMeanProcessingTime = -1;
		}
		
		getExplanationsRepeater().add(
				new BarExplanationPanelAsymmetric(
					getExplanationsRepeater().newChildId(), 
					getMessage("head2"),
					getMessage("exp2"), 
					new Double(termReport.getMissingValueRatingInPercent()).intValue(), 
					termReport.getMissingValueRating()
				)
			);
		
		getExplanationsRepeater().add(
			new BarExplanationPanelSymmetric(
					getExplanationsRepeater().newChildId(), 
					getMessage("head1"), 
					getMessage("exp1"), 
					new Double(deviationFromMeanProcessingTime).intValue(), 
					termReport.getDeviationFromMeanProcessingTimeRating()
			)
		);
	}

}
