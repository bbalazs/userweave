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
package com.userweave.components.navigation;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.protocol.http.request.WebClientInfo;
import org.apache.wicket.request.resource.PackageResourceReference;

/**
 * @author oma
 */
@SuppressWarnings("serial")
public class NavigationBorder extends Border {
	
	public enum Color {
		Gray("#A2A2A2"), Green("#99B8BB");
		
		public final String hexcode;
		
		private Color(String hexcode) {
			this.hexcode = hexcode;
		}
	};

	public NavigationBorder(String id, IModel header, Color color) 
	{
		super(id);
		
		WebMarkupContainer col0 = new WebMarkupContainer("col0");
		WebMarkupContainer col1 = new WebMarkupContainer("col1");
		WebMarkupContainer col2 = new WebMarkupContainer("col2");
		WebMarkupContainer col3 = new WebMarkupContainer("col3");
		
		add(col0);
		add(col1);
		add(col2);
		add(col3);
		
		String openingIconName = color.equals(Color.Gray) ? 
				"header_line_rounded_left_part.png" : 
				"header_line_rounded_left_part_green.png";
		String iconName 	   = color.equals(Color.Gray) ? "arrow_gray.jpg" : "arrow_green.jpg";
		String closingIconName = color.equals(Color.Gray) ? 
				"header_line_rounded_right_part.png":
			    "header_line_rounded_right_part_green.png";
		
		col0.add(new Image("openingImage", new PackageResourceReference(NavigationBorder.class, openingIconName)));
		
		col1.add(new SimpleAttributeModifier("style", "background-color:" + color.hexcode));
		col1.add(new Image("image", new PackageResourceReference(NavigationBorder.class, iconName)));
		
		col2.add(new SimpleAttributeModifier("style", "background-color:" + color.hexcode));
		col2.add(new Label("header", header));
		
		col3.add(new Image("closingImage", new PackageResourceReference(NavigationBorder.class, closingIconName)));
	}

	@Override
	public void renderHead(IHeaderResponse response)
	{
		/**
		 * @bugfix: Conditinal Comments wont work, so add
		 * 			IE7 fix by HeaderContributor
		 * @author: opr
		 */
		WebClientInfo clientInfo = WebSession.get().getClientInfo();
		
		if(clientInfo.getUserAgent().contains("MSIE 7.0"))
		{
			response.renderCSSReference(new PackageResourceReference(
					NavigationBorder.class, "NavigationBorder_ie7.css"));
		}
	}
}

