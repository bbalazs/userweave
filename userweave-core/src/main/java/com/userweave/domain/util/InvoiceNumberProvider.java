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

import org.joda.time.DateTime;

public class InvoiceNumberProvider {

	public static String getNextNumber(int next) {
		DateTime today = new DateTime(); 
		return String.format("UM%02d%02d%04d",
				today.getYearOfCentury(), today.getMonthOfYear(), next);
	}

	public static String getNextNumberPrefix() {
		DateTime today = new DateTime(); 
		return String.format("UM%02d%02d",
				today.getYearOfCentury(), today.getMonthOfYear());
	}

}
