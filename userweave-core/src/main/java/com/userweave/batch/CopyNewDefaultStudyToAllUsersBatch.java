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

import com.userweave.dao.UserDao;
import com.userweave.domain.User;
import com.userweave.domain.service.UserService;

public class CopyNewDefaultStudyToAllUsersBatch {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-batch.xml");
		UserService userService = (UserService) context.getBean("userService");

		UserDao userDao = (UserDao) context.getBean("userDaoImpl");
		if(args.length == 0) {
			System.err.println("no args given, exiting");
			System.exit(100);
		} else {
			for(String arg : args) {
				copyDefaultStudyToUserBatch(Integer.valueOf(arg), userService, userDao);
			}
		}
	}

	private static void copyDefaultStudyToUserBatch(Integer studyId, UserService userService, UserDao userDao) {
		List<User> users = userDao.findAll();
		List<String> emails = new ArrayList<String>();
		for(User user : users) {
			emails.add(user.getEmail());
		}
		
		copyDefaultStudyToUserBatch(studyId, emails, userService);
	}

	private static void copyDefaultStudyToUserBatch(Integer studyId, List<String> emails, UserService userService) {
		for(String email: emails) {
			try
			{
				userService.copyStudyToUser(studyId, email);
			}
			catch(NullPointerException e)
			{
				System.out.println(e.getMessage());
			}
		}
	}
	
}