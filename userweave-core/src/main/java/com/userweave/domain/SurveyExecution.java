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
package com.userweave.domain;

import java.util.Date;
import java.util.Locale;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.userweave.domain.util.HashProvider;

@Entity
public class SurveyExecution extends EntityBase {

	private static final long serialVersionUID = 6341300469827543379L;

	private Study study;

	@ManyToOne(cascade=CascadeType.ALL)
	//@OnDelete(action=OnDeleteAction.CASCADE)
	public Study getStudy() {
		return study;
	}

	public void setStudy(Study study) {
		this.study = study;
	}

	private Date executionStarted = null;

	public Date getExecutionStarted() {
		return executionStarted;
	}

	public void setExecutionStarted(Date date) {
		this.executionStarted = date;
	}

	@Transient
	public boolean executionStartedIsSet() {
		return executionStarted != null;
	}

	private Date executionFinished = null;

	public Date getExecutionFinished() {
		return executionFinished;
	}

	public void setExecutionFinished(Date executionFinished) {
		this.executionFinished = executionFinished;
	}

	@Transient
	public boolean executionFinishedIsSet() {
		return executionFinished != null;
	}

	private String hashCode;

	public String getHashCode() {
		return hashCode;
	}

	public void setHashCode(String hashCode) {
		this.hashCode = hashCode;
	}

	@Transient
	public void createHashCode() {
		this.setHashCode(HashProvider.uniqueUUID());
	}

	@Transient
	public boolean isCompleted() {
		return executionFinishedIsSet();
	}

	@Transient
	public boolean isStarted() {
		return executionStartedIsSet();
	}

	private StudyState studyState = null;

	/**
	 * @return the state of the study while this survey execution was started
	 * 
	 */
	@Enumerated
	public StudyState getStudyState() {
		return studyState;
	}

	public void setStudyState(StudyState studyState) {
		this.studyState = studyState;
	}

	private Locale locale;
	
	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	
	private SurveyExecutionState state;

	@Enumerated
	public SurveyExecutionState getState() {
		return state;
	}

	public void setState(SurveyExecutionState state) {
		this.state = state;
	}

	private Integer modulesExecuted;

	public Integer getModulesExecuted() {
		return modulesExecuted;
	}

	public void setModulesExecuted(Integer modulesExecuted) {
		this.modulesExecuted = modulesExecuted;
	}

	private String remoteAddr;

	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}
	
	public String getRemoteAddr() {
		return remoteAddr;
	}
	
	@Transient
	public boolean executedWhileStudyInRunningState() { 
		return getStudyState().equals(StudyState.RUNNING);
	}

	@Transient 
	public boolean setCompletionState() {
		if(getState() != null) {
			// never overwrite known state
			return false;
		}
		setState(evaluateCompletionState());
		return true;
	}

	@Transient 
	private SurveyExecutionState evaluateCompletionState() {
		if(!executedWhileStudyInRunningState()) {
			return SurveyExecutionState.INVALID;
		}
		if (isCompleted()) {
			return SurveyExecutionState.COMPLETED;
		}
		if(isStarted()) {
			return SurveyExecutionState.STARTED;
		}
		return SurveyExecutionState.NOT_STARTED;
	}
}
