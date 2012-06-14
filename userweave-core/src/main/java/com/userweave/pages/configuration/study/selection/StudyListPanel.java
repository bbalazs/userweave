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
package com.userweave.pages.configuration.study.selection;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

import com.userweave.components.callback.EventHandler;
import com.userweave.domain.Study;

/**
 * Panel to display a list of studies.
 * 
 * @author opr
 */
public class StudyListPanel extends Panel 
{	
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 * 
	 * @param id
	 * 		Component id.
	 * @param studiesList
	 * 		List of studies to display.
	 * @param callback
	 * 		Callback to fire on event.
	 */
	public StudyListPanel(String id, List<Study> studiesList, final EventHandler callback) 
	{
		super(id);
	
		init(studiesList, callback);
	}
	
	/**
	 * Initialize the list panel.
	 * 
	 * @param studiesList
	 * 		List of studies to display.
	 * @param callback
	 *		Callback to fire on click.
	 */
	protected void init(List<Study> studiesList, final EventHandler callback) 
	{
		Component displayComponent;
		
		/*
		 *  if no studies available, 
		 *  set a label as display component.
		 */
		Label label = new Label("noStudiesInList", 
				new StringResourceModel("no_studies_available", this, null));
		
		add(label);
		
		if (studiesList.size() == 0) 
		{
			label.setVisible(true);
			displayComponent = new WebMarkupContainer("studiesList");
		}
		else
		{
			label.setVisible(false);
			
			RepeatingView rv = new RepeatingView("studiesList");
			
			for(Study study : studiesList) 
			{
				final int studyId = study.getId();
				
				StudySelectionAccordionHeader selection = 
					new StudySelectionAccordionHeader(rv.newChildId(), studyId, callback);
				
				selection.add(new AttributeModifier("class", true, new Model("selectableItem")));
				rv.add(selection);
			}
			
			add(rv);
			
			displayComponent = rv;
		}
		
		add(displayComponent);
	}
}
