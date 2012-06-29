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
package com.userweave.pages.homepage;

import javax.servlet.http.Cookie;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.request.resource.PackageResourceReference;

import com.userweave.pages.base.BaseUserWeavePage;

public class IntroPage extends WebPage {
	
	public static String COOKIE_NAME = "UserWeaveSkipIntro";
	public static String SKIP_TRUE_VALUE = "true";
	public static String SKIP_FALSE_VALUE = "false";
	
	
	public IntroPage() 
	{
		add(new IntroPanel("introPanel")
		{
			@Override
			protected void onOk(AjaxRequestTarget target) 
			{
				((WebResponse)getRequestCycle().getResponse()).addCookie(new Cookie(COOKIE_NAME, SKIP_TRUE_VALUE));	
				setResponsePage( getApplication().getHomePage() );
			}

			@Override
			protected void onCancel(AjaxRequestTarget target) {
				((WebResponse)getRequestCycle().getResponse()).addCookie(new Cookie(COOKIE_NAME, SKIP_FALSE_VALUE));
				CharSequence redirectPage = "http://survey.userweave.net/beta/survey/8b4889f9b91e4a56afd5000c96883c4c/";
			    setResponsePage(new RedirectPage(redirectPage));
			}});
	}
	
	@Override
	public void renderHead(IHeaderResponse response)
	{
		response.renderCSSReference(new PackageResourceReference(
				BaseUserWeavePage.class, "configBase.css"));
		
		response.renderCSSReference(new PackageResourceReference(
				BaseHomepage.class, "BaseHomepage.css"));
	}

}
