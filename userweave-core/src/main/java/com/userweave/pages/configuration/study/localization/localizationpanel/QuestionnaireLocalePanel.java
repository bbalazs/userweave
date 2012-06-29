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
package com.userweave.pages.configuration.study.localization.localizationpanel;

import java.util.List;
import java.util.Locale;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.AbstractTextComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.ajax.IAjaxLocalizedStringUpdater;
import com.userweave.ajax.form.AjaxBehaviorFactory;
import com.userweave.dao.LocalizedStringDao;
import com.userweave.domain.EntityBase;
import com.userweave.domain.LocalizedString;
import com.userweave.module.ModuleConfigurationImpl;
import com.userweave.module.methoden.questionnaire.domain.QuestionnaireConfigurationEntity;
import com.userweave.module.methoden.questionnaire.domain.question.Question;
import com.userweave.pages.configuration.study.localization.LocalizationPanelFactory;
import com.userweave.utils.LocalizationUtils;

public class QuestionnaireLocalePanel 
	extends Panel
	implements IAjaxLocalizedStringUpdater
{
	private static final long serialVersionUID = 1L;

	@SpringBean
	private LocalizedStringDao localizedStringDao;
	
	public QuestionnaireLocalePanel(String id, List<Locale> locales,
			ModuleConfigurationImpl<?> entity, Locale studyLocale)
	{
		super(id);
		
		//add(new Label("entityName", getEntityName(entity)));
		
		RepeatingView rv = new RepeatingView("questions");
		
		add(rv);
		
		rv.add(new AbstractLocalizationPanelWithHeadline(
			"headline", locales, entity.getLocalizedStrings(), entity, studyLocale)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected IModel getEntityName(EntityBase entity)
			{
				return QuestionnaireLocalePanel.this.getEntityName(entity);
			}
			
			@Override
			protected AbstractTextComponent getTextComponentForLocaleConfiguration(
					String id, LocalizedString localeString, int index, Locale locale)
			{
				TextField textField = new TextField(
						id, 
						new PropertyModel(
								localeString, 
								"value" + LocalizationUtils.getIsoLangCode(locale).toUpperCase()));
					
				textField.add(AjaxBehaviorFactory.getUpdateBehaviorForLocalizedString(
						"onblur", localeString, QuestionnaireLocalePanel.this));
				
				return textField;
			}
			
			@Override
			protected IModel getLabelForConfigurationRow(int index, int size)
			{
				return getLabelForRow(1, 0);
			}
		});
		
		List<Question> questions = 
			((QuestionnaireConfigurationEntity) entity).getQuestions();
		
		for(Question question : questions)
		{
			rv.add(LocalizationPanelFactory.createQuestionLocalePanel(
					rv.newChildId(), locales, question, studyLocale));
		}
		
	}

	protected IModel getEntityName(EntityBase entity)
	{
		return new StringResourceModel(
				"questionnaire", 
				this, 
				null, 
				new Object[] {((QuestionnaireConfigurationEntity)entity).getName()});
	}
	
	@Override
	public void onUpdate(AjaxRequestTarget target, LocalizedString string)
	{
		localizedStringDao.save(string);
	}
}
