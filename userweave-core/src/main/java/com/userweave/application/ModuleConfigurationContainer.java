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
package com.userweave.application;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.request.http.handler.RedirectRequestHandler;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.dao.SurveyExecutionDao;
import com.userweave.domain.LocalizedString;
import com.userweave.domain.Study;
import com.userweave.domain.SurveyExecution;
import com.userweave.domain.SurveyExecutionState;
import com.userweave.module.ModuleConfiguration;
import com.userweave.module.methoden.iconunderstandability.page.survey.Timer;
import com.userweave.pages.CounterDisplay;
import com.userweave.pages.test.ITestUI;
import com.userweave.pages.test.SurveyUI;
import com.userweave.utils.LocalizationUtils;

@SuppressWarnings("serial")
public class ModuleConfigurationContainer extends Panel {

	private int configurationIndex = 0;
	
	private final List<ModuleConfiguration> configurations;
	
	private Component testUI;

	private static final String idTestUI = "modul";
	
	private final int surveyExecutionId;

	private final CounterDisplay counterDisplay;
	
	private final String nextButtonColor;
	
	private final Locale selectedLocale;
	
	@Override
	public Locale getLocale()
	{
		return selectedLocale;
	}
	
	@SpringBean 
	private SurveyExecutionDao surveyExecutionDao;
	
	public ModuleConfigurationContainer(String id, 
			CounterDisplay counterDisplay, List<ModuleConfiguration> configurations, 
			int surveyExecutionId, String nextButtonColor, Locale locale)
	{
		super(id);
		this.selectedLocale = locale;
		this.configurations = configurations;
		this.surveyExecutionId = surveyExecutionId;
		this.counterDisplay = counterDisplay;
		this.nextButtonColor = nextButtonColor;
		init();		
		setCounterDisplayModels();
	}

	protected SurveyExecution getSurveyExecution() {
		return surveyExecutionDao.findById(surveyExecutionId);
	}
	
	
	public String getActiveConfigurationName() {
		ModuleConfiguration activeConfiguration = getActiveConfiguration();
		if (activeConfiguration != null) {
			return activeConfiguration.getName();
		} else {
			return "Keine Konfiguration verfügbar";
		}
	}
	
	public LocalizedString getActiveConfigurationDescription() {
		ModuleConfiguration activeConfiguration = getActiveConfiguration();
		if (activeConfiguration != null) {
			return activeConfiguration.getDescription();
		} else {
			return new LocalizedString();
		}
	}
	
	public ModuleConfiguration getActiveConfiguration() {
		if (configurationIndex >= configurations.size()) {
			return null;
		} else {
			return configurations.get(configurationIndex);
		}
	}
	
	public void setCounterDisplayModels() {
		
		if (counterDisplay != null) {
			
			counterDisplay.setModuleConfigurationDescriptionModel( 
				new AbstractReadOnlyModel() {

					@Override
					public Object getObject() {
						LocalizedString activeConfigurationDescription = getActiveConfigurationDescription();
						if (activeConfigurationDescription != null) {
							return LocalizationUtils.getValue(activeConfigurationDescription, getLocale());
						} else {
							return null;
						}
					}					
				}			
			);			
			
			counterDisplay.setCountModel(
				new AbstractReadOnlyModel() {
	
					@Override
					public Object getObject() {
						if (configurationAvailable()) {
							return new Integer(configurationIndex+1);
						} else {
							return new Integer(0);
						}
					}
				}
			);
			
			counterDisplay.setMaxCountModel(
				new AbstractReadOnlyModel() {
	
					@Override
					public Object getObject() {
						return new Integer(configurations.size());
					}
				}	
			);
			
			counterDisplay.setPercentCountModel(
				new AbstractReadOnlyModel() {
		
					@Override
					public Object getObject() {
							
						float percent = 
							(float)(configurationIndex+1) / 
							(float) configurations.size() * 
							100;
								
						return new String((int)percent + " %");
					}
				}	
			);
		}
	}
	
	private void init() {
		testUI = getUI(configurationIndex);
		add(testUI);
	}

	private boolean configurationAvailable() {
		return getActiveConfiguration() != null;
	}
	
	private Component getUI(int configurationIndex ) {

		Component ui = null;
		if (!configurationAvailable()) {
			ui = getNoConfigurationAvailableUI();
		} else {
			SurveyUI testUI = getActiveConfigurationTestUI(configurationIndex, surveyExecutionId, 
				new OnFinishCallback() {
					public void onFinish() {
						next();
					}
				}
			);	
			if (testUI != null) {			
				testUI.setColorNextButton(nextButtonColor);
				ui = testUI;			
			} else {
				ui = new Label(idTestUI, 
						"getTestUI on module " +
						configurations.get(configurationIndex).getModule().getModuleId() +
						" delivered a null Component. You want to correct this :-)");				
			}
		}
		ui.setOutputMarkupId(true);
		return ui;
	}

	public void setStartTime(long startTime) {
		if (testUI instanceof  ITestUI) {
			((ITestUI) testUI).startResult(startTime);
		}
	}
	
	private ModuleConfigurationContainer next() {
		if (configurationLeft()) {
			setSurveyExecutionModulesExecuted(configurationIndex+1);
			if(!configurationsLeftStoresResults()) { 
				setSurveyExecutionEndTimeNow();
			}
			displayNextTestUI();
		} else {			
			setSurveyExecutionEndTimeNow();		
			displayFinishedPage();
		}
		return this;
	}

	private void setSurveyExecutionModulesExecuted(int executed) {
		SurveyExecution surveyExecution = getSurveyExecution();
		surveyExecution.setModulesExecuted(executed);
		surveyExecutionDao.save(surveyExecution);
		
	}

	private void displayFinishedPage() {
		testUI.replaceWith(getFinishedUI());	
		String finishedPageUrl = getActiveConfiguration().getStudy().getFinishedPageUrl();
		if (finishedPageUrl != null) 
		{
			getRequestCycle().scheduleRequestHandlerAfterCurrent(
					new RedirectRequestHandler(finishedPageUrl));
		}
		else
			getRequestCycle().scheduleRequestHandlerAfterCurrent(
					new RedirectRequestHandler(Study.DEFAULT_FINISHED_URL));
	}

	private void displayNextTestUI() {
		Component nextTestUI = getUI(++configurationIndex);			
		testUI.replaceWith(nextTestUI);
		testUI = nextTestUI;
	}

	private boolean configurationLeft() {
		return configurationIndex < configurations.size() - 1;
	}

	private boolean configurationsLeftStoresResults() {
		for(int idx = configurationIndex+1; idx < configurations.size(); idx++) {
			if(configurations.get(idx).getModule().storesResults()) {
				return true;
			}
		}
		return false;		
	}
	
	private void setSurveyExecutionEndTimeNow() {
		SurveyExecution surveyExecution = getSurveyExecution();
		surveyExecution.setExecutionFinished(new Date(new Timer().getCurrentTime()));
		if(surveyExecution.executedWhileStudyInRunningState()) {
			surveyExecution.setState(SurveyExecutionState.COMPLETED);
		}
		surveyExecutionDao.save(surveyExecution);
	}

	private SurveyUI getActiveConfigurationTestUI(int configurationIndex, int surveyExecutionId, OnFinishCallback onFinishCallback) {
		return getActiveConfiguration().getModule().getTestUI(idTestUI, surveyExecutionId, getActiveConfiguration(), onFinishCallback, selectedLocale);		
	}

	private Label getNoConfigurationAvailableUI() {
		return new Label(idTestUI, "no active configurations available");
	}

	private Component getFinishedUI() {
		return new Label(idTestUI, "finished");
	}
	
	public Component getHelpTextPanel(String id)
	{
		if(testUI instanceof SurveyUI)
		{
			return ((SurveyUI)testUI).getHelpTextPanel(id);
		}
		else
		{
			return new WebMarkupContainer(id);
		}
	}
}
