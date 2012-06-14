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

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

@SuppressWarnings("serial")
public class BarExplanationPanelSymmetric extends BarExplanationPanel {
	
	public BarExplanationPanelSymmetric(String id, IModel title, IModel explanation, int percentage, int rating) {
		super(id, title, explanation, rating);
		
		String cssClass = "upper_bound_bar";
		
		// Kompensation der falschen Darstellung bei 0 
		if (percentage == 0) {
			percentage = -1;			
		}
		
		if (percentage < 0) {
			percentage *= -1;
			cssClass = "lower_bound_bar";
		}
		
		String percentageString = Integer.toString(percentage);
		
		add(new Label("value", new Model(percentageString)));
		add(new Label("negativeValue", new Model("-" + percentageString)));
	
		add(
			new WebMarkupContainer("bar")
			.add(new AttributeModifier("class", true, new Model(cssClass)))
		);
	}

}
