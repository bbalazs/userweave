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
import java.util.Locale;

import org.apache.wicket.Component;

import com.userweave.domain.Project;
import com.userweave.domain.ProjectInvitation;
import com.userweave.domain.Role;
import com.userweave.domain.User;

public interface ProjectInvitationDao extends BaseDao<ProjectInvitation>
{
	/**
	 * Send invitation to existend user.
	 * 
	 * @param addresser
	 * @param addressee
	 * @param project
	 * @param role
	 */
	public void sendInvitation(User addresser, User addressee, Project project, Role role);
	
	/**
	 * Send invitation to new user.
	 * 
	 * @param recipantEmail
	 * @param addresser
	 * @param project
	 * @param role
	 */
	public void sendInvitation(String recipantEmail, User addresser, Project project, Role role, Locale locale, Component localeStringProvider);
	
	public List<ProjectInvitation> findByRecipant(User recipant);
	
	public List<ProjectInvitation> findByEmail(String email);
	
	public List<ProjectInvitation> findByProject(Project project);
	
	
	public List<ProjectInvitation> findByRecipantAndProject(User recipant, Project project);
	
	public List<ProjectInvitation> findByEmailAndProject(String email, Project project);
}
