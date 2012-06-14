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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.userweave.application.setup.SetupBean;
import com.userweave.base.TestCaseBase;
import com.userweave.module.methoden.iconunderstandability.domain.Interval;
import com.userweave.module.methoden.iconunderstandability.domain.ScoreRangeList;
import com.userweave.module.methoden.iconunderstandability.domain.ScoreRangeList.Alignment;
import com.userweave.module.methoden.iconunderstandability.domain.ScoreRangeList.ValueType;


public class ScoreRangeListTest extends TestCaseBase {
	
	@Test
	public void testInterval() {
		ScoreRangeList scoreRangeList = SetupBean.createScoreRangeList(
				ValueType.DEVIATION_FROM_MEAN_TIME,
				Alignment.SYMMETRIC,
				new Integer[]{-40, -30, -20, -15, -10, -5, 5, 10, 15, 20, 30, null}, 
				null,
				true);
		
		// [-40, 30[ //
		Interval scoreInterval = scoreRangeList.getScoreInterval(0);		
		assertTrue(scoreInterval.contains(-40));
		assertFalse(scoreInterval.contains(-30));
		
		// [5, 5]//
		scoreInterval = scoreRangeList.getScoreInterval(5);
		assertTrue(scoreInterval.contains(-5));
		assertTrue(scoreInterval.contains(5));

		// ]20, 30] //
		scoreInterval = scoreRangeList.getScoreInterval(9);		
		assertTrue(scoreInterval.contains(30));
		assertFalse(scoreInterval.contains(20));
		
		// ]30, inf //
		scoreInterval = scoreRangeList.getScoreInterval(10);
		assertTrue(scoreInterval.contains(31));
		assertFalse(scoreInterval.contains(30));

		scoreRangeList = SetupBean.createScoreRangeList(
				ValueType.DEVIATION_FROM_MEAN_TIME,
				Alignment.NOT_SYMMETRIC,
				new Integer[]{null, -30, -20, -15, -10, -5, 5, 10, 15, 20, 30, null}, 
				null,
				true);
		
		// -inf, 30[ //
		scoreInterval = scoreRangeList.getScoreInterval(0);		
		assertTrue(scoreInterval.contains(-40));
		assertFalse(scoreInterval.contains(-30));
		
		// [5, 5[//
		scoreInterval = scoreRangeList.getScoreInterval(5);
		assertTrue(scoreInterval.contains(-5));
		assertFalse(scoreInterval.contains(5));

		// [20, 30[ //
		scoreInterval = scoreRangeList.getScoreInterval(9);		
		assertFalse(scoreInterval.contains(30));
		assertTrue(scoreInterval.contains(20));
		
		// [30, inf //
		scoreInterval = scoreRangeList.getScoreInterval(10);
		assertTrue(scoreInterval.contains(30));
		assertTrue(scoreInterval.contains(35));
	}
}
