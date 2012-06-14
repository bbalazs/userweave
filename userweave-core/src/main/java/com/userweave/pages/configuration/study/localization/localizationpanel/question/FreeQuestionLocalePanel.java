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
package com.userweave.pages.configuration.study.localization.localizationpanel.question;

import java.util.List;
import java.util.Locale;

import org.apache.wicket.markup.html.form.AbstractTextComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

import com.userweave.domain.EntityBase;
import com.userweave.domain.LocalizedString;
import com.userweave.module.methoden.questionnaire.domain.question.Question;

public class FreeQuestionLocalePanel extends QuestionLocalePanel
{
	private static final long serialVersionUID = 1L;

	public FreeQuestionLocalePanel(String id, List<Locale> locales,
			Question question, Locale studyLocale)
	{
		super(id, locales, question, studyLocale);
	}


	@Override
	protected IModel getEntityName(EntityBase entity)
	{
		return new StringResourceModel(
				"freeQuestion", 
				this, 
				null, 
				new Object[] {((Question)entity).getName().getValue(getLocale())});
	}
	
	
	@Override
	protected IModel getLabelForQuestionSpecificTerm(int index, int size)
	{
		return null;
	}

	@Override
	protected AbstractTextComponent getQuestionSpecificTextComponentForLocale(
			String id, LocalizedString localeString, int index, Locale locale)
	{
		return null;
	}

}
