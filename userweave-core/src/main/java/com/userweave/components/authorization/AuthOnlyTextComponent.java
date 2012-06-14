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
package com.userweave.components.authorization;

import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.form.AbstractTextComponent;
import org.apache.wicket.model.IModel;

/**
 * Base class for text components, that are only 
 * accessible, if the user has the right to edit
 * this field. Otherwise set a label to display.
 * Authorization is set in the 
 * 		UserWeaveAuthorizationStrategy
 * class.
 * 
 * @author opr
 *
 */
public abstract class AuthOnlyTextComponent 
	extends AbstractTextComponent 
	implements IAuthOnly
{
	private static final long serialVersionUID = 1L;

	/**
	 * If the user is authorized, render the input field,
	 * else render a label.
	 */
	private boolean isAuthorized = false;
	
	/**
	 * If a textarea has to be rendered, set this to true.
	 */
	private boolean hasNoValueAttr = false;
	
	/**
	 * Ajax behavior to attach to the input field.
	 */
	private final Behavior behavior;
	
	/**
	 * @Constructor
	 * @param id
	 * @param model
	 * @param behavior
	 */
	public AuthOnlyTextComponent(String id, IModel model, Behavior behavior) 
	{
		super(id, model);
		
		this.behavior = behavior;
		
		if(hasBehavior())
		{
			this.add(getBehavior());
		}
		
		// set type to string for to avoid validation errors 
		// if the underlying model is of type LocalizedString.
		setType(String.class);
	}

	public AuthOnlyTextComponent(String id, Behavior behavior)
	{
		super(id);
		
		this.behavior = behavior;
		
		if(hasBehavior())
		{
			this.add(getBehavior());
		}
		
		setType(String.class);
	}
	
	public void sethasNoValueAttr(boolean hasNoValueAttr)
	{
		this.hasNoValueAttr = hasNoValueAttr;
	}
	
	@Override
	public void setIsAuthorized(boolean isAuthorized)
	{
		this.isAuthorized = isAuthorized;
	}
	
	protected boolean hasBehavior()
	{
		return behavior != null;
	}
	
	public Behavior getBehavior()
	{
		return behavior;
	}
	
	@Override
	protected void onComponentTag(ComponentTag tag) 
	{
		if(authorizedOnTag()) 
		{
			onIsAuthorizedComponentTag(tag);
		} 
		else 
		{
			onIsUnauthorizedComponentTag(tag);
		}
		
		super.onComponentTag(tag);
	}
	
	protected boolean authorizedOnTag()
	{
		return isAuthorized && isEditAllowed();
	}
	
	@Override
	public void onComponentTagBody(final MarkupStream markupStream, final ComponentTag tag)
	{
		if (!isEditAllowed() || !isAuthorized || hasNoValueAttr)
		{
            replaceComponentTagBody(markupStream, tag, getValue());
		}
		else
		{	
            super.onComponentTagBody(markupStream, tag);
		}
	}
	
	/**
	 * Determine, if authorization strategy has to be executed,
	 * or if this component is by default not editable.
	 * The rendering often depends on the study state, so override
	 * this method to change editablity in different study states.
	 * 
	 * @return
	 */
	protected boolean isEditAllowed()
	{
		return true;
	}
	
	/**
	 * Manipulate the tag if a user is authorized.
	 * @param tag
	 */
	protected abstract void onIsAuthorizedComponentTag(ComponentTag tag);
	
	/**
	 * Manipulate the tag if a user is not authorized.
	 * @param tag
	 */
	protected abstract void onIsUnauthorizedComponentTag(ComponentTag tag);
}
