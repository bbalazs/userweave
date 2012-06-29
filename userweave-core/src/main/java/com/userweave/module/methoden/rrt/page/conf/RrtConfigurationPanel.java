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
package com.userweave.module.methoden.rrt.page.conf;

import java.util.Locale;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.dao.StudyDependendDao;
import com.userweave.module.methoden.rrt.dao.RrtConfigurationDao;
import com.userweave.module.methoden.rrt.domain.RrtConfigurationEntity;
import com.userweave.pages.configuration.module.ModuleConfigurationBaseUI;

/**
 * @author oma
 */
public class RrtConfigurationPanel extends ModuleConfigurationBaseUI<RrtConfigurationEntity> 
{
	private static final long serialVersionUID = 1L;

	public RrtConfigurationPanel(String id, Integer configurationId, Locale studyLocale) 
	{
		super(id, configurationId);
		
		addFormComponent(new RrtPrefixPostfixConfigurationPanel("prefixpostfix", configurationId, studyLocale));
		
		addFormComponent(new RrtTermConfigurationPanel("terms", configurationId, studyLocale));		
	}

	@SpringBean
	private RrtConfigurationDao dao;

	@Override
	protected StudyDependendDao<RrtConfigurationEntity> getBaseDao() 
	{
		return dao;
	}
	
	@Override
	protected StudyDependendDao<RrtConfigurationEntity> getConfigurationDao() 
	{
		return dao;
	}

	@Override
	protected IModel getTypeModel()
	{
		return new StringResourceModel("rrt_type", RrtConfigurationPanel.this, null);
	}
}

