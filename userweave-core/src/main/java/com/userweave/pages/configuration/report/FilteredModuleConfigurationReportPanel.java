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
package com.userweave.pages.configuration.report;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

import com.userweave.domain.ModuleConfigurationEntityBase;
import com.userweave.domain.Study;
import com.userweave.domain.service.GeneralStatistics;
import com.userweave.presentation.model.EntityBaseLoadableDetachableModel;


public abstract class FilteredModuleConfigurationReportPanel<T extends ModuleConfigurationEntityBase> 
	extends FilteredReportPanel 
{
	private static final long serialVersionUID = 1L;

	public FilteredModuleConfigurationReportPanel(
			String id, 
			EntityBaseLoadableDetachableModel<T> model,
			FilterFunctorCallback callback) 
	{
		super(id, model, callback);
	}

	@Override
	public void initPanel()
	{
		IModel iModel = getTypeOfModule();
		
		if(iModel != null)
		{
			add(new Label("typeOfModule", iModel));
		}
		else
		{
			add(new WebMarkupContainer("typeOfModule").setVisible(false));
		}
		
		super.initPanel();
	}
	
	@Override
	public Study getStudy() 
	{
		return getConfiguration().getStudy();
	}

	@Override
	protected GeneralStatistics getGeneralStatistics(FilterFunctorCallback callback) 
	{
		return evaluateGeneralStatistics(callback);
	}

	@SuppressWarnings("unchecked")
	protected final T getConfiguration() 
	{
		return (T) getDefaultModelObject();
	}

	protected abstract IModel getTypeOfModule();
	
	protected abstract GeneralStatistics evaluateGeneralStatistics(FilterFunctorCallback callback);

}
