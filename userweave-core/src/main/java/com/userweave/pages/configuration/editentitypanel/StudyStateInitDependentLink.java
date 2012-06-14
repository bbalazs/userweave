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
/**
 * 
 */
package com.userweave.pages.configuration.editentitypanel;

import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;

import com.userweave.components.authorization.AuthOnlyAjaxLink;
import com.userweave.domain.Role;
import com.userweave.domain.StudyState;
import com.userweave.pages.configuration.EnabledInStudyState;

/**
 * An ajax link enabled only if the study is in
 * init state.
 * 
 * @author opr
 *
 */
@AuthorizeAction(
		action = Action.ENABLE, 
		roles = {Role.PROJECT_ADMIN, 
				 Role.PROJECT_PARTICIPANT})
@EnabledInStudyState(states={StudyState.INIT})
public abstract class StudyStateInitDependentLink extends AuthOnlyAjaxLink
{
	private static final long serialVersionUID = 1L;

	public StudyStateInitDependentLink(String id)
	{
		super(id);
	}
}
