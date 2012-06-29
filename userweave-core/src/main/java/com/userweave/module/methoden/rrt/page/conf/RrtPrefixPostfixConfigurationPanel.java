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

import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.ajax.form.AjaxBehaviorFactory;
import com.userweave.components.authorization.AuthOnlyTextField;
import com.userweave.components.model.LocalizedPropertyModel;
import com.userweave.dao.StudyDependendDao;
import com.userweave.domain.StudyState;
import com.userweave.module.methoden.rrt.dao.RrtConfigurationDao;
import com.userweave.module.methoden.rrt.domain.RrtConfigurationEntity;
import com.userweave.pages.configuration.module.ModuleConfigurationFormPanelBase;

/**
 * @author oma
 */
public class RrtPrefixPostfixConfigurationPanel extends 
	ModuleConfigurationFormPanelBase<RrtConfigurationEntity> 
{
	private static final long serialVersionUID = 1L;

	@SpringBean
	private RrtConfigurationDao dao;
	
	public RrtPrefixPostfixConfigurationPanel(String id, final Integer configurationId, Locale studyLocale) {
		super(id, configurationId, studyLocale);		
		
		AuthOnlyTextField prefix = 
			new AuthOnlyTextField(
					"prefix", 
					new LocalizedPropertyModel(getDefaultModel(), "prefix", getStudyLocale()),
					AjaxBehaviorFactory.getUpdateBehavior("onblur", RrtPrefixPostfixConfigurationPanel.this))
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected boolean isEditAllowed()
			{
				return getConfiguration().getStudy().getState() == StudyState.INIT;
			}
		};
		
		prefix.setRequired(true);
		
		getForm().add(prefix);
		
		
		AuthOnlyTextField postfix = 
			new AuthOnlyTextField(
					"postfix", 
					new LocalizedPropertyModel(getDefaultModel(), "postfix", getStudyLocale()),
					AjaxBehaviorFactory.getUpdateBehavior("onblur", RrtPrefixPostfixConfigurationPanel.this))
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			protected boolean isEditAllowed()
			{
				return getConfiguration().getStudy().getState() == StudyState.INIT;
			}
		};
		
		postfix.setRequired(true);
		
		getForm().add(postfix);
	}

	@Override
	protected StudyDependendDao<RrtConfigurationEntity> getConfigurationDao() {
		return dao;
	}
}

