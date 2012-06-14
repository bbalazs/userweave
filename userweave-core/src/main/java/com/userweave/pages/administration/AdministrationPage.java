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
package com.userweave.pages.administration;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.userweave.components.header.UserMenuPanel;
import com.userweave.pages.base.BaseUserWeavePage;
import com.userweave.pages.components.ajaxindicator.AjaxIndicator;

/**
 * Web age to administer an user account.
 * 
 * @author opr
 *
 */
public class AdministrationPage extends BaseUserWeavePage 
{
	private static final long serialVersionUID = 1L;

	public AdministrationPage()
	{
		add(new SimpleAdministrationPanel("administrationPanel"));
		
		add(new AjaxIndicator("ajaxIndicator"));
	}
	
	@Override
	protected Component getUserMenuPanel(String id, IModel<String> userModel)
	{
		return new UserMenuPanel(id, userModel)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected IModel<String> getAdminLinkCssModel()
			{
				return new Model<String>("accountPageActive");
			}
		};
	}
}
