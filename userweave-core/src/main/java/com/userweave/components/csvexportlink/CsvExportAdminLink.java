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
package com.userweave.components.csvexportlink;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.IResourceStream;

import com.userweave.application.UserWeaveSession;
import com.userweave.domain.Study;
import com.userweave.domain.StudyState;
import com.userweave.domain.service.CsvExportService;
import com.userweave.pages.configuration.EnabledInStudyState;

@EnabledInStudyState(states={StudyState.RUNNING,StudyState.FINISHED})
public class CsvExportAdminLink extends Link<Study>
{	
	private static final long serialVersionUID = 1L;
	
	@SpringBean
	private CsvExportService csvExportService;
	
	public CsvExportAdminLink(String id, IModel<Study> studyModel)
	{
		super(id, studyModel);
	}

	@Override
	public void onClick()
	{
		Study study = (Study) getDefaultModelObject();
		
		IResourceStream resourceStream = 
			csvExportService.exportStudyResultsToCsv(study);
		
		String fileName = study.getName() + ".csv";
		
		getRequestCycle().scheduleRequestHandlerAfterCurrent(
				new ResourceStreamRequestHandler(resourceStream).setFileName(fileName));
	}
	
	@Override
	public boolean isVisible()
	{
		return UserWeaveSession.get().isAdmin();
	}

}
