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
package com.userweave.pages.components.servicePanel;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.StringResourceModel;

import com.userweave.application.UserWeaveSession;
import com.userweave.components.customModalWindow.CustomModalWindow;

@SuppressWarnings("serial")
@Deprecated
public class ServicePanel extends Panel {

	public enum ServicePanelType {
		SAVE_TIME { @Override
			public String toString() { return "save_time"; }},
		NOT_SURE_QUESTIONS { @Override
			public String toString() { return "not_sure_questions"; }},
		NEW_METHODS { @Override
			public String toString() { return "new_methods"; }},
		MORE_LANGUAGES { @Override
			public String toString() { return "more_languages"; }},
		TEST_MEMBER { @Override
			public String toString() { return "test_member"; }},
		NOT_SURE_INTERPRETATION { @Override
			public String toString() { return "not_sure_interpretation"; }},
		ADVANCE_PRODUCT { @Override
			public String toString() { return "advance_product"; }},
		OWN_HOSTING { @Override
			public String toString() { return "own_hosting"; }},
		ACTIVE_SEMINAR { @Override
			public String toString() { return "active_seminar"; }}
	}
	
	private CustomModalWindow modalWindow;
	
	public ServicePanel(String id, ServicePanelType serviceTextResource) {
		super(id);
		
		final StringResourceModel srm = 
			new StringResourceModel(serviceTextResource.toString(), this, null);
		
		final StringResourceModel srm_link = 
			new StringResourceModel(serviceTextResource.toString() + "_link_text", this, null);
		
		add(new Label("servicePanelHeadline", 
				new StringResourceModel(serviceTextResource.toString() + "_headline", this, null)));
		
		add(new Label("serviceText", srm));
		
		add(new AjaxLink("more") {

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				modalWindow.show(target);
				
			}
		}.add(new Label("more_label", 
				new StringResourceModel(serviceTextResource.toString() + "_link_text", this, null))));
		
		add(modalWindow = new CustomModalWindow("showMoreModalWindow"));
		
		modalWindow.setPageCreator(new ModalWindow.PageCreator() {
            public Page createPage() 
            {
            	return new ServicePanelPopupPage(modalWindow, srm, srm_link);
            };
        });
		
//		modalWindow.setCssClassName(CustomModalWindow.CSS_CLASS_MINT);
		modalWindow.setInitialHeight(570);
		modalWindow.setInitialWidth(580);
	}
	
	@Override
	public boolean isVisible() {
		if(UserWeaveSession.get().originFromReport()) {
			return false;
		} else {
			return super.isVisible();
		}
	}
}
