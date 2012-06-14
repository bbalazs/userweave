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
package com.userweave.module.methoden.iconunderstandability.page.conf;

import org.apache.wicket.Component;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.components.navigation.breadcrumb.StateChangeTrigger;
import com.userweave.dao.StudyDependendDao;
import com.userweave.domain.Study;
import com.userweave.module.methoden.iconunderstandability.dao.IconTermMatchingConfigurationDao;
import com.userweave.module.methoden.iconunderstandability.domain.IconTermMatchingConfigurationEntity;
import com.userweave.module.methoden.iconunderstandability.page.report.bmi.BMIReportUI;
import com.userweave.pages.components.slidableajaxtabpanel.ChangeTabsCallback;
import com.userweave.pages.configuration.base.ModuleConfigurationReportPanel;

/**
 * Configuration UI for icon test method.
 * 
 * @author opr
 */
public class IconTermMatchingConfUI //extends ModuleConfigurationBaseUI 
extends ModuleConfigurationReportPanel<IconTermMatchingConfigurationEntity>
{
	private static final long serialVersionUID = 1L;
	
	@SpringBean 
	private IconTermMatchingConfigurationDao dao;
	
	@Override
	protected StudyDependendDao<IconTermMatchingConfigurationEntity> getBaseDao() 
	{
		return dao;
	}
	
	@Override
	protected StudyDependendDao<IconTermMatchingConfigurationEntity> getConfigurationDao() 
	{
		return dao;
	}
	
	/**
	 * Default constructor.
	 * 
	 * @param id
	 * 		Component id.
	 * @param configurationId
	 * 		Entity id.
	 */
	public IconTermMatchingConfUI(
			String id, 
			StateChangeTrigger trigger,
			final int configurationId,
			ChangeTabsCallback callback)//, 
//			EventHandler callback, final SlidableAjaxTabbedPanel tabbedPanel,
//			final int moduleIndex) 
	{
//		super(id, configurationId, callback, tabbedPanel, moduleIndex);
		
		super(id, trigger, configurationId, callback);
	}

	@Override
	protected Component getConfigurationComponent(String id)
	{	
		return new ItmConfPanel(id, getEntityId(), getStudyLocale());
	}

	@Override
	protected Component getReportComponent(String id, StateChangeTrigger trigger)
	{
		return new BMIReportUI(id, getEntity(), trigger.getFilterFunctorCallback());
	}

	@Override
	protected Study getStudy()
	{
		return getEntity().getStudy();
	}
	
//	@Override
//	protected IModel getType() {
//		return new StringResourceModel("type_value", this, null);
//	}
}
