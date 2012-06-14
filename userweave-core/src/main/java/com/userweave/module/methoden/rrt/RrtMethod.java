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
package com.userweave.module.methoden.rrt;

import java.util.Locale;

import org.apache.wicket.markup.html.panel.Panel;
import org.springframework.beans.factory.annotation.Autowired;

import com.userweave.application.OnFinishCallback;
import com.userweave.components.callback.EventHandler;
import com.userweave.components.navigation.breadcrumb.StateChangeTrigger;
import com.userweave.dao.StudyDependendDao;
import com.userweave.dao.TestResultDao;
import com.userweave.module.AbstractModule;
import com.userweave.module.methoden.rrt.dao.RrtConfigurationDao;
import com.userweave.module.methoden.rrt.dao.RrtResultDao;
import com.userweave.module.methoden.rrt.domain.RrtConfigurationEntity;
import com.userweave.module.methoden.rrt.page.conf.RrtConfUI;
import com.userweave.module.methoden.rrt.page.survey.RrtSurveyUI;
import com.userweave.pages.components.slidableajaxtabpanel.ChangeTabsCallback;
import com.userweave.pages.test.SurveyUI;

public class RrtMethod extends AbstractModule<RrtConfigurationEntity> {

	public RrtMethod() {
		init("Relevance Analysis", "Begriffe in Reihenfolge bringen");
	}

	public static final String moduleId = "rrtMethod";
	
	public String getModuleId() {
		return moduleId;
	}
	
	@Autowired
	private RrtConfigurationDao configurationDao;
	
	@Override
	public StudyDependendDao<RrtConfigurationEntity> getConfigurationDao() {
		return configurationDao;
	}
	
	@Autowired
	private RrtResultDao resultDao;

	@Override
	protected TestResultDao getTestResultDao() {
		return resultDao;
	}


	@Override
	protected RrtConfigurationEntity createConfiguration() {
		return new RrtConfigurationEntity();
	}

	@Override
	public Panel getConfigurationUI(
			String id, 
			RrtConfigurationEntity configuration, 
//			EventHandler callback, final SlidableAjaxTabbedPanel tabbedPanel, final int moduleIndex,
//			FilterFunctorCallback filterFunctorCallback) {
			StateChangeTrigger trigger,
			ChangeTabsCallback callback,
			EventHandler addFilterCallback)
	{
		//return new RrtConfUI(id, configuration.getId(), callback, tabbedPanel, moduleIndex);
		return new RrtConfUI(id, configuration.getId(), trigger, callback);
	}

//	@Override
//	public Component getReportUI(String id, RrtConfigurationEntity configuration, FilterFunctorCallback callback) { 
//		return new RrtReportUI(id, configuration.getId(), callback);
//	}

	@Override
	public SurveyUI getTestUI(String id, int surveyExecutionID, RrtConfigurationEntity configuration, OnFinishCallback onFinishCallback,
			Locale locale) {
		return new RrtSurveyUI(id, surveyExecutionID, configuration.getId(), onFinishCallback, locale);
	}
	
}