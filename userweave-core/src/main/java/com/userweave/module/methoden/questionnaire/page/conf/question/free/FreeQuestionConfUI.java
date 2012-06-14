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
package com.userweave.module.methoden.questionnaire.page.conf.question.free;

import java.util.Locale;

import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.components.callback.EventHandler;
import com.userweave.dao.BaseDao;
import com.userweave.domain.service.QuestionService;
import com.userweave.module.methoden.questionnaire.dao.FreeQuestionDao;
import com.userweave.module.methoden.questionnaire.dao.QuestionDao;
import com.userweave.module.methoden.questionnaire.domain.question.FreeQuestion;
import com.userweave.module.methoden.questionnaire.page.conf.question.QuestionConfigurationBaseUI;
import com.userweave.pages.components.slidableajaxtabpanel.ChangeTabsCallback;

/**
 * @author Florian Pavkovic
 */
public class FreeQuestionConfUI extends QuestionConfigurationBaseUI<FreeQuestion> 
{
	private static final long serialVersionUID = 1L;

	@SpringBean
	private QuestionDao questionDao;
	@SpringBean
	private QuestionService questionService;
	@SpringBean
	private FreeQuestionDao dao;
	
	public FreeQuestionConfUI (
			String id,
			int configurationId,
			int questionId,
			Locale studyLocale,
			EventHandler callback,
			ChangeTabsCallback changeTabsCallback) 
	{
		super(id, questionId, callback, changeTabsCallback);
		
		addFormComponent(new FreeQuestionConfigurationPanel("panel", configurationId, questionId, studyLocale));
	}

	@Override
	protected QuestionDao getQuestionDao() {
		return questionDao;
	}
	
	@Override
	protected QuestionService getQuestionService() {
		return questionService;
	}
	
	@Override
	protected BaseDao<FreeQuestion> getBaseDao() {
		return dao;
	}
}