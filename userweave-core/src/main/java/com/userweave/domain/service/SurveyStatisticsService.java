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
package com.userweave.domain.service;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.userweave.domain.Study;
import com.userweave.domain.SurveyExecution;
import com.userweave.domain.TestResultEntityBase;

public interface SurveyStatisticsService {
	public GeneralStatistics evaluateResultStatistics(Study study, List<? extends TestResultEntityBase> validResults, boolean withMean);

	public GeneralStatistics evaluateResultStatistics(Integer overallStarted, List<? extends TestResultEntityBase> validResults, boolean withMean);

	public GeneralStatistics evaluateResultStatistics(List<? extends TestResultEntityBase> validResults, boolean withMean);
	
	public GeneralStatistics evaluateSurveyExecutionStatistics(List<SurveyExecution> surveyExecutions, boolean withMean);
	
	public Set<Entry<String, Integer>> evaluateSurveyExecutionsInLocales(List<SurveyExecution> surveyExecutions);
	
	public final static Long UNBELIEVABLE_LONG = 1000*60*60*12L; // longer than 12 hours

}
