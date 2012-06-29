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

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.userweave.dao.ProjectDao;
import com.userweave.dao.ProjectUserRoleJoinDao;
import com.userweave.dao.RoleDao;
import com.userweave.domain.Project;
import com.userweave.domain.ProjectUserRoleJoin;
import com.userweave.domain.Role;
import com.userweave.domain.User;

@Repository
public class ProjectDaoImpl extends BaseDaoImpl<Project> implements ProjectDao
{
	@Autowired
	private RoleDao roleDao;
	
	@Autowired
	private ProjectUserRoleJoinDao purjDao;
	
	@Override
	public Class<Project> getPersistentClass()
	{
		return Project.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Project> findAll() {
		return getCurrentSession()
					.createQuery("from " + getEntityName() + " p order by p.name")
					.list();
	}
	
	@Override
	public List<Project> findAllNotDeleted()
	{
		return getCurrentSession()
					.createQuery("from " + getEntityName() + " p where deletedAt is null order by p.name")
					.list();
	}
	
	@Override
	public Project findProjectById(int id)
	{
		return (Project) getCurrentSession()
			.createQuery("from " + getEntityName() + " where id = :id")
			.setParameter("id", id)
			.uniqueResult();
	}
	
	@Override
	public List<Project> findByUser(User user, boolean alsoDeleted)
	{
		String query = "select p from " + getEntityName() + " p " +
					   "left join p.projectUserRoleJoins purj " +
					   "where purj.user = :user " +
					   "and parentproject_id is null";
		
		if(!alsoDeleted)
		{
			query += " and deletedat is null";
		}
		
		query += " order by purj.position";
		
		Query q = getCurrentSession().createQuery(query).setParameter("user", user);
		
		return q.list();
	}
	
	public List<Project> findByUser(User user, boolean alsoDeleted, List<Role> roles)
	{
		String query = "select p from " + getEntityName() + " p " +
		   "left join p.projectUserRoleJoins purj " +
		   "where purj.user = :user " +
		   "and parentproject_id is null";
		
		if(roles.size() == 1)
		{
			query +=  "and purj.role = :role";
		}
		else
		{
			query += " and purj.role in (";
			
			for(int i = 0; i < roles.size(); i++)
			{
				if(i == 0)
				{
					query += ":role0";
				}
				else
				{
					query += ", :role" + i;
				}
			}
			
			query += ")";
		}
		   

		if(!alsoDeleted)
		{
			query += " and deletedat is null";
		}
		
		
		Query q = getCurrentSession().createQuery(query).setParameter("user", user);
		
		
		if(roles.size() == 1)
		{
			q.setParameter("role", roles.get(0));
		}
		else
		{
			for(int i = 0; i < roles.size(); i++)
			{
				if(i == 0)
				{
					q.setParameter("role0", roles.get(0));
				}
				else
				{
					q.setParameter("role" + i, roles.get(i));
				}
			}
		}
		
		return q.list();
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public List<Project> findByParentProject(Project project, boolean alsoDeleted)
	{
		String query = "from " + getEntityName() + " where parentProject = :parentProject";
		
		if(!alsoDeleted)
		{
			query += " and deletedat is null";
		}
		
		Query q = getCurrentSession().createQuery(query).setParameter("parentProject", project);
		
		return q.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Project> findByParentProjectId(int projectId, boolean alsoDeleted)
	{
		String query = "from " + getEntityName() + " where parentproject_id = :parentProjectId";
		
		if(!alsoDeleted)
		{
			query += " and deletedat is null";
		}
		
		Query q = getCurrentSession().createQuery(query).setParameter("parentProjectId", projectId);
		
		return q.list();
	}

	@Override
	public void finalizeProject(Project project, User user)
	{	
		project.setProjectUserRoleJoins(new ArrayList<ProjectUserRoleJoin>());
		
		this.save(project);
		
		ProjectUserRoleJoin newJoin = 
			purjDao.createJoin(project, user, roleDao.findByName(Role.PROJECT_ADMIN));
		
		purjDao.save(newJoin);
	}

	@Override
	public List<Project> findPublicProjects()
	{
		return findPublicProjects(null);
	}

	@Override
	public List<Project> findPublicProjects(User user)
	{
		String query = "from " + getEntityName(); 
					   
		if(user != null)
		{
			query += " left join p.projectUserRoleJoins purj " +
			   		 "where purj.user = :user";
		}
		else
		{
			query += " where private = false";
		}
		
		Query q = getCurrentSession().createQuery(query);
		
		if(user != null)
		{
			q.setParameter("user", user);
		}
		
		return q.list();
	}
}
