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

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.userweave.dao.ProjectDao;
import com.userweave.domain.ImageBase;
import com.userweave.domain.Project;

public class CreateImageForEachProject
{

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException
	{
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-batch.xml");
		
		CreateImageForEachProject obj = new CreateImageForEachProject();

		String path = "usab-meth-logo_white.jpg";

		InputStream stream = obj.getClass().getResourceAsStream(path);

		if (stream != null)
		{
			byte[] data = new byte[10000];
			stream.read(data);

			ImageBase logo = new ImageBase();
			logo.setImageData(data);
			
			ProjectDao projectDao = (ProjectDao) context.getBean("projectDaoImpl");
			
			List<Project> projects = projectDao.findAll();
		
			for(Project project : projects)
			{
				project.setLogo(logo.copy());
				
				projectDao.save(project);
			}
		}

	}

}
