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
package com.userweave.module.methoden.iconunderstandability;

import java.util.Locale;

import com.userweave.application.OnFinishCallback;
import com.userweave.module.methoden.iconunderstandability.dao.IconTermMatchingType;
import com.userweave.module.methoden.iconunderstandability.domain.IconTermMatchingConfigurationEntity;
import com.userweave.module.methoden.iconunderstandability.page.survey.bmt.BestMatchingTermTestUI;
import com.userweave.pages.test.SurveyUI;

public class BestMatchingTermMethod extends IconMatchingMethod {

	public BestMatchingTermMethod() {
		init("Best Matching Term", "Icons mit dem bestmöglichen Begriff versehen");
	}

	public static final String moduleId = "bestMatchingTermMethod";
	
	public String getModuleId() {
		return moduleId;
	}

	@Override
	protected IconTermMatchingType getType() {
		return IconTermMatchingType.BMT;
	}
	
	public SurveyUI getTestUI(
		String id, int surveyExecutionId, IconTermMatchingConfigurationEntity configuration, OnFinishCallback onFinishCallback,
		Locale locale)  
	{		
		return new BestMatchingTermTestUI(id, surveyExecutionId, configuration.getId(), onFinishCallback, locale);
	}
}
