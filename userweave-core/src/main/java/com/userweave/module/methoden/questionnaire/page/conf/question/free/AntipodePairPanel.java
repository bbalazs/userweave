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
package com.userweave.module.methoden.questionnaire.page.conf.question.free;

import java.util.Locale;

import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import com.userweave.components.authorization.AuthOnlyTextField;
import com.userweave.components.model.LocalizedPropertyModel;

/**
 * @author oma
 */
@Deprecated
public abstract class AntipodePairPanel extends Panel {

	private final AuthOnlyTextField antipode1;
	private final AuthOnlyTextField antipode2;
	
	public AntipodePairPanel(String id, IModel antipodeModel, Locale locale) {
		super(id);				

		antipode1 = new AuthOnlyTextField(
				"antipode1", 
				new LocalizedPropertyModel(antipodeModel, "antipode1", locale),
				getUpdateBehavior())
		{
			@Override
			protected boolean isEditAllowed()
			{
				return AntipodePairPanel.this.isEditAllowed();
			}
		};
		
		antipode1.setOutputMarkupId(true);
		antipode1.setRequired(true);
		
		add(antipode1);
		
		antipode2 = new AuthOnlyTextField(
				"antipode2", 
				new LocalizedPropertyModel(antipodeModel, "antipode2", locale),
				getUpdateBehavior())
		{
			@Override
			protected boolean isEditAllowed()
			{
				return AntipodePairPanel.this.isEditAllowed();
			}
		};
		
		antipode2.setOutputMarkupId(true);
		antipode2.setRequired(true);
		
		add(antipode2);		
	}
	
	protected abstract AjaxFormComponentUpdatingBehavior getUpdateBehavior();
	
	protected abstract boolean isEditAllowed();
	
	public void addUpdateEventToAntipode1(AjaxFormComponentUpdatingBehavior updateBehavior)
	{
		antipode1.add(updateBehavior);
	}
	
	public void addUpdateEventToAntipode2(AjaxFormComponentUpdatingBehavior updateBehavior)
	{
		antipode2.add(updateBehavior);
	}
}

