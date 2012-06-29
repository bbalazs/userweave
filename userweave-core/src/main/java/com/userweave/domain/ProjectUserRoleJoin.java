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

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class ProjectUserRoleJoin implements Serializable
{
	private static final long serialVersionUID = -4740635834971752760L;

	@Embeddable
	public static class Id implements Serializable
	{
		private static final long serialVersionUID = 2797764030438869805L;

		@Column(name="projectid")
		private Integer projectId;
		
		@Column(name="userid")
		private Integer userId;
		
		@Column(name="roleid")
		private Integer roleId;
		
		public Id() {};
		
		public Id(Integer projectId, Integer userId, Integer roleId)
		{
			this.projectId = projectId;
			this.userId = userId;
			this.roleId = roleId;
		}
		
		public Id(Project project, User user, Role role)
		{
			this.projectId = project.getId();
			this.userId = user.getId();
			this.roleId = role.getId();
		}

		@Override
		public int hashCode()
		{
			return projectId.hashCode() + userId.hashCode() + roleId.hashCode();
		}

		@Override
		public boolean equals(Object obj)
		{
			if(obj != null && obj instanceof Id)
			{
				Id that = (Id) obj;
				
				return this.projectId.equals(that.projectId) &&
					   this.userId.equals(that.userId) &&
					   this.roleId.equals(that.roleId);
			}
			else
			{
				return false;
			}
		}
	}
	
	@EmbeddedId
	private final Id id = new Id();
	
	@ManyToOne
	@JoinColumn(name = "projectid", insertable=false, updatable=false)
	private Project project;
	
	@ManyToOne
	@JoinColumn(name = "userid", insertable=false, updatable=false)
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "roleid", insertable=false, updatable=false)
	private Role role;
	
	
	public ProjectUserRoleJoin() {}
	
	public ProjectUserRoleJoin(Project project, User user, Role role) 
	{
		this.project = project;
		
		this.user = user;
		
		this.role = role;
		
		this.id.projectId = project.getId();
		this.id.roleId = role.getId();
		this.id.userId = user.getId();
	}
	
	public Project getProject()
	{
		return project;
	}

	public User getUser()
	{
		return user;
	}

	public Role getRole()
	{
		return role;
	}
	
	/**
	 * Give a join a position for ordering. Since evry join is
	 * unique, it will save the users prefered ordering.
	 * 
	 * @see #1289
	 */
	private Integer position;

	public Integer getPosition()
	{
		return position;
	}

	public void setPosition(Integer position)
	{
		this.position = position;
	}
}
