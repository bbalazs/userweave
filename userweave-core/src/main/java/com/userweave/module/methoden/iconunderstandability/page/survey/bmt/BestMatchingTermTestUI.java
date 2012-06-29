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
package com.userweave.module.methoden.iconunderstandability.page.survey.bmt;

import java.util.Locale;
import java.util.Map;

import com.userweave.application.OnFinishCallback;
import com.userweave.domain.LocalizedString;
import com.userweave.module.methoden.iconunderstandability.domain.ITMImage;
import com.userweave.module.methoden.iconunderstandability.domain.ITMTestResult;
import com.userweave.module.methoden.iconunderstandability.page.survey.IconMatchingTestUIWithMultiplePages;
import com.userweave.module.methoden.iconunderstandability.page.survey.IconTermMapping;

@SuppressWarnings("serial")
public class BestMatchingTermTestUI extends IconMatchingTestUIWithMultiplePages {

	public BestMatchingTermTestUI(
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
		shuffleImages();
		setImageV(0);
	}

	@Override
	public void completeMethodStep(ITMTestResult testResult, Map<String, String> matchingResult, long executionTime) {
		// get all terms from list, check if there is a result that 
		// is stored in itemtestresult but is still in list of non-visited terms 
		for(ITMImage image : getImages().subList(getPosition(), getTerms().size())) {
			IconTermMapping mapping = testResult.getIconTermMapping(image);
			if(mapping != null) {
				removeIconTermMappingFromTestResult(testResult, mapping);
			}
		}		
		
		String iconStr = getImagesV().get(0).getId().toString();

		ITMImage itmImage = itmImageDao.findById(Integer.decode(iconStr));
		LocalizedString term = findTermForIcon(matchingResult, iconStr);
		
		// todo: check for null value.

		addIconTermMappingToTestResult(executionTime, testResult, term, itmImage);
		
		itmTestResultDao.save(testResult);
		
		// prepare next round
		incrementPosition();
		
		if(!finished()) {		
			setImageV(getPosition());
			shuffleTermsV();						
		}
	}

	@Override
	protected boolean finished() {
		return getPosition() >= getNumberOfImages();
	}

	private LocalizedString findTermForIcon(Map<String, String> matchingResult, String iconStr) {
		String term = null;
		for(String mrTerm : matchingResult.keySet()) {
			if(matchingResult.get(mrTerm).equals(iconStr)) {
				term = mrTerm;
				break;
			}
		}
		return findTermForString(term);
	}

	@Override
	public int getMaxPosition() {
		return getNumberOfImages();
	}
}
