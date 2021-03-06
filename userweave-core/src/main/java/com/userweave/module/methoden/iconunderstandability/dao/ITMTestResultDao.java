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
package com.userweave.module.methoden.iconunderstandability.dao;

import java.util.List;

import com.userweave.dao.TestResultDao;
import com.userweave.dao.filter.FilterFunctor;
import com.userweave.domain.ModuleConfigurationWithResultsEntity;
import com.userweave.domain.service.GeneralStatistics;
import com.userweave.module.methoden.iconunderstandability.domain.ITMTestResult;
import com.userweave.module.methoden.iconunderstandability.domain.IconTermMatchingConfigurationEntity;

public interface ITMTestResultDao extends TestResultDao<ITMTestResult, IconTermMatchingConfigurationEntity> {
	public List<Object[]> findAllValidExecutionTimesAndIconCount();
	
	public GeneralStatistics findValidResultStatisticsForTerm(
			ModuleConfigurationWithResultsEntity configuration,
			FilterFunctor filterFunctor,
			Integer termId);
}
