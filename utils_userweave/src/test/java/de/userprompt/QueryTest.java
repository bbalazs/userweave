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

import junit.framework.TestCase;
import de.userprompt.utils_userweave.query.model.AndConditions;
import de.userprompt.utils_userweave.query.model.Join;
import de.userprompt.utils_userweave.query.model.ObjectCondition;
import de.userprompt.utils_userweave.query.model.OrConditions;
import de.userprompt.utils_userweave.query.model.PropertyCondition;
import de.userprompt.utils_userweave.query.model.QueryEntity;
import de.userprompt.utils_userweave.query.model.QueryObject;
import de.userprompt.utils_userweave.query.template.QueryTemplate;

public class QueryTest extends TestCase {
	public void testQuery() {
		
		QueryObject query = new QueryObject();
		query.setName("MY QUERY");
		
		QueryEntity entity = new QueryEntity("SOME ENTITY", "result");
		query.addQueryEntity(entity);
		
		query
			.setResult(entity)
			.addQueryEntity(new QueryEntity("SOME OTHER ENTITY", "e2"))
			.addLeftJoin(new Join("e1.test", "alias"))
			.addLeftJoin(new Join("e1.somejoin", "alias2"))
			.addLeftJoinFetch(new Join("e1.somejoin", "alias2"))		
			.setAndConditions(				
				new AndConditions()
					.addCondition(ObjectCondition.equals("entityA", "entityB"))
					.addCondition(PropertyCondition.like("e2.name", "blah"))
					.addCondition(PropertyCondition.greater("e2.size", "2"))
					.addCondition(
						new OrConditions()
							.addCondition(PropertyCondition.greater("Y", 1))
							.addCondition(PropertyCondition.less("Z", 2))
					)
			);
		
		
		QueryTemplate queryTemplate = new QueryTemplate(query);
		String queryStr = queryTemplate.getQueryString().replaceAll("\n"," ").trim().replaceAll("\\s+", "\n");
		assertEquals(queryStr, ("SELECT result FROM SOME ENTITY result, SOME OTHER ENTITY "+
				"e2 left join e1.test alias left join e1.somejoin alias2 left join fetch "+
				"e1.somejoin alias2 WHERE ((entityA = entityB) and (e2.name like %blah%) "+
				"and (e2.size > 2) and ((Y > 1) or (Z < 2)))").replaceAll(" ","\n"));
	}
	
	public void testQuery2() {
		
		QueryObject query = new QueryObject();
		query.setName("MY QUERY");
		
		QueryEntity entity = new QueryEntity("SOME ENTITY", "result");
		query.addQueryEntity(entity);
		
		query
			.setResult(entity)
			.addQueryEntity(new QueryEntity("SOME OTHER ENTITY", "e2"))
			.addLeftJoin(new Join("e1.test", "alias"))
			.addLeftJoin(new Join("e1.somejoin", "alias2"))
			.addLeftJoinFetch(new Join("e1.somejoin", "alias2"))		
			.addAndCondition(ObjectCondition.equals("entityA", "entityB"))
			.addAndCondition(PropertyCondition.like("e2.name", "blah"))
			.addAndCondition(PropertyCondition.greater("e2.size", "2"))
			.addAndCondition(
				new OrConditions()
					.addCondition(PropertyCondition.greater("Y", 1))
					.addCondition(PropertyCondition.less("Z", 2))
			)			
			.setGroupBy("a, b, c")
			;
		
		
		QueryTemplate queryTemplate = new QueryTemplate(query);
		String queryStr = queryTemplate.getQueryString().replaceAll("\n"," ").trim().replaceAll("\\s+", "\n");
		assertEquals(queryStr, ("SELECT result FROM SOME ENTITY result, SOME OTHER ENTITY "+
				"e2 left join e1.test alias left join e1.somejoin alias2 left join fetch "+
				"e1.somejoin alias2 WHERE ((entityA = entityB) and (e2.name like %blah%) "+
				"and (e2.size > 2) and ((Y > 1) or (Z < 2))) GROUP BY a, b, c").replaceAll(" ","\n"));
	}

}
