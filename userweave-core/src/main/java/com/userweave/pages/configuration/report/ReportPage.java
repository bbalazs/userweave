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

import java.util.Locale;

import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.application.UserWeaveSession;
import com.userweave.components.SerializedPageLink;
import com.userweave.dao.StudyDao;
import com.userweave.domain.Study;
import com.userweave.domain.StudyState;
import com.userweave.domain.util.StudyPaymentInspector;
import com.userweave.pages.base.BaseUserWeavePage;
import com.userweave.pages.test.DisplaySurveyUI;
import com.userweave.pages.test.error.notexisting.StudyNotExisitingPanel;

@SuppressWarnings("serial")
public class ReportPage extends BaseUserWeavePage {

	@SpringBean
	private StudyDao studyDao;

	public ReportPage(PageParameters parameters) {
		String hashCode = DisplaySurveyUI.getHashCode(parameters);
		Locale locale = DisplaySurveyUI.getLocale(parameters);
		init(getStudy(hashCode), locale, parameters);
	}

	private Study getStudy(String hashCode) {
		if(hashCode == null) {
			return null;
		}
		return studyDao.findByReportCode(hashCode);
	}

	private void init (final Study study, Locale locale, PageParameters parameters) {
 		if(study == null || study.getState() == StudyState.INIT || study.getOwner().isDeactivated()) { 
 			add(new StudyNotExisitingPanel("study_does_not_exist"));
 			add(new WebComponent("content"));
 			add(new Label("paymentReminder", new StringResourceModel("report_no_payment_message", this, null) ) {
 				@Override
 				public boolean isVisible() {
 					boolean isVisible = false; 					
 					return isVisible;
 				}
 			});
 		} else {
 			// store page parameters and call to this page
 			UserWeaveSession.get().setSessionOrigin(new SerializedPageLink(getClass(), parameters, SerializedPageLink.Type.REPORT));
			
 			if(locale != null) { // && study.getSupportedLocales().contains(locale)) {
 				getSession().setLocale(locale);
 			}
 			add(new WebComponent("study_does_not_exist"));
 			//add(new ConfigurationPanel("content", study.getId(), false));
 			
 			add(new Label("paymentReminder", new StringResourceModel("report_no_payment_message", this, null) ) {
 				@Override
 				public boolean isVisible() {
 					boolean isVisible = false;
 					
 					if ( !StudyPaymentInspector.isPaid(study) ) {
 						isVisible = true;
 					}
 					
 					return isVisible;
 				}
 			});
 		}
	}
}

