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

public class ObjectCondition implements IObjectCondition {

	private String objectPath1;
	
	private String objectPath2;
	
	private String operator;

	public static ObjectCondition equals(String objectPath1, String objectPath2) {
		return new ObjectCondition(objectPath1, objectPath2, "=");
	}
	
	public static ObjectCondition in(String objectPath1, String objectPath2) {
		return new ObjectCondition(objectPath1, "elements(" + objectPath2 + ")", "in");
	}
	
	public static ObjectCondition isNull(String property) {
		return new ObjectCondition(property, "null", "is");
	}
	
	protected ObjectCondition(String objectPath1, String objectPath2, String operator) {
		super();
		this.objectPath1 = objectPath1;
		this.objectPath2 = objectPath2;
		this.operator = operator;
	}

	@Override
	public String getObjectPath1() {
		return objectPath1;
	}

	@Override
	public String getObjectPath2() {
		return objectPath2;
	}

	@Override
	public String getOperator() {
		return operator;
	}

	@Override
	public String getType() {
		return "object";
	}

}
