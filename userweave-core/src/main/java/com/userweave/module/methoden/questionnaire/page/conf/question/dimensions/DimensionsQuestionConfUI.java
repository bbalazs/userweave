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
package com.userweave.module.methoden.questionnaire.page.conf.question.dimensions;

import java.util.Locale;

import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.components.callback.EventHandler;
import com.userweave.dao.BaseDao;
import com.userweave.domain.service.QuestionService;
import com.userweave.module.methoden.questionnaire.dao.DimensionsQuestionDao;
import com.userweave.module.methoden.questionnaire.dao.QuestionDao;
import com.userweave.module.methoden.questionnaire.domain.question.DimensionsQuestion;
import com.userweave.module.methoden.questionnaire.page.conf.question.QuestionConfigurationBaseUI;
import com.userweave.pages.components.slidableajaxtabpanel.ChangeTabsCallback;

/**
 * @author Florian Pavkovic
 */
@SuppressWarnings("serial")
public class DimensionsQuestionConfUI 
	extends QuestionConfigurationBaseUI<DimensionsQuestion> {
	
	@SpringBean
	private QuestionDao questionDao;
	@SpringBean
	private QuestionService questionService;
	@SpringBean
	private DimensionsQuestionDao dao;

	public DimensionsQuestionConfUI (
			String id,
			int configurationId,
			int questionId,
			Locale studyLocale,
			EventHandler callback,
			ChangeTabsCallback changeTabsCallback) 
	{
		super(id, questionId, callback, changeTabsCallback);
		
		addFormComponent(
				new DimensionsQuestionConfigurationPanel(
						"panel", configurationId, questionId, studyLocale));
	}

	@Override
	protected QuestionDao getQuestionDao() {
		return questionDao;
	}
	
	@Override
	protected QuestionService getQuestionService() {
		return questionService;
	}

//	@Override
//	protected IModel getType() {
//		return new StringResourceModel("type_value", this, null);
//	}
	
	@Override
	protected BaseDao<DimensionsQuestion> getBaseDao() {
		return dao;
	}
}