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
package com.userweave.module.methoden.questionnaire.page.grouping.multiplepossibleanswers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.components.model.LocalizedModel;
import com.userweave.domain.LocalizedString;
import com.userweave.module.methoden.questionnaire.dao.QuestionDao;
import com.userweave.module.methoden.questionnaire.domain.QuestionWithMultiplePossibleAnswers;
import com.userweave.module.methoden.questionnaire.domain.group.MultiplePossibleAnswersGroup;
import com.userweave.module.methoden.questionnaire.page.grouping.GroupAddedCallback;
import com.userweave.pages.grouping.GroupingPanelWithName;


/**
 * @author oma
 */
@SuppressWarnings("serial")
public abstract class MultiplePossibleAnswersGroupingPanel<T extends MultiplePossibleAnswersGroup> extends GroupingPanelWithName<T> {
	
	@SpringBean
	private QuestionDao questionDao;

	private final List<LocalizedString> answers = new ArrayList<LocalizedString>();
	
	private final Integer questionId;

	
	public MultiplePossibleAnswersGroupingPanel(
		String id, QuestionWithMultiplePossibleAnswers question, 
		T group, final Locale locale, 
		GroupAddedCallback groupAddedCallback) 
	{
		super(id, group, locale, groupAddedCallback);

		questionId = question.getId();
		
		CheckGroup answers = 
			new CheckGroup(
					"answers", 
					new PropertyModel(MultiplePossibleAnswersGroupingPanel.this, "answers"));
	    
		if(isOnChangeAjaxBehaviorNeeded())
		{
			answers.add(new AjaxFormChoiceComponentUpdatingBehavior()
			{
				@Override
				protected void onUpdate(AjaxRequestTarget target)
				{
					MultiplePossibleAnswersGroupingPanel.this.onUpdate(target);
				}
			});
		}
		
		add(answers);
	    
		IModel possibleAnswersModel = new LoadableDetachableModel() {

			@Override
			protected Object load() {
				QuestionWithMultiplePossibleAnswers question = (QuestionWithMultiplePossibleAnswers) questionDao.findById(questionId);
				return question.getPossibleAnswers();
			}
			
		};
		
		answers.add(
			new ListView("values", possibleAnswersModel) {
			    @Override
				protected void populateItem(ListItem item) {
			      item.add(new Check("check", item.getModel()));
			      item.add(new Label("content", new LocalizedModel((Serializable) item.getModelObject(), locale)));
			    };
		    }
		);		
	}
	
	public Integer getQuestionId() {
		return questionId;
	}

	@Override
	public void submit() 
	{
		submit(getGroup(), answers);		
	}

	/**
	 * Attach an ajax behavior to the chech group.
	 * @return
	 */
	protected boolean isOnChangeAjaxBehaviorNeeded()
	{
		return false;
	}
	
	/**
	 * Override this method, if an ajax behavior is attached.
	 * @param target
	 */
	protected void onUpdate(AjaxRequestTarget target)
	{
		// do nothing
	}
	
	protected List<LocalizedString> getAnswers()
	{
		return answers;
	}
	
	protected abstract void submit(T group, List<LocalizedString> answers);

}

