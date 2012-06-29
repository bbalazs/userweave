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
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;

import com.userweave.application.UserWeaveSession;
import com.userweave.pages.user.configuration.UserEditPersonalPanel;
import com.userweave.pages.user.overview.UserOverviewPanel;
import com.userweave.presentation.model.UserModel;

public class SimpleAdministrationPanel extends Panel
{
	private static final long serialVersionUID = 1L;
	
	private final static String CONTENT_ID = "content";
	
	private Component content;
	
	public SimpleAdministrationPanel(String id)
	{
		super(id);
		
		replaceContentWithUserEdit(
				UserWeaveSession.get().getUser().getId(), false, null);
		
		add(new UserOverviewPanel("userPanel")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onEditUser(AjaxRequestTarget target, Integer userId)
			{
				replaceContentWithUserEdit(userId, true, target);
			}
			
			@Override
			public boolean isVisible()
			{
				return UserWeaveSession.get().isAdmin();
			}
		});
	}
	
	private void replaceContentWithUserEdit(
			Integer userId, final boolean toOverview, AjaxRequestTarget target) 
	{
		replaceContent(
			new UserEditPersonalPanel(CONTENT_ID, new UserModel(userId)),
			target
		);
	}
	
	private void replaceContent(Component replacement, AjaxRequestTarget target) 
	{
		replacement.setOutputMarkupId(true);
		
		if(content == null) 
		{
			add(replacement);
		}
		else
		{
			content.replaceWith(replacement);
		}
		content = replacement;
		
		if (target != null) 
		{
			target.add(content);
		}
		
	}
}
