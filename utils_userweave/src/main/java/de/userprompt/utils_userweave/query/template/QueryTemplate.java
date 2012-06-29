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
package de.userprompt.utils_userweave.query.template;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import de.userprompt.utils_userweave.query.model.IAndConditions;
import de.userprompt.utils_userweave.query.model.ICondition;
import de.userprompt.utils_userweave.query.model.IInCondition;
import de.userprompt.utils_userweave.query.model.IPropertyCondition;
import de.userprompt.utils_userweave.query.model.Join;
import de.userprompt.utils_userweave.query.model.QueryObject;

public class QueryTemplate implements IQueryTemplate {

	private StringTemplate queryTemplate;
	
	private QueryObject query;
	
	public QueryTemplate(QueryObject query) {
		super();
		this.query = query;		
	}

	public String getQueryString() {

		StringTemplateGroup group;
		
		InputStream resource = this.getClass().getResourceAsStream("/query/Query.st");
		if(resource == null) {
			return null;
		}
			
		group = new StringTemplateGroup(new InputStreamReader(resource));

		queryTemplate = group.getInstanceOf("root");
			
		queryTemplate.setAttribute("query", query);
						
		return queryTemplate.toString();
			
	}
	
	private Map<String, Object> namedParameter2ConditionValue = new HashMap<String, Object>();
		
	private int conditionCount = 0;
	
	// replace all parameter values in query-model by named parameters
	// to create prepared statement
	private void replaceParameterValuesByParameterName(QueryObject query) {
		conditionCount = 0;
		replaceParameterValuesByParameterName(query.getAndConditions());		
		conditionCount = 0;
	}

	private void replaceParameterValuesByParameterName(ICondition condition) {
		if (condition instanceof IPropertyCondition) {			
			replaceParameterValuesByParameterName((IPropertyCondition) condition);			
		}
		else if (condition instanceof IAndConditions) {
			for (ICondition cond : ((IAndConditions) condition).getConditions()) {
				replaceParameterValuesByParameterName(cond);
			}
		}
	}

	private void replaceParameterValuesByParameterName(IPropertyCondition propertyCondition) {
		if(propertyCondition.getValue() == null) { 
			return;
		}
	
		String parameterName = getParameterName(conditionCount);
		
		namedParameter2ConditionValue.put(parameterName, propertyCondition.getValue());
		
		String namedQueryParameter = ":" + parameterName;
		
		if (propertyCondition instanceof IInCondition) {
			namedQueryParameter = "(" + namedQueryParameter + ")"; 
		} 
		
		propertyCondition.setValue(namedQueryParameter);
		
		conditionCount++;
	}

	private String getParameterName(int conditionCount) {
		return "parameter_" + Integer.toString(conditionCount);
	}

	public Query create(Session session) {
		
		removeDuplicateJoins(query);
		
		replaceParameterValuesByParameterName(query);		
		
		String queryString = getQueryString();
		
		Query hibernateQuery = session.createQuery(queryString);
		
		setNamedParameterValues(hibernateQuery);
				
		return hibernateQuery;
	}

	public SQLQuery createSqlQuery(Session session)
	{
		removeDuplicateJoins(query);
		
		replaceParameterValuesByParameterName(query);		
		
		String queryString = getQueryString();
		
		SQLQuery sqlQuery = session.createSQLQuery(queryString);
		
		setNamedParameterValues(sqlQuery);
				
		return sqlQuery;
	}
	
	private void removeDuplicateJoins(QueryObject query) {		
		query.getLeftJoinFetches().removeAll(findDuplicateJoins(query.getLeftJoinFetches()));
		query.getLeftJoins().removeAll(findDuplicateJoins(query.getLeftJoins()));		
	}

	private List<Join> findDuplicateJoins(List<Join> joins) {
		List<Join> duplicateJoins = new ArrayList<Join>();
		
		for (Join join : joins) {
			duplicateJoins.addAll(findDuplicateJoins(joins, join));
		}
		
		return duplicateJoins;
	}

	private List<Join> findDuplicateJoins(List<Join> joins, Join join) {
		List<Join> rv = new ArrayList<Join>();
		for (Join join2 : joins) {
			if (join2.getAlias().equals(join.getAlias()) && 
				join2.getPath().equals(join.getPath()) 
				&& !join2.equals(join)) 
			{
				rv.add(join2);
			}
		}
		return rv;
	}
	
	private void setNamedParameterValues(Query hibernateQuery) {
		for (String namedParameter : namedParameter2ConditionValue.keySet()) {
			Object conditionValue = namedParameter2ConditionValue.get(namedParameter);

			if (conditionValue instanceof Collection) {				
				hibernateQuery.setParameterList(namedParameter, (Collection) conditionValue);
			} else {
				hibernateQuery.setParameter(namedParameter, conditionValue);
			}
		}
	}

}
