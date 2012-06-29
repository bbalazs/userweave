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
package com.userweave.module.methoden.questionnaire.page.conf;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.domain.service.QuestionService;
import com.userweave.module.methoden.questionnaire.domain.QuestionnaireConfigurationEntity;
import com.userweave.module.methoden.questionnaire.domain.question.DimensionsQuestion;
import com.userweave.module.methoden.questionnaire.domain.question.FreeQuestion;
import com.userweave.module.methoden.questionnaire.domain.question.MultipleAnswersQuestion;
import com.userweave.module.methoden.questionnaire.domain.question.MultipleRatingQuestion;
import com.userweave.module.methoden.questionnaire.domain.question.Question;
import com.userweave.module.methoden.questionnaire.domain.question.SingleAnswerQuestion;
import com.userweave.pages.components.slidableajaxtabpanel.ChangeTabsCallback;
import com.userweave.pages.components.slidableajaxtabpanel.addmethodpanel.AbstractAddPanel;

public class AddQuestionPanel extends AbstractAddPanel<String>
{
	private static final long serialVersionUID = 1L;
	
	@SpringBean
	private QuestionService questionService;
	
	protected class LocalizedQuestionTypesChoiceRenderer extends ChoiceRenderer
	{
		private static final long serialVersionUID = 1L;
	
		private final Component parent;
		
		public LocalizedQuestionTypesChoiceRenderer(
				String displayExpression, Component parent)
		{
			super(displayExpression);
			this.parent = parent;
		}
		
		@Override
		public Object getDisplayValue(Object object)
		{
			/*
			 * Internationalization of module names
			 * using their names to map a 
			 * StringResourceModel
			 */
			Object displayValue = super.getDisplayValue(object);
			
			String stringDisplayValue = ((String) displayValue).replace(" ", "_");
			
			StringResourceModel model = new StringResourceModel(
					stringDisplayValue, parent, null);
			
			return model.getObject();
		}
	}
	
	public AddQuestionPanel(
			String id, final List<String> choices, 
			final ChangeTabsCallback callback, 
			IModel configurationModel)
	{
		super(id, choices, callback);
		
		setDefaultModel(configurationModel);
	}
	
	@Override
	protected String getImageResource(String selectedItem)
	{
		if(selectedItem.compareTo(MultipleAnswersQuestion.TYPE) == 0)
		{
			return "res/FragebogenMultiple.png";
		}
		
		if(selectedItem.compareTo(SingleAnswerQuestion.TYPE) == 0)
		{
			return "res/FragebogenSingle.png";
		}
		
		if(selectedItem.compareTo(FreeQuestion.TYPE) == 0)
		{
			return "res/FragebogenFrei.png";
		}
		
		if(selectedItem.compareTo(MultipleRatingQuestion.TYPE) == 0)
		{
			return "res/FragebogenMatrix.png";
		}
		
		if(selectedItem.compareTo(DimensionsQuestion.TYPE) == 0)
		{
			return "res/FragebogenSemantisch.png";
		}
		
		return null;
	}
	
	private QuestionnaireConfigurationEntity getConfiguration()
	{
		return (QuestionnaireConfigurationEntity) getDefaultModelObject(); 
	}

	@Override
	protected IChoiceRenderer getChoiceRenderer()
	{
		return new LocalizedQuestionTypesChoiceRenderer("toString", AddQuestionPanel.this);
	}

	@Override
	protected IModel getPreviewNameOnUpdate(String selectedItem)
	{
		String displayValue = selectedItem.replace(" ", "_");
		
		return new StringResourceModel(displayValue + "_PREVIEW", this, null);
	}

	@Override
	protected int createNewItem(String selectedItem, String name)
	{
		QuestionnaireConfigurationEntity conf = getConfiguration();
		
		Question q = questionService.createQuestion(conf.getId(), name, selectedItem);
		
		conf.addToQuestions(q);
		
		conf.save();
		
		return q.getPosition();
	}
	
}
