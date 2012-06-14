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

import java.util.Locale;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.domain.StudyState;
import com.userweave.domain.service.GeneralStatistics;
import com.userweave.module.methoden.questionnaire.dao.QuestionnaireResultDao;
import com.userweave.module.methoden.questionnaire.domain.QuestionnaireConfigurationEntity;
import com.userweave.module.methoden.questionnaire.domain.question.Question;
import com.userweave.module.methoden.questionnaire.page.QuestionConfigurationPanelFactory;
import com.userweave.pages.configuration.report.FilterFunctorCallback;
import com.userweave.pages.configuration.report.FilteredModuleConfigurationReportPanel;
import com.userweave.presentation.model.EntityBaseLoadableDetachableModel;

/**
 * @author oma
 */
@Deprecated
public class QuestionnaireReportUI extends FilteredModuleConfigurationReportPanel<QuestionnaireConfigurationEntity> {

	@SpringBean
	private QuestionnaireResultDao resultDao;
	
	private final QuestionConfigurationPanelFactory reportFactory;

	private final Locale locale;
	
	public QuestionnaireReportUI(String id, final int configurationId, FilterFunctorCallback callback) {
		super(
			id,
			new EntityBaseLoadableDetachableModel<QuestionnaireConfigurationEntity>(QuestionnaireConfigurationEntity.class, configurationId),
			callback);
		
		reportFactory = new QuestionConfigurationPanelFactory();
		
		locale = getConfiguration().getStudy().getLocale();
		
		initPanel();
	}
	
	@Override
	protected boolean showDetails() 
	{
		return getStudy().getState() == StudyState.RUNNING ? false : true;
	}
	

	@Override
	protected Component createReportContainer(boolean showDetails) {
		
		if(! showDetails)
		{
			return new WebMarkupContainer("reportsContainer")
				.add(new WebMarkupContainer("reports"))
					.add(new WebMarkupContainer("report"));
		}
		
		
		return new WebMarkupContainer("reportsContainer")
			.add(
				new ListView("reports", new PropertyModel(QuestionnaireReportUI.this.getDefaultModel(),"questions")) {
					@Override
					protected void populateItem(ListItem item) {
						Component report = 
							reportFactory.getReportComponent(
									"report", 
									locale, 
									(Question) item.getModelObject(), 
									getFilterFuctorCallback());					
						if (report != null) {
							item.add(report);
						} else {
							item.add(new Label("report", "Kein Report vorhanden"));
						}
					}
				}
			);
	}

	@Override
	protected GeneralStatistics evaluateGeneralStatistics(FilterFunctorCallback callback) {
		return resultDao.findValidResultStatistics(getConfiguration(), callback.getFilterFunctor());
	}
	
	@Override
	protected IModel getTypeOfModule()
	{
		return null;
	}

}

