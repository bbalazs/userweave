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

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.userweave.domain.OrderedEntityBase;

@Entity
@Table(name="rrt_OrderedTerm")
public class OrderedTerm extends OrderedEntityBase<OrderedTerm> {

	private static final long serialVersionUID = 6035379527849943702L;
	
	private RrtTerm term;

	public OrderedTerm() {}
	
	public OrderedTerm(RrtTerm term, Integer position) {
		super(position);
		this.term = term;
	}

	@OneToOne
	@JoinColumn(name="termId")
	public RrtTerm getTerm() {
		return term;
	}

	public void setTerm(RrtTerm term) {
		this.term = term;
	}
}
