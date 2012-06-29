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
package com.userweave.module;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.userweave.dao.StudyDao;


public class TestBase {

	private static ClassPathXmlApplicationContext appContext;

	protected SessionFactory sessionFactory = null;

	static {
    	appContext = new ClassPathXmlApplicationContext(new String[] {"applicationContext-batch.xml"});
    }

    @Before public void setUp() throws Exception {
	      sessionFactory = (SessionFactory) getBean("sessionFactory");
	      Session session = SessionFactoryUtils.getSession(sessionFactory, true);
	      TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
	}

    @After public void tearDown() throws Exception {
      SessionHolder sessionHolder = (SessionHolder) TransactionSynchronizationManager.unbindResource(sessionFactory);
      SessionFactoryUtils.closeSession(sessionHolder.getSession());

    }

	public <T> T getBean(String name) {
		return (T) appContext.getBean(name);
	}

	protected StudyDao getStudyDao() {
		return getBean("studyDao");
	}
}
