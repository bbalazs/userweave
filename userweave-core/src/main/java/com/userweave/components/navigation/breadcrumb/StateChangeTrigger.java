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
package com.userweave.components.navigation.breadcrumb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;

import com.userweave.components.callback.EventHandler;
import com.userweave.pages.configuration.base.IConfigReportStateChanger;
import com.userweave.pages.configuration.base.IConfigReportStateChanger.UiState;
import com.userweave.pages.configuration.report.FilterFunctorCallback;

/**
 * Simple class to delegate the change from
 * report to config view and vice versa to
 * listening components.
 * 
 * @author opr
 *
 */
public class StateChangeTrigger implements Serializable
{
	private static final long serialVersionUID = 1L;

	private FilterFunctorCallback filterFunctorCallback = null;
	
	public void setFilterFunctorCallback(FilterFunctorCallback callback)
	{
		this.filterFunctorCallback = callback;
	}
	
	public FilterFunctorCallback getFilterFunctorCallback()
	{
		return filterFunctorCallback;
	}
	
	/**
	 * Current state the ui is in.
	 */
	private UiState state;
	
	/**
	 * Listeners to notify, if a state
	 * chage occurs.
	 */
	private final List<IConfigReportStateChanger> listeners;
	
	public UiState getState()
	{
		return state;
	}

	public void setState(UiState state)
	{
		this.state = state;
	}

	/**
	 * Default constructor.
	 * 
	 * @param state
	 * 		Initial state.
	 */
	public StateChangeTrigger(UiState state)
	{
		this.state = state;
		
		listeners = new ArrayList<IConfigReportStateChanger>();
	}
	
	/**
	 * Registers a listener.
	 * 
	 * @param listener
	 */
	public void register(IConfigReportStateChanger listener)
	{
		listeners.add(listener);
	}
	
	/**
	 * Unregister a listener.
	 *  
	 * @param listener
	 */
	public void unregister(IConfigReportStateChanger listener)
	{
		listeners.remove(listener);
	}
	
	/**
	 * State change method to notify the listeners.
	 * 
	 * @param newState
	 * 		New state to propagate.
	 * @param target
	 * 		AjaxRequestTarget.
	 * @param callback
	 * 		Callback to trigger when th ui has to change
	 * 		to project overview. May be null.
	 */
	public void triggerChange(UiState newState, AjaxRequestTarget target, EventHandler callback)
	{
		this.state = newState;
		
		for(IConfigReportStateChanger listener : listeners)
		{
			listener.onStateChange(state, target, callback, StateChangeTrigger.this);
		}
	}
}
