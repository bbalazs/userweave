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

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class ProjectInvitation extends EntityBase
{
	private User addressee;
	
	@ManyToOne
	public User getAddressee()
	{
		return addressee;
	}

	public void setAddressee(User addressee)
	{
		this.addressee = addressee;
	}
	
	private User addresser;
	
	@ManyToOne
	public User getAddresser()
	{
		return addresser;
	}

	public void setAddresser(User addresser)
	{
		this.addresser = addresser;
	}


	private Project project;
	
	@ManyToOne
	public Project getProject()
	{
		return project;
	}

	public void setProject(Project project)
	{
		this.project = project;
	}

	
	private Role role;
	
	@ManyToOne
	public Role getRole()
	{
		return role;
	}

	public void setRole(Role role)
	{
		this.role = role;
	}
	
	private String email;
	
	
	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}
}
