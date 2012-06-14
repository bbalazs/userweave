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

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.userweave.domain.LocalizedString;
import com.userweave.module.methoden.mockup.dao.MockupConfigurationDao;
import com.userweave.module.methoden.mockup.domain.MockupConfigurationEntity;

public class CreateLocalizedUrlForMockup
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-batch.xml");
	
		MockupConfigurationDao mockupConfigurationDao = 
			(MockupConfigurationDao) context.getBean("mockupConfigurationDaoImpl");

		List<MockupConfigurationEntity> mockups = mockupConfigurationDao.findAll();
	
		for(MockupConfigurationEntity mockup : mockups)
		{
			LocalizedString url = new LocalizedString();
		
			url.setValue(mockup.getURL(), mockup.getStudy().getLocale());
				
			mockup.setLocaleUrl(url);
				
			mockupConfigurationDao.save(mockup);
		}
	}
}
