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
package com.userweave.dao;

import java.util.List;

import com.userweave.dao.filter.FilterFunctor;
import com.userweave.domain.Study;
import com.userweave.domain.SurveyExecution;

public interface SurveyExecutionDao extends StudyDependendDao<SurveyExecution> {
	public List<SurveyExecution> findByStudy(Study study);

	public SurveyExecution findByHashcode(String hashCode);

	public List<SurveyExecution> findByStudy(Study study, FilterFunctor filterFunctor);

	public List<SurveyExecution> findFinishedByStudy(Study study, FilterFunctor filterFunctor);
}
