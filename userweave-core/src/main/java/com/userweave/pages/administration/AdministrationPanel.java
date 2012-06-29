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
import com.userweave.pages.user.configuration.UserEditPanel;
import com.userweave.pages.user.overview.UserOverviewPanel;

@SuppressWarnings("serial")
public class AdministrationPanel extends Panel{
	private final static String CONTENT_ID = "content";
	
	private Component content;
	
	public AdministrationPanel(String id) {
		super(id);
		
		if(UserWeaveSession.get().isAdmin()) {
			replaceContentWithUserOverView(null);
		} else {
			replaceContentWithUserEdit(UserWeaveSession.get().getUser().getId(), false, null);
		}
	}
	
	private void replaceContentWithUserOverView(AjaxRequestTarget target) {
		replaceContent(new UserOverviewPanel(CONTENT_ID) {

			@Override
			public void onEditUser(AjaxRequestTarget target2, Integer userId) {
				replaceContentWithUserEdit(userId, true, target2);
			}
			
		}, target);
	}
	
	private void replaceContentWithUserEdit(Integer userId, final boolean toOverview, AjaxRequestTarget target) {
		replaceContent(new UserEditPanel(CONTENT_ID, userId), target);
	}
	
	private void replaceContent(Component replacement, AjaxRequestTarget target) {
		replacement.setOutputMarkupId(true);
		if(content == null) {
			add(replacement);
		} else {
			content.replaceWith(replacement);
		}
		content = replacement;
		
		if (target != null) {
			//target.addComponent(this.setOutputMarkupId(true));
			target.addComponent(content);
		}
		
	}

}
