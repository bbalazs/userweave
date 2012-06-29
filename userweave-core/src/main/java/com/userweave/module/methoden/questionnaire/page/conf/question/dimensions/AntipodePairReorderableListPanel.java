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
package com.userweave.module.methoden.questionnaire.page.conf.question.dimensions;

import java.util.Locale;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.ajax.IAjaxLocalizedStringUpdater;
import com.userweave.ajax.form.AjaxBehaviorFactory;
import com.userweave.components.authorization.AuthOnlyTextField;
import com.userweave.components.model.LocalizedPropertyModel;
import com.userweave.dao.LocalizedStringDao;
import com.userweave.domain.LocalizedString;
import com.userweave.module.methoden.questionnaire.domain.question.AntipodePair;
import com.userweave.pages.components.button.AddLink;
import com.userweave.pages.components.reorderable.ReorderableListPanel;

public abstract class AntipodePairReorderableListPanel 
	extends ReorderableListPanel<AntipodePair> 
	implements IAjaxLocalizedStringUpdater
{
	private static final long serialVersionUID = 1L;

	@SpringBean 
	private LocalizedStringDao localizedStringDao;
	
	private final Locale locale;
	
	public AntipodePairReorderableListPanel(String markupId, boolean editable, Locale locale) {
		super(markupId, editable, AddLink.ADD_DIMENSION);
		this.locale = locale;
	}

	@Override
	protected void populateItem(ListItem item) {
		final LocalizedPropertyModel model1 =  
			new LocalizedPropertyModel(item.getModel(),"antipode1", locale);
		
		final LocalizedPropertyModel model2 =  
			new LocalizedPropertyModel(item.getModel(),"antipode2", locale);
		
		if(isEditable()) 
		{
			item.add(new AuthOnlyTextField(
					"antipode1", 
					model1, 
					AjaxBehaviorFactory.getUpdateBehaviorForLocalizedString(
							"onblur", model1, AntipodePairReorderableListPanel.this)));
			
			item.add(new AuthOnlyTextField(
				"antipode2", 
				model2, 
				AjaxBehaviorFactory.getUpdateBehaviorForLocalizedString(
					"onblur", model2, AntipodePairReorderableListPanel.this)));
		} 
		else 
		{
			item.add(new Label("antipode1", model1));
			item.add(new Label("antipode2", model2));
		}
	}

	@Override
	public void onUpdate(AjaxRequestTarget target, LocalizedString string)
	{
		if(string != null) 
		{
			localizedStringDao.save(string);
		}
	}
	
	@Override
	protected boolean askBeforeDelete() {
		return false;
	}

}
