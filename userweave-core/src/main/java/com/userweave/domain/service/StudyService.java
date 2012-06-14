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
package com.userweave.domain.service;

import java.util.List;
import java.util.Locale;

import com.userweave.domain.LocalizedString;
import com.userweave.domain.Project;
import com.userweave.domain.Study;
import com.userweave.domain.StudyState;
import com.userweave.domain.SurveyExecution;
import com.userweave.domain.User;

public interface StudyService {
	
	public Study finalizeStudy(Study study);
	
	public void delete(Study study);

	public void restore(Study study);
	
	public void purge(Integer studyId);
	
	public void purge(Study study);
	
	//public void purge(User user);
	
	public void purge(Project project);
	
	public Study copy(Study study, String newName, Project parentProject);

	public SurveyExecution createSurveyExecution(Study study, String hashCode);
	
	public void sort(List<Study> studies);

	//public List<Study> findByOwner(User user, boolean alsoDeleted);

	//public List<Study> findByOwner(User user);

	//public List<Study> findByOwnerAndState(User user, boolean alsoDeleted, StudyState state);
	
	//public List<Study> findByOwnerAndState(User user, StudyState state);
	
	public List<Study> findDeletedStudies(User user);
	
	public List<Integer> findAgedDeletedStudies();

	public List<Integer> findAll();

	public Study load(int studyId);
	
	public Study createPreConfiguredPresentationStudy(LocalizedString imgUrl, Locale locale, String studyName);
	
	public Study preConfigureStudy(Project parentProject);
	
	// project specific
	public List<Study> findByProject(Project project, boolean alsoDeleted);

	public List<Study> findByProject(Project project);

	public List<Study> findByProjectAndState(Project project, boolean alsoDeleted, StudyState state);
	
	public List<Study> findByProjectAndState(Project project, StudyState state);
	
	public List<Study> findByProjectIdAndState(int projectId, boolean alsoDeleted, StudyState state);
	
	public List<Study> findDeletedStudies(Project project);
	
	public boolean isAtLeastOneAdminRegistered(Project project);
	
	/**
	 * For faster access to study results, the questionnaire answers
	 * are processed in new tables.
	 * 
	 * @param study
	 */
	public void generateStudyResults(Integer studyId);

}
