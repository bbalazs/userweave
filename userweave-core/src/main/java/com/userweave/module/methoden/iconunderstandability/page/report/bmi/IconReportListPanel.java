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

import java.util.List;
import java.util.Locale;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.util.WildcardListModel;

import com.userweave.components.panelWithDetails.PanelWithDetails;
import com.userweave.module.methoden.iconunderstandability.domain.report.IconReport;
import com.userweave.module.methoden.iconunderstandability.domain.report.TermReport;

/**
 * @author oma
 */
public class IconReportListPanel extends PanelWithDetails 
{
	private static final long serialVersionUID = 1L;

	private static int MAX_ICON_REPORTS = 3;
	
	private final TermReport termReport;
	
	private final Locale studyLocale;
	
	public IconReportListPanel(String id, TermReport theTermReport, Locale aStudyLocale) {		
		super(id);	
		this.termReport = theTermReport;
		this.studyLocale = aStudyLocale;
		
		setOutputMarkupId(true);
		
		WildcardListModel<IconReport> iconModel = new WildcardListModel<IconReport>() 
		{
			private static final long serialVersionUID = 1L;

			@Override
			public List<IconReport> getObject()
			{
				if (detailsAreShown()) 
				{
					return termReport.getBestMatchingIconReports();
				} 
				else 
				{						
					return termReport.getBestMatchingIconReports(MAX_ICON_REPORTS);
				}
			}
			
		};
		
		add(new ListView<IconReport>("icons", iconModel)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<IconReport> item)
			{
				item.add(new IconReportPanel(
					"iconDetails", 
					item.getModelObject(), 
					studyLocale, 
					item.getIndex())
				{
					private static final long serialVersionUID = 1L;

					@Override
					protected void onModalWindowClose(AjaxRequestTarget target)
					{
						target.add(IconReportListPanel.this);
					}
				});
			}
		});
	}
	
	@Override
	protected boolean hasDetails() {
		return termReport.getNumberOfIcons() >  MAX_ICON_REPORTS;
	}
}

