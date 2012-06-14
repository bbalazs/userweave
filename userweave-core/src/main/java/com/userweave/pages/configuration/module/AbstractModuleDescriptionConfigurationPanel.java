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
package com.userweave.pages.configuration.module;

import java.util.Locale;

import com.userweave.ajax.form.AjaxBehaviorFactory;
import com.userweave.components.authorization.AuthOnlyTextArea;
import com.userweave.components.model.LocalizedPropertyModel;
import com.userweave.domain.ModuleConfigurationEntityBase;

/**
 * @author oma
 */
@Deprecated
@SuppressWarnings("serial")
public abstract class AbstractModuleDescriptionConfigurationPanel<T extends ModuleConfigurationEntityBase> extends ModuleConfigurationFormPanelBase<T> {
	
	public AbstractModuleDescriptionConfigurationPanel(String id, final Integer configurationId, Locale studyLocale) {
		super(id, configurationId, studyLocale);
		
		AuthOnlyTextArea area  = 
			new AuthOnlyTextArea(
					"description", 
					new LocalizedPropertyModel(getDefaultModel(), "description", getStudyLocale()),
					AjaxBehaviorFactory.getUpdateBehavior("onblur",AbstractModuleDescriptionConfigurationPanel.this),
					null,
					3, 50);
		
		area.setRequired(true);
		
		getForm().add(area);
	}
}

