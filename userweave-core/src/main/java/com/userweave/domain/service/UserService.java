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
package com.userweave.domain.service;

import java.util.Locale;

import com.userweave.domain.Project;
import com.userweave.domain.SecurityToken.SecurityTokenType;
import com.userweave.domain.User;

public interface UserService {
	
	public User createUser(String email, Locale locale);

	/**
	 * physically removes the user and all associated entities (studies, log entries)
	 * @param user
	 */
	//public void purgeUser(User user);
	
	/**
	 * copy all studies of default user to new user identified by email
	 * 
	 * @param email
	 */
	public void copyStudiesOfDefaultUser(String email);
	
	public void copyStudiesOfDefaultUser(User user);

	public Project findDefaultProjectOfUser(User user);
	
	public void copyStudyToUser(Integer studyId, String email)
		throws NullPointerException;
	
	//public boolean purge(String email);
	
	public void deactivate(User user);
	public void activate(User user);
	
	public void changePassword(User user, String password);
	public boolean validatePassword(User user, String password);
	
	public User findByEmail(String email);
	public User findById(Integer userId);

	public void sendRegisterMail(String email, Locale locale, MailMessageProvider provider);

	public void sendInvitationMail(User owner, String email, Locale locale, MailMessageProvider provider);
	
	public void sendResetPasswordMail(User user, MailMessageProvider provider);

	public User validateToken(String code, SecurityTokenType ... securityTokenTypes);

}