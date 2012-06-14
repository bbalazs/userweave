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
package com.userweave.pages.components.button;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.StringResourceModel;

/**
 * Simple class to show a link with specific markup.
 * 
 * @author opr
 *
 */
public abstract class AddLink extends Panel
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constants for displaying a predefined label.
	 */
	public static final String ADD_METHOD 	 = "add_method";
	public static final String ADD_QUESTION  = "add_question";
	public static final String ADD_TERM 	 = "add_term";
	public static final String ADD_ANSWER	 = "add_answer";
	public static final String ADD_DIMENSION = "add_dimension";
	
	/**
	 * Default constructor.
	 * 
	 * @param id
	 * 		Component id.
	 * @param labelModel
	 * 		String to display.
	 */
	public AddLink(String id, StringResourceModel labelModel)
	{
		super(id);
		
		initLink(labelModel);
	}
	
	/**
	 * Constructor for links where the label title
	 * is one of the constants defined in this class. 
	 * 
	 * @param id
	 * 		Component id.
	 * @param labelTitle
	 * 		One of this class constants.
	 */
	public AddLink(String id, String labelTitle)
	{
		super(id);
		
		initLink(new StringResourceModel(labelTitle, this, null));
	}
	
	/**
	 * Initializes this panel with a link.
	 * 
	 * @param labelModel
	 * 		StringResourceModel for the label.
	 */
	private void initLink(StringResourceModel labelModel)
	{
		AjaxLink link = new AjaxLink("add")
		{
			@Override
			public void onClick(AjaxRequestTarget target)
			{
				AddLink.this.onClick(target);
			}			
		};
		
		add(link);
		
		link.add(new Label("message", labelModel));
	}
	
	/**
	 * Callback for click events.
	 * 
	 * @param target
	 * 		AjaxRequestTarget.
	 */
	protected abstract void onClick(AjaxRequestTarget target);
}
