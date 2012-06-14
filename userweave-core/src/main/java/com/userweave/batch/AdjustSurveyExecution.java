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
package com.userweave.batch;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;

import com.userweave.dao.SurveyExecutionDao;
import com.userweave.domain.ModuleConfigurationWithResultsEntity;
import com.userweave.domain.SurveyExecution;
import com.userweave.domain.TestResultEntityBase;
import com.userweave.module.Module;
import com.userweave.module.ModuleConfiguration;

public class AdjustSurveyExecution {

	@Resource(name = "surveyExecutionDaoImpl")
	private SurveyExecutionDao surveyExecutionDao;

	@Resource(name = "modules")
	private List<Module<?>> modules;

	@Transactional
	public List<Integer> getSurveyExecutions() {
		List<SurveyExecution> surveys = surveyExecutionDao.findAll();
		Set<Integer> result = new TreeSet<Integer>();
		for (SurveyExecution survey : surveys) {
			if (!survey.executionFinishedIsSet()) {
				result.add(survey.getId());
			}
		}
		return new ArrayList<Integer>(result);
	}

	@Transactional
	public boolean checkAndsetExecutionFinished(int id) {
		SurveyExecution survey = surveyExecutionDao.findById(id);
		if (survey != null && !survey.executionFinishedIsSet()
				&& isCompleted(survey, modules)) {
			survey.setExecutionFinished(new Date());
			surveyExecutionDao.save(survey);
			return true;
		} else {
			return false;
		}
	}

	@Transactional
	private boolean isCompleted(SurveyExecution survey, List<Module<?>> modules) {
		List<ModuleConfiguration> moduleConfigurations = survey.getStudy()
				.getActiveModuleConfigurations(modules);
		for (ModuleConfiguration moduleConfiguration : moduleConfigurations) {
			if (!hasResult(moduleConfiguration, survey.getId())) {
				return false;
			}
		}
		return true;
	}

	@Transactional
	public boolean hasResult(ModuleConfiguration moduleConfiguration,
			int surveyExecutionID) {
		if (moduleConfiguration instanceof ModuleConfigurationWithResultsEntity<?, ?>) {
			for (TestResultEntityBase<?> result : ((ModuleConfigurationWithResultsEntity<?, ?>) moduleConfiguration)
					.getResults()) {
				if (resultBelongsToSurvey(surveyExecutionID, result)) {
					return true;
				}
			}
			return false;
		} else {
			return true;
		}
	}

	@Transactional
	private boolean resultBelongsToSurvey(int surveyExecutionID,
			TestResultEntityBase<?> result) {
		SurveyExecution surveyExecution = result.getSurveyExecution();
		Integer id = surveyExecution.getId();
		return id.intValue() == surveyExecutionID;
	}

}
