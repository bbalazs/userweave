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
package com.userweave.module.methoden.questionnaire.page.conf.question;

import java.util.List;
import java.util.Locale;

import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.domain.LocalizedString;
import com.userweave.domain.OrderedEntityBase;
import com.userweave.domain.StudyState;
import com.userweave.module.methoden.questionnaire.dao.QuestionDao;
import com.userweave.module.methoden.questionnaire.domain.QuestionWithMultiplePossibleAnswers;
import com.userweave.pages.components.button.AddLink;
import com.userweave.pages.components.reorderable.LocalizedStringReorderableListPanel;

/**
 * @author oma
 */
public abstract class MultiplePossibleAnswersConfigurationPanel<T extends QuestionWithMultiplePossibleAnswers> 
	extends QuestionConfigurationPanel<T> 
{
	private static final long serialVersionUID = 1L;
	
	@SpringBean
	private QuestionDao questionDao;
	
	public MultiplePossibleAnswersConfigurationPanel(
		final String id, final int configurationId, 
		Integer questionId, String questionType, 
		Locale studyLocale) 
	{
		super(id, configurationId, questionId, questionType, studyLocale);
		
		getQuestionForm().add(
			new LocalizedStringReorderableListPanel(
				"valueListPanel", 
				studyIsInState(StudyState.INIT), 
				getStudyLocale(), 
				AddLink.ADD_ANSWER) 
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void delete(LocalizedString objectToDelete, List<LocalizedString> objects) 
			{
				getQuestion().removeFromPossibleAnswers(objectToDelete);
				questionDao.save(getQuestion());
			}

			@Override
			protected List<LocalizedString> getDisplayObjects() {
				return getQuestion().getPossibleAnswers();
			}

			@Override
			protected void moveDown(LocalizedString ordered, List<LocalizedString> orderedList) {
				OrderedEntityBase.moveDown(orderedList, ordered);
				questionDao.save(getQuestion());				
			}

			@Override
			protected void moveUp(LocalizedString ordered, List<LocalizedString> orderedList) {
				OrderedEntityBase.moveUp(orderedList, ordered);
				questionDao.save(getQuestion());			
				
			}

			@Override
			protected void append(LocalizedString localizedString) {
				getQuestion().addToPossibleAnswers(localizedString);
				questionDao.save(getQuestion());

			}
			
		});
	}
	
	@Override
	protected QuestionDao getQuestionDao() {
		return questionDao;
	}
}

