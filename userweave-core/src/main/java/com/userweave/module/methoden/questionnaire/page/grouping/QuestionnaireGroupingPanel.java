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
package com.userweave.module.methoden.questionnaire.page.grouping;



import java.util.List;
import java.util.Locale;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.components.authorization.AuthOnlyDropDownChoice;
import com.userweave.dao.StudyGroupDao;
import com.userweave.dao.StudyLocalesGroupDao;
import com.userweave.domain.StudyGroup;
import com.userweave.module.methoden.questionnaire.dao.QuestionnaireConfigurationDao;
import com.userweave.module.methoden.questionnaire.dao.QuestionnaireGroupDao;
import com.userweave.module.methoden.questionnaire.domain.QuestionnaireConfigurationEntity;
import com.userweave.module.methoden.questionnaire.domain.group.QuestionnaireGroup;
import com.userweave.presentation.model.SpringLoadableDetachableModel;

/**
 * @author oma
 */
@SuppressWarnings("serial")
public class QuestionnaireGroupingPanel extends Panel {

	// FIXME: put Study Grouping details to different panel. This is a hack here!
	//private Question selectedQuestion;
	private GroupType selectedGroupType;
	
	private Component addGroupPanel;
	
	@SpringBean
	private QuestionnaireConfigurationDao questionnaireConfigurationDao;
	

	@SpringBean
	private QuestionnaireGroupDao questionnaireGroupDao;
	
	@SpringBean
	private StudyGroupDao studyGroupDao;
	
	@SpringBean
	private StudyLocalesGroupDao studyLocalesGroupDao;
	
	public QuestionnaireGroupingPanel(String id, final QuestionnaireConfigurationEntity configuration) {
		super(id);
		
		setOutputMarkupId(true);

		//final int configurationId = configuration.getId();
		
		final Locale locale = configuration.getStudy().getLocale();
		
		setDefaultModel(new SpringLoadableDetachableModel(questionnaireConfigurationDao, configuration));
		
		add(
			new ListView("moduleGroups", new PropertyModel(getDefaultModel(), "groups")) {

				@Override
				protected void populateItem(ListItem item) {
					
					if(item.getIndex() % 2 == 1)
					{
						item.add(new AttributeModifier("class", true, new Model("oddRow")));
					}
					
					Panel groupingDescriptionPanel = new QuestionnaireGroupingPanelFactoryImpl().getGroupingDescriptionPanel(item.getModel(), locale);
					if (groupingDescriptionPanel != null) {					
						item.add(groupingDescriptionPanel);
					} 
					else {
						item.add(new Label("group", "EMPTY !"));
					}
					
					Form form = new Form("deleteForm");

					item.add(form);
					
					QuestionnaireGroup group = (QuestionnaireGroup) item.getModelObject();
				 
				 	final Class<? extends QuestionnaireGroup> class1 = group.getClass();
					
				 	final Integer groupId = group.getId();

					form.add(
						new AjaxButton("delete", form) {

							@Override
							protected void onSubmit(AjaxRequestTarget target, Form form) {
								QuestionnaireConfigurationEntity configuration = getConfiguration();
								QuestionnaireGroup group = questionnaireGroupDao.findById(class1, groupId);
								configuration.removeFromGroups(group);
								questionnaireGroupDao.delete(group);
								target.addComponent(QuestionnaireGroupingPanel.this);
							}
							
							@Override
							protected void onError(AjaxRequestTarget target, Form<?> form)
							{
							}
							
						}
					);
				
				}
				
			}
		);
		
		add(new ListView("studyGroups", new PropertyModel(getDefaultModel(), "study.groups")) 
		{
			@Override
			protected void populateItem(ListItem item) {
				
				if(item.getIndex() % 2 == 1)
				{
					item.add(new AttributeModifier("class", true, new Model("oddRow")));
				}
				
				Panel groupingDescriptionPanel = new StudyGroupingFactoryImpl().getGroupingDescriptionPanel(item.getModel(), locale);
				if (groupingDescriptionPanel != null) {					
					item.add(groupingDescriptionPanel);
				} 
				else {
					item.add(new Label("group", "EMPTY !"));
				}
				
				Form form = new Form("deleteForm");

				item.add(form);
				
				StudyGroup group = (StudyGroup) item.getModelObject();
			 
			 	final Class<? extends StudyGroup> class1 = group.getClass();
				
			 	final Integer groupId = group.getId();

				form.add(
					new AjaxButton("delete", form) {

						@Override
						protected void onSubmit(AjaxRequestTarget target, Form form) {
							StudyGroup group = studyGroupDao.findById(class1, groupId);
							getConfiguration().getStudy().removeFromGroups(group);
							studyGroupDao.delete(group);
							target.add(QuestionnaireGroupingPanel.this);
						}
						
						@Override
						protected void onError(AjaxRequestTarget target, Form<?> form)
						{
						}
						
					}
				);

			}
			
		});
		
		Form form = new Form("form") {};		
		
		add(form);
		
		AuthOnlyDropDownChoice choice = new AuthOnlyDropDownChoice(
				"groupTypes", 
				new PropertyModel(this, "selectedGroupType"), 
				getSelectionObjects(configuration, locale),
				new IChoiceRenderer() {

					@Override
					public Object getDisplayValue(Object object) {
						return ((GroupType) object).getName().getObject();
					}

					@Override
					public String getIdValue(Object object, int index) {
						return Integer.toString(index);
					}
					
				}
			);
		
		choice.setRequired(true);
		
		choice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) 
			{
				if (selectedGroupType instanceof QuestionnaireGroupType) {
					replaceGroupingPanel(
						target, 
						new QuestionnaireGroupingPanelFactoryImpl().createGroupingPanel("addGroupPanel", (QuestionnaireGroupType) selectedGroupType, locale,
							new GroupAddedCallback<QuestionnaireGroup>() 
							{
								@Override
								public void onAdd(AjaxRequestTarget target, QuestionnaireGroup group) 
								{
									addGroupAndSaveConfiguration(group);
									removeGroupingPanel(target);			
								}
							}
						)
					);
				}
				else if (selectedGroupType instanceof StudyGroupType) {
					replaceGroupingPanel(
							target, 
							new StudyGroupingFactoryImpl().createGroupingPanel("addGroupPanel", (StudyGroupType) selectedGroupType, locale,
								new GroupAddedCallback<StudyGroup>() 
								{
									@Override
									public void onAdd(AjaxRequestTarget target, StudyGroup group) 
									{
										addGroupAndSaveStudy(group);
										removeGroupingPanel(target);			
									}
								}
							)
					);
				}
			}
		});
		
		form.add(choice);
	
		addGroupPanel = new WebMarkupContainer("addGroupPanel");
		
		add(addGroupPanel);
		
		addGroupPanel.setOutputMarkupId(true);
		
	}

	private List<GroupType> getSelectionObjects(QuestionnaireConfigurationEntity configuration, Locale locale) {
		List<GroupType> groupTypes = new QuestionnaireGroupingPanelFactoryImpl().getGroupTypes(configuration.getQuestions(), locale);		
		groupTypes.addAll(new StudyGroupingFactoryImpl().getGroupTypes(configuration.getStudy()));
		return groupTypes;	
	}

	private QuestionnaireConfigurationEntity getConfiguration() {
		return (QuestionnaireConfigurationEntity) getDefaultModelObject();
	};

	private void addGroupAndSaveConfiguration(QuestionnaireGroup group) {
		getConfiguration().getGroups().add(group);
		questionnaireConfigurationDao.save(getConfiguration());
	}	

	private void addGroupAndSaveStudy(StudyGroup group) {
		group.setStudy(getConfiguration().getStudy());
		studyLocalesGroupDao.save(group);
	}	

	private void replaceGroupingPanel(AjaxRequestTarget target, Component replacement) {
		replacement.setOutputMarkupId(true);
		addGroupPanel.replaceWith(replacement);
		addGroupPanel = replacement;
		target.addComponent(addGroupPanel);
	}

	private void removeGroupingPanel(AjaxRequestTarget target) {
		replaceGroupingPanel(target, new WebMarkupContainer("addGroupPanel"));			
		target.addComponent(QuestionnaireGroupingPanel.this);
	}

}

