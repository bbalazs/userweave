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
package com.userweave.module.methoden.iconunderstandability.page.report.bmi;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.domain.service.GeneralStatistics;
import com.userweave.module.methoden.iconunderstandability.dao.ITMTestResultDao;
import com.userweave.module.methoden.iconunderstandability.domain.IconTermMatchingConfigurationEntity;
import com.userweave.pages.configuration.report.FilterFunctorCallback;
import com.userweave.pages.configuration.report.FilteredModuleConfigurationReportPanel;
import com.userweave.presentation.model.EntityBaseLoadableDetachableModel;

/**
 * @author oma
 */

public class BMIReportUI extends FilteredModuleConfigurationReportPanel<IconTermMatchingConfigurationEntity> 
{
	private static final long serialVersionUID = 1L;
	
	@SpringBean
	private ITMTestResultDao resultDao;
	
	public BMIReportUI(
			String id, 
			IconTermMatchingConfigurationEntity configuration,
			FilterFunctorCallback callback) 
	{
			
		super(id, 
			  new EntityBaseLoadableDetachableModel<IconTermMatchingConfigurationEntity>(configuration),
			  callback);
		
		initPanel();
	}
	
	@Override
	protected Component createReportContainer(boolean showDetails) 
	{
		if(! showDetails)
		{
			return new WebMarkupContainer("reportPanel");
		}
		
		return new IconTestReportPanel("reportPanel", getConfiguration(), getFilterFuctorCallback());	
	}

	@Override
	protected GeneralStatistics evaluateGeneralStatistics(FilterFunctorCallback callback) 
	{
		return resultDao.findValidResultStatistics(getConfiguration(), callback.getFilterFunctor());
	}	

	@Override
	protected IModel getTypeOfModule()
	{
		return new StringResourceModel("icontest_type", BMIReportUI.this, null);
	}
}

