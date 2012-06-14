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

import org.apache.wicket.model.IModel;

import com.userweave.domain.Study;


@SuppressWarnings("serial")
public class StudyGroupType implements GroupType {
		
	private final IModel name;
	
	private final String id;
	
	private final Study study;

	public StudyGroupType(IModel name, String id, Study study) {
		super();
		this.name = name;
		this.id = id;
		this.study = study;
	}

	public Study getStudy() {
		return study;
	}

	public IModel getName() {
		return name;
	}

	public String getId() {
		return id;
	}
}
