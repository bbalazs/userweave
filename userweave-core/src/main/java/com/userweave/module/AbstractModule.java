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
package com.userweave.module;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.springframework.beans.factory.annotation.Autowired;

import com.userweave.dao.StudyDao;
import com.userweave.dao.StudyDependendDao;
import com.userweave.dao.TestResultDao;
import com.userweave.dao.filter.FilterFunctor;
import com.userweave.domain.ModuleConfigurationWithResultsEntity;
import com.userweave.domain.Study;
import com.userweave.domain.service.GeneralStatistics;

public abstract class AbstractModule<T extends ModuleConfiguration<?>> implements Module<T> {

	protected void init(String name, String shortDescription) {
		this.name = name;
		this.shortDescription = shortDescription;
	}
	
	// name of the module 
	private String name ="NOT CONFIGURED";

	@Override
	public String getName() {
		return name;
	}
	
	private String shortDescription = "NOT CONFIGURED";

	@Override	
	public String getShortDescription() {
		return shortDescription;
	}
	
	// dao to read study
	@Autowired
	private StudyDao studyDao;

	private StudyDao getStudyDao() {
		return studyDao;
	}
	
	@Override
	public boolean storesResults() {
		return true;
	}

	protected abstract T createConfiguration();
	
	@Override
	public T create(int studyId, Integer position) {
		T configuration = createConfiguration();
		configuration.setStudy(getStudyDao().findById(studyId));
		configuration.setPosition(position);
		getConfigurationDao().save(configuration);
		return configuration;
	}
	
	public void save(T configuration) {
		getConfigurationDao().save(configuration);
	}

	public void delete(T configuration) {
		getConfigurationDao().delete(configuration);
	}

	protected abstract StudyDependendDao<T> getConfigurationDao();
	
	protected abstract TestResultDao getTestResultDao();	
	
	@Override
	public GeneralStatistics getValidResultStatistics(
			ModuleConfigurationWithResults moduleConfiguration,
			FilterFunctor filter) {
		return getTestResultDao()
			.findValidResultStatistics((ModuleConfigurationWithResultsEntity) moduleConfiguration, filter);
	}

	@Override
	public List<T> getConfigurations(Study studie) {	
		return getConfigurationDao().findByStudy(studie);
	}
	
	public Component getGroupingUI(String id, T configuration) {
		return new WebMarkupContainer(id);		
	};
	
	@Override
	public boolean hasGroupingUI() {
		return false;
	}
}
