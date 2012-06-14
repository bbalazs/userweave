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
package com.userweave.pages.configuration.study.localization;

import java.util.List;
import java.util.Locale;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.domain.Study;
import com.userweave.domain.service.ModuleService;
import com.userweave.module.ModuleConfiguration;
import com.userweave.module.ModuleConfigurationImpl;

/**
 * @author oma
 */
public class LocalizationPanel extends Panel 
{
	private static final long serialVersionUID = 1L;
	@SpringBean
	private ModuleService moduleService;
	
	public LocalizationPanel(
		String id, IModel studyModel, List<Locale> locales, Locale studyLocale) 
	{
		super(id, studyModel);
		
		List<ModuleConfiguration> moduleConfigurations = 
			moduleService.getSortedModuleConfigurationsForStudy(getStudy());

		RepeatingView moduleConfigurationsRepeater = 
			new RepeatingView("moduleConfigurations");
		
		moduleConfigurationsRepeater.add(
			LocalizationPanelFactory.createLocalePanelForEntity(
					moduleConfigurationsRepeater.newChildId(), 
					locales, 
					getStudy(),
					studyLocale));
		
		for(ModuleConfiguration<?> conf : moduleConfigurations)
		{
			moduleConfigurationsRepeater.add(
				LocalizationPanelFactory.createLocalePanelForEntity(
						moduleConfigurationsRepeater.newChildId(), 
						locales,
						(ModuleConfigurationImpl)conf,
						studyLocale));
		}
		
		add(moduleConfigurationsRepeater);		
	}

	private Study getStudy() {
		return (Study) getDefaultModelObject();
	}

}

