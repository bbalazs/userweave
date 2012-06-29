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

import java.util.Collections;
import java.util.List;

import com.userweave.application.SpringApplicationContext;
import com.userweave.dao.EmptyFilterFunctor;
import com.userweave.dao.filter.FilterFunctor;
import com.userweave.domain.Group;
import com.userweave.domain.OrderedEntityBase;

@SuppressWarnings("serial")
public abstract class ModuleConfigurationImpl<T extends ModuleConfiguration> 
	extends OrderedEntityBase<ModuleConfiguration> 
	implements ModuleConfiguration<ModuleConfigurationImpl> 
{
	
	public String getStudyStructureId() {
		return getSpringApplicationContextName() + "_" + getId();
	}
	
	@Override
	public List<? extends Group> getGroups() {
		return Collections.emptyList();
	}
	
	@Override
	public FilterFunctor getFilterFunctorForGroup(Group group) {		
		return new EmptyFilterFunctor();
	}
	
	@Override
	public void save() {
		getModule().save(this);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Module<ModuleConfiguration> getModule() {
		String beanName = getSpringApplicationContextName();
			Object bean = SpringApplicationContext.getBean(beanName);
			return  (Module<ModuleConfiguration>) bean;
	}

	protected abstract String getSpringApplicationContextName();	

}