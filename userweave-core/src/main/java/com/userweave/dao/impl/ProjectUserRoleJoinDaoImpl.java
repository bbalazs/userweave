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

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.userweave.dao.ProjectUserRoleJoinDao;
import com.userweave.domain.Project;
import com.userweave.domain.ProjectUserRoleJoin;
import com.userweave.domain.Role;
import com.userweave.domain.User;

@Transactional
@Repository
public class ProjectUserRoleJoinDaoImpl implements ProjectUserRoleJoinDao
{
	@Resource
    private SessionFactory sessionFactory;

//	@Resource
//	private RoleDao roleDao;
	
    protected Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}
	
	public ProjectUserRoleJoinDaoImpl() 
	{
		super();
	}
	
	@Override
	public Class<ProjectUserRoleJoin> getPersistentClass()
	{
		return ProjectUserRoleJoin.class;
	}

	public String getEntityName() 
	{
		return getPersistentClass().getSimpleName();
	}
	
	@Override
	public ProjectUserRoleJoin createJoin(Project project, User user, Role role)
	{
		ProjectUserRoleJoin newJoin = new ProjectUserRoleJoin(project, user, role);
		
		Integer position = findLargestOrderingPosition(user);
		
		if(position == null)
		{
			newJoin.setPosition(0);
		}
		else
		{
			newJoin.setPosition(position + 1);
		}
		
		return newJoin;
	}
	
	@Override
	public void save(ProjectUserRoleJoin entity) 
	{
		sessionFactory.getCurrentSession().saveOrUpdate(entity);
	}
	
	@Override
	public void delete(ProjectUserRoleJoin entity) 
	{
		sessionFactory.getCurrentSession().delete(entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProjectUserRoleJoin> findAll()
	{
		return getCurrentSession()
					.createQuery("from " + getEntityName())
					.list();
	}
	
	@Override
	public Integer findLargestOrderingPosition(User user)
	{
		Query query = getCurrentSession().createQuery(
				"select max(purj.position) from " + 
				getEntityName() + " purj" +  
				" where purj.user = :user");
		
		query.setParameter("user", user);
		
		return (Integer)query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ProjectUserRoleJoin> getJoinsByProject(Project project) 
	{
		String query = "from " + getEntityName() + " where project = :project";
		
		Query q = getCurrentSession().createQuery(query).setParameter("project", project);
		
		return q.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProjectUserRoleJoin> getJoinsByUser(User user)
	{
		String query = "from " + getEntityName() + 
					   " where user = :user " +
					   "order by position";
		
		Query q = getCurrentSession().createQuery(query).setParameter("user", user);
		
		return q.list();
	}
	
	@Override
	public List<ProjectUserRoleJoin> getProjectAdmins(Project project)
	{
//		String query = "from " + getEntityName() + " where project = :project " +
//				"and role = :role";
//		
//		Query q = getCurrentSession()
//					.createQuery(query)
//						.setParameter("project", project)
//						.setParameter("role", roleDao.findByName(Role.PROJECT_ADMIN));
//		
//		return q.list();
		
		return getJoinsByProjectAndRole(project, Role.PROJECT_ADMIN);
	}

	@Override
	public List<ProjectUserRoleJoin> getProjectParticipants(Project project)
	{
		return getJoinsByProjectAndRole(project, Role.PROJECT_PARTICIPANT);
	}

	@Override
	public List<ProjectUserRoleJoin> getProjectGuests(Project project)
	{
		return getJoinsByProjectAndRole(project, Role.PROJECT_GUEST);
	}
	
	@SuppressWarnings("unchecked")
	private List<ProjectUserRoleJoin> getJoinsByProjectAndRole(Project project, String role)
	{
		String query = "select purj from " + getEntityName() + " purj " +
		   "left join purj.role r " +
		   "where purj.project = :project and r.roleName = :role " +
		   "and purj.user.deactivatedAt is null";

		Query q = getCurrentSession()
				.createQuery(query)
					.setParameter("project", project)
					.setParameter("role", role);
		
		List<ProjectUserRoleJoin> joins = q.list();
		
		return joins;
	}
}
