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

import java.io.Serializable;
import java.util.Locale;

import org.apache.wicket.model.IModel;

import com.userweave.components.model.LocalizedPropertyModel;
import com.userweave.module.methoden.questionnaire.domain.question.Question;

@SuppressWarnings("serial")
public class QuestionnaireGroupType implements GroupType, Serializable{
	
	public QuestionnaireGroupType(Question question, Locale locale) {
		super();
		this.question = question;
		this.locale = locale;
	}

	private final Locale locale;
	
	private final Question question;

	public Question getQuestion() {
		return question;
	}

	@Override
	public String getId() {
		return Integer.toString(question.getId());
	}

	@Override
	public IModel getName() {
		return new LocalizedPropertyModel(question, "name", locale);
	}
		
}
