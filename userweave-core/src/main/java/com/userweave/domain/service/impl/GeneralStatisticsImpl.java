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
package com.userweave.domain.service.impl;

import java.text.DecimalFormat;
import java.util.Formatter;

import org.apache.commons.math.stat.descriptive.SummaryStatistics;

import com.userweave.domain.service.GeneralStatistics;

@SuppressWarnings("serial")
public class GeneralStatisticsImpl implements GeneralStatistics {
	
	private final Long average;
	//private final Long maximum;
	//private final Long minimum;

	private final Double deviation;

	private       Integer overallStarted;
	private final Integer started;
	private final Integer finished;

	
	public GeneralStatisticsImpl(SummaryStatistics stats, Integer overallStarted, Integer started, Integer finished) {
		this(overallStarted, started, finished, 
				stats == null ? 0L : Double.valueOf(stats.getMean()).longValue(), 
				stats == null ? 0L : stats.getStandardDeviation());
	}
	public GeneralStatisticsImpl(Integer overallStarted, Integer started,	Integer finished, Long average, Double deviation) {
		this.started        = started;
		this.finished       = finished;
		this.overallStarted = overallStarted;
		this.average        = average; 
		this.deviation      = deviation;
	}

	@Override
	public Long getAverage() {
		return average;
	}

	@Override
	public Double getDeviation() {
		return deviation;
	}

	@Override
	public Integer getDropout() {
		if(getStarted() != null && getStarted() != 0) {
			return Double.valueOf((getStarted()-getFinished())*100.0/getStarted()).intValue();
		} else {
			return 0;
		}
	}
	
	@Override
	public Integer getFinished() {
		return finished;
	}

	@Override
	public Integer getStarted() {
		return started;
	}


	@Override
	public String getAverageToMinute() {
		long seconds = getAverage()%60;
		
		long minutes = getAverage()/60L;
		
		return new Formatter().format("%d:%02d",minutes,seconds).toString();
	}


	@Override
	public String getDeviationToMinute() {
		if(getDeviation()>0d) {
			return new DecimalFormat("###########.##").format(getDeviation()/60);
		} else {
			return "0,00";
		}
	}

	@Override
	public Integer getStartedToPercent() {
		if(overallStarted != null && overallStarted != 0) {
			return started*100/overallStarted;
		} else {
			return 100;
		}
	}

	@Override
	public Integer getFinishedToPercent() {
		if(overallStarted != null && overallStarted != 0) {
			return finished*100/overallStarted;
		} else {
			return 100;
		}
	}

	@Override
	public Integer getOverallStarted() {
		return overallStarted;
	}
	@Override
	public void setOverallStarted(Integer overallStarted) {
		this.overallStarted = overallStarted;
	}

}
