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
package com.userweave.module.methoden.iconunderstandability.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.userweave.dao.impl.StudyDependendDaoImpl;
import com.userweave.domain.Study;
import com.userweave.module.methoden.iconunderstandability.dao.IconTermMatchingConfigurationDao;
import com.userweave.module.methoden.iconunderstandability.dao.IconTermMatchingType;
import com.userweave.module.methoden.iconunderstandability.domain.IconTermMatchingConfigurationEntity;

@Repository
@Transactional
public class IconTermMatchingConfigurationDaoImpl extends StudyDependendDaoImpl<IconTermMatchingConfigurationEntity> implements IconTermMatchingConfigurationDao {
	
	@Override
	public Class<IconTermMatchingConfigurationEntity> getPersistentClass() {
		return IconTermMatchingConfigurationEntity.class;
	}	
	
	@SuppressWarnings("unchecked")
	public List<IconTermMatchingConfigurationEntity> findByStudyAndType(Study study, IconTermMatchingType type) {
		return getCurrentSession()
		.createQuery("from " + getEntityName() + " where study = :study and type =:type")
		.setParameter("study", study)
		.setParameter("type", type)
		.list();
	}
	
}

