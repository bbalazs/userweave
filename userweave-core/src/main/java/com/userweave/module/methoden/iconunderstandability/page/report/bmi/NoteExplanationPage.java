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

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

/**
 * @author: opr
 * 
 * Note Explanation page for modal window
 */
public abstract class NoteExplanationPage extends WebPage 
{
	public static final String RATING1 = "rating1";
	
	public static final String RATING2 = "rating2";
	
	public static final String RATING3 = "rating3";
	
	public NoteExplanationPage()
	{
		super();
		
		add(new Label("note_explanation", getNoteExplanation()));
		
		add(new Label("disclaimer", getDisclaimer()));
	}
	
	/**
	 * Get message for note explanation.
	 * @param key
	 * @return
	 */
	protected abstract IModel getNoteExplanation();
	
	protected IModel getDisclaimer()
	{
		return new StringResourceModel("disclaimer", this, null);
	}
}
