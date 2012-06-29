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

import org.apache.wicket.model.LoadableDetachableModel;

import com.userweave.dao.BaseDao;
import com.userweave.domain.EntityBase;

@SuppressWarnings("serial")
@Deprecated
public class SpringLoadableDetachableModel extends LoadableDetachableModel {

	private BaseDao baseDao;

	protected BaseDao getDao() {
		return baseDao;
	}
	
	private final int id;

	public SpringLoadableDetachableModel(BaseDao baseDao, int id) {
		this(id);
		if(baseDao == null) {
			throw new NullPointerException();
		}
		this.baseDao = baseDao;
	}

	public SpringLoadableDetachableModel(BaseDao baseDao, EntityBase entity) {
		this(entity);
		if(baseDao == null) {
			throw new NullPointerException();
		}
		this.baseDao = baseDao;
	}

	public SpringLoadableDetachableModel(int id) {
		this.id = id;
	}
	
	public SpringLoadableDetachableModel(EntityBase entity) {
		super(entity);
		this.id = entity.getId();
	}
	
	@Override
	protected Object load() {
		return postLoad(getDao().findById(id));
	}

	protected Object postLoad(Object object) {
		return object;
	}
}
