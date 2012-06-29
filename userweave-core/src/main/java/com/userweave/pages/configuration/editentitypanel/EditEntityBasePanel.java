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
package com.userweave.pages.configuration.editentitypanel;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.userweave.domain.EntityBase;

/**
 * Base class for entity specific functions.
 * (like copy, edit, ...). This class only
 * represents a label. Extend for further usage.
 * 
 * @author opr
 *
 */
@Deprecated
public class EditEntityBasePanel extends Panel
{
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 * 
	 * @param id
	 * 		Component id.
	 */
	public EditEntityBasePanel(String id, EntityBase entity)
	{
		super(id);
		
		add(new Label("entityName", getEntityNameModel(entity)));
	}
	
	/**
	 * Returns the model for the display label.
	 * (Return entity name here in subclasses).
	 * 
	 * @return
	 * 		Model for the entityName label.
	 */
	protected IModel getEntityNameModel(EntityBase entity)
	{
		return new Model("Start");
	}
}
