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
package com.userweave.pages.user;

import java.util.ArrayList;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.StringResourceModel;

import com.userweave.pages.homepage.MainContent;

/**
 * 
 * @author opr
 *
 */
@SuppressWarnings("serial")
public class RegisterFinishedPanel extends Panel {

	public RegisterFinishedPanel(String id)	{
		super(id);
		
		init();
		
		add(new BookmarkablePageLink("signin", MainContent.class).add(new Label("start_page",
				new StringResourceModel("start_page", this, null))));
		
		setOutputMarkupId(true);
	}
	
	protected void init() {
		
		ArrayList<StringResourceModel> list = new ArrayList<StringResourceModel>(2);
		
		list.add(new StringResourceModel(
				"email_send", this, null));
		list.add(new StringResourceModel(
				"email_not_received", this, null));
		
		add(new MessageRepeater("init_messages", list));
	}
}
