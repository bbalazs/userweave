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
package com.userweave.module;

import java.util.List;
import java.util.Locale;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.panel.Panel;

import com.userweave.application.OnFinishCallback;
import com.userweave.components.callback.EventHandler;
import com.userweave.components.navigation.breadcrumb.StateChangeTrigger;
import com.userweave.dao.filter.FilterFunctor;
import com.userweave.domain.Study;
import com.userweave.domain.service.GeneralStatistics;
import com.userweave.pages.components.slidableajaxtabpanel.ChangeTabsCallback;
import com.userweave.pages.test.SurveyUI;

public interface Module<T extends ModuleConfiguration> {
	
	/**
	 * return the unique module id 
	 * @return
	 */
	public String getModuleId();
	
	public String getName();
	
	public String getShortDescription();
	
	/**
	 * get configuration user interface
	 * @param id
	 * @param configuration 
	 * @param structure configure configuration with (sub-)structure structure
	 * @param structureSelectionOrChangeListener listener to notify, if study structure changes
	 * @param onUpdateOrDeleteCallback
	 * @return
	 */
	public Panel getConfigurationUI(
			String id, 
			T configuration,
			StateChangeTrigger trigger,
			ChangeTabsCallback callback,
			EventHandler addFilterCallback);
//			EventHandler callback,
//			SlidableAjaxTabbedPanel tabbedPanel,
//			final int moduleIndex,
//			FilterFunctorCallback filterFunctorCallback);
	
	/**
	 * Get survey-interface for survey and configuration
	 * @param id
	 * @param surveyExecutionID id of running survey
	 * @param configuration
	 * @return
	 */
	public SurveyUI<?> getTestUI(String id, int surveyExecutionID, T configuration, OnFinishCallback onFinishCallback, Locale locale); // Model von T
	
	/**
	 * True if module stores some results. E.g. FreeTextConfiguration
	 * does not have a survey result, therefore it must return false.
	 *	
	 * @return 
	 */
	public boolean storesResults();
	
	public Component getGroupingUI(String id, T configuration);
	
	public boolean hasGroupingUI();
	
	
	/**
	 * Get all module configurations for study
	 * @param study
	 * @return
	 */
	public List<T> getConfigurations(Study study);
	
	/**
	 * create a new {@link ModuleConfiguration} for this module  
	 * @param studyId
	 * @param position
	 * @return
	 */
	public T create(int studyId, Integer position);
	
	/**
	 * delete a {@link ModuleConfiguration} of this module
	 * @param configuration
	 */
	public void delete(T configuration);
	
	/**
	 * save a {@link ModuleConfiguration} of this module
	 * @param configuration
	 */
	public void save(T configuration);

	/**
	 * @return all valid results based on the given filter functor
	 */
	public GeneralStatistics getValidResultStatistics(ModuleConfigurationWithResults moduleConfiguration, FilterFunctor filter);
}
