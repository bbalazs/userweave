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
package com.userweave.module.methoden.iconunderstandability.page.conf;

import java.util.Locale;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.dao.BaseDao;
import com.userweave.dao.StudyDependendDao;
import com.userweave.module.methoden.iconunderstandability.dao.IconTermMatchingConfigurationDao;
import com.userweave.module.methoden.iconunderstandability.domain.IconTermMatchingConfigurationEntity;
import com.userweave.module.methoden.iconunderstandability.page.conf.images.ITMImagesConfigurationPanel;
import com.userweave.module.methoden.iconunderstandability.page.conf.terms.ITMTermsConfigurationPanel;
import com.userweave.pages.configuration.module.ModuleConfigurationBaseUI;

public class ItmConfPanel extends ModuleConfigurationBaseUI<IconTermMatchingConfigurationEntity>
{
	private static final long serialVersionUID = 1L;

	@SpringBean 
	private IconTermMatchingConfigurationDao dao;
	
	@Override
	protected StudyDependendDao<IconTermMatchingConfigurationEntity> getConfigurationDao()
	{
		return dao;
	}

	@Override
	protected BaseDao<IconTermMatchingConfigurationEntity> getBaseDao()
	{
		return dao;
	}
	
	public ItmConfPanel(String id, int entityId, Locale locale)
	{
		super(id, entityId);
		
		addFormComponent(new ITMTermsConfigurationPanel("itmTermsConf", entityId, locale));
		
		addFormComponent(new ITMImagesConfigurationPanel("itmConf", entityId, locale));
	}

	@Override
	protected IModel getTypeModel()
	{
		return new StringResourceModel("itm_type", ItmConfPanel.this, null);
	}
}
