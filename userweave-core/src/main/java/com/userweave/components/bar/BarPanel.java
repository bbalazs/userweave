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
package com.userweave.components.bar;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * @author oma
 */
@SuppressWarnings("serial")
public class BarPanel extends Panel {

	public BarPanel(String id, int max, int value, String cssClassName) {
		super(id);
		
		int percentage = 100 * value/max;
		
		add(new Label("value", Integer.toString(value)));
		
		Component bar = new WebMarkupContainer("bar")
			.add(new SimpleAttributeModifier("style", "width: " + Integer.toString(percentage) + "%;"));
		
		if (cssClassName != null) {
			bar.add(new SimpleAttributeModifier("class", cssClassName));
		}
		
		add(bar);
	}

}

