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
package com.userweave.config;

import org.hibernate.MappingException;
import org.springframework.context.ApplicationContextException;

import com.userweave.domain.ApiCredentials;
import com.userweave.domain.Consideration;
import com.userweave.domain.ImageBase;
import com.userweave.domain.Invitation;
import com.userweave.domain.Invoice;
import com.userweave.domain.LocalizedString;
import com.userweave.domain.LogEntry;
import com.userweave.domain.ModuleReachedGroup;
import com.userweave.domain.Project;
import com.userweave.domain.ProjectInvitation;
import com.userweave.domain.ProjectUserRoleJoin;
import com.userweave.domain.Role;
import com.userweave.domain.SecurityToken;
import com.userweave.domain.Study;
import com.userweave.domain.StudyGroup;
import com.userweave.domain.StudyLocalesGroup;
import com.userweave.domain.Suitor;
import com.userweave.domain.SurveyExecution;
import com.userweave.domain.User;
import com.userweave.module.methoden.freetext.domain.FreeTextConfigurationEntity;
import com.userweave.module.methoden.freetext.domain.FreeTextResult;
import com.userweave.module.methoden.iconunderstandability.domain.ITMImage;
import com.userweave.module.methoden.iconunderstandability.domain.ITMTestResult;
import com.userweave.module.methoden.iconunderstandability.domain.IconTermMatchingConfigurationEntity;
import com.userweave.module.methoden.iconunderstandability.domain.ItmTerm;
import com.userweave.module.methoden.iconunderstandability.domain.ScoreRangeList;
import com.userweave.module.methoden.iconunderstandability.domain.report.IconTestReactionTimeStatistics;
import com.userweave.module.methoden.iconunderstandability.page.survey.IconTermMapping;
import com.userweave.module.methoden.mockup.domain.MockupConfigurationEntity;
import com.userweave.module.methoden.mockup.domain.MockupResult;
import com.userweave.module.methoden.questionnaire.domain.QuestionWithMultiplePossibleAnswers;
import com.userweave.module.methoden.questionnaire.domain.QuestionnaireConfigurationEntity;
import com.userweave.module.methoden.questionnaire.domain.QuestionnaireResult;
import com.userweave.module.methoden.questionnaire.domain.answer.Answer;
import com.userweave.module.methoden.questionnaire.domain.answer.AnswerToSingleAnswerQuestion;
import com.userweave.module.methoden.questionnaire.domain.answer.FreeAnwer;
import com.userweave.module.methoden.questionnaire.domain.answer.FreeNumberAnswer;
import com.userweave.module.methoden.questionnaire.domain.answer.FreeTextAnswer;
import com.userweave.module.methoden.questionnaire.domain.answer.MultipleAnswersAnwer;
import com.userweave.module.methoden.questionnaire.domain.answer.MultipleDimensionsAnswer;
import com.userweave.module.methoden.questionnaire.domain.answer.MultipleRatingAnswer;
import com.userweave.module.methoden.questionnaire.domain.answer.SingleDimensionAnswer;
import com.userweave.module.methoden.questionnaire.domain.answer.SingleRatingAnswer;
import com.userweave.module.methoden.questionnaire.domain.group.DimensionsGroup;
import com.userweave.module.methoden.questionnaire.domain.group.FreeQuestionNumberGroup;
import com.userweave.module.methoden.questionnaire.domain.group.FreeQuestionTextGroup;
import com.userweave.module.methoden.questionnaire.domain.group.MultipleAnswersGroup;
import com.userweave.module.methoden.questionnaire.domain.group.MultiplePossibleAnswersGroup;
import com.userweave.module.methoden.questionnaire.domain.group.MultipleRatingGroup;
import com.userweave.module.methoden.questionnaire.domain.group.QuestionnaireGroup;
import com.userweave.module.methoden.questionnaire.domain.group.SingleAnswerGroup;
import com.userweave.module.methoden.questionnaire.domain.question.AntipodePair;
import com.userweave.module.methoden.questionnaire.domain.question.DimensionsQuestion;
import com.userweave.module.methoden.questionnaire.domain.question.FreeQuestion;
import com.userweave.module.methoden.questionnaire.domain.question.MultipleAnswersQuestion;
import com.userweave.module.methoden.questionnaire.domain.question.MultipleRatingQuestion;
import com.userweave.module.methoden.questionnaire.domain.question.Question;
import com.userweave.module.methoden.questionnaire.domain.question.RatingTerm;
import com.userweave.module.methoden.questionnaire.domain.question.SingleAnswerQuestion;
import com.userweave.module.methoden.rrt.domain.OrderedTerm;
import com.userweave.module.methoden.rrt.domain.RrtConfigurationEntity;
import com.userweave.module.methoden.rrt.domain.RrtResult;
import com.userweave.module.methoden.rrt.domain.RrtTerm;

@SuppressWarnings("serial")
public class AnnotationConfiguration extends org.hibernate.cfg.AnnotationConfiguration {

	public AnnotationConfiguration() {
		super();
    	try {
			// Platform
    		addAnnotatedClass(ProjectUserRoleJoin.class);
    		addAnnotatedClass(Project.class);
	    	addAnnotatedClass(Study.class);
	    	addAnnotatedClass(ImageBase.class);
	    	addAnnotatedClass(SurveyExecution.class);
	    	addAnnotatedClass(User.class);
	    	addAnnotatedClass(Role.class);
	    	addAnnotatedClass(LocalizedString.class);
	    	addAnnotatedClass(StudyGroup.class);
	    	addAnnotatedClass(StudyLocalesGroup.class);            	
	    	addAnnotatedClass(ModuleReachedGroup.class);

	    	addAnnotatedClass(LogEntry.class);
	    	
	    	addAnnotatedClass(SecurityToken.class);
	    	addAnnotatedClass(Invitation.class);
	    	addAnnotatedClass(Suitor.class);
	    	
	    	addAnnotatedClass(ProjectInvitation.class);
	    	
	    	// Payment
	    	addAnnotatedClass(Consideration.class);
	    	addAnnotatedClass(Invoice.class);
	    	
	    	// ITM
	    	addAnnotatedClass(IconTermMatchingConfigurationEntity.class);
			addAnnotatedClass(ITMImage.class);
			addAnnotatedClass(ItmTerm.class);
			addAnnotatedClass(ITMTestResult.class);
			addAnnotatedClass(IconTermMapping.class);
			addAnnotatedClass(ScoreRangeList.class);
			addAnnotatedClass(IconTestReactionTimeStatistics.class);
			
			// Free-Text
			addAnnotatedClass(FreeTextConfigurationEntity.class);
			addAnnotatedClass(FreeTextResult.class);
			
			// Rangreihen
			addAnnotatedClass(RrtConfigurationEntity.class);
			addAnnotatedClass(RrtTerm.class);
			addAnnotatedClass(RrtResult.class);
			addAnnotatedClass(OrderedTerm.class);
			
			// Questionnaire				
			addAnnotatedClass(QuestionnaireConfigurationEntity.class);
			addAnnotatedClass(QuestionWithMultiplePossibleAnswers.class);
			addAnnotatedClass(MultipleAnswersQuestion.class);
			addAnnotatedClass(SingleAnswerQuestion.class);
			addAnnotatedClass(FreeQuestion.class);
			addAnnotatedClass(MultipleRatingQuestion.class);
			addAnnotatedClass(Question.class);
		
			addAnnotatedClass(RatingTerm.class);										  
			addAnnotatedClass(QuestionnaireGroup.class);
			addAnnotatedClass(SingleAnswerGroup.class);
			addAnnotatedClass(MultipleAnswersGroup.class);
			addAnnotatedClass(MultiplePossibleAnswersGroup.class);
			addAnnotatedClass(MultipleRatingGroup.class);
			addAnnotatedClass(DimensionsGroup.class);
			addAnnotatedClass(FreeQuestionNumberGroup.class);
			addAnnotatedClass(FreeQuestionTextGroup.class);
			
			addAnnotatedClass(QuestionnaireResult.class);
			addAnnotatedClass(AnswerToSingleAnswerQuestion.class);
			addAnnotatedClass(Answer.class);
			addAnnotatedClass(MultipleAnswersAnwer.class);
			addAnnotatedClass(MultipleRatingAnswer.class);
			
			addAnnotatedClass(FreeAnwer.class);
			addAnnotatedClass(FreeTextAnswer.class);
			addAnnotatedClass(FreeNumberAnswer.class);		
			addAnnotatedClass(SingleRatingAnswer.class);
			addAnnotatedClass(AntipodePair.class);
			
			addAnnotatedClass(DimensionsQuestion.class);
			addAnnotatedClass(SingleDimensionAnswer.class);				
			addAnnotatedClass(MultipleDimensionsAnswer.class);

			// Mockup
			addAnnotatedClass(MockupConfigurationEntity.class);
			addAnnotatedClass(MockupResult.class);
			
			// api
			addAnnotatedClass(ApiCredentials.class);

	    }
	    catch (MappingException e) {
	        throw new ApplicationContextException("Unable to register class", e);
	    }	       

	}
}
