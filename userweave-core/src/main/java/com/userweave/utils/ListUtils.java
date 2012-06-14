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
package com.userweave.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ListUtils {
	
	public static <T> List<T> mixListOrder(List<T> list) {
		return mixListOrder(list, null);		
	}
	
	public static <T> List<T> mixListOrder(List<T> list, List<Integer> order) {
		
		if (order == null) {
			order = new ArrayList<Integer>();
		}
		
		if (order.isEmpty()) {
			Random rand = new Random();			
	 		for (int index=0; index < list.size(); index++) {
	 			order.add(rand.nextInt(list.size() - index));
			}
		} else {
		}
		
		return applyOrder(list, order);
	}

	private static <T> List<T> applyOrder(List<T> list, List<Integer> randomIndices) {
		// copy images to temp list
		List<T> tmpList = new ArrayList<T>(list);
		
		List<T> result = new ArrayList<T>();		
		for (int index=0; index < list.size(); index++) {			
			int randomIndex = randomIndices.get(index);
			result.add(tmpList.get(randomIndex));
			tmpList.remove(randomIndex);
		}
		return result;
	}
	
	
}
