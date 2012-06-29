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

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.userweave.dao.StudyGroupDao;
import com.userweave.domain.StudyGroup;

@Repository
@Transactional
public class StudyGroupDaoImpl extends BaseDaoImpl<StudyGroup> implements StudyGroupDao {
	
	@Override
	public Class<StudyGroup> getPersistentClass() {
		return StudyGroup.class;
	}

	@Override
	public <T extends StudyGroup> T findById(Class<T> clazz, Integer id) {
		return (T) getCurrentSession().get(clazz, id);
	}
}

