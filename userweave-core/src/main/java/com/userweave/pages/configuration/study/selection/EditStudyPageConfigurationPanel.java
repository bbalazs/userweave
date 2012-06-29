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
package com.userweave.pages.configuration.study.selection;

import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import com.userweave.components.localerenderer.LocaleChoiceRenderer;
import com.userweave.domain.Study;
import com.userweave.module.methoden.questionnaire.page.conf.question.feedbackl.CustomFeedbackPanel;
import com.userweave.utils.LocalizationUtils;

public class EditStudyPageConfigurationPanel extends Panel
{
	private static final long serialVersionUID = 1L;
	
	public EditStudyPageConfigurationPanel(String id, final Study study, ModalWindow window)
	{
		super(id);
		
		setDefaultModel(new CompoundPropertyModel(study));
		
		init(window);
	}

	private void init(final ModalWindow window)
	{
		add(new TextField("name").setRequired(true));
		
		DropDownChoice localeDropDown = new DropDownChoice(
			"locale", 						
			LocalizationUtils.getSupportedStudyLocales(), 
			new LocaleChoiceRenderer(getLocale())
		);
		
		localeDropDown.setRequired(true);
		localeDropDown.setOutputMarkupId(true);
		
		add(localeDropDown);
		
		// #478: disable label of reference language
		localeDropDown.setVisible(getStudy().getId() == null);
		
		final FeedbackPanel feedbackPanel = new CustomFeedbackPanel("feedback");
		feedbackPanel.setOutputMarkupId(true);
		
		add(feedbackPanel);
	}
	
	public Study getStudy() 
	{
		return (Study) getDefaultModelObject(); 
	}
}
