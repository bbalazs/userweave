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
package com.userweave.pages.configuration.study.selection;

import java.util.List;

import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.components.callback.EventHandler;
import com.userweave.domain.Project;
import com.userweave.domain.Role;
import com.userweave.domain.Study;
import com.userweave.domain.service.StudyService;

@SuppressWarnings("serial")
@Deprecated
public class StudyInitPanel extends StudyListPanel {

	@AuthorizeAction(action = Action.RENDER, roles = Role.PROJECT_ADMIN)
	private abstract class AuthoriziedLink extends AjaxLink
	{
		public AuthoriziedLink(String id)
		{
			super(id);
		}
	}
	
	@SpringBean
	private StudyService studyService;
	
	private ModalWindow addStudyModalWindow;
	
	private final int createdStudyId = -1;
	
	public StudyInitPanel(String id, 
			List<Study> studiesList,
			Project project,
			final EventHandler callback)
	{
		super(id, studiesList, callback);
		//addAddStudyModalWindow(project, callback);
	}
	
	
}
