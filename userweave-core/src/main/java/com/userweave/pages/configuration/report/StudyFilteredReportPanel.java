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

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.components.callback.EventHandler;
import com.userweave.dao.SurveyExecutionDao;
import com.userweave.domain.Study;
import com.userweave.domain.SurveyExecution;
import com.userweave.domain.service.GeneralStatistics;
import com.userweave.domain.service.SurveyStatisticsService;
import com.userweave.pages.components.customizedtabpanel.SpecialAjaxTabbedPanel;

public class StudyFilteredReportPanel extends FilteredReportPanel
{
	private static final long serialVersionUID = 1L;

	@SpringBean
	private SurveyStatisticsService surveyStatisticsService;
		
	@SpringBean
	private SurveyExecutionDao surveyExecutionDao;
	
	private final EventHandler callback, addFilterCallback;
	
	/**
	 * Default constructor.
	 * 
	 * @param id
	 * 		Markup id of component.
	 * @param studyModel
	 * 		Model of study to evaluate.
	 * @param callback
	 * 		Callback to fire on event.
	 * @param addFilterCallback
	 * 		Callback to fire after a new filter has been created.
	 * @param filterFunctorCallback
	 * 		Filter functor to specify results.
	 */
	public StudyFilteredReportPanel(
			String id, 
			IModel studyModel, 
			EventHandler callback,
			EventHandler addFilterCallback,
			FilterFunctorCallback filterFunctorCallback) 
	{
		super(id, studyModel, filterFunctorCallback);
		
		this.callback = callback;
		this.addFilterCallback = addFilterCallback;
		
		initPanel();
	}
	
	@Override
	protected Study getStudy() {
		return (Study) getDefaultModelObject();
	}

	@Override
	protected Component createReportContainer(boolean showdetails) 
	{
		List<ITab> tabs = new ArrayList<ITab>(2);
		
		tabs.add(
			new AbstractTab(
					new StringResourceModel(
							"course", 
							StudyFilteredReportPanel.this, 
							null))
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Panel getPanel(String panelId)
			{
				return new ModuleReportListPanel(
				panelId, 
				getDefaultModel(), 
				getGeneralStatisticsModel(), 
				callback, 
				addFilterCallback,
				getFilterFuctorCallback());
			}
		});
		
		tabs.add(
			new AbstractTab(
					new StringResourceModel(
							"locale", 
							StudyFilteredReportPanel.this, 
							null))
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Panel getPanel(String panelId)
			{	
				return new StudyLocaleReportPanel(
					panelId, 
					getDefaultModel(), 
					addFilterCallback,
					getFilterFuctorCallback());
			}
		});
		
		return new SpecialAjaxTabbedPanel("study", tabs);
	}

	@Override
	protected GeneralStatistics getGeneralStatistics(FilterFunctorCallback callback) 
	{
		List<SurveyExecution> surveyExecutions = 
			surveyExecutionDao.findByStudy(getStudy(), callback.getFilterFunctor());
		
		return surveyStatisticsService
				.evaluateSurveyExecutionStatistics(surveyExecutions, true);
	}

}
