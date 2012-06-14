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

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;

import com.userweave.components.callback.EventHandler;
import com.userweave.domain.OrderedEntityBase;

/**
 * Panel with reorderable functionality. Used, when an 
 * entity is reorderable, which means, that it has a 
 * position in a list of entities (applies for Questions 
 * and Modules)
 * 
 * @author opr
 *
 * @param <T> Type of Entity
 */
public abstract class ReorderableEntityPanel<T extends OrderedEntityBase> extends CopyEntityPanel<T>
{
	private static final long serialVersionUID = 1L;

	public ReorderableEntityPanel(String id, IModel<T> entityModel, EventHandler callback)
	{
		super(id, entityModel, callback);
		
		add(new StudyStateInitDependentLink("moveup") 
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) 
			{
				moveUp(target);
			}
			
			@Override
			public boolean isEnabled() 
			{	
				return super.isEnabled() && moveUpIsEnabled(getEntity().getPosition());
			}
			
			@Override
			protected void onBeforeRender()
			{
				if(! isEnabled())
				{
					setToolTipType(ToolTipType.IMPOSSIBLE);
				}
				
				super.onBeforeRender();
			}
		});
		
		add(new StudyStateInitDependentLink("movedown") 
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) 
			{
				moveDown(target);
			}
			
			@Override
			public boolean isEnabled() 
			{
				return super.isEnabled() && moveDownIsEnabled(getEntity().getPosition());
			}
			
			@Override
			protected void onBeforeRender()
			{
				if(! isEnabled())
				{
					setToolTipType(ToolTipType.IMPOSSIBLE);
				}
				
				super.onBeforeRender();
			}
		});
	}

	/**
	 * Defines, if the move up link is visible. That is, if
	 * it is not the first, the second or the last element 
	 * of a list.
	 * 
	 * @param moduleIndex
	 * 		Index of this module in superior list.
	 * 
	 * @return
	 */
	protected abstract boolean moveUpIsEnabled(int moduleIndex);
	
	/**
	 * Defines, if the move down link is visible. That is, if
	 * it is not the first, the last or the last but one 
	 * element of a list.
	 * 
	 * @param moduleIndex
	 * 		Index of this module in superior list.
	 * @param sizeOfList
	 * 		Size of the list this module is in.
	 * @return
	 */
	protected abstract boolean moveDownIsEnabled(int moduleIndex);
	
	/**
	 * Callback triggered on move up.
	 * 
	 * @param target
	 * 		AjaxRequestTarget.
	 * @param tabbedPanel
	 */
	protected abstract void moveUp(AjaxRequestTarget target);
	
	/**
	 * Callback triggered on move down.
	 * 
	 * @param target
	 * 		AjaxRequestTarget.
	 * @param tabbedPanel
	 */
	protected abstract void moveDown(AjaxRequestTarget target);
}
