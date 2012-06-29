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

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableLabel;
import org.apache.wicket.model.IModel;

public class AuthOnlyAjaxEditableLabel extends AjaxEditableLabel implements
		IAuthOnly {

	private static final long serialVersionUID = 1L;
	
	private boolean isAuthorized = false;
	
	public AuthOnlyAjaxEditableLabel(String id)
	{
			super(id);
	}
	
	public AuthOnlyAjaxEditableLabel(String id, IModel model) 
	{
		super(id, model);
	}

	@Override
	public void setIsAuthorized(boolean isAuthorized) 
	{
		this.isAuthorized = isAuthorized;
	}

	@Override
	public void onEdit(AjaxRequestTarget target)
	{
		if(isAuthorized)
		{
			super.onEdit(target);
		}
	}
	
}
