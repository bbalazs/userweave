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

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.userweave.dao.LogEntryDao;
import com.userweave.domain.LogEntry;
import com.userweave.domain.User;

@Transactional
@Repository
public class LogEntryDaoImpl extends BaseDaoImpl<LogEntry> implements LogEntryDao {

	@Override
	public Class<LogEntry> getPersistentClass() {
		return LogEntry.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LogEntry> findByUser(User user) {
		return getCurrentSession()
				.createQuery("from "+getEntityName() + " where user = :user")
				.setParameter("user", user)
				.list();
	}

}
