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
import java.util.List;

import com.userweave.domain.ImageBase;
import com.userweave.module.methoden.iconunderstandability.domain.ScoreRangeList;


@SuppressWarnings("serial")
public class IconReport implements Serializable {

	private final ScoreRangeList reactionTimeScoreRangeList;

	private final ScoreRangeList assignmentScoreRangeList;

	private final ScoreRangeList highestAssignmentToOtherTermScoreRangeList;

	private final List<TermAssignment> termAssignments;

	public IconReport(
			ImageBase image, 
			double assignment, 
			double highestAssignmentToOtherTerm,
			double reactionTime,
			List<TermAssignment> termAssignments,
			ScoreRangeList assignmentScoreRangeList,
			ScoreRangeList highestAssignmentToOtherTermScoreRangeList,
			ScoreRangeList reactionTimeScoreRangeList
			) {
		super();
		this.image = image;
		this.assignment = assignment;
		this.highestAssignmentToOtherTerm = highestAssignmentToOtherTerm;
		this.reactionTime = reactionTime;
		this.termAssignments = termAssignments;
		
		this.assignmentScoreRangeList = assignmentScoreRangeList;
		this.highestAssignmentToOtherTermScoreRangeList = highestAssignmentToOtherTermScoreRangeList;
		this.reactionTimeScoreRangeList = reactionTimeScoreRangeList;
	}

	private final ImageBase image;

	private final double assignment;
	
	private final double reactionTime;
	
	private final double highestAssignmentToOtherTerm;
	

	public ImageBase getImage() {
		return image;
	}
	
	public double getAssignment() {
		return assignment;
	}

	// assignmenta to all terms
	public List<TermAssignment> getTermAssignments() {
		return termAssignments;
	}
	
	public int getAssignmentRating() {
		if (assignment != -1) {
			return	assignmentScoreRangeList.getScoreForValue(assignment);
		} else {
			return 0;
		}
	}
	
	public double getReactionTime() {
		return reactionTime;
	}
	
	public int getReactionTimeRating() {
		return reactionTimeScoreRangeList.getScoreForValue(reactionTime);
	}
	
	public double getHighestAssignmentToOtherTerm() {
		return highestAssignmentToOtherTerm;
	}
	
	public int getHighestAssignmentToOtherTermRating() {
		return highestAssignmentToOtherTermScoreRangeList.getScoreForValue(highestAssignmentToOtherTerm);
	}
	
	/**
	 * Get highest assignment to other term rating in percent
	 * @return
	 */
	public double getHighestAssignmentToOtherTermRatingInPercent() {
		return 100 * getHighestAssignmentToOtherTermRating()/10; 
	}

	public double getTotalRating() {
		if (getAssignmentRating() != 0) {
			return (6*getAssignmentRating() + 2*getReactionTimeRating() + 2*getHighestAssignmentToOtherTermRating())/ 10.0;
		} else {
			return 0;
		}
	}
}
