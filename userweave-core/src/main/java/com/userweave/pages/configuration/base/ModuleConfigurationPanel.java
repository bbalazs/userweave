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
package com.userweave.pages.configuration.base;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.components.callback.EventHandler;
import com.userweave.components.navigation.breadcrumb.StateChangeTrigger;
import com.userweave.dao.StudyDao;
import com.userweave.domain.Study;
import com.userweave.domain.StudyState;
import com.userweave.domain.service.ModuleService;
import com.userweave.module.ModuleConfiguration;
import com.userweave.pages.components.slidableajaxtabpanel.ChangeTabsCallback;

/**
 * Wrapper panel to show either a configuration
 * ui or a report ui.
 * 
 * @author oma
 */
public class ModuleConfigurationPanel extends Panel 
{
	private static final long serialVersionUID = 1L;

	@SpringBean
	private ModuleService moduleService;

	@SpringBean
	private StudyDao studyDao;

	/**
	 * Default constructor
	 * 
	 * @param id
	 * 		Markup id.
	 * @param studyId
	 * 		Id of study this panel depends on.
	 * @param moduleConfigurationId
	 * 		Id of moduleConfiguration this panel depends on.
	 * @param callback
	 * 		Callback to delegate to conf or report ui.
	 * @param showConfig
	 * 		Determines if either the configuration or the
	 * 		report is shown.
	 * 
	 * TODO: replace tabbed panel with callback.
	 * @param tabbedPanel
	 * 		The tabbed panel this component belong to
	 * @param moduleIndex
	 * 		Index of the backed module in the method 
	 * 		list of the study
	 * @param filterFunctorCallback
	 * 		Callback to delegate filter conf to report ui.
	 */
	public ModuleConfigurationPanel(
			String id, 
			int studyId, 
			int moduleConfigurationId, 
			ChangeTabsCallback callback,
			StateChangeTrigger trigger,
			EventHandler addFilterCallback) 
	{
		super(id);
		
		add(createDefaultModuleConfigurationPanel(
				moduleConfigurationId, studyId, callback, trigger, addFilterCallback));
	}

	/**
	 * Creates the display component.
	 * 
	 * @param moduleConfigurationId
	 * @param studyId
	 * @param callback
	 * @param showConfig
	 * @param tabbedPanel
	 * @param moduleIndex
	 * @param filterFunctorCallback
	 * @return
	 */
	private Component createDefaultModuleConfigurationPanel(
			int moduleConfigurationId, 
			int studyId, 
			ChangeTabsCallback callback,
			StateChangeTrigger trigger,
			EventHandler addFilterCallback)
	{	
		ModuleConfiguration moduleConfiguration = getModuleConfiguration(moduleConfigurationId, studyId);		
		
		return moduleConfiguration
				.getModule()
					.getConfigurationUI(
						"module", moduleConfiguration, trigger, callback, addFilterCallback);
	}
	
	/**
	 * Get the backed module configuration.
	 * 
	 * @param moduleConfigurationId
	 * @param studyId
	 * @return
	 */
	private ModuleConfiguration getModuleConfiguration(int moduleConfigurationId, int studyId) 
	{
		Study study = studyDao.findById(studyId);
		
		ModuleConfiguration moduleConfiguration = moduleService.getModuleConfigurationForStudy(study, moduleConfigurationId);
	
		return moduleConfiguration;
	}
	
	/**
	 * Check, if the underlying study is in one of
	 * the given states.
	 * 
	 * @param studyStates
	 * 		List of study states.
	 * @return
	 * 		true, if study is in one of the given states.
	 */
	protected boolean studyIsInState(Study study, StudyState ... studyStates) 
	{
		return StudyState.studyIsInState(study.getState(), studyStates);
	}
}

