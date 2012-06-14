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
package com.userweave.dao;

import java.util.List;

import com.userweave.domain.Project;
import com.userweave.domain.ProjectUserRoleJoin;
import com.userweave.domain.Role;
import com.userweave.domain.User;

public interface ProjectUserRoleJoinDao
{
	public Class<ProjectUserRoleJoin> getPersistentClass();
	
	public ProjectUserRoleJoin createJoin(Project project, User user, Role role);
	
	public void save(ProjectUserRoleJoin entity);
	
	public void delete(ProjectUserRoleJoin entity);
	
	public List<ProjectUserRoleJoin> findAll();
	
	public Integer findLargestOrderingPosition(User user);
	
	public List<ProjectUserRoleJoin> getJoinsByProject(Project project);
	
	public List<ProjectUserRoleJoin> getJoinsByUser(User user);
	
	public List<ProjectUserRoleJoin> getProjectAdmins(Project project);
	
	public List<ProjectUserRoleJoin> getProjectParticipants(Project project);
	
	public List<ProjectUserRoleJoin> getProjectGuests(Project project);
}
