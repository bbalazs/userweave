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

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

/**
 * @author oma
 */
@SuppressWarnings("serial")
public abstract class BarListExplanationPanel extends Panel {

	private RepeatingView barListExplanations;

	public BarListExplanationPanel(String id, double totalRating) {
		super(id);
		
		add(barListExplanations = new RepeatingView("barExplanations"));
		add(new Label("totalRating", Double.toString(totalRating)));
	}

	protected RepeatingView getExplanationsRepeater() {
		return barListExplanations;
	}
	
	protected IModel getMessage(String key) {	
		return new StringResourceModel(key, this, null);
	}
}

