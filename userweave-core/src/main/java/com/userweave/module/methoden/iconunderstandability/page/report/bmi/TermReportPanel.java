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

import java.util.Locale;

import org.apache.wicket.markup.html.panel.Panel;

import com.userweave.module.methoden.iconunderstandability.domain.report.TermReport;

/**
 * @author oma
 */
public class TermReportPanel extends Panel 
{ 
	private static final long serialVersionUID = 1L;
	
	private final Locale studyLocale;
	
	public TermReportPanel(String id, TermReport termReport, int configurationId, Locale aStudyLocale) {		
		super(id);
		this.studyLocale = aStudyLocale;

		add(new IconReportListPanel("iconRatingListPanel", termReport, studyLocale));
		
		add(new IndicatorPanel("indicatorPanel", termReport));
	}
}

