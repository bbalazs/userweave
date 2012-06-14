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
package com.userweave.pages.configuration.base;

import java.util.Locale;

import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.dao.StudyDao;
import com.userweave.domain.Study;

public abstract class StudyConfigurationReportPanel extends ConfigurationReportPanel
{
	private static final long serialVersionUID = 1L;

	@SpringBean
	private StudyDao studyDao;
	
	public StudyConfigurationReportPanel(String id, final int studyId)
	{
		super(id);
		
		setDefaultModel(
			new CompoundPropertyModel<Study>(
				new LoadableDetachableModel<Study>()
				{
					private static final long serialVersionUID = 1L;

					@Override
					protected Study load()
					{
						return studyDao.findById(studyId);
					}
				}));
	}
	
	/**
	 * Get the underlying model of this panel.
	 * 
	 * @return
	 */
	protected IModel<Study> getStudyModel() 
	{
		return (IModel<Study>) getDefaultModel();
	}
	
	/**
	 * Get the underlying study of this panel.
	 * 
	 * @return
	 */
	protected Study getStudy() 
	{
		return (Study) getDefaultModelObject();
	}
	
	/**
	 * Get the locale of the underlying study.
	 * 
	 * @return
	 */
	protected Locale getStudyLocale() 
	{
		return getStudy().getLocale();
	}
	
	/**
	 * Saves the underlying study.
	 */
	protected void save() 
	{
		save(getStudy());		
	}

	/**
	 * Convinient method to save.
	 * 
	 * @param study
	 * 		The study to save.
	 */
	protected void save(Study study) 
	{
		studyDao.save(study);		
	}
}
