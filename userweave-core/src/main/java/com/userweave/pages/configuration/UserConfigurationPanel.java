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
package com.userweave.pages.configuration;

import java.util.List;

import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.dao.ProjectDao;
import com.userweave.dao.RoleDao;
import com.userweave.dao.UserDao;
import com.userweave.domain.Project;
import com.userweave.domain.Role;
import com.userweave.domain.User;

/**
 * @author oma
 */
@SuppressWarnings("serial")
public class UserConfigurationPanel extends Panel {

	@SpringBean
	private UserDao userDao;
	
	@SpringBean
	private ProjectDao projectDao;
	
	@SpringBean
	private RoleDao roleDao;
	
	private Integer userId;

	public UserConfigurationPanel(String id, int userId, Integer projectId) {
		super(id);
		init(userId, projectId);
	}
	
	public UserConfigurationPanel(String id, User user, Integer projectId) 
	{
		super(id);
		init(user.getId(), projectId);
	}

	private void init(final int id, final Integer projectId) {
		this.userId = id;
		
		setDefaultModel(
			new LoadableDetachableModel() {

				@Override
				protected Object load() 
				{
					User user = userDao.findById(userId);
					
					if(projectId != null)
					{
						Project project = projectDao.findById(projectId);
						
						// TODO: Think about, that a user may contain rights for 
						// another project in this project.
						List<Role> roles = roleDao.getRolesByUserAndProject(user, project);
						
						Roles role = new Roles();
						
						for(Role r : roles)
						{
							role.add(r.getRoleName());
						}
						
						user.setCurrentProjectRoles(role);
					}
					
					return user;
				}
			}
		);
	}
	
	protected User getUser() {
		return (User) getDefaultModelObject();
	}
	
	protected int getUserId() {
		return getUser().getId();
	}
	
	protected void setUserId(Integer userId) {
		this.userId = userId;
	}
}

