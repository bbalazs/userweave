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
package com.userweave.pages.components.reorderable;

import java.util.Locale;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.ajax.IAjaxLocalizedStringUpdater;
import com.userweave.ajax.form.AjaxBehaviorFactory;
import com.userweave.components.authorization.AuthOnlyTextField;
import com.userweave.components.model.LocalizedModel;
import com.userweave.dao.LocalizedStringDao;
import com.userweave.domain.LocalizedString;

public abstract class LocalizedStringReorderableListPanel 
	extends ReorderableListPanel<LocalizedString>
	implements IAjaxLocalizedStringUpdater
{
	private static final long serialVersionUID = 1L;

	@SpringBean
	private LocalizedStringDao localizedStringDao;
	
	private final Locale locale;
	
	//private LocalizedModel model;
	
	public LocalizedStringReorderableListPanel(String id, boolean editable, Locale locale) {
		super(id, editable, null);
		this.locale = locale;
	}
	
	public LocalizedStringReorderableListPanel(String id, boolean editable, Locale locale, String addButtonRowResourceString) {
		super(id, editable, addButtonRowResourceString);
		this.locale = locale;
	}
	
	@Override
	protected void populateItem(ListItem<LocalizedString> item) 
	{
		final LocalizedModel model = 
			new LocalizedModel(item.getModelObject(), locale);
		
		if(isEditable()) 
		{
			item.add(
				new AuthOnlyTextField(
					"value", model,
					AjaxBehaviorFactory.getUpdateBehaviorForLocalizedString(
						"onblur", model, LocalizedStringReorderableListPanel.this)));
		} 
		else 
		{
			item.add(new Label("value", model));
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
	protected boolean addIsVisible() {
		return isEditable();
	}

	@Override
	public LocalizedString append() {
		LocalizedString localizedString = new LocalizedString("",locale);
		append(localizedString);
		return localizedString;
	}

	protected abstract void append(LocalizedString localizedString);

	@Override
	protected boolean askBeforeDelete() {
		return false;
	}
}
