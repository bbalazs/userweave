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

import com.userweave.dao.UserDao;
import com.userweave.domain.User;

@Repository
@Transactional
public class UserDaoImpl extends BaseDaoImpl<User> implements UserDao {

	@Override
	public Class<User> getPersistentClass() {
		return User.class;
	}
	
	public User findByEmail(String email) {
		return (User) getCurrentSession()
		.createQuery("from " + getEntityName() + " where email = :email")
		.setParameter("email", email)
		.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<User> findAllByEmail() {
		return getCurrentSession()
		.createQuery("from " + getEntityName() + " order by email")
		.list();
	}

}
