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
package com.userweave.components.bar;

import java.util.List;

import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * @author oma
 */
public class BarListPanel extends Panel 
{
	private static final long serialVersionUID = 1L;

	public BarListPanel(String id, List<Integer> barValues, final int maxBarValue) 
	{
		super(id);		
	
		init(barValues, maxBarValue, null);		
	}

	public BarListPanel(
		String id, List<Integer> barValues, final int maxBarValue, List<String> cssClassNames) 
	{	
		super(id);		
		
		init(barValues, maxBarValue, cssClassNames);		
	}
	
	private void init(
		List<Integer> barValues, final int maxBarValue, final List<String> cssClassNames) 
	{			
		add(new ListView("ratingBars", barValues)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem item)
			{
				Integer rating = (Integer) item.getModelObject();

				String cssClassName = null;
				int index = item.getIndex();
				if ((cssClassNames != null) && (cssClassNames.size() > index))
				{
					cssClassName = cssClassNames.get(index);
				}

				item.add(new BarPanel("bar", maxBarValue, rating.intValue(),
						cssClassName));
			}
		});	
	}
}

