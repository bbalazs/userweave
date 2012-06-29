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

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;

import com.userweave.domain.LocalizedString;
import com.userweave.domain.OrderedEntityBase;
import com.userweave.domain.util.LocalizedAnswer;

@Entity
@Table(name="questionnaire_ratingterm")
public class RatingTerm extends OrderedEntityBase<RatingTerm> implements LocalizedAnswer {
	
	private static final long serialVersionUID = -4423096858962683442L;

	public RatingTerm() {};
	
	public RatingTerm(LocalizedString text) {
		this.text = text;
	};
	
	private LocalizedString text;

	@OneToOne
	@Cascade(value={org.hibernate.annotations.CascadeType.ALL})
	public LocalizedString getText() {
		return text;
	}

	public void setText(LocalizedString text) {
		this.text = text;
	}	
	
	@Transient
	public List<LocalizedString> getLocalizedStrings() {
		List<LocalizedString> rv = new ArrayList<LocalizedString>();
		rv.add(text);
		return rv;
	}

	@Transient
	public RatingTerm copy() {
		RatingTerm clone = new RatingTerm();
		super.copy(clone);
		if(text != null) {
			clone.setText(text.copy());
		}
		
		return clone;
	}
	
	@Transient
	public LocalizedString getLocalized() {
		return text;
	}
}
