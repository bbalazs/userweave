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
package com.userweave.module.methoden.iconunderstandability.page.survey.itm;

import java.util.Locale;
import java.util.Map;

import com.userweave.application.OnFinishCallback;
import com.userweave.domain.LocalizedString;
import com.userweave.module.methoden.iconunderstandability.domain.ITMImage;
import com.userweave.module.methoden.iconunderstandability.domain.ITMTestResult;
import com.userweave.module.methoden.iconunderstandability.page.survey.IconMatchingTestUI;

@SuppressWarnings("serial")
public class IconTermMatchingTestUI extends IconMatchingTestUI {

	public IconTermMatchingTestUI(
		String id, int surveyExecutionId, 
		int configurationId, 
		OnFinishCallback onFinishCallback,
		Locale locale) 
	{
		super(id, surveyExecutionId, configurationId, onFinishCallback, locale);
	}

	@Override
	protected void initTermsV() {
		shuffleTermsV();		
	}

	@Override
	protected void initImagesV() {
		shuffleImagesV();
	}

	@Override
	public void completeMethodStep(ITMTestResult testResult, Map<String, String> matchingResult, long executionTime) {
		
		for (String term : matchingResult.keySet()) {
			String iconStr = matchingResult.get(term);
			Integer iconId = Integer.decode(iconStr);
			ITMImage itmImage = itmImageDao.findById(iconId);
			
			LocalizedString lsTerm = findTermForString(term);
		
			addIconTermMappingToTestResult(executionTime, testResult, lsTerm, itmImage);
		}
		
		itmTestResultDao.save(testResult);		
	}
	
	@Override
	protected boolean finished() {
		return true;
	}

}
