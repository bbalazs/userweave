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
package com.userweave.module.methoden.iconunderstandability.page.survey;

import java.util.Locale;

import org.apache.wicket.model.PropertyModel;

import com.userweave.application.OnFinishCallback;
import com.userweave.module.methoden.iconunderstandability.page.survey.termCounter.TermCounter;

@SuppressWarnings("serial")
public abstract class IconMatchingTestUIWithMultiplePages extends IconMatchingTestUI {

	private int position = -1;
	
	protected int getPosition() {
		return position;
	}

	protected void setPosition(int position) {
		this.position = position;
	}
	
	protected void  incrementPosition() {
		position++;
	}
	
	public abstract int getMaxPosition();
	
	public IconMatchingTestUIWithMultiplePages(
		String id, int surveyExecutionId, 
		int configurationId, 
		OnFinishCallback onFinishCallback,
		Locale locale) 
	{
		super(id, surveyExecutionId, configurationId, onFinishCallback, locale);
		
		setPosition(0);
		
		add(
			new TermCounter("termCounter",
				new PropertyModel(this, "positionText"),
				new PropertyModel(this, "maxPosition")	
			)
		);
	}

	public String getPositionText() {
		return Integer.toString(position + 1);
	}	
	
}
