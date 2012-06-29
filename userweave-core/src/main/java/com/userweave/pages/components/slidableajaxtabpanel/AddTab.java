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
package com.userweave.pages.components.slidableajaxtabpanel;

import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.userweave.domain.Role;

/**
 * Wrapper class to display an icon instead of a
 * string title.
 * 
 * @author opr
 *
 */
@AuthorizeAction(action = Action.RENDER, 
				 roles = Role.PROJECT_ADMIN)
public abstract class AddTab extends AbstractTab
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Define a dummy title for the tab.
	 */
	private static IModel<String> emptyTitle = new Model<String>("addIcon");
	
	public AddTab()
	{
		super(emptyTitle);
	}
}
