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
package com.userweave.pages.components.headerpanel;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.userweave.components.customModalWindow.CustomModalWindow;
import com.userweave.components.navigation.RoundedBorderGray;

/**
 * this class represents a generified panel
 * consists of three parts:
 * - a header panel
 * - a content part and
 * - a links repeater
 * 
 * @author ipavkovic
 */
@SuppressWarnings("serial")
public class HeaderPanel extends Panel {

	private Component header;
	private RepeatingView repeater;

	public HeaderPanel(String id) {
		super(id);
		this.setOutputMarkupId(true);
		init();
	}
	
	public HeaderPanel(String id, IModel model) {
		super(id, model);
		this.setOutputMarkupId(true);
		init();
	}

	protected void init() {
		RoundedBorderGray border = new RoundedBorderGray("border");
		border.add(getHeaderPanel());
		border.add(getLinksRepeater());
			
		add(border);
	}

	protected final Component getHeaderPanel() {
		if(header ==null) {
			header = createHeaderComponent("header");
		}
		return header;
	}

	protected final RepeatingView getLinksRepeater() {
		if(repeater == null) {
			repeater = new RepeatingView("links");
		}
		return repeater;
	}
	
	protected Component createHeaderComponent(String markupId) {
		return new Label(markupId, new Model("overwrite "+this.getClass().getName()+".createHeaderComponent!!"));
	}
	
	protected CustomModalWindow addLinkModalWindow() {
		String markupId = repeater.newChildId();
		CustomModalWindow component = new CustomModalWindow(markupId);
		repeater.add(component);
		return component;
	}
	
	protected LinkComponent addLinkComponent(LinkComponentCreator creator) {
		String markupId = repeater.newChildId();
		LinkComponent linkComponent = creator.create(markupId);
		repeater.add(linkComponent);
		return linkComponent;
	}

}

