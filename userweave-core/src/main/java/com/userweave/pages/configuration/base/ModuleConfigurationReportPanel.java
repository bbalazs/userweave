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
package com.userweave.pages.configuration.base;

import java.util.Locale;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;

import com.userweave.application.UserWeaveSession;
import com.userweave.components.callback.EventHandler;
import com.userweave.components.navigation.breadcrumb.StateChangeTrigger;
import com.userweave.dao.BaseDao;
import com.userweave.dao.StudyDependendDao;
import com.userweave.domain.OrderedEntityBase;
import com.userweave.domain.Study;
import com.userweave.module.ModuleConfiguration;
import com.userweave.pages.components.slidableajaxtabpanel.ChangeTabsCallback;
import com.userweave.pages.configuration.editentitypanel.BaseFunctionEditEntityPanel;
import com.userweave.pages.configuration.editentitypanel.EditModuleConfigurationEntityPanel;
import com.userweave.pages.configuration.report.FilteredReportPanel;

/**
 * Base class to configure a method.
 * 
 * @author opr
 *
 * @param <T>
 */
public abstract class ModuleConfigurationReportPanel<T extends OrderedEntityBase<?>> 
	extends ConfigurationReportPanel
{
	private static final long serialVersionUID = 1L;

	/**
	 * Id of entity, which backs this configuration.
	 */
	private final int entityId;
	
	/**
	 * Getter for the entity id.
	 */
	protected int getEntityId() 
	{
		return entityId;
	}
	
	/**
	 * Get the entity from the model.
	 * 
	 * @return
	 * 		entity which backs this panel.
	 */
	@SuppressWarnings("unchecked")
	protected T getEntity() 
	{
		return (T) getDefaultModelObject();
	}
	
	private ChangeTabsCallback callback;
	
	public ModuleConfigurationReportPanel(
			String id, StateChangeTrigger trigger, final int entityId, ChangeTabsCallback callback)
	{
		super(id);
		
		this.callback = callback;
		
		this.entityId = entityId;
		
		setDefaultModel(
			new CompoundPropertyModel( 
				new LoadableDetachableModel() 
				{
					private static final long serialVersionUID = 1L;

					@Override
					protected Object load() 
					{
						return getBaseDao().findById(entityId);
					}
				}
			));
		
		initView(trigger, null);
	}
	
	@Override
	protected BaseFunctionEditEntityPanel getActionComponent(
			String id, UiState state, EventHandler callback)
	{
		return new EditModuleConfigurationEntityPanel(
			id, getDefaultModel(), callback, this.callback, getEntity().getPosition())
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected boolean moveUpIsEnabled(int moduleIndex)
			{
				return ModuleConfigurationReportPanel.this.moveUpIsEnabled(moduleIndex);
			}

			@Override
			protected boolean moveDownIsEnabled(int moduleIndex)
			{
				return ModuleConfigurationReportPanel.this.moveDownIsEnabled(moduleIndex);
			}

			@Override
			protected void moveModuleUp(AjaxRequestTarget target)
			{
				ModuleConfigurationReportPanel.this.moveModuleUp(target);
			}

			@Override
			protected void moveModuleDown(AjaxRequestTarget target)
			{
				ModuleConfigurationReportPanel.this.moveModuleDown(target);
			}
			
			@Override
			protected void onEdit(ModuleConfiguration configuraton,
					AjaxRequestTarget target)
			{
				ModuleConfigurationReportPanel.this.callback.fireAppend(target);
			}

			@Override
			protected void onAfterDelete(AjaxRequestTarget target, final EventHandler callback, Integer positionBeforeDeletion)
			{
				if(positionBeforeDeletion != null)
				{
					int preSelectedTab = 
						positionBeforeDeletion - 1 > 0 ? 
								positionBeforeDeletion -1 : 
								0;
					
					ModuleConfigurationReportPanel.this.callback.fireChange(target, preSelectedTab);
				}
				else
				{
					ModuleConfigurationReportPanel.this.callback.fireChange(target, null);
				}
			}
			
		};
	}
	
	/**
	 * BaseDao to load entity from.
	 * @return
	 * 		The BaseDao.
	 */
	protected abstract BaseDao<T> getBaseDao();
	
	/**
	 * Get the study this configuration depends on.
	 * 
	 * @return
	 * 		Study.
	 */
	protected abstract Study getStudy();
	
	/**
	 * this function provides either the study locale OR - 
	 * if coming from ReportLink - the locale of the user.
	 * 
	 * @return
	 */
	public Locale getStudyLocale()
	{
		if(showOnConfigurationButNotOnReport()) 
		{
			return getStudy().getLocale();
		} 
		else 
		{
			return getLocale();
		}
	}
	
	public final boolean showOnConfigurationButNotOnReport() 
	{
		return ! UserWeaveSession.get().originFromReport();
	}
	
	@SuppressWarnings("unchecked")
	protected T getConfiguration() 
	{
		return (T) getDefaultModelObject();
	}
	
	protected void saveConfiguration() {
		getConfigurationDao().save(getConfiguration());
	}
	
	protected abstract StudyDependendDao<T> getConfigurationDao();

	protected boolean moveUpIsEnabled(int moduleIndex)
	{
		return moduleIndex > 1 && moduleIndex < callback.getSizeOfTabList() - 2;
	}

	protected boolean moveDownIsEnabled(int moduleIndex)
	{
		return moduleIndex > 0 && moduleIndex < callback.getSizeOfTabList() - 3;
	}
	
	protected void moveModuleUp(AjaxRequestTarget target)
	{
		callback.fireChange(target, getEntity().getPosition());
	}
	
	protected void moveModuleDown(AjaxRequestTarget target)
	{
		callback.fireChange(target, getEntity().getPosition());
	}
	
	@Override
	public void onFilter(AjaxRequestTarget target, StateChangeTrigger trigger)
	{
		super.onFilter(target, trigger);
		
		Component panel = this.get(STATE_DEPEND_COMPONENT_ID);
		
		if(panel != null && panel instanceof FilteredReportPanel)
		{
			((FilteredReportPanel)panel).onFilter(target);
		}
	}
}
