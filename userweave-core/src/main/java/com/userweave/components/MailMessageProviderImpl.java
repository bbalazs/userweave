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

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.RequestUtils;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.userweave.domain.service.MailMessageProvider;
import com.userweave.pages.login.SigninPage;
import com.userweave.utils.LocalizationUtils;

public abstract class MailMessageProviderImpl implements MailMessageProvider {
	private final Component component;
	
	private Locale origLocale;
	
	public MailMessageProviderImpl(Component component) {
		this.component = component;
		
	}

	@Override
	public final String toUrl(String token) 
	{
		String url = getUrl(component, token, Session.get().getLocale());
		// remove prefix "../"
		while(url.contains("../")) 
		{
			url = url.replaceFirst("\\.\\./", "");
		}
		
		return url;
		
		//return toAbsolutePath(url);		
	}
	
	public String getUrl(Component component, String token, Locale locale) {
		PageParameters parameters = new PageParameters();
		parameters.set(0, token);
		parameters.set(1, LocalizationUtils.getLocaleShort(locale)); 
		
		//return component.urlFor(SigninPage.class, parameters).toString();
		
		return RequestCycle.get().getUrlRenderer().renderFullUrl(
				   Url.parse(component.urlFor(SigninPage.class,parameters).toString()));
	}

	public void onAttach(Locale locale) {
		Locale sessionLocale = Session.get().getLocale();
		if(locale != null) {
			Session.get().setLocale(locale);
			if(sessionLocale != null && !sessionLocale.equals(locale)) {
				origLocale = sessionLocale;
			}
		}
	}

	public void onDetach() {
		if(origLocale != null) {
			Session.get().setLocale(origLocale);
			origLocale = null;
		}		
	}
	
	private final static String toAbsolutePath(final String relativePagePath) 
	{
		HttpServletRequest req = (HttpServletRequest)(RequestCycle.get().getRequest()).getContainerRequest();
	    
		return RequestUtils.toAbsolutePath(req.getRequestURL().toString(), relativePagePath);
	}
}
