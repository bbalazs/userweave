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
package com.userweave.module.methoden.iconunderstandability.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.userweave.domain.EntityBase;

@Entity
public class ScoreRangeList extends EntityBase {
	
	private static final long serialVersionUID = -4997891864605745970L;
	
	private transient final Logger logger = LoggerFactory.getLogger(ScoreRangeList.class);

	public enum ValueType {
		DEVIATION_FROM_MEAN_TIME("deviation_from_mean_time"),
		MISSING_VALUE("missing_value"),
		ASSIGNMENT("assignment"),
		REACTION_TIME("reaction_time"),
		HIGHEST_ASSIGNMENT_TO_OTHER_TERM("highest_assignment_to_other_term");
		
		private final String value;
		
		ValueType(String value) {
			this.value = value;			
		}
		
		@Override
		public String toString() {
			return value;
		}
	};

	@Transient
	public boolean isAscending() {
		
		Integer rangeA = null;
		Integer rangeB = null;
	
		for (Integer range : getRanges()) {
			if (range != null) {
				if (rangeA == null) {
					rangeA = range;
				} else {
					rangeB = range;
					break;
				}
			}
		}
		if ((rangeA != null) && (rangeB != null)) {
			return rangeA < rangeB;
		} else {
			return true;
		}
	}

	private ValueType type;

	@Enumerated
	public ValueType getType() {
		return type;
	}

	public void setType(ValueType type) {
		this.type = type;
	}
	
	public enum Alignment {
		NOT_SYMMETRIC("not_symmetric"), SYMMETRIC("symmetric");
		
		private final String value;
		
		Alignment(String value) {
			this.value = value;
		}
		
		@Override
		public String toString() {
			return value;
		}
	}
	
	private Alignment alignment = Alignment.NOT_SYMMETRIC;
	
	@Enumerated
	public Alignment getAlignment() {
		return alignment;
	}

	public void setAlignment(Alignment alignment) {
		this.alignment = alignment;
	}

	public Integer getScoreForValue(double value) {
		for (int count=0; count<11; count++) {
			if (getScoreInterval(count).contains(value)) {
				return getScore(count);
			}
		}
		return null;
	}
	
	@Transient
	private List<Integer> getScores() {
		List<Integer> rv = new ArrayList<Integer>();
		rv.add(score0);
		rv.add(score1);
		rv.add(score2);
		rv.add(score3);
		rv.add(score4);
		rv.add(score5);
		rv.add(score6);
		rv.add(score7);
		rv.add(score8);
		rv.add(score9);
		rv.add(score10);
		return rv;		
	}
	
	public void setScore(int index, Integer score) {
		switch (index) {
		case 0:
			setScore0(score);
			break;
		case 1:
			setScore1(score);
			break;
		case 2:
			setScore2(score);
			break;
		case 3:
			setScore3(score);
			break;
		case 4:
			setScore4(score);
			break;
		case 5:
			setScore5(score);
			break;
		case 6:
			setScore6(score);
			break;
		case 7:
			setScore7(score);
			break;
		case 8:
			setScore8(score);
			break;
		case 9:
			setScore9(score);
			break;
		case 10:
			setScore10(score);
			break;

		default:
			logger.error("score not found for index " + index);
			break;
		}
	}
	
	public Integer getScore(Integer index) {
		switch (index) {
		case 0:
			return score0;
		
		case 1:
			return score1;
		
		case 2:
			return score2;
		
		case 3:
			return score3;
		
		case 4:
			return score4;
		
		case 5:
			return score5;
		
		case 6:
			return score6;
		
		case 7:
			return score7;
		
		case 8:
			return score8;
		
		case 9:
			return score9;
		
		case 10:
			return score10;
		

		default:
			logger.error("score not found for index " + index);
			return -1;
		}
	}
	private Integer score0;

	public void setScore0(Integer  score) {
		this.score0 = score;		
	}

	private Integer score1;

	public void setScore1(Integer  score) {
		this.score1 = score;		
	}
	
	private Integer score2;

	public void setScore2(Integer  score) {
		this.score2 = score;		
	}
	
	private Integer score3;

	public void setScore3(Integer  score) {
		this.score3 = score;		
	}

	private Integer score4;

	public void setScore4(Integer  score) {
		this.score4 = score;		
	}
	
	private Integer score5;

	public void setScore5(Integer  score) {
		this.score5 = score;		
	}
	
	private Integer score6;

	public void setScore6(Integer  score) {
		this.score6 = score;		
	}
	
	private Integer score7;

	public void setScore7(Integer  score) {
		this.score7 = score;		
	}

	private Integer score8;

	public void setScore8(Integer  score) {
		this.score8 = score;		
	}
	
	private Integer score9;

	public void setScore9(Integer  score) {
		this.score9 = score;		
	}
	
	private Integer score10;

	public void setScore10(Integer  score) {
		this.score10 = score;		
	}
	

	private Integer range0;

	public void setRange0(Integer range) {	
		this.range0 = range;		
	}

	private Integer range1;

	public void setRange1(Integer range) {
		this.range1 = range;		
	}
	
	private Integer range2;

	public void setRange2(Integer range) {
		this.range2 = range;		
	}
	
	private Integer range3;

	public void setRange3(Integer range) {
		this.range3 = range;		
	}

	private Integer range4;

	public void setRange4(Integer range) {
		this.range4 = range;		
	}
	
	private Integer range5;

	public void setRange5(Integer range) {
		this.range5 = range;		
	}
	
	private Integer range6;

	public void setRange6(Integer range) {
		this.range6 = range;		
	}
	
	private Integer range7;

	public void setRange7(Integer range) {
		this.range7 = range;		
	}

	private Integer range8;

	public void setRange8(Integer range) {
		this.range8 = range;		
	}
	
	private Integer range9;

	public void setRange9(Integer range) {
		this.range9 = range;		
	}
	
	private Integer range10;

	public void setRange10(Integer range) {
		this.range10 = range;		
	}

	private Integer range11;

	public void setRange11(Integer range) {
		this.range11 = range;		
	}
	
	@Transient
	private List<Integer> getRanges() {
		List<Integer> rv = new ArrayList<Integer>();
		rv.add(range0);
		rv.add(range1);
		rv.add(range2);
		rv.add(range3);
		rv.add(range4);
		rv.add(range5);
		rv.add(range6);
		rv.add(range7);
		rv.add(range8);
		rv.add(range9);
		rv.add(range10);
		rv.add(range11);
		return rv;
		
	}
	
	public Integer getScore0() {
		return score0;
	}

	public Integer getScore1() {
		return score1;
	}

	public Integer getScore2() {
		return score2;
	}

	public Integer getScore3() {
		return score3;
	}

	public Integer getScore4() {
		return score4;
	}

	public Integer getScore5() {
		return score5;
	}

	public Integer getScore6() {
		return score6;
	}

	public Integer getScore7() {
		return score7;
	}

	public Integer getScore8() {
		return score8;
	}

	public Integer getScore9() {
		return score9;
	}

	public Integer getScore10() {
		return score10;
	}

	public Integer getRange0() {
		return range0;
	}

	public Integer getRange1() {
		return range1;
	}

	public Integer getRange2() {
		return range2;
	}

	public Integer getRange3() {
		return range3;
	}

	public Integer getRange4() {
		return range4;
	}

	public Integer getRange5() {
		return range5;
	}

	public Integer getRange6() {
		return range6;
	}

	public Integer getRange7() {
		return range7;
	}

	public Integer getRange8() {
		return range8;
	}

	public Integer getRange9() {
		return range9;
	}

	public Integer getRange10() {
		return range10;
	}

	public Integer getRange11() {
		return range11;
	}

	public Integer getRange(Integer index) {
		switch (index) {
		case 0:
			return range0;
		
		case 1:
			return range1;
		
		case 2:
			return range2;
		
		case 3:
			return range3;
		
		case 4:
			return range4;
		
		case 5:
			return range5;
		
		case 6:
			return range6;
		
		case 7:
			return range7;
		
		case 8:
			return range8;
		
		case 9:
			return range9;
		
		case 10:
			return range10;
			
		case 11:
			return range11;

		default:
			logger.error("range not found for index " + index);
			return -1;
		}
	}
	
	public void setRange(Integer index, Integer range) {
		switch (index) {
		case 0:
			setRange0(range);
			break;
		case 1:
			setRange1(range);
			break;
		case 2:
			setRange2(range);
			break;
		case 3:
			setRange3(range);
			break;
		case 4:
			setRange4(range);
			break;
		case 5:
			setRange5(range);
			break;
		case 6:
			setRange6(range);
			break;
		case 7:
			setRange7(range);
			break;
		case 8:
			setRange8(range);
			break;
		case 9:
			setRange9(range);
			break;
		case 10:
			setRange10(range);
			break;
		
		case 11:
			setRange11(range);
			break;
		
		default:
			logger.error("range not found for index " + index);
			break;
		}
	}

	public Interval getScoreInterval(int index) {
		return new Interval(getRange(index),getRange(index+1), index, 5, alignment, isAscending());		
	}
}
