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

import org.apache.wicket.markup.html.panel.Panel;

import com.userweave.application.OnFinishCallback;
import com.userweave.components.callback.EventHandler;
import com.userweave.components.navigation.breadcrumb.StateChangeTrigger;
import com.userweave.module.methoden.iconunderstandability.dao.IconTermMatchingType;
import com.userweave.module.methoden.iconunderstandability.domain.IconTermMatchingConfigurationEntity;
import com.userweave.module.methoden.iconunderstandability.page.survey.bmi.BestMatchingIconTestUI;
import com.userweave.pages.components.slidableajaxtabpanel.ChangeTabsCallback;
import com.userweave.pages.test.SurveyUI;

public class BestMatchingIconMethod extends IconMatchingMethod {

	public BestMatchingIconMethod() {
		init("Icon Quality Test", "Begriffe mit dem bestmöglichen Icon versehen");
	}

	public static final String moduleId = "bestMatchingIconMethod";
	
	public String getModuleId() {
		return moduleId;
	}

//	@Override
//	public Component getReportUI(String id, IconTermMatchingConfigurationEntity configuration,FilterFunctorCallback callback) {
//		return new BMIReportUI(id, configuration, callback);
//	}
	
//	@Override
//	public Component getConfigurationUI(
//			String id,
//			IconTermMatchingConfigurationEntity configuration,
//			EventHandler callback,
//			final SlidableAjaxTabbedPanel tabbedPanel,
//			final int moduleIndex,
//			FilterFunctorCallback filterFunctorCallback) {
//		
////		if (structure.getDefinition().getType().equals(TermStructureDefinition.TYPE)) {
////			TermStructureDefinition termStructureDefinition = (TermStructureDefinition) structure.getDefinition();
////			return new BMITermConfUI(id, termStructureDefinition.getTermId(), callback);
////			//return new BMITermReportPanel(id, configuration, termStructureDefinition.getTermId());
////		}		
////		else {
//			return super.getConfigurationUI(id, configuration, callback, tabbedPanel, moduleIndex, filterFunctorCallback);
////		}
//	}
	
	@Override
	public Panel getConfigurationUI(
			String id,
			IconTermMatchingConfigurationEntity configuration,
			StateChangeTrigger trigger,
			ChangeTabsCallback callback,
			EventHandler addFilterCallback)
	{
		return super.getConfigurationUI(id, configuration, trigger, callback, addFilterCallback);
	}
	
	@Override
	protected IconTermMatchingType getType() {
		return IconTermMatchingType.BMI;
	}
	
	public SurveyUI getTestUI(String id, int surveyExecutionId, IconTermMatchingConfigurationEntity configuration, OnFinishCallback onFinishCallback,
			Locale locale)  {		
		return new BestMatchingIconTestUI(id, surveyExecutionId, configuration.getId(), onFinishCallback, locale);
	}

}
