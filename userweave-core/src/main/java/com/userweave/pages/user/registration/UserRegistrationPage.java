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
package com.userweave.pages.user.registration;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.request.resource.PackageResourceReference;

import com.userweave.domain.User;
import com.userweave.pages.base.BaseUserWeavePage;

/**
 * @author oma
 */
public class UserRegistrationPage extends BaseUserWeavePage 
{
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("serial")
	public UserRegistrationPage(User user) {
		add(new UserRegistrationPanel("userEdit", user) {

				@Override
				public void onSave(User user, AjaxRequestTarget target) {
					setResponsePage(getApplication().getHomePage());
				}
			}
		);
	}
	
	@Override
	public void renderHead(IHeaderResponse response)
	{
		response.renderCSSReference(new PackageResourceReference(
				BaseUserWeavePage.class, "signin.css"));
	}
}

