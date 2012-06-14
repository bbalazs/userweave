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

import java.util.Locale;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.userweave.components.model.LocalizedPropertyModel;
import com.userweave.module.methoden.questionnaire.domain.group.DimensionsGroup;
import com.userweave.module.methoden.questionnaire.domain.group.RatingGroup;

/**
 * @author oma
 */
@SuppressWarnings("serial")
public class RatingGroupingDescriptionPanel extends Panel {
	
	public static RatingGroupingDescriptionPanel createMultipleRatingGroupingDescriptionPanel(String id, IModel multipleRatingGroupModel, final Locale locale) {
		IModel model = new CompoundPropertyModel(multipleRatingGroupModel);
		return new RatingGroupingDescriptionPanel(id, model, new LocalizedPropertyModel(model, "ratingTerm.text", locale), locale);
	}
	
	public static RatingGroupingDescriptionPanel createFreeQuestionNumberRatingGroupingDescriptionPanel(String id, IModel freeQuestionNumberRatingGroupModel, final Locale locale) {
		IModel model = new CompoundPropertyModel(freeQuestionNumberRatingGroupModel);
		return new RatingGroupingDescriptionPanel(id, model, new Model(""), locale);
	}
	
	public static RatingGroupingDescriptionPanel createDimensionsGroupingDescriptionPanel(String id, final IModel dimensionsGroupModel, final Locale locale) {
		IModel model = new CompoundPropertyModel(dimensionsGroupModel);
		return new RatingGroupingDescriptionPanel(
			id, 
			model, 
			new Model<String>() 
			{
				@Override
				public String getObject() {
					DimensionsGroup dimensionsGroup = (DimensionsGroup) dimensionsGroupModel.getObject();
					return dimensionsGroup.getAntipodePair().toString(locale);					
				}
			}
		, locale);
	}

	private RatingGroupingDescriptionPanel(String id, IModel model, IModel ratingTarget, final Locale locale) {
		super(id);
		setDefaultModel(model);
		
		RatingGroup group = (RatingGroup) model.getObject();
		
		String operator = "";
		
		if ((group.getLowerBound() != null) && (group.getUpperBound()!=null)) {
			operator = "between";
		} else {
			if ((group.getLowerBound() == null) && (group.getUpperBound()==null)) {
				operator = "missing value";
			}
			else if ((group.getLowerBound() != null)) {
				operator = "greater or equal";
			} else {
				operator = "less or equal";
			}
		}
		
		add(new Label("name"));
		
		add(new Label("ratingTarget", ratingTarget));
		
		add(new Label("operator", operator));
		
		add(new Label("question", new LocalizedPropertyModel(model, "question.name", locale)));
	
		add(new Label("lowerBound"));
	
		add(new Label("and", operator.equals("between") ? "and" : ""));
		
		add(new Label("upperBound"));
	}

}

