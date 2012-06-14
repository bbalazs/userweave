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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import javax.mail.MessagingException;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.userweave.dao.InvitationDao;
import com.userweave.dao.ProjectDao;
import com.userweave.dao.StudyDao;
import com.userweave.dao.UserDao;
import com.userweave.domain.Address;
import com.userweave.domain.Callnumber;
import com.userweave.domain.Invitation;
import com.userweave.domain.Project;
import com.userweave.domain.SecurityToken;
import com.userweave.domain.SecurityToken.SecurityTokenType;
import com.userweave.domain.Study;
import com.userweave.domain.User;
import com.userweave.domain.service.LogService;
import com.userweave.domain.service.MailMessageProvider;
import com.userweave.domain.service.ProjectService;
import com.userweave.domain.service.SecurityTokenService;
import com.userweave.domain.service.StudyService;
import com.userweave.domain.service.UserService;
import com.userweave.domain.service.mail.MailService;

@Service(value="userService")
public class UserServiceImpl implements UserService {

	private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	private UserDao userDao;

	@Autowired
	private StudyDao studyDao;

	@Autowired
	private InvitationDao invitationDao;

	@Autowired
	private ProjectDao projectDao;
	
	@Autowired
	private SecurityTokenService securityTokenService;

	@Autowired
	private MailService mailService;
	
	@Autowired
	private StudyService studyService;
	
	@Autowired
	private LogService logService;
	
	@Autowired
	private ProjectService projectService;
	
	/**
	 * create a user with given email and locale. 
	 */
	@Transactional
	public User createUser(String email, Locale locale) {
		email = email.toLowerCase();
		User user = new User();
		user.setLocale(locale);
		user.setAddress(new Address());
		user.setCallnumber(new Callnumber());
		user.setVerified(false);
		//user.setRegistered(false);
		user.setReceiveNews(true);
		user.setHasAlreadyDefaultProject(true);
		
		
		if(userDao.findByEmail(email) == null) {
			user.setEmail(email.toLowerCase());
		} 
		
		userDao.save(user);
		return user;
	}
	
	@Transactional
	public void copyStudiesOfDefaultUser(String email) {
		User user = findByEmail(email);
		copyStudiesOfDefaultUser(user);
	}
	
	@Transactional
	public void copyStudiesOfDefaultUser(User user) {
		String email = "demo@userweave.net";
		User defaultUser = userDao.findByEmail(email);
		if(defaultUser == null) {
			logger.error("error finding default user '"+email+"'");
			return;
		}
		if(user == null) {
			logger.error("user is null, aborting...");
			return;
		}

		if(user.equals(defaultUser)) {
			logger.error("user and defaultUser are the same, aborting...");
			return;
		}
		
		// Create Example Project(s) for User
		List<Project> projects = projectDao.findByUser(defaultUser, false);
		
		for(Project project : projects)
		{
			projectService.copy(project, project.getName(), true, user);
		}
		
//		for(Study study : studyService.findByOwner(defaultUser)) {
//			// Study cloneStudy = 
//			studyService.copy(study, study.getName(), user);
//		}
	}

	@Override
	@Transactional
	public void copyStudyToUser(Integer studyId, String email) 
		throws NullPointerException {
		User user = findByEmail(email);
		Study study = studyDao.findById(studyId);
		
		Project project = findDefaultProjectOfUser(user);
		
		if(project == null)
		{
			throw new NullPointerException(
					"no default project for user " + 
					email + 
					" found or user does not have any projects!");
		}
		
		studyService.copy(study, study.getName(), project);
	}

	@Transactional
	public void changePassword(User user, String password) {
		if(password == null) {
			user.setPassword(null);
		} else {
			user.setPasswordMD5(password);
		}
		userDao.save(user);
	}

	@Override
	public boolean validatePassword(User user, String password) {
		if(password == null || user.getPassword() == null) {
			return false;
		}
		String md5password = user.getPasswordMD5(password);
		return user.getPassword().equals(md5password);
	}

	
	@Override
	public User findById(Integer userId) {
		return userDao.findById(userId);
	}
	
	public User findByEmail(String email) {
		return userDao.findByEmail(email.toLowerCase());
	}

	@Override
	public void sendInvitationMail(User owner, String email, Locale locale, MailMessageProvider provider) {
		email = email.toLowerCase();
		Invitation invitation = createInvitation(owner, email, locale);
		String securityToken = securityTokenService.createToken(invitation, SecurityTokenType.INVITE_USER);

		sendMail(email, locale, provider, securityToken);
	}

	@Override
	public void sendRegisterMail(String email, Locale locale, MailMessageProvider provider) {
		email = email.toLowerCase();
		Invitation invitation = createInvitation(null, email, locale);
		String securityToken = securityTokenService.createToken(invitation, SecurityTokenType.VERIFY_USER);

		sendMail(email, locale, provider, securityToken);
	}

	private void sendMail(String email, MailMessageProvider provider, String securityToken) {
		sendMail(email, null, provider, securityToken);
	}
		
	private void sendMail(String email, Locale locale, MailMessageProvider provider, String securityToken) {
		provider.onAttach(locale);

		String urlStr = provider.toUrl(securityToken);
		logger.info("URL to register: "+urlStr);

		try {
			URL url = new URL(urlStr);
			String host = url.getHost();
			
			mailService.sendMail(
					email,
					provider.getMailSubject(),
					provider.getMailMessage(urlStr), 
					//"info@"+host);
					"info@userweave.net");
			
		} catch (MessagingException e) {
			logger.error("Error sending an email",e);
		} catch (MalformedURLException e) {
			logger.error("Error sending an email",e);
		}
		provider.onDetach();
	}

	@Override
	public void sendResetPasswordMail(User user, MailMessageProvider provider) {
		String securityToken = securityTokenService.createToken(user,SecurityTokenType.RESET_PASSWORD);
		
		sendMail(user.getEmail(), provider, securityToken);
	}

	
//	@Override
//	@Transactional
//	public boolean purge(String email) {
//		User user = userDao.findByEmail(email);
//		if(user == null) {
//			return false;
//		}
//		purgeUser(user);
//		return true;
//	}

//	@Override
//	@Transactional
//	public void purgeUser(User user) {
//
//		logService.deleteLogEntries(user);
//		
//		studyService.purge(user);
//		
//		userDao.delete(user);
//	}

	@Override
	public void activate(User user) {
		user.setDeactivatedAt(null);
		userDao.save(user);
	}

	@Override	
	public void deactivate(User user) { 
		user.setDeactivatedAt(new DateTime());
		userDao.save(user);		
	}

	@Override
	@Transactional
	public User validateToken(String code, SecurityTokenType ...securityTokenTypes) {
		SecurityToken token = securityTokenService.validateToken(code, securityTokenTypes);
		if(token == null) {
			return null;
		}
		User user = null;
		
		switch (token.getType()) {
			case RESET_PASSWORD:
				user = token.getUser();
				changePassword(user, null);
				break;
			case VERIFY_USER:
				Invitation invitation = token.getInvitation();
				user = createUser(invitation.getEmail(),invitation.getLocale());
				// now we can remove the invitation
				invitationDao.delete(invitation);
		default:
			break;
		}
		
		//securityTokenService.deleteToken(token);
		
		return user;
	}

	private Invitation createInvitation(User owner, String email, Locale locale) {
		Invitation invitation = new Invitation();
		invitation.setOwner(owner);
		invitation.setEmail(email);
		invitation.setLocale(locale);

		invitationDao.save(invitation);
		
		return invitation;
	}

	@Override
	public Project findDefaultProjectOfUser(User user)
	{
		List<Project> projects = projectDao.findByUser(user, false);
		
		Project firstFoundProject = null;
		
		Project defaultProject = null;
		
		boolean isFirstProject = true;
		
		for(Project project : projects)
		{
			if(isFirstProject)
			{
				firstFoundProject = project;
				isFirstProject = false;
			}
			
			if(project.getName() == "default project")
			{
				defaultProject = project;
				break;
			}
		}
		
		if(defaultProject != null)
		{
			return defaultProject;
		}
		else
		{
			return firstFoundProject;
		}
	}

}


