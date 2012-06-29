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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;

import com.userweave.dao.filter.FilterFunctor;
import com.userweave.domain.service.GeneralStatistics;
import com.userweave.module.ModuleConfiguration;
import com.userweave.module.ModuleConfigurationWithResults;

@MappedSuperclass
public abstract class ModuleConfigurationWithResultsEntity<T extends ModuleConfiguration, U extends TestResultEntityBase> extends ModuleConfigurationEntityBase<T> implements ModuleConfigurationWithResults {

	private static final long serialVersionUID = -3794976458527398560L;

	private List<U> results = new ArrayList<U>();

	@OneToMany(mappedBy="configuration")
	@Cascade(value={org.hibernate.annotations.CascadeType.ALL})
	public List<U> getResults() {
		return results;
	}
	
	/* (non-Javadoc)
	 * @see com.userweave.domain.ModlueXXX#getValidResults()
	 */
	@Transient
	public List<U> getValidResults() {
		List<U> res = new ArrayList<U>();
		for(U result: getResults()) {
			if(result.getSurveyExecution().executedWhileStudyInRunningState()) {
				res.add(result);
			}
		}
		return res;
	}

	@Transient
	public GeneralStatistics getValidResultStatistics(FilterFunctor filter) {
		return getModule().getValidResultStatistics(this, filter);
	}

	void addToResults(U result) {
		results.add(result);
	}

	void removeFromResults(U result) {
		results.remove(result);
	}

	void setResults(List<U> results) {
		this.results = results;
	}

}

