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
package com.userweave.domain.util;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Locale;

@SuppressWarnings({ "serial", "unchecked" })
public class LocalizedAnswerComparator implements Comparator<LocalizedAnswer>, Serializable {
	
	private final Locale locale;
	public LocalizedAnswerComparator(Locale locale) {
		this.locale = locale;
	}
	
	public int compare(LocalizedAnswer o1, LocalizedAnswer o2) {

		// -1: o1 less than o2
		//  0: o1 equal to o2
		//  1: o1 greater than o2
		
		String t1 = o1.getLocalized().getValue(this.locale);
		String t2 = o2.getLocalized().getValue(this.locale);
		int i = t1.compareTo(t2);
		
		if (o1.getLocalized() == null || o1.getLocalized().getValue(this.locale) == null) {
			return (o2.getLocalized() == null || o2.getLocalized().getValue(this.locale) == null) ? -1 : 0;
		} 
		else {
			return (o2.getLocalized() == null || o2.getLocalized().getValue(this.locale) == null) ? 1 : o1.getLocalized().getValue(this.locale).compareTo( o2.getLocalized().getValue(this.locale) );
		}
	}
}
