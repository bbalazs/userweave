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
package com.userweave.csv.table;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import com.userweave.module.methoden.rrt.domain.OrderedTerm;
import com.userweave.module.methoden.rrt.domain.RrtConfigurationEntity;
import com.userweave.module.methoden.rrt.domain.RrtResult;
import com.userweave.module.methoden.rrt.domain.RrtTerm;

public class RrtCsvColumnGroup implements ICsvColumnGroup
{
	private final String title;
	
	private final RrtConfigurationEntity entity;
	
	private final CsvSurveyExecutionColumnGroup idGroup;
	
	private final List<Object[]> results;
	
	private final Locale locale;
	
	public RrtCsvColumnGroup(
		RrtConfigurationEntity entity, 
		List<Object[]> results,
		CsvSurveyExecutionColumnGroup idGroup,
		Locale locale)
	{
		this.title = entity.getName();
		this.entity = entity;
		this.idGroup = idGroup;
		this.results = results;
		this.locale = locale;
	}
	
	@Override
	public String getHeadlinesAsCsv(String prefix)
	{
		StringBuilder csvOutput = new StringBuilder();
		
		for(RrtTerm term : entity.getTerms())
		{
			csvOutput.append("\"" + title + " | " + 
							 term.getValue().getValue(locale) + 
							 "\";");
		}

		return csvOutput.toString();
	}

	@Override
	public String getRow(int index)
	{
		Integer seId = idGroup.getSurveyIdByIndex(index);
		
		StringBuilder csvOutput = new StringBuilder();
		
		for(Object[] result : results)
		{
			if(seId.equals(result[1]))
			{
				createOutputRow(csvOutput, (RrtResult) result[0]);
				
				break;
			}
		}
		
		return csvOutput.toString();
	}

	private void createOutputRow(StringBuilder csvOutput, RrtResult rrtResult)
	{
		// fill with empty cells
		if(rrtResult.getExecutionFinished() == null)
		{
			for(RrtTerm term : entity.getTerms())
			{
				csvOutput.append(";");
			}
		}
		// fill with ordered terms positions
		else
		{
			List<OrderedTerm> orderedTerms = rrtResult.getOrderedTerms();
			
			if(orderedTerms == null || orderedTerms.isEmpty())
			{
				// module has been executed but no ordering has been given
				for(RrtTerm term : entity.getTerms())
				{
					csvOutput.append("0;");
				}
			}
			else
			{
				Collections.sort(orderedTerms, new Comparator<OrderedTerm>()
				{
					@Override
					public int compare(OrderedTerm o1, OrderedTerm o2)
					{
						return o1.getTerm().comparePositions(o2.getTerm());
					}
				});

				for (OrderedTerm term : orderedTerms)
				{
					csvOutput.append(term.getPosition() + 1 + ";");
				}
			}
		}
	}
	
	@Override
	public int getNumberOfCols()
	{
		return entity.getTerms().size();
	}

	@Override
	public void addColumn(ICsvColumn column)
	{
		// do nothing
	}

	@Override
	public void addColumnGroup(ICsvColumnGroup group)
	{
		// do nothing
	}

}
