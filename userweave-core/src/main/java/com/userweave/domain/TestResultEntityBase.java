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

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;


@MappedSuperclass
public class TestResultEntityBase<T extends ModuleConfigurationEntityBase> extends EntityBase {
	
	private static final long serialVersionUID = 1L;
	
	// belongs to configuration
	private T configuration;
	
	@ManyToOne
	public T getConfiguration() {
		return configuration;
	}

	public void setConfiguration(T configuration) {
		this.configuration = configuration;  
	}
	
	// belongs to survey execution
	private SurveyExecution surveyExecution;
	
	@ManyToOne
	@JoinColumn(name = "surveyExecution_id")
	public SurveyExecution getSurveyExecution() {
		return surveyExecution;
	}

	public void setSurveyExecution(SurveyExecution surveyExecution) {
		this.surveyExecution = surveyExecution;
	}

	// store start time
	private Date executionStarted = null;

	public Date getExecutionStarted() {
		return executionStarted;
	}

	public void setExecutionStarted(Date date) {
		this.executionStarted = date;
	}

	// store finish time
	private Date executionFinished = null;

	public void setExecutionFinished(Date date) {
		this.executionFinished = date;
	}
	
	public Date getExecutionFinished() {
		return executionFinished;
	}
	
	// store execution time
	private Long executionTime = null;

	public void setExecutionTime(Long executionTime) {
		this.executionTime= executionTime;
	}
	
	public Long getExecutionTime() {
		return executionTime;
	}
	
}
