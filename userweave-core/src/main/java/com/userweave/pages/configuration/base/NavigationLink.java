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
package com.userweave.pages.configuration.base;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import com.userweave.components.navigation.NavigationBorder;
import com.userweave.components.navigation.RoundedBorderGray;

/**
 * @author oma
 */
@SuppressWarnings("serial")
public abstract class NavigationLink extends Panel {

	public NavigationLink(String id, IModel header, IModel name) {
		super(id);			
		add(
			new RoundedBorderGray("roundedBorderModule").add(
				new NavigationBorder("navigationBorder", header, NavigationBorder.Color.Gray).add(
					new AjaxLink("link") {
		
						@Override
						public void onClick(AjaxRequestTarget target) {
							NavigationLink.this.onClick(target);					
						}
						
					}.add(
						new Label("name", shortenModelString(name))
					).add(
						new AttributeModifier("title", true, name)
					)
				)
			)
		);
	}
	
	protected abstract void onClick(AjaxRequestTarget target);
	
	private String shortenModelString(IModel name)
	{
		String shortenTitle = "";
		
		String title = (String)name.getObject();
		
		String[] titleTokens = title.split(" ");
		
		for (String curToken : titleTokens) {
			
			if ( curToken.length() > 20 ) {
				shortenTitle += curToken.substring(0, 18) + "... ";
			}
			else {
				shortenTitle += curToken + " ";
			}
		}
		
		return shortenTitle;
	}
}

