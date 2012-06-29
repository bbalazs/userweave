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

import java.util.List;
import java.util.Locale;

import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.dao.StudyDependendDao;
import com.userweave.domain.OrderedEntityBase;
import com.userweave.domain.StudyState;
import com.userweave.module.methoden.rrt.dao.RrtConfigurationDao;
import com.userweave.module.methoden.rrt.domain.RrtConfigurationEntity;
import com.userweave.module.methoden.rrt.domain.RrtTerm;
import com.userweave.pages.configuration.DisableComponentIfStudyStateNotInitVisitor;
import com.userweave.pages.configuration.module.ModuleConfigurationPanelBase;

/**
 * @author oma
 */
public class RrtTermConfigurationPanel extends 
	ModuleConfigurationPanelBase<RrtConfigurationEntity>
{ 
	private static final long serialVersionUID = 1L;
	
	@SpringBean
	private RrtConfigurationDao dao;	
	
	//private final int index = 0;
	
	public RrtTermConfigurationPanel(String id, Integer configurationId, Locale studyLocale) {
		super(id, configurationId, studyLocale);
		
		add(new RrtTermReorderableListPanel(
				"termPanel", studyIsInState(StudyState.INIT), studyLocale) 
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected List<RrtTerm> getDisplayObjects() {
				return getConfiguration().getTerms();
			}

			@Override
			protected void moveDown(RrtTerm orderedObject, List<RrtTerm> objects) {
				OrderedEntityBase.moveDown(objects, orderedObject);
				getConfigurationDao().save(getConfiguration());				
			}

			@Override
			protected void moveUp(RrtTerm orderedObject, List<RrtTerm> objects) {
				OrderedEntityBase.moveUp(objects, orderedObject);
				getConfigurationDao().save(getConfiguration());				
			}


			@Override
			protected RrtTerm append() {
				RrtTerm term = new RrtTerm();
				//term.setValue(new LocalizedString("VALUE", getStudyLocale()));
				//term.setDescription(new LocalizedString("DESCRIPTION", getStudyLocale()));
				
				getConfiguration().addToTerms(term);
				getConfigurationDao().save(getConfiguration());
				
				return term;
			}
			
			@Override
			protected void delete(RrtTerm objectToDelete, List<RrtTerm> objects) {
				getConfiguration().removeFromTerms(objectToDelete);
				getConfigurationDao().save(getConfiguration());				
			}
			
		});
		//final RepeatingView repeating = new RepeatingView("repeater");
	    
		
        //addTerms(repeating);
		
		//addTermInputForm(repeating);
		
	}
	
	@Override
	protected void onBeforeRender() {
		visitChildren(
			new DisableComponentIfStudyStateNotInitVisitor(
				new PropertyModel(getDefaultModel(), "study.state"))
		);
		super.onBeforeRender();
	}
	/*

	private void addTerms(final RepeatingView repeating) {
		add(repeating);
		
        RrtConfigurationEntity conf = (RrtConfigurationEntity) getModelObject();
        
        List<RrtTerm> terms = conf.getTerms();
        if (terms != null) {
			for (RrtTerm term: terms) {
	        	addTermLabel(repeating, term);
	        }
        }
	}

	private void addTermInputForm(final RepeatingView repeating) {
		
		add(
			new Form("form") { 
	
				private String value = "";
				private String description = "";
				
				// initialize form
				{
					setModel(new CompoundPropertyModel(this));
					add(new TextField("value").setRequired(true));
					add(new TextArea("description").setRequired(true));
					add(new Button("submit"));
				}
				
				@Override
				protected void onSubmit() {					
					RrtTerm term = addTermToConfiguration(value, description);				
					addTermLabel(repeating, term);
					value = "";
					description = "";
				}
			}
		);
	}

	private RrtTerm addTermToConfiguration(String value, String description) {	
		RrtTerm term = new RrtTerm();
		
		LocalizedString localizedValue = term.getValue();
		if (localizedValue == null) {
			localizedValue = new LocalizedString();
			term.setValue(localizedValue);
		}
		localizedValue.setValue(value, getStudyLocale());
		
		LocalizedString localizedDescription = term.getDescription();
		if (localizedDescription == null) {
			localizedDescription = new LocalizedString();
			term.setDescription(localizedDescription);
		}
		localizedDescription.setValue(description, getStudyLocale());
		
		getConfiguration().addToTerms(term);
		saveConfiguration();
		return term;
	}
	
	private void addTermLabel(final RepeatingView repeating, RrtTerm term) {
		
		final WebMarkupContainer item = new WebMarkupContainer(repeating.newChildId());
	    repeating.add(item);
	    
	    final int termId = term.getId();
	   
	    Form editForm = new Form("editForm") {

			// initializer block
	    	{	    
	    		// Term Model
	    		final IModel termModel = 
    		    	new LoadableDetachableModel() {
    		
    					@Override
    					protected Object load() {
    						return getConfiguration().getTermById(termId);
    					}
    				};
    			
	    		TextField value = new TextField("value", new LocalizedPropertyModel(termModel, "value", getStudyLocale()));
				add(value);
	    		
				// submit form onblur
				value.add(
					new AjaxFormSubmitBehavior("onblur") {

						@Override
						protected void onError(AjaxRequestTarget target) {}

						@Override
						protected void onSubmit(AjaxRequestTarget target) {}
					}
				);
	    	    
	    		TextArea textarea = new TextArea("description", new LocalizedPropertyModel(termModel, "description", getStudyLocale()));
				add(textarea);
				textarea.add(
						new AjaxFormSubmitBehavior("onblur") {

							@Override
							protected void onError(AjaxRequestTarget target) {}

							@Override
							protected void onSubmit(AjaxRequestTarget target) {}
						}
					);
	    	
	    		add( new Link("delete") {

	    			@Override
	    			public void onClick() {				
	    				RrtTerm term = (RrtTerm) termModel.getObject();	 
						getConfiguration().removeFromTerms(term);	    				
						saveConfiguration();						
	    				repeating.remove(item);			
	    			}}
	    		);		
	    	}
	    	
	    	@Override
    		protected void onSubmit() {
	    		saveConfiguration();
	    	}
	    };
	    
	    item.add(editForm);
		
	    RrtTermConfigurationPanel.this.index++;
	}
	*/
	@Override
	protected StudyDependendDao<RrtConfigurationEntity> getConfigurationDao() {
		return dao;
	}

}

