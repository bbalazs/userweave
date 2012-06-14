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

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import com.userweave.application.UserWeaveSession;
import com.userweave.domain.Study;
import com.userweave.domain.StudyState;
import com.userweave.domain.service.GeneralStatistics;
import com.userweave.pages.report.GeneralStatisticsPanel;

/**
 * @author oma
 */
public abstract class FilteredReportPanel extends Panel 
{
	private static final long serialVersionUID = 1L;

	private GeneralStatisticsPanel statisticsPanel;

	private Component reportContainer;

	private final FilterFunctorCallback filterFunctorCallback;
	
	public FilteredReportPanel(String id, IModel model, FilterFunctorCallback filterFunctorCallback) {
		super(id, model);
		
		this.filterFunctorCallback = filterFunctorCallback;
	}

	protected boolean showDetails() {
		return StudyState.studyIsInState(getStudy().getState(), StudyState.FINISHED)|| 
				UserWeaveSession.get().isAdmin();
			//|| UserWeaveSession.get().originFromReport();
	}
	
	protected abstract Study getStudy();

	
	public void initPanel() 
	{
		add(statisticsPanel = new GeneralStatisticsPanel("statistics") 
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected GeneralStatistics getGeneralStatistics() {
				return FilteredReportPanel.this.getGeneralStatistics(filterFunctorCallback);
			}
		});
		
		statisticsPanel.setOutputMarkupId(true);
		
		replaceReportContainer(createReportContainer(showDetails()));
		
		if(reportContainer != null) {
			add(reportContainer);
		}
	}

	protected IModel getGeneralStatisticsModel() {
		return statisticsPanel.getDefaultModel();
	}

	protected abstract GeneralStatistics getGeneralStatistics(FilterFunctorCallback callback);


	protected FilterFunctorCallback getFilterFuctorCallback()
	{
		return filterFunctorCallback;
	}
	
	protected abstract Component createReportContainer(boolean showdetails);
	
	public void onFilter(AjaxRequestTarget target) 
	{
		Component replacement = createReportContainer(showDetails());
		if(replacement != null) {
			replaceReportContainer(replacement);
			target.addComponent(replacement);
		}	
		target.addComponent(statisticsPanel);
	}

	protected void replaceReportContainer(Component replacement) 
	{
		if(replacement == null) 
		{
			return;
		}
		
		replacement.setOutputMarkupId(true);
		
		if(reportContainer != null) 
		{
			if(reportContainer instanceof AjaxTabbedPanel &&
			   replacement instanceof AjaxTabbedPanel)
			{
				// keep selected tab of tabbed panel
				int selectedTab = ((AjaxTabbedPanel) reportContainer).getSelectedTab();
			
				((AjaxTabbedPanel) replacement).setSelectedTab(selectedTab);
			}
			
			reportContainer.replaceWith(replacement);
		} 
		else 
		{
			add(replacement);
		}
		
		this.reportContainer = replacement;
		
	}

}

