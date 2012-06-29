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
package com.userweave.pages.components.slidableajaxtabpanel.addmethodpanel;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.ListChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.resource.PackageResourceReference;

import com.userweave.ajax.IAjaxUpdater;
import com.userweave.ajax.form.AjaxBehaviorFactory;
import com.userweave.pages.components.slidableajaxtabpanel.ChangeTabsCallback;

public abstract class AbstractAddPanel<T> extends Panel implements IAjaxUpdater
{
	private static final long serialVersionUID = 1L;

	/**
	 * Display for error messages.
	 */
	private FeedbackPanel feedbackPanel;
	
	private final Label previewLegendLabel;
	
	private transient T selectedItem;
	
	protected T getSelectedItem()
	{
		return selectedItem;
	}
	
	private String name;
	
	private Image imagePreview;
	
	public AbstractAddPanel(String id, final List<T> choices, final ChangeTabsCallback callback)
	{
		super(id);
		
		try
		{
			selectedItem = choices.get(0);
		}
		catch(IndexOutOfBoundsException e) 
		{
			selectedItem = null;
		}
		
		previewLegendLabel = new Label("preview_legend", getPreviewNameOnUpdate(selectedItem));
		
		ListChoice moduleChoice = new ListChoice(
				"choices", 
				new PropertyModel(AbstractAddPanel.this, "selectedItem"),
				choices,
				getChoiceRenderer());
		
		moduleChoice.add(AjaxBehaviorFactory.getUpdateBehavior("onclick", AbstractAddPanel.this));
		
		moduleChoice.setRequired(true);
		
		moduleChoice.setOutputMarkupId(true);
		
		init(moduleChoice, callback);
	}
	
	public AbstractAddPanel(String id, final IModel choices, final ChangeTabsCallback callback)
	{
		super(id);
		
		try
		{
			selectedItem = (T) ((List)choices.getObject()).get(0);
		}
		catch(IndexOutOfBoundsException e) 
		{
			selectedItem = null;
		}
		
		previewLegendLabel = new Label("preview_legend", getPreviewNameOnUpdate(selectedItem));
		
		ListChoice moduleChoice = new ListChoice(
				"choices", 
				new PropertyModel(AbstractAddPanel.this, "selectedItem"),
				choices,
				getChoiceRenderer());
		
		moduleChoice.add(AjaxBehaviorFactory.getUpdateBehavior("onclick", AbstractAddPanel.this));
		
		moduleChoice.setRequired(true);
		
		moduleChoice.setOutputMarkupId(true);
		
		init(moduleChoice, callback);
	}
	
	private void init(ListChoice choices, final ChangeTabsCallback callback)
	{
		Form form = new Form("form");
		
		add(form);
		
		form.add(choices);
		
		
		imagePreview = new Image(
			"previewImage", 
			new PackageResourceReference(AbstractAddPanel.class, getImageResource(selectedItem)));
		
		imagePreview.setOutputMarkupId(true);
		
		form.add(imagePreview);
		
		
		final TextField methodName = 
			new TextField(
					"itemName", 
					new PropertyModel(AbstractAddPanel.this, "name"));
		
		methodName.setRequired(true);
		
		methodName.setOutputMarkupId(true);
		
		form.add(methodName);
		
		form.add(new AjaxButton("create", form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form form)
			{
				int position = createNewItem(selectedItem, name);
						
				callback.fireChange(target, position);
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, Form form)
			{
				target.addComponent(feedbackPanel);
			}
		});
		
		previewLegendLabel.setOutputMarkupId(true);
		
		form.add(previewLegendLabel);
		
		
		add(feedbackPanel = new FeedbackPanel("feedback"));
		 
		feedbackPanel.setOutputMarkupId(true);
	}
	
//	private AjaxFormComponentUpdatingBehavior getChangeSelectionBehavior(String event)
//	{
//		return new AjaxFormComponentUpdatingBehavior(event)
//		{
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			protected void onUpdate(AjaxRequestTarget target)
//			{
//				previewLegendLabel.setModel(getPreviewNameOnUpdate(selectedItem));
//				previewLegendLabel.modelChanged();
//				
//				replacePreviewImage(target);
//				
//				target.addComponent(previewLegendLabel);
//			}
//		};
//	}
	
	@Override
	public void onUpdate(AjaxRequestTarget target)
	{
		previewLegendLabel.setDefaultModel(getPreviewNameOnUpdate(selectedItem));
		previewLegendLabel.modelChanged();
		
		replacePreviewImage(target);
		
		target.addComponent(previewLegendLabel);
	}
	
	private void replacePreviewImage(AjaxRequestTarget target)
	{
		Image replacement = new Image(
			"previewImage", 
			new PackageResourceReference(AbstractAddPanel.class, getImageResource(selectedItem)));
		
		replacement.setOutputMarkupId(true);
		
		imagePreview.replaceWith(replacement);
		
		imagePreview = replacement;
		
		target.addComponent(imagePreview);
	}
	
	protected abstract IModel getPreviewNameOnUpdate(T selectedItem);
	
	protected abstract String getImageResource(T selectedItem);
	
	protected abstract IChoiceRenderer getChoiceRenderer();
	
	protected abstract int createNewItem(T selectedItem, String name);
	
}
