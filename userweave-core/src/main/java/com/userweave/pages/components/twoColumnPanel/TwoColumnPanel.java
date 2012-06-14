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
package com.userweave.pages.components.twoColumnPanel;

import java.io.Serializable;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;


/**
 * @author oma
 */
@SuppressWarnings("serial")
public abstract class TwoColumnPanel extends Panel {
	
	private int index;
	
	public TwoColumnPanel(String id, PropertyModel propertyModel, final List possibleAnswers, IChoiceRenderer renderer) {
		super(id);		
		
		MarkupContainer group = createGroup("group", propertyModel);
		add(group);
		
		RepeatingView repeater = new RepeatingView("checkboxes");
		
		final int lastIndex = possibleAnswers.size();
		
		index = 0;
		
		int numberOfColumns = 2;

		WebMarkupContainer row = null;

		while(index < lastIndex) {	
			
			if (index % numberOfColumns == 0) {
				row = new WebMarkupContainer(repeater.newChildId());
				repeater.add(row);
			}
			
			row.add(createColumn("checkbox1", new Model((Serializable) possibleAnswers.get(index)), index, renderer));
			index++;	
			
			if (index != lastIndex) {
				row.add(createColumn("checkbox2", new Model((Serializable) possibleAnswers.get(index)), index, renderer));
			} else {
				row.add(createEmptyColumn("checkbox2"));			
			}									

			index++;

		};

		group.add(repeater);
	}


	protected abstract Component createEmptyColumn(String id);
	
	protected abstract MarkupContainer createGroup(String id, PropertyModel propertyModel);
	
	protected abstract Component createColumn(String id, IModel model, int idx, IChoiceRenderer renderer);
}

