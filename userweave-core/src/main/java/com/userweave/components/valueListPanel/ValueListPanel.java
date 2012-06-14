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
package com.userweave.components.valueListPanel;

import java.awt.Dimension;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IFormSubmitter;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;

/**
 * @author oma
 */
@SuppressWarnings("serial")
public abstract class ValueListPanel<T> extends Panel {

	private ValueListController<T> controller;
	
	private FeedbackPanel feedbackPanel;

	public interface ValueListController<T> extends Serializable {
		public List<T> getValues();
		public Dimension getLargestDimension();
		public void addValue(T value);
		public void changeValue(T oldValue, T newValue);
		public void removeValue(T value);
		public String validate(T value);
	};

	private Locale studyLocale;
	
	protected Locale getStudyLocale() {
		return studyLocale;
	}
	
	public interface StringValueListController extends ValueListController<String>{};

	public ValueListPanel(String id, ValueListController<T> cb) {
		super(id);
		init(cb);			
	}

	
	public ValueListPanel(String id, Locale studyLocale, ValueListController<T> cb) {
		super(id);
		this.studyLocale = studyLocale;
		init(cb);			
	}


	private void init(ValueListController<T> cb) {
		this.controller = cb;
  
		final RepeatingView rows = new RepeatingView("repeater");	    
		add(rows);
			
		int index = 0;
		
		List<T> values = controller.getValues();
		
		if (values != null) {
			for (T value : values) {				
				addListRow(rows, value);
				index++;
			}	
		}
	    add(
			new Form("form") { 
	
				{					
					add(getInputComponent("input"));					
					add(new Button("submit"));
				}
			
				
				@Override
				protected void onSubmit() {				
					List<T> input = getInput();
					
					for (T t : input) {
						controller.addValue(t);
						addListRow(rows, t);						
					}
					clearInputComponent();
				}
				
				@Override
				// don't submit/validate this form if root form is submitted  
			    public boolean isEnabled() {					
					Form rootForm = getRootForm();
					if (rootForm != null) {
						IFormSubmitter submittingButton = rootForm.findSubmittingButton();
						if (submittingButton != null) {
							return submittingButton.getForm() == this;
						} 
					};
					return true;
			    }
			}
		);
	}

	private void addListRow(final RepeatingView repeating, final T value) {
		
		final WebMarkupContainer item = new WebMarkupContainer(repeating.newChildId());
	    repeating.add(item);
	    
		Component listComponent = getListComponent("rowDisplay", value);
		item.add(listComponent);
		
		final IModel model  = listComponent.getDefaultModel();		
		
		item.add( 
			new Link("delete") {
	
				@Override
				public void onClick() {				
					repeating.remove(item);					
					controller.removeValue((T) model.getObject());						
				}
			}
		);
	}

	protected abstract Component getInputComponent(String id);
	
	/**
	 * ListComponent must have a model containing displayed object
	 */
	protected abstract Component getListComponent(String id, final T value);
	
	protected void onError(AjaxRequestTarget target) {
		if(feedbackPanel != null) {
			target.addComponent(feedbackPanel);
		}
	}
	
	protected abstract void clearInputComponent();

	protected abstract List<T> getInput();

	protected void changeValue(T oldValue, T newValue) {
		controller.changeValue(oldValue, newValue);
	}

	protected void validateValue(IValidatable validatable) {
		String message = controller.validate((T) validatable.getValue());	
		if (message != null) {
			validatable.error(new ValidationError().setMessage(message));
		}
	}
}

