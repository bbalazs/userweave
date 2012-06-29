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
package com.userweave.pages.login;


import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.userweave.application.UserWeaveSession;

/**
 * @author oma
 */
@SuppressWarnings("serial")
public class SignoutPanel extends Panel {

	public SignoutPanel(String id) {
		super(id);

		PageParameters parameters = new PageParameters();
		
		add(new BookmarkablePageLink("signout", SignoutPage.class, parameters) {
			@Override
			public boolean isVisible() {
				return UserWeaveSession.get().isAuthenticated();
			}
		});
	}
}
