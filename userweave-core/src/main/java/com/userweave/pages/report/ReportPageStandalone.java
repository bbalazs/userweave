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
package com.userweave.pages.report;

import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.module.methoden.iconunderstandability.dao.IconTermMatchingConfigurationDao;
import com.userweave.module.methoden.iconunderstandability.page.report.bmi.BMIReportUI;
import com.userweave.pages.base.BaseUserWeavePage;

/**
 * @author oma
 */
//@AdminOnly
@Deprecated
public class ReportPageStandalone extends BaseUserWeavePage {
	
	@SpringBean
	private IconTermMatchingConfigurationDao dao;
	
	public ReportPageStandalone() {
		Integer a = null;
		a.intValue();
//		add(HeaderContributor.forJavaScript(BasePageSurvey.class, "jquery-1.2.6.min.js"));
//		add(HeaderContributor.forCss(BasePageSurvey.class, "basepagesurvey.css"));
		add(new BMIReportUI("report", dao.findById(640), null));		

	}
}

