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

import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.model.StringResourceModel;

import com.userweave.domain.Role;

/**
 * Authorized variant of an add link.
 * 
 * @author opr
 */
@AuthorizeAction(action = Action.RENDER, roles = {Role.PROJECT_ADMIN, Role.PROJECT_PARTICIPANT})
public abstract class AuthOnlyAddLink extends AddLink
{
	private static final long serialVersionUID = 1L;

	public AuthOnlyAddLink(String id, StringResourceModel labelModel)
	{
		super(id, labelModel);
	}
	
	public AuthOnlyAddLink(String id, String labelTitle)
	{
		super(id, labelTitle);
	}
}
