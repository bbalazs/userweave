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
package com.userweave.module.methoden.questionnaire.page.report;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.domain.service.GeneralStatistics;
import com.userweave.module.methoden.questionnaire.dao.QuestionnaireResultDao;
import com.userweave.module.methoden.questionnaire.domain.QuestionnaireConfigurationEntity;
import com.userweave.pages.configuration.report.FilterFunctorCallback;
import com.userweave.pages.configuration.report.FilteredModuleConfigurationReportPanel;
import com.userweave.presentation.model.EntityBaseLoadableDetachableModel;

/**
 * @author oma
 */
public class QuestionnaireReportPanel 
	extends FilteredModuleConfigurationReportPanel<QuestionnaireConfigurationEntity> 
{
	private static final long serialVersionUID = 1L;

	@SpringBean
	private QuestionnaireResultDao resultDao;

	public QuestionnaireReportPanel(
			String id, 
			final QuestionnaireConfigurationEntity configuration,
			FilterFunctorCallback callback) 
	{
		super(id, 
			  new EntityBaseLoadableDetachableModel<QuestionnaireConfigurationEntity>(configuration),
			  callback);
		
		initPanel();
	}

	@Override
	protected GeneralStatistics evaluateGeneralStatistics(FilterFunctorCallback callback) 
	{
		return resultDao.findValidResultStatistics(getConfiguration(), callback.getFilterFunctor());
	}	

	
	@Override
	protected Component createReportContainer(boolean showDetails) 
	{
		// this panel has no special report component.
		return null;
	}
	
	@Override
	protected IModel getTypeOfModule()
	{
		return new StringResourceModel("questionnaire_type", this, null);
	}
}

