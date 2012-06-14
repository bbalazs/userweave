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
package com.userweave.pages.configuration.study.localization.localizationpanel;

import java.util.List;
import java.util.Locale;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.AbstractTextComponent;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.ajax.IAjaxLocalizedStringUpdater;
import com.userweave.dao.LocalizedStringDao;
import com.userweave.domain.EntityBase;
import com.userweave.domain.LocalizedString;

public abstract class AbstractLocalizationPanel 
	extends Panel 
	implements IAjaxLocalizedStringUpdater
{
	private static final long serialVersionUID = 1L;

	@SpringBean
	private LocalizedStringDao localizedStringDao;
	
	private class TextAreaFragment extends Fragment
	{
		private static final long serialVersionUID = 1L;

		public TextAreaFragment(String id, TextArea textArea)
		{
			super(id, "textAreaFragment", AbstractLocalizationPanel.this);
		
			add(textArea);
		}
	}
	
	private class TextFieldFragment extends Fragment
	{
		private static final long serialVersionUID = 1L;

		public TextFieldFragment(String id, TextField textField)
		{
			super(id, "textFieldFragment", AbstractLocalizationPanel.this);
		
			add(textField);
		}
	}
	
	private class LabelFragment extends Fragment
	{
		private static final long serialVersionUID = 1L;

		public LabelFragment(String id, LocalizedString value, Locale l)
		{
			super(id, "labelFragment", AbstractLocalizationPanel.this);
			
			add(new Label("value", value.getValue(l)));
		}
	}
	
	public AbstractLocalizationPanel(
		String id, List<Locale> locales, 
		List<LocalizedString> localizedStrings, 
		EntityBase entity, Locale studyLocale)
	{
		super(id);
		
		add(new Label("entityName", getEntityName(entity)));
		
		Form form = new Form("form");
		
		add(form);
		
		RepeatingView rv = new RepeatingView("configurations");
		
		form.add(rv);
		
		form.add(new Label("hostLocale", locales.get(0).getDisplayName()));
		form.add(new Label("translateToLocale", locales.get(1).getDisplayName()));
		
		// for each localized string, create a row,
		// which contains in each column a translation.
		// The first column ist always the study locale.
		int sizeOfList = localizedStrings.size();
		
		for(int index = 0; index < sizeOfList ; index++)
		{
			LocalizedString currentString = localizedStrings.get(index);
			
			// the row for each configuration term
			WebMarkupContainer row = new WebMarkupContainer(rv.newChildId());
			
			// name of the configuration term
			row.add(new Label("label", getLabelForRow(index, sizeOfList)));
			
			rv.add(row);
			
			
			// columns for locales
			RepeatingView localeColums = new RepeatingView("localeColumns");
			
			row.add(localeColums);
			
			// add a column for each locale, which contains
			// a text field
			boolean isFirst = true;
			
			for(Locale locale : locales)
			{
				WebMarkupContainer column = new WebMarkupContainer(localeColums.newChildId());
				
				if(currentString != null)
				{
					if(isFirst)
					{
						isFirst = false;
						
						column.add(new LabelFragment("valueFragment", currentString, studyLocale));
					}
					else
					{
						AbstractTextComponent component = 
							getTextComponentForLocale("value", currentString, index, locale);
				
						if(component instanceof TextField)
						{
							column.add(new TextFieldFragment("valueFragment", (TextField)component));
						}
						else
						{
							column.add(new TextAreaFragment("valueFragment", (TextArea)component));
						}	
					}
				}
				else
				{
					column.add(new WebMarkupContainer("valueFragment"));
				}
				
				localeColums.add(column);
			}
		}
	}
	
	@Override
	public void onUpdate(AjaxRequestTarget target, LocalizedString localeString)
	{
		localizedStringDao.save(localeString);
	}
	
//	protected AjaxFormComponentUpdatingBehavior getUpdateBehavior(
//			String event, final LocalizedString localeString) 
//	{
//		return new AjaxFormComponentUpdatingBehavior(event)
//		{
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			protected void onUpdate(AjaxRequestTarget target) 
//			{
//				localizedStringDao.save(localeString);
//			}
//		};
//	}
	
	protected abstract IModel getEntityName(EntityBase entity);
	
	protected abstract IModel getLabelForRow(int index, int size);
	
	protected abstract AbstractTextComponent getTextComponentForLocale(
			String id, LocalizedString localeString, int index, Locale locale);
}
