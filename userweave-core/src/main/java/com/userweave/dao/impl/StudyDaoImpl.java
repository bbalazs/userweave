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
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.userweave.dao.StudyDao;
import com.userweave.domain.Project;
import com.userweave.domain.Study;
import com.userweave.domain.StudyState;
import com.userweave.domain.User;

@Repository
@Transactional
public class StudyDaoImpl extends BaseDaoImpl<Study> implements StudyDao {

	@Override
	public Class<Study> getPersistentClass() {
		return Study.class;
	}

	@Override
	public Study findByHashcode(String hashCode) {
		return (Study) getCurrentSession()
			.createQuery("from " + getEntityName() + " where hashcode = :hashcode")
			.setParameter("hashcode", hashCode)
			.uniqueResult();
	}

	@Override
	public Study findByReportCode(String hashCode) {
		return (Study) getCurrentSession()
		.createQuery("from " + getEntityName() + " where reportcode = :hashcode")
		.setParameter("hashcode", hashCode)
		.uniqueResult();
	
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Study> findByOwnerAndState(User owner, boolean alsoDeleted, StudyState state) {
		String query = "from " + getEntityName() + " where 1=1";
		
		if(owner != null) {
			query += " and owner = :owner";
		}
		if(!alsoDeleted) {
			query += " and deletedAt is null";
		}
		if(state != null) {
			query += " and state = :state";
		}

		Query hquery = getCurrentSession().createQuery(query);
		
		if(owner != null) {
			hquery.setParameter("owner", owner);
		}
		
		if(state != null) {
			hquery.setParameter("state", state);
		}
		
		return hquery.list();

	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Study> findDeletedStudies(User owner)
	{
		String query = "from " + getEntityName() + " where 1=1";
		
		if(owner != null) {
			
				query += " and owner = :owner";
		}
		
		query += " and deletedAt is not null";
		
		Query hquery = getCurrentSession().createQuery(query);
		
		if(owner != null) {
			hquery.setParameter("owner", owner);
		}
		
		return hquery.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Study> findAgedDeletedStudies() {
		return getCurrentSession()
		.createQuery("from " + getEntityName() + " where deletedAt < :date and deletedAt is not null")
		.setParameter("date", new DateTime().minusMonths(6))
		.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Study> findByProjectAndState(Project project,
			boolean alsoDeleted, StudyState state)
	{
		String query = "from " + getEntityName() + " where 1=1";
		
		if(project != null)
		{
			query += " and parentproject_id = :parentproject_id";
		}
		
		if(!alsoDeleted) 
		{
			query += " and deletedAt is null";
		}
		
		if(state != null) 
		{
			query += " and state = :state";
		}
		
		Query hquery = getCurrentSession().createQuery(query);
		
		if(project != null)
		{
			hquery.setParameter("parentproject_id", project.getId());
		}
		
		if(state != null) 
		{
			hquery.setParameter("state", state);
		}
		
		return hquery.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Study> findByProjectIdAndState(int projectId,
			boolean alsoDeleted, StudyState state)
	{
		String query = "from " + getEntityName() + " where 1=1" + 
					   " and parentproject_id = :parentproject_id";
		
		if(!alsoDeleted) 
		{
			query += " and deletedAt is null";
		}
		
		if(state != null) 
		{
			query += " and state = :state";
		}
		
		Query hquery = getCurrentSession().createQuery(query);
		
		hquery.setParameter("parentproject_id", projectId);
		
		
		if(state != null) 
		{
			hquery.setParameter("state", state);
		}
		
		return hquery.list();
	}

	@Override
	public List<Study> findDeletedStudies(Project project)
	{
		String query = "from " + getEntityName() + " where 1=1";
		
		if(project != null) {
			
				query += " and parentproject_id = :projectId";
		}
		
		query += " and deletedAt is not null";
		
		Query hquery = getCurrentSession().createQuery(query);
		
		if(project != null) {
			hquery.setParameter("projectId", project.getId());
		}
		
		return hquery.list();
	}
}
