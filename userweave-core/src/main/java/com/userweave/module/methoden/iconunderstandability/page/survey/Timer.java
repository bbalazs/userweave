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
package com.userweave.module.methoden.iconunderstandability.page.survey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.userweave.domain.TimerTarget;

@SuppressWarnings("serial")
public class Timer implements TimerTarget, Serializable {
	
	private long startTime = -1;

	public long getStartTime() {
		return startTime;
	}

	public boolean startTimeIsSet() {
		return isValid(startTime);
	}
	
	public void setStartTime(long time) {		
		if (isValid(time)) {
			for (TimerTarget timerTarget : getTimerTargets()) {
				if (!timerTarget.startTimeIsSet()) {
					timerTarget.setStartTime(time);
				}
			}
		}
		this.startTime = time;		
	}

	private long endTime = -1;

	public boolean endTimeIsSet() {
		return isValid(endTime);
	}
	
	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long time) {	
		if (isValid(time)) {
			for (TimerTarget timerTarget : getTimerTargets()) {
				timerTarget.setEndTime(time);
			}
		}
		this.endTime = time;
	}

	public long getExecutionTime() {
		
		long executiontime;
		
		if (!isValid(getEndTime())) {
			executiontime = getCurrentTime() - getStartTime();
		} else {
			executiontime = getEndTime() - getStartTime();
		}
		return executiontime;
	}
	
	private boolean isValid(long time) {
		return time != -1;
	}

	public long getCurrentTime() {
		return System.currentTimeMillis();
	}

	public Timer setEndTimeNow() {
		setEndTime(getCurrentTime());	
		return this;
	}

	public void clear() {
		setStartTime(-1);
		setEndTime(-1);		
	}
	
	private final List<TimerTarget> timerTargets = new ArrayList<TimerTarget>();

	/**
	 * Delegate first call to setStartTime and last call to setEndTime to timerTarget.
	 * @param timerTarget
	 */
	public void connectTo(TimerTarget timerTarget) {
		timerTargets.add(timerTarget);		
	}

	protected List<TimerTarget> getTimerTargets() {
		return timerTargets;
	}	
}
