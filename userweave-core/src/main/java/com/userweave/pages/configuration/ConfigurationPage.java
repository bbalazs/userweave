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

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.protocol.http.ClientProperties;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.protocol.http.request.WebClientInfo;
import org.apache.wicket.request.ClientInfo;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.application.UserWeaveSession;
import com.userweave.application.auth.AuthenticatedOnly;
import com.userweave.components.callback.EventHandler;
import com.userweave.components.callback.EventType;
import com.userweave.components.callback.IEntityEvent;
import com.userweave.components.navigation.breadcrumb.BreadcrumbPanel;
import com.userweave.dao.ProjectInvitationDao;
import com.userweave.dao.ProjectUserRoleJoinDao;
import com.userweave.dao.RoleDao;
import com.userweave.domain.EntityBase;
import com.userweave.domain.Project;
import com.userweave.domain.ProjectInvitation;
import com.userweave.domain.ProjectUserRoleJoin;
import com.userweave.domain.Role;
import com.userweave.domain.Study;
import com.userweave.domain.User;
import com.userweave.pages.base.BaseUserWeavePage;
import com.userweave.pages.components.ajaxindicator.AjaxIndicator;
import com.userweave.pages.components.studypanel.StudyPanel;
import com.userweave.pages.configuration.project.SelectProjectPanel;
import com.userweave.pages.configuration.project.overview.ProjectOverviewPanel;
import com.userweave.pages.homepage.NoIe6Support;

/**
 * The main configurations view for the 
 * usability methods configuration.
 * 
 * @author oma
 */
@AuthenticatedOnly
public class ConfigurationPage extends BaseUserWeavePage 
{
	private static final long serialVersionUID = 1L;

	@SpringBean
	private ProjectInvitationDao projectInvitationDao;
	
	@SpringBean
	private RoleDao roleDao;
	
	@SpringBean
	private ProjectUserRoleJoinDao purjDao;
	
	/**
	 * The users client properties. Used to kick out
	 * IE6 users.
	 */
	private ClientProperties clientProperties = null;
	
	/**
	 * The component to display on this panel. Changed by
	 * the callback, triggered by the breadcrumb. 
	 */
	private Component content;
	
	/**
	 * The wicket id for the content component.
	 */
	private static final String contentId = "content";
	
	/**
	 * Default constructor.
	 */
	public ConfigurationPage() 
	{
		checkAndAddPendingInvitations();
		
		content = createProjectOverviewPanel(null);
	
		add(content);
		
		replaceBreadcrumb(null, null);
		
		init();
	}
	
	/**
	 * TODO: find out for what this constructor is needed and
	 * 		 rebuild it. See API.
	 * 
	 * Initial display component is a study.
	 * 
	 * @param study
	 * 		Study to display.
	 */
	public ConfigurationPage(final Study study)
	{
		checkAndAddPendingInvitations();
		
		content = createStudyConfigurationPanel(study.getId());
		add(content);
		
		replaceBreadcrumb(study, null);
		
		init();
	}
	
	/**
	 * Initial display component is a project.
	 * 
	 * @param project
	 * 		Project to display.
	 */
	public ConfigurationPage(final Project project)
	{
		UserWeaveSession.get().setProjectId(project.getId());
		
		checkAndAddPendingInvitations();
		
		content = createSelectProjectPanel(null);
		
		add(content);
		
		replaceBreadcrumb(project, null);
		
		init();
	}
	
	/**
	 * Corrects ajax bug if page is loaded form cache.
	 * 
	 * TODO: check, if this still holds with wicket 1.5
	 * 
	 * @see http://www.richardnichols.net/2010/03/apache-wicket-force-page-reload-to-fix-ajax-back/
	 */
	@Override
	protected void configureResponse(WebResponse response)
	{
		super.configureResponse(response);
		
		response.disableCaching();
	}
	
	@Override
	public void renderHead(IHeaderResponse response)
	{
		super.renderHead(response);
		
		appendColorPickerInitializer(response);
	}
	
	private void appendColorPickerInitializer(IHeaderResponse response)
	{
		/*
		 * This schould be only accessed, if a user is
		 * authorized, that is, if the current user
		 * has role admin or participant. Because we 
		 * haven't any project here to get the rights form
		 * and a guest can't trigger the icon upload form in
		 * StudyConfigurationPanel, we simply append the script
		 * here.
		 * 
		 * This script is needed, if a user submits an upload form.
		 * This refreshes the page, because resources aren't ajax-uploadable.
		 * 
		 * TODO: Check, if till needed.
		 */
		response.renderJavaScriptReference(new JavaScriptResourceReference(
				ConfigurationPage.class, "usability_methods_init_scripts.js"));
	}
	
	/**
	 * Lookup pending invitations to projects
	 * where only e-mail address is set and no
	 * user. If one exists, this means that the
	 * user has just finished the register process
	 * and must accept all invitations.
	 */
	private void checkAndAddPendingInvitations()
	{
		User user = UserWeaveSession.get().getUser();
		
		List<ProjectInvitation> invitations = 
			projectInvitationDao.findByEmail(user.getEmail());
	
		for(ProjectInvitation invitation : invitations)
		{
			ProjectUserRoleJoin newJoin = 
				purjDao.createJoin(
						invitation.getProject(), 
						user, 
						roleDao.findByName(invitation.getRole().getRoleName()));
			
			purjDao.save(newJoin);
			
			projectInvitationDao.delete(invitation);
		}
	}
	
	/**
	 * Check, if users uses IE6. If so, bump him to
	 * the no IE6 error page.
	 */
	public void init()
	{
		// we are back on the homepage so we can reset this value
		UserWeaveSession.get().setSessionOrigin(null);
		
		if (isIe6()) 
		{	
			UserWeaveSession.get().setUserId(null);
			setResponsePage(NoIe6Support.class);
		}
	}
	
	/**
	 * Check, if users uses IE6.
	 * 
	 * @return boolean
	 * 		True, if users client identifies as IE6
	 */
	protected boolean isIe6() 
	{
		return (getClientProperties().isBrowserInternetExplorer() 
				&& getClientProperties().getBrowserVersionMajor() == 6);
	}
	
	/**
	 *  Shortcut to the client properties.
	 *  
	 *  @return ClientProperties
	 *  	The client properties of the users browser.
	 */
	protected ClientProperties getClientProperties()
	{
		if( clientProperties == null )
		{
			ClientInfo clientInfo = WebSession.get().getClientInfo();	      
			if( clientInfo == null || !(clientInfo instanceof WebClientInfo) )
			{
				clientInfo = new WebClientInfo( getRequestCycle() );
				WebSession.get().setClientInfo( clientInfo );
			}
			clientProperties = ((WebClientInfo) clientInfo).getProperties();
		}
		
		return(clientProperties);
	}
	
	/**
	 * Adds the navigation and the information panel
	 * to the page. Also adds the ajax indicator.
	 */
	@Override
	protected void addHeaderComponents(
		final EntityBase entity, final EventHandler callback)
	{
		super.addHeaderComponents(entity, callback);
		
		add(new AjaxIndicator("ajaxIndicator"));
	}
	
	/**
	 * Replacing the content with a ProjectOverviewPanel.
	 * 
	 * @param target
	 * 		AjaxRequestTarget
	 */
	protected void replaceContentWithProjectOverviewPanel(AjaxRequestTarget target, Integer preSelectedTab)
	{
		replaceContent(createProjectOverviewPanel(preSelectedTab), target);
	}
	
	/**
	 * Replacing the content with a ProjectOverviewPanel.
	 * 
	 * @param target
	 * 		AjaxRequestTarget
	 * @param preSelectedTab
	 * 		Tab to preselect.	 
	 */
	protected void replaceContentWithSelectProjectPanel(AjaxRequestTarget target, Integer preSelectedTab)
	{
		replaceContent(createSelectProjectPanel(preSelectedTab), target);
	}

	/**
	 * Replacing the content with a StudyConfigurationTabPanel.
	 * 
	 * @param target
	 * 		AjaxRequestTarget
	 * @param sutdyId
	 * 		The id of the study to load.	 
	 */
	protected void replaceContentWithStudyConfigurationPanel(AjaxRequestTarget target, int studyId) 
	{
		replaceContent(createStudyConfigurationPanel(studyId), target);
	}
		
	/**
	 * Convenient method to replace the displayed component.
	 * 
	 * @param replacement
	 * 		The replacement component
	 * @param target
	 * 		AjaxRequestTarget
	 */
	protected void replaceContent(Component replacement, AjaxRequestTarget target) 
	{	
		content.replaceWith(replacement);
		content = replacement;
		
		if (target != null) 
		{
			target.add(content);
		}
	}
	
	/**
	 * Convenient method to replace the current breadcrumb with 
	 * a fresh one.
	 * 
	 * @param replacement
	 * 		The replacement component
	 * @param target
	 * 		AjaxRequestTarget
	 */
	protected void replaceBreadcrumb(EntityBase entity, AjaxRequestTarget target) 
	{	
		BreadcrumbPanel newCrumb = 
				new BreadcrumbPanel("breadcrumb", entity, getProjectEventHandler());
		
		newCrumb.setOutputMarkupId(true);
		
		getBreadcrumb().replaceWith(newCrumb);
		setBreadcrumb(newCrumb);
		
		if (target != null) 
		{
			target.add(getBreadcrumb());
		}
	}
	
	/**
	 * Creates and returns a ProjectOverviewPanel.
	 * 
	 * @see com.userweave.pages.configuration.project.overview.ProjectOverviewPanel
	 * @return
	 * 		A ProjectOverviewPanel.
	 */
	protected Component createProjectOverviewPanel(Integer preSelectedTab)
	{
		ProjectOverviewPanel panel;
		
		if(preSelectedTab == null)
		{
			panel = new ProjectOverviewPanel(contentId, getProjectEventHandler());
		}
		else
		{
			panel = new ProjectOverviewPanel(contentId, getProjectEventHandler(), preSelectedTab);
		}
	
		panel.setOutputMarkupId(true);
		
		return panel;
	}
	
	/**
	 * Creates and returns a SelectProjectPanel with
	 * the project given by the projectId.
	 * 
	 * @param projectId
	 * 		Project id from project to load.
	 * @return
	 * 	A SelectProjectPanel.
	 */
	private Component createSelectProjectPanel(Integer preSelectedTab) 
	{
		SelectProjectPanel panel;
		
		if(preSelectedTab == null)
		{
			panel = new SelectProjectPanel(
				contentId, 
				getProjectEventHandler(),
				getStudyEventHandler());
		}
		else
		{
			panel = new SelectProjectPanel(
				contentId, 
				preSelectedTab, 
				getProjectEventHandler(), 
				getStudyEventHandler());
		}
	
		panel.setOutputMarkupId(true);
		
		return panel;
	}
	
	/**
	 * Creates and returns a StudyConfigurationTabPanel with
	 * the study given by the studyId.
	 * 
	 * @param studyId
	 * 		Study id from study to load.
	 * @return
	 * 	A StudyConfigurationTabPanel.
	 */
	private Component createStudyConfigurationPanel(final int studyId) 
	{		
		StudyPanel panel = new StudyPanel(
				contentId, studyId, getStudyEventHandler());
		
		panel.setOutputMarkupId(true);
		
		return panel;
	}
		
	
	
	/**
	 * Get an event handler for projects.
	 * 
	 * @return
	 */
	protected EventHandler getProjectEventHandler()
	{
		EventHandler callback = new EventHandler()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean onEvent(IEntityEvent event)
			{
				if (event.getType() == EventType.Selected)
				{
					
					// Redirect to either my projects or to
					// public projects.
					if(event.getEntity() == null)
					{
						// back on project selection panel
						UserWeaveSession.get().setProjectId(null);
						
						replaceBreadcrumb(null, event.getTarget());
						
						replaceContentWithProjectOverviewPanel(event.getTarget(), null);
						
						return true;
					}
					
					if (event.getEntity() instanceof Project)
					{
						// set current project to selected project
						Integer id = ((Project)event.getEntity()).getId();
						UserWeaveSession.get().setProjectId(id);
						
						replaceBreadcrumb(event.getEntity(), event.getTarget());
						
						replaceContentWithSelectProjectPanel(event.getTarget(), null);
						
						return true;
					}
				}
				
				if(event.getType() == EventType.Update)
				{
//					if(event.getEntity() instanceof Study)
//					{
//						replaceContentWithStudyConfigurationPanel(
//								event.getTarget(), ((Study)event.getEntity()).getId());
//						return true;
//					}
					
					replaceBreadcrumb(event.getEntity(), event.getTarget());
					
					return true;
				}
				
				if(event.getType() == EventType.Purge)
				{
					// back on project selection panel
					UserWeaveSession.get().setProjectId(null);
					
					replaceBreadcrumb(null, event.getTarget());
					
					replaceContentWithProjectOverviewPanel(event.getTarget(), null);
					
					return true;
				}
				
				return false;
			}
		};
		
		return callback;
	}
	
	/**
	 * Get an event handler for studies.
	 * 
	 * @return
	 */
	protected EventHandler getStudyEventHandler()
	{
		EventHandler callback = new EventHandler()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean onEvent(IEntityEvent event)
			{
				/* Triggered, if a study schould be previewed and
				 * there is no project admin, who is registered.
				 * 
				 * if user = admin
				 * 		redirect to project overview page and select my account tab
				 * 
				 * else
				 * 		redirect to select project panel and select active user tab
				 */
				if(event.getEntity() instanceof User)
				{
					User user = (User)event.getEntity();

					if(user.hasRole(Role.PROJECT_ADMIN))
					{
						/** FIXME: replace breadcrumb panel */
						replaceContentWithProjectOverviewPanel(
								event.getTarget(), ProjectOverviewPanel.MY_ACCOUNT_TAB);
					}
					else
					{
						/** FIXME: replace breadcrumb panel */
						replaceContentWithSelectProjectPanel(
								event.getTarget(), SelectProjectPanel.ACTIVE_USERS_TAB);
					}
					
					return true;
				}
				
				// set view to study configuration overview
				if(event.getType() == EventType.Selected)
				{
					Study study = (Study)event.getEntity();
					
					// set current project to selected study's parent project
					Project parrentProject = study.getParentProject();
					
					if(parrentProject != null)
					{
						UserWeaveSession.get().setProjectId(parrentProject.getId());
						
						replaceBreadcrumb(event.getEntity(), event.getTarget());
						
						replaceContentWithStudyConfigurationPanel(
								event.getTarget(), study.getId());
						
						return true;
					
					}
					
					return false;
				}
				
				// reset view to select project panel
				// NOTICE: Purge is handeled before this call.
				if(event.getType() == EventType.Purge)
				{
					Study study = (Study) event.getEntity();
					
					replaceBreadcrumb(study.getParentProject(), event.getTarget());
					
					replaceContentWithSelectProjectPanel(event.getTarget(), null);
					
					return true;
				}
				
				// update breadcrumb panel to display updated study name.
				if(event.getType() == EventType.Update)
				{
					replaceBreadcrumb(event.getEntity(), event.getTarget());
					
					return true;
				}
				
				return false;
			}
		};
		
		return callback;
	}

//  FIXME: Delete someday, if event handler are finished
//	/**
//	 * 
//	 * 
//	 * Returns an event handler to process an event from 
//	 * either the breadcrumb or a link from the content
//	 * 
//	 * @return EventHandler
//	 * 		The event handler to attach to links.
//	 */
//	@Deprecated
//	protected EventHandler getEventHandler()
//	{
//		EventHandler callback = new EventHandler()
//		{
//			@Override
//			public boolean onEvent(IEntityEvent event) 
//			{
//				if (event.getType() == EventType.Selected)
//				{
//					/*
//					 * Home link in breadcrumb
//					 */
//					if(event.getEntity() == null)
//					{
//						// back on project selection panel
//						UserWeaveSession.get().setProjectId(null);
//						
//						replaceBreadcrumb(null, event.getTarget(), true);
//						
//						replaceContentWithProjectOverviewPanel(event.getTarget());
//
//						return true;
//					}
//					
//					if(event.getEntity() instanceof Project)
//					{
//						// set current project to selected project
//						Integer id = ((Project)event.getEntity()).getId();
//						UserWeaveSession.get().setProjectId(id);
//						
//						replaceBreadcrumb(event.getEntity(), event.getTarget(), true);
//						
//						replaceContentWithSelectProjectPanel(event.getTarget(), id);
//						
//						return true;
//					}
//					
//					if(event.getEntity() instanceof Study)
//					{
//						// set current project to selected study's parent project
//						Integer projectId = null;
//						
//						Project parrentProject = ((Study)event.getEntity()).getParentProject();
//						
//						if(parrentProject != null)
//						{
//							projectId = parrentProject.getId();
//						}
//						
//						UserWeaveSession.get().setProjectId(projectId);
//						
//						replaceBreadcrumb(event.getEntity(), event.getTarget(), false);
//						
//						replaceContentWithStudyConfigurationPanel(
//								event.getTarget(), ((Study)event.getEntity()).getId(), true);
//						
//						return true;
//					}
//					
//					if(event.getEntity() instanceof ModuleConfiguration)
//					{
//						// we only display study deep
//						Study study = ((ModuleConfiguration) event.getEntity()).getStudy();
//						
//						replaceBreadcrumb(study, event.getTarget(), false);
//						
//						replaceContentWithModuleConfigurationPanel(
//								event.getTarget(), ((ModuleConfiguration)event.getEntity()));
//						
//						return true;
//					}
//					
//					if(event.getEntity() instanceof Question)
//					{
//						// we only display study deep
//						Study study = ((Question) event.getEntity()).getConfiguration().getStudy();
//						
//						replaceBreadcrumb(study, event.getTarget(), false);
//						
//						replaceContentWithQuestionConfigurationPanel(
//								event.getTarget(), (Question)event.getEntity());
//						
//						return true;
//					}
//				}
//				
//				if(event.getType() == EventType.Update)
//				{
//					if(event.getEntity() instanceof Study)
//					{
//						replaceContentWithStudyConfigurationPanel(
//								event.getTarget(), ((Study)event.getEntity()).getId(), true);
//						return true;
//					}
//				}
//				
//				if(event.getType() == EventType.Purge)
//				{
//					
//				}
//				
//				if(event.getType() == EventType.Create)
//				{
//					
//				}
//				
//				return false;
//			}
//		};
//		
//		return callback;
//	}
}

