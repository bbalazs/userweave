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
package com.userweave.module.methoden.rrt.page.conf;


import org.apache.wicket.Component;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.components.navigation.breadcrumb.StateChangeTrigger;
import com.userweave.dao.StudyDependendDao;
import com.userweave.domain.Study;
import com.userweave.module.methoden.rrt.dao.RrtConfigurationDao;
import com.userweave.module.methoden.rrt.domain.RrtConfigurationEntity;
import com.userweave.module.methoden.rrt.page.report.RrtReportUI;
import com.userweave.pages.components.slidableajaxtabpanel.ChangeTabsCallback;
import com.userweave.pages.configuration.base.ModuleConfigurationReportPanel;

/**
 * Configuration UI for rrt method.
 * 
 * @author opr
 */
public class RrtConfUI //extends ModuleConfigurationBaseUI<RrtConfigurationEntity> 
	extends ModuleConfigurationReportPanel<RrtConfigurationEntity>
{
	private static final long serialVersionUID = 1L;

	@SpringBean
	private RrtConfigurationDao dao;

	@Override
	protected StudyDependendDao<RrtConfigurationEntity> getBaseDao() 
	{
		return dao;
	}
	
	@Override
	protected StudyDependendDao<RrtConfigurationEntity> getConfigurationDao() 
	{
		return dao;
	}
	
	public RrtConfUI(
			String id, Integer configurationId,
//			final EventHandler callback, final SlidableAjaxTabbedPanel tabbedPanel,
//			final int moduleIndex) 
			final StateChangeTrigger trigger,
			ChangeTabsCallback callback)
	{
		//super(id, configurationId, callback, tabbedPanel, moduleIndex);
		super(id, trigger, configurationId, callback);
		
//		AuthOnlyTextField prefix = 
//			new AuthOnlyTextField(
//					"prefix", 
//					new LocalizedPropertyModel(getModel(), "prefix", getStudyLocale()),
//					getUpdateBehavior("onblur"))
//		{
//			@Override
//			protected boolean isEditAllowed()
//			{
//				return getStudy().getState() == StudyState.INIT;
//			}
//		};
//		
//		AuthOnlyTextField postfix = 
//			new AuthOnlyTextField(
//					"postfix", 
//					new LocalizedPropertyModel(getModel(), "postfix", getStudyLocale()),
//					getUpdateBehavior("onblur"))
//		{
//			@Override
//			protected boolean isEditAllowed()
//			{
//				return getConfiguration().getStudy().getState() == StudyState.INIT;
//			}
//		};
//		
//		prefix.setRequired(true);
//		postfix.setRequired(true);
//		
//		addFormComponent(prefix);
//		addFormComponent(postfix);
//		
//		
//		addFormComponent(new RrtTermConfigurationPanel("terms", configurationId, getStudyLocale()));
		
		/**
		 * TODO:RrtConfigurationPanel contains elements which are
		 * inherited from a form.
		 */
		//addFormComponent(new RrtConfigurationPanel("rrt", configurationId, getStudyLocale()));
	}

	@Override
	protected Study getStudy()
	{
		return getConfiguration().getStudy();
	}

	@Override
	protected Component getConfigurationComponent(String id)
	{
		return new RrtConfigurationPanel(id, getConfiguration().getId(), getStudyLocale());
	}

	@Override
	protected Component getReportComponent(String id, StateChangeTrigger trigger)
	{
		return new RrtReportUI(id, getConfiguration().getId(), trigger.getFilterFunctorCallback());
	}
}