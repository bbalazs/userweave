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

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class SecurityToken extends EntityBase {

	public enum SecurityTokenType {
		VERIFY_USER,RESET_PASSWORD, INVITE_USER, API_ACCESS;

		public static boolean contains(SecurityTokenType type, SecurityTokenType ... types) {
			for(SecurityTokenType curType : types) {
				if(curType.equals(type)) {
					return true;
				}
			}
			return false;
		}
	};
	
	private static final long serialVersionUID = 1L;
	
	private String hashCode;

	private Date creationDate;

	private SecurityTokenType type;

	private User user;

	public String getHashCode() {
		return hashCode;
	}

	public void setHashCode(String hashCode) {
		this.hashCode = hashCode;
	}

	@Enumerated
	public SecurityTokenType getType() {
		return type;
	}

	public void setType(SecurityTokenType type) {
		this.type = type;
	}

	@ManyToOne
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
			
	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	
	private Invitation invitation;

	@OneToOne
	public Invitation getInvitation() {
		return invitation;
	}
	
	public void setInvitation(Invitation invitation) {
		this.invitation = invitation;
	}
	
}
