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
package com.userweave.module.methoden.questionnaire.page.report.question;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math.stat.descriptive.SummaryStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class AnswerStatistics<T> implements Serializable {
	
	private transient final Logger logger = LoggerFactory.getLogger(AnswerStatistics.class);
	
	private final Map<T, Triple<SummaryStatistics, AnswerCounter<Integer>, Integer>> value2RatingStatistics = new HashMap<T, Triple<SummaryStatistics, AnswerCounter<Integer>, Integer>>();

	public void addRating(T object, Integer rating) {		
		
		Triple<SummaryStatistics, AnswerCounter<Integer>, Integer> pair = value2RatingStatistics.get(object);
		if (pair == null) {			
			pair = new Triple(SummaryStatistics.newInstance(), new AnswerCounter<Integer>(), new Integer(0));
			value2RatingStatistics.put(object, pair);
		}
		if (rating != null) {
			pair.getFirst().addValue(rating);
		}
		
		pair.getSecond().addAnswer(rating);		
	}
	
	public Double getRatingMean(T object) {
		SummaryStatistics summaryStatistics = getSummaryStatistics(object);		
		if (summaryStatistics != null) {
			return summaryStatistics.getMean();
		} else { 
			return 0d;
		}
	}
	
	public Double getRatingStandardDeviation(T object) {
		SummaryStatistics summaryStatistics = getSummaryStatistics(object);		
		if (summaryStatistics != null) {
			return summaryStatistics.getStandardDeviation();
		} else { 
			return null;
		}
	}

	private SummaryStatistics getSummaryStatistics(T object) {
		Triple<SummaryStatistics, AnswerCounter<Integer>, Integer> pair = value2RatingStatistics.get(object);
		if (pair != null) {
			return pair.getFirst();
		} else {
			return null;
		}		
	}
	
	public int getCount(T object, int rating) {
		Triple<SummaryStatistics, AnswerCounter<Integer>, Integer> pair = value2RatingStatistics.get(object);
		if (pair != null) {
			return pair.getSecond().getCount(rating);			
		} else {
			logger.error("no count for " + object.toString());		
		}
		return 0;
	}
	
	
	/**
	 * total count of answers  for this object
	 * @param object
	 * @return
	 */
	public int getTotalCount(T object) {
		Triple<SummaryStatistics, AnswerCounter<Integer>, Integer> pair = value2RatingStatistics.get(object);
		if (pair != null) {
			return pair.getSecond().getTotalCount();
		}
		return 0;
	}

	/**
	 * total count of null-answers for this object (missing-answers)
	 * @param object
	 * @return
	 */
	public int getMissing(T object) {
		Triple<SummaryStatistics, AnswerCounter<Integer>, Integer> triple = value2RatingStatistics.get(object);
		if (triple != null) {
			return triple.getSecond().getCount(null);
		}
		return 0;
	}
	
}

