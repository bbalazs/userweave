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
package com.userweave.module.methoden.questionnaire.domain.group;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.userweave.module.methoden.questionnaire.domain.question.FreeQuestion;
import com.userweave.module.methoden.questionnaire.domain.question.Question;

@Entity
public class FreeQuestionTextGroup extends QuestionnaireGroup 
{
	private static final long serialVersionUID = -3028797134679555636L;
	
	private FreeQuestion question;
	
	private String filterText;
	
	private String choice;
	
	@ManyToOne
	public FreeQuestion getFreeQuestion()
	{
		return question;
	}
	
	public void setFreeQuestion(FreeQuestion question)
	{
		this.question = question;
	}
	
	public String getFilterText() 
	{
		return filterText;
	}

	public void setFilterText(String filterText) 
	{
		this.filterText = filterText;
	}

	public String getChoice() 
	{
		return choice;
	}

	public void setChoice(String choice) 
	{
		this.choice = choice;
	}

	@Override
	@Transient
	public QuestionnaireGroup copy(Question cloneQuestion) 
	{
		FreeQuestionTextGroup clone = new FreeQuestionTextGroup();
		
		super.copy(clone);
		
		clone.setFreeQuestion((FreeQuestion)cloneQuestion);
		
		return clone;
	}

	@Override
	@Transient
	public FreeQuestion getQuestion() 
	{
		return getFreeQuestion();
	}
}
