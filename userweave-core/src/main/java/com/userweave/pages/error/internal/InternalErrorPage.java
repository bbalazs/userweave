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
package com.userweave.pages.error.internal;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.StringResourceModel;

import com.userweave.application.UserWeaveSession;
import com.userweave.components.SerializedPageLink;
import com.userweave.pages.base.BaseUserWeavePage;

/**
 * @author oma
 */
public class InternalErrorPage extends BaseUserWeavePage {
	public InternalErrorPage() {
		
		SerializedPageLink pageLink = UserWeaveSession.get().getSessionOrigin();
		addPageLink("link", pageLink);
		addErrorMessages(pageLink);
	}
	
	private void addErrorMessages(SerializedPageLink pageLink) {
		boolean surveyError = pageLink != null && pageLink.getType() == SerializedPageLink.Type.SURVEY;
		if(surveyError) {
			add(new Label("error", new StringResourceModel("errorSurvey",this, null)).setEscapeModelStrings(false));
		} else {
			add(new Label("error", new StringResourceModel("error",this, null)).setEscapeModelStrings(false));
		}
	}

	private void addPageLink(String id, SerializedPageLink pageLink) {
		if(pageLink == null) {
			add(homePageLink(id));
		} else {
			add(pageLink.createPageLink(id));
		}
	}

	@Override
	public boolean isErrorPage() {
		return true;
	}
}

