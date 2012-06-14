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
package com.userweave.module.methoden.freetext;

import java.util.Locale;

import org.apache.wicket.markup.html.panel.Panel;
import org.springframework.beans.factory.annotation.Autowired;

import com.userweave.application.OnFinishCallback;
import com.userweave.components.callback.EventHandler;
import com.userweave.components.navigation.breadcrumb.StateChangeTrigger;
import com.userweave.dao.StudyDependendDao;
import com.userweave.dao.TestResultDao;
import com.userweave.module.AbstractModule;
import com.userweave.module.methoden.freetext.dao.FreeTextConfigurationDao;
import com.userweave.module.methoden.freetext.dao.FreeTextResultDao;
import com.userweave.module.methoden.freetext.domain.FreeTextConfigurationEntity;
import com.userweave.module.methoden.freetext.page.conf.FreeTextConfUI;
import com.userweave.module.methoden.freetext.page.survey.FreeTextTestUI;
import com.userweave.pages.components.slidableajaxtabpanel.ChangeTabsCallback;
import com.userweave.pages.test.SurveyUI;

public class FreeTextMethod extends AbstractModule<FreeTextConfigurationEntity> {

	public FreeTextMethod() {
		init("Freetext", "Enter some free text...");
	}
	
	public static final String moduleId = "freeTextMethod";
	
	public String getModuleId() {
		return moduleId;
	}
	
	@Autowired
	private FreeTextConfigurationDao configurationDao;
	
	@Override
	protected StudyDependendDao<FreeTextConfigurationEntity> getConfigurationDao() {
		return configurationDao;
	}

	@Autowired
	private FreeTextResultDao resultDao;

	@Override
	protected TestResultDao getTestResultDao() {
		return resultDao;
	}

	@Override
	public SurveyUI<FreeTextConfigurationEntity> getTestUI(String id, int surveyExecutionId, FreeTextConfigurationEntity configuration, OnFinishCallback onFinishCallback,
			Locale locale) {
		return new FreeTextTestUI(id, configuration, surveyExecutionId, onFinishCallback, locale);
	}
	
//	@Override
//	public Component getConfigurationUI(
//			String id, 
//			FreeTextConfigurationEntity configuration, 
//			EventHandler callback,
//			SlidableAjaxTabbedPanel tabbedPanel,
//			final int moduleIndex,
//			FilterFunctorCallback filterFunctorCallback) {
//		return new FreeTextConfUI(id, configuration, callback, tabbedPanel, moduleIndex);
//	}

	@Override
	public Panel getConfigurationUI(
			String id, 
			FreeTextConfigurationEntity configuration, 
			StateChangeTrigger trigger,
			ChangeTabsCallback callback,
			EventHandler addFilterCallback)
	{
//		return new FreeTextConfUI(id, configuration, callback, tabbedPanel, moduleIndex);
		
		return new FreeTextConfUI(id, configuration, trigger, callback);
	}
	
//	@Override
//	public Component getReportUI(String id, FreeTextConfigurationEntity configuration, FilterFunctorCallback filterFunctorCallback) {
//		return new FreeTextReportPanel(id, configuration, filterFunctorCallback);
//	}
	
	
//	@Override
//	public boolean hasReportUI() {
//		return true;
//	}
	
	@Override
	public boolean storesResults() {
		return false;
	}
	
	@Override
	protected FreeTextConfigurationEntity createConfiguration() {
		return new FreeTextConfigurationEntity();
	}

}