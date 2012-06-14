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
package com.userweave.module.methoden.freetext.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;

import com.userweave.domain.LocalizedString;
import com.userweave.domain.ModuleConfigurationWithResultsEntity;
import com.userweave.module.methoden.freetext.FreeTextMethod;

@Entity
@Table(name="freetext_configuration")
public class FreeTextConfigurationEntity extends ModuleConfigurationWithResultsEntity<FreeTextConfigurationEntity, FreeTextResult> {

	private static final long serialVersionUID = 1L;

	@Override
	@Transient
	protected String getSpringApplicationContextName() {
		return FreeTextMethod.moduleId;
	}

	private LocalizedString freetext = new LocalizedString();
	
	@OneToOne
	@Cascade(value={org.hibernate.annotations.CascadeType.ALL})
	public LocalizedString getFreeText() {
		return freetext;
	};
	
	public void setFreeText(LocalizedString freetext) {
		this.freetext = freetext;
	}
	
	@Override
	@Transient
	public List<LocalizedString> getLocalizedStrings() {
		List<LocalizedString> rv = super.getLocalizedStrings();
		rv.add(getFreeText());
		return rv;
	}

	@Override
	@Transient
	public FreeTextConfigurationEntity copy() {
		FreeTextConfigurationEntity clone = new FreeTextConfigurationEntity();
		super.copy(clone);
		if(freetext != null) {
			clone.setFreeText(freetext.copy());
		}
		return clone;
	}
}
