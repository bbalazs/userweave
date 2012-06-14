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
package com.userweave.pages.configuration.editentitypanel.copydialog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.application.UserWeaveSession;
import com.userweave.components.customModalWindow.BaseModalWindowPage;
import com.userweave.dao.ProjectDao;
import com.userweave.dao.RoleDao;
import com.userweave.dao.StudyDao;
import com.userweave.domain.Project;
import com.userweave.domain.Role;
import com.userweave.domain.Study;
import com.userweave.domain.StudyState;
import com.userweave.domain.service.ModuleService;
import com.userweave.module.ModuleConfiguration;
import com.userweave.module.ModuleConfigurationImpl;
import com.userweave.module.methoden.questionnaire.domain.QuestionnaireConfigurationEntity;

/**
 * Web page to display a series of browser panels.
 * 
 * @author opr
 *
 */
public abstract class BrowseEntityWebPage extends BaseModalWindowPage
{	
	@SpringBean
	private StudyDao studyDao;
	
	@SpringBean
	private ProjectDao projectDao;
	
	@SpringBean
	private ModuleService moduleService;
	
	@SpringBean
	private RoleDao roleDao;
	
	/**
	 * The selected project.
	 */
	private Project selectedProject;
	
	/**
	 * The selected study.
	 */
	private Study selectedStudy;
	
	/**
	 * The selected module
	 */
	private ModuleConfigurationImpl<?> selectedModule;
	
	/**
	 * The name of the entity to copy.
	 */
	private String copyName;
	
	private void setCopyName(String name)
	{
		this.copyName = name;
	}
	
	private final FeedbackPanel feedback;
	
	/**
	 * Default constructor
	 * 
	 * @param copyEntityName
	 * 		Name for the copy entity.
	 */
	public BrowseEntityWebPage(
			String copyEntityName, 
			final ModalWindow window, 
			final int numberOfRequiredFields)
	{	
		super(window);
		
		setCopyName(copyEntityName);
		
		
		feedback = new FeedbackPanel("feedback");
		feedback.setOutputMarkupId(true);
		
		addToForm(feedback);
		
		
		int requiredFields = numberOfRequiredFields > 1 ? numberOfRequiredFields : 1;
		
		List<Project> projects;
		
		if(UserWeaveSession.get().getUser().isAdmin())
		{
			projects = projectDao.findAllNotDeleted();
		}
		else
		{
			projects = 
				projectDao.findByUser(
					UserWeaveSession.get().getUser(), 
					false,
					roleDao.findByNames(Role.PROJECT_ADMIN, Role.PROJECT_PARTICIPANT));
		}
		
		
		final BrowserPanel<Project> projectPanel = getProjectsBrowser(requiredFields, projects);
		final BrowserPanel<Study> studyPanel = getStudiesBrowser(requiredFields - 1);
		final BrowserPanel<ModuleConfigurationImpl<?>> modulePanel = getModuleBrowser(requiredFields - 2);
		//final BrowserPanel<Question> questionPanel = getQuestionBrowser(requiredFields - 3);
		
		if(projects.contains(UserWeaveSession.get().getProject()))
		{
			// preselect current project
			Project currentProject = UserWeaveSession.get().getProject();
			this.selectedProject = currentProject;
			projectPanel.setSelectedItem(currentProject);
			
			// load studies, if nessesary
			if(requiredFields - 1 > 0)
			{
				studyPanel.resetDisplayList(
					studyDao.findByProjectAndState(currentProject, false, StudyState.INIT));
			}
		}
	
		studyPanel.setOutputMarkupId(true);
		modulePanel.setOutputMarkupId(true);
		//questionPanel.setOutputMarkupId(true);
		
		projectPanel.setRightSibling(studyPanel);
		studyPanel.setRightSibling(modulePanel);
		//modulePanel.setRightSibling(questionPanel);
		modulePanel.setRightSibling(null);
		
		
		addToForm(projectPanel);		
		addToForm(studyPanel);
		addToForm(modulePanel);
		//form.add(questionPanel);
		
		addToForm(new TextField(
			"name", new PropertyModel(BrowseEntityWebPage.this, "copyName")));

	}

	@Override
	protected WebMarkupContainer getAcceptButton(String componentId,
			final ModalWindow window)
	{
		return new AjaxButton(componentId, getForm())
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form form)
			{
				BrowseEntityWebPage.this.onCopy(
					copyName, selectedProject, selectedStudy, selectedModule);
				
				window.close(target);
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, Form form)
			{
				target.addComponent(feedback);
			}
		};
	}
	
	@Override
	protected IModel getAcceptLabel()
	{
		return new StringResourceModel("copy", this, null);
	}
	
	/**
	 * Get a BrowserPanel for projects.
	 * 
	 * @return
	 */
	private BrowserPanel<Project> getProjectsBrowser(
		final int numberOfRequiredFields, List<Project> projects)
	{
		final BrowserPanel<Project> projectPanel = 
			new BrowserPanel<Project>(
				"projects", 
				projects, 
				numberOfRequiredFields > 0, 
				new StringResourceModel("project", BrowseEntityWebPage.this, null))
		{
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("rawtypes")
			@Override
			protected void onClick(
				AjaxRequestTarget target, Project entity, BrowserPanel<?> rightSibling)
			{
				// if a module or a question hast to be moved,
				// update right sibling
				if(numberOfRequiredFields > 1)
				{
					rightSibling.resetDisplayList(
							studyDao.
							findByProjectAndState(entity, false, StudyState.INIT));
					
					// reset other display list
					BrowserPanel<?> rightSiblingSibling = rightSibling.getRightSibling();
					rightSiblingSibling.resetDisplayList(new ArrayList());
					
//					BrowserPanel<?> last = rightSiblingSibling.getRightSibling();
//					last.resetDisplayList(new ArrayList());
					
					target.addComponent(rightSibling);
					target.addComponent(rightSiblingSibling);
//					target.addComponent(last);
				}
				
				// save selected project
				BrowseEntityWebPage.this.selectedProject = entity;
			}

			@Override
			protected String getEntityName(Project entity)
			{
				return entity.getName();
			}
		};
		
		return projectPanel;
	}
	
	/**
	 * Get a BrowserPanel for studies
	 * 
	 * @return
	 */
	private BrowserPanel<Study> getStudiesBrowser(final int numberOfRequiredFields)
	{
		final BrowserPanel<Study> studyPanel = 
			new BrowserPanel<Study>(
					"studies", 
					new ArrayList<Study>(), 
					numberOfRequiredFields > 0, 
					new StringResourceModel("study", BrowseEntityWebPage.this, null))
		{
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("rawtypes")
			@Override
			protected void onClick(AjaxRequestTarget target, Study entity,
					BrowserPanel<?> rightSibling)
			{
				if(numberOfRequiredFields > 1)
				{
					// find all modules which are questionnaires
					List<ModuleConfiguration> modules = 
						moduleService.getModuleConfigurationsForStudy(entity);
					
					List<ModuleConfiguration> questionnaires = 
						new ArrayList<ModuleConfiguration>();
					
					Iterator<ModuleConfiguration> i = modules.iterator();
					
					while(i.hasNext())
					{
						ModuleConfiguration module = i.next();
						
						if(module instanceof QuestionnaireConfigurationEntity)
						{
							questionnaires.add(module);
						}
					}
					
					rightSibling.resetDisplayList(questionnaires);
					
//					BrowserPanel<?> rightSiblingSibling = rightSibling.getRightSibling();
//					rightSiblingSibling.resetDisplayList(new ArrayList());
					
					target.addComponent(rightSibling);
					//target.addComponent(rightSiblingSibling);
				}
				
				BrowseEntityWebPage.this.selectedStudy = entity;
			}

			@Override
			protected String getEntityName(Study entity)
			{
				return entity.getName();
			}
			
			@Override
			public boolean isVisible()
			{
				return numberOfRequiredFields > 0;
			}
		};
		return studyPanel;
	}

	/**
	 * Get a BrowserPanel for module configurations
	 * 
	 * @return
	 */
	private BrowserPanel<ModuleConfigurationImpl<?>> getModuleBrowser(final int numberOfRequiredFields)
	{
		final BrowserPanel<ModuleConfigurationImpl<?>> modulePanel =
			new BrowserPanel<ModuleConfigurationImpl<?>>(
					"modules", 
					new ArrayList<ModuleConfigurationImpl<?>>(), 
					numberOfRequiredFields > 0, 
					new StringResourceModel("module", BrowseEntityWebPage.this, null))
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(AjaxRequestTarget target,
					ModuleConfigurationImpl entity, BrowserPanel rightSibling)
			{
//				List list;
//				
//				if(entity instanceof QuestionnaireConfigurationEntity)
//				{
//					QuestionnaireConfigurationEntity q = configurationDao.findById(entity.getId());
//					
//					list = q.getQuestions();
//				}
//				else
//				{
//					list = new ArrayList();
//				}
//				
//				rightSibling.resetDisplayList(list);
				
				BrowseEntityWebPage.this.selectedModule = entity;
				
				//target.addComponent(rightSibling);
			}

			@Override
			protected String getEntityName(ModuleConfigurationImpl entity)
			{
				return entity.getName();
			}
			
			@Override
			public boolean isVisible()
			{
				return numberOfRequiredFields > 0;
			}
		};
		return modulePanel;
	}
	
	/**
	 * Get a BrowserPanel for Questions.
	 * 
	 * @return
	 */
//	private BrowserPanel<Question> getQuestionBrowser(final int numberOfRequiredFields)
//	{
//		final BrowserPanel<Question> questionPanel = 
//			new BrowserPanel<Question>(
//					"questions", 
//					new ArrayList<Question>(), 
//					numberOfRequiredFields > 0, 
//					new StringResourceModel("question", BrowseEntityWebPage.this, null))
//		{
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			protected void onClick(AjaxRequestTarget target,
//					Question entity, BrowserPanel rightSibling)
//			{
//				// Do nothing
//			}
//
//			@Override
//			protected String getEntityName(Question entity)
//			{
//				return entity.getName().getValue(
//						entity.getConfiguration().getStudy().getLocale());
//			}
//		};
//		return questionPanel;
//	}

	/**
	 * Copies the entity to its destiny.
	 * 
	 * @param copyEntity
	 * 		The entity to copy.
	 * @param copyName
	 * @param destinyProject
	 * @param destinyStudy
	 * @param configuration
	 */
	protected abstract void onCopy(
			String copyName, 
			Project destinyProject, 
			Study destinyStudy,
			ModuleConfiguration configuration);
}
