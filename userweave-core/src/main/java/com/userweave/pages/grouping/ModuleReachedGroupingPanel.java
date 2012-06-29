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

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.dao.StudyDao;
import com.userweave.domain.ModuleReachedGroup;
import com.userweave.domain.service.ModuleService;
import com.userweave.module.ModuleConfiguration;
import com.userweave.module.methoden.questionnaire.page.grouping.GroupAddedCallback;

/**
 * @author oma
 */
public class ModuleReachedGroupingPanel extends GroupingPanelWithName<ModuleReachedGroup> 
{
	private static final long serialVersionUID = 1L;

	@SpringBean
	private StudyDao studyDao;
	
	@SpringBean
	private ModuleService moduleService;
	
	private ModuleConfiguration selectedModuleConfiguration;

	public ModuleReachedGroupingPanel(
		String id, ModuleReachedGroup group, 
		Locale locale, final Integer studyId, 
		GroupAddedCallback groupAddedCallback) 
	{
		super(id, group, locale, groupAddedCallback);
	
		IModel activeModuleConfigurations = new LoadableDetachableModel() 
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected Object load() {
				return studyDao.findById(studyId).getActiveModuleConfigurations(moduleService.getModules());
			}
		};
		
		add(
			new DropDownChoice(
				"selectedModuleConfiguration", 
				new PropertyModel(this, "selectedModuleConfiguration"), 
				activeModuleConfigurations, 
				new ChoiceRenderer("name")
			)
		);
	}

	@Override
	protected IModel getTitle() 
	{
		return new StringResourceModel("module_reached", this, null);
	}

	@Override
	public void submit() 
	{
		ModuleReachedGroup group = getGroup();
		
//		group.setName(group.getName() + " (" +
//					  Integer.toString(selectedModuleConfiguration.getPosition()) +
//					  ")");
		
		group.setModulePosition(selectedModuleConfiguration.getPosition());
	}

	@Override
	protected boolean isStimulusVisible()
	{
		return false;
	}
}

