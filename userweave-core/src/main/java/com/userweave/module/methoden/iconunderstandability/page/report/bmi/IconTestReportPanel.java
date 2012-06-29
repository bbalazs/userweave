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
package com.userweave.module.methoden.iconunderstandability.page.report.bmi;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.domain.service.ModuleService;
import com.userweave.module.methoden.iconunderstandability.domain.IconTermMatchingConfigurationEntity;
import com.userweave.module.methoden.iconunderstandability.domain.ItmTerm;
import com.userweave.pages.components.slidableajaxtabpanel.SlidableTabbedPanel;
import com.userweave.pages.configuration.report.FilterFunctorCallback;

/**
 * @author oma
 */
public class IconTestReportPanel extends Panel 
{
	private static final long serialVersionUID = 1L;
	
	@SpringBean
	private ModuleService moduleService;
	
	public IconTestReportPanel(
			String id, 
			final IconTermMatchingConfigurationEntity configuration,
			final FilterFunctorCallback callback) 
	{
		super(id);
		
		setDefaultModel(new LoadableDetachableModel()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected Object load()
			{
				return moduleService.getModuleConfigurationForStudy(configuration.getStudy(), configuration.getId());
			}
		});
		
		addSelectionChoice(callback);
	}

	private void addSelectionChoice(final FilterFunctorCallback callback)
	{
		final IconTermMatchingConfigurationEntity conf = getConfiguration();
		
		List<ItmTerm> terms = conf.getTerms();
		
		if(terms.size() == 0)
		{
			add(new WebMarkupContainer("tabs"));
		}
		else
		{
			final Locale locale = conf.getStudy().getLocale();
			
			SlidableTabbedPanel tabPanel = 
				new SlidableTabbedPanel(
						"tabs", getTabs(conf, terms, locale, callback), SlidableTabbedPanel.ICON_TABBED_PANEL)
				{
					private static final long serialVersionUID = 1L;

					@Override
					protected String getTabContainerCssClass()
					{
						return "icontestTabs tab-row";
					}
				};
			
			// trigger slidable functions
			AjaxRequestTarget target = AjaxRequestTarget.get();
				
			if(target != null)
			{
				target.appendJavaScript(tabPanel.getTriggerScripts());
			}
				
			add(tabPanel);
		}
		
	}
	
	private List<ITab> getTabs(
		final IconTermMatchingConfigurationEntity conf,
		final List<ItmTerm> terms, 
		final Locale locale, 
		final FilterFunctorCallback callback)
	{
		List<ITab> tabs = new ArrayList<ITab>();
		
		for(final ItmTerm term : terms)
		{
			tabs.add(new AbstractTab(new Model(term.getValue().getValue(locale)))
			{
				private static final long serialVersionUID = 1L;

				@Override
				public Panel getPanel(String panelId)
				{
					return new BMITermReportPanel
						(panelId, conf, term, locale, callback);
				}
			});
		}
		
		return tabs;
	}
	
	private IconTermMatchingConfigurationEntity getConfiguration()
	{
		return (IconTermMatchingConfigurationEntity) getDefaultModelObject();
	}
}

