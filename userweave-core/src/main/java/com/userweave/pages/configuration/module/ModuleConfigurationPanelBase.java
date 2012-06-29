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
package com.userweave.pages.configuration.module;

import java.util.Locale;

import org.apache.wicket.feedback.ContainerFeedbackMessageFilter;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import com.userweave.dao.StudyDependendDao;
import com.userweave.domain.ModuleConfigurationEntityBase;
import com.userweave.domain.StudyState;
import com.userweave.module.methoden.questionnaire.page.conf.question.feedbackl.CustomFeedbackPanel;

/**
 * @author oma
 */
@SuppressWarnings("serial")
public abstract class ModuleConfigurationPanelBase<T extends ModuleConfigurationEntityBase> extends Panel {

	private final Integer configurationId;
	
	private final Locale studyLocale;
	
	private CustomFeedbackPanel feedback;
	
	public FeedbackPanel getFeedbackPanel()
	{
		return feedback;
	}
	
	protected Integer getConfigurationId() {
		return configurationId;
	}
	
	public ModuleConfigurationPanelBase(String id, Integer configurationId, Locale studyLocale) {
		super(id);
		this.configurationId = configurationId;
		this.studyLocale = studyLocale;
		init(configurationId);		
	}

	private void init(final Integer configurationId) {
		setDefaultModel(
			new CompoundPropertyModel( 
				new LoadableDetachableModel() {

					@Override
					protected Object load() {
						return getConfigurationDao().findById(configurationId);
					}
				}
			)
		);

		add(feedback = new CustomFeedbackPanel("feedbackPanel"));
		
		feedback.setOutputMarkupId(true);
		
		feedback.setFilter(new ContainerFeedbackMessageFilter(ModuleConfigurationPanelBase.this));

	}
	
	protected IModel getConfigurationModel() {
		return getDefaultModel();
	}
	
	protected final Locale getStudyLocale() {
		return studyLocale;
	}
	
	protected T getConfiguration() {
		return (T) getDefaultModelObject();
	}
	
	protected void saveConfiguration() {
		getConfigurationDao().save(getConfiguration());
	}
	
	protected abstract StudyDependendDao<T> getConfigurationDao();

	protected boolean studyIsInState(StudyState state) {
		return getConfiguration().getStudy().getState() == state;
	}
	
	
}

