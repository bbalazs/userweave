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
package com.userweave.pages.report;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.userweave.domain.service.GeneralStatistics;

public abstract class GeneralStatisticsPanel extends Panel 
{
	private static final long serialVersionUID = 1L;
	
	private final static Logger logger = LoggerFactory.getLogger(GeneralStatisticsPanel.class);

	public GeneralStatisticsPanel(String id) {
		super(id);
		
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() 
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			protected Object load() {
				long startTime = System.currentTimeMillis();
				GeneralStatistics generalStatistics = getGeneralStatistics();
				long overallTime = System.currentTimeMillis()-startTime;
				logger.info("OVERALLTIME: "+overallTime+ " milliseconds");
				return generalStatistics;
			}	
		}));
		
		add(new Label("started"));
		add(new Label("finished"));
		add(new Label("dropout"));
		add(new Label("averageToMinute"));
		add(new Label("deviationToMinute"));
		
	}

	protected abstract GeneralStatistics getGeneralStatistics();

}
