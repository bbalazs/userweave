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

public class Join {
	public Join(String path, String alias) {
		super();
		this.path = path;
		this.alias = alias;
	}

	public Join(String path, String alias, String onFirstParam, String onSecondParam) {
		super();
		this.path = path;
		this.alias = alias;
		this.onConditionFirstParameter = onFirstParam;
		this.onConditionSecondParameter = onSecondParam;
	}
	
	private String path;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	private String alias;

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	
	private String onConditionFirstParameter;
	
	public String getOnConditionFirstParameter()
	{
		return onConditionFirstParameter;
	}

	public void setOnConditionFirstParameter(String onConditionFirstParameter)
	{
		this.onConditionFirstParameter = onConditionFirstParameter;
	}

	
	private String onConditionSecondParameter;
	
	public String getOnConditionSecondParameter()
	{
		return onConditionSecondParameter;
	}

	public void setOnConditionSecondParameter(String onConditionSecondParameter)
	{
		this.onConditionSecondParameter = onConditionSecondParameter;
	}

	
	public boolean getHasOn() {
		return onConditionFirstParameter != null && 
			   !onConditionFirstParameter.equals("");
	}
	
	@Override
	public int hashCode()
	{
		int hash = alias.hashCode() + path.hashCode();
		
		if(onConditionFirstParameter != null && 
		   !onConditionFirstParameter.equals(""))
		{
			hash += onConditionFirstParameter.hashCode();
			hash += onConditionSecondParameter.hashCode();
		}
		
		return hash;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		Join join = (Join) obj;
		
		boolean equals = 
		   join.getAlias().equals(alias) &&
		   join.getPath().equals(path);
		
		if(onConditionFirstParameter != null && 
		   !onConditionFirstParameter.equals("") &&
		   join.getOnConditionFirstParameter() != null &&
		   !join.getOnConditionFirstParameter().equals(""))
		{
			equals = 
				equals && 
				join.getOnConditionFirstParameter().equals(onConditionFirstParameter) &&
				join.getOnConditionSecondParameter().equals(onConditionSecondParameter);
		}
		
		return equals;
	}
	
}
