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
package com.userweave.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class Role extends EntityBase 
{
	public static final String PROJECT_ADMIN = "PROJECT_ADMIN";
	
	public static final String PROJECT_PARTICIPANT = "PROJECT_PARTICIPANT";
	
	public static final String PROJECT_GUEST = "PROJECT_GUEST";
	
	private String roleName;

	public String getRoleName()
	{
		return roleName;
	}

	public void setRoleName(String roleName)
	{
		this.roleName = roleName;
	}
	
	private List<ProjectUserRoleJoin> projectUserRoleJoins;

	@OneToMany(mappedBy="role")
	public List<ProjectUserRoleJoin> getProjectUserRoleJoins()
	{
		return projectUserRoleJoins;
	}

	public void setProjectUserRoleJoins(
			List<ProjectUserRoleJoin> projectUserRoleJoins)
	{
		this.projectUserRoleJoins = projectUserRoleJoins;
	}
}
