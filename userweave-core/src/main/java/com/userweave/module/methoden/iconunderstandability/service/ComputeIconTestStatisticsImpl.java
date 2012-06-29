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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math.stat.regression.SimpleRegression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.userweave.module.methoden.iconunderstandability.dao.ITMTestResultDao;
import com.userweave.module.methoden.iconunderstandability.dao.ReactionTimeStatisticsDao;
import com.userweave.module.methoden.iconunderstandability.domain.report.IconTestReactionTimeStatistics;

@Service(value="computeIconTestStatistics")
public class ComputeIconTestStatisticsImpl implements ComputeIconTestStatistics {
	
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ITMTestResultDao testResultDao;
	
	@Autowired
	private ReactionTimeStatisticsDao reactionTimeStatisticsDao;
	
	public void computeStatisticsAndSave() {
		LOGGER.info("computeStatisticsAndSave : cron job started");
		
		final OverallStatistics overallStatistics = computeOverallStatistics();
		
		if (overallStatistics != null) {
		
			for (int iconCount = 2; iconCount < 40; iconCount++) {
							
				IconTestReactionTimeStatistics statisticsEntity = getStatisticsPersistentEntityForIconCount(iconCount);
				
				setMeanReactionTime(overallStatistics, iconCount, statisticsEntity);
				
				setRegression(overallStatistics, iconCount, statisticsEntity);
				
				reactionTimeStatisticsDao.save(statisticsEntity);
			}
		}
		LOGGER.info("computeStatisticsAndSave : cron job finished");
	}

	private void setRegression(final OverallStatistics overallStatistics, int iconCount, IconTestReactionTimeStatistics statisticsEntity) {
		
		// set regress if regression was computed
		if (overallStatistics.hasValidRegression()) {
			statisticsEntity.setRegressionValue(overallStatistics.getRegression().predict(iconCount)); 
		} else { // set mean otherwise
			statisticsEntity.setRegressionValue(overallStatistics.getMean());
		}
	}

	private void setMeanReactionTime(final OverallStatistics overallStatistics, int iconCount, IconTestReactionTimeStatistics statisticsEntity) {
		final DescriptiveStatistics statisticsForIconCount = overallStatistics.getIconCount2Statistics().get(new Integer(iconCount));				
		if (statisticsForIconCount != null) {
			statisticsEntity.setMeanReactionTime(statisticsForIconCount.getMean());
		} else {
			statisticsEntity.setMeanReactionTime(-1);
		}
	}

	private IconTestReactionTimeStatistics getStatisticsPersistentEntityForIconCount(int iconCount) {
		IconTestReactionTimeStatistics reactionTimeStatistics = reactionTimeStatisticsDao.findByIconCount(iconCount);
		
		if (reactionTimeStatistics == null) {
			reactionTimeStatistics = new IconTestReactionTimeStatistics();
			reactionTimeStatistics.setIconCount(iconCount);
		}
		return reactionTimeStatistics;
	}

	// contains 
	// 1. regression and mean for all icons
	// 2. map of descriptive statistics for each icon count
	private class OverallStatistics {
		
		public OverallStatistics(SimpleRegression regression, Double mean, Map<Integer, DescriptiveStatistics> iconCount2Statistics) {
			super();
			this.regression = regression;
			this.mean = mean;
			this.iconCount2Statistics = iconCount2Statistics;
		}

		public Map<Integer, DescriptiveStatistics> getIconCount2Statistics() {
			return iconCount2Statistics;
		}

		private final SimpleRegression regression;

		public SimpleRegression getRegression() {
			return regression;
		}

		private final Double mean;

		public Double getMean() {
			return mean;
		}
		
		public boolean hasValidRegression() {
			return regression != null;
		}
		
		private final Map<Integer, DescriptiveStatistics> iconCount2Statistics;
		
	};	

	/**
	 * return regression, if regression can be computed
	 * @return
	 */
	private OverallStatistics computeOverallStatistics() {
		
		SimpleRegression regression = new SimpleRegression();
		
		DescriptiveStatistics overallStatistics = DescriptiveStatistics.newInstance();
		
		Map<Integer, DescriptiveStatistics> iconCount2Statistics = new HashMap<Integer, DescriptiveStatistics>();
		
		List<Object[]> executionTimesIconCount = testResultDao.findAllValidExecutionTimesAndIconCount();
		
		if (!executionTimesIconCount.isEmpty()) {

			// check, if there is variation in x (only one x value for all observation yield NaN!)
			boolean canComputeRegression = false;

			int iconCountForFirstResult = ((Long) executionTimesIconCount.get(0)[1]).intValue();
					 
			for (Object[] executionTimeIconCount : executionTimesIconCount) {
			
				int iconCount = ((Long) executionTimeIconCount[1]).intValue();
				if (iconCount != iconCountForFirstResult) {
					canComputeRegression = true;
				}
	
				double executionTime = (Long) executionTimeIconCount[0];
			
				if (isValid(executionTime)) {
					regression.addData(iconCount, executionTime);
					overallStatistics.addValue(executionTime);					
					getStatisticsForIconCount(iconCount2Statistics, iconCount).addValue(executionTime);
				}		
			}

			if (canComputeRegression) {
				return new OverallStatistics(regression, overallStatistics.getMean(), iconCount2Statistics);
			} else { 
				return new OverallStatistics(null, overallStatistics.getMean(), iconCount2Statistics);
			}
		} else {
			return null;
		}
	}

	private DescriptiveStatistics getStatisticsForIconCount(Map<Integer, DescriptiveStatistics> iconCount2Statistics, int iconCount) {
		final Integer count = new Integer(iconCount);
		DescriptiveStatistics statsForIconCount = iconCount2Statistics.get(count);
		if (statsForIconCount == null) {
			statsForIconCount = DescriptiveStatistics.newInstance();
			iconCount2Statistics.put(count, statsForIconCount);
		}
		return statsForIconCount;
	}

	private boolean isValid(double value) {
		
		// only accept values betwenn minSeconds and maxSeconds
		double minSeconds = 1.5;			
		
		double maxSeconds = 60;
	
		return ((value > minSeconds * 1000) && (value < maxSeconds * 1000));	
	}
	
}
