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
package com.userweave.components;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.model.StringResourceModel;

/**
 * Extension to an ajax link to add a tooltip (i.e. appends 
 * 'title' to the tag), if the link is disabled.
 * 
 * @author opr
 *
 */
public abstract class ToolTipAjaxLink 
	extends AjaxLink<Void> 
	implements IToolTipComponent
{
	private static final long serialVersionUID = 1L;

	/**
	 * The current tooltip to be displayed, if this component
	 * gets disabled.
	 */
	private ToolTipType toolTipType = ToolTipType.RIGHTS;
	
	public void setToolTipType(ToolTipType toolTipType)
	{
		if(toolTipType.ordinal() < this.toolTipType.ordinal())
		{
			this.toolTipType = toolTipType;
		}
	}

	public ToolTipType getToolTipType()
	{
		return toolTipType;
	}
	
	/**
	 * Default constructor.
	 * 
	 * @param id
	 * 		Component markup id.
	 */
	public ToolTipAjaxLink(String id)
	{
		super(id);
	}
	
	public ToolTipAjaxLink(String id, ToolTipType type)
	{
		super(id);
		
		setToolTipType(type);
	}
	
	/**
	 * Adds an AttributeModifier for the title attribute, 
	 * if this component is disabled.
	 */
	@Override
	protected void onBeforeRender()
	{	
		boolean isEnabled = isEnabled();
		
		if(! isEnabled)
		{
			add(new AttributeModifier("title", new StringResourceModel(
				this.toolTipType.getStringResourceKey(), 
				this, null)));
		}
		
		super.onBeforeRender();
	}
	
}
