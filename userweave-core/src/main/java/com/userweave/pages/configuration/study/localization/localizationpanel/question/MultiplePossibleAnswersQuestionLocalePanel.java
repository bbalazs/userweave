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
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;

import com.userweave.ajax.form.AjaxBehaviorFactory;
import com.userweave.domain.EntityBase;
import com.userweave.domain.LocalizedString;
import com.userweave.module.methoden.questionnaire.domain.question.MultipleAnswersQuestion;
import com.userweave.module.methoden.questionnaire.domain.question.Question;
import com.userweave.utils.LocalizationUtils;

public class MultiplePossibleAnswersQuestionLocalePanel extends QuestionLocalePanel
{
	private static final long serialVersionUID = 1L;

	public MultiplePossibleAnswersQuestionLocalePanel(String id, List<Locale> locales,
			Question question, Locale studyLocale)
	{
		super(id, locales, question, studyLocale);
	}

	@Override
	protected IModel getEntityName(EntityBase entity)
	{
		String id;
		
		if(entity instanceof MultipleAnswersQuestion)
		{
			id = "multipleAnswerQuestion";
		}
		else
		{
			id = "singleAnswerQuestion";
		}
		
		return new StringResourceModel(
				id, 
				this, 
				null, 
				new Object[] {((Question)entity).getName().getValue(getLocale())});
	}
	
	@Override
	protected IModel getLabelForQuestionSpecificTerm(int index, int size)
	{
		return new StringResourceModel("answer", this, null, new Object[] {index + 1});
	}

	@Override
	protected AbstractTextComponent getQuestionSpecificTextComponentForLocale(
			String id, LocalizedString localeString, int index, Locale locale)
	{
		TextField textField = new TextField(
				id, 
				new PropertyModel(
						localeString, 
						"value" + LocalizationUtils.getIsoLangCode(locale).toUpperCase()));
			
		textField.add(AjaxBehaviorFactory.getUpdateBehaviorForLocalizedString(
				"onblur", localeString, MultiplePossibleAnswersQuestionLocalePanel.this));
			
		return textField;
	}

}
