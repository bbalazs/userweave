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
import java.util.Locale;

import javax.persistence.Entity;
import javax.persistence.Transient;

import org.hibernate.annotations.CollectionOfElements;

@Entity
public class StudyLocalesGroup extends StudyGroup {
	private static final long serialVersionUID = -2939083163866319789L;

	private List<Locale> locales;

	@CollectionOfElements
	public List<Locale> getLocales() {
		return locales;
	}
	
	public void setLocales(List<Locale> locales) {
		this.locales = locales;
	}
	
	@Override
	@Transient
	public StudyLocalesGroup copy() {
		StudyLocalesGroup clone = new StudyLocalesGroup();
		super.copy(clone);
		if(locales != null) {
			clone.setLocales(new ArrayList<Locale>(locales));
		}
		return clone;
	} 

}
