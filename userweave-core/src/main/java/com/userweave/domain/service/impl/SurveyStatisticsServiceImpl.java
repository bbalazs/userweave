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
package com.userweave.domain.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.math.stat.descriptive.SummaryStatistics;
import org.springframework.stereotype.Service;

import com.userweave.domain.Study;
import com.userweave.domain.SurveyExecution;
import com.userweave.domain.SurveyExecutionState;
import com.userweave.domain.TestResultEntityBase;
import com.userweave.domain.service.GeneralStatistics;
import com.userweave.domain.service.SurveyStatisticsService;

@Service
public class SurveyStatisticsServiceImpl implements SurveyStatisticsService {
	
	@Override
	public GeneralStatistics evaluateResultStatistics(Study study, List<? extends TestResultEntityBase> validResults, boolean withMean) {
		Integer overall = 0;
		if(study != null) {
			GeneralStatistics studyStatistics = evaluateSurveyExecutionStatistics(study.getSurveyExecutions(), withMean);
			overall = studyStatistics.getStarted();
		}
		return evaluateResultStatistics(overall, validResults, withMean);
	}

	@Override
	public GeneralStatistics evaluateSurveyExecutionStatistics(List<SurveyExecution> surveyExecutions, boolean withMean) {
		Integer started = 0;
		Integer finished = 0;
		SummaryStatistics stats = withMean ? SummaryStatistics.newInstance() : null;

		for(SurveyExecution se:	surveyExecutions) {
			if(se.getState() == SurveyExecutionState.STARTED) {
				started++;
			} 
			if(se.getState() == SurveyExecutionState.COMPLETED) {
				started++;
				finished++;
				if(stats != null) {
					if(se.getExecutionFinished() != null && se.getExecutionStarted() != null) {
						long executionTime = se.getExecutionFinished().getTime() - se.getExecutionStarted().getTime();
						if(executionTime < UNBELIEVABLE_LONG) {
							stats.addValue(executionTime/1000);
						}
					}
				}
			}
		}
		return new GeneralStatisticsImpl(stats, started, started, finished);
	}

	@Override
	public GeneralStatistics evaluateResultStatistics(Integer overallStarted, List<? extends TestResultEntityBase> validResults, boolean withMean) {
		if(validResults == null) {
			return null;
		}
		
		Integer started = 0;
		Integer finished = 0;
		SummaryStatistics stats = withMean ? SummaryStatistics.newInstance() : null;
		
		for(TestResultEntityBase<?> result : validResults) {
			if(result.getExecutionStarted() != null) {
				started++;
				if(result.getExecutionFinished() != null) {
					finished++;
					if(stats != null) {
						long executionTime = result.getExecutionFinished().getTime() - result.getExecutionStarted().getTime();
						if(executionTime < UNBELIEVABLE_LONG) {
							stats.addValue(executionTime/1000);
						}
					}
				}
			}
		}

		return new GeneralStatisticsImpl(stats, overallStarted != null ? overallStarted : started, started, finished);
	}

	@Override
	public GeneralStatistics evaluateResultStatistics(List<? extends TestResultEntityBase> validResults, boolean withMean) {
		return evaluateResultStatistics((Integer) null, validResults, withMean);
	}

	@Override
	public Set<Entry<String, Integer>> evaluateSurveyExecutionsInLocales(
			List<SurveyExecution> surveyExecutions)
	{
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		
		for(SurveyExecution exec : surveyExecutions)
		{
			if(exec.getLocale() != null &&
			   exec.getState() != SurveyExecutionState.INVALID && 
			   exec.getState() != SurveyExecutionState.NOT_STARTED)
			{
				String lang = exec.getLocale().getDisplayLanguage();
				
				if(map.containsKey(lang))
				{
					map.put(lang, new Integer(map.get(lang) + 1));
				}
				else
				{
					map.put(lang, new Integer(1));
				}
			}
		}
		
		return map.entrySet();
	}
}
