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

import java.util.List;
import java.util.Locale;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AbstractAjaxTimerBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.http.handler.RedirectRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;

import com.userweave.application.ModuleConfigurationContainer;
import com.userweave.application.UserWeaveSession;
import com.userweave.components.SerializedPageLink;
import com.userweave.components.customModalWindow.CustomModalWindow;
import com.userweave.dao.StudyDao;
import com.userweave.dao.SurveyExecutionDao;
import com.userweave.domain.Study;
import com.userweave.domain.StudyState;
import com.userweave.domain.SurveyExecution;
import com.userweave.domain.service.ModuleService;
import com.userweave.domain.service.StudyService;
import com.userweave.module.ModuleConfiguration;
import com.userweave.pages.test.error.finished.StudyFinishedPanel;
import com.userweave.pages.test.error.notavailable.StudyNotAvailablePanel;
import com.userweave.pages.test.error.notexisting.StudyNotExisitingPanel;
import com.userweave.pages.test.popups.registration.InformAboutIncompleteRegistrationPage;
import com.userweave.presentation.utils.WebClientHelper;
import com.userweave.utils.LocalizationUtils;

public class DisplaySurveyUI extends BasePageSurvey 
{
	private static final long serialVersionUID = 1L;

	@SpringBean 
	private ModuleService moduleService;
	
	@SpringBean
	private StudyDao studyDao;
	
	@SpringBean
	private SurveyExecutionDao surveyExecutionDao;
	
	@SpringBean
	private StudyService studyService;
	
	private ModuleConfigurationContainer moduleConfigurationContainer = null;
	
	private Component content;

	private final String CONTENT_ID =  "modulContainer";
	
	private String bgColor;
	
	private Boolean studyFontColor;
	
	private Locale selectedLocale;
	
	@Override
	public Locale getLocale()
	{
		return selectedLocale;
	}
	
	public DisplaySurveyUI(PageParameters parameters) 
	{
		String hashCode = getHashCode(parameters);
		Locale locale = getLocale(parameters);
		
		// set stying of the resulting layout
		if(getMobile(parameters))
		{
			UserWeaveSession.get().setStyle("mobile");
		}
		else
		{
			UserWeaveSession.get().setStyle("");
		}
		
		init(hashCode, locale, parameters);
	}

	/**
	 * Check, if one of the indexed parameters have the value 'mobile'.
	 * If so, return true.
	 *  
	 * @param parameters
	 * 		Page parameters.
	 * @return
	 * 		True, if indexed page parameters contains the keyword 'mobile'.
	 */
	public static boolean getMobile(PageParameters parameters)
	{
		for(int i = 0; i < parameters.getIndexedCount(); i++)
		{
			if(parameters.get(i).equals("mobile"))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public static Locale getLocale(PageParameters parameters) 
	{
		return LocalizationUtils.getSupportedStudyLocale(parameters.get(1).toString());
	}
	
	public static String getHashCode(PageParameters parameters) 
	{
		int key = 0;
		String hashCode = parameters.get(key).toOptionalString();
		
		// support old school links like ./hashcode/<code>
		if(hashCode != null && "hashcode".equals(hashCode)) 
		{
			key++;
			hashCode = parameters.get(key).toString();
		}
		
		boolean test = "".equals(hashCode.trim());
		
		if(hashCode == null || test) 
		{
			return null;
		}
		
		return hashCode;
	} 
	
	
	private Study getStudy(String hashCode) 
	{
		if(hashCode == null) {
			return null;
		}
		return studyDao.findByHashcode(hashCode);		
	}

	
	@Override
	protected boolean progressCounterIsVisible() {
		return moduleConfigurationContainer != null;
	}
	
	@Override
	public void renderHead(IHeaderResponse response)
	{
		super.renderHead(response);
		setColorCss(response);
	}
	
	private void init(final String hashCode, final Locale locale, PageParameters parameters) 
	{
		
		Study study = getStudy(hashCode);
		
		SurveyExecution surveyExecution = studyService.createSurveyExecution(study, hashCode);

		addModalWindow(surveyExecution);
		
		if(surveyExecution == null) 
		{	
			addErrorComponentAndDisplayCounter(new StudyNotExisitingPanel(CONTENT_ID));
			return;
		}
		
		study = surveyExecution.getStudy();
		
		
		// now study != null
		setBgColor(study);
		
		// store page parameters and call to this page
		UserWeaveSession.get().setSessionOrigin(new SerializedPageLink(getClass(), parameters, SerializedPageLink.Type.SURVEY));

		surveyExecution.setRemoteAddr(new WebClientHelper().getRemoteAddress());

		setLogo(study.getLogo());
		
		final int surveyExecutionId = surveyExecution.getId();
		
		/*
		 * Determine locale as follows:
		 * 	1. given locale in url, if supported
		 *  2. selected locale from loacale choice panel
		 * 	3. browser locale, if supported
		 * 	4. study default locale
		 * 	5. default locale (en)
		 */
		
		if(locale != null && study.getSupportedLocales().contains(locale)) 
		{
			selectedLocale = locale;
			
			// also store locale in surveyExecution
			surveyExecution.setLocale(LocalizationUtils.getRegistrationLocaleShort(locale));
			
			surveyExecutionDao.save(surveyExecution);

		} 
		else 
		{	
			boolean displayLocaleSelection = 
			   StudyState.studyIsInState(study.getState(),StudyState.INIT) && 
			   study.isMultiLingual() && 
			   !study.isDeleted();
			
			boolean displayInRunningOnlyIfSelected = 
				StudyState.studyIsInState(study.getState(),StudyState.RUNNING) && 
				study.isMultiLingual() && 
				!study.isDeleted() && 
				study.isLocaleSelectable();
			
			// display a locale selection dialog before the study survey
			// to select a prefered locale
			if(displayLocaleSelection || displayInRunningOnlyIfSelected)
			{
				setCountModel(new Model<Integer>(0));	
				setPercentCountModel(new Model<Integer>(0));
				
				content = new SelectLocalePanel(CONTENT_ID, study.getSupportedLocales()) 
				{
					private static final long serialVersionUID = 1L;

					@Override
					protected void onLocaleSelected(Locale selectedLocale) 
					{
						if(selectedLocale != null) 
						{
							DisplaySurveyUI.this.selectedLocale = selectedLocale;
							
							SurveyExecution surveyExecution = surveyExecutionDao.findById(surveyExecutionId);
						
							surveyExecution.setLocale(LocalizationUtils.getRegistrationLocaleShort(selectedLocale));
						
							surveyExecutionDao.save(surveyExecution);
						
							replaceSurveyExecutionComponentAndSetHeadline(surveyExecution.getStudy(), surveyExecution);
						}
					}

					@Override
					protected String getColorForNextButton() 
					{
						return computeBrightnessOfBackgroundAndSetColorValue();
					}				
				};
				
				add(content);
				
				return;
			}
			else 
			{
				// Browser locale is stored in the session
				if(study.supportsLocale(LocalizationUtils.mapLocale(getSession().getLocale())))
				{
					this.selectedLocale = LocalizationUtils.mapLocale(getSession().getLocale());
				}
				
				// study default locale
				else if (study.getLocale() != null) 
				{
					this.selectedLocale = study.getLocale();
				} 
				// default locale (en)
				else 
				{
					this.selectedLocale = LocalizationUtils.getDefaultLocale();
				}	
				
				surveyExecution.setLocale(LocalizationUtils.getRegistrationLocaleShort(selectedLocale));
			
				surveyExecutionDao.save(surveyExecution);
			}
		}
		
		replaceSurveyExecutionComponentAndSetHeadline(study, surveyExecution);
	}
	

	private void addModalWindow(SurveyExecution surveyExecution) {
		String ID = "modalWindow";
		if(surveyExecution == null || 
		   evaluateError(surveyExecution) != ErrorType.NONE || 
		   studyService.isAtLeastOneAdminRegistered(surveyExecution.getStudy().getParentProject())) 
		{
			add(new WebMarkupContainer(ID));
		} 
		else 
		{
			final ModalWindow modalWindow = new CustomModalWindow(ID);
			modalWindow.setInitialHeight(379);
			add(modalWindow);
			
			modalWindow.add(new AbstractAjaxTimerBehavior(Duration.seconds(1)) 
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onTimer(AjaxRequestTarget target) {
					this.stop();
					modalWindow.show(target);					
				}
			});
				
			modalWindow.setPageCreator(new ModalWindow.PageCreator() 
			{
				private static final long serialVersionUID = 1L;

				@Override
				public Page createPage() {
					return new InformAboutIncompleteRegistrationPage(modalWindow);
				}
			});
		}
		
	}
	
	private void replaceSurveyExecutionComponentAndSetHeadline(Study study, SurveyExecution surveyExecution) {
		setHeadlineAndDescription(
			study.getHeadline() != null    ? LocalizationUtils.getValue(study.getHeadline(), selectedLocale)    : "", 
			study.getDescription() != null ? LocalizationUtils.getValue(study.getDescription(), selectedLocale) : ""
		);

		Component replacement = createSurveyExecutionComponent(study, surveyExecution);
		
		if (content != null) {
			content.replaceWith(replacement);
		} 
		content = replacement;
		add(content);
	}
	
	private Component createSurveyExecutionComponent(Study study, SurveyExecution surveyExecution ) 
	{
		Component errorComponent = 
			createErrorComponent(study, surveyExecution,CONTENT_ID);
		
		if (errorComponent != null)
		{
			setCountModel(new Model<Integer>(0));
			setPercentCountModel(new Model<Integer>(0));
			return errorComponent;
		}
		else
		{
			// surveyExecution.setExecutionStarted(new Date());
			surveyExecutionDao.save(surveyExecution);

			final List<ModuleConfiguration> moduleConfigurations = study
					.getActiveModuleConfigurations(moduleService.getModules());

			moduleConfigurationContainer = new ModuleConfigurationContainer(
					CONTENT_ID, 
					this, 
					moduleConfigurations, 
					surveyExecution.getId().intValue(),
					computeBrightnessOfBackgroundAndSetColorValue(),
					selectedLocale);

			moduleConfigurationContainer.setOutputMarkupId(true);

			return moduleConfigurationContainer;
		}
	}

	private void setBgColor(Study study)
	{
		bgColor = study.getBackgroundColor() == null ? Study.DEFAULT_COLOR: study.getBackgroundColor();
		
		studyFontColor = study.getFontColor();
	}
	
	private void setColorCss(IHeaderResponse response) 
	{
		final String color = computeBrightnessOfBackgroundAndSetColorValue();
		
		String css =" .um_background { background-color: "+bgColor+";}" +
					" .um_background_font_color { color:" + color + "; }" +
					" .um_background_border { border-color : "+bgColor+"; }" + 
					" .um_background_color { color:" + bgColor + " }";
		
		response.renderCSS(css, null);
	}

	/**
	 * Compute brightness of given background color
	 * and returns matching font color.
	 * 
	 * Uses the HSV color model.
	 * 
	 * @return
	 */
	private String computeBrightnessOfBackgroundAndSetColorValue() 
	{	
		if(studyFontColor != null && !studyFontColor)
			return "black";
		else
			return "white";
	}
	
	@Override
	protected String getColorForModalWindow()
	{
		return computeBrightnessOfBackgroundAndSetColorValue();
	}
	
	private Component createErrorComponent(Study study, SurveyExecution surveyExecution, String CONTENT_ID) {
		ErrorType errorType = evaluateError(study, surveyExecution);
		
		switch (errorType) {
		case DELETED:
		case DEACTIVATED:
		case ALREADY_STARTED:
		case NOT_FOUND:
			return new StudyNotAvailablePanel(CONTENT_ID);
		case FINISHED:
			String notAvailableUrl = study.getNotAvailableUrl();
			if (notAvailableUrl != null) 
			{
				getRequestCycle().scheduleRequestHandlerAfterCurrent(
					new RedirectRequestHandler((notAvailableUrl)));
			}
			return new StudyNotAvailablePanel(CONTENT_ID);
		case COMPLETED:
			return new StudyFinishedPanel(CONTENT_ID);
		default:
			break;
		}
		return null;
	}
	
	private enum ErrorType { NONE, NOT_FOUND, DELETED, DEACTIVATED, FINISHED, COMPLETED, ALREADY_STARTED }
	
	private ErrorType evaluateError(SurveyExecution surveyExecution) {
		if(surveyExecution == null) {
			return ErrorType.NOT_FOUND;
		}
		return evaluateError(surveyExecution.getStudy(), surveyExecution);
	}
	
	private ErrorType evaluateError(Study study, SurveyExecution surveyExecution) {
		if(study == null) {
			return ErrorType.NOT_FOUND;
		}
		
		if(study.isDeleted()) {
			return ErrorType.DELETED;
		}
		
		if(study.isDeactivated())
		{
			return ErrorType.DEACTIVATED;
		}
		
		if(study.getState() == StudyState.FINISHED) {// && 
		   //surveyExecution.getState() != SurveyExecutionState.INVALID){
			return ErrorType.FINISHED;
		}
		
		if(surveyExecution == null) {
			return ErrorType.NOT_FOUND;
		}
		
		if(surveyExecution.isCompleted()) {
			return ErrorType.COMPLETED;
		}
		
		if(! surveyExecution.executedWhileStudyInRunningState()) { 

			if(surveyExecution.getExecutionStarted() != null) {
				return ErrorType.ALREADY_STARTED;
			}
		}
		
		return ErrorType.NONE;		
		
	}

	private void addErrorComponentAndDisplayCounter(Component errorComponent) {		
		setCountModel(new Model<Integer>(0));			
		setPercentCountModel(new Model<Integer>(0));		
		add(errorComponent);
	}

	@Override
	protected void pageLoaded(long startTime) {

		// if study is not found the moduleConfigurationContainer is empty
		if(moduleConfigurationContainer != null) {
			moduleConfigurationContainer.setStartTime(startTime);
		}
	}

	@Override
	protected String getCssClassForColorComponents() 
	{
		return computeBrightnessOfBackgroundAndSetColorValue();
	}
	
	@Override
	protected Component getHelpPanel(String id)
	{
		if(moduleConfigurationContainer != null)
		{
			return moduleConfigurationContainer.getHelpTextPanel(id);
		}
		else
		{
			return new WebMarkupContainer(id);
		}
	}
}
