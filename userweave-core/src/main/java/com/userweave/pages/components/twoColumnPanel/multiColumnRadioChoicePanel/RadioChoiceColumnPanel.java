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

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * @author oma
 */
public class RadioChoiceColumnPanel extends Panel 
{
	private static final long serialVersionUID = 1L;

	public RadioChoiceColumnPanel(String id, IModel model, IChoiceRenderer renderer) 
	{
		super(id);
		
		Radio radio = new Radio("radio", model);
		
		radio.setOutputMarkupId(true);
		
		add(radio);
		
		Label label = new Label("label", getLabel(this,model,renderer));
		
		label.add(new AttributeModifier("for", new Model<String>(radio.getMarkupId())));
		
		add(label);
	}
	
	public static String getLabel(Component component, IModel model, IChoiceRenderer renderer) 
	{
		Object objectValue = renderer.getDisplayValue(model.getObject());
		Class objectClass = objectValue.getClass();
		String displayValue = "";
		if (objectClass != null && objectClass != String.class)
		{
			displayValue = component.getApplication().getConverterLocator()
				.getConverter(objectClass).convertToString(objectValue, component.getLocale());
				
				//convertToString(objectValue, component.getLocale());
		}
		else if (objectValue != null)
		{
			displayValue = objectValue.toString();
		}
		return displayValue;
	}
	
}

