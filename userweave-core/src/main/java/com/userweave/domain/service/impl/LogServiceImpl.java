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
package com.userweave.domain.service.impl;

import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.userweave.dao.LogEntryDao;
import com.userweave.domain.LogEntry;
import com.userweave.domain.User;
import com.userweave.domain.LogEntry.LogEntryType;
import com.userweave.domain.service.LogService;

@Service()
public class LogServiceImpl implements LogService {

	private final static Logger logger = LoggerFactory.getLogger(LogServiceImpl.class);
	
	@Autowired
	private LogEntryDao logEntryDao;

	@Override
	public LogEntry log(LogEntryType type, User user, String remoteAddress) {
		LogEntry logEntry = new LogEntry();
		logEntry.setDate(new DateTime());
		logEntry.setLogEntryType(type);
		logEntry.setUser(user);
		logEntry.setRemoteAddr(remoteAddress);

		logEntryDao.save(logEntry);
		
		return logEntry;
	}
	
	@Transactional
	@Override
	public void deleteLogEntries(User user) {
		List<LogEntry> entries = logEntryDao.findByUser(user);
		logEntryDao.delete(entries);
	}

}


