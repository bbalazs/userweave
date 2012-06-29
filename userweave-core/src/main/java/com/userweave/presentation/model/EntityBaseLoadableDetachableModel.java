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
package com.userweave.presentation.model;

import java.io.Serializable;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.hibernate.proxy.HibernateProxy;

import com.userweave.dao.GeneralDao;
import com.userweave.domain.EntityBase;

@SuppressWarnings("serial")
public class EntityBaseLoadableDetachableModel<T extends EntityBase> extends LoadableDetachableModel {

	@SpringBean
	private GeneralDao generalDao;

	private T entity;
	private final Class<T> clazz;
	private Serializable id;

	/**
	 * returns the stored entity if it is transient
	 * 
	 * loads the object from the database (if persistent)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected T load() {
		if(generalDao == null) 
		{
			Injector.get().inject(this);
		}

		if(entity != null) {
			if(entity.isTransient()) {
				// return the re
				return entity;
			} else {
				id = entity.getId();
				// remove reference to entity, we use the
				// persistent id now
				entity = null;
			}
		}

		// object comes from database 
		return (T) generalDao.load(clazz, id);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T getObject() {
		return (T) super.getObject();
	}
	
	public EntityBaseLoadableDetachableModel(Class<T> clazz, Integer id) {
		this.clazz = clazz;
		this.id = id;

		if(id == null) {
			try {
				// here we dynamically create an instance
				// this *may* fail so we throw a runtime exception
				entity = clazz.newInstance();
			} catch (InstantiationException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public EntityBaseLoadableDetachableModel(T entity) {
		super(entity);

		if(entity == null) {
			throw new NullPointerException();
		}
		
        if(entity.isTransient()) {
        	// keep an object reference if entity is transient
        	this.entity = entity;
        } else {
        	this.id = entity.getId();
        }

		if (entity instanceof HibernateProxy) {
	        HibernateProxy proxy = (HibernateProxy) entity;
	        this.clazz = proxy.getHibernateLazyInitializer().getPersistentClass();
	    } else {
	        clazz = (Class<T>) entity.getClass();
	    }
	}

}
