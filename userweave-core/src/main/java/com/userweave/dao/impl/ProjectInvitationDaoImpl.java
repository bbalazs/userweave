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
package com.userweave.dao.impl;

import java.util.List;
import java.util.Locale;

import org.apache.wicket.Component;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.userweave.application.UserWeaveSession;
import com.userweave.components.MailMessageProviderImpl;
import com.userweave.dao.ProjectInvitationDao;
import com.userweave.domain.Project;
import com.userweave.domain.ProjectInvitation;
import com.userweave.domain.Role;
import com.userweave.domain.User;
import com.userweave.domain.service.UserService;
import com.userweave.pages.user.RegisterUser;
import com.userweave.utils.LocalizationUtils;

@Repository
public class ProjectInvitationDaoImpl extends BaseDaoImpl<ProjectInvitation> implements
		ProjectInvitationDao
{
	@Autowired
	private UserService userService;
	
	@Override
	public void sendInvitation(User addresser, User addressee, Project project, Role role)
	{
		ProjectInvitation invitation = new ProjectInvitation();
		
		invitation.setAddressee(addressee);
		
		invitation.setEmail("");
		
		invitation.setAddresser(addresser);
		
		invitation.setProject(project);
		
		invitation.setRole(role);
		
		this.save(invitation);

	}
	
	@Override
	public void sendInvitation(String recipantEmail, User addresser,
			Project project, Role role, Locale locale, final Component localeStringProvider)
	{
		ProjectInvitation invitation = new ProjectInvitation();
		
		invitation.setAddressee(null);
		
		invitation.setEmail(recipantEmail);
		
		invitation.setAddresser(addresser);
		
		invitation.setProject(project);
		
		invitation.setRole(role);
		
		this.save(invitation);
		
		userService.sendInvitationMail(
			UserWeaveSession.get().getUser(), 
			recipantEmail, 
			locale, 
			new MailMessageProviderImpl(localeStringProvider) {
			
				@Override
				public String getUrl(Component component, String token, Locale locale) {
					
					PageParameters parameters = new PageParameters();
					parameters.set(0, token);
					parameters.set(1, LocalizationUtils.getLocaleShort(locale));
	
					String url =  RequestCycle.get().getUrlRenderer().renderFullUrl(
							   Url.parse(component.urlFor(RegisterUser.class,parameters).toString()));
					
					return url;
					
//					return  component.urlFor(RegisterUser.class, parameters).toString();
				}
				
				@Override
				public String getMailSubject() {
					User user = UserWeaveSession.get().getUser();
					
					return new StringResourceModel("subject_invite", localeStringProvider, null, 
							new Object[] {
								user.getForename(),
								user.getSurname()
							}).getString();
				}
				
				@Override
				public String getMailMessage(String urlStr) {
					User user = UserWeaveSession.get().getUser();
					
					return new StringResourceModel("mail_invite", localeStringProvider, null, new Object[] {
							user.getForename(),
							user.getSurname(),
							urlStr
						}).getString();
				}
		});
	}

	@Override
	public Class<ProjectInvitation> getPersistentClass()
	{
		return ProjectInvitation.class;
	}

	@Override
	public List<ProjectInvitation> findByRecipant(User recipant)
	{
		String query ="from " + getEntityName() + " where addressee = :addressee";
		
		Query q = getCurrentSession().createQuery(query).setParameter("addressee", recipant);
		
		return q.list();
	}
	
	@Override
	public List<ProjectInvitation> findByEmail(String email)
	{
		String query ="from " + getEntityName() + " where email = :email" +
				" and addressee_id is null";
		
		Query q = getCurrentSession().createQuery(query).setParameter("email", email);
		
		return q.list();
	}

	@Override
	public List<ProjectInvitation> findByProject(Project project)
	{
		String query ="from " + getEntityName() + " where project = :project";
		
		Query q = getCurrentSession().createQuery(query).setParameter("project", project);
		
		return q.list();
	}

	@Override
	public List<ProjectInvitation> findByRecipantAndProject(User recipant,
			Project project)
	{
		String query ="from " + getEntityName() + " where project = :project" +
				" and addressee = :addressee";
		
		Query q = getCurrentSession()
					.createQuery(query)
						.setParameter("project", project)
						.setParameter("addressee", recipant);
		
		return q.list();
	}

	@Override
	public List<ProjectInvitation> findByEmailAndProject(String email,
			Project project)
	{
		String query ="from " + getEntityName() + " where project = :project" +
		" and email = :email";

		Query q = getCurrentSession()
					.createQuery(query)
						.setParameter("project", project)
						.setParameter("email", email);
		
		return q.list();
	}
}
