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
package com.userweave.pages.configuration.study;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.application.UserWeaveSession;
import com.userweave.components.authorization.AuthOnlyAjaxLink;
import com.userweave.components.customModalWindow.BaseModalWindowPage;
import com.userweave.components.model.LocalizedPropertyModel;
import com.userweave.dao.StudyDao;
import com.userweave.dao.StudyGroupDao;
import com.userweave.domain.Group;
import com.userweave.domain.Study;
import com.userweave.domain.StudyGroup;
import com.userweave.domain.service.ModuleService;
import com.userweave.module.methoden.questionnaire.dao.QuestionnaireConfigurationDao;
import com.userweave.module.methoden.questionnaire.dao.QuestionnaireGroupDao;
import com.userweave.module.methoden.questionnaire.domain.QuestionnaireConfigurationEntity;
import com.userweave.module.methoden.questionnaire.domain.group.QuestionnaireGroup;

/**
 * Page to display all created filters for a study.
 * 
 * @author opr
 */
public class FilterOverviewPage extends BaseModalWindowPage
{
	private static final long serialVersionUID = 1L;

	@SpringBean
	private StudyDao studyDao;
	
	@SpringBean
	private ModuleService moduleService;
	
	@SpringBean
	private QuestionnaireGroupDao questionnaireGroupDao;
	
	@SpringBean
	private QuestionnaireConfigurationDao questionnaireConfigurationDao;
	
	@SpringBean
	private StudyGroupDao studyGroupDao;
	
	/**
	 * Container for the list view of filters.
	 */
	private WebMarkupContainer listViewContainer;
	
	/**
	 * Default constructor.
	 * 
	 * @param window
	 * 		Modal window this page belongs to.
	 * @param studyId
	 * 		Id of study to load filters from.
	 */
	public FilterOverviewPage(ModalWindow window, final int studyId)
	{
		super(window);
		
		setOutputMarkupId(true);
		
		setDefaultModel(new LoadableDetachableModel<Study>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected Study load()
			{
				return studyDao.findById(studyId);
			}
		});
		
		addToForm(listViewContainer = createListView());
	}
	
	/**
	 * Factory method to create a list view of filters.
	 * 
	 * @return
	 * 		A container with a list view as child component.
	 */
	private WebMarkupContainer createListView()
	{
		WebMarkupContainer container = new WebMarkupContainer("listViewContainer");
		
		container.setOutputMarkupId(true);
		
		
		ListView<Group> lv = new ListView<Group>("groups", getFilterGroups())
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Group> item)
			{
				final Group group = item.getModelObject();
				
				item.add(new Label("filterName", group.getName()));
				
				if(group instanceof QuestionnaireGroup)
				{
					item.add(
						new Label(
							"type", 
							new LocalizedPropertyModel(
									group, "question.name", getStudy().getLocale())));
					
					final QuestionnaireGroup questionnaireGroup = 
						(QuestionnaireGroup) item.getModelObject();
					 
				 	final Class<? extends QuestionnaireGroup> class1 = 
				 		questionnaireGroup.getClass();
					
				 	final Integer groupId = questionnaireGroup.getId();
				 	
					item.add(new AuthOnlyAjaxLink("delete")
					{
						private static final long serialVersionUID = 1L;

						@Override
						public void onClick(AjaxRequestTarget target)
						{
							QuestionnaireConfigurationEntity configuration = 
								questionnaireConfigurationDao.findById(
										questionnaireGroup.getQuestion().getConfiguration().getId());
							
							QuestionnaireGroup deleteGroup = questionnaireGroupDao
									.findById(class1, groupId);
							
							configuration.removeFromGroups(deleteGroup);
							
							questionnaireGroupDao.delete(deleteGroup);
							
							replaceListView(target);
							
							UserWeaveSession.get().setHasStateToBeChanged(true);
						}

					});
				}
				else
				{
					item.add(new WebMarkupContainer("type"));
					
					StudyGroup studyGroup = (StudyGroup) item.getModelObject();
					 
				 	final Class<? extends StudyGroup> class1 = studyGroup.getClass();
					
				 	final Integer groupId = studyGroup.getId();
				 	
					item.add(new AuthOnlyAjaxLink("delete")
					{
						private static final long serialVersionUID = 1L;

						@Override
						public void onClick(AjaxRequestTarget target)
						{
							StudyGroup deleteGroup = 
								studyGroupDao.findById(class1, groupId);
							
							getStudy().removeFromGroups(deleteGroup);
							
							studyGroupDao.delete(deleteGroup);
							
							replaceListView(target);
							
							UserWeaveSession.get().setHasStateToBeChanged(true);
						}

					});
				}
			}
		};
		
		container.add(lv);
		
		return container;
	}
	
	private void replaceListView(AjaxRequestTarget target)
	{
		WebMarkupContainer replacement = createListView();
		
		replacement.setOutputMarkupId(true);
		
		listViewContainer.replaceWith(replacement);
		
		listViewContainer = replacement;
		
		target.add(listViewContainer);
	}
	
	private List<Group> getFilterGroups()
	{
		return getStudy().getGroups(moduleService.getModules());
	}
	
	private Study getStudy()
	{
		return (Study) getDefaultModelObject();
	}
	
	@Override
	protected WebMarkupContainer getAcceptButton(String componentId,
			ModalWindow window)
	{
		WebMarkupContainer button = super.getAcceptButton(componentId, window);
		
		button.setVisible(false);
		
		return button;
	}
	
	@Override
	protected IModel<String> getDeclineLabel()
	{
		return new StringResourceModel("close", this, null);
	}
}
