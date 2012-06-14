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
package com.userweave.pages.configuration.editentitypanel.copydialog;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import com.userweave.domain.EntityBase;

/**
 * Displays a list of entities which are
 * selectable. If an entity is selected,
 * open in the right sibling browser panel
 * the child elements of this entity
 * (Projects -> Studies -> Modules (-> Questions))
 *  
 * @author opr
 *
 */
public abstract class BrowserPanel<T extends EntityBase> extends Panel
{
	private static final long serialVersionUID = 1L;

	/**
	 * The right sibling browser panel. Changes its
	 * content, if a entity in this panel is clicked.
	 */
	private BrowserPanel<?> rightSibling;
	
	/**
	 * The list view to display the list of entities.
	 */
//	private final ListView lv;
	
	private final DropDownChoice choices;
	
	private T selectedItem;
	
	public void setSelectedItem(T item)
	{
		selectedItem = item;
	}
	
	/**
	 * Default constructor.
	 * 
	 * @param id
	 * 		Component id.
	 * @param model
	 * 		Model which contains a list of entities.
	 */
	public BrowserPanel(String id, List<T> list, boolean isRequired, IModel labelModel)
	{
		super(id);
		
//		lv = new ListView("list", list)
//		{	
//			@Override
//			protected void populateItem(final ListItem item)
//			{
//				final T entity = (T)item.getModelObject();
//				
//				AjaxLink link = new AjaxLink("link")
//				{
//					@Override
//					public void onClick(AjaxRequestTarget target)
//					{
//						BrowserPanel.this.onClick(target, entity, getRightSibling());
//					}
//				};
//				
//				link.add(new Label("entityName", getEntityName(entity)));
//				
//				item.add(link);
//				
//			}
//		};
		
//		add(lv);
		
		// @see:#971
//		Form form = new Form("form");
//		
//		add(form);
		
		choices = 
			new DropDownChoice(
					"dropDown", 
					new PropertyModel(this, "selectedItem"),
					list,
					new IChoiceRenderer()
					{
						private static final long serialVersionUID = 1L;

						@SuppressWarnings("unchecked")
						@Override
						public String getIdValue(Object object, int index)
						{
							return getEntityName((T) object);
						}
						
						@SuppressWarnings("unchecked")
						@Override
						public Object getDisplayValue(Object object)
						{
							return getEntityName((T) object);
						}
					});
		
		choices.add(new AjaxFormComponentUpdatingBehavior("onchange")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				onClick(target, selectedItem, getRightSibling());
			}
		});
		
		choices.setLabel(labelModel);
		
//		form.add(choices);
		add(choices);
		if(isRequired)
		{
			choices.setRequired(true);
		}

	}
	/**
	 * Get the right sibling of this browser panel.
	 * 
	 * @return
	 */
	public BrowserPanel<?> getRightSibling()
	{
		return rightSibling;
	}
	
	/**
	 * Sets the right sibling browser panel.
	 * 
	 * @param rightSibling
	 * 		The BrwoserPanel to set as right sibling.
	 */
	public void setRightSibling(BrowserPanel<?> rightSibling)
	{
		this.rightSibling = rightSibling;
	}
	
	/**
	 * Changes the displayed list with the given list.
	 * 
	 * @param list
	 * 		New list to display.
	 */
	public void resetDisplayList(List<?> list)
	{
		//lv.setList(list);
		choices.setChoices(list);
	}
	
	/**
	 * Callback if an entity has been clicked.
	 * Usage: Since you want to change the list of the right
	 * sibling, call 
	 * 		target.appendComponent(rightSibling.setList(newList))
	 * 
	 * @param target
	 * 		AjaxRequestTarget.
	 * @param entity
	 * 		The entity, which has been clicked.
	 * @param rightSibling
	 * 		The right sibling of this panel.
	 */
	protected abstract void onClick(
			AjaxRequestTarget target, T entity, BrowserPanel<?> rightSibling);
	
	/**
	 * Returns the name of the given entity.
	 * 
	 * @param entity
	 * 		Entity to get name from.
	 * @return
	 * 		The name of the entity.
	 */
	protected abstract String getEntityName(T entity);
}
