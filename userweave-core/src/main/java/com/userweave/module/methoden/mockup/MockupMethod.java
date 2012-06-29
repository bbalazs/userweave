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
package com.userweave.module.methoden.mockup;

import java.util.Locale;

import org.apache.wicket.markup.html.panel.Panel;
import org.springframework.beans.factory.annotation.Autowired;

import com.userweave.application.OnFinishCallback;
import com.userweave.components.callback.EventHandler;
import com.userweave.components.navigation.breadcrumb.StateChangeTrigger;
import com.userweave.dao.StudyDependendDao;
import com.userweave.dao.TestResultDao;
import com.userweave.module.AbstractModule;
import com.userweave.module.methoden.mockup.dao.MockupConfigurationDao;
import com.userweave.module.methoden.mockup.dao.MockupResultDao;
import com.userweave.module.methoden.mockup.domain.MockupConfigurationEntity;
import com.userweave.module.methoden.mockup.page.conf.MockupConfUI;
import com.userweave.module.methoden.mockup.page.survey.MockupSurveyUI;
import com.userweave.pages.components.slidableajaxtabpanel.ChangeTabsCallback;
import com.userweave.pages.test.SurveyUI;

public class MockupMethod extends AbstractModule<MockupConfigurationEntity> {

	public MockupMethod() {
		init("Mockup", "Enter some Mockup...");
	}
	
	public static final String moduleId = "mockupMethod";
	
	public String getModuleId() {
		return moduleId;
	}
	
	@Autowired
	private MockupConfigurationDao configurationDao;
	
	@Override
	protected StudyDependendDao<MockupConfigurationEntity> getConfigurationDao() {
		return configurationDao;
	}

	@Autowired
	private MockupResultDao resultDao;

	@Override
	protected TestResultDao getTestResultDao() {
		return resultDao;
	}

	@Override
	public SurveyUI<MockupConfigurationEntity> getTestUI(String id, int surveyExecutionId, MockupConfigurationEntity configuration, OnFinishCallback onFinishCallback,
			Locale locale) {
		return new MockupSurveyUI(id, configuration, surveyExecutionId, onFinishCallback, locale);
	}
	
//	@Override
//	public Component getConfigurationUI(
//			String id, MockupConfigurationEntity configuration, 
//			EventHandler callback, final SlidableAjaxTabbedPanel tabbedPanel,
//			final int moduleIndex,
//			FilterFunctorCallback filterFunctorCallback) 
//	{
//		return new MockupConfUI(id, configuration, callback, tabbedPanel, moduleIndex);
//	}

	@Override
	public Panel getConfigurationUI(
			String id, 
			MockupConfigurationEntity configuration, 
			StateChangeTrigger trigger,
			ChangeTabsCallback callback,
			EventHandler addFilterCallback) 
	{
//		return new MockupConfUI(id, configuration, callback, tabbedPanel, moduleIndex);
		return new MockupConfUI(id, configuration, trigger, callback);
	}
	
//	@Override
//	public Component getReportUI(String id, MockupConfigurationEntity configuration, FilterFunctorCallback callback) {
//		return new MockupReportPanel(id, configuration, callback);
//	}
	
	
//	@Override
//	public boolean hasReportUI() {
//		return true;
//	}
//	
	@Override
	public boolean storesResults() {
		return false;
	}
	
	@Override
	protected MockupConfigurationEntity createConfiguration() {
		return new MockupConfigurationEntity();
	}

}