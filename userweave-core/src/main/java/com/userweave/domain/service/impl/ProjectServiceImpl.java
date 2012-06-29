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

import java.io.InputStream;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.userweave.dao.ProjectDao;
import com.userweave.dao.ProjectUserRoleJoinDao;
import com.userweave.domain.ImageBase;
import com.userweave.domain.Project;
import com.userweave.domain.ProjectUserRoleJoin;
import com.userweave.domain.Study;
import com.userweave.domain.User;
import com.userweave.domain.service.ProjectService;
import com.userweave.domain.service.StudyService;

@Service(value="projectService")
public class ProjectServiceImpl implements ProjectService
{
	@Resource
	private ProjectDao projectDao;
	
	@Resource
	private ProjectUserRoleJoinDao purjDao;
	
	@Resource
	private StudyService studyService;
	
	@Override
	public void purge(Project project)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void purge(Integer projectId)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public Project copy(Project srcProject, String name, boolean useOriginalStudyName, User user)
	{
		Project copyProject = new Project();
		
		copyProject.setParentProject(srcProject.getParentProject());
		copyProject.setPrivate(srcProject.isPrivate());
		copyProject.setName(name);
		
		projectDao.save(copyProject);
		
		List<ProjectUserRoleJoin> srcJoins = srcProject.getProjectUserRoleJoins();
		
		for(ProjectUserRoleJoin srcJoin : srcJoins)
		{
			ProjectUserRoleJoin newJoin;
			
			if(user != null)
			{
				 newJoin = purjDao.createJoin(copyProject, user, srcJoin.getRole());
			}
			else
			{
				newJoin = purjDao.createJoin(copyProject, srcJoin.getUser(), srcJoin.getRole());
			}
			
			
			purjDao.save(newJoin);
		}
		
		List<Study> studies = studyService.findByProject(srcProject);
		
		for(Study study : studies)
		{
			if(useOriginalStudyName)
			{
				studyService.copy(study, study.getName(), copyProject);
			}
			else
			{
				studyService.copy(study, study.getName() + "_copy", copyProject);
			}
		}
		
		return copyProject;
	}

	@Override
	public Project createPreConfiguredProject(String name)
	{
		Project project = new Project();
		
		project.setName(name);
		project.setPrivate(true);
		project.setFontColor(true);
		project.setBackgroundColor("#99cc33");
		
		try
		{
			String path = "res/user_weaver_logo_kl.png";
			
			InputStream stream = 
				getClass().getResourceAsStream(path);
			
			if(stream != null)
			{
				byte[] data = new byte[10000];
			    stream.read(data);
				
			    ImageBase logo = new ImageBase();
			    logo.setImageData(data);
			    project.setLogo(logo);
			}
			else
			{
				project.setLogo(null);
			}
			
		} 
		catch (Exception e)
		{
			project.setLogo(null);
		} 
		
		return project;
	}
}
