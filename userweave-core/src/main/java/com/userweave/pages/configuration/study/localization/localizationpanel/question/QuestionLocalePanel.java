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

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.form.AbstractTextComponent;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;

import com.userweave.ajax.form.AjaxBehaviorFactory;
import com.userweave.domain.LocalizedString;
import com.userweave.module.methoden.questionnaire.domain.question.Question;
import com.userweave.pages.configuration.study.localization.localizationpanel.AbstractLocalizationPanel;
import com.userweave.utils.LocalizationUtils;

public abstract class QuestionLocalePanel extends AbstractLocalizationPanel
{
	private static final long serialVersionUID = 1L;

	public QuestionLocalePanel(String id, List<Locale> locales,
			Question question, Locale studyLocale)
	{
		super(id, locales, question.getLocalizedStrings(), question, studyLocale);
	}

	@Override
	protected IModel getLabelForRow(int index, int size)
	{
		// name of question
//		if(index == 0)
//		{
//			return new StringResourceModel("question_name", this, null);
//		}
		
		// stimulus of question
		if(index == 0)
		{
			return new StringResourceModel("question_stimulus", this, null);
		}
		
		return getLabelForQuestionSpecificTerm(index, size);
	}

	@Override
	protected AbstractTextComponent getTextComponentForLocale(String id,
			LocalizedString localeString, int index, Locale locale)
	{
		// name of question
//		if(index == 0)
//		{
//			TextField textField = new TextField(
//					id, 
//					new PropertyModel(
//							localeString, 
//							"value" + LocalizationUtils.getIsoLangCode(locale).toUpperCase()));
//				
//			textField.add(getUpdateBehavior("onblur", localeString));
//				
//			return textField;
//			
//		}
		
		// stimulus of question
		if(index == 0)
		{
			TextArea textArea = new TextArea(
					id, 
					new PropertyModel(
						localeString, 
						"value" + LocalizationUtils.getIsoLangCode(locale).toUpperCase())); 
			
			textArea.add(AjaxBehaviorFactory.getUpdateBehaviorForLocalizedString(
					"onblur", localeString, QuestionLocalePanel.this));
			textArea.add(new AttributeModifier("row", true, new Model(8)));
			textArea.add(new AttributeModifier("col", true, new Model(35)));
			
			return textArea;
		}
		
		return getQuestionSpecificTextComponentForLocale(id, localeString, index, locale);
	}

	protected abstract IModel getLabelForQuestionSpecificTerm(int index, int size);
	
	protected abstract AbstractTextComponent getQuestionSpecificTextComponentForLocale(
			String id,LocalizedString localeString, int index, Locale locale);
}
