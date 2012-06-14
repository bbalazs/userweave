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
package com.userweave.pages.test;

import java.util.Date;
import java.util.Locale;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.application.OnFinishCallback;
import com.userweave.dao.StudyDependendDao;
import com.userweave.dao.SurveyExecutionDao;
import com.userweave.domain.ModuleConfigurationEntityBase;
import com.userweave.domain.SurveyExecution;
import com.userweave.domain.SurveyExecutionState;
import com.userweave.domain.TestResultEntityBase;
import com.userweave.module.methoden.iconunderstandability.page.survey.Timer;
import com.userweave.pages.test.helptext.HelpTextPanel;
import com.userweave.pages.test.jquery.JQuery;
/**
 * Base-Class for Test-UserInterface. <br/>
 * Has configuration as Compound-Property-Model. <br/>
 * <br/>
 * provides: <br/>
 * 	* getter for Configuration and SurverExecution <br/>
 * 	* timer for singleStep and whole Survey-Execution <br/>
 *  * callback to be called when survey has finished 
 * @author oma
 *
 * @param <T> Configuration Class
 */

public abstract class SurveyUI<T extends ModuleConfigurationEntityBase<?>> 
	extends Panel 
	implements ITestUI 
{
	private static final long serialVersionUID = 1L;

	private String nextButtonColor = "white";
	
	private Locale selectedLocale;
	
	@Override
	public Locale getLocale()
	{
		return selectedLocale;
	}
	
	public SurveyUI(
			String id, 
			final int configurationId, 
			int surveyExecutionId, 
			OnFinishCallback onFinishCallback,
			Locale locale) 
	{
		super(id);
		
		this.selectedLocale = locale;
		this.surveyExecutionId = surveyExecutionId;
		this.onFinishCallback = onFinishCallback;
		
		surveyStepTimer = new Timer();
		
		setDefaultModel( 
			new CompoundPropertyModel<T>(
				new LoadableDetachableModel<T>() 
				{
					private static final long serialVersionUID = 1L;

					@Override
					protected T load() 
					{
						return getConfigurationDao().findById(configurationId); 
					}
				}
			)
		);
		
	}

	/** 
	 * Get component for helptext-display, defaults to HelpTextPanel. 
	 * Can be averridden in subclass to provide custom helptext-component.
	 * @param id
	 * @return
	 */
	protected Component getHelpText(String id) {
		return new HelpTextPanel(id, new StringResourceModel("helptext", this, null));
	}
	
	public Component getHelpTextPanel(String id)
	{
		return getHelpText(id);
	}
	
	/**
	 * Get Configuration for this survey.
	 */
	@SuppressWarnings("unchecked")
	protected T getConfiguration() {
		return (T) getDefaultModelObject();
	}
	
	@SpringBean
	private SurveyExecutionDao surveyExecutionDao;
	
	private final int surveyExecutionId;
	
	/**
	 * Get current survey execution.
	 * @return
	 */
	protected SurveyExecution getSurveyExecution() {
		return surveyExecutionDao.findById(surveyExecutionId);
	}
	
	protected abstract StudyDependendDao<T> getConfigurationDao();
	
	// timer for single step
	private final Timer surveyStepTimer;	
	
	/**
	 * Get timer for current step of configuration.
	 * @return
	 */
	protected Timer getSurveyStepTimer() {
		return surveyStepTimer;
	}

	@Override
	public void startResult(long time) {
		
		SurveyExecution surveyExecution = getSurveyExecution();
		if (surveyExecution != null && !surveyExecution.executionStartedIsSet()) {
			
			surveyExecution.setExecutionStarted(new Date(time));
			if(surveyExecution.executedWhileStudyInRunningState()) {
				surveyExecution.setState(SurveyExecutionState.STARTED);
			}
			surveyExecutionDao.save(surveyExecution);
		}
		
		getSurveyStepTimer().setStartTime(time);
		
		createOrLoadResult();
	}

	protected abstract TestResultEntityBase<T> createOrLoadResult();
	
	protected abstract void finishResult(TestResultEntityBase<T> aResult);

	private final OnFinishCallback onFinishCallback;

	// calling this method will finish the test of the given configuration.
	protected void finish() {
		finishResult();
		onFinishCallback.onFinish();
	}
	
	protected boolean saveScopeToResult(TestResultEntityBase<T> result) {
		boolean changed = false;
		if(result.getConfiguration() == null) {
			result.setConfiguration(getConfiguration());
			changed = true;
		}
		
		if(result.getSurveyExecution() == null) {
			result.setSurveyExecution(getSurveyExecution());
			changed = true;
		}
		
		if(result.getExecutionStarted() == null && getSurveyStepTimer().startTimeIsSet()) {
			result.setExecutionStarted(new Date(getSurveyStepTimer().getStartTime()));
			changed = true;
		} 	
		
		return changed;
	}

	public final void finishResult() {
		TestResultEntityBase<T> result = createOrLoadResult();
		
		Date executionFinished = new Date(getSurveyStepTimer().getCurrentTime());
		result.setExecutionFinished(executionFinished);
		
		Date executionStarted = result.getExecutionStarted();
		if(executionStarted == null) {
			// fallback if values are wrong
			result.setExecutionStarted(executionFinished);
			result.setExecutionTime(0L);
		} else {
			result.setExecutionTime(executionFinished.getTime()-executionStarted.getTime());
		}
		
		finishResult(result);
	}
	
	protected String getColorNextButton()
	{
		return nextButtonColor;
	}
	
	public void setColorNextButton(String color)
	{
		nextButtonColor = color;
	}
	
	@Override
	public void renderHead(IHeaderResponse response)
	{
		super.renderHead(response);
		
		JQuery.addSurveyUiJavaScript(getPage(), response);
	}
	
	
}
