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
package com.userweave.pages.components.address;

import java.text.Collator;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.StringResourceModel;

import com.userweave.domain.Country;

@SuppressWarnings("serial")
public class LocalizedCountryModel extends LoadableDetachableModel {
	Component parent;
	
	public LocalizedCountryModel(Component parent) {
		this.parent = parent;
	}

	@Override
	protected Object load() {
		List<Country> countries = Arrays.asList(Country.values());
		
		Collections.sort(countries, new LocalizedCountryComparator(parent));
		
		return countries;
	}
	
	private static class LocalizedCountryComparator implements Comparator<Country> {
		Component parent;

		public LocalizedCountryComparator(Component parent) {
			this.parent = parent;
		}
		
		@Override
		public int compare(Country c1, Country c2) {
			String o1 = new StringResourceModel(c1.name(), parent, null).getString();
			String o2 = new StringResourceModel(c2.name(), parent, null).getString();
	
			return Collator.getInstance(Session.get().getLocale()).compare(o1, o2);
		}
		
	}

}
