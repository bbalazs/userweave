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
package com.userweave.module.methoden.questionnaire.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;

import com.userweave.dao.EmptyFilterFunctor;
import com.userweave.dao.filter.FilterFunctor;
import com.userweave.domain.Group;
import com.userweave.domain.ModuleConfigurationWithGroups;
import com.userweave.domain.OrderedEntityBase;
import com.userweave.module.methoden.questionnaire.QuestionnaireMethod;
import com.userweave.module.methoden.questionnaire.dao.filter.DimensionsFilterFunctor;
import com.userweave.module.methoden.questionnaire.dao.filter.FreeQuestionNumberFilterFunctor;
import com.userweave.module.methoden.questionnaire.dao.filter.FreeQuestionTextFilterFunctor;
import com.userweave.module.methoden.questionnaire.dao.filter.MultipleAnswerFilterFunctor;
import com.userweave.module.methoden.questionnaire.dao.filter.MultipleRatingFilterFunctor;
import com.userweave.module.methoden.questionnaire.dao.filter.SingleAnswerFilterFunctor;
import com.userweave.module.methoden.questionnaire.domain.group.DimensionsGroup;
import com.userweave.module.methoden.questionnaire.domain.group.FreeQuestionNumberGroup;
import com.userweave.module.methoden.questionnaire.domain.group.FreeQuestionTextGroup;
import com.userweave.module.methoden.questionnaire.domain.group.MultipleAnswersGroup;
import com.userweave.module.methoden.questionnaire.domain.group.MultipleRatingGroup;
import com.userweave.module.methoden.questionnaire.domain.group.QuestionnaireGroup;
import com.userweave.module.methoden.questionnaire.domain.group.SingleAnswerGroup;
import com.userweave.module.methoden.questionnaire.domain.question.Question;

@Entity
@Table(name="questionnaire_configuration")
public class QuestionnaireConfigurationEntity 
	extends ModuleConfigurationWithGroups<QuestionnaireConfigurationEntity, QuestionnaireResult, QuestionnaireGroup> {

	private static final long serialVersionUID = 1L;

	@Override
	@Transient
	protected String getSpringApplicationContextName() {
		return QuestionnaireMethod.moduleId;
	}	

	private List<Question> questions;

	@OneToMany(mappedBy="configuration")
	@Cascade(value={org.hibernate.annotations.CascadeType.ALL})
	@OrderBy(value="position")
	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}
	
	public void addToQuestions(Question question) {
		this.questions.add(question);
		OrderedEntityBase.renumberPositions(questions);
	}

	public void removeFromQuestions(Question question) {
		this.questions.remove(question);
		OrderedEntityBase.renumberPositions(questions);
	}
	
	@Transient
	public int getMaxQuestionPosition() {
		int rv = 0;
		for (Question question : getQuestions()) {
			Integer currentPosition = question.getPosition();
			if ((currentPosition != null) && (currentPosition > rv)) {
				rv = currentPosition;
			}
		}
		return rv;
	}
	
	public void removeFromQuestions(Integer questionId) {
		Question questionToRemove = null;
		if (!getQuestions().isEmpty()) {
			for (Question question : getQuestions()) {
				if (question.getId().equals(questionId)) {
					questionToRemove = question;
					break;
				}
			}
		}
		if (questionToRemove != null) {
			removeFromQuestions(questionToRemove);
		}
	}
	
//	@Override
//	@Transient
//	public List<LocalizedString> getLocalizedStrings() {
//		List<LocalizedString> rv = super.getLocalizedStrings();
//		for (Question question : getQuestions()) {
//			rv.addAll(question.getLocalizedStrings());
//		}
//		return rv;
//	}
	
	@Override
	@Transient
	public FilterFunctor getFilterFunctorForGroup(Group group) {
		if (group instanceof SingleAnswerGroup) {
			return new SingleAnswerFilterFunctor((SingleAnswerGroup) group);
		} if (group instanceof MultipleAnswersGroup) {
			return new MultipleAnswerFilterFunctor((MultipleAnswersGroup) group);
		} if (group instanceof MultipleRatingGroup) {
			return new MultipleRatingFilterFunctor((MultipleRatingGroup) group);
		} if (group instanceof DimensionsGroup) {
			return new DimensionsFilterFunctor((DimensionsGroup) group);
		} if (group instanceof FreeQuestionNumberGroup) {
			return new FreeQuestionNumberFilterFunctor((FreeQuestionNumberGroup) group);
		} if (group instanceof FreeQuestionTextGroup) {
			return new FreeQuestionTextFilterFunctor((FreeQuestionTextGroup) group);
		}
		else {
			return new EmptyFilterFunctor();
		}
	}

	@Override
	@Transient
	public QuestionnaireConfigurationEntity copy() {
		QuestionnaireConfigurationEntity clone = new QuestionnaireConfigurationEntity();
		super.copy(clone);
		
		if(questions != null) {
			List<Question> cloneQuestions = new ArrayList<Question>();
			for(Question question : questions) {
				Question cloneQuestion = question.copy();
				cloneQuestions.add(cloneQuestion);
				// remark: both sides have knowledge about the 
				// configuration so we must adjust it here
				cloneQuestion.setConfiguration(clone);
				
				/* FIXME(pavkovic 2009.06.22): this MUST be reenabled later, but copy does not help us here as we have to use some references that yet not
				  have been created in time 
				// adjust questions in clones QuestionnaireGroup
				for(QuestionnaireGroup group : clone.getGroups()) {
					if(group.getQuestion().equals(question)) {
						group.setQuestion(cloneQuestion);
						if(group instanceof MultipleRatingGroup) {
							// also adjust RatingTerm in MultipleRatingGroup
							MultipleRatingGroup mrg = (MultipleRatingGroup) group;
							MultipleRatingQuestion mrCloneQuestion = (MultipleRatingQuestion) cloneQuestion;
							int idx = mrg.getMultipleRatingQuestion().getRatingTerms().indexOf(mrg.getRatingTerm());
							mrg.setRatingTerm(mrCloneQuestion.getRatingTerms().get(idx));
						}
					}
				}
				*/
			}
			clone.setQuestions(cloneQuestions);
			
			if(getGroups() != null) {
				List<QuestionnaireGroup> cloneGroups = new ArrayList<QuestionnaireGroup>(); 
				for(QuestionnaireGroup group : getGroups()) {
					int idx = questions.indexOf(group.getQuestion());
					Question cloneQuestion = clone.getQuestions().get(idx);
					QuestionnaireGroup cloneGroup = group.copy(cloneQuestion);
					if(cloneGroup != null) {
						cloneGroups.add(cloneGroup);
					}
				}
				clone.setGroups(cloneGroups);
			}
		}
		
		return clone;
	}
}
