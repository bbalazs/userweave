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
package com.userweave.domain.service.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.userweave.domain.LocalizedString;
import com.userweave.domain.service.QuestionService;
import com.userweave.module.methoden.questionnaire.dao.QuestionDao;
import com.userweave.module.methoden.questionnaire.dao.QuestionnaireConfigurationDao;
import com.userweave.module.methoden.questionnaire.dao.QuestionnaireGroupDao;
import com.userweave.module.methoden.questionnaire.domain.QuestionnaireConfigurationEntity;
import com.userweave.module.methoden.questionnaire.domain.group.QuestionnaireGroup;
import com.userweave.module.methoden.questionnaire.domain.question.Question;
import com.userweave.module.methoden.questionnaire.page.QuestionConfigurationPanelFactory;

@Service
public class QuestionServiceImpl implements QuestionService {

	@Autowired
	private QuestionnaireConfigurationDao dao;

	@Autowired
	private QuestionnaireGroupDao groupDao;
	
	@Autowired
	private QuestionDao questionDao;
	
	public void saveQuestion(int configurationId, Question question) {		
		QuestionnaireConfigurationEntity configuration = dao.findById(configurationId);
		if (configuration != null) {
			if (question != null) {
				question.setConfiguration(configuration);
				questionDao.save(question);
			}
		}		
	}

	public void copyQuestionInConfiguration(int configurationId, Question question)
	{
		QuestionnaireConfigurationEntity configuration = dao.findById(configurationId);
		
		if (configuration != null) 
		{
			if (question != null) 
			{
				configuration.addToQuestions(question);
				
				question.setConfiguration(configuration);
				
				questionDao.save(question);
			}
		}		
	}
	
	public Question createQuestion(int configurationId, String name, String questionType) {
		QuestionnaireConfigurationEntity configuration = dao.findById(configurationId);

		Question question = new QuestionConfigurationPanelFactory().createQuestion(questionType);
		
		LocalizedString nameString = new LocalizedString();
		nameString.setValue(name, configuration.getStudy().getLocale());
		question.setName(nameString);
		
		question.setConfiguration(configuration);
		question.setPosition(configuration.getMaxQuestionPosition()+1);
		questionDao.save(question);
		return question;
	}
	
	public void deleteQuestion(int questionId) {
		
		Question question = questionDao.findById(questionId);
		
		question.getConfiguration().removeFromQuestions(question);
		for(QuestionnaireGroup group: new ArrayList<QuestionnaireGroup>(question.getConfiguration().getGroups())) {
			if(group.getQuestion().equals(question)) {
				question.getConfiguration().getGroups().remove(group);
				groupDao.delete(group);
			}
		}
		questionDao.delete(question);
	}

}

