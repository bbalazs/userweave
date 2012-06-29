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
package com.userweave.module.methoden.questionnaire.page.conf.question.singleanswer;

import java.util.Arrays;
import java.util.Locale;

import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

import com.userweave.ajax.form.AjaxBehaviorFactory;
import com.userweave.components.authorization.AuthOnlyDropDownChoice;
import com.userweave.module.methoden.questionnaire.domain.question.SingleAnswerQuestion;
import com.userweave.module.methoden.questionnaire.domain.question.SingleAnswerQuestion.DisplayType;
import com.userweave.module.methoden.questionnaire.page.conf.question.MultiplePossibleAnswersConfigurationPanel;

public class SingleAnswerConfigurationPanel 
	extends MultiplePossibleAnswersConfigurationPanel<SingleAnswerQuestion> 
{
	private static final long serialVersionUID = 1L;

	public SingleAnswerConfigurationPanel(
		String id, int configurationId, Integer theQuestionId, Locale studyLocale) 
	{
		super(id, configurationId, theQuestionId, SingleAnswerQuestion.TYPE, studyLocale);	
		
		AuthOnlyDropDownChoice choices = new AuthOnlyDropDownChoice(
				"displayType", 
				Arrays.asList(DisplayType.values()),
				new LocalizedAnswerTypeChoiceRenderer(this));
		
		choices.setRequired(true);
		
		choices.setOutputMarkupId(true);
		
		getQuestionForm().add(choices);
		
		choices.add(AjaxBehaviorFactory.getUpdateBehavior(
			"onchange", SingleAnswerConfigurationPanel.this));
	}

	@Override
	protected SingleAnswerQuestion createNewQuestion() {
		SingleAnswerQuestion question = super.createNewQuestion();
		question.setDisplayType(DisplayType.DROP_DOWN);
		return question;
	}
	
	private class LocalizedAnswerTypeChoiceRenderer implements IChoiceRenderer 
	{
		private static final long serialVersionUID = 1L;

		private final SingleAnswerConfigurationPanel parent;
		
		public LocalizedAnswerTypeChoiceRenderer(SingleAnswerConfigurationPanel parent)
		{
			this.parent = parent;
		}
		
		@Override
		public Object getDisplayValue(Object object) {
			StringResourceModel srm = new StringResourceModel(((DisplayType) object).name(), parent, null);
			
			return srm.getObject();
		}

		@Override
		public String getIdValue(Object object, int index) {
			return ((DisplayType) object).toString();
		}
		
	}
	
	@Override
	protected IModel getTypeModel()
	{
		return new StringResourceModel("singleanswer_type", this, null);
	}
}
