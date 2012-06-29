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
package com.userweave.components.authorization;

import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;

import com.userweave.components.ToolTipAjaxLink;
import com.userweave.domain.Role;

/**
 * Authorized version of an ajax link. It gets <b>enabled</b> only if
 * the authorization strategy says so. Currently it is enabled,
 * if the user is a project contributor, that is, a project admin
 * or a project participant.
 * 
 * @author opr
 *
 */
@AuthorizeAction(
		action = Action.ENABLE, 
		roles = {Role.PROJECT_ADMIN, 
				 Role.PROJECT_PARTICIPANT})
public abstract class AuthOnlyAjaxLink extends ToolTipAjaxLink
{
	private static final long serialVersionUID = 1L;
	
	public AuthOnlyAjaxLink(String id)
	{
		super(id);
	}
	
	public AuthOnlyAjaxLink(String id, ToolTipType type)
	{
		super(id, type);
	}
}
