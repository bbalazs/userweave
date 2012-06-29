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
package com.userweave.module.methoden.mockup.page.conf;


import org.apache.wicket.Component;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.components.navigation.breadcrumb.StateChangeTrigger;
import com.userweave.dao.StudyDependendDao;
import com.userweave.domain.Study;
import com.userweave.module.methoden.mockup.dao.MockupConfigurationDao;
import com.userweave.module.methoden.mockup.domain.MockupConfigurationEntity;
import com.userweave.module.methoden.mockup.page.report.MockupReportPanel;
import com.userweave.pages.components.slidableajaxtabpanel.ChangeTabsCallback;
import com.userweave.pages.configuration.base.ModuleConfigurationReportPanel;

/**
 * Configuration UI for mockup method.
 * 
 * @author opr
 */
public class MockupConfUI
extends ModuleConfigurationReportPanel<MockupConfigurationEntity>
{
	private static final long serialVersionUID = 1L;
	
	@SpringBean
	private MockupConfigurationDao dao;
	
	@Override
	protected StudyDependendDao<MockupConfigurationEntity> getBaseDao() 
	{
		return dao;
	}
	
	@Override
	protected StudyDependendDao<MockupConfigurationEntity> getConfigurationDao() 
	{
		return dao;
	}
	
	/**
	 * Default constructor.
	 * 
	 * @param id
	 * 		Component id.
	 * @param configuration
	 * 		Entity to configure.
	 */
	public MockupConfUI(
			String id, 
			MockupConfigurationEntity configuration, 
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
		return new MockupConfigurationPanel(id, getEntityId())
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected StudyDependendDao<MockupConfigurationEntity> getConfigurationDao()
			{
				return dao;
			}
		};
	}

	@Override
	protected Component getReportComponent(String id, StateChangeTrigger trigger)
	{
		return new MockupReportPanel(id, getEntity(), trigger.getFilterFunctorCallback());
	}
}

