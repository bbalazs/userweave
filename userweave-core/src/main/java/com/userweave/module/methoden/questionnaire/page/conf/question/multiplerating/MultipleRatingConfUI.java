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
package com.userweave.module.methoden.questionnaire.page.conf.question.multiplerating;

import java.util.Locale;

import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.components.callback.EventHandler;
import com.userweave.dao.BaseDao;
import com.userweave.domain.service.QuestionService;
import com.userweave.module.methoden.questionnaire.dao.MultipleRatingQuestionDao;
import com.userweave.module.methoden.questionnaire.dao.QuestionDao;
import com.userweave.module.methoden.questionnaire.domain.question.MultipleRatingQuestion;
import com.userweave.module.methoden.questionnaire.page.conf.question.QuestionConfigurationBaseUI;
import com.userweave.pages.components.slidableajaxtabpanel.ChangeTabsCallback;

/**
 * @author Florian Pavkovic
 */
@SuppressWarnings("serial")
public class MultipleRatingConfUI extends QuestionConfigurationBaseUI<MultipleRatingQuestion> {
	
	@SpringBean
	private QuestionDao questionDao;
	
	@SpringBean
	private QuestionService questionService;
	
	@SpringBean
	private MultipleRatingQuestionDao dao;
	
	public MultipleRatingConfUI (
			String id,
			int configurationId,
			int questionId,
			Locale studyLocale,
			EventHandler callback,
			ChangeTabsCallback changeTabsCallback) 
	{
		super(id, questionId, callback, changeTabsCallback);
		
		addFormComponent(new MultipleRatingConfigurationPanel("panel", configurationId, questionId, studyLocale));
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
	protected BaseDao<MultipleRatingQuestion> getBaseDao() {
		return dao;
	}
}