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
package com.userweave.dao;

import de.userprompt.utils_userweave.query.model.Join;
import de.userprompt.utils_userweave.query.model.QueryObject;
import de.userprompt.utils_userweave.query.template.QueryTemplate;

public class SurveyExecutionDependentQuery extends QueryObject 
{	
	/**
	 * Alias of table to join over.
	 */
	private String surveyExecutionAlias;
	
	public String getSurveyExecutionAlias() 
	{
		return surveyExecutionAlias;
	}

	public void setSurveyExecutionAlias(String survey) 
	{
		this.surveyExecutionAlias = survey;
	}
	
	/**
	 * Name of the colum to join on.
	 */
	private String column;

	public String getColumn()
	{
		return column;
	}
	
	public void setColumn(String column)
	{
		this.column = column;
	}
	
	/**
	 * Default constructor.
	 * 
	 * @param surveyExecutionAlias
	 * 		Alias of table to join over.
	 * @param column
	 * 		Name of the colum to join on.
	 */
	public SurveyExecutionDependentQuery(String surveyExecutionAlias, String column) 
	{
		setSurveyExecutionAlias(surveyExecutionAlias);
		setColumn(column);
		//addAndCondition(PropertyCondition.in(surveyExecutionAlias+".state", Arrays.asList(SurveyExecutionState.STARTED, SurveyExecutionState.COMPLETED)));
		//addAndCondition(PropertyCondition.greaterOrEqual(surveyExecutionAlias+".state", SurveyExecutionState.STARTED));
	}
	
	/**
	 * Joins the given table on the survey execution id.
	 * 
	 * @param joinTableName
	 * 		Join table to join over
	 * @param joinTableAlias
	 * 		Alias of join table
	 * @param joinClumn
	 * 		Colun to join over.
	 * @return
	 * 		This query object.
	 */
	public SurveyExecutionDependentQuery connectToSurveyExecution(
			String joinTableName, String joinTableAlias, String joinClumn) 
	{
		this.addLeftJoin(new Join(
			joinTableName, 
			joinTableAlias,
			surveyExecutionAlias + "." + column,
			joinTableAlias + "." + joinClumn));
		
		//addAndCondition(ObjectCondition.equals(pathToSurveyExecution, getSurveyExecutionAlias()));
	
		return this;
	}
	
	@Override
	public String toString() {
		return new QueryTemplate(this).getQueryString();
	}
}
