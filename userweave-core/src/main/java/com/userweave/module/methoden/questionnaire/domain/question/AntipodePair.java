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
package com.userweave.module.methoden.questionnaire.domain.question;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;

import com.userweave.domain.LocalizedString;
import com.userweave.domain.OrderedEntityBase;
import com.userweave.domain.util.LocalizedAnswer;
import com.userweave.utils.LocalizationUtils;

@Entity
public class AntipodePair extends OrderedEntityBase<AntipodePair> implements LocalizedAnswer {

	private static final long serialVersionUID = 3211294589391946744L;

	private LocalizedString antipode1;
	
	@OneToOne
	@Cascade(value={org.hibernate.annotations.CascadeType.ALL})
	public LocalizedString getAntipode1() {
		return antipode1;
	}

	public void setAntipode1(LocalizedString antipode1) {
		this.antipode1 = antipode1;
	}

	private LocalizedString antipode2;
	
	@OneToOne
	@Cascade(value={org.hibernate.annotations.CascadeType.ALL})
	public LocalizedString getAntipode2() {
		return antipode2;
	}

	public void setAntipode2(LocalizedString antipode2) {
		this.antipode2 = antipode2;
	}
	
	public String toString(Locale locale) {	
		return LocalizationUtils.getValue(antipode1, locale) + " : " + LocalizationUtils.getValue(antipode2, locale);
	}
	
	@Override
	public String toString() {	
		return antipode1 + " : " + antipode2;
	}

	@Transient
	public List<LocalizedString> getLocalizedStrings() {
		List<LocalizedString> rv = new ArrayList<LocalizedString>();
		rv.add(getAntipode1());
		rv.add(getAntipode2());
		return rv;
	}

	@Transient
	public AntipodePair copy() {
		AntipodePair clone = new AntipodePair();

		super.copy(clone);

		if(antipode1 != null) {
			clone.setAntipode1(antipode1.copy());
		}
		if(antipode2 != null) {
		clone.setAntipode2(antipode2.copy());
		}

		return clone;
	}
	
	@Transient
	public LocalizedString getLocalized() {
		return antipode1;
	}
}
