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
package com.userweave.components.header;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import com.userweave.application.UserWeaveSession;
import com.userweave.pages.administration.AdministrationPage;
import com.userweave.pages.login.SignoutPage;

/**
 * Displays the name of the user and the logout link
 * as well as a "my account" link in the header.
 * 
 * @author opr
 *
 */
public class UserMenuPanel extends Panel 
{
	private static final long serialVersionUID = 1L;

	public UserMenuPanel(String id, IModel model) 
	{
		super(id, model);
		
		init();
	}
	
	private void init()
	{
		//add(new Label("username", getModel()));
		
		add(createAdministrationLink("myaccount"));
		
		add(createSignoutLink("logout"));
	}

	private BookmarkablePageLink createSignoutLink(String linkId) 
	{
		return new BookmarkablePageLink(linkId, SignoutPage.class) 
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible() 
			{
				return UserWeaveSession.get().isAuthenticated();
			}
		};
	}
	
	private BookmarkablePageLink createAdministrationLink(String linkId) 
	{
		BookmarkablePageLink link = 
			new BookmarkablePageLink(linkId, AdministrationPage.class) 
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible() 
			{
				return UserWeaveSession.get().isAuthenticated();
			}
		};
		
		IModel attributeModifier = getAdminLinkCssModel();
		
		if(attributeModifier != null)
		{
			link.add(new AttributeAppender("class", true, attributeModifier, " "));
		}
		
		link.add(new Label("username", getDefaultModel()));
		
		return link;
	}
	
	protected IModel getAdminLinkCssModel()
	{
		return null;
	}
}
