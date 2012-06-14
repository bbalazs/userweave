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

import org.apache.wicket.ajax.AjaxRequestTarget;

import com.userweave.domain.EntityBase;


public class EntityEvent implements IEntityEvent {

	private final EventType type;

	private final AjaxRequestTarget target;

	private final EntityBase entity;

	public EntityEvent(EventType type, AjaxRequestTarget target, EntityBase entity) {
		this.type = type;
		this.target = target;
		this.entity = entity;
	}


	public EntityEvent(EventType type, AjaxRequestTarget target) {
		this(type, target, null);
	}

	public EntityEvent(EventType type) {
		this(type, null, null);
	}

	
	@Override
	public EntityBase getEntity() {
		return entity;
	}

	@Override
	public AjaxRequestTarget getTarget() {
		return target;
	}

	@Override
	public EventType getType() {
		return type;
	}


	public static EntityEvent Created(AjaxRequestTarget target, EntityBase entity) {
		return new EntityEvent(EventType.Create, target, entity);
	}

	public static EntityEvent Purged(AjaxRequestTarget target, EntityBase entity) {
		return new EntityEvent(EventType.Purge, target, entity);
	}

	public static EntityEvent Updated(AjaxRequestTarget target, EntityBase entity) {
		return new EntityEvent(EventType.Update, target, entity);
	}
	
	public static EntityEvent Selected(AjaxRequestTarget target, EntityBase entity) {
		return new EntityEvent(EventType.Selected, target, entity);
	}
	
	/**
	 * helper method to ease firing the event
	 * 
	 * @param handler
	 * @return
	 */
	public boolean fire(IEventHandler handler) {
		return EventBroker.fireEvent(handler, this);
	}
}
