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
package com.userweave.components.panelWithDetails;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * @author oma
 */
public abstract class PanelWithDetails extends Panel 
{
	private static final long serialVersionUID = 1L;

	private boolean detailsAreShown = false;
	
	public boolean detailsAreShown() {
		return detailsAreShown;
	}

	public void showDetails() {
		showMore(null);
	}
	
	public PanelWithDetails(String id, boolean detailsAreShown) {
		super(id);
		this.detailsAreShown = detailsAreShown;
	}
	
	public PanelWithDetails(String id) {
		super(id);
		
		setOutputMarkupId(true);
		
		add(
			new AjaxFallbackLink("showMore") 
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick(AjaxRequestTarget target) {					
					showMore(target);
					if (target != null) {
						target.addComponent(PanelWithDetails.this);
					}
				}
				
				@Override
				public boolean isVisible() {
					return !detailsAreShown && hasDetails();
				}
			}
		);
		
		add(
			new AjaxFallbackLink("showLess") 
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick(AjaxRequestTarget target) {				
					showLess(target);					
				}
				
				@Override
				public boolean isVisible() {
					return detailsAreShown && hasDetails();
				}
			}
		);
	}
	
	protected abstract boolean hasDetails();
	
	protected void showMore(AjaxRequestTarget target) {
		detailsAreShown = true;
	};
	
	public void showLess(AjaxRequestTarget target) {
		showLess();
		if (target != null) {
			target.addComponent(PanelWithDetails.this);
		}
		detailsAreShown = false;
	};
	
	protected void showLess() {};
}

