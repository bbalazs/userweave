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

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.application.UserWeaveSession;
import com.userweave.components.callback.EntityEvent;
import com.userweave.components.callback.EventHandler;
import com.userweave.domain.StudyState;
import com.userweave.domain.service.StudyService;
import com.userweave.pages.configuration.study.StudyConfigurationPanelBase;
import com.userweave.pages.configuration.study.details.StudyDetailsParameterPanel;

/**
 * @author oma
 */
@SuppressWarnings("serial")
public class StudySelectionAccordionHeader extends StudyConfigurationPanelBase {

	@SpringBean
	private StudyService studyService;

	public StudySelectionAccordionHeader(String id, final int studyId, final EventHandler callback) {
		super(id, studyId);
		
		AbstractLink select = new AjaxLink("select") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				EntityEvent.Selected(target, getStudy()).fire(callback);
			}			
		};
			
		select.add(new Label("name"));
		final StudyState state = getStudy().getState(); 
		if( state == StudyState.INIT) {
			select.add(new StudyDetailsParameterPanel("date", new StringResourceModel("constructed", this, null), new PropertyModel(getDefaultModel(), "creationDate.toDate")));
		} else if(state == StudyState.RUNNING) {
			select.add(new StudyDetailsParameterPanel("date", new StringResourceModel("activate", this, null), new PropertyModel(getDefaultModel(), "activationDate.toDate")));
		} else {
			select.add(new StudyDetailsParameterPanel("date", new StringResourceModel("closed", this, null), new PropertyModel(getDefaultModel(), "finishDate.toDate")));			
		}

			// currently disabled

//			add(new Label("state") {
//				@Override
//				public boolean isVisible() {
//					return !getStudy().isDeleted();
//				}
//			});
			
		select.add(
			new WebMarkupContainer("container") {
				@Override
				public boolean isVisible() {				
					return UserWeaveSession.get().getUser().isAdmin();
				}
			}
			.add(new Label("owner.email"))
		);
			
		select.add(new StudyDetailsParameterPanel("deletedAt", new StringResourceModel("deleted", this, null), new PropertyModel(getDefaultModel(), "deletedAt.toDate")) {
			@Override
			public boolean isVisible() {
				return getStudy().isDeleted();
			}
		});
		
		add(select);
		
//		AjaxLink reset = new AjaxLink("reset")
//		{
//			@Override
//			public void onClick(AjaxRequestTarget target)
//			{
//				if(state == StudyState.RUNNING)
//				{
//					getStudy().setState(StudyState.INIT);
//					EntityEvent.Purged(target, getStudy()).fire(callback);
//				}
//			}
//		};
//		
//		add(reset);
	}
}

