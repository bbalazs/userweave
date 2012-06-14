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
package com.userweave.module.methoden.iconunderstandability.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.userweave.domain.LocalizedString;
import com.userweave.domain.TestResultEntityBase;
import com.userweave.module.methoden.iconunderstandability.page.survey.IconTermMapping;

@Entity
@Table(name="itm_result")
public class ITMTestResult extends TestResultEntityBase<IconTermMatchingConfigurationEntity> {

	private static final long serialVersionUID = 3918946084876859314L;
	
	private List<IconTermMapping> iconTermMappings = new ArrayList<IconTermMapping>();
	
	@OneToMany( cascade={CascadeType.ALL}, mappedBy="itmTestResult")
	public List<IconTermMapping> getIconTermMappings() {
		return iconTermMappings;
	}

	public void addToIconTermMappings(IconTermMapping iconTermMapping) {
		iconTermMappings.add(iconTermMapping);
	}
	
	public void setIconTermMappings(List<IconTermMapping> iconTermMappings) {
		this.iconTermMappings = iconTermMappings;
	}

	@Transient
	public IconTermMapping getIconTermMapping(LocalizedString term) {
		for(IconTermMapping mapping: getIconTermMappings()) {
			if(mapping.getTerm().equals(term)) {
				return mapping;
			}
		}
		return null;
	}

	@Transient
	public IconTermMapping getIconTermMapping(ITMImage image) {
		for(IconTermMapping mapping: getIconTermMappings()) {
			if(mapping.getIcon().equals(image)) {
				return mapping;
			}
		}
		return null;
	}

}
