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

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.StringResourceModel;

@SuppressWarnings("serial")
public class ResetPasswordPanel extends RegisterPanel {

	public ResetPasswordPanel(String id, Form form) {
		super(id, form, false);
	}

	@Override
	protected void initMessages() {
		ArrayList<StringResourceModel> list = new ArrayList<StringResourceModel>(1);
		
		list.add(new StringResourceModel(
				"get_email", this, null));
		
		add(new MessageRepeater("init_messages", list));
	}
	
}
