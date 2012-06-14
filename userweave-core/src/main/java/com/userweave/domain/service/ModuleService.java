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
package com.userweave.domain.service;

import java.util.List;

import com.userweave.domain.Study;
import com.userweave.module.Module;
import com.userweave.module.ModuleConfiguration;
import com.userweave.module.ModuleConfigurationState;

public interface ModuleService {

	/**
	 * Get all configured Modules.
	 * @return
	 */
	public List<Module<?>> getModules();

	/**
	 * get all active modules. This is a subset of getModules
	 * @return
	 */
	List<Module<?>> getActiveModules();
	
	public Module<?> findModuleById(String moduleId);

	public List<ModuleConfiguration> getModuleConfigurationsForStudy(Study study);

	/**
	 *
	 * @param state
	 * @return List of all module configurations for this study having the given state
	 * (state == null means: all configurations)
	 */
	public List<ModuleConfiguration> getSortedModuleConfigurationsForStudy(ModuleConfigurationState state, Study study);
	
	public List<ModuleConfiguration> getSortedModuleConfigurationsForStudy(Study study);
	
	public Integer getMaxPosition(List<ModuleConfiguration> moduleConfigurations);
	
	public boolean activeConfigurationExists(List<ModuleConfiguration> modules);
	
	public ModuleConfiguration createNewConfigurationInStudyForModule(String moduleId, Study study);	

	public ModuleConfiguration createNewConfigurationInStudyForModule(ModuleConfiguration moduleConfiguration, Study study);
		
	public void moveUp(List<ModuleConfiguration> moduleConfigurations, ModuleConfiguration moduleConfiguration);
	
	public void moveDown(List<ModuleConfiguration> moduleConfigurations, ModuleConfiguration moduleConfiguration);
	
	/**
	 * Delete given ModulConfiguration object from given list an from data storage
	 * @param moduleConfigurations
	 * @param moduleConfiguration
	 */
	public void delete(List<ModuleConfiguration> moduleConfigurations, ModuleConfiguration moduleConfiguration);

	public void delete(ModuleConfiguration moduleConfiguration);
	
	public ModuleConfiguration getModuleConfigurationForStudy(Study study, int moduleConfigurationId);
	
	//public void copy(ModuleConfiguration )


}