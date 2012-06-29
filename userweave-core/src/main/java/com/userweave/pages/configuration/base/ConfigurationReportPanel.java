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

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;

import com.userweave.components.callback.EventHandler;
import com.userweave.components.navigation.breadcrumb.StateChangeTrigger;
import com.userweave.domain.EntityBase;
import com.userweave.pages.configuration.editentitypanel.BaseFunctionEditEntityPanel;

public abstract class ConfigurationReportPanel<T extends EntityBase> extends Panel implements IConfigReportStateChanger
{
	private static final long serialVersionUID = 1L;

	protected static final String STATE_DEPEND_COMPONENT_ID = "content";
	
	private BaseFunctionEditEntityPanel<T> actionComponent;
	
	private Component stateDependComponent;
	
	public Component getStateDependComponent()
	{
		return stateDependComponent;
	}
	
	public void setStateDependComponent(Component stateDependComponent)
	{
		this.stateDependComponent = stateDependComponent;
	}
	
	public ConfigurationReportPanel(String id)
	{
		super(id);
	}

	/**
	 * Needs to be called explicit by sub classes.
	 * Initializes the view, depending on the 
	 * stateChangeTrigger ui state var.
	 * 
	 * @param stateChangeTrigger
	 * 		Triggers ui state form reort to conf an vice vera.
	 * @param callback
	 * 		Callback to fire on event.
	 */
	protected void initView(StateChangeTrigger stateChangeTrigger, EventHandler callback)
	{
		if(stateChangeTrigger.getState() == UiState.CONFIG)
		{
			stateDependComponent = getConfigurationComponent(STATE_DEPEND_COMPONENT_ID);
		}
		else
		{
			stateDependComponent = getReportComponent(STATE_DEPEND_COMPONENT_ID, stateChangeTrigger);
		}
		
		stateDependComponent.setOutputMarkupId(true);
		
		add(stateDependComponent);
		
		
		actionComponent = 
			getActionComponent("actions", stateChangeTrigger.getState(), callback);
		
		actionComponent.setOutputMarkupId(true);
		
		add(actionComponent);
	}
	
	@Override
	public void onStateChange(
			UiState state, AjaxRequestTarget target, EventHandler callback, StateChangeTrigger trigger)
	{
		BaseFunctionEditEntityPanel<T> actionComponentReplacement = 
			getActionComponent("actions", state, callback);
		
		Component stateDependReplacement;
		
		if(state == UiState.CONFIG)
		{
			stateDependReplacement = getConfigurationComponent(STATE_DEPEND_COMPONENT_ID);
		}
		else
		{
			stateDependReplacement = getReportComponent(STATE_DEPEND_COMPONENT_ID, trigger);
		}
		
		
		actionComponentReplacement.setOutputMarkupId(true);
		stateDependReplacement.setOutputMarkupId(true);
		
		
		actionComponent.replaceWith(actionComponentReplacement);
		actionComponent = actionComponentReplacement;
		
		stateDependComponent.replaceWith(stateDependReplacement);
		stateDependComponent = stateDependReplacement;
		
		
		target.add(actionComponent);
		target.add(stateDependComponent);
	}
	
	/**
	 * Returns a component which contains the action buttons 
	 * for this view.
	 * 
	 * @param id
	 * 		Component markup id
	 * @param state
	 * 		View state of ui (config or report)
	 * @param callback
	 * 		Callback to fire on event
	 * @return
	 */
	protected abstract BaseFunctionEditEntityPanel<T> getActionComponent(String id, UiState state, EventHandler callback);
	
	/**
	 * Returns a component for the configuration view.
	 * 
	 * @param id
	 * 		Component markup id.
	 */
	protected abstract Component getConfigurationComponent(String id);
	
	/**
	 * Returns a component for the report view.
	 * 
	 * @param id
	 * 		Component markup id.
	 * @param trigger
	 * 		The trigger vor the view state change.
	 */
	protected abstract Component getReportComponent(String id, StateChangeTrigger trigger);
	
	/**
	 * Report components can be filtered. This method 
	 * represents the filter action to perform on a 
	 * filter request.
	 * 
	 * @param target
	 * 		Ajax request target.
	 */
	public void onFilter(AjaxRequestTarget target, StateChangeTrigger trigger)
	{
		target.add(stateDependComponent);
	}
}
