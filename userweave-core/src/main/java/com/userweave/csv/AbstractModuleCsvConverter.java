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
package com.userweave.csv;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections.map.HashedMap;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;

import com.userweave.domain.ModuleConfigurationWithResultsEntity;

public abstract class AbstractModuleCsvConverter<T extends ModuleConfigurationWithResultsEntity> 
	implements ICsvConverter<T>
{
	@SpringBean
    private SessionFactory sessionFactory;

    protected Session getCurrentSession() 
    {
		return sessionFactory.getCurrentSession();
	}
	
	public AbstractModuleCsvConverter() 
	{
		Injector.get().inject(this);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected List<AbstractCsvCell> getRowForSurveyExecution(
			HashedMap map, Object result)
	{
		List<AbstractCsvCell> row;
		
		// is there already a row?
		if(map.containsKey(result))
		{
			row = (List<AbstractCsvCell>)map.get(result);
		}
		else
		{
			row = new LinkedList<AbstractCsvCell>();
			map.put(result, row);
		}
		
		return row;
	}
}
