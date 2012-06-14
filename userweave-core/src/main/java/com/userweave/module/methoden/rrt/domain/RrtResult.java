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
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import com.userweave.domain.OrderedEntityBase;
import com.userweave.domain.TestResultEntityBase;

@Entity
@Table(name="rrt_Result")
public class RrtResult extends TestResultEntityBase<RrtConfigurationEntity> {
	
	private static final long serialVersionUID = -3782908695132764124L;

	private List<OrderedTerm> orderedTerms = new ArrayList<OrderedTerm>();
	
	@OneToMany
	@JoinColumn(name="result_id")
	@OrderBy(value="position")
	@Cascade(value={org.hibernate.annotations.CascadeType.ALL})
	public List<OrderedTerm> getOrderedTerms() {
		return orderedTerms;
	}
	
	public void setOrderedTerms(List<OrderedTerm> orderedTerms) {
		this.orderedTerms = orderedTerms;
	}
	
	public void addToOrderedTerms(OrderedTerm term) {
		orderedTerms.add(term);
		OrderedEntityBase.renumberPositions(orderedTerms);
	}

	public void removeFromOrderedTerms(RrtTerm term) {
		orderedTerms.remove(term);
		OrderedEntityBase.renumberPositions(orderedTerms);
	}

	public OrderedTerm getOrderedTerm(RrtTerm term) {
		for(OrderedTerm orderedTerm : getOrderedTerms()) {
			if(orderedTerm.getTerm().equals(term)) {
				return orderedTerm;
			}
		}
		return null;
	}
}
