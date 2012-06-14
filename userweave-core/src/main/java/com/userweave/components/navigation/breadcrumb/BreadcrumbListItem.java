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
package com.userweave.components.navigation.breadcrumb;

import java.io.Serializable;

import com.userweave.domain.EntityBase;

/**
 * Container class to hold an entity and
 * the name of the entity.
 * 
 * @author opr
 */
public class BreadcrumbListItem implements Serializable
{
	private static final long serialVersionUID = 1L;

	private final String name;
	
	private final EntityBase entity;
	
	public String getName() 
	{
		return name;
	}

	public EntityBase getEntity() 
	{
		return entity;
	}
	
	public BreadcrumbListItem(String name, EntityBase entity)
	{
		this.name = name;
		
		this.entity = entity;
	}
}
