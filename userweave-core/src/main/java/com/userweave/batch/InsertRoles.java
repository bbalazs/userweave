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
package com.userweave.batch;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.userweave.dao.RoleDao;
import com.userweave.domain.Role;

public class InsertRoles {

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{	
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-batch.xml");
		
		RoleDao roleDao = (RoleDao) context.getBean("roleDaoImpl");
		
Role projectAdmin = new Role();
		
		projectAdmin.setRoleName(Role.PROJECT_ADMIN);
		
		roleDao.save(projectAdmin);
		
		
		Role projectParticipant = new Role();
		
		projectParticipant.setRoleName(Role.PROJECT_PARTICIPANT);
		
		roleDao.save(projectParticipant);
		
		
		Role projectGuest = new Role();
		
		projectGuest.setRoleName(Role.PROJECT_GUEST);

		roleDao.save(projectGuest);
	}

}
