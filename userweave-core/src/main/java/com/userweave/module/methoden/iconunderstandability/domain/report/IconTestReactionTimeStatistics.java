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
package com.userweave.module.methoden.iconunderstandability.domain.report;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.userweave.domain.EntityBase;

@Entity
@Table(name="itm_reaction_time_statistics")
public class IconTestReactionTimeStatistics extends EntityBase {
	
	private static final long serialVersionUID = -36511617924598985L;
	
	private int iconCount;
		
	@Column(unique=true)
	public int getIconCount() {
		return iconCount;
	}

	public void setIconCount(int iconCount) {
		this.iconCount = iconCount;
	}

	private double meanReactionTime;

	public double getMeanReactionTime() {
		return meanReactionTime;
	}

	public void setMeanReactionTime(double meanReactionTime) {
		this.meanReactionTime = meanReactionTime;
	}
	
	private double regressionValue;

	public double getRegressionValue() {
		return regressionValue;
	}

	public void setRegressionValue(double regressionValue) {
		this.regressionValue = regressionValue;
	}
}
