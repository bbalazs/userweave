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

import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.userweave.dao.SecurityTokenDao;
import com.userweave.domain.Invitation;
import com.userweave.domain.SecurityToken;
import com.userweave.domain.User;
import com.userweave.domain.SecurityToken.SecurityTokenType;

@Repository
@Transactional
public class SecurityTokenDaoImpl extends BaseDaoImpl<SecurityToken> implements SecurityTokenDao {

	@Override
	public Class<SecurityToken> getPersistentClass() {
		return SecurityToken.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SecurityToken> findByUserAndType(User user, SecurityTokenType type) {
		return getCurrentSession()
		.createQuery("from " + getEntityName() + " where user = :user and type = :type")
		.setParameter("user", user)
		.setParameter("type", type)
		.list();
	}

	@Override
	public SecurityToken findByHashCode(String hashcode) {
		return (SecurityToken) getCurrentSession()
		.createQuery("from " + getEntityName() + " where hashcode = :hashcode")
		.setParameter("hashcode", hashcode)
		.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SecurityToken> findByCreationDate(DateTime date) {
		return getCurrentSession()
		.createQuery("from " + getEntityName() + " where creationDate < :date")
		.setParameter("date", date)
		.list();
	}

	@Override
	public SecurityToken findByInvitationAndType(Invitation invitation, SecurityTokenType type) {
		return (SecurityToken) getCurrentSession()
		.createQuery("from " + getEntityName() + " where invitation = :invitation and type = :type")
		.setParameter("invitation", invitation)
		.setParameter("type", type)
		.uniqueResult();
	}
	
}
