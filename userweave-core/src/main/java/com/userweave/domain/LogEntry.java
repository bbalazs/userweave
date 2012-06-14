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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;

import org.joda.time.DateTime;

@Entity
public class LogEntry extends EntityBase {

	public enum LogEntryType { LOGIN, LOGOUT };
	
	private static final long serialVersionUID = -5557850674932704085L;
	
	private DateTime date;

	@org.hibernate.annotations.Type(type="org.joda.time.contrib.hibernate.PersistentDateTime")
	public DateTime getDate() {
		return date;
	}

	public void setDate(DateTime date) {
		this.date = date;
	}
	
	private User user;
	
	@OneToOne
	public User getUser() {
		return user;
	} 

	public void setUser(User user) {
		this.user = user;
	}
	
	private String remoteAddr;

	public String getRemoteAddr() {
		return remoteAddr;
	}

	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}
	
	private LogEntryType logEntryType;


	@Enumerated(EnumType.STRING)
	public LogEntryType getLogEntryType() {
		return logEntryType;
	}
	
	public void setLogEntryType(LogEntryType logEntryType) {
		this.logEntryType = logEntryType;	
	}
}
