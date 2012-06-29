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

// term -> classificator
// icon - Object
@SuppressWarnings("serial")
public class ObjectCategorization implements Serializable {
	
	private final ItemCount itemCount = new ItemCount();
	
	public Collection<String> getCategories() {
		return itemCount.getTerms();
	}
	
	public Collection<String> getObjectsForCategory(String category) {
		return itemCount.getIconsForTerm(category);
	}
	
	//getIconsForTermSum
	public int getObjectCountForCategory(String category) {
		return itemCount.getIconsForTermSum(category);
	}
	
	public int getCountForCategoryAndObject(String category, String object) {
		return itemCount.getCountForTermAndIcon(category, object);
	}

	public void addObjectForCategory(String category, String object) {
		itemCount.addTermForIcon( category,  object);
	}
}
