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

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

import com.userweave.domain.User;

/**
 * The header for each configuration page.
 * 
 * @author opr
 * 
 * TODO: Remove unnecessary sources from /res
 */
@Deprecated
public class Header extends Panel 
{
	public Header(String id, User user)
	{
		super(id);
		
		init(user);
	}
	
	private void init(User user)
	{
		IModel userName;
		
		if(user != null && 
				(user.getForename() != null || user.getSurname() != null)) 
		{
			userName = new Model(user.getForename() + " " + user.getSurname());
		} 
		else 
		{
			userName = new StringResourceModel("guest", this, null);
		}
		
//		add(new BookmarkablePageLink("home", getApplication().getHomePage()));
		
		add(new UserMenuPanel("userMenuPanel", userName));
	}
}
