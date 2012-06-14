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
package com.userweave.pages.configuration.report;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;

import com.userweave.domain.Group;

/**
 * Panel which wrapps a drop down choice for filters.
 * 
 * @author oma
 * @author opr
 */
public abstract class GroupFilterPanel extends Panel 
{
	private static final long serialVersionUID = 1L;

	/**
	 * The selected filter in the drop down.
	 */
	private Group selectedGroup;

	public Group getSelectedGroup() 
	{
		return selectedGroup;
	}

	public void setSelectedGroup(Group selectedGroup) 
	{
		this.selectedGroup = selectedGroup;
	}
	
	/**
	 * Drop down for a list of avaliable filters.
	 */
	private DropDownChoice<Group> choices;
	
	protected DropDownChoice<Group> getChoices()
	{
		return choices;
	}
	
	/**
	 * Default constructor.
	 * 
	 * @param id
	 * 		Component markup id.
	 * @param group
	 * 		Preselected group. May be null.
	 */
	public GroupFilterPanel(String id, Group group) 
	{
		super(id);
		
		this.selectedGroup = group;
		
		Form<Void> form = new Form<Void>("form");
		
		add(form);
		
//		form.add(
//			new AjaxButton("add", form) 
//			{
//				private static final long serialVersionUID = 1L;
//
//				@Override
//				protected void onSubmit(AjaxRequestTarget target, Form<?> form) 
//				{
//					onAdd(target);
//				}
//				
//				@Override
//				protected void onError(AjaxRequestTarget target, Form<?> form)
//				{
//				}
//				
//				@Override
//				public boolean isVisible() 
//				{
//					return addButtonIsVisible(GroupFilterPanel.this);
//				}
//				
//			}
//		);
		
		form.add(
			new AjaxButton("remove", form) 
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) 
				{
					GroupFilterPanel.this.onRemove(target);
				}
				
				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form)
				{
				}
				
//				@Override
//				public boolean isVisible() 
//				{
//					return addButtonIsVisible(GroupFilterPanel.this);
//				}
			}
		);	
	
		form.add(
			choices = new DropDownChoice<Group>(
					"groups", 
					new PropertyModel<Group>(this, "selectedGroup"), 
					newGroupsModel(), 
					new ChoiceRenderer<Group>("name")));
			
		choices.setOutputMarkupId(true);
		choices.setRequired(true);
		
		choices.add(
			new AjaxFormSubmitBehavior("onchange") 
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onError(AjaxRequestTarget target) {}

				@Override
				protected void onSubmit(AjaxRequestTarget target) 
				{
					if(getSelectedGroup() != null && addButtonIsVisible(GroupFilterPanel.this))
					{
						onAdd(target);
					}
					
					onSelect(target);
				}
			});
		
	}

	/**
	 * Get a model for the list of available filters.
	 * 
	 * @return
	 * 		Model which wraps a list of filters.
	 */
	private LoadableDetachableModel<List<Group>> newGroupsModel()
	{
		return new LoadableDetachableModel<List<Group>>() 
		{
			private static final long serialVersionUID = 1L;
	
			@Override
			protected List<Group> load() {
				
				// get list of all avalable groups exept the this one
				List<Group> rv = new ArrayList<Group>();
				
				List<Group> selectedGroups = getSelectedGroups();
				
				for (Group group : getGroups()) {
					if (!selectedGroups.contains(group) || group.equals(selectedGroup)) {
						rv.add(group);
					}
				}
				return rv;									
			}	
		};
	}
	
	
	
	protected abstract boolean addButtonIsVisible(GroupFilterPanel panel);

	protected abstract void onSelect(AjaxRequestTarget target);

	protected abstract List<Group> getSelectedGroups();

	protected abstract void onAdd(AjaxRequestTarget target);
	
	protected abstract void onRemove(AjaxRequestTarget target);

	protected abstract List<Group> getGroups();

}

