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
package com.userweave.module.methoden.iconunderstandability.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.userweave.dao.filter.FilterFunctor;
import com.userweave.module.methoden.iconunderstandability.dao.ReactionTimeStatisticsDao;
import com.userweave.module.methoden.iconunderstandability.dao.ScoreRangeListDao;
import com.userweave.module.methoden.iconunderstandability.domain.ITMImage;
import com.userweave.module.methoden.iconunderstandability.domain.IconTermMatchingConfigurationEntity;
import com.userweave.module.methoden.iconunderstandability.domain.ItmTerm;
import com.userweave.module.methoden.iconunderstandability.domain.ScoreRangeList;
import com.userweave.module.methoden.iconunderstandability.domain.report.IconReport;
import com.userweave.module.methoden.iconunderstandability.domain.report.TermReport;

@Service
public class ReportServiceImpl implements ReportService {
	
	@Resource
	private ScoreRangeListDao scoreRangeListDao;
	
	@Resource
	private ReactionTimeStatisticsDao reactionTimeStatisticsDao;
	
	private ScoreRangeList assignmentScoreRangeList;

	private ScoreRangeList highestAssignmentToOtherTermScoreRangeList;

	private ScoreRangeList reactionTimeScoreRangeList;

	private ScoreRangeList missingValueScoreRangeList;

	private ScoreRangeList deviationFromMeanReactionTimeScoreRangeList;

	@Override
	public TermReport getTermReport(IconTermMatchingConfigurationEntity configuration, FilterFunctor filterFunctor, int termId) {
		initStatistics(configuration, filterFunctor);
		
		if(!statistics.isValid()) {
			return null;
		}
			
		init();
		
		for (ItmTerm term : configuration.getTerms()) {
			if (term.getId().intValue() == termId) {
				return createTermReport(configuration, term);
			}
		}
		
		return null;
	} 

	private TermReport createTermReport(IconTermMatchingConfigurationEntity configuration, ItmTerm term) {
		TermReport termReport = 
			new TermReport(
				term.getValue(), 
				statistics.getDifferenceFromMeanReactionTime(term), 
				statistics.getMissingValueRate(term),
				deviationFromMeanReactionTimeScoreRangeList,
				missingValueScoreRangeList
			);
		
		
		for (ITMImage icon : configuration.getImages()) {			
			double differenceFromMeanReactionTime = statistics.getDifferenceFromMeanReactionTime(term, icon);
			if (Double.toString(differenceFromMeanReactionTime).equals("NaN")) {
				differenceFromMeanReactionTime = 0;
			};
					
			termReport.addToIconReports(
				new IconReport(
					icon,
					statistics.getAssignmentRate(term, icon),
					statistics.getHighestAssignmentForOtherTerm(term, icon),
					differenceFromMeanReactionTime,
					statistics.getRoundedTermAssignments(icon),
					assignmentScoreRangeList,
					highestAssignmentToOtherTermScoreRangeList,
					reactionTimeScoreRangeList
				)						
			);
		}
		return termReport;
	}

	private void init() {
		assignmentScoreRangeList = scoreRangeListDao.getScoreRangeListForAssignment();
		highestAssignmentToOtherTermScoreRangeList = scoreRangeListDao.getScoreRangeListForHighestAssignmentToOtherTerm();
		reactionTimeScoreRangeList = scoreRangeListDao.getScoreRangeListForReactionTime();
		missingValueScoreRangeList = scoreRangeListDao.getScoreRangeListForMissingValue();
		deviationFromMeanReactionTimeScoreRangeList = scoreRangeListDao.getScoreRangeListForDeviationFromMeanReactionTime();
	} 
	
	private boolean initStatistics(IconTermMatchingConfigurationEntity configuration, FilterFunctor filterFunctor) {
		statistics = new IconMatchingStatistics(reactionTimeStatisticsDao, configuration, filterFunctor);		
		return statistics.isValid();
	}

	private IconMatchingStatistics statistics;


	
}
