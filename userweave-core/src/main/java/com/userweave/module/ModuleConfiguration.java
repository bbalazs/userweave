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

import com.userweave.dao.filter.FilterFunctor;
import com.userweave.domain.Group;
import com.userweave.domain.LocalizedString;
import com.userweave.domain.Study;
import com.userweave.domain.util.Ordered;

// FIXME: remove generic T
public interface ModuleConfiguration<T extends ModuleConfiguration> extends Ordered<ModuleConfiguration>{

	public Integer getId();
	
	public String getName();
	public void setName(String name);
	
	public LocalizedString getDescription();
	
	// reference to study and module
	public Study getStudy();
	public void setStudy(Study study);
	public Module<ModuleConfiguration> getModule();

	public List<? extends Group> getGroups();
	public FilterFunctor getFilterFunctorForGroup(Group group);
	
	
	public ModuleConfigurationState getState();
	public void setState(ModuleConfigurationState state);
	
	public List<LocalizedString> getLocalizedStrings();
	
	// is already annotated in Ordered
	// public Integer getPosition();
	// public void setPosition(Integer position);
	
	public void save();
	
	public ModuleConfiguration copy();	
}
