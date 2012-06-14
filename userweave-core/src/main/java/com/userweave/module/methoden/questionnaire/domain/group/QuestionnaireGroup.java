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
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Transient;

import com.userweave.domain.Group;
import com.userweave.module.methoden.questionnaire.domain.question.Question;

@Entity(name="questionnaire_group")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public abstract class QuestionnaireGroup extends Group {

	private static final long serialVersionUID = -6339046498049945449L;

	@Transient
	public abstract Question getQuestion();

	@Transient
	public abstract QuestionnaireGroup copy(Question cloneQuestion);
}
