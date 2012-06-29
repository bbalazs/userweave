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
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;


@SuppressWarnings("serial")
public class BarExplanationPanelAsymmetric extends BarExplanationPanel {
	
	public BarExplanationPanelAsymmetric(String id, IModel title, IModel explanation, double percentage, int rating) {
		super(id, title, explanation, rating);	
				
		int height = new Double(percentage).intValue();
		int maxHeight = 100;
		int margin_top  = maxHeight - height;
		
		String style = "margin-top:" + String.valueOf(margin_top) +  "px; height:" + String.valueOf(height) + "px;";
		add(
			new WebMarkupContainer("bar")
				.add(new AttributeModifier("style", true, new Model(style)))
		);
	}
}
