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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventBroker {
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	private EventBroker() {}
	
	/**
	 * convenience method for instantiating
	 * @param handler
	 * @param event
	 * @return
	 */
	public static boolean fireEvent(IEventHandler handler, IEntityEvent event) {
		return new EventBroker().fire(handler, event);
	}
	
	/**
	 * use this function to fire a new event to the EventHandler and it's parent
	 * 
	 * @param event
	 */
	public final boolean fire(IEventHandler handler, IEntityEvent event) {
		return fire(handler, event, false);
	}
	
	public final boolean fire(IEventHandler handler, IEntityEvent event, boolean consumed) {
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("firing event "+event.getType()+ " on "+handler);
		}
		if(handler == null) {
			LOGGER.warn("event "+event.getType()+" for entity "+event.getEntity()+" ignored (handler is null)");
		} else {
			consumed = consumed || handler.onEvent(event);
			IEventHandler parent = handler.getParent();
			if(parent != null) {
				consumed = consumed || this.fire(parent, event);
			}
			if(!consumed && parent == null) {
				LOGGER.warn("event "+event.getType()+" for entity "+event.getEntity()+" ignored");
			}
		}
		return consumed;
	}


}
