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
package com.userweave.module.methoden.questionnaire.page.conf.question.multiplerating;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.ajax.form.AjaxBehaviorFactory;
import com.userweave.components.authorization.AuthOnlyDropDownChoice;
import com.userweave.components.authorization.AuthOnlyTextField;
import com.userweave.components.model.LocalizedPropertyModel;
import com.userweave.domain.LocalizedString;
import com.userweave.domain.OrderedEntityBase;
import com.userweave.domain.StudyState;
import com.userweave.module.methoden.questionnaire.dao.QuestionDao;
import com.userweave.module.methoden.questionnaire.domain.question.MultipleRatingQuestion;
import com.userweave.module.methoden.questionnaire.domain.question.RatingTerm;
import com.userweave.module.methoden.questionnaire.page.conf.question.QuestionConfigurationPanel;
import com.userweave.pages.components.button.AddLink;
import com.userweave.pages.components.reorderable.LocalizedStringReorderableListPanel;

/**
 * @author oma
 */
public class MultipleRatingConfigurationPanel 
	extends QuestionConfigurationPanel<MultipleRatingQuestion> 
{
	private static final long serialVersionUID = 1L;
	
	@SpringBean
	private QuestionDao questionDao;
	
	public MultipleRatingConfigurationPanel(
		String id, int configurationId, Integer theQuestionId, Locale studyLocale) 
	{
		super(id, configurationId, theQuestionId, MultipleRatingQuestion.TYPE, studyLocale);
	
		addFormComponents(getQuestionForm(), configurationId);
	}
	
	private void addFormComponents(Form form, final int configurationId) 
	{	
		AuthOnlyDropDownChoice dropdown = new AuthOnlyDropDownChoice(
				"numberOfRatingSteps", 					
				Arrays.asList(new Integer[] {4, 5, 6, 7, 8, 9})
			);
		
		dropdown.setRequired(true);
		
		dropdown.setOutputMarkupId(true);
		
		form.add(dropdown);
		
		dropdown.add(AjaxBehaviorFactory.getUpdateBehavior(
			"onchange", MultipleRatingConfigurationPanel.this));
		
		addAntipodes(
			form, new PropertyModel(getDefaultModel(), "antipodePair"), getStudyLocale());
		
		
		Boolean showNoAnswer = getQuestion().getShowNoAnswerOption();
		
		// may be null, if question has been created
		if(showNoAnswer == null)
		{
			getQuestion().setShowNoAnswerOption(false);
			addQuestionToConfigurationAndSave(configurationId);
		}
		
		AuthOnlyDropDownChoice noAnswer = 
			new AuthOnlyDropDownChoice(
				"showNoAnswerOption",
				new PropertyModel(getDefaultModel(), "showNoAnswerOption"),
				Arrays.asList(new Boolean[] {true, false}),
				new IChoiceRenderer()
				{
					private static final long serialVersionUID = 1L;

					@Override
					public String getIdValue(Object object, int index)
					{
						return object.toString() + index;
					}
					
					@Override
					public Object getDisplayValue(Object object)
					{
						if(((Boolean) object))
						{
							return new StringResourceModel(
								"showNoAnswer", 
								MultipleRatingConfigurationPanel.this,
								null).getObject();
						}
						
						return new StringResourceModel(
								"showNoAnswerReset", 
								MultipleRatingConfigurationPanel.this,
								null).getObject();
					}
				});
		
		noAnswer.add(AjaxBehaviorFactory.getUpdateBehavior(
			"onchange", MultipleRatingConfigurationPanel.this));
		
//		noAnswer.add(new AjaxFormComponentUpdatingBehavior("onchange") 
//		{	
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			protected void onUpdate(AjaxRequestTarget target) 
//			{
//				getQuestion().setShowNoAnswerOption(showNoAnswer);
//				
//				target.addComponent(MultipleRatingConfigurationPanel.this.get("feedbackPanel"));				
//				addQuestionToConfigurationAndSave(configurationId);
//			}
//		});
		
		//AuthOnlyCheckBox noAnswer = new AuthOnlyCheckBox("showNoAnswerOption");
		
		noAnswer.setOutputMarkupId(true);
		
		form.add(noAnswer);
		
		//noAnswer.add(getUpdateBehavior("onchange"));
		
		
		
		form.add(new LocalizedStringReorderableListPanel(
			"valueListPanel", 
			studyIsInState(StudyState.INIT), 
			getStudyLocale(), AddLink.ADD_ANSWER) 
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void delete(LocalizedString objectToDelete, List<LocalizedString> objects) {

				getQuestion().removeFromRatingTerms(objectToDelete);
				questionDao.save(getQuestion());

				// also change display objects
				objects.remove(objectToDelete);				
			}

			@Override
			protected List<LocalizedString> getDisplayObjects() {
				List<LocalizedString> rv = new ArrayList<LocalizedString>();
			    for (RatingTerm term: getQuestion().getRatingTerms()) {
			    	term.getText().setPosition(term.getPosition());
			    	rv.add(term.getText());
			    }	    
			    return rv;
			}

			@Override
			protected void moveDown(LocalizedString orderedObject,	List<LocalizedString> objects) {
				RatingTerm term = findTerm(orderedObject);
				List<RatingTerm> terms = getQuestion().getRatingTerms();
				OrderedEntityBase.moveDown(terms, term);
				questionDao.save(getQuestion());
				
				// also change display objects
				OrderedEntityBase.moveDown(objects, orderedObject);

			}

			@Override
			protected void moveUp(LocalizedString orderedObject, List<LocalizedString> objects) {
				RatingTerm itmTerm = findTerm(orderedObject);
				List<RatingTerm> itmTerms = getQuestion().getRatingTerms();
				OrderedEntityBase.moveUp(itmTerms, itmTerm);
				questionDao.save(getQuestion());
				
				// also change display objects
				OrderedEntityBase.moveUp(objects, orderedObject);
				
			}

			@Override
			protected void append(LocalizedString localizedString) {
				getQuestion().addToRatingTerms(localizedString);
				questionDao.save(getQuestion());
				
			}
			
		});
	}
	
	private void addAntipodes(Form form, IModel antipodeModel, Locale locale)
	{
		Component antipode1 = 
			createAntipode("antipode1", "antipode1", antipodeModel, locale);
		
		form.add(antipode1);
		
		Component antipode2 = 
			createAntipode("antipode2", "antipode2", antipodeModel, locale);
		
		form.add(antipode2);		
	}
	
	private Component createAntipode(
			String componentId, final String antipodeName, IModel antipodeModel, Locale locale)
	{
		Component antipode;
		
		if(studyIsInState(StudyState.INIT))
		{
			antipode = new AuthOnlyTextField(
				componentId, 
				new LocalizedPropertyModel(antipodeModel, antipodeName, locale),
				AjaxBehaviorFactory.getUpdateBehavior("onblur", MultipleRatingConfigurationPanel.this));
					
			((AuthOnlyTextField)antipode).setRequired(true);
			
			antipode.setOutputMarkupId(true);
		}
		else
		{
			antipode = new Label(
				componentId,new LocalizedPropertyModel(antipodeModel, antipodeName, locale));
		}
		
		return antipode;
	}
	
	@Override
	protected QuestionDao getQuestionDao() {
		return questionDao;
	}
	
	private RatingTerm findTerm(LocalizedString termValue) {
		if(termValue == null) return null;
		
		for (RatingTerm term: getQuestion().getRatingTerms()) {
			if ((term.getText() != null) && term.getText().equals(termValue)) {
				return term;
			}
		}
		
		return null;
	}
	
	@Override
	protected IModel getTypeModel()
	{
		return new StringResourceModel("mutliplerating_type", this, null);
	}
}

