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

import java.util.List;

import org.apache.wicket.markup.html.panel.Panel;
import org.springframework.beans.factory.annotation.Autowired;

import com.userweave.components.callback.EventHandler;
import com.userweave.components.navigation.breadcrumb.StateChangeTrigger;
import com.userweave.dao.TestResultDao;
import com.userweave.domain.Study;
import com.userweave.module.AbstractModule;
import com.userweave.module.methoden.iconunderstandability.dao.ITMTestResultDao;
import com.userweave.module.methoden.iconunderstandability.dao.IconTermMatchingConfigurationDao;
import com.userweave.module.methoden.iconunderstandability.dao.IconTermMatchingType;
import com.userweave.module.methoden.iconunderstandability.domain.IconTermMatchingConfigurationEntity;
import com.userweave.module.methoden.iconunderstandability.page.conf.IconTermMatchingConfUI;
import com.userweave.pages.components.slidableajaxtabpanel.ChangeTabsCallback;

public abstract class IconMatchingMethod extends AbstractModule<IconTermMatchingConfigurationEntity> {

	@Autowired
	private IconTermMatchingConfigurationDao configurationDao;
	
	@Override
	public IconTermMatchingConfigurationDao getConfigurationDao() {
		return configurationDao;
	}

	@Autowired
	private ITMTestResultDao resultDao;

	@Override
	protected TestResultDao getTestResultDao() {
		return resultDao;
	}

	@Override
	public Panel getConfigurationUI(
			String id, 
			IconTermMatchingConfigurationEntity configuration, 
			StateChangeTrigger trigger,
			ChangeTabsCallback callback,
			EventHandler addFilterCallback)
			//EventHandler callback,
			//SlidableAjaxTabbedPanel tabbedPanel,
			//final int moduleIndex,
			//FilterFunctorCallback filterFunctorCallback) 
	{
		//return new IconTermMatchingConfUI(id, configuration.getId(), callback, tabbedPanel, moduleIndex);
		return new IconTermMatchingConfUI(id, trigger, configuration.getId(), callback);
	}

//	public Component getReportUI(String id, IconTermMatchingConfigurationEntity configuration, FilterFunctorCallback callback) {	
//		return new ITMReportUI(id, configuration);
//	}

	@Override
	protected IconTermMatchingConfigurationEntity createConfiguration() {
		IconTermMatchingConfigurationEntity e = new IconTermMatchingConfigurationEntity();
		e.setType(getType());
		return e;
	}

	@Override 
	public List<IconTermMatchingConfigurationEntity> getConfigurations(Study studie) {
		return getConfigurationDao().findByStudyAndType(studie, getType());
	}


	protected abstract IconTermMatchingType getType();
}
