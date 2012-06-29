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
package com.userweave.module.methoden.iconunderstandability.page.report.bmi;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

import com.userweave.components.model.LocalizedPropertyModel;
import com.userweave.domain.EntityBase;

/**
 * @author oma
 */
@SuppressWarnings("serial")
public abstract class SelectableItemList extends Panel {

	private final ListView itemListView;
	private final Label noItemsAvailableLabel;
	
	/**
	 * attention: propertyExpression will be used on a LocalizedString!!
	 * 
	 * @param id
	 * @param listModel
	 * @param propertyExpression
	 */
	public SelectableItemList(String id, IModel listModel, final String propertyExpression) {
		
		super(id);
		
		itemListView = new ListView("items", listModel) {

				boolean isOdd = true;
				
				@Override
				protected void populateItem(final ListItem item) {
					
					isOdd = !isOdd;
					
					EntityBase entity = (EntityBase) item.getModelObject();
					
					final int entityId = entity.getId();
					
					item.add(
						new IndicatingAjaxLink("link") {

							@Override
							public void onClick(AjaxRequestTarget target) {
								onSelect(target, item, entityId);								
							}
							
						}.add(
							new Label("name", new LocalizedPropertyModel(item.getModel(), propertyExpression, getLocale()))
						)
					);
					
					// add css class "odd" to odd lines
					if(isOdd)
					{
						item.add(new SimpleAttributeModifier("class", "odd"));	
					}
					// add css class "odd" to even lines
					else
					{
						item.add(new SimpleAttributeModifier("class", "even"));
					}
					
					// add css classes "even_first" to 1st line
					if(item.getIndex() == 0)
						item.add(new AttributeModifier("class", true, new Model(
						"even_first")));
				}
			};
		
		add(itemListView);
		
		// create no item available label with empty list message as default
		noItemsAvailableLabel =  new Label("emptyListMessage", new StringResourceModel("empty_list_message", this, null));
		
		// add label to QuestionsConfigurationPanel
		add (noItemsAvailableLabel);
		
		// set visibility of no items available label
		updateNoItemsAvailableLabelVisibility();
	}
	
	protected abstract void onSelect(AjaxRequestTarget target, ListItem item, int entityId);

	
	/**
	 * Get count of covered items
	 * @return count of covered items
	 */
	public int getItemCount() {
		return itemListView.getViewSize();
	}
	
	/**
	 * Set visibility of noItemsAvailableLabel, which depends on count of selectable list items.
	 * If count of selectable list item is greater than zero hide label, else show it
	 */
	private void updateNoItemsAvailableLabelVisibility() {
		// if SelectableItemList contains item hide noItemsAvailableLabel
		if (getItemCount() > 0) {
			noItemsAvailableLabel.setVisible(false);
		}
		// if SelectableItemList doesn't contains item show noItemsAvailableLabel 
		else {
			noItemsAvailableLabel.setVisible(true);
		}
	}

}

