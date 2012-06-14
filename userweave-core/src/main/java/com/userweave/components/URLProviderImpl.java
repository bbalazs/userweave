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
package com.userweave.components;

import org.apache.wicket.Component;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.userweave.domain.service.URLProvider;
import com.userweave.pages.login.SigninPage;

public class URLProviderImpl implements URLProvider {
	private final Component component;
	
	public URLProviderImpl(Component component) {
		this.component = component;
	}

	@Override
	public String toUrl(String token) {
			PageParameters parameters = new PageParameters();
			parameters.set(0, token);

			return createUrl(parameters);
	}

	private String createUrl(PageParameters parameters)
	{
		return RequestCycle.get().getUrlRenderer().renderFullUrl(
				   Url.parse(component.urlFor(SigninPage.class, parameters).toString()));

	}
	
}
