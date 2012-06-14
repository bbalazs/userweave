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
package com.userweave.pages.configuration.project;

import java.io.InputStream;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.application.UserWeaveSession;
import com.userweave.components.customModalWindow.CustomModalWindow;
import com.userweave.dao.BaseDao;
import com.userweave.dao.ProjectDao;
import com.userweave.domain.ImageBase;
import com.userweave.domain.Project;
import com.userweave.domain.Role;
import com.userweave.domain.User;
import com.userweave.pages.configuration.base.AbstractEntityConfigurationPanel;

/**
 * Panel to configure a given project.
 * 
 * @author opr
 */
public class ProjectConfigurationPanel extends AbstractEntityConfigurationPanel<Project>
{
	private static final long serialVersionUID = 1L;
	
	@SpringBean
	private ProjectDao projectDao;
	
	@Override
	protected BaseDao<Project> getBaseDao()
	{
		return projectDao;
	}
	
	/**
	 * Project admin enabled ajax link.
	 * 
	 * @author opr
	 */
	private abstract class ProjectAdminLink extends AjaxLink<Void>
	{
		private static final long serialVersionUID = 1L;

		public ProjectAdminLink(String id)
		{
			super(id);
		}
		
		@Override
		public boolean isEnabled()
		{
			return 
				super.isEnabled() && 
				UserWeaveSession.get().getUser().hasRole(Role.PROJECT_ADMIN) &&
				getProject().isPrivate();
		}
	}
	
	/**
	 * Flag, to show acceptance state of setToPublicPage.
	 */
	private boolean accepted = false;
	
	/**
	 * Reference to the 'set to public' link.
	 */
	private ProjectAdminLink link;
	
	/**
	 * Label for above link.
	 */
	private Label acceptLinkLabel;
	
	/**
	 * Window to display an accept conditions dialog.
	 */
	private CustomModalWindow window;
	
	/**
	 * Default constructor.
	 * 
	 * @param id
	 * 		Component markup id.
	 * @param projectId
	 * 		Id of project to configure.
	 */
	public ProjectConfigurationPanel(String id, final Integer projectId)
	{
		super(id, projectId);
		
		setOutputMarkupId(true);
	}
	
	@Override
	protected void initComponent(Form form)
	{	
		form.add(getSetToPublicModal());
		
		form.add(getSetPublicLink());
	}

	/**
	 * Factory method to create an ajax link, that sets
	 * the underlying project to public.
	 * 
	 * @return
	 * 		A ProjectAdminLink.
	 */
	private ProjectAdminLink getSetPublicLink()
	{
		Project project = getProject();

		if(project.isPrivate())
		{
			acceptLinkLabel = new Label(
				"setPublicLabel", 
				new StringResourceModel(
						"set_to_public_link_label", 
						ProjectConfigurationPanel.this, 
						null,
						new Object[] { project.getName() }));
		}
		else
		{
			acceptLinkLabel = new Label(
					"setPublicLabel", 
					new StringResourceModel(
							"is_already_public", 
							ProjectConfigurationPanel.this, 
							null,
							new Object[] { project.getName() }));
		}
		
		link = new ProjectAdminLink("setPublic")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				window.show(target);
			}
		};
		
		acceptLinkLabel.setOutputMarkupId(true);
		
		link.add(acceptLinkLabel);
		
		return link;
	}
	
	@Override
	protected void onBackgroundColorUpdate(AjaxRequestTarget target)
	{
		Project project = getEntity();
		
		// compoute new font color, if background color changes
		project.setFontColor(
			computeBrightnessOfBackgroundAndSetColorValue(
				project.getBackgroundColor()));
		
		projectDao.save(getProject());
	}
	
	@Override
	protected boolean userIsAuthorized()
	{
		User user = UserWeaveSession.get().getUser();
		
		return user.hasRole(Role.PROJECT_ADMIN) || 
			   user.hasRole(Role.PROJECT_PARTICIPANT);
	}
	
	@Override
	protected byte[] getImageData()
	{
		ImageBase logo = getProject().getLogo();
						
		if (logo != null) {	return logo.getImageData();	} 
		
		return null;
	}
	
	@Override
	protected void deleteLogo()
	{
		Project project = getProject();
		
		try
		{
			String path = "user_weaver_logo_kl.png";

			InputStream stream = getClass().getResourceAsStream(path);

			if (stream != null)
			{
				byte[] data = new byte[10000];
				stream.read(data);
				
				project.getLogo().setImageData(data);
			}
			else
			{
				project.getLogo().setImageData(null);
			}
		}
		catch (Exception e) 
		{
			project.getLogo().setImageData(null);
		}
		
		projectDao.save(project);
	}
	
	@Override
	protected ImageBase getEntityLogo()
	{
		return getProject().getLogo();
	}
	
	@Override
	protected void setEntityLogo(Project entity, ImageBase image)
	{
		entity.setLogo(image);
	}
		
	/**
	 * Factory method to create a dilaog fot the 
	 * "set to public" link.
	 * 
	 * @return
	 * 		A modal window.
	 */
	private ModalWindow getSetToPublicModal()
	{
		final Project project = getProject();
		
		window = new CustomModalWindow("setToPublicModal");
		
		window.setTitle(new StringResourceModel(
			"setToPublicTitle", 
			ProjectConfigurationPanel.this,
			null,
			new Object[] { project.getName() }));
		
		window.setPageCreator(new ModalWindow.PageCreator()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page createPage()
			{
				return new SetToPublicWebPage(window, project.getName())
				{
					@Override
					protected void onAccept(AjaxRequestTarget target)
					{
						accepted = true;
					}
				};
			}
		});
		
		window.setWindowClosedCallback(new ModalWindow.WindowClosedCallback()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClose(AjaxRequestTarget target)
			{
				if(accepted)
				{
					Project project = getProject();
				
					project.setPrivate(false);
				
					projectDao.save(project);
					
					acceptLinkLabel.setDefaultModel(new StringResourceModel(
						"is_already_public", 
						ProjectConfigurationPanel.this, 
						null,
						new Object[] { project.getName() }));
					
					target.addComponent(acceptLinkLabel);
					target.addComponent(link);
				}
			}
		});
		
		return window;
	}
	
	/**
	 * Shortcut to get the project to configure.
	 * 
	 * @return
	 * 		The project to configure.
	 */
	private Project getProject()
	{
		return (Project) getDefaultModelObject();
	}
	
	
	
	
}
