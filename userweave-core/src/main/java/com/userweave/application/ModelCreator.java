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
package com.userweave.application;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import com.userweave.dao.BaseDao;
import com.userweave.domain.EntityBase;

@SuppressWarnings("serial")
public class ModelCreator {
	public static <T extends EntityBase> IModel createLoadableDetachableModel(T entity, final BaseDao<T> dao) {
		final int entityId = entity.getId();
		
		return new LoadableDetachableModel() {

			@Override
			protected Object load() {
				return dao.findById(entityId);
			}
		};		
	}
}
