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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.userweave.dao.ProjectInvitationDao;
import com.userweave.dao.ProjectUserRoleJoinDao;
import com.userweave.dao.RoleDao;
import com.userweave.domain.ProjectInvitation;
import com.userweave.domain.ProjectUserRoleJoin;
import com.userweave.domain.service.ProjectInvitationService;

@Service(value="projectInvitationService")
public class ProjectInvitationServiceImpl implements ProjectInvitationService
{
	@Autowired
	private ProjectUserRoleJoinDao purjDao;
	
	@Autowired
	private ProjectInvitationDao projectInvitationDao;
	
	@Autowired
	private RoleDao roleDao;
	
	/**
	 * Synchronized, because of race condition between accept of 
	 * an invitation and a delete request for the given invitation.
	 */
	@Override
	public synchronized void acceptProjectInvitation(int invitationId)
	{
		ProjectInvitation currentInvitation = 
			projectInvitationDao.findById(invitationId);
		
		if(currentInvitation == null)
		{
			// invitation has already been deleted,
			// so there ist nothing to do.
			return;
		}
		
		ProjectUserRoleJoin newJoin = 
			purjDao.createJoin(
					currentInvitation.getProject(), 
					currentInvitation.getAddressee(), 
					roleDao.findByName(currentInvitation.getRole().getRoleName()));
		
		purjDao.save(newJoin);
		
		projectInvitationDao.delete(currentInvitation);
	}

	/**
	 * Synchronized to prevent acception of invitation.
	 */
	@Override
	public synchronized void declineProjectInvitation(int invitationId)
	{
		projectInvitationDao.delete(invitationId);
	}

}
