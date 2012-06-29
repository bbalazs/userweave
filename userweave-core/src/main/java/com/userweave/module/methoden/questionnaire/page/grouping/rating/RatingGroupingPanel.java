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
package com.userweave.module.methoden.questionnaire.page.grouping.rating;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.RangeValidator;

import com.userweave.module.methoden.questionnaire.domain.group.RatingGroup;
import com.userweave.module.methoden.questionnaire.page.grouping.GroupAddedCallback;
import com.userweave.pages.grouping.GroupingPanelWithName;


/**
 * @author oma
 */
@SuppressWarnings("serial")
public abstract class RatingGroupingPanel<T extends RatingGroup> extends GroupingPanelWithName<T> {
	
	private final T group;

	@Override
	public T getGroup() {
		return group;
	}
	
	private Operator operator;

	private enum Operator {
		LESS("less or equal"), GREATER("greater or equal"), BETWEEN("between"), MISSING_VALUE("missing value");
		
		private String name;

		private Operator(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
	};
	
	public RatingGroupingPanel(String id, Integer numberOfRatingSteps, T group, final Locale locale, GroupAddedCallback groupAddedCallback, boolean groupMissingValues) {
		super(id, group, locale, groupAddedCallback);
 
		this.group = group;
		
		setDefaultModel(new CompoundPropertyModel(group));
				
		final FormComponent lowerBoundTextField = new TextField("lowerBound") {
			@Override
			public boolean isVisible() {
				return operator != null && !operator.equals(Operator.LESS) && !operator.equals(Operator.MISSING_VALUE); 
			}
			
		};
		
		setValidation(lowerBoundTextField, numberOfRatingSteps);
		
		add(lowerBoundTextField);
		
		
		final FormComponent upperBoundTextField = new TextField("upperBound") {
			@Override
			public boolean isVisible() {
				return operator != null && !operator.equals(Operator.GREATER) && !operator.equals(Operator.MISSING_VALUE);
			}
		};
		
		setValidation(upperBoundTextField, numberOfRatingSteps);
		
		add(upperBoundTextField);
		
		final Component and = new WebMarkupContainer("and") {
			@Override
			public boolean isVisible() {
				return operator != null 
					&& operator.equals(Operator.BETWEEN) 
					&& !operator.equals(Operator.MISSING_VALUE);
			}
		}
		.setOutputMarkupPlaceholderTag(true);
		
		add(and);
		
		List<Operator> operators = new ArrayList<Operator>();
		for (Operator op : Operator.values()) {
			if (!op.equals(Operator.MISSING_VALUE) || groupMissingValues) {
				operators.add(op);
			}			
		}
		
		add(
			new RadioChoice(
				"operator", 
				new PropertyModel(this, "operator"), 
				operators,
				new ChoiceRenderer("name")
			)
			.setRequired(true)
			.add(
				new AjaxFormChoiceComponentUpdatingBehavior()
				{
					@Override
					protected void onUpdate(AjaxRequestTarget target)
					{
						target.addComponent(lowerBoundTextField);
						target.addComponent(upperBoundTextField);
						target.addComponent(and);
					}				
				})
					
		);

	}

	@Override
	public void submit() {
		if (operator.equals(Operator.MISSING_VALUE)) {
			getGroup().setMissing(true);
		}
		
	}
	
	private void setValidation(final FormComponent textField, Integer numberOfRatingSteps) {
		textField
			.setRequired(true)
			.setOutputMarkupPlaceholderTag(true);
		
		if (numberOfRatingSteps != null) {
			textField.add(new RangeValidator(1, numberOfRatingSteps));
		}
	}
	
}

