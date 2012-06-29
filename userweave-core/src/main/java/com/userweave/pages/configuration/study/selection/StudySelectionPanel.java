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
package com.userweave.pages.configuration.study.selection;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.application.UserWeaveSession;
import com.userweave.components.callback.EntityEvent;
import com.userweave.components.callback.EventHandler;
import com.userweave.components.customModalWindow.CustomModalWindow;
import com.userweave.domain.ImageBase;
import com.userweave.domain.Project;
import com.userweave.domain.Role;
import com.userweave.domain.Study;
import com.userweave.domain.StudyState;
import com.userweave.domain.service.StudyService;
import com.userweave.pages.components.button.AddLink;

/**
 * Displays the project studies, ordered by
 * their state.
 *  
 * @author oma
 */
public class StudySelectionPanel extends Panel 
{
	private static final long serialVersionUID = 1L;

	/**
	 * A link, which is only visible, if a user
	 * has the given roles.
	 * 
	 * @author opr
	 */
	@AuthorizeAction(action = Action.RENDER, 
					 roles = {Role.PROJECT_ADMIN, Role.PROJECT_PARTICIPANT})
	private abstract class ProjectContributorLink extends AddLink
	{
		private static final long serialVersionUID = 1L;

		public ProjectContributorLink(String id, StringResourceModel labelModel)
		{
			super(id, labelModel);
		}
	}
	
	@SpringBean
	private StudyService studyService;
	
	/**
	 * Dialog to add a new study to this project.
	 */
	private ModalWindow addStudyModalWindow;
	
	/**
	 * Default constructor.
	 * 
	 * @param id
	 * 		Component id.
	 * @param project
	 * 		the project to which the studies belong.
	 * @param callback
	 * 		Callbacks to fire, if a link is clicked. 
	 * 		[0] Project event handler
	 * 		[1] Study event handler
	 */
	public StudySelectionPanel(String id, final Project project, final EventHandler... callbacks) 
	{
		super(id);
				
		createStudiesList(project, callbacks[1]);
		
		addAddStudyLinkAndModalWindow(project, callbacks[1]);
	}

	/**
	 * Fetch the studies of the given project for
	 * each study state.
	 * 
	 * @param project
	 * 		Project to fetch studies for.
	 * @param callback
	 * 		Study callback to fire on event.
	 */
	private void createStudiesList(Project project, final EventHandler callback) 
	{	
		List<Study> studiesInInit = 
			studyService.findByProjectAndState(project, false, StudyState.INIT);
		
		studyService.sort(studiesInInit);
		
		List<Study> studiesInRunning = 
			studyService.findByProjectAndState(project, false, StudyState.RUNNING);
		
		studyService.sort(studiesInRunning);

		List<Study> studiesInFinished = 
			studyService.findByProjectAndState(project, false, StudyState.FINISHED);
				
		studyService.sort(studiesInFinished);
		
		add(new Label("InitHeader", new StringResourceModel("init", this, null)));
		add(new StudyListPanel("studiesInInit", studiesInInit, callback));
		
		add(new Label("RunningHeader", new StringResourceModel("running", this, null)));
		add(new StudyListPanel("studiesInRunning", studiesInRunning, callback));
		
		add(new Label("FinishedHeader", new StringResourceModel("finished", this, null)));
		add(new StudyListPanel("studiesInFinished", studiesInFinished, callback));
				
		// add deleted studies to extra panel
		Label deleted = 
			new Label("DeletedHeader", new StringResourceModel("deleted", this, null));
		
		add(deleted);
		
		if(UserWeaveSession.get().isAdmin())
		{
			List<Study> deletedStudies = studyService.findDeletedStudies(project);
			
			studyService.sort(deletedStudies);
				
			add(new StudyListPanel("deletedStudies", deletedStudies, callback));
		}
		else
		{
			add(new WebMarkupContainer("deletedStudies"));
			
			deleted.setVisible(false);
		}
		
        
		// redirect to study, which the user want to pay
		if (UserWeaveSession.get().getPayPalRedirectStudy() != null) 
		{
			add(new AbstractDefaultAjaxBehavior() 
			{
				private static final long serialVersionUID = 1L;
				boolean isEnabled = true;
				
				@Override
				protected void respond(AjaxRequestTarget target) 
				{
					isEnabled = false;
					
					if (UserWeaveSession.get().getPayPalRedirectStudy() != null) 
					{
						// get study
						Study study = UserWeaveSession.get().getPayPalRedirectStudy();
						// reset Session
						UserWeaveSession.get().setPayPalRedirectStudy(null);
						//redirect to PayPal study
						EntityEvent.Selected(target, study ).fire(callback);
					}
				}
	
				@Override
				public void renderHead(Component component, IHeaderResponse response) 
				{
					super.renderHead(component, response);
					response.renderOnDomReadyJavaScript(getCallbackScript().toString());
				}
	
				@Override
				public boolean isEnabled(Component component)	
				{
					return isEnabled;
				}
			});
		}
	}
	
	/**
	 * Adds a dialog to create a new study.
	 * 
	 * @param project
	 * 		Project to add study to.
	 * @param callback
	 * 		Callback to fire, when a new study is created.
	 */
	private void addAddStudyLinkAndModalWindow(final Project project, final EventHandler callback) 
	{
		add(addStudyModalWindow = new CustomModalWindow("addStudyModalWindow"));
		
		addStudyModalWindow.setTitle(new StringResourceModel("create_new_study", this, null));
		
		addStudyModalWindow.setPageCreator(new ModalWindow.PageCreator() 
		{
			private static final long serialVersionUID = 1L;

			public Page createPage() {
            	
				Study study = studyService.preConfigureStudy(project);
                
				if(study.getLogo() == null)
				{
					// create logo
	                try
	        		{
	        			String path = "user_weaver_logo_kl.png";
	        			
	        			InputStream stream = 
	        				getClass().getResourceAsStream(path);
	        			
	        			if(stream != null)
	        			{
	        				byte[] data = new byte[10000];
	        			    stream.read(data);
	        				
	        			    ImageBase logo = new ImageBase();
	        			    logo.setImageData(data);
	        			    study.setLogo(logo);
	        			}
	        			else
	        			{
	        				study.setLogo(null);
	        			}
	        			
	        		} 
	        		catch (FileNotFoundException e)
	        		{
	        			study.setLogo(null);
	        		} 
	        		catch (IOException e)
	        		{
	        			study.setLogo(null);
	        		}
				}
				return new EditStudyPage(addStudyModalWindow, study) 
				{	
					private static final long serialVersionUID = 1L;

					@Override
					protected void onFinish(AjaxRequestTarget target, Study study) 
                	{
						Integer createdStudyId = studyService.finalizeStudy(study).getId();
					
						UserWeaveSession.get().setCreatedEntityId(createdStudyId);
                	}
                };
            }
        });
        
        addStudyModalWindow.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() 
        {
			private static final long serialVersionUID = 1L;

			public void onClose(AjaxRequestTarget target) 
            {
				Integer createdStudyId = UserWeaveSession.get().getCreatedEntityId();
				
            	if (createdStudyId != null) 
            	{
            		EntityEvent.Selected(target, studyService.load(createdStudyId)).fire(callback);
            		UserWeaveSession.get().setCreatedEntityId(null);
            	} 
            }
        });

        add(new ProjectContributorLink(
        		"addStudy", new StringResourceModel("create_new_study", this, null)) 
        {
			private static final long serialVersionUID = 1L;

			@Override
            public void onClick(AjaxRequestTarget target) 
            {
                addStudyModalWindow.show(target);
            }
        });
	}
}

