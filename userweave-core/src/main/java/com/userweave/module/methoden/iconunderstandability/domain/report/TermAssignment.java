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
/**
 * 
 */
package com.userweave.module.methoden.iconunderstandability.domain.report;

import java.io.Serializable;

import com.userweave.domain.LocalizedString;

@SuppressWarnings("serial")
public class TermAssignment implements Serializable {
	public TermAssignment(LocalizedString term, double assignment) {
		super();
		this.term = term;
		this.assignment = assignment;
	}

	private final LocalizedString term;
	
	private final double assignment;

	public LocalizedString getTerm() {
		return term;
	}

	public double getAssignment() {
		return assignment;
	}
}