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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math.stat.descriptive.SummaryStatistics;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.userweave.dao.filter.FilterFunctor;
import com.userweave.domain.LocalizedString;
import com.userweave.domain.util.FormatUtils;
import com.userweave.module.methoden.iconunderstandability.dao.IconTermMappingDao;
import com.userweave.module.methoden.iconunderstandability.dao.ReactionTimeStatisticsDao;
import com.userweave.module.methoden.iconunderstandability.domain.ITMImage;
import com.userweave.module.methoden.iconunderstandability.domain.IconTermMatchingConfigurationEntity;
import com.userweave.module.methoden.iconunderstandability.domain.ItmTerm;
import com.userweave.module.methoden.iconunderstandability.domain.report.IconTestReactionTimeStatistics;
import com.userweave.module.methoden.iconunderstandability.domain.report.TermAssignment;

public class IconMatchingStatistics {
	
	private int totalNumberOfSurveyExecutions = -1;
	
	private double overallPredictedReactionTime = -1;

	private final int iconCountForConfiguration;
	
	private final IconTermMatchingConfigurationEntity configuration;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private boolean isValid = false;
	
	@SpringBean
	private IconTermMappingDao iconTermMappingDao;
	
	private List<ItmTerm> getTerms() {
		return configuration.getTerms();
	}
	
	// store execution-times for icon and term
	private static class ValueArray {
		private final Map<Integer, Map<Integer, List<Double>>> termId2Icon2Time = new HashMap<Integer, Map<Integer, List<Double>>>();

		/*
		public ValueArray(List<IconTermMapping> mappings) {
			for (IconTermMapping mapping : mappings) {
				
				Integer termId = getTermKey(mapping.getTerm());
				
				if (termId != null) {
					Map<Integer, List<Double>> iconMap = termId2Icon2Time.get(termId);
					if (iconMap == null) {
						iconMap = new HashMap<Integer, List<Double>>();
						termId2Icon2Time.put(termId, iconMap);
					}
					
					ITMImage icon = mapping.getIcon();
					if (icon != null) {
						Integer iconId = icon.getId();
						List<Double> values = iconMap.get(iconId);
						if (values == null) {
							values = new ArrayList<Double>();
							iconMap.put(iconId, values);
						}
						values.add(mapping.getExecutionTime().doubleValue());
					}
				}
			}
		}
		*/
		
		public ValueArray(List<Object[]> mappingsRaw) {
			for(Object[] mapping:mappingsRaw) {
				Integer termId = (Integer) mapping[0];
				Integer iconId = (Integer) mapping[1];
				Long  executionTime = ((BigInteger) mapping[2]).longValue();
				if (termId != null) {
					Map<Integer, List<Double>> iconMap = termId2Icon2Time.get(termId);
					if (iconMap == null) {
						iconMap = new HashMap<Integer, List<Double>>();
						termId2Icon2Time.put(termId, iconMap);
					}

					if (iconId != null) {
						List<Double> values = iconMap.get(iconId);
						if (values == null) {
							values = new ArrayList<Double>();
							iconMap.put(iconId, values);
						}
						values.add(executionTime.doubleValue());
					}
				}
			}
		}

		private List<Double> getValues(ItmTerm term, ITMImage icon) {
			List<Double> rv = new ArrayList<Double>();
			Map<Integer, List<Double>> iconMap = termId2Icon2Time.get(getTermKey(term.getValue()));
			if (iconMap != null) {		
				rv = getValueList(iconMap, icon.getId());
			}
			return rv;
		}

		private Integer getTermKey(LocalizedString term) {
			return term.getId();
		}
		
		public int getValueCount(ItmTerm term, ITMImage icon) {
			return getValues(term, icon).size();
		}
		
		public List<Double> getValues(ItmTerm term) {
			List<Double> rv = new ArrayList<Double>();
			Map<Integer, List<Double>> iconMap = termId2Icon2Time.get(getTermKey(term.getValue()));
			if (iconMap != null) {
				for (Integer iconId : iconMap.keySet()) {
					rv.addAll(getValueList(iconMap, iconId));
				}
			}
			return rv;
		}
		
		public int getValueCount(ItmTerm term) {
			return getValues(term).size();
		}		

		public List<Double> getValueList(Map<Integer, List<Double>> iconMap, Integer iconId) {
			List<Double> valueList = iconMap.get(iconId);
			if (valueList != null) {
				return valueList;
			} else {
				return new ArrayList<Double>();
			}
		}
	}
	
	private final ValueArray valueArray;
	
	public IconMatchingStatistics(ReactionTimeStatisticsDao reactionTimeStatisticsDao, IconTermMatchingConfigurationEntity configuration, FilterFunctor filterFunctor) {
		
		Injector.get().inject(this);
		
		this.configuration = configuration;
	
		iconCountForConfiguration = configuration.getImages().size();
		
		List<Object[]> mappingsRaw = iconTermMappingDao.findValidResultsFast(configuration, filterFunctor);
 
		Set<Object> results = new HashSet<Object>();
		for(Object[] mapping: mappingsRaw) {
			results.add(mapping[3]);
		}

		totalNumberOfSurveyExecutions = results.size();
		
		valueArray = new ValueArray(mappingsRaw);
				
		final Double predictedReactionTime = computeOverallPredictedReactionTime(reactionTimeStatisticsDao);		
		
		if (predictedReactionTime != null) {
			isValid = true;
			overallPredictedReactionTime = predictedReactionTime.doubleValue();
		} else {
			isValid = false;
			overallPredictedReactionTime = -1;
		}
	}

	boolean isValid() {
		return isValid;
	}
		
	public  double getDifferenceFromMeanReactionTime(ItmTerm term) {
		return getDifferenceRateFromMeanReactionTime(getMeanReactionTimeForValues(valueArray.getValues(term)));
	}	

	public double getDifferenceFromMeanReactionTime(ItmTerm term,  ITMImage icon) {
		return getDifferenceRateFromMeanReactionTime(getMeanReactionTimeForValues(valueArray.getValues(term, icon)));
	}
	
	private double getMeanReactionTimeForValues(List<Double> values) {		
		SummaryStatistics stats = SummaryStatistics.newInstance();
		for (Double value : values) {
			if (value != null && isValid(value)) {
				stats.addValue(value);
			}
		}		
		return stats.getMean();
	}
		
	private final Map<ITMImage, List<TermAssignment>> termAssignmentMap = new HashMap<ITMImage, List<TermAssignment>>();
	
	// get assignment-rates to all terms
	public List<TermAssignment> getRoundedTermAssignments(ITMImage image) {
	
		List<TermAssignment> termAssignments = termAssignmentMap.get(image);
		
		if (termAssignments == null) {
			termAssignments = new ArrayList<TermAssignment>();
			
			for (ItmTerm term : getTerms()) {
				termAssignments.add(
					new TermAssignment(
						term.getValue(), 
						FormatUtils.round(getAssignmentRate(term, image), 0)
					)
				);
			}
			
			// sort by assignment
			Collections.sort(termAssignments, 
				new Comparator<TermAssignment>() {

					@Override
					public int compare(TermAssignment termAssignment1, TermAssignment termAssignment2) {
						return -Double.compare(termAssignment1.getAssignment(), termAssignment2.getAssignment());
					}
				
				}
			);
			termAssignmentMap.put(image, termAssignments);
			
		}
		
		return termAssignments;
	}

	double getHighestAssignmentForOtherTerm(ItmTerm theTerm, ITMImage icon) {
		double rv = 0;
		for (ItmTerm term : getTerms()) {
			if (!term.equals(theTerm)) {
				double assignmentRate = getAssignmentRate(term, icon);
				if (assignmentRate > rv) {
					rv = assignmentRate;
				}
			}
		}
		return rv;
	}

	double getAssignmentRate(ItmTerm term, ITMImage icon) {	
		// total assignments of icons to this term
		int maxAssignments = valueArray.getValueCount(term);
		
		// how many assignments of this icon to term
		int assignmentCountForIcon = valueArray.getValueCount(term, icon);
		
		if (maxAssignments > 0) {
			return 100 * ((double) assignmentCountForIcon)/(maxAssignments);
		} else {
			return -1;
		}
	}

	double getMissingValueRate(ItmTerm term) {
		int maxAssignments = valueArray.getValueCount(term);
		
		double rv = -1;
		
		if (totalNumberOfSurveyExecutions > 0) {
			rv  = 100 * (totalNumberOfSurveyExecutions - maxAssignments)/((double)totalNumberOfSurveyExecutions);
		} 
		
		return rv;
	}
	
	public int getTotalNumberOfSurveyExecutions() {
		return totalNumberOfSurveyExecutions;
	}

	private double getDifferenceRateFromMeanReactionTime(double time) {
		return 100 * (time -  getOverallMeanReactionTime())/getOverallMeanReactionTime();
	}
	
	private Double computeOverallPredictedReactionTime(ReactionTimeStatisticsDao reactionTimeStatisticsDao) {
		final IconTestReactionTimeStatistics reactionTimeStatistics = reactionTimeStatisticsDao.findByIconCount(iconCountForConfiguration);
		if (reactionTimeStatistics != null) {		
			return reactionTimeStatistics.getRegressionValue();
		} else {		
			logger.error("no reaction time statistics found for iconcount " + iconCountForConfiguration);
			return null;			
		}
	}
	
	private boolean isValid(double value) {
		
		// only accept values betwenn minSeconds and maxSeconds
		double minSeconds = 1.5;			
		
		double maxSeconds = 60;
	
		return ((value > minSeconds * 1000) && (value < maxSeconds * 1000));
		
	}
	
	private double getOverallMeanReactionTime() {
		return overallPredictedReactionTime;				
	}

}
