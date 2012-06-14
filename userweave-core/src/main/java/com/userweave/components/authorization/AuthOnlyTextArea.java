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
package com.userweave.components.authorization;

import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.model.IModel;

public class AuthOnlyTextArea extends AuthOnlyTextComponent 
{
	private static final long serialVersionUID = 1L;

	private String textAreaName;
	
	private Integer cols, rows;
	
	public AuthOnlyTextArea(String id, IModel model, Behavior behavior) 
	{
		super(id, model, behavior);
		
		sethasNoValueAttr(true);
	}
	
	public AuthOnlyTextArea(String id, IModel model, Behavior behavior, String textAreaName, int rows, int cols) 
	{
		super(id, model, behavior);
		
		sethasNoValueAttr(true);
		
		this.textAreaName = textAreaName;
		
		this.rows = rows;
		
		this.cols = cols;
	}

	@Override
	protected void onIsAuthorizedComponentTag(ComponentTag tag) 
	{
		tag.setName("textarea");
		
		if(textAreaName != null)
		{
			tag.put("name", textAreaName);
		}
		
		if(cols != null)
		{
			tag.put("cols", cols);
		}
		
		if(rows != null)
		{
			tag.put("rows", rows);
		}

	}

	@Override
	protected void onIsUnauthorizedComponentTag(ComponentTag tag) 
	{
		tag.setName("p");
	}

}
