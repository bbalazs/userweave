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
package com.userweave.domain.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.userweave.dao.ConfigurationDao;
import com.userweave.domain.ModuleConfigurationEntityBase;
import com.userweave.domain.OrderedEntityBase;
import com.userweave.domain.Study;
import com.userweave.domain.service.ModuleService;
import com.userweave.domain.util.OrderedComparator;
import com.userweave.module.Module;
import com.userweave.module.ModuleConfiguration;
import com.userweave.module.ModuleConfigurationState;

@Service
@Transactional
public class ModuleServiceImpl implements ModuleService {
	@Resource(name="modules")
	private List<Module<?>> modules;

	@Resource(name="activeModules")
	private List<Module<?>> activeModules;
	
	@Resource(name="configurationDao")
	private ConfigurationDao configurationDao;

	@Override
	public List<Module<?>> getModules() {
		return modules;
	}

	@Override
	public List<Module<?>> getActiveModules() {
		return activeModules;
	}
	
	@Override
	public List<ModuleConfiguration> getSortedModuleConfigurationsForStudy(Study study) {
		return getSortedModuleConfigurationsForStudy(null, study);
	}
	
	@Override
	public List<ModuleConfiguration> getSortedModuleConfigurationsForStudy(ModuleConfigurationState state, Study study) {
		return getSortedModuleConfigurationsForStudy(modules, state, study);
	}
	
	public static List<ModuleConfiguration> getSortedModuleConfigurationsForStudy(List<Module<?>> modules, ModuleConfigurationState state,
			Study study) {
		Set<ModuleConfiguration> sortedConfigurations = new TreeSet<ModuleConfiguration>(new OrderedComparator());

		for (Module<?> module: modules) {
			for(ModuleConfiguration conf : module.getConfigurations(study)) {
				if(state == null || state == conf.getState()) {
					sortedConfigurations.add(conf);
				}
			}
		}

		return new ArrayList<ModuleConfiguration>(sortedConfigurations);
	}

	public Integer getMaxPosition(List<ModuleConfiguration> moduleConfigurations) {
		if(!moduleConfigurations.isEmpty()) {									
			return getLastModuleConfiguration(moduleConfigurations).getPosition();										
		}
		return 0;
	}

	private ModuleConfiguration getLastModuleConfiguration(List<ModuleConfiguration> moduleConfigurations) {
		if(!moduleConfigurations.isEmpty()) {	
			return moduleConfigurations.get(moduleConfigurations.size()-1);
		} else {
			return null;
		}
	}
	
	public List<ModuleConfiguration> getModuleConfigurationsForStudy(Study study) {
		List<ModuleConfiguration> moduleConfigurations = new ArrayList<ModuleConfiguration>();	
		moduleConfigurations.addAll(study.getModuleConfigurations(modules));
		return moduleConfigurations;
	}
	

	@Override
	public ModuleConfiguration getModuleConfigurationForStudy(Study study, int moduleConfigurationId) {
		List<ModuleConfiguration> moduleConfigurations = study.getModuleConfigurations(modules);		
		for (ModuleConfiguration moduleConfiguration : moduleConfigurations) {
			Integer id = moduleConfiguration.getId();
			if (id.equals(moduleConfigurationId)) {
				return moduleConfiguration;
			}
		}
		return null;
	}
	
	public Module<?> findModuleById(String moduleId) {
		for(Module<?> module : getModules()) {
			if(module.getModuleId().equals(moduleId)) {
			return module;
			}
		}
		return null;
	}
	
	public boolean activeConfigurationExists(List<ModuleConfiguration> modules) {
		for (ModuleConfiguration moduleConfiguration : modules) {
			if (moduleConfiguration.getState() == ModuleConfigurationState.ACTIVE) {
				return true;
			}
		}
		return false;
	}

	private int moveLastConfiguration(Study study) {
		List<ModuleConfiguration> moduleConfigurationsForStudy = getModuleConfigurationsForStudy(study);
	
		int newPosition = 0;
		ModuleConfiguration lastConfiguration = getLastModuleConfiguration(moduleConfigurationsForStudy);
		if (lastConfiguration != null) {
			newPosition = lastConfiguration.getPosition();
			lastConfiguration.setPosition(lastConfiguration.getPosition()+1);
			lastConfiguration.save();
		}
		return newPosition;
	}

	public ModuleConfiguration createNewConfigurationInStudyForModule(String moduleId, Study study) {		
		int newPosition = moveLastConfiguration(study);

		ModuleConfiguration newConfiguration = findModuleById(moduleId).create(study.getId(), newPosition);

		newConfiguration.setState(ModuleConfigurationState.ACTIVE);
		newConfiguration.save();
		return newConfiguration;
	}	

	public ModuleConfiguration createNewConfigurationInStudyForModule(ModuleConfiguration moduleConfiguration, Study study) {
		int newPosition = moveLastConfiguration(study);

		ModuleConfiguration newConfiguration = moduleConfiguration.copy();
		newConfiguration.setStudy(study);
		newConfiguration.setPosition(newPosition);	
		
		newConfiguration.setState(ModuleConfigurationState.ACTIVE);
		newConfiguration.save();
		return newConfiguration;
	}
	
	public void moveUp(List<ModuleConfiguration> moduleConfigurations, ModuleConfiguration moduleConfiguration) {
		OrderedEntityBase.moveUp(moduleConfigurations, moduleConfiguration);		
		saveConfigurations(moduleConfigurations);
	}


	public void moveDown(List<ModuleConfiguration> moduleConfigurations, ModuleConfiguration moduleConfiguration) {
		OrderedEntityBase.moveDown(moduleConfigurations, moduleConfiguration);	
		saveConfigurations(moduleConfigurations);
	}
	
	public void delete(List<ModuleConfiguration> moduleConfigurations, ModuleConfiguration moduleConfiguration) {
		// delete item from list
		moduleConfigurations.remove(moduleConfiguration);
		// delete item from data storage
		configurationDao.delete((ModuleConfigurationEntityBase)moduleConfiguration);
		
		// update list
		OrderedEntityBase.renumberPositions(moduleConfigurations);
		saveConfigurations(moduleConfigurations);
	}

	@Override
	public void delete(ModuleConfiguration moduleConfiguration) {
		List<ModuleConfiguration> moduleConfigurations = getModuleConfigurationsForStudy(moduleConfiguration.getStudy());
		delete(moduleConfigurations, moduleConfiguration);
	}

	private void saveConfigurations(List<ModuleConfiguration> moduleConfigurations) {
		for (ModuleConfiguration conf : moduleConfigurations) {
			configurationDao.save((ModuleConfigurationEntityBase)conf);		
		}
	}




}
