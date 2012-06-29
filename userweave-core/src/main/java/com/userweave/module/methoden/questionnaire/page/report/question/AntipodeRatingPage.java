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
package com.userweave.module.methoden.questionnaire.page.report.question;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

import com.userweave.components.customModalWindow.BaseModalWindowPage;

public abstract class AntipodeRatingPage extends BaseModalWindowPage
{
	public AntipodeRatingPage(IModel mean, IModel sd, int count)
	{		
		addToForm(getRatingReport("antipodeRatingReport"));
		
		addToForm(new Label("mean", mean));
		
		addToForm(new Label("sd", sd));
		
		addToForm(new Label("count", new Model(count)));
	}
	
	@Override
	protected WebMarkupContainer getAcceptButton(String componentId,
			ModalWindow window)
	{
		WebMarkupContainer link = super.getAcceptButton(componentId, window);
	
		link.setVisible(false);
		
		return link;
	}
	
	@Override
	protected IModel getDeclineLabel()
	{
		return new StringResourceModel("close", this, null);
	}
	
	protected abstract Component getRatingReport(String componentId);
}
