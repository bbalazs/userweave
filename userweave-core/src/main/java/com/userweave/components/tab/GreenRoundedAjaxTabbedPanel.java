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
package com.userweave.components.tab;

import java.util.List;

import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.request.resource.PackageResourceReference;

/**
 * CommonRoundedAjaxTabbedPanel overrides AjaxTabbedPanel of wicket only
 * to add additional design, see CommonRoundedAjaxTabbedPanel.html
 * 
 * @author fpavkovic
 */
@SuppressWarnings("serial")
@Deprecated
public class GreenRoundedAjaxTabbedPanel extends AjaxTabbedPanel {

	/**
	 * @param id
	 * @param tabs
	 */
	public GreenRoundedAjaxTabbedPanel(String id, List tabs) {
		super(id, tabs);
	}
	
	@Override
	public void renderHead(IHeaderResponse response)
	{
		response.renderCSSReference(new PackageResourceReference(
				GrayRoundedAjaxTabbedPanel.class, "tabbedPanel.css"));
	}
}
