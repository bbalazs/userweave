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
package com.userweave.pages.configuration.study.details;

import java.util.Date;

import org.apache.wicket.datetime.StyleDateConverter;
import org.apache.wicket.datetime.markup.html.basic.DateLabel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import com.userweave.pages.components.headerpanel.LinkComponent;

/**
 * @author oma
 */
@SuppressWarnings("serial")
public class StudyDetailsParameterWithLinkPanel extends Panel {

	public StudyDetailsParameterWithLinkPanel(String id, IModel nameModel, IModel valueModel, LinkComponent link) {
		super(id);
		add(new Label("name", nameModel));
		if(valueModel.getObject() instanceof Date) {
			add(new DateLabel("value", valueModel, new StyleDateConverter("SS", true)));
		} else {
			add(new Label("value", valueModel));
		}
		
		add ( link );
	}
	
	
	public static String getLinkId() {
		return "link";
	}
}

