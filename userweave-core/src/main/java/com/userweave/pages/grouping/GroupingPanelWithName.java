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
package com.userweave.pages.grouping;

import java.util.Locale;

import org.apache.wicket.markup.html.form.TextField;

import com.userweave.domain.Group;
import com.userweave.module.methoden.questionnaire.page.grouping.GroupAddedCallback;


/**
 * @author oma
 */
@SuppressWarnings("serial")
public abstract class GroupingPanelWithName<T extends Group> extends GroupingPanel<T> {

	public GroupingPanelWithName(
		String id, T group, Locale locale, GroupAddedCallback<T> groupAddedCallback) 
	{
		super(id, group, locale, groupAddedCallback);
		
		add(new TextField("name").setRequired(true));		
	}
}

