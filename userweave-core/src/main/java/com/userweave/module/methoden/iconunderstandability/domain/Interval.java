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

import com.userweave.module.methoden.iconunderstandability.domain.ScoreRangeList.Alignment;

public class Interval {
	private int index = 0;

	private final Alignment alignment;

	private final int middle;

	private final boolean ascending;
	
	public Interval(Integer from, Integer to, int index, int middle, Alignment alignment, boolean ascending) {
		super();
		this.from = from;
		this.to = to;
		this.index = index;
		this.middle = middle;
		this.alignment = alignment;
		this.ascending = ascending;
	}
	
	public Integer from;
	
	public Integer to;

	public Integer getFrom() {
		return from;
	}

	public Integer getTo() {
		return to;
	}
	
	public String getFromString() {
		String rv = null;
		if (from == null) {
			if (ascending) {
				rv = "-inf";
			} else {
				rv = "inf";
			}
		} else {
			rv = Integer.toString(from);
		}
		
		if (alignment == Alignment.NOT_SYMMETRIC) {
			rv = "[" + rv;
		} else {
			if (index <= middle) {
				rv = "[" + rv;
			} else {
				rv = "]" + rv;
			}
		}
		return rv;
	}
	
	public String getToString() {
		String rv = null;
		if (to == null) {
			if (ascending) {
				rv = "inf";
			} else {
				rv = "-inf";
			}
		} else {
			rv = Integer.toString(to);
		}
		
		if (alignment == Alignment.NOT_SYMMETRIC) {
			rv = rv + "[";
		} else {
			if (index < middle) {
				rv = rv + "[";
			} else {
				rv = rv + "]";
			}
		}
		return rv;
	}
	
	public boolean contains(double value) {		
		return inFrom(value) ? inTo(value) : false;		
	}
	
	public boolean inFrom(double value) {
		if (from == null) {
			return true;
		} 
		
		if (alignment == Alignment.SYMMETRIC) {
			if ((index <= middle) && (value >= from)) {
				return true;
			} else if ((index> middle) && (value > from)) {
				return true;
			} 
			
		}	
		else if ((value >= from)) {
				return true;
		}		
		return false;
	}
	
	public boolean inTo(double value) {
		if (to == null) {
			return true;
		} 		
		
		if (alignment == Alignment.SYMMETRIC) {
			if ((value <= middle) && (value < to)) {
				return true;
			} else if ((value >= middle) && (value <= to)) {
				return true;
			} 
		}	
		else if ((value < to)) {
				return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return getFromString() + " : " + getToString();
	}
}
