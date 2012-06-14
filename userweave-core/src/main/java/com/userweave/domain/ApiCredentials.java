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
package com.userweave.domain;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import org.joda.time.DateTime;

@Entity
public class ApiCredentials extends EntityBase {

	private static final long serialVersionUID = 8176776426342402857L;

	private String hash;
	private boolean active = false;
	private DateTime activeLastChange;
	private User user;

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getHash() {
		return hash;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isActive() {
		return active;
	}

	public void setActiveLastChange(DateTime activeLastChange) {
		this.activeLastChange = activeLastChange;
	}

	public DateTime getActiveLastChange() {
		return activeLastChange;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@OneToOne(mappedBy="apiCredentials")
	public User getUser() {
		return user;
	}
	
	
}
