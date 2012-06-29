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

import java.util.Arrays;
import java.util.Locale;

import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.ajax.form.AjaxBehaviorFactory;
import com.userweave.components.authorization.AuthOnlyDropDownChoice;
import com.userweave.module.methoden.questionnaire.dao.QuestionDao;
import com.userweave.module.methoden.questionnaire.domain.question.FreeQuestion;
import com.userweave.module.methoden.questionnaire.domain.question.FreeQuestion.AnswerType;
import com.userweave.module.methoden.questionnaire.page.conf.question.QuestionConfigurationPanel;

/**
 * @author oma
 */
public class FreeQuestionConfigurationPanel extends QuestionConfigurationPanel<FreeQuestion> 
{
	private static final long serialVersionUID = 1L;
	
	@SpringBean
	private QuestionDao questionDao;
	
	public FreeQuestionConfigurationPanel(
		String id, int configurationId, Integer theQuestionId, Locale studyLocale) 
	{
		super(id, configurationId, theQuestionId, FreeQuestion.TYPE, studyLocale);
		
		AuthOnlyDropDownChoice dropdown = new AuthOnlyDropDownChoice("answerType", 
				Arrays.asList(FreeQuestion.AnswerType.values()),
				new LocalizedAnswerTypeChoiceRenderer(this));
		
		dropdown.setRequired(true);
		
		dropdown.setOutputMarkupId(true);
		
		getQuestionForm().add(dropdown);
		
		dropdown.add(AjaxBehaviorFactory.getUpdateBehavior("onchange", FreeQuestionConfigurationPanel.this));
	}
	
	@Override
	protected FreeQuestion createNewQuestion() 
	{
		FreeQuestion freeQuestion = super.createNewQuestion();
		freeQuestion.setAnswerType(FreeQuestion.AnswerType.SHORT_TEXT);
		return freeQuestion;
	}
	
	@Override
	protected QuestionDao getQuestionDao() {
		return questionDao;
	}
	
	private class LocalizedAnswerTypeChoiceRenderer implements IChoiceRenderer 
	{
		private static final long serialVersionUID = 1L;
	
		FreeQuestionConfigurationPanel parent;
		
		public LocalizedAnswerTypeChoiceRenderer(FreeQuestionConfigurationPanel parent)
		{
			this.parent = parent;
		}
		
		@Override
		public Object getDisplayValue(Object object) {
			StringResourceModel srm = new StringResourceModel(
				((AnswerType) object).name(), parent, null);
			
			return srm.getObject();
		}

		@Override
		public String getIdValue(Object object, int index) {
			return ((AnswerType) object).toString();
		}
	}

	@Override
	protected IModel getTypeModel()
	{
		return new StringResourceModel("freequestion_type", this, null);
	}
}

