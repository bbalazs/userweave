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
import com.userweave.domain.Role;
import com.userweave.domain.User;


public interface ProjectDao extends BaseDao<Project>
{
	public Project findProjectById(int id);
	
	public List<Project> findAllNotDeleted();
	
//	@Deprecated
//	public List<Project> findByOwner(User owner);
//	
//	@Deprecated
//	public List<Project> findByOwnerAndIsTopProject(User owner);
	
	public List<Project> findByUser(User user, boolean alsoDeleted);
	
	public List<Project> findByUser(User user, boolean alsoDeleted, List<Role> roles);
	
	public List<Project> findByParentProject(Project project, boolean alsoDeleted);
	
	public List<Project> findByParentProjectId(int projectId, boolean alsoDeleted);
	
	public void finalizeProject(Project project, User user);
	
	public List<Project> findPublicProjects();
	
	public List<Project> findPublicProjects(User user);
}
