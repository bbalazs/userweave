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
package com.userweave.pages.configuration.study.localization;

import java.util.Arrays;
import java.util.Locale;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.dao.StudyDao;
import com.userweave.domain.Study;
import com.userweave.domain.StudyState;
import com.userweave.pages.base.BaseUserWeavePage;
import com.userweave.pages.test.DisplaySurveyUI;
import com.userweave.presentation.model.EntityBaseLoadableDetachableModel;
import com.userweave.utils.LocalizationUtils;

/**
 * Page to translate the study and modules text.
 * 
 * @author opr
 */
public class TranslationPage extends BaseUserWeavePage
{
	@SpringBean
	private StudyDao studyDao;
	
	/**
	 * Default constructor.
	 * 
	 * @param parameters
	 * 		List of given GET parameters. Contains the locale
	 * 		to trnaslate to and the study hash code.
	 */
	public TranslationPage(PageParameters parameters)
	{
		Study study = findStudyByHashCode(parameters);
		Locale selectedLocale = getLocale(parameters);
		
		if(study != null && 
		   study.getState() == StudyState.INIT && // @see #1310, only works, when study is in init
		   selectedLocale != null &&
		   !study.getLocale().equals(selectedLocale)) // @see #1310, can't translate study locale
		{
			Locale studyLocale = study.getLocale();
			
			IModel studyModel = 
				new EntityBaseLoadableDetachableModel<Study>(
					Study.class, study.getId());
			
			add(new LocalizationPanel("localizationPanel", 
					studyModel, 
					Arrays.asList(new Locale[] {studyLocale, selectedLocale}),
					studyLocale));
			
			PageParameters parametersForPreview = 
				new PageParameters();
			
			parametersForPreview.set(0, parameters.get(0));
			
			add(new BookmarkablePageLink("preview", DisplaySurveyUI.class, parametersForPreview));
		}
		else
		{
			add(new WebMarkupContainer("localizationPanel"));
			
			add(new WebMarkupContainer("preview"));
		}
	}

	private Study findStudyByHashCode(PageParameters parameters)
	{
		if(!parameters.get(0).equals(""))
		{
			String studyHashCode = parameters.get(0).toString();
			
			return studyDao.findByHashcode(studyHashCode);
		}
		
		return null;
	}
	
	private Locale getLocale(PageParameters parameters)
	{
		if(! parameters.get(1).equals(""))
		{
			return LocalizationUtils.getSupportedStudyLocale(parameters.get(1).toString());
		}
		
		return null;
	}
	
	@Override
	protected Component getUserMenuPanel(String id, IModel userModel)
	{
		return new WebMarkupContainer(id);
	}
}
