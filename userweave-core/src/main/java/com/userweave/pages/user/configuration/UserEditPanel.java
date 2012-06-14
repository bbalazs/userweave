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
package com.userweave.pages.user.configuration;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.StringResourceModel;

import com.userweave.application.UserWeaveSession;
import com.userweave.components.tab.GrayRoundedAjaxTabbedPanel;
import com.userweave.presentation.model.UserModel;

@SuppressWarnings("serial")
public class UserEditPanel extends Panel {

	public UserEditPanel(String id, Integer userId) {
		this(id, new UserModel(userId));
	}

	public UserEditPanel(String id, UserModel userModel) {
		super(id, userModel);
		
		add(new UserEditHeaderPanel("header", userModel));
		
		add(createTabbedPanel("tabbedPanel", userModel));
	}

	private Component createTabbedPanel(String markupId, final UserModel userModel) {

		List<AbstractTab> tabs = new ArrayList<AbstractTab>();
		
		tabs.add(new AbstractTab(new StringResourceModel("personal", this, null)) {
	        @Override
			public Panel getPanel(String panelId) { 
	        	return new UserEditPersonalPanel(panelId, userModel); 
	        }
		});
		
		tabs.add(new AbstractTab(new StringResourceModel("contact", this, null)) {
	        @Override
			public Panel getPanel(String panelId) { 
	        	return new UserEditContactPanel(panelId, userModel); 
	        }
		});

		tabs.add(new AbstractTab(new StringResourceModel("professional", this, null)) {
	        @Override
			public Panel getPanel(String panelId) { 
	        	return new UserEditProfessionalPanel(panelId, userModel); 
	        }
		});

		tabs.add(new AbstractTab(new StringResourceModel("invoice", this, null)) {
	        @Override
			public Panel getPanel(String panelId) { 
	        	return null; //new UserInvoicePanel(panelId, userModel); 
	        }
		});
		
		if (UserWeaveSession.get().isAdmin() ||
				userModel.load().getApiCredentials() != null ) {
		
			tabs.add(new AbstractTab(new StringResourceModel("apiCredentials", this, null)) {
		        @Override
				public Panel getPanel(String panelId) { 
		        	return new UserApiCredentialsPanel(panelId, userModel); 
		        }
			});	
		}
		

		return new GrayRoundedAjaxTabbedPanel(markupId, tabs);
	}
	
	

}
