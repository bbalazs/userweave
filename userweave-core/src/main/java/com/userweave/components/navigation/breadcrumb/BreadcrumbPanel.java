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

import java.util.Iterator;
import java.util.LinkedList;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;

import com.userweave.components.callback.EntityEvent;
import com.userweave.components.callback.EventHandler;
import com.userweave.domain.EntityBase;
import com.userweave.domain.Project;
import com.userweave.domain.Study;

/**
 * Breadcrumb navigation. To Display the path,
 * recurse from an entity back to the first 
 * project.
 * 
 * @author opr
 *
 */
public class BreadcrumbPanel extends Panel
{
	private static final long serialVersionUID = 1L;
	
	private class LabelFragment extends Fragment
	{
		private static final long serialVersionUID = 1L;

		public LabelFragment(String id, String displayValue)
		{
			super(id, "labelFragment", BreadcrumbPanel.this);
			
			add(new Label("label", displayValue));
		}
	}
	
	private class LinkFragment extends Fragment
	{
		private static final long serialVersionUID = 1L;

		public LinkFragment(
				String id, 
				String displayValue, 
				final EntityBase entity,
				final EventHandler callback)
		{
			super(id, "linkFragment", BreadcrumbPanel.this);
			
			@SuppressWarnings("serial")
			AjaxLink link = new AjaxLink("link")
			{
				@Override
				public void onClick(AjaxRequestTarget target)
				{
					EntityEvent.Selected(target, entity).fire(callback);
				}
			};
			
			link.add(new Label("displayName", displayValue));
			
			add(link);
		}
	}
	
	
	
	/**
	 * Stop recursion after this number of steps.
	 */
	private static final int MAX_RECURSION_DEPTH = 10;
	
	/**
	 * Default Constructor.
	 * 
	 * @param id
	 * 		Component id.
	 * @param entity
	 * 		Entity to build navigation from.
	 * @param callback
	 * 		Callback to fire, if a navigation link is clicked.
	 */
	public BreadcrumbPanel(
			String id, EntityBase entity, final EventHandler callback)
	{
		super(id);
		
		init(entity, callback);
	}
	
	/**
	 * Builds the navigation by recursing from the entity
	 * through all parent entities.
	 * 
	 * @param entity
	 * 		Entity to start recursion from.
	 * @param callback
	 * 		Callback to fire, when a link is clicked.
	 */
	private void init(EntityBase entity, final EventHandler callback)
	{
		WebMarkupContainer homePageLinkContainer = 
			new WebMarkupContainer("homePageLinkContainer");
		
		AjaxLink homePageLink = getHomePageLink(callback);
		
		homePageLinkContainer.add(homePageLink);
		
		add(homePageLinkContainer);
		
		if(entity == null)
		{
			add(new WebMarkupContainer("crumb"));
			
			homePageLinkContainer.add(
				new AttributeAppender(
					"class", true, new Model("homePageActive"), " "));
			
			return;
		}
			
		LinkedList<BreadcrumbListItem> breadcrumbs = getBreadcrumbs(entity);
		
		RepeatingView rv = new RepeatingView("crumb");
		
		add(rv);
		
		Iterator<BreadcrumbListItem> iterator = breadcrumbs.iterator();
		
		while(iterator.hasNext())
		{
			final BreadcrumbListItem item = iterator.next();
			
			Component next;
			
			StringBuilder cssClass = new StringBuilder("crumb");
			
			// last element is not clickable
			if(!iterator.hasNext())
			{
				next = new LabelFragment(rv.newChildId(), item.getName());
				
				cssClass.append(" lastCrumb");
			}
			else
			{
				next = new LinkFragment(
					rv.newChildId(), item.getName(), item.getEntity(), callback);
			}
			
			rv.add(next);
			
			if(cssClass != null)
			{
				next.add(new AttributeAppender("class", true, new Model(cssClass), " "));
			}
		}
	}
	
	/**
	 * Link to redirect to project overview.
	 */
	@SuppressWarnings("serial")
	private AjaxLink getHomePageLink(final EventHandler callback)
	{
		AjaxLink homeLink = new AjaxLink("home") 
		{	
			@Override
			public void onClick(AjaxRequestTarget target) 
			{
				EntityEvent.Selected(target, null).fire(callback);
			}
		};
		
		return homeLink;
	}
	
	private LinkedList<BreadcrumbListItem> getBreadcrumbs(EntityBase entity)
	{
		LinkedList<BreadcrumbListItem> breadcrumbs = 
			new LinkedList<BreadcrumbListItem>();
		
		int depth = 0;
		
		EntityBase processedEntity = entity;
		
		// populates the breadcrumb list with breadcrumbs
		while(processedEntity != null && depth < MAX_RECURSION_DEPTH)
		{	
			BreadcrumbListItem item;
			
			if(processedEntity instanceof Project)
			{
				Project project = (Project) processedEntity;
				
				item = new BreadcrumbListItem(project.getName(), processedEntity);
				
				breadcrumbs.addFirst(item);
				
				processedEntity = project.getParentProject();
			}
			else if(processedEntity instanceof Study)
			{
				Study study = (Study) processedEntity;
				
				item = new BreadcrumbListItem(study.getName(), processedEntity);
				
				breadcrumbs.addFirst(item);
				
				processedEntity = ((Study) processedEntity).getParentProject();
			}
			else
			{
				processedEntity = null;
			}
			
			depth++;
		}
		
		return breadcrumbs;
	}
}
