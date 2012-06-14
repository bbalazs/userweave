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
package com.userweave.pages.configuration.project.invitation;

import java.util.List;
import java.util.Locale;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.application.UserWeaveSession;
import com.userweave.components.localerenderer.LocaleChoiceRenderer;
import com.userweave.dao.RoleDao;
import com.userweave.domain.Role;
import com.userweave.domain.User;
import com.userweave.module.methoden.questionnaire.page.conf.question.feedbackl.CustomFeedbackPanel;
import com.userweave.utils.LocalizationUtils;

/**
 * Panel to invite a new user to the UserWeave platform.
 * 
 * @author opr
 *
 */
public class InvitationFormPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	@SpringBean
	private RoleDao roleDao;
	
	/**
	 *  the selected role for the addressee. 
	 */
	private final Role role;
	
	public Role getRole()
	{
		return role;
	}
	
	
	/**
	 * The addressee email address.
	 */
	private String invitaitonAddressee;
	
	public String getInvitaitonAddressee()
	{
		return invitaitonAddressee;
	}

	
	/**
	 * The selected locale for the addresse.
	 */
	private final Locale selectedLocale;
	
	public Locale getSelectedLocale()
	{
		return selectedLocale;
	}
	
	
	/**
	 * Feedback panel for error messages.
	 */
	private FeedbackPanel feedbackPanel;
	
	public FeedbackPanel getFeedbackPanel()
	{
		return feedbackPanel;
	}
	
	/**
	 * Default constructor.
	 * 
	 * @param id
	 * 		Markup id of component.
	 */
	public InvitationFormPanel(String id)
	{
		super(id);
		
		addAddressee();
		
		addFeedback();
		
		
		List<Role> roles = roleDao.findAll();
		
		role = getUserRole(roles);
		
		addRoleSelection(roles);
		
		
		selectedLocale = UserWeaveSession.get().getUser().getLocale();
		
		addLocaleChoice();
	}
	
	/**
	 * Adds a text field for the email address the invitation
	 * has to be send to.
	 */
	private void addAddressee()
	{
		TextField addressee = new TextField("emailAddress", 
				new PropertyModel(InvitationFormPanel.this, 
				"invitaitonAddressee"));
		
		addressee.setRequired(true);
		
		add(addressee);
	}

	/**
	 * Adds a feedback panel for error messages.
	 */
	private void addFeedback()
	{
		feedbackPanel = new CustomFeedbackPanel("feedback");
		
		feedbackPanel.setOutputMarkupId(true);
		
		add(feedbackPanel);
	}
	
	/**
	 * Adds a selection for the role the invitee will have
	 * in the project.
	 */
	private void addRoleSelection(List<Role> roles)
	{
		if(! UserWeaveSession.get().getUser().hasRole(Role.PROJECT_ADMIN))
		{
			// only admins can give other users admin rights
			for(Role role : roles)
			{
				if(role.getRoleName().equals(Role.PROJECT_ADMIN))
				{
					roles.remove(role);
					break;
				}
			}
		}
			
		DropDownChoice roleChoice = 
			new DropDownChoice(
				"roles", 
				new PropertyModel(this, "role"), 
				roles, 
				getChoiceRenderer());
		
		add(roleChoice);
	}
	
	private Role getUserRole(List<Role> roles)
	{
		User user = UserWeaveSession.get().getUser();
		
		// ugliest hackety hack
		for(Role role : roles)
		{
			if(user.hasRole(role.getRoleName()))
			{
				return role;
			}
		}
		
		return null;
	}
	
	/**
	 * The locale for the config interface the invitee will
	 * have by default.
	 */
	private void addLocaleChoice()
	{
		add(new DropDownChoice(
			"locales", 
			new PropertyModel(InvitationFormPanel.this, "selectedLocale"),
			LocalizationUtils.getSupportedConfigurationFrontendLocales(),
			new LocaleChoiceRenderer(getLocale())));
	}
	
	/**
	 * Translator for Role names.
	 * 
	 * @return
	 */
	private IChoiceRenderer getChoiceRenderer()
	{
		return new IChoiceRenderer() 
		{	
			private static final long serialVersionUID = 1L;

			@Override
			public String getIdValue(Object object, int index) 
			{
				return ((Role) object).toString();
			}
			
			@Override
			public Object getDisplayValue(Object object) 
			{
				StringResourceModel srm = 
					new StringResourceModel(
							((Role) object).getRoleName(), InvitationFormPanel.this, null);
				
				return srm.getObject();
			}
		};
	}
}
