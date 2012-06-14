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
package com.userweave.pages.components.twoColumnPanel.multiColumnRadioChoicePanel;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import com.userweave.pages.components.twoColumnPanel.TwoColumnPanel;

/**
 * @author oma
 */
@SuppressWarnings("serial")
public class TwoColumnRadioChoicePanel extends TwoColumnPanel {

	public TwoColumnRadioChoicePanel(String id, PropertyModel propertyModel, List possibleAnswers, IChoiceRenderer renderer) {
		super(id, propertyModel, possibleAnswers, renderer);
	}

	@Override
	protected MarkupContainer createGroup(String id, PropertyModel propertyModel) {		
		return new RadioGroup(id, propertyModel);
	}

	@Override
	protected Component createColumn(String id, IModel model, int idx, IChoiceRenderer renderer) {
		return new RadioChoiceColumnPanel(id, model, renderer);		
	}
	
	@Override
	protected Component createEmptyColumn(String id) {
		return new EmptyRadioChoiceColumnPanel(id);
	}
}

