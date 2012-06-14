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
package com.userweave.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.userweave.dao.RoleDao;
import com.userweave.domain.Project;
import com.userweave.domain.Role;
import com.userweave.domain.User;

@Repository
public class RoleDaoImpl extends BaseDaoImpl<Role> implements RoleDao
{
	@Override
	public Class<Role> getPersistentClass()
	{
		return Role.class;
	}

	@Override
	public Role findByName(String name)
	{
		return (Role) getCurrentSession()
		.createQuery("from " + getEntityName() + " where rolename = :rolename")
		.setParameter("rolename", name)
		.uniqueResult();
	}

	@Override
	public List<Role> findByNames(String... names)
	{
		String query = "from " + getEntityName() + " where rolename in (";
		
		for(int i= 0; i < names.length; i++)
		{
			if(i == 0)
			{
				query += "'" + names[0] + "'";
			}
			else
			{
				query += ", '" + names[i] + "'";
			}
		}
		
		query += ")";
		
		
		Query q = getCurrentSession().createQuery(query);
		
		return q.list();
	}
	
	@Override
	public List<Role> getRolesByUserAndProject(User user, Project project)
	{
		String query = "select r from " + getEntityName() + " r " + 
					  "left join r.projectUserRoleJoins purj " +
					  "where purj.user = :user and purj.project = :project";
		
		Query q = getCurrentSession()
					.createQuery(query)
						.setParameter("user", user)
						.setParameter("project", project);
		
		return q.list();
	}

}
