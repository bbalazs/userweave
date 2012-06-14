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

import java.util.List;

public class PropertyCondition implements IPropertyCondition {
	
	public static PropertyCondition like(String property, String value) {
		return new PropertyCondition(property, "like", "%" + value + "%");
	}
	
	public static IPropertyCondition ilike(String property, String value) {
		// TODO: we are using different lower logics here, postgres AND java
		// better would be "lower(property) like lower('%value%')
		if (value != null) {
			value = value.toLowerCase();
		}
		
		return new PropertyCondition(lower(property), "like", "%" + value + "%");
	}
	
	public static IPropertyCondition notIlike(String property, String value) {
		// TODO: we are using different lower logics here, postgres AND java
		// better would be "lower(property) like lower('%value%')
		if (value != null) {
			value = value.toLowerCase();
		}
		
		return new PropertyCondition(lower(property), "not like", "%" + value + "%");
	}
	
	private static String lower(String value) {
		return "lower(" + value + ")";
	}

	public static PropertyCondition equals(String property, Object value) {
		return new PropertyCondition(property, "=", value);
	}
	
	public static PropertyCondition notEquals(String property, Object value) {
		return new PropertyCondition(property, "!=", value);
	}
	
	public static PropertyCondition greater(String property, Object value) {
		return new PropertyCondition(property, ">", value);
	}
	
	public static PropertyCondition greaterOrEqual(String property, Object value) {
		return new PropertyCondition(property, ">=", value);
	}
	
	public static PropertyCondition less(String property, Object value) {
		return new PropertyCondition(property, "<", value);
	}
	
	public static PropertyCondition lessOrEqual(String property, Object value) {
		return new PropertyCondition(property, "<=", value);
	}
	
	public static PropertyCondition in(String property, List<? extends Object> values) {
		return new InCondition(property, values);
	}

	public static PropertyCondition isNotNull(String property) {
		return new PropertyCondition(property, "is not null", null);
	}

	public static PropertyCondition isNull(String property) {
		return new PropertyCondition(property, "is null", null);
	}

	private String property;
	
	private String operator;
	
	private Object value;

	public void setValue(Object value) {
		this.value = value;
	}
	
	@Override
	public String getType() {
		return "property";
	}

	protected PropertyCondition(String property, String operator, Object value) {
		super();
		this.property = property;
		this.operator = operator;
		this.value = value;
	}
	
	public String getProperty() {
		return property;
	}
	
	public String getOperator() {
		return operator;
	}
	
	public Object getValue() {
		return value;
	}



}
