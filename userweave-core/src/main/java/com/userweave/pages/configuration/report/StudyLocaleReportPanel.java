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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.application.UserWeaveSession;
import com.userweave.components.callback.EventHandler;
import com.userweave.components.callback.EventType;
import com.userweave.components.callback.IEntityEvent;
import com.userweave.components.customModalWindow.CustomModalWindow;
import com.userweave.dao.StudyLocalesGroupDao;
import com.userweave.dao.SurveyExecutionDao;
import com.userweave.domain.EntityBase;
import com.userweave.domain.Study;
import com.userweave.domain.StudyGroup;
import com.userweave.domain.service.SurveyStatisticsService;

public class StudyLocaleReportPanel extends StudyBaseFilterPanel
{
	@SpringBean
	private SurveyExecutionDao surveyExecutionDao;
	
	private static final long serialVersionUID = 1L;
	
	private class LocaleFragment extends Fragment
	{
		private static final long serialVersionUID = 1L;

		public LocaleFragment(String id, String locale, int result)
		{
			super(id, "localeFragment", StudyLocaleReportPanel.this);
			
			add(new Label("locale", new Model<String>(locale)));
			add(new Label("result", new Model<Integer>(result)));
		}
	}
	
	@SpringBean
	private StudyLocalesGroupDao studyLocalesGroupDao;
	
	@SpringBean
	private SurveyStatisticsService statisticService;
	
	public StudyLocaleReportPanel(
		String id, final IModel studyModel, EventHandler addFilterCallback,
		FilterFunctorCallback filterFunctorCallback)
	{
		super(id, studyModel, addFilterCallback);
		
		Iterator<Entry<String, Integer>> i = 
			statisticService.evaluateSurveyExecutionsInLocales(
					surveyExecutionDao.findByStudy(
						getStudy(), 
						filterFunctorCallback.getFilterFunctor())).iterator();
				
		RepeatingView rv = new RepeatingView("locales");
		
		add(rv);
		
		List<String> locales = getSupportedLocalesAsString();
		
		while(i.hasNext())
		{
			Entry<String, Integer> entry = i.next();
			
			String locale = entry.getKey();
			
			locales.remove(locale);
			
			rv.add(new LocaleFragment(rv.newChildId(),locale , entry.getValue()));
		}
		
		for(String locale : locales)
		{
			rv.add(new LocaleFragment(rv.newChildId(), locale , 0));
		}
	}
	
	private List<String> getSupportedLocalesAsString()
	{
		List<Locale> locales = getStudy().getSupportedLocales();
		
		List<String> stringlocales = new ArrayList<String>();
		
		for(Locale locale : locales)
		{
			stringlocales.add(locale.getDisplayLanguage());
		}
		
		return stringlocales;
	}
	
	@Override
	protected CustomModalWindow createFilterModal(final EventHandler addFilterCallback)
	{
		final CustomModalWindow filterModal = new CustomModalWindow("filterModal");
		
		final Study study = getStudy();
		
		filterModal.setPageCreator(new ModalWindow.PageCreator()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page createPage()
			{
				return new LocaleGroupingPage(study, filterModal)
				{
					private static final long serialVersionUID = 1L;

					@Override
					protected void addGroupAndSaveStudy(StudyGroup group)
					{
						group.setStudy(study);
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
}
