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
package com.userweave.pages.api;

import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;

import com.userweave.pages.base.BaseUserWeavePage;
import com.userweave.utils.LocalizationUtils;

public class ApiLandingPage extends BaseUserWeavePage {

	public ApiLandingPage()
	{
		add(new BookmarkablePageLink("more_info", getApplication().getHomePage()));
		
		add(new LoginRegisterPanel("loginRegisterPanel"));
		
		add(new Link("switchToGerman"){
			@Override 
			public void onClick() {
				getSession().setLocale(LocalizationUtils.getSupportedConfigurationFrontendLocale("de"));
			}
		});
		
		add(new Link("switchToEnglish"){
			@Override 
			public void onClick() {
				getSession().setLocale(LocalizationUtils.getSupportedConfigurationFrontendLocale("en"));
			}
		});
	}
}
