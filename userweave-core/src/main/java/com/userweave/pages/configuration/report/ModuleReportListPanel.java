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
package com.userweave.pages.configuration.report;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.userweave.application.UserWeaveSession;
import com.userweave.components.callback.EventHandler;
import com.userweave.components.callback.EventType;
import com.userweave.components.callback.IEntityEvent;
import com.userweave.components.customModalWindow.CustomModalWindow;
import com.userweave.dao.StudyLocalesGroupDao;
import com.userweave.domain.EntityBase;
import com.userweave.domain.StudyGroup;
import com.userweave.domain.service.GeneralStatistics;
import com.userweave.domain.service.ModuleService;
import com.userweave.module.ModuleConfigurationWithResults;

public class ModuleReportListPanel extends StudyBaseFilterPanel
{
	private static final long serialVersionUID = 1L;

	private final static Logger logger = 
		LoggerFactory.getLogger(ModuleReportListPanel.class);

	@SpringBean
	private StudyLocalesGroupDao studyLocalesGroupDao;
	
	@SpringBean
	private ModuleService moduleService;
	
	//private final FilterFunctor filter;
	private final FilterFunctorCallback filterFunctorCallback;
	
	private final IModel overallStatisticsModel;
		
	public ModuleReportListPanel(
		String id, IModel studyModel, IModel overallStatisticsModel, 
		EventHandler moduleCreatedOrSelectedCallback, 
		EventHandler addFilterCallback, 
		FilterFunctorCallback filterFunctorCallback) 
	{
		super(id, studyModel, addFilterCallback);
		
		this.filterFunctorCallback = filterFunctorCallback;
		
		this.overallStatisticsModel = overallStatisticsModel;
		
		
		add(new ListView("modules", moduleService.getModuleConfigurationsForStudy(getStudy()))
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem item)
			{
				ModuleReportListPanel.this.populateItem(item);
			}	
		});
	}

	@Override
	protected CustomModalWindow createFilterModal(final EventHandler addFilterCallback)
	{
		final CustomModalWindow filterModal = new CustomModalWindow("filterModal");
		
		filterModal.setPageCreator(new ModalWindow.PageCreator()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page createPage()
			{
				return new ModuleReachedGroupingPage(
						getStudy().getLocale(), 
						getStudy().getId(),
						filterModal)
				{
					@Override
					protected void addGroupAndSaveStudy(StudyGroup group)
					{
						group.setStudy(getStudy());
						studyLocalesGroupDao.save(group);

						UserWeaveSession.get().setHasStateToBeChanged(true);
					}	
				};
			}	
		});
		
		filterModal.setWindowClosedCallback(
				new ModalWindow.WindowClosedCallback()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClose(final AjaxRequestTarget target)
			{
				if(UserWeaveSession.get().isHasStateToBeChanged())
				{
					UserWeaveSession.get().setHasStateToBeChanged(false);
					
					addFilterCallback.onEvent(new IEntityEvent()
					{
						@Override
						public EventType getType()
						{
							return null;
						}
						
						@Override
						public AjaxRequestTarget getTarget()
						{
							return target;
						}
						
						@Override
						public EntityBase getEntity()
						{
							return null;
						}
					});
				}
			}
		});
		
		return filterModal;
	}
	
	private Integer getOverallStarted() {
		GeneralStatistics stats = (GeneralStatistics) overallStatisticsModel.getObject();
		return stats.getOverallStarted();
	}
	
	protected void populateItem(final ListItem item) {

//		item.add(
//				new IndicatingAjaxLink("select") {
//
//					@Override
//					public void onClick(AjaxRequestTarget target) {
//						ModuleConfiguration moduleConfiguration = (ModuleConfiguration) item.getModelObject();
//						onModuleSelected(target, moduleConfiguration);
//						
//					}
//				
//				}.add(new Label("name"))
//			);	
		item.add(new Label("name", new PropertyModel(item.getModel(),"name")));
		//item.add(new Label("position", new PropertyModel(item.getModel(),"position")));
		
		final IModel statisticsModel = new LoadableDetachableModel() 
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected Object load() {
				ModuleConfigurationWithResults<?> moduleConfiguration = (ModuleConfigurationWithResults<?>) item.getModelObject();
				long startTime = System.currentTimeMillis();
				GeneralStatistics generalStatistics = getGeneralStatistics(moduleConfiguration);
				long overallTime = System.currentTimeMillis()-startTime;
				logger.info("OVERALLTIME: "+overallTime+ " milliseconds");
				return generalStatistics;
			}
			
		} ;
		
//		item.add(
//				new IndicatingAjaxLink("select2") {
//
//					@Override
//					public void onClick(AjaxRequestTarget target) {
//						ModuleConfiguration moduleConfiguration = (ModuleConfiguration) item.getModelObject();
//						onModuleSelected(target, moduleConfiguration);
//					}
//				
//				}.add(new Label("count",new PropertyModel(statisticsModel,"finished")))
//			);	
		item.add(new Label("count",new PropertyModel(statisticsModel,"finished")));
		
//		item.add(
//				new IndicatingAjaxLink("select3") {
//
//					@Override
//					public void onClick(AjaxRequestTarget target) {
//						ModuleConfiguration moduleConfiguration = (ModuleConfiguration) item.getModelObject();
//						onModuleSelected(target, moduleConfiguration);
//					}
//				
//				}.add(new Label("percent",new PropertyModel(statisticsModel,"finishedToPercent")))
//			);	
		item.add(new Label("percent",new PropertyModel(statisticsModel,"finishedToPercent")));
	}

	protected GeneralStatistics getGeneralStatistics(ModuleConfigurationWithResults moduleConfiguration) {
		GeneralStatistics generalStatistics = 
			moduleConfiguration.getValidResultStatistics(filterFunctorCallback.getFilterFunctor());
		generalStatistics.setOverallStarted(getOverallStarted());
		return generalStatistics;
		//List<TestResultEntityBase> validResults = moduleConfiguration.getValidResults(filter);
		//return surveyStatisticsService.evaluateResultStatistics(getOverallStarted(), validResults, false);
	}
}
