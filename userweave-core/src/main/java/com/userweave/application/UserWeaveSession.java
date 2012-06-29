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
package com.userweave.application;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.Session;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.userweave.components.SerializedPageLink;
import com.userweave.dao.ProjectDao;
import com.userweave.dao.RoleDao;
import com.userweave.domain.Group;
import com.userweave.domain.Project;
import com.userweave.domain.Role;
import com.userweave.domain.Study;
import com.userweave.domain.User;
import com.userweave.domain.LogEntry.LogEntryType;
import com.userweave.domain.service.LogService;
import com.userweave.domain.service.UserService;
import com.userweave.pages.api.CreateMockupStudyData;
import com.userweave.presentation.utils.WebClientHelper;
import com.userweave.utils.LocalizationUtils;

/**
 * Session object to contain needed infos.
 * 
 * @author opr
 *
 */
public class UserWeaveSession extends WebSession 
{
	private static final long serialVersionUID = 1L;

	@SpringBean
	private RoleDao roleDao;
	
	@SpringBean
	private ProjectDao projectDao;
	
	@SpringBean
	private UserService userService;
	
	@SpringBean 
	private LogService logService;
	
	private final static Logger logger = LoggerFactory.getLogger(UserWeaveSession.class);
	
	/**
	 * Available login states.
	 * 
	 * @author opr
	 */
	public static enum LoginState {
		INIT, AUTHENTICATED, PASSWORD_CHECKED, USER_VERIFIED, LOGGED_IN;
	};
	
	/**
	 * The current login state of the user.
	 */
	private LoginState loginState = LoginState.INIT;

	public void setLoginState(LoginState state) 
	{
		logger.info("Set LoginState from "+loginState+" to "+state);
		this.loginState = state;
		dirty();
	}
	
	public LoginState getLoginState() 
	{
		return loginState;
	}
	
	/**
	 * Default constructor,
	 * 
	 * @param request
	 *            The current request
	 */
	public UserWeaveSession(Request request) 
	{
		super(request);
		Injector.get().inject(this);
	}

	/**
	 * Get an instance of the session.
	 * 
	 * @return
	 */
	public static UserWeaveSession get() 
	{
		return (UserWeaveSession) Session.get();
	}
	
	public boolean isAuthenticated() 
	{
		return loginState.equals(LoginState.LOGGED_IN);
	}

	/**
	 * Id of user who is associated with this session.
	 */
	private Integer userId = null;

	// will be set to null after each request
	transient private User user;
	
	public void setUserId(Integer userId) 
	{
		if(userId == null) {
			setLoginState(LoginState.INIT);
			logService.log(LogEntryType.LOGOUT, getUser(), new WebClientHelper().getRemoteAddress());
			this.userId = null;
		} else {
			setLoginState(LoginState.AUTHENTICATED);
			this.userId=userId;
			setLocaleForUser();
			logService.log(LogEntryType.LOGIN, getUser(), new WebClientHelper().getRemoteAddress());
		}
		
		dirty();
	}

	public void invalidateUser()
	{
		user = null;
	}
	
	public User getUser() 
	{
		if(userId == null) 
		{
			return null;
		}
		
		if(user == null)
		{
			user = userService.findById(userId);
		}
	
		// load roles, if a user is in a project.
		if(projectId != null)
		{
			if(project == null)
			{
				project = projectDao.findById(projectId);
			}
			
			if(project != null)
			{
				Roles role = new Roles();
				
				if(user.isAdmin())
				{
					role.add(Role.PROJECT_ADMIN);
				}
				else
				{	
					List<Role> roles = roleDao.getRolesByUserAndProject(user, project);
					
					for(Role r : roles)
					{
						role.add(r.getRoleName());
					}
				}
				
				user.setCurrentProjectRoles(role);
			}
		}
		
		return user;
	}	
	
	/**
	 * Id of project the user currrently works on.
	 */
	private Integer projectId = null;
	
	// will be set to null after each request
	transient private Project project;
	
	public void setProjectId(Integer projectId)
	{
		this.projectId = projectId;
	}
	
	public void invalidateProject()
	{
		project = null;
	}
	
	public Project getProject()
	{
		if(projectId == null)
		{
			return null;
		}
		
		if(project == null)
		{
			project = projectDao.findById(projectId);
		}
		
		return project;
	}
	
	public boolean isAdmin() {
		User user = getUser();
		return user != null && 
			user.isAdmin();
	}
	
	public void setLocaleForUser() {
		User user = getUser();
		
		if(user != null && user.getLocale() != null) {
			setLocale(user.getLocale());
		} else {
			setLocale(LocalizationUtils.getDefaultLocale());
		}
	}
	
	/**
	 * Selected filters in a study.
	 * 
	 * Keys are the ids of the available studies.
	 * Values are a list of selected filters.
	 */
	private final Map<Integer, List<Group>> selectedGroups = 
		new HashMap<Integer, List<Group>>();

	public List<Group> getSelectedGroups(Integer studyId) 
	{
		return selectedGroups.get(studyId);
	}

	public void setSelectedGroups(Integer studyId, List<Group> selectedGroups) 
	{
		this.selectedGroups.put(studyId, selectedGroups);
	}
	
	// ConfigurationUI can be in full featured mode or only show report-relevant components
	private SerializedPageLink sessionOrigin = null;

	public void setSessionOrigin(SerializedPageLink sessionOrigin) {
		this.sessionOrigin = sessionOrigin;

		dirty();
	}

	public SerializedPageLink getSessionOrigin() {
		return sessionOrigin;
	}

	public boolean originFromSurvey() {
		return sessionOrigin != null &&
			sessionOrigin.getType() == SerializedPageLink.Type.SURVEY;
	}

	public boolean originFromReport() {
		return sessionOrigin != null && 
			sessionOrigin.getType() == SerializedPageLink.Type.REPORT;
	}

	// used for paypal redirect 
	private Study payPalRedirectStudy = null;
	
	public void setPayPalRedirectStudy(Study payPalRedirectStudy) {
		this.payPalRedirectStudy = payPalRedirectStudy;
		
		dirty();
	}

	public Study getPayPalRedirectStudy() 
	{
		return payPalRedirectStudy;
	}

	// used for api, if user is not registered yet
	private CreateMockupStudyData createMockupStudyData;
		
	public void setCreateMockupStudyData(CreateMockupStudyData createMockupStudyData) {
		this.createMockupStudyData = createMockupStudyData;
	}

	public CreateMockupStudyData getCreateMockupStudyData() {
		return createMockupStudyData;
	}
	
	/**
	 * If a user creates a new entity (project, study, etc.), 
	 * save the id of this entity. This is needed, because a 
	 * modal window does not preserve the state of the calling 
	 * page (and thus the panel which contains this id is gone). 
	 */
	private Integer createdEntityId;

	public Integer getCreatedEntityId()
	{
		return createdEntityId;
	}

	public void setCreatedEntityId(Integer createdEntityId)
	{
		this.createdEntityId = createdEntityId;
	}
	
	/**
	 * For the same reason as above, we need this var to
	 * start/stop a study, delete an entity, add a filter, etc.
	 */
	private boolean hasStateToBeChanged;

	public boolean isHasStateToBeChanged()
	{
		return hasStateToBeChanged;
	}

	public void setHasStateToBeChanged(boolean hasStateToBeChanged)
	{
		this.hasStateToBeChanged = hasStateToBeChanged;
	}
}
