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
package de.userprompt.utils_userweave.query.model;

import java.util.ArrayList;
import java.util.List;


public class QueryObject{
	
	private int parameterCount = 0;
	
	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private List<QueryEntity> queryEntities = new ArrayList<QueryEntity>();

	public List<QueryEntity> getQueryEntities() {
		return queryEntities;
	}

	public void setQueryEntities(List<QueryEntity> entities) {
		this.queryEntities = entities;
	}
	
	public QueryObject addQueryEntity(String name, String alias) {
		return addQueryEntity(new QueryEntity(name, alias));
		
	}
	public QueryObject addQueryEntity(QueryEntity entity) {
		queryEntities.add(entity);
		return this;
	}
	
	private String result;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	public QueryObject setResult(QueryEntity result) {
		this.result = result.getInstance();
		return this;
	}
	
	public QueryObject setDistinctResult(QueryEntity result) {
		this.result = " distinct " + result.getInstance();
		return this;
	}
	
	private List<Join> leftJoins = new ArrayList<Join>();


	public List<Join> getLeftJoins() {
		return leftJoins;
	}

	public void setLeftJoins(List<Join> leftJoins) {
		this.leftJoins = leftJoins;
	}

	public QueryObject addLeftJoin(String path, String alias) {
		return addLeftJoin(new Join(path, alias));
	}
	
	public QueryObject addLeftJoin(Join join) {
		if(!leftJoins.contains(join))
		{
			leftJoins.add(join);
		}
		
		return this;
	}
	
	private List<Join> leftJoinFetches = new ArrayList<Join>();


	public List<Join> getLeftJoinFetches() {
		return leftJoinFetches;
	}

	public void setLeftJoinFetches(List<Join> leftJoinFetches) {
		this.leftJoinFetches = leftJoinFetches;
	} 
	
	public QueryObject addLeftJoinFetch(String path, String alias) {
		return addLeftJoinFetch(new Join(path, alias));
	}
	
	public QueryObject addLeftJoinFetch(Join join) {
		leftJoinFetches.add(join);
		return this;
	}
	
	private IAndConditions andConditions = new AndConditions();

	public IAndConditions getAndConditions() {
		return andConditions;
	}

	public void setAndConditions(IAndConditions condition) {
		this.andConditions = condition;
	}
	
	public QueryObject addAndCondition(ICondition condition) {
		if(andConditions == null) {
			andConditions = new AndConditions();
		}
		this.andConditions.addCondition(condition);
		return this;
	}

	public boolean getHasConditions() {
		return andConditions != null;
	}

	private String orderBy;

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public boolean getHasOrderBy() {
		return orderBy != null;
	}

	private String groupBy;

	public String getGroupBy() {
		return groupBy;
	}

	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}

	public boolean getHasGroupBy() 
	{
		return groupBy != null && !groupBy.equals("");
	}
	
	
	private String having;

	public String getHaving() {
		return having;
	}

	public void setHaving(String having) {
		this.having = having;
	}

	public boolean getHasHaving() 
	{
		return having != null && !having.equals("");
	}

}
