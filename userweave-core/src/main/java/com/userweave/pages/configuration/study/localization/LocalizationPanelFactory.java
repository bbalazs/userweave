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

import java.util.List;
import java.util.Locale;

import org.apache.wicket.markup.html.panel.Panel;

import com.userweave.domain.EntityBase;
import com.userweave.domain.Study;
import com.userweave.module.ModuleConfiguration;
import com.userweave.module.ModuleConfigurationImpl;
import com.userweave.module.methoden.freetext.domain.FreeTextConfigurationEntity;
import com.userweave.module.methoden.iconunderstandability.domain.IconTermMatchingConfigurationEntity;
import com.userweave.module.methoden.mockup.domain.MockupConfigurationEntity;
import com.userweave.module.methoden.questionnaire.domain.QuestionnaireConfigurationEntity;
import com.userweave.module.methoden.questionnaire.domain.question.DimensionsQuestion;
import com.userweave.module.methoden.questionnaire.domain.question.FreeQuestion;
import com.userweave.module.methoden.questionnaire.domain.question.MultipleAnswersQuestion;
import com.userweave.module.methoden.questionnaire.domain.question.MultipleRatingQuestion;
import com.userweave.module.methoden.questionnaire.domain.question.Question;
import com.userweave.module.methoden.questionnaire.domain.question.SingleAnswerQuestion;
import com.userweave.module.methoden.rrt.domain.RrtConfigurationEntity;
import com.userweave.pages.configuration.study.localization.localizationpanel.FreeTextLocalePanel;
import com.userweave.pages.configuration.study.localization.localizationpanel.IconTermMatchingLocalePanel;
import com.userweave.pages.configuration.study.localization.localizationpanel.MockupLocalePanel;
import com.userweave.pages.configuration.study.localization.localizationpanel.QuestionnaireLocalePanel;
import com.userweave.pages.configuration.study.localization.localizationpanel.RrtLocalePanel;
import com.userweave.pages.configuration.study.localization.localizationpanel.StudyLocalePanel;
import com.userweave.pages.configuration.study.localization.localizationpanel.question.DimensionQuestionLocalePanel;
import com.userweave.pages.configuration.study.localization.localizationpanel.question.FreeQuestionLocalePanel;
import com.userweave.pages.configuration.study.localization.localizationpanel.question.MultiplePossibleAnswersQuestionLocalePanel;
import com.userweave.pages.configuration.study.localization.localizationpanel.question.MultipleRatingQuestionLocalePanel;

public class LocalizationPanelFactory
{
	public LocalizationPanelFactory(){}
	
	public static Panel createLocalePanelForEntity(
		String id, List<Locale> locales, final EntityBase entity, Locale studyLocale)
	{
		if(entity instanceof Study)
		{
			return new StudyLocalePanel(id, locales, (Study) entity, studyLocale);
		}
		
		else if(entity instanceof ModuleConfiguration<?>)
		{
			if(entity instanceof FreeTextConfigurationEntity)
			{
				return new FreeTextLocalePanel(id, locales, (ModuleConfigurationImpl)entity, studyLocale);
			}
			
			if(entity instanceof MockupConfigurationEntity)
			{
				return new MockupLocalePanel(id, locales, (ModuleConfigurationImpl)entity, studyLocale);
			}
			
			if(entity instanceof QuestionnaireConfigurationEntity)
			{
				return new QuestionnaireLocalePanel(id, locales, (ModuleConfigurationImpl)entity, studyLocale);
			}
			
			if(entity instanceof RrtConfigurationEntity)
			{
				return new RrtLocalePanel(id, locales, (ModuleConfigurationImpl)entity, studyLocale);
			}
			
			if(entity instanceof IconTermMatchingConfigurationEntity)
			{
				return new IconTermMatchingLocalePanel(id, locales, (ModuleConfigurationImpl)entity, studyLocale);
			}
		}
		
		return null;
	}
	
	public static Panel createQuestionLocalePanel(
			String id, List<Locale> locales, Question question, Locale studyLocale)
	{
		if(question instanceof DimensionsQuestion)
		{
			return new DimensionQuestionLocalePanel(id, locales, question, studyLocale);
		}
		
		if(question instanceof FreeQuestion)
		{
			return new FreeQuestionLocalePanel(id, locales, question, studyLocale);
		}
		
		if(question instanceof MultipleRatingQuestion)
		{
			return new MultipleRatingQuestionLocalePanel(id, locales, question, studyLocale);
		}
		
		if(question instanceof MultipleAnswersQuestion)
		{
			return new MultiplePossibleAnswersQuestionLocalePanel(id, locales, question, studyLocale);
		}
		
		if(question instanceof SingleAnswerQuestion)
		{
			return new MultiplePossibleAnswersQuestionLocalePanel(id, locales, question, studyLocale);
		}
		return null;
	}
}
