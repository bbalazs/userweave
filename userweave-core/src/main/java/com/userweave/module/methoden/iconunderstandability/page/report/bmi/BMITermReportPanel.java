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

import java.util.Locale;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.domain.service.GeneralStatistics;
import com.userweave.module.methoden.iconunderstandability.dao.ITMTestResultDao;
import com.userweave.module.methoden.iconunderstandability.domain.IconTermMatchingConfigurationEntity;
import com.userweave.module.methoden.iconunderstandability.domain.ItmTerm;
import com.userweave.module.methoden.iconunderstandability.domain.report.TermReport;
import com.userweave.module.methoden.iconunderstandability.service.ReportService;
import com.userweave.pages.configuration.report.FilterFunctorCallback;
import com.userweave.pages.configuration.report.FilteredModuleConfigurationReportPanel;
import com.userweave.presentation.model.EntityBaseLoadableDetachableModel;

/**
 * @author oma
 */
public class BMITermReportPanel extends FilteredModuleConfigurationReportPanel<IconTermMatchingConfigurationEntity>
{
	private static final long serialVersionUID = 1L;

	@SpringBean
	private ReportService reportService;
	
	@SpringBean
	private ITMTestResultDao resultDao;
	
	private final int termId;
	
	private final int termValueId;
	
	private final Locale studyLocale;
	
	public BMITermReportPanel(
			String id, 
			IconTermMatchingConfigurationEntity configuration, 
			ItmTerm term, 
			Locale studyLocale,
			FilterFunctorCallback callback) 
	{
		super(id, 
			  new EntityBaseLoadableDetachableModel<IconTermMatchingConfigurationEntity>(configuration),
			  callback);		
		
		this.termId =  term.getId();
		this.termValueId = term.getValue().getId();
		this.studyLocale = studyLocale;
		
		initPanel();
	}

	@Override
	protected Component createReportContainer(boolean showDetails) 
	{
		if(showDetails) 
		{
			IconTermMatchingConfigurationEntity configuration = getConfiguration();
			
			TermReport termReport = 
				reportService.getTermReport(
						configuration, 
						getFilterFuctorCallback().getFilterFunctor(), 
						termId);
		
			if (termReport == null) 
			{
				return new Label("reportPanel", new StringResourceModel("no_report_message", this, null));
			} 
			else 
			{
				return new TermReportPanel("reportPanel", termReport, configuration.getId(), studyLocale);
			}
		} 
		else 
		{
			return new WebComponent("reportPanel");
		} 
	}

	@Override
	protected GeneralStatistics evaluateGeneralStatistics(FilterFunctorCallback callback) 
	{
		return resultDao.findValidResultStatisticsForTerm(
			getConfiguration(), callback.getFilterFunctor(), termValueId);
	}

	@Override
	protected IModel getTypeOfModule()
	{
		return null;
	}
}

