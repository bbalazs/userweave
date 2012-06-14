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
package com.userweave.module.methoden.questionnaire.page.grouping.rating.dimensions;

import java.util.Locale;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.components.model.LocalizedPropertyModel;
import com.userweave.module.methoden.questionnaire.dao.QuestionDao;
import com.userweave.module.methoden.questionnaire.dao.QuestionnaireGroupDao;
import com.userweave.module.methoden.questionnaire.domain.group.DimensionsGroup;
import com.userweave.module.methoden.questionnaire.domain.group.QuestionnaireGroup;
import com.userweave.module.methoden.questionnaire.domain.question.AntipodePair;
import com.userweave.module.methoden.questionnaire.domain.question.DimensionsQuestion;
import com.userweave.module.methoden.questionnaire.page.grouping.GroupAddedCallback;
import com.userweave.module.methoden.questionnaire.page.grouping.rating.RatingGroupingPanel;


/**
 * @author oma
 */
@SuppressWarnings("serial")
public class DimensionsGroupingPanel extends RatingGroupingPanel<DimensionsGroup> {
	
	@SpringBean
	private QuestionDao questionDao;
	
	private final Integer questionId;
	
	@SpringBean
	private QuestionnaireGroupDao questionnaireGroupDao;
	
	private AntipodePair antipodePair;
	
	public DimensionsGroupingPanel(String id, DimensionsQuestion question, DimensionsGroup group, final Locale locale, GroupAddedCallback<QuestionnaireGroup> groupAddedCallback) {
		super(id, question.getNumberOfRatingSteps(), group, locale, groupAddedCallback, true);
		
		questionId = question.getId();
		
		getStimulus().setDefaultModel(new LocalizedPropertyModel(question, "text", locale));
		getStimulus().modelChanged();
		
		IModel antipodePairsModel = new LoadableDetachableModel() {

			@Override
			protected Object load() {
				return ((DimensionsQuestion) questionDao.findById(questionId)).getAntipodePairs();
			}
			
		};		
	
		add(
			new DropDownChoice("antipodePair", antipodePairsModel, 
				new IChoiceRenderer() {

					@Override
					public Object getDisplayValue(Object object) {
						return ((AntipodePair)object).toString(locale);
					}

					@Override
					public String getIdValue(Object object, int index) {
						return ((AntipodePair)object).getId().toString();
					}
					
				}
			)
			.setRequired(true)
		);

	}
	
	@Override
	protected IModel getTitle() 
	{
		return new StringResourceModel("dimensions", this, null);
	}
	
	
	@Override
	public void submit() 
	{
		super.submit();
		getGroup().setDimensionsQuestion((DimensionsQuestion) questionDao.findById(questionId));
		questionnaireGroupDao.save(getGroup());			
	}

	@Override
	protected boolean isStimulusVisible()
	{
		return true;
	}

}

