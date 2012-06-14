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
package com.userweave.pages.test.jquery;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.protocol.http.request.WebClientInfo;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.PackageResourceReference;

import com.userweave.pages.test.BasePageSurvey;

/**
 * @author opr
 */
public class JQuery 
{
	/**
	 * Dummy Class to get to the .js files in this
	 * package.
	 * Use the HeaderContibutor with this class, to
	 * get jquery in your page
	 */
	public static PackageResourceReference getLatest()
	{
		// use non minified version here, because the minified version 
		// breaks the navigation with a 'not terminated string literal' error.
		return new PackageResourceReference(JQuery.class, "jquery-1.6.4.js");
	}

	/**
	 * Add draggable/dropable/sortable support to survey ui components.
	 * 
	 * @param response
	 * 		IHeaderResonse.
	 */
	public static void addSurveyUiJavaScript(Page page, IHeaderResponse response)
	{
		response.renderJavaScriptReference(
			new JavaScriptResourceReference(JQuery.class, "jquery.min.js"), 
			null);
		
		response.renderJavaScriptReference(
				new JavaScriptResourceReference(JQuery.class, "jquery-ui-1.8.16.custom.min.js"), 
				null);
		
		response.renderJavaScriptReference(
				new JavaScriptResourceReference(BasePageSurvey.class, "init.js"));
			
		addKonquerorHacks(page, response);
			
		addIe6PngFix(page, response);
	}
	
	public static boolean addKonquerorHacks(final Page page, IHeaderResponse response) 
	{
		if(page.getSession().getClientInfo() instanceof WebClientInfo && 
			((WebClientInfo)page.getSession().getClientInfo()).getProperties().isBrowserKonqueror()) 
		{
			page.add(new Behavior()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void renderHead(Component component, IHeaderResponse response)
				{
					response.renderCSSReference(new PackageResourceReference(
							BasePageSurvey.class, "konquerorhacks.css"));
					
					if(page.getApplication().getConfigurationType().equals(RuntimeConfigurationType.DEVELOPMENT)) 
					{
						response.renderJavaScriptReference(
							"http://getfirebug.com/releases/lite/1.2/firebug-lite-compressed.js");
					}
				}
			});
			
			return true;
		}
		
		return false;
	}
	
	public static void addIe6PngFix(Page page, IHeaderResponse response)
	{
		if(isIe6(page))
		{
			response.renderJavaScriptReference(
					new JavaScriptResourceReference(JQuery.class, "supersleight.plugin.js"));
			
			String script = "$(document).ready(function(){" +
		    					"$('body').supersleight({" +
		    					"shim: '../wicket/resource/com.userweave.pages.test.jquery.JQuery/x.gif'});});";
			
			response.renderJavaScript(script, null);
		}
	}
	
	public static boolean isIe6(Page page)
	{
		return page.getSession().getClientInfo() instanceof WebClientInfo &&
		   ((WebClientInfo)page.getSession().getClientInfo()).getProperties().isBrowserInternetExplorer() &&
		   ((WebClientInfo)page.getSession().getClientInfo()).getProperties().getBrowserVersionMajor() == 6;
	}
}
