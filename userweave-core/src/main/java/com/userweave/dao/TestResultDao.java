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

import java.util.List;

import com.userweave.dao.filter.FilterFunctor;
import com.userweave.domain.ModuleConfigurationEntityBase;
import com.userweave.domain.ModuleConfigurationWithResultsEntity;
import com.userweave.domain.SurveyExecution;
import com.userweave.domain.TestResultEntityBase;
import com.userweave.domain.service.GeneralStatistics;

public interface TestResultDao<T extends TestResultEntityBase<U>, U extends ModuleConfigurationEntityBase> extends BaseDao<T> {
	
	public T findByConfigurationAndSurveyExecution(ModuleConfigurationWithResultsEntity configuration, SurveyExecution surveyExecution);
	
	public SurveyExecutionDependentQuery createQuery(
			ModuleConfigurationWithResultsEntity configuration, FilterFunctor filterFunctor);
	
	public List<T> findValidResults(ModuleConfigurationWithResultsEntity configuration, FilterFunctor filterFunctor);

	public GeneralStatistics findValidResultStatistics(ModuleConfigurationWithResultsEntity configuration, FilterFunctor filterFunctor);

	int getValidResultCount(U configuration, FilterFunctor filterFunctor);
}
