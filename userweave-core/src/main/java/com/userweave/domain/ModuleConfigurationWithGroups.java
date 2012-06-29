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

import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.userweave.module.ModuleConfiguration;

@MappedSuperclass
public abstract class ModuleConfigurationWithGroups<T extends ModuleConfiguration<?>, U extends TestResultEntityBase<?>, G extends Group> extends ModuleConfigurationWithResultsEntity<T,U> {
	
	private static final long serialVersionUID = -8217578599192259160L;
	
	private List<G> groups = new ArrayList<G>();

	public void setGroups(List<G> groups) {
		this.groups = groups;
	}

	@Override
	@OneToMany
	@Cascade(value={CascadeType.ALL})
	public List<G> getGroups() {
		return groups;
	}
	
	public void removeFromGroups(G group) {
		groups.remove(group);
	}

	/* this has to be done by the children itself
	@Transient
	public ModuleConfigurationWithGroups<T, U, G> copy(ModuleConfigurationWithGroups<T, U, G> clone) {
		super.copy(clone);

		if(groups != null) {
			List<Group> cloneGroups = new ArrayList<Group>();
			for(Group group : groups) {
				Group cloneGroup = group.copy();
				cloneGroups.add(cloneGroup);
			}
			clone.setGroups((List<G>) cloneGroups);
		}
		
		return clone;
	}
*/
}
