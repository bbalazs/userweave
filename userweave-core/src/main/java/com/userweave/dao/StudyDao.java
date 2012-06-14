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

import com.userweave.domain.Project;
import com.userweave.domain.Study;
import com.userweave.domain.StudyState;
import com.userweave.domain.User;

public interface StudyDao extends BaseDao<Study> {

	public Study findByHashcode(String hashCode);
	
	public Study findByReportCode(String hashCode);
	
	//public List<Study> findByOwner(User owner);

	public List<Study> findAgedDeletedStudies();

	public List<Study> findByOwnerAndState(User user, boolean alsoDeleted, StudyState state);
	
	public List<Study> findByProjectAndState(Project project, boolean alsoDeleted, StudyState state);
	
	public List<Study> findByProjectIdAndState(int projectId, boolean alsoDeleted, StudyState state);
	
	public List<Study> findDeletedStudies(User user);
	
	public List<Study> findDeletedStudies(Project project);
}
