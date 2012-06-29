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
package com.userweave.module.methoden.questionnaire.domain.group;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@MappedSuperclass
public abstract class RatingGroup extends QuestionnaireGroup {
	
	private static final long serialVersionUID = -3614078022996175843L;
	
	private Boolean missing = false;
	
	public Boolean getMissing() {
		return missing;
	}

	public void setMissing(Boolean missing) {
		this.missing = missing;
	}


	private Integer lowerBound = null;

	public Integer getLowerBound() {
		return lowerBound;
	}

	public void setLowerBound(Integer lowerBound) {
		this.lowerBound = lowerBound;
	}
	
	
	private Integer upperBound = null;

	public Integer getUpperBound() {
		return upperBound;
	}

	public void setUpperBound(Integer upperBound) {
		this.upperBound = upperBound;
	}
	
	@Transient
	public RatingGroup copy(RatingGroup clone) {
		super.copy(clone);
		
		clone.setLowerBound(lowerBound);
		clone.setMissing(missing);
		clone.setUpperBound(upperBound);
		
		return clone;
	}

}
