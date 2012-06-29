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

import java.io.Serializable;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class SerializedPageLink implements Serializable 
{
	private static final long serialVersionUID = 1L;

	public static enum Type  { REPORT, SURVEY };
	
	private final Class<? extends Page> page;

	private final PageParameters parameters;
	
	private final Type type;
	
	public SerializedPageLink(Class<? extends Page> page, PageParameters parameters, Type type) 
	{
		this.page = page;
		this.parameters = parameters;
		this.type = type;
	};
	
	public BookmarkablePageLink createPageLink(String id) 
	{
		return new BookmarkablePageLink(id, page, new PageParameters(parameters));
	}
	
	public Type getType() {
		return type;
	}
	
	@Override
	public String toString() {
		return "SerializedPageLink{"+page.getSimpleName()+","+parameters+"}";
	}
	
}
