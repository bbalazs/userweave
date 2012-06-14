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

import java.util.HashMap;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.userweave.dao.ProjectUserRoleJoinDao;
import com.userweave.domain.ProjectUserRoleJoin;
import com.userweave.domain.User;

public class GeneratePositionsForPurj
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		HashMap<User, Integer> userPositionMap = 
			new HashMap<User, Integer>();
		
		ApplicationContext context = 
			new ClassPathXmlApplicationContext("applicationContext-batch.xml");
	
		ProjectUserRoleJoinDao purjDao = 
			(ProjectUserRoleJoinDao) context.getBean("projectUserRoleJoinDaoImpl");
	
		List<ProjectUserRoleJoin> joins = purjDao.findAll();
		
		for(ProjectUserRoleJoin join : joins)
		{
			User user = join.getUser();
			
			if(! userPositionMap.containsKey(user))
			{
				userPositionMap.put(user, 0);
			}
			
			Integer position = userPositionMap.get(user);
			
			join.setPosition(userPositionMap.get(user));
		
			purjDao.save(join);
			
			userPositionMap.put(user, position + 1);
		}
		
	}

}
