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
package com.userweave.pages.playground;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.components.validator.ValidationStyleBehavior;
import com.userweave.dao.UserDao;
import com.userweave.module.methoden.questionnaire.page.survey.singleratingpanel.RadioChoicePanel;
import com.userweave.pages.base.BaseUserWeavePage;

@SuppressWarnings("serial")
public class PlaygroundForComponentsPage extends BaseUserWeavePage {
	String label = "";
	List<Row> items = new ArrayList<Row>();
	
	@SpringBean
	private UserDao userDao;
	
//	private FeedbackPanel feedback;
//	private RefreshingView view;
	
	private Integer answerNumber;
	
	public PlaygroundForComponentsPage() 
	{
//		this.setOutputMarkupId(true);
//		
//		add(new UserRegistrationPanel("userPanel", userDao.findByEmail("name@host.de"))
//		{
//			@Override
//			public void onSave(User user, AjaxRequestTarget target)
//			{
//			}
//		});
		
//		add(new UserVerificationPanel("userPanel", userDao.findByEmail("name@host.de"))
//		{
//			@Override
//			public void onSave(User user, AjaxRequestTarget target)
//			{
//			}
//		});
		
		//add(new SlidableTabbedPanel("tabs", getTabs()));
		
//		add(new SlidableAjaxTabbedPanelWithAddTab("tabs", tabs, 2)
//		{
//			
//			@Override
//			public Panel getAddTabPanel(String panelId)
//			{
//				return new DummyPanel(panelId, "add");
//			}
//
//			@Override
//			protected boolean isAddTabActive()
//			{
//				return true;
//			}
//		});
		
//		add(new AjaxEditableLabel("label", new PropertyModel(this, "label")) {
//			@Override
//			public void onSubmit(AjaxRequestTarget target) {
//				super.onSubmit(target);
//				System.out.println("SUBMITTED: "+label);
//			}
//		});
//		
//		add(view = new RefreshingView("items") {
//
//			@Override
//			protected Iterator getItemModels() {
//				return new ModelIteratorAdapter(items.iterator()) {
//					@Override
//					protected IModel model(Object object) {
//						return new CompoundPropertyModel(object);
//					}
//				};
//			}
//
//			@Override
//			protected void populateItem(Item item) {
//				item.add(new AjaxEditableLabel("s1"));
//				item.add(new AjaxEditableLabel("s2"));
//			}
//		});
//		view.setOutputMarkupId(true);
//
//		
		final IModel ratingModel = new Model();
		
		Form form = new Form("form") 
		{
			
			@Override
			public void onSubmit() {
				System.out.println("Model object: " + ratingModel.getObject());
				super.onSubmit();
			}
		};
		
        add(form);
        
        RadioGroup group = new RadioGroup("group", 
    			new Model<Integer>() 
    			{
    			@Override
    			public Integer getObject() {
    				return (Integer)ratingModel.getObject();
    			}

    			@Override
    			public void setObject(Integer object) {	
    					Integer index = object;
    					ratingModel.setObject(object);
    				}			
    	   		}
    	   );
        
        form.add(group);
        
        RepeatingView choices = new RepeatingView("choices");
 	    group.add(choices);
 	    
 	   for (int index = 0; index < 5; index++) 
 	   {
		   String id = Integer.toString(index);
		   choices.add(createRadioChoice(id, index));
	   }
 	   
 	  form.add(new TextField(
 			 "answerNumber", 
 			 new PropertyModel(this, "answerNumber"), Integer.class)
 	  			.add(new ValidationStyleBehavior()));
//        
//	    final TextField field = new TextField("field", new PropertyModel(this, "label"));
//	    field.setOutputMarkupId(true);
//	    form.add(field);
//
//	    form.add(new AjaxButton("submit"){
//
//			@Override
//			protected void onSubmit(AjaxRequestTarget target, Form form) {
//				perhapsAddValue(target);
//				System.out.println("UNWANTED SUBMIT IN AJAX BUTTON");
//				target.addComponent(feedback);
//				
//			}
//		
//			@Override
//			protected void onError(AjaxRequestTarget target, Form form) {
//				System.out.println("UNWANTED SUBMIT IN AJAX BUTTON");
//				target.addComponent(feedback);
//			}
//
//	    });
//	    
//		field.add(
//				new AjaxFormComponentUpdatingBehavior("onupdate") {
//
//					@Override
//					protected void onUpdate(AjaxRequestTarget target) {
//						System.out.println( "C"+ field.getModelObjectAsString()+ "t");
//						target.addComponent(field);
//						perhapsAddValue(target);
//					}
//
//				}
//			);
//
//		form.add(new AjaxCheckBox("validatePromise", new Model()){
//
//                        @Override
//                        protected void onUpdate(AjaxRequestTarget target) {
//                        	System.out.println( "VALIDATEPROMISE:");
//                        }
//                        
//                        
//                });
		/*
		field.add(new AjaxFormSubmitBehavior(form, "onsubmit") {

			@Override
			protected void onError(AjaxRequestTarget target) {
				System.out.println( "E"+ field.getModelObjectAsString()+ "t");
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				System.out.println( "S"+ field.getModelObjectAsString()+ "t");
			}});
*/		
		
		
//		AjaxFormValidatingBehavior.addToAllFormComponents(form, "onchange");
//
//		add(feedback = new FeedbackPanel("feedback"));
//		feedback.setOutputMarkupId(true);
	}
	
	private WebMarkupContainer createRadioChoice(String id, int index) {
		WebMarkupContainer container = new WebMarkupContainer(id);	   
	    container.add(new RadioChoicePanel("content", index));
	    return container;
	}
	
	private void perhapsAddValue(AjaxRequestTarget target) {
		if(label != null && !label.trim().isEmpty()) {
			items.add(new Row(label.trim(), "SSSSS"));
		}
		label = "";
		target.addComponent(this);
	}

	private static class Row implements Serializable {
		private final String s1;
		private final String s2;

		Row(String s1, String s2) {
			this.s1 = s1;
			this.s2 = s2;
		}
	}
	
	private List<ITab> getTabs()
	{
		List<ITab> tabs = new ArrayList<ITab>();
		
		for(int i = 1; i <= 10; i++)
		{
			final String panelContent = "panel " + i;
			
			tabs.add(new AbstractTab(new Model("title" + i))
			{
				@Override
				public Panel getPanel(String panelId)
				{
					return new DummyPanel(panelId, panelContent);
				}
			});
		}
		
		return tabs;
	}
}
