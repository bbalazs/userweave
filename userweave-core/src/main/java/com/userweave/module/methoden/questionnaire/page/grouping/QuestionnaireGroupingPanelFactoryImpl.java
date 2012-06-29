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
package com.userweave.module.methoden.questionnaire.page.grouping;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import com.userweave.domain.Group;
import com.userweave.module.methoden.questionnaire.domain.group.DimensionsGroup;
import com.userweave.module.methoden.questionnaire.domain.group.FreeQuestionNumberGroup;
import com.userweave.module.methoden.questionnaire.domain.group.FreeQuestionTextGroup;
import com.userweave.module.methoden.questionnaire.domain.group.MultipleAnswersGroup;
import com.userweave.module.methoden.questionnaire.domain.group.MultipleRatingGroup;
import com.userweave.module.methoden.questionnaire.domain.group.QuestionnaireGroup;
import com.userweave.module.methoden.questionnaire.domain.group.SingleAnswerGroup;
import com.userweave.module.methoden.questionnaire.domain.question.DimensionsQuestion;
import com.userweave.module.methoden.questionnaire.domain.question.FreeQuestion;
import com.userweave.module.methoden.questionnaire.domain.question.MultipleAnswersQuestion;
import com.userweave.module.methoden.questionnaire.domain.question.MultipleRatingQuestion;
import com.userweave.module.methoden.questionnaire.domain.question.Question;
import com.userweave.module.methoden.questionnaire.domain.question.SingleAnswerQuestion;
import com.userweave.module.methoden.questionnaire.page.grouping.multiplepossibleanswers.MultiplePossibleAnswersGroupingDescriptionPanel;
import com.userweave.module.methoden.questionnaire.page.grouping.multiplepossibleanswers.multipleanswer.MultipleAnswerGroupingPanel;
import com.userweave.module.methoden.questionnaire.page.grouping.multiplepossibleanswers.singleanswer.SingleAnswerGroupingPanel;
import com.userweave.module.methoden.questionnaire.page.grouping.rating.RatingGroupingDescriptionPanel;
import com.userweave.module.methoden.questionnaire.page.grouping.rating.dimensions.DimensionsGroupingPanel;
import com.userweave.module.methoden.questionnaire.page.grouping.rating.freequestionnumber.FreeQuestionNumberGroupingPanel;
import com.userweave.module.methoden.questionnaire.page.grouping.rating.freequestiontext.FreeQuestionGroupingTextDescriptionPanel;
import com.userweave.module.methoden.questionnaire.page.grouping.rating.freequestiontext.FreeQuestionTextGroupingPanel;
import com.userweave.module.methoden.questionnaire.page.grouping.rating.multiplerating.MultipleRatingGroupingPanel;
import com.userweave.pages.grouping.GroupingPanel;

public class QuestionnaireGroupingPanelFactoryImpl implements QuestionnaireGroupingFactory {

	@Override
	public GroupingPanel createGroupingPanel(String id, QuestionnaireGroupType questionaireGroupType, Locale locale, GroupAddedCallback<QuestionnaireGroup> groupAddedCallback) {
		
		Question question = questionaireGroupType.getQuestion();
		
		if (question instanceof SingleAnswerQuestion) {
			return new SingleAnswerGroupingPanel(id, (SingleAnswerQuestion) question, locale, groupAddedCallback);
		} 
		else if (question instanceof MultipleAnswersQuestion) {
			return new MultipleAnswerGroupingPanel(id, (MultipleAnswersQuestion) question, locale, groupAddedCallback);
		} 
		else if (question instanceof MultipleRatingQuestion) {
			return new MultipleRatingGroupingPanel(id, (MultipleRatingQuestion) question, new MultipleRatingGroup(), locale, groupAddedCallback);					
		} 
		else if (question instanceof DimensionsQuestion) {
			return new DimensionsGroupingPanel(id, (DimensionsQuestion) question, new DimensionsGroup(), locale, groupAddedCallback);
		} 
		else if (question instanceof FreeQuestion) 
		{
			FreeQuestion q = (FreeQuestion) question;
			
			if(((FreeQuestion) question).getAnswerType().equals(FreeQuestion.AnswerType.NUMBER))
				return new FreeQuestionNumberGroupingPanel(id, (FreeQuestion) question, new FreeQuestionNumberGroup(), locale, groupAddedCallback);
			else
				return new FreeQuestionTextGroupingPanel(id, (FreeQuestion) question, new FreeQuestionTextGroup(), locale, groupAddedCallback);
		}  
		else {
			return null;
		}		
	}

	@Override
	public List<GroupType> getGroupTypes(List<Question> questions, Locale locale) {
		List<GroupType> rv = new ArrayList<GroupType>();
		
		for (Question question : questions) {
			rv.add(new QuestionnaireGroupType(question, locale));
		}
	
		return rv;
	}

	@Override
	public Panel getGroupingDescriptionPanel(IModel groupModel, Locale locale) {
		
		Group group = (Group) groupModel.getObject();
		
		if (group instanceof SingleAnswerGroup) {
			return new MultiplePossibleAnswersGroupingDescriptionPanel("group", groupModel, locale);
		} 
		else if (group instanceof MultipleAnswersGroup) {
			return new MultiplePossibleAnswersGroupingDescriptionPanel("group", groupModel, locale);
		} 
		else if (group instanceof MultipleRatingGroup) {
			return RatingGroupingDescriptionPanel.createMultipleRatingGroupingDescriptionPanel("group", groupModel, locale);
		} 
		else if (group instanceof DimensionsGroup) {
			return RatingGroupingDescriptionPanel.createDimensionsGroupingDescriptionPanel("group", groupModel, locale);
		} 
		else if (group instanceof FreeQuestionNumberGroup) {
			return RatingGroupingDescriptionPanel.createFreeQuestionNumberRatingGroupingDescriptionPanel("group", groupModel, locale);
		}
		else if (group instanceof FreeQuestionTextGroup) {
			return new FreeQuestionGroupingTextDescriptionPanel("group", groupModel, locale);
		}
		else {
			return null;
		}
	}
}
