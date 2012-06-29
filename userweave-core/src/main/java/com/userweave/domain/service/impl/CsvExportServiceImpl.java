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
package com.userweave.domain.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;

import org.apache.wicket.util.resource.IResourceStream;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.userweave.csv.questionnaire.QuestionnaireToCsv;
import com.userweave.csv.questionnaire.RrtToCsv;
import com.userweave.csv.table.CsvSurveyExecutionColumnGroup;
import com.userweave.csv.table.CsvTable;
import com.userweave.dao.SurveyExecutionDao;
import com.userweave.domain.Study;
import com.userweave.domain.SurveyExecution;
import com.userweave.domain.SurveyExecutionState;
import com.userweave.domain.service.CsvExportService;
import com.userweave.domain.service.ModuleService;
import com.userweave.module.ModuleConfiguration;
import com.userweave.module.methoden.questionnaire.domain.QuestionnaireConfigurationEntity;
import com.userweave.module.methoden.rrt.domain.RrtConfigurationEntity;

@Service
@Transactional
public class CsvExportServiceImpl implements CsvExportService
{
	private static final long serialVersionUID = 1L;
	
	@Resource
	private SurveyExecutionDao surveyExecutionDao;
	
	@Resource
	private ModuleService moduleService;
	
	@Override
	//public void exportStudyResultsToCsv(CsvWriter writer, Study study)
	public IResourceStream exportStudyResultsToCsv(Study study)
	{
		return export(study);
	}
	
	/**
	 * Creates the csv table in memory and writes it
	 * to the specified output stream in the dedicated
	 * writer.
	 * 
	 * @param writer
	 * 		Writer to write csv table.
	 * @param study
	 * 		Study to exprt dat from.
	 */
	@SuppressWarnings("rawtypes")
	//private void export(CsvWriter writer, Study study)
	private IResourceStream export(Study study)
	{
		CsvTable csvTable = new CsvTable();
		
		/*
		 * We need only survey executions that are started
		 * or finished.
		 */
		
		List<SurveyExecution> surveyExecutions = 
			surveyExecutionDao.findByStudy(study, null);
		
		List<SurveyExecution> finishedSurveyExecutions = 
			new ArrayList<SurveyExecution>();
		
		for(SurveyExecution exec : surveyExecutions)
		{
			if(exec.getState().equals(SurveyExecutionState.COMPLETED) || 
			   exec.getState().equals(SurveyExecutionState.STARTED))
			{
				finishedSurveyExecutions.add(exec);
			}
		}
		
		Collections.sort(finishedSurveyExecutions, new Comparator<SurveyExecution>()
		{
			@Override
			public int compare(SurveyExecution o1, SurveyExecution o2)
			{
				try
				{
					int compare = o1.getExecutionStarted().compareTo(o2.getExecutionStarted());
					return compare;
				}
				catch(Exception e)
				{
					return -1;
				}
			}
			
			
		});
		
		/*
		 * First columns of the csv table are the survey execution id
		 * and the locale, in which it was executed.
		 */
		
		CsvSurveyExecutionColumnGroup idGroup = 
			new CsvSurveyExecutionColumnGroup(
					finishedSurveyExecutions, null);
		
		csvTable.addColGroup(idGroup);
		
		/*
		 * Find all module configuratons for the given study
		 * and call for each module a dedicated method to transform
		 * the answers in table rows and columns.
		 * 
		 * @important 
		 * 		Only questionnaires and rrts are implemented.
		 */
		
		List<ModuleConfiguration> configs = getModuleConfigurations(study);
		
		for(ModuleConfiguration conf : configs)
		{
			if(conf instanceof QuestionnaireConfigurationEntity)
			{
				addResultsForQuestionnaire(
					(QuestionnaireConfigurationEntity) conf, 
					idGroup, csvTable, study.getLocale());
				
			}
			else if(conf instanceof RrtConfigurationEntity)
			{
				addResultsForRrt(
					(RrtConfigurationEntity)conf, 
					idGroup, csvTable, study.getLocale());
			}
		}
		
		return csvTable.transformToCsv(finishedSurveyExecutions.size());
		//return writeResults(response, csvTable, finishedSurveyExecutions.size());
	}
	
	@SuppressWarnings("rawtypes")
	private List<ModuleConfiguration> getModuleConfigurations(Study study)
	{	
		List<ModuleConfiguration> modules = 
			moduleService.getModuleConfigurationsForStudy(study);
		
		List<ModuleConfiguration> configs = new ArrayList<ModuleConfiguration>();
		
		for(ModuleConfiguration module : modules)
		{
			configs.add(
				moduleService.getModuleConfigurationForStudy(study, module.getId()));
		}
		
		return configs;
	}
	
	/**
	 * Create columns for a questionnaire.
	 * 
	 * @param conf
	 * 		The configuration entity.
	 * @param idGroup
	 * 		The column group, which contains the survey execution ids.
	 * @param csvTable
	 * 		The table to add result to.
	 * @param locale
	 * 		The locale to display strings in.
	 */
	private void addResultsForQuestionnaire(
		QuestionnaireConfigurationEntity conf, 
		CsvSurveyExecutionColumnGroup idGroup,
		CsvTable csvTable, Locale locale)
	{
		QuestionnaireToCsv questionnaireConverter = 
			new QuestionnaireToCsv();
		
		csvTable.addColGroup(
			questionnaireConverter.convertToCsv(conf, idGroup, locale));
	}
	
	/**
	 * Create columns for a rrt.
	 * 
	 * @param conf
	 * 		The configuration entity.
	 * @param idGroup
	 * 		The column group, which contains the survey execution ids.
	 * @param csvTable
	 * 		The table to add result to.
	 * @param locale
	 * 		The locale to display strings in.
	 */
	private void addResultsForRrt(
		RrtConfigurationEntity conf, 
		CsvSurveyExecutionColumnGroup idGroup, 
		CsvTable csvTable,  Locale locale)
	{
		RrtToCsv rrtConverter = new RrtToCsv();
		
		csvTable.addColGroup(rrtConverter.convertToCsv(conf, idGroup, locale));
	}
	
	/**
	 * Write to output stream.
	 * 
	 * @param writer
	 * 		Writer to write the csv table to the
	 * 		output stream.
	 * @param csvTable
	 * 		The csv table to wirte.
	 * @param size
	 * 		Number of survey executions. Needed to stop writer.
	 */
//	private IResourceStream writeResults(WebResponse response, CsvTable csvTable, int size)
//	//private void writeResults(CsvWriter writer, CsvTable csvTable, int size)
//	{
//		return csvTable.transformToCsv(response, size);
//	}
}
