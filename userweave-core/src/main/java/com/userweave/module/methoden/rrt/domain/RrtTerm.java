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
package com.userweave.module.methoden.rrt.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;

import com.userweave.domain.LocalizedString;
import com.userweave.domain.OrderedEntityBase;


@Entity
@Table(name="rrt_Term")
public class RrtTerm  extends OrderedEntityBase<RrtTerm> {
	
	private static final long serialVersionUID = 1L;

	private LocalizedString value;

	@OneToOne
	@Cascade(value={org.hibernate.annotations.CascadeType.ALL})
	public LocalizedString getValue() {
		return value;
	}

	public void setValue(LocalizedString value) {
		this.value = value;
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
	
	@Transient
	public List<LocalizedString> getLocalizedStrings() {
		List<LocalizedString> rv = new ArrayList<LocalizedString>();
		rv.add(value);
		rv.add(description);
		return rv;
	}

	@Transient
	public RrtTerm copy() {
		RrtTerm clone = new RrtTerm();
		super.copy(clone);
		if(description != null) {
			clone.setDescription(description.copy());
		}
		if(value != null) {
			clone.setValue(value.copy());
		}
		
		return clone;
	}
}
