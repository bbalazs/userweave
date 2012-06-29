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
package com.userweave.pages.configuration.editentitypanel;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.WindowClosedCallback;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.components.IToolTipComponent.ToolTipType;
import com.userweave.components.authorization.AuthOnlyAjaxLink;
import com.userweave.components.callback.EventHandler;
import com.userweave.domain.Project;
import com.userweave.domain.Study;
import com.userweave.domain.service.ModuleService;
import com.userweave.module.ModuleConfiguration;
import com.userweave.module.ModuleConfigurationImpl;
import com.userweave.pages.components.slidableajaxtabpanel.ChangeTabsCallback;
import com.userweave.pages.configuration.editentitypanel.copydialog.BrowseEntityWebPage;

public abstract class EditModuleConfigurationEntityPanel 
	extends ReorderableEntityPanel<ModuleConfigurationImpl>
{
	private static final long serialVersionUID = 1L;

	@SpringBean
	private ModuleService moduleService;
	
	private final ChangeTabsCallback changeTabsCallback;
	
	private final int moduleIndex;
	
	public EditModuleConfigurationEntityPanel(
			String id, IModel configurationModel, EventHandler callback, 
			ChangeTabsCallback changeTabsCallback, int moduleIndex)
	{
		super(id, configurationModel, callback);
		
		this.changeTabsCallback = changeTabsCallback;
		
		this.moduleIndex = moduleIndex;
	}

	@Override
	protected IModel getCopyModalTitle()
	{
		return new StringResourceModel("copyMethod", this, null);
	}
	
	@Override
	protected WebPage getCopyWebPage(EventHandler callback,ModalWindow window)
	{
		String newName = new StringResourceModel("copy_of", this, null).getString() + 
						 "_" + getConfiguration().getName();
		
		return new BrowseEntityWebPage(newName, window, 2)
		{	
			@Override
			protected void onCopy(String copyName, Project destinyProject,
					Study destinyStudy, ModuleConfiguration configuration)
			{
				ModuleConfiguration moduleConfiguration = 
					moduleService.createNewConfigurationInStudyForModule(getConfiguration(), destinyStudy);
				
				moduleConfiguration.setName(copyName);
				moduleConfiguration.save();
			}
		};
	}

	@Override
	protected void onClose(AjaxRequestTarget target)
	{
		changeTabsCallback.fireChange(target, moduleIndex);
	}
	
	@Override
	protected AuthOnlyAjaxLink getEditLink(String id)
	{
		return new StudyStateInitDependentLink(id)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				onEditLinkActivate(target);
			}
		};
	}
	
	@Override
	protected WebPage getEditWebPage(final EventHandler callback, ModalWindow window)
	{
		return new EditModuleConfigurationWebPage(getDefaultModel(), window);
	}

	@Override
	protected WindowClosedCallback getEditWindowClosedCallback(final EventHandler callback)
	{
		return new ModalWindow.WindowClosedCallback()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClose(AjaxRequestTarget target)
			{
				onEdit(getConfiguration(), target);
			}
		};
	}
	
	protected abstract void onEdit(ModuleConfiguration configuraton, AjaxRequestTarget target);
	
	@Override
	protected void onDelete(AjaxRequestTarget target)
	{
		moduleService.delete(getConfiguration());
	}
	
	private ModuleConfigurationImpl getConfiguration()
	{
		return getEntity();
	}

	@Override
	protected IModel getStudyStateModel()
	{
		return new PropertyModel(getConfiguration(), "study.state");
	}
	
	@Override
	protected void moveUp(AjaxRequestTarget target)
	{
		List<ModuleConfiguration> modules = 
			moduleService.getModuleConfigurationsForStudy(getStudy());
		
		moduleService.moveUp(modules, getConfiguration());
		
		moveModuleUp(target);
		
		//target.addComponent(tabbedPanel);
	}

	@Override
	protected void moveDown(AjaxRequestTarget target)
	{
		List<ModuleConfiguration> modules = 
			moduleService.getModuleConfigurationsForStudy(getStudy());
		
		moduleService.moveDown(modules, getConfiguration());
		
		moveModuleDown(target);
		
		//target.addComponent(tabbedPanel);
	}
	
	@Override
	protected AuthOnlyAjaxLink getDeleteLink(String id)
	{
		StudyStateInitDependentLink link = 
			(StudyStateInitDependentLink) super.getDeleteLink(id);
	
		if(! deleteLinkIsEnabled())
		{
			link.setToolTipType(ToolTipType.IMPOSSIBLE);
		}
		
		return link;
	}
	
	@Override
	protected boolean deleteLinkIsEnabled()
	{
		List<ModuleConfiguration> modules = 
			moduleService.getModuleConfigurationsForStudy(getStudy());
		
		int position = getConfiguration().getPosition();
		
		return position != 0 && 
			   position != modules.size() - 1;
	}
	
	private Study getStudy()
	{
		return getConfiguration().getStudy();
	}
	
	@Override
	protected String getEntityName()
	{
		return getConfiguration().getName();
	}
	
	protected abstract void moveModuleUp(AjaxRequestTarget target);
	
	protected abstract void moveModuleDown(AjaxRequestTarget target);
}
