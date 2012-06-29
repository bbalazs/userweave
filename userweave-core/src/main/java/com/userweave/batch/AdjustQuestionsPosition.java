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
package com.userweave.batch;

import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.userweave.domain.OrderedEntityBase;
import com.userweave.module.methoden.questionnaire.dao.QuestionDao;
import com.userweave.module.methoden.questionnaire.dao.QuestionnaireConfigurationDao;
import com.userweave.module.methoden.questionnaire.domain.QuestionnaireConfigurationEntity;
import com.userweave.module.methoden.questionnaire.domain.question.Question;

public class AdjustQuestionsPosition
{
	private final static Logger logger = LoggerFactory.getLogger(AdjustQuestionsPosition.class);
	
	public static void main(String[] args)
	{
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-batch.xml");
		
		QuestionnaireConfigurationDao configurationDao = 
			(QuestionnaireConfigurationDao) context.getBean("questionnaireConfigurationDaoImpl");
	
		QuestionDao questionDao = (QuestionDao) context.getBean("questionDaoImpl");
		
		startAdjustment(configurationDao, questionDao);
	}
	
	//@Transactional
	private static void startAdjustment(
		QuestionnaireConfigurationDao configurationDao, QuestionDao questionDao)
	{
		List<QuestionnaireConfigurationEntity> list = configurationDao.findAll();
		
		Iterator<QuestionnaireConfigurationEntity> i = list.iterator();
		
		while(i.hasNext())
		{
			QuestionnaireConfigurationEntity conf = i.next();
			
			String name = conf.getStudy().getName() + " - " + conf.getName();
			
			logger.info("------ retrieve questions of " + name + " ------");
			
			List<Question> questions = questionDao.findByConfiguration(conf.getId());
			
			if(questions != null && !questions.isEmpty())
			{
				if (questions.get(0).getPosition() != 0)
				{
					logger.info("------ conf " + name
							+ " has old positions ------");

					OrderedEntityBase.renumberPositions(questions);

					for (Question q : questions)
					{
						questionDao.save(q);
					}
				}
			}
			else
			{
				logger.info("------ " + name + " has no questions! ------");
			}
			
		}
	}
}
