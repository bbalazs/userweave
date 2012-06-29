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

import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.userweave.dao.BaseDao;
import com.userweave.domain.EntityBase;

@Transactional
@Repository
public abstract class BaseDaoImpl<T extends EntityBase> implements BaseDao<T> {

	@Resource
    private SessionFactory sessionFactory;

    protected Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}
    
	public BaseDaoImpl() {
		super();
	}
	
	public abstract Class<T> getPersistentClass();
	
	public String getEntityName() {
		return getPersistentClass().getSimpleName();
	}

	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		return getCurrentSession()
					.createQuery("from " + getEntityName())
					.list();
	}

	@SuppressWarnings("unchecked")
	public T findById(Integer id) {
		return (T) getCurrentSession()
		.createQuery("from " + getEntityName() + " where id = :id")
		.setParameter("id", id)
		.uniqueResult();
//		return (T) getCurrentSession().load(getPersistentClass(), id);
	}

	public void save(T entity) {
		getCurrentSession().saveOrUpdate(entity);
	}
	
	@Override
	public void save(List<T> entities) {
		for (T t : entities) {
			if (t != null) {
				getCurrentSession().saveOrUpdate(t);
			}
		}
	}
	
	public void delete(List<T> entities) {
		for(T entity: entities) {
			delete(entity);
		}
	}

	public void delete(T entity) {
		getCurrentSession().delete(entity);		
	}
	
	public void delete(Integer id) {
		T entity = findById(id);
		if (entity != null) {
			delete(entity);
		}
	}
}