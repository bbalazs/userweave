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
package com.userweave.dao.filter;

import java.util.ArrayList;
import java.util.List;

import com.userweave.dao.SurveyExecutionDependentQuery;

import de.userprompt.utils_userweave.query.model.QueryObject;

/**
 * Wrapper class to apply a list of defined filters
 * on a query object.
 * 
 * @author opr
 */
public class CompoundFilterFunctor implements FilterFunctor 
{
	private static final long serialVersionUID = 1L;

	/**
	 * The list of filters to apply on the query.
	 */
	private  List<FilterFunctor> filterFunctors = new ArrayList<FilterFunctor>();
	
	/**
	 * Constructor for predefined list of filters.
	 * 
	 * @param filterFunctors
	 * 		List of filter functors.
	 */
	public CompoundFilterFunctor(List<FilterFunctor> filterFunctors) 
	{
		super();
		this.filterFunctors = filterFunctors;
	}
	
	/**
	 * Default construcor.
	 */
	public CompoundFilterFunctor() 
	{
		super();
	}
	
	/**
	 * Adds a filter functor to the given filter functor list.
	 * 
	 * @param filterFunctor
	 * 		Filter functor to add.
	 */
	public void addFilterFunctor(FilterFunctor filterFunctor) 
	{
		filterFunctors.add(filterFunctor);
	}

	@Override
	public QueryObject apply(SurveyExecutionDependentQuery queryObject) 
	{
		for (FilterFunctor filterFunctor : filterFunctors) 
		{
			filterFunctor.apply(queryObject);
		}
		
		return queryObject;
	}
}
