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
package com.userweave.module.methoden.rrt.page.conf;

import java.util.Locale;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.ajax.IAjaxLocalizedStringUpdater;
import com.userweave.ajax.form.AjaxBehaviorFactory;
import com.userweave.components.authorization.AuthOnlyTextArea;
import com.userweave.components.authorization.AuthOnlyTextField;
import com.userweave.components.model.LocalizedPropertyModel;
import com.userweave.dao.LocalizedStringDao;
import com.userweave.domain.LocalizedString;
import com.userweave.module.methoden.rrt.domain.RrtTerm;
import com.userweave.pages.components.button.AddLink;
import com.userweave.pages.components.reorderable.ReorderableListPanel;

public abstract class RrtTermReorderableListPanel 
	extends ReorderableListPanel<RrtTerm> 
	implements IAjaxLocalizedStringUpdater
{
	private static final long serialVersionUID = 1L;

	@SpringBean
	protected LocalizedStringDao localizedStringDao;
	
	private final Locale locale;

	public RrtTermReorderableListPanel(String id, boolean editable, Locale locale) {
		super(id, editable, AddLink.ADD_TERM);
		this.locale = locale;
	}
	
	@Override
	protected void populateItem(ListItem item) {
		final LocalizedPropertyModel modelValue = 
			new LocalizedPropertyModel(item.getModel(), "value", locale);
		
		final LocalizedPropertyModel modelDescription = 
			new LocalizedPropertyModel(item.getModel(), "description", locale);
		
		if(isEditable()) 
		{
			item.add(new AuthOnlyTextField(
				"value", 
				modelValue, 
				AjaxBehaviorFactory.getUpdateBehaviorForLocalizedString(
					"onblur", modelValue, RrtTermReorderableListPanel.this)));
			
			item.add(new AuthOnlyTextArea(
				"description", 
				modelDescription, 
				AjaxBehaviorFactory.getUpdateBehaviorForLocalizedString(
						"onblur", modelDescription, RrtTermReorderableListPanel.this)));
			
//			item.add(new AuthOnlyAjaxEditableLabel("value", modelValue) 
//			{
//				private static final long serialVersionUID = 1L;
//
//				@Override
//				public void onSubmit(AjaxRequestTarget target) {
//					
//					super.onSubmit(target);
//				}
//				
//				@Override
//				protected String defaultNullLabel()
//				{
//					return new StringResourceModel("value", this, null).getString();
//				}
//			});
			
//			AuthOnlyAjaxEditableMultiLineLabel ajaxLabel = 
//				new AuthOnlyAjaxEditableMultiLineLabel("description", modelDescription) 
//				{
//				    private static final long serialVersionUID = 1L;
//
//					@Override
//					public void onSubmit(AjaxRequestTarget target) {
//						LocalizedString string = modelDescription.getLocalizedString();
//						if(string != null) {
//							localizedStringDao.save(string);
//						}
//						super.onSubmit(target);
//					}
//					
//					@Override
//					protected String defaultNullLabel() {
//						return new StringResourceModel("description", this, null).getString();
//					}
//				};
//			
//			ajaxLabel.setCols(29);	
//				
//			item.add(ajaxLabel);
		} 
		else 
		{
			item.add(new Label("value", modelValue));
			item.add(new Label("description", modelDescription));
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
