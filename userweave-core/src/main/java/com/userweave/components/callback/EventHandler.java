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
package com.userweave.components.callback;

import java.io.Serializable;


@SuppressWarnings("serial")
public abstract class EventHandler implements IEventHandler, Serializable {
	private final IEventHandler parent;

	public EventHandler(IEventHandler parent) {
		this.parent = parent;
	}

	public EventHandler() {
		this(null);
	}
	
	/**
	 * this function is called by the event producing component and must be implemented 
	 * by the component that created the producing component 
	 * 
	 * @param event
	 * @return true indicates that the event is consumed
	 */
	public abstract boolean onEvent(IEntityEvent event);

	@Override
	public IEventHandler getParent() {
		return parent;
	}
}
