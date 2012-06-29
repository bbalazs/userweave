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

import org.apache.wicket.model.StringResourceModel;

import com.userweave.module.methoden.iconunderstandability.domain.report.IconReport;

@SuppressWarnings("serial")
public class IconBarListExplanationPanel extends BarListExplanationPanel {

	public IconBarListExplanationPanel(String id, IconReport iconReport) {
		super(id, iconReport.getTotalRating());

		getExplanationsRepeater().add(
			new BarExplanationPanelAsymmetric(
				getExplanationsRepeater().newChildId(), 
				new StringResourceModel("assoziationsstaerke", this, null), 
				getMessage("exp1"), 
				iconReport.getAssignment(), 
				iconReport.getAssignmentRating()
			)
		);
		
		getExplanationsRepeater().add(
			new BarExplanationPanelAsymmetric(
				getExplanationsRepeater().newChildId(), 
				new StringResourceModel("trenn", this, null), 
				getMessage("exp2"), 
				// BugFix 308: show same percent value like in BarPanel
				iconReport.getHighestAssignmentToOtherTermRatingInPercent(),
				iconReport.getHighestAssignmentToOtherTermRating()
			)
		);
		
		getExplanationsRepeater().add(
			new BarExplanationPanelSymmetric(
				getExplanationsRepeater().newChildId(), 
				new StringResourceModel("auffaelligkeit", this, null), 
				getMessage("exp3"), 
				new Double(iconReport.getReactionTime()).intValue(), 
				iconReport.getReactionTimeRating()
			)
		);
	}

}
