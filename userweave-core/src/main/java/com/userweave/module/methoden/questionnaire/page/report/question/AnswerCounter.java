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
package com.userweave.module.methoden.questionnaire.page.report.question;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class AnswerCounter<T> implements Serializable 
{
	private static final long serialVersionUID = 1L;

	private final Map<T, Integer> value2Count = new HashMap<T, Integer>();
	
	private int totalCount = 0;
	
	public void addAnswer(T value) 
	{		
		Integer oldCount = value2Count.get(value);
		if (oldCount == null) {
			oldCount = 0;
		}
		value2Count.put(value, oldCount + 1);
		
		totalCount++;
	}
	
	public int getCount(T value) 
	{
		Integer count = value2Count.get(value);
		if (count != null) {
			return count;
		} else {
			return 0;
		}
	}
	
	public int getTotalCount() 
	{
		return totalCount;
	}
	
}
