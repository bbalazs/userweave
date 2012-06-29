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
package de.userprompt;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.userprompt.utils_userweave.query.model.PropertyCondition;
import de.userprompt.utils_userweave.query.model.QueryEntity;
import de.userprompt.utils_userweave.query.model.QueryObject;
import de.userprompt.utils_userweave.query.template.QueryTemplate;
import junit.framework.TestCase;

public class InConditionTest extends TestCase 
{
	public void testInConditionWithFilledList()
	{
		LinkedList<String> testList = new LinkedList<String>();
		
		testList.add("(\"int\",");
		testList.add("\"float\",");
		testList.add("\"string\")");
		
		String queryStr = getQueryString(testList);
		
		String resultStr = ("SELECT true FROM SOME ENTITY true " +
				"WHERE ((\"string\" "+
				"in (\"int\",\"float\",\"string\")))").replaceAll(" ","\n");
		
		assertEquals(queryStr, resultStr);
	}
	
	public void testInConditionWithEmptyList()
	{
		LinkedList<String> testList = new LinkedList<String>();
		
		String queryStr	= getQueryString(testList);
		
		String resultStr = ("SELECT true FROM SOME ENTITY true " +
				"WHERE ((\"string\" "+
				"in ))").replaceAll(" ","\n");
		
		assertEquals(queryStr, resultStr);
	}
	
	public void testInConditionWithNullList()
	{
		String queryStr	= getQueryString(null);
			
		String resultStr = ("SELECT true FROM SOME ENTITY true " +
				"WHERE ((\"string\" "+
				"in ))").replaceAll(" ","\n");
		
		assertEquals(queryStr, resultStr);
	}
	
	private String getQueryString(List testList)
	{
		QueryObject query = new QueryObject();
		query.setName("IN CONDITION QUERY");
		
		QueryEntity entity = new QueryEntity("SOME ENTITY", "true");
		query.addQueryEntity(entity);
		
		query.setResult(entity)
			 .addAndCondition(PropertyCondition.in(
					 "\"string\"", testList));
		
		QueryTemplate queryTemplate = new QueryTemplate(query);
		return queryTemplate.getQueryString().replaceAll("\n"," ").trim().replaceAll("\\s+", "\n");
	}
}
