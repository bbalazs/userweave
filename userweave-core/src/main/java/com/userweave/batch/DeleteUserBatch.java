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

import com.userweave.domain.service.UserService;

public class DeleteUserBatch {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-batch.xml");
		UserService userService = (UserService) context.getBean("userService");
		if(args.length == 0) {
			args = new String[] { "malloc@tng.de" };
		}
		
		throw new Exception("user purging temporary disabled");
//		for(String arg: args) {
//			purge(userService, arg);
//		}
	}

//	private static void purge(UserService service, String email) {
//		try {
//			boolean b = service.purge(email);
//			if(b) {
//				System.out.println("Deleted user with e-mail "+email);
//			} else {
//				System.out.println("NOT Deleted user with e-mail "+email);
//			}
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//	}

}
