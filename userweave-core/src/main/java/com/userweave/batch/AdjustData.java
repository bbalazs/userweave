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
package com.userweave.batch;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.userweave.domain.LocalizedString;
import com.userweave.domain.Study;

public class AdjustData {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"applicationContext-batch.xml");

		//new AdjustData().adjustSurveyExecution(context);
		new AdjustData().adjustLocaleInLocalizedStrings(context);
	}

	private void adjustLocaleInLocalizedStrings(ApplicationContext context) {
		AdjustLocaleInLocalizedStrings adjustLocaleInLocalizedStrings = (AdjustLocaleInLocalizedStrings)
			context.getBean("adjustLocaleInLocalizedStrings");
		
		List<Study> studies = adjustLocaleInLocalizedStrings.getStudies();
		logger.info("Found "+studies.size()+" studies");
		int checked = 0;
		for(Study study : studies) {
			checked++;
			logger.info("Checking study "+study.getName()+" ("+checked+")");
			List<LocalizedString> localizedStrings = adjustLocaleInLocalizedStrings.getLocalizedStrings(study.getId());
			for(LocalizedString localizedString : localizedStrings) {
				if(localizedString != null && localizedString.getLocale() == null) {
					localizedString.setLocale(study.getLocale());
					adjustLocaleInLocalizedStrings.save(localizedString);
				}
			}
		}
	}
	
	private void adjustSurveyExecution(ApplicationContext context) {
		AdjustSurveyExecution se = (AdjustSurveyExecution) 
			context.getBean("adjustSurveyExecution");

		List<Integer> surveyIds = se.getSurveyExecutions();
		logger.info("Found "+surveyIds.size()+" surveys");
		long overallStart = System.currentTimeMillis();
		int i=1;
		int checked = 0;
		for(Integer surveyId: surveyIds) {
			long startTime = System.currentTimeMillis();
			logger.info("Checking survey "+surveyId+" ("+(i++)+") ");
			if(se.checkAndsetExecutionFinished(surveyId)) {
				checked++;
				logger.info("  Adjusting execution finished time to NOW");
			}
			double esttime = (System.currentTimeMillis()-overallStart)*1.0f*(surveyIds.size()-i)/Math.max(1, checked)/60/1000;
			logger.info("  "+(System.currentTimeMillis()-startTime) +"ms (rest: "+esttime+" mins)");
		}	
		logger.info("done adjusting executionfinished");
	}
}
