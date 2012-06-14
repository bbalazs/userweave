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
package com.userweave.pages.user;

import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.StringResourceModel;

/**
 * 
 * @author opr
 *
 */
@SuppressWarnings("serial")
public class MessageRepeater extends Panel {

	private RepeatingView rv;
	
	public MessageRepeater(String id, List<StringResourceModel> list) {
		super(id);
		init(list);
	}
	
	private void init(List<StringResourceModel> list) {
		add(rv = new RepeatingView("message_repeater"));
		
		for(StringResourceModel message : list)	{
            WebMarkupContainer parent = new WebMarkupContainer(rv.newChildId());

            rv.add(parent);
            
            parent.add(new Label("message_text", message));
		}
	}
	
}
