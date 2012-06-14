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
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * This comparator glues two comparators together.
 * 
 * <p>
 * In order to overcome the inconsistency with equals this comparator allows the
 * introduction of a sub-comparator which is applied in cases of equality.
 * </p>
 */
public class GroupingComparator<T> implements Comparator<T> {

	protected Comparator<T> main = null;
	protected GroupingComparator<T> sub = null;

	/**
	 * Creates a new <tt>GroupingComparator</tt> object.
	 * 
	 * @param mainComparator
	 *            the comparator used to compare the given objects
	 * @param subComparator
	 *            the comparator used to compare the given objects if
	 *            mainComparator delivers 0.
	 */
	public GroupingComparator(Comparator<T> mainComparator,
			GroupingComparator<T> subComparator) {
		if (mainComparator == null)
			throw new NullPointerException();
		main = mainComparator;
		sub = subComparator;
	}

	public GroupingComparator(Comparator mainComparator,
			Comparator subComparator) {
		this(mainComparator, new GroupingComparator<T>(subComparator, null));
	}

	// public static GroupingComparator buildFromList(Comparator[] comparators)
	// {
	// return buildFromList(Arrays.asList(comparators));
	// }

	// private static GroupingComparator buildFromList(List comparators) {
	// if(comparators == null || comparators.isEmpty()) {
	// return null;
	// }

	// return new GroupingComparator((Comparator) comparators.remove(0),
	// buildFromList(comparators));
	// }

	/**
	 * Compares its two arguments.
	 */
	public int compare(T o1, T o2) {
		int ret = main.compare(o1, o2);
		return ret == 0 && sub != null ? sub.compare(o1, o2) : ret;
	}

	public static GroupingComparator<?> build(List<Comparator<?>> comparators) {
		List<Comparator<?>> copyList = new ArrayList<Comparator<?>>(comparators);
		return buildIt(copyList);
	}

	private static GroupingComparator<?> buildIt(List<Comparator<?>> comparators) {
		if (comparators == null || comparators.isEmpty()) {
			return null;
		}
		return new GroupingComparator(comparators.remove(0), buildIt(comparators));
	}

	public static GroupingComparator<?> build(Comparator<?>[] comparators) {
		return build(Arrays.asList(comparators));
	}
}
