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
package com.userweave.pages.homepage.quotepanel;

import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.resource.PackageResourceReference;

/**
 * Panel to display a quote.
 * 
 * @author opr
 */
public class QuotePanel extends Panel 
{
	private static final long serialVersionUID = 1L;
	
	public QuotePanel(String id)
	{
		super(id);
		
		add(new QuoteDisplayPanel("quote", QuoteDisplayPanel.ELO));
	}
	
	@Override
	public void renderHead(IHeaderResponse response)
	{
		response.renderCSSReference(new PackageResourceReference(
				QuotePanel.class, "quotePanel.css"));
	}
}
