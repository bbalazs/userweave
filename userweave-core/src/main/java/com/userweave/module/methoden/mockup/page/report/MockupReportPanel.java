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
package com.userweave.module.methoden.mockup.page.report;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.domain.service.GeneralStatistics;
import com.userweave.module.methoden.mockup.dao.MockupResultDao;
import com.userweave.module.methoden.mockup.domain.MockupConfigurationEntity;
import com.userweave.pages.configuration.report.FilterFunctorCallback;
import com.userweave.pages.configuration.report.FilteredModuleConfigurationReportPanel;
import com.userweave.presentation.model.EntityBaseLoadableDetachableModel;

/**
 * @author oma
 */
public class MockupReportPanel 
	extends FilteredModuleConfigurationReportPanel<MockupConfigurationEntity> 
{
	private static final long serialVersionUID = 1L;

	@SpringBean
	private MockupResultDao resultDao;
	
	public MockupReportPanel(String id, final MockupConfigurationEntity configuration, FilterFunctorCallback callback) {
		super(id, new EntityBaseLoadableDetachableModel<MockupConfigurationEntity>(configuration), callback);
		
		initPanel();
	}

	@Override
	protected Component createReportContainer(boolean showDetails) {
		return null;
	}

	@Override
	protected GeneralStatistics evaluateGeneralStatistics(FilterFunctorCallback callback) {
		return resultDao.findValidResultStatistics(getConfiguration(), callback.getFilterFunctor());
	}

	@Override
	protected IModel getTypeOfModule()
	{
		return new StringResourceModel("mockup_type", this, null);
	}	

}

