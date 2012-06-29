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
package com.userweave.pages.configuration.study;

import java.util.Locale;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.dao.StudyDao;
import com.userweave.domain.Study;
import com.userweave.presentation.model.SpringLoadableDetachableModel;

/**
 * Base view class to configure a study.
 * 
 * @author opr
 */
public class StudyConfigurationPanelBase extends Panel 
{
	private static final long serialVersionUID = 1L;

	@SpringBean
	private StudyDao studyDao;
	
	/**
	 * Default constructor.
	 * 
	 * @param id
	 * 		Component id.
	 * @param studyId
	 * 		Id of study to configure.
	 */
	public StudyConfigurationPanelBase(String id, final int studyId) 
	{
		super(id);	
		
		setOutputMarkupId(true);
		
		setDefaultModel(
			new CompoundPropertyModel(
				new SpringLoadableDetachableModel(studyDao, studyId)));			
	}

	/**
	 * Get the underlying model of this panel.
	 * 
	 * @return
	 */
	protected IModel getStudyModel() 
	{
		return getDefaultModel();
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
