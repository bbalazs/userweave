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
/**
 * 
 */
package com.userweave.module.methoden.iconunderstandability.page.report.bmi;

class BarExplanationModel {
	
	public BarExplanationModel(String title, String explanation, double percentage, int rating) {
		super();
		this.explanation = explanation;
		this.percentage = percentage;
		this.rating = rating;
		this.title = title;
	}
	
	private String title;
	private String explanation;
	private double percentage;
	private int rating;
	
	public String getTitle() {
		return title;
	}
	public String getExplanation() {
		return explanation;
	}
	public double getPercentage() {
		return percentage;
	}
	public int getRating() {
		return rating;
	}
}