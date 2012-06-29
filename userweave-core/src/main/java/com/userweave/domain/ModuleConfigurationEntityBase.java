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
package com.userweave.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;

import com.userweave.module.ModuleConfiguration;
import com.userweave.module.ModuleConfigurationImpl;
import com.userweave.module.ModuleConfigurationState;

@MappedSuperclass
public abstract class ModuleConfigurationEntityBase<T extends ModuleConfiguration> extends ModuleConfigurationImpl<T> {

	private static final long serialVersionUID = 758474412825157324L;
	
	// Name 	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// Study
	private Study study;
	
	@ManyToOne
	@JoinColumn(name = "studyid")
	public Study getStudy() {
		return study;
	}
	
	public void setStudy(Study study) {
		this.study =study;
	}
	
	// State
	private ModuleConfigurationState state = ModuleConfigurationState.INACTIVE;
	
	@Enumerated
	public ModuleConfigurationState getState() {
		return state;
	}
	
	public void setState(ModuleConfigurationState state) {
		this.state = state;
	}
	
	private LocalizedString description;
	
	@OneToOne
	@Cascade(value={org.hibernate.annotations.CascadeType.ALL})
	public LocalizedString getDescription() {
		return description;
	}

	public void setDescription(LocalizedString description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	@Override
	@Transient
	public List<LocalizedString> getLocalizedStrings() {
		List<LocalizedString> rv = new ArrayList<LocalizedString>();
		rv.add(getDescription());
		return rv;
	}
	
	@Transient
	protected ModuleConfigurationEntityBase copy(ModuleConfigurationEntityBase clone) {
		super.copy(clone);
		if(description != null) {
			clone.setDescription(description.copy());
		}
		clone.setName(name);
		clone.setState(state);
		clone.setStudy(study);
		return clone;
	}

}
