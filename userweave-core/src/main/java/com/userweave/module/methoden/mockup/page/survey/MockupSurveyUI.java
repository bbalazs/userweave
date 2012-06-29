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
package com.userweave.module.methoden.mockup.page.survey;

import java.util.Locale;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.application.OnFinishCallback;
import com.userweave.components.model.LocalizedPropertyModel;
import com.userweave.dao.StudyDependendDao;
import com.userweave.domain.TestResultEntityBase;
import com.userweave.module.methoden.mockup.dao.MockupConfigurationDao;
import com.userweave.module.methoden.mockup.dao.MockupResultDao;
import com.userweave.module.methoden.mockup.domain.MockupConfigurationEntity;
import com.userweave.module.methoden.mockup.domain.MockupResult;
import com.userweave.module.methoden.mockup.domain.MockupConfigurationEntity.SwitchToNextConfigType;
import com.userweave.pages.test.NextButtonForMockupModule;
import com.userweave.pages.test.jquery.JQuery;
import com.userweave.pages.test.singleSurveyTestUI.SingleFormSurveyUI;
import com.userweave.utils.LocalizationUtils;

@SuppressWarnings("serial")
public class MockupSurveyUI extends SingleFormSurveyUI<MockupConfigurationEntity> {

	@SpringBean
	private MockupConfigurationDao dao;
	
	@SpringBean
	private MockupResultDao freeTextResultDao;
	
	public MockupSurveyUI(
		String id, 
		MockupConfigurationEntity configuration, 
		int surveyExecutionId, 
		OnFinishCallback onFinishCallback,
		Locale locale) 
	{
		super(id, configuration.getId(), surveyExecutionId , onFinishCallback, locale);		
		
		add(new WebComponent("URL").add(new AttributeModifier(
			"src", new Model<String>(LocalizationUtils.getValue(
					getConfiguration().getLocaleUrl(), getLocale())))));

		/*
		 * in this case, we have to change visibility of timeVisivle via display:none
		 * because for the javascript-function, who implements the countdown, it's mandantory 
		 * that the time value is on the site
		 */
		WebMarkupContainer timeVisible = new WebMarkupContainer("timeVisible");
		timeVisible.add(new Label("time"));
		if (getConfiguration().getSwitchToNextConfigType() == SwitchToNextConfigType.BUTTON
				|| !getConfiguration().isTimeVisible())
		{
			timeVisible.add(new AttributeModifier("style", new Model<String>(
					"display:none;")));
		}
		else if (getConfiguration().getSwitchToNextConfigType() == SwitchToNextConfigType.TIMER)
		{
			timeVisible.add(new AttributeModifier("style", new Model<String>(
					"margin-right:0px;")));
		}

		add(timeVisible);

		if (getConfiguration().getSwitchToNextConfigType() != SwitchToNextConfigType.BUTTON) 
		{
			add(new AbstractDefaultAjaxBehavior() {
				boolean isEnabled = true;
				
				@Override
				protected void respond(AjaxRequestTarget target) {
					isEnabled = false;
					
					target.appendJavaScript("$(document).ready(startTimer());");
				}
	
				@Override
				public void renderHead(Component component,
						IHeaderResponse response)
				{
					super.renderHead(component, response);
					response.renderOnDomReadyJavaScript(getCallbackScript().toString());
				}
	
				@Override
				public boolean isEnabled(Component component)	{
					return isEnabled;
				}
			});
		}
		
		
		

		add(new Label("freetext", new LocalizedPropertyModel(getDefaultModel(), "freetext", getLocale())).setEscapeModelStrings(false));
		
		WebMarkupContainer layer = new WebMarkupContainer("layer"){
			@Override
			public boolean isVisible() {
				return getConfiguration().isLayerVisible();
			}
		};
		add(layer);
	}
	
	
	
	@Override
	protected StudyDependendDao<MockupConfigurationEntity> getConfigurationDao() {
		return dao;
	}

	@Override
	protected void onSubmit() {
		finish();		
	}

	@Override
	protected MockupResult createOrLoadResult() {
		MockupResult result = freeTextResultDao.findByConfigurationAndSurveyExecution(getConfiguration(), getSurveyExecution());
		if(result == null) {
			result = new MockupResult();
		}
		
		if(saveScopeToResult(result)) {
			freeTextResultDao.save(result);
		}
		
		return result;
	}

	@Override
	protected void finishResult(TestResultEntityBase<MockupConfigurationEntity> aResult) {
		MockupResult result = (MockupResult) aResult;

		freeTextResultDao.save(result);		
	}



	@Override
	protected Component getNextButton() 
	{
		return new NextButtonForMockupModule("nextButton")
		{
			@Override
			public boolean isVisible() {
				return (getConfiguration().getSwitchToNextConfigType() != SwitchToNextConfigType.TIMER);
			}
		};
	}
	
	@Override
	public void renderHead(IHeaderResponse response)
	{
		super.renderHead(response);
		
		response.renderJavaScriptReference(
				new JavaScriptResourceReference(MockupSurveyUI.class, "jquery.debug.js"));
		
		response.renderJavaScriptReference(
			new JavaScriptResourceReference(MockupSurveyUI.class, "mockup.js"));
		
		if(JQuery.isIe6(getPage()))
		{
			String skript = "$(document).ready(function() { " +
							"$(\".surveyForm\").prependTo(\"body\"); });";
			
			response.renderJavaScript(skript, null);
		}
	}
}
