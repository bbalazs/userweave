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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.userweave.domain.LocalizedString;
import com.userweave.module.methoden.iconunderstandability.domain.ScoreRangeList;

@SuppressWarnings("serial")
public class TermReport implements Serializable {

	private LocalizedString term;

	private final ScoreRangeList missingValueScoreRangeList;

	private final ScoreRangeList deviationFromMeanProcessingTimeScoreRangeList;
	
	public TermReport(LocalizedString term, double deviationFromMeanProcessingTime, double missingValue, ScoreRangeList deviationFromMeanProcessingTimeScoreRangeList,
			ScoreRangeList missingValueScoreRangeList) {
		super();
		this.deviationFromMeanProcessingTime = deviationFromMeanProcessingTime;
		this.missingValue = missingValue;
		this.term = term;
		this.missingValueScoreRangeList = missingValueScoreRangeList;
		this.deviationFromMeanProcessingTimeScoreRangeList = deviationFromMeanProcessingTimeScoreRangeList;
	}

	public LocalizedString getTerm() {
		return term;
	}

	public void setTerm(LocalizedString term) {
		this.term = term;
	}

	private double deviationFromMeanProcessingTime;
	
	private double missingValue;
	
	private List<IconReport> iconReports = new ArrayList<IconReport>();

	private double indicator;
	
	public double getDeviationFromMeanProcessingTime() {
		return deviationFromMeanProcessingTime;
	}
	
	public void setDeviationFromMeanProcessingTime(double deviationFromMeanProcessingTime) {
		this.deviationFromMeanProcessingTime = deviationFromMeanProcessingTime;
	}
	
	public int getDeviationFromMeanProcessingTimeRating() {
		Integer scoreForValue = deviationFromMeanProcessingTimeScoreRangeList.getScoreForValue(getDeviationFromMeanProcessingTime());
		if (scoreForValue != null) {
			return scoreForValue;
		} else {
			return -1;
		}
	}
	
	public double getMissingValue() {
		return missingValue;
	}

	public int getMissingValueRating() {
		if (missingValue != -1) {
			Integer score = missingValueScoreRangeList.getScoreForValue(missingValue);
			if (score != null) {
				return score;
			} else {
				return 0;
			}
		} 
		
		return 0;		
	}
	
	/**
	 * 
	 * @return
	 */
	public double getMissingValueRatingInPercent() {
		return 100 * getMissingValueRating()/10;		
	}
	
	
	public void setMissingValue(double missingValue) {
		this.missingValue = missingValue;
	}
	
	public void addToIconReports(IconReport iconReport) {
		iconReports.add(iconReport);
	}
	
	public List<IconReport> getIconReports() {
		return iconReports;
	}
	
	public void setIconReports(List<IconReport> iconReports) {
		this.iconReports = iconReports;
	}

	public  List<IconReport> getBestMatchingIconReports() {
		List<IconReport> sortedIconReports = new ArrayList<IconReport>();
		sortedIconReports.addAll(iconReports);

		Collections.sort( sortedIconReports, 
			new Comparator<IconReport>() {
				public int compare(IconReport report1, IconReport report2) {
					return Double.compare(report2.getTotalRating(), report1.getTotalRating());
				}
			}
		);
		
		return sortedIconReports;
	}

	public  List<IconReport> getBestMatchingIconReports(int numberOfReports) {
		List<IconReport> bestMatchingIconReports = getBestMatchingIconReports();
		if (numberOfReports < bestMatchingIconReports.size()) {
			return bestMatchingIconReports.subList(0, numberOfReports);
		} else {
			return bestMatchingIconReports;
		}
	}

	public int getNumberOfIcons() {
		return getIconReports().size();
	}

	public double getIndicator() {
		return 0.5 * (getMissingValueRating() + getDeviationFromMeanProcessingTimeRating());
	}

}
