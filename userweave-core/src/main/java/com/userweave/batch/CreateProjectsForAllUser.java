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

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.userweave.dao.ProjectDao;
import com.userweave.dao.ProjectUserRoleJoinDao;
import com.userweave.dao.RoleDao;
import com.userweave.dao.StudyDao;
import com.userweave.dao.UserDao;
import com.userweave.domain.Project;
import com.userweave.domain.ProjectUserRoleJoin;
import com.userweave.domain.Role;
import com.userweave.domain.Study;
import com.userweave.domain.StudyState;
import com.userweave.domain.User;

public class CreateProjectsForAllUser
{
	
	public static void main(String[] args)
	{
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-batch.xml");

		UserDao userDao = (UserDao) context.getBean("userDaoImpl");
		
		ProjectDao projectDao = (ProjectDao) context.getBean("projectDaoImpl");
		
		StudyDao studyDao = (StudyDao) context.getBean("studyDaoImpl");
		
		RoleDao roleDao = (RoleDao) context.getBean("roleDaoImpl");
		
		ProjectUserRoleJoinDao purjDao = (ProjectUserRoleJoinDao) context.getBean("projectUserRoleJoinDaoImpl");

		List<User> userList = userDao.findAllByEmail();
		
		
		
		ArrayList roles = new ArrayList();
		
		Role projectAdmin = new Role();
		
		projectAdmin.setRoleName("PROJECT_ADMIN");
		
				
		Role projectParticipant = new Role();
		
		projectParticipant.setRoleName(Role.PROJECT_PARTICIPANT);
		
		
		Role projectGuest = new Role();
		
		projectGuest.setRoleName(Role.PROJECT_GUEST);

		
		roles.add(projectAdmin);
		roles.add(projectParticipant);
		roles.add(projectGuest);
		
		roleDao.save(roles);
		
		for(User u : userList)
		{
			if(u.getHasAlreadyDefaultProject() != null && u.getHasAlreadyDefaultProject() == true)
			{
				continue;
			}
			
			// create default project for each user
			Project p = new Project();
			
			p.setPrivate(true);
			
			p.setName(u.getForename() + " " + u.getSurname() + " - default project");
			
			p.setParentProject(null);
			
			projectDao.save(p);
			
			
			ArrayList<Study> studies = new ArrayList<Study>();
			
			studies.addAll(studyDao.findByOwnerAndState(u, true, StudyState.INIT));
			
			studies.addAll(studyDao.findByOwnerAndState(u, true, StudyState.RUNNING));
			
			studies.addAll(studyDao.findByOwnerAndState(u, true, StudyState.FINISHED));
			
			
			for(Study s : studies)
			{
				// remap studies of user to users default project
				s.setParentProject(p);
				
				studyDao.save(s);
			}
			
			// Create ternary association between user, project and role
			ProjectUserRoleJoin join = purjDao.createJoin(p, u, projectAdmin);
			purjDao.save(join);
			
			u.setHasAlreadyDefaultProject(true);
			userDao.save(u);
		}
	}

}
