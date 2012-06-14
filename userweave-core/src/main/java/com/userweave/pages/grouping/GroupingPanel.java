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
package com.userweave.pages.grouping;

import java.util.Locale;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.userweave.domain.Group;
import com.userweave.module.methoden.questionnaire.page.grouping.GroupAddedCallback;

/**
 * @author oma
 */
public abstract class GroupingPanel<T extends Group> extends Panel 
{
	private static final long serialVersionUID = 1L;

	private final FeedbackPanel feedback;

	private Label stimulus;
	
	protected Label getStimulus()
	{
		return stimulus;
	}
	
	public T getGroup() 
	{
		return group;
	}
	
	private final T group;
	
	public GroupingPanel(
			String id, 
			final T group, 
			final Locale locale, 
			final GroupAddedCallback groupAddedCallback) 
	{	
		super(id);
	
		this.group = group;
		
		setDefaultModel(new CompoundPropertyModel(new Model(group)));
		
		
		// Label to display a specific stimulus for methods
		add(stimulus = new Label("stimulus", new Model("")));
		
		stimulus.setVisible(isStimulusVisible());
		
		add(new Label("title", getTitle()));
		
		
		feedback = new FeedbackPanel("feedback");
		
		feedback.setOutputMarkupId(true);
		
		add(feedback);
	}
	
	protected boolean isStimulusVisible()
	{
		return false;
	}

	protected abstract IModel getTitle();

	public abstract void submit();
	

}

