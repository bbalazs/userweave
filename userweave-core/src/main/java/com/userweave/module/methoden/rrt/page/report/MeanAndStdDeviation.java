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
package com.userweave.module.methoden.rrt.page.report;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.math.stat.descriptive.SummaryStatistics;

@SuppressWarnings("serial")
public class MeanAndStdDeviation implements Serializable {

	private double mean;

	private double stdDeviation;

	public MeanAndStdDeviation(List<Double> values) {
		SummaryStatistics stats = new SummaryStatistics();
		for(Double value : values) {
			stats.addValue(value);
		}
		
		this.mean = stats.getMean();
		this.stdDeviation = stats.getStandardDeviation();
	}
	
	public MeanAndStdDeviation(double mean, double stdDeviation) {
		super();
		this.mean = mean;
		this.stdDeviation = stdDeviation;
	}

	public double getMean() {
		return mean;
	}

	public void setMean(double mean) {
		this.mean = mean;
	}

	public double getStdDeviation() {
		return stdDeviation;
	}

	public void setStdDeviation(double stdDeviation) {
		this.stdDeviation = stdDeviation;
	}
}
