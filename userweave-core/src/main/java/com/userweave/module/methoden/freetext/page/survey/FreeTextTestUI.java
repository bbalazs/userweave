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
package com.userweave.module.methoden.freetext.page.survey;

import java.util.Locale;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.application.OnFinishCallback;
import com.userweave.components.model.LocalizedPropertyModel;
import com.userweave.dao.StudyDependendDao;
import com.userweave.domain.TestResultEntityBase;
import com.userweave.module.methoden.freetext.dao.FreeTextConfigurationDao;
import com.userweave.module.methoden.freetext.dao.FreeTextResultDao;
import com.userweave.module.methoden.freetext.domain.FreeTextConfigurationEntity;
import com.userweave.module.methoden.freetext.domain.FreeTextResult;
import com.userweave.pages.test.singleSurveyTestUI.SingleFormSurveyUI;

public class FreeTextTestUI extends SingleFormSurveyUI<FreeTextConfigurationEntity> 
{
	private static final long serialVersionUID = 1L;

	@SpringBean
	private FreeTextConfigurationDao dao;
	
	@SpringBean
	private FreeTextResultDao freeTextResultDao;
	
	public FreeTextTestUI(
		String id, 
		FreeTextConfigurationEntity configuration, 
		int surveyExecutionId, 
		OnFinishCallback onFinishCallback,
		Locale locale) 
	{
		super(id, configuration.getId(), surveyExecutionId , onFinishCallback, locale);		
		add(new Label("freetext", new LocalizedPropertyModel(getDefaultModel(), "freetext", getLocale())).setEscapeModelStrings(false));					
	}

	@Override
	protected StudyDependendDao<FreeTextConfigurationEntity> getConfigurationDao() 
	{
		return dao;
	}

	@Override
	protected void onSubmit() 
	{
		finish();		
	}

	@Override
	protected FreeTextResult createOrLoadResult() 
	{
		FreeTextResult result = freeTextResultDao.findByConfigurationAndSurveyExecution(getConfiguration(), getSurveyExecution());
		if(result == null) {
			result = new FreeTextResult();
		}
		
		if(saveScopeToResult(result)) {
			freeTextResultDao.save(result);
		}
		
		return result;
	}

	@Override
	protected void finishResult(TestResultEntityBase<FreeTextConfigurationEntity> aResult) {
		FreeTextResult result = (FreeTextResult) aResult;

		freeTextResultDao.save(result);		
	}
}
