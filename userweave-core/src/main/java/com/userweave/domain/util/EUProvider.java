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

import java.util.ArrayList;
import java.util.List;

import com.userweave.domain.Country;

public class EUProvider {

	public static List<Country> euMembers = new ArrayList<Country>()
    {
        {
    		add(Country.AUSTRIA);
    		add(Country.BELGIUM);
    		add(Country.BULGARIA);
    		add(Country.CYPRUS);
    		add(Country.CZECH_REPUBLIC);
    		
    		add(Country.DENMARK);
    		add(Country.ESTONIA);
    		add(Country.FINLAND);
    		add(Country.FRANCE);
    		add(Country.GERMANY);
    		
    		add(Country.GREECE);
    		add(Country.HUNGARY);
    		add(Country.IRELAND);
    		add(Country.ITALY);
    		add(Country.LATVIA);
    		
    		add(Country.LITHUANIA);
    		add(Country.LUXEMBOURG);
    		add(Country.MALTA);
    		add(Country.NETHERLANDS);
    		add(Country.POLAND);
    		
    		add(Country.PORTUGAL);
    		add(Country.ROMANIA);
    		add(Country.SLOVAKIA);
    		add(Country.SLOVENIA);
    		add(Country.SPAIN);
    		
    		add(Country.SWEDEN);
    		add(Country.UNITED_KINGDOM);
        }
    };
	
    
    public static boolean isInEU(Country country) {
    	return euMembers.contains(country);
    }
}
