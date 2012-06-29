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
package com.userweave.module.methoden.rrt.page.report;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.domain.StudyState;
import com.userweave.domain.service.GeneralStatistics;
import com.userweave.module.methoden.rrt.dao.RrtResultDao;
import com.userweave.module.methoden.rrt.domain.RrtConfigurationEntity;
import com.userweave.pages.configuration.report.FilterFunctorCallback;
import com.userweave.pages.configuration.report.FilteredModuleConfigurationReportPanel;
import com.userweave.presentation.model.EntityBaseLoadableDetachableModel;

/**
 * @author oma
 */
public class RrtReportUI extends FilteredModuleConfigurationReportPanel<RrtConfigurationEntity> 
{	
	private static final long serialVersionUID = 1L;
	
	@SpringBean
	private RrtResultDao resultDao;
	
	public RrtReportUI(String id, Integer configurationId,FilterFunctorCallback callback) {
		super(id, 
			  new EntityBaseLoadableDetachableModel<RrtConfigurationEntity>(RrtConfigurationEntity.class, configurationId), callback);	
		
		initPanel();
	}

	@Override
	protected Component createReportContainer(boolean showDetails) 
	{
		boolean show = showDetails;
		
		if(getStudy().getState() == StudyState.RUNNING)
		{
			show = false;
		}
		
		return new RrtReportPanel(
			"reportPanel", 
			show, 
			getConfiguration(),
			getFilterFuctorCallback());
	}

	@Override
	protected GeneralStatistics evaluateGeneralStatistics(FilterFunctorCallback callback) {
		return resultDao.findValidResultStatistics(getConfiguration(), callback.getFilterFunctor());
	}
	
	@Override
	protected IModel getTypeOfModule()
	{
		return new StringResourceModel("rrt_type", this, null);
	}
}



