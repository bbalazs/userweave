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
package com.userweave.pages.components.twoColumnPanel.multiColumnCheckboxMultipleChoice;

import java.io.Serializable;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.userweave.pages.components.twoColumnPanel.multiColumnRadioChoicePanel.RadioChoiceColumnPanel;

/**
 * @author oma
 */
public class CheckBoxColumn extends Panel 
{
	private static final long serialVersionUID = 1L;

	public CheckBoxColumn(String id, final IModel model, IChoiceRenderer renderer) {
		super(id);   
		 
		Check check = new Check("check", new Model<Serializable>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Serializable getObject()
			{
				return (Serializable)model.getObject();
			}
		});
		
		check.setOutputMarkupId(true);
		
		add(check);
		
		Label label = new Label("label", RadioChoiceColumnPanel.getLabel(this, model, renderer));
		
		label.add(new AttributeModifier("for", true, new Model(check.getMarkupId())));
		
		add(label);		
	}	
}

