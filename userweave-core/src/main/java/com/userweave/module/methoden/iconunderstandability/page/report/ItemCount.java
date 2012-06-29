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
package com.userweave.module.methoden.iconunderstandability.page.report;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class ItemCount implements Serializable {

	private final Map<String, Map<String, Integer>> termToIconCountMap = new HashMap<String, Map<String, Integer>>();
	
	public void addTermForIcon(String term, String icon) {
		Map<String, Integer> iconCountMap = getIconCountMap(term);
		iconCountMap.put(icon, getIconCount(icon, iconCountMap) + 1);
	}
	
	private Map<String, Integer> getIconCountMap(String term) {
		Map<String, Integer> iconCountMap = termToIconCountMap.get(term);
		if (iconCountMap == null) {
			iconCountMap = new HashMap<String, Integer>();
			termToIconCountMap.put(term, iconCountMap);
		}
		return iconCountMap;
	}
	
	private Integer getIconCount(String icon, Map<String, Integer> iconCountMap) {
		Integer count = iconCountMap.get(icon);
		if (count == null) {
			count = 0;
		}
		return count;
	}

	public Collection<String> getTerms() {
		return termToIconCountMap.keySet();
	}
	
	public Collection<String> getIconsForTerm(String term) {
		return getIconCountMap(term).keySet();
	}
	
	public int getIconsForTermSum(String term) {
		int rv = 0;
		Map<String, Integer> iconCountMap = getIconCountMap(term);
		for(Integer count: iconCountMap.values()) {
			rv  = rv + count.intValue();
		}
		return rv;
	}
	
	public int getCountForTermAndIcon(String term, String icon) {
		return getIconCount(icon, getIconCountMap(term));
	}
}
