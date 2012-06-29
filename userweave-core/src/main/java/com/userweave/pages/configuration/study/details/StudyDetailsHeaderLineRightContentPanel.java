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

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

@SuppressWarnings("serial")
public class StudyDetailsHeaderLineRightContentPanel extends Panel {

	public StudyDetailsHeaderLineRightContentPanel(String id, StringResourceModel study_state, IModel referenceLanguage, IModel supportedLocalesCount, IModel supportedLocales) {
		super(id);
		
		WebMarkupContainer localeCon = new WebMarkupContainer("localeContainer");
		add (localeCon);
		
		add( new Label("studyStatus", study_state) );
		
		localeCon.add( new Label("referenceLanguage", referenceLanguage) );
		
		
		int supportedLocalesCountAsInt = ((Integer)supportedLocalesCount.getObject()).intValue();
		
		Label supportesLocalesLabel =  new Label("totalLanguages", "");
		if ( supportedLocalesCountAsInt > 0 ) {
			supportesLocalesLabel =  new Label("totalLanguages", "(+" + supportedLocalesCountAsInt +")" );
		}
		localeCon.add( supportesLocalesLabel );
		
		localeCon.add( new AttributeModifier("title", true, supportedLocales) );
	}

}
