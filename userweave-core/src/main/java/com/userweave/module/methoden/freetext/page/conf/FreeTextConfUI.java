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
package com.userweave.module.methoden.freetext.page.conf;


import org.apache.wicket.Component;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.components.navigation.breadcrumb.StateChangeTrigger;
import com.userweave.dao.StudyDependendDao;
import com.userweave.domain.Study;
import com.userweave.module.methoden.freetext.dao.FreeTextConfigurationDao;
import com.userweave.module.methoden.freetext.domain.FreeTextConfigurationEntity;
import com.userweave.module.methoden.freetext.page.report.FreeTextReportPanel;
import com.userweave.pages.components.slidableajaxtabpanel.ChangeTabsCallback;
import com.userweave.pages.configuration.base.ModuleConfigurationReportPanel;

/**
 * Configuration UI for freetext method.
 * 
 * @author opr
 */
public class FreeTextConfUI 
extends ModuleConfigurationReportPanel<FreeTextConfigurationEntity> 
{
	private static final long serialVersionUID = 1L;

	@SpringBean
	private FreeTextConfigurationDao dao;
	
	@Override
	protected StudyDependendDao<FreeTextConfigurationEntity> getBaseDao() 
	{
		return dao;
	}
	
	@Override
	protected StudyDependendDao<FreeTextConfigurationEntity> getConfigurationDao() 
	{
		return dao;
	}
	
	/**
	 * Default constructor.
	 * 
	 * @param id
	 * 		Component id.
	 * @param configuration
	 * 		Configuration entity to load.
	 */
	public FreeTextConfUI(
			String id, 
			FreeTextConfigurationEntity configuration,
			StateChangeTrigger trigger,
			ChangeTabsCallback callback)
	{
		super(id, trigger, configuration.getId(), callback);
	}
	@Override
	protected Study getStudy()
	{
		return getEntity().getStudy();
	}

	@Override
	protected Component getConfigurationComponent(String id)
	{
		return new FreeTextConfigurationPanel(id, getConfiguration().getId());
	}

	@Override
	protected Component getReportComponent(String id, StateChangeTrigger trigger)
	{
		return new FreeTextReportPanel(id, getConfiguration(), trigger.getFilterFunctorCallback());
	}
}

