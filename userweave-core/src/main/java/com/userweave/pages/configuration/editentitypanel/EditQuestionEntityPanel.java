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
package com.userweave.pages.configuration.editentitypanel;

import java.util.List;
import java.util.Locale;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.WindowClosedCallback;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.application.UserWeaveSession;
import com.userweave.components.authorization.AuthOnlyAjaxLink;
import com.userweave.components.callback.EventHandler;
import com.userweave.components.callback.EventType;
import com.userweave.components.callback.IEntityEvent;
import com.userweave.components.customModalWindow.CustomModalWindow;
import com.userweave.components.navigation.breadcrumb.StateChangeTrigger;
import com.userweave.domain.EntityBase;
import com.userweave.domain.LocalizedString;
import com.userweave.domain.Project;
import com.userweave.domain.Study;
import com.userweave.domain.StudyState;
import com.userweave.domain.service.QuestionService;
import com.userweave.module.ModuleConfiguration;
import com.userweave.module.methoden.questionnaire.dao.QuestionDao;
import com.userweave.module.methoden.questionnaire.dao.QuestionnaireConfigurationDao;
import com.userweave.module.methoden.questionnaire.domain.question.Question;
import com.userweave.pages.components.slidableajaxtabpanel.ChangeTabsCallback;
import com.userweave.pages.configuration.EnabledInStudyState;
import com.userweave.pages.configuration.base.IConfigReportStateChanger.UiState;
import com.userweave.pages.configuration.editentitypanel.copydialog.BrowseEntityWebPage;
import com.userweave.pages.configuration.editentitypanel.webpages.FilterWebPage;

public abstract class EditQuestionEntityPanel extends ReorderableEntityPanel<Question>
{
	private static final long serialVersionUID = 1L;
	
	@EnabledInStudyState(states={StudyState.FINISHED})
	private abstract class StudyStateFinishedDependendLink extends AuthOnlyAjaxLink
	{
		private static final long serialVersionUID = 1L;

		public StudyStateFinishedDependendLink(String id)
		{
			super(id);
		}
	}
	
	@SpringBean
	private QuestionService questionService;
	
	@SpringBean
	private QuestionDao questionDao;
	
	@SpringBean
	private QuestionnaireConfigurationDao configurationDao;
	
	private CustomModalWindow filterModalWindow;
	
	private ChangeTabsCallback changeTabsCallback;
	
	private int moduleIndex;
	
	public EditQuestionEntityPanel(
		String id, IModel entityModel, 
		final StateChangeTrigger trigger, 
		final EventHandler addFilterCallback,
		ChangeTabsCallback changeTabsCallback,
		int moduleIndex)
	{
		super(id, entityModel, null);
		
		this.changeTabsCallback = changeTabsCallback;
		
		this.moduleIndex = moduleIndex;
		
		addFilterModal(addFilterCallback);
		
		add(new StudyStateFinishedDependendLink("filter")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				filterModalWindow.show(target);
			}
			
			@Override
			public boolean isEnabled()
			{
				return trigger.getState() == UiState.REPORT;
			}
			
			@Override
			protected void onBeforeRender()
			{
				if(! isEnabled() && trigger.getState() != UiState.REPORT)
				{
					setToolTipType(ToolTipType.VIEW);
				}
				
				super.onBeforeRender();
			}
		});
	}

	private void addFilterModal(final EventHandler addFilterCallback)
	{
		filterModalWindow = new CustomModalWindow("filterDialog");
		
		final Locale locale = getQuestion().getConfiguration().getStudy().getLocale();
		
		filterModalWindow.setPageCreator(new ModalWindow.PageCreator()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page createPage()
			{
				return new FilterWebPage(getQuestion(), locale, filterModalWindow)
				{
					@Override
					protected void onAfterAdd()
					{
						UserWeaveSession.get().setHasStateToBeChanged(true);
					}
				};
			}	
		});
		
		filterModalWindow.setWindowClosedCallback(
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
		
		add(filterModalWindow);
	}
	
	@Override
	protected IModel getCopyModalTitle()
	{
		return new StringResourceModel("copyQuestion", this, null);
	}
	
	/**
	 * 
	 * @param callback
	 * 		CALLBACK IST NULL!!
	 */
	@Override
	protected WebPage getCopyWebPage(EventHandler callback, ModalWindow window)
	{	
		final Locale locale = getQuestion().getConfiguration().getStudy().getLocale();
		
		String newName = new StringResourceModel("copy_of", this, null).getString() + 
						 "_" + getQuestion().getName().getValue(locale);
		
		return new BrowseEntityWebPage(newName, window, 3)
		{
			@Override
			protected void onCopy(String copyName, Project destinyProject,
					Study destinyStudy, ModuleConfiguration configuration)
			{
				Question copy = getQuestion().copy();
				
				copy.setName(new LocalizedString(copyName, locale));
				
				questionService.copyQuestionInConfiguration(configuration.getId(), copy);
			}
		};
	}

	@Override
	protected void onClose(AjaxRequestTarget target)
	{
		changeTabsCallback.fireChange(target, moduleIndex);
	}
	
	@Override
	protected AuthOnlyAjaxLink getEditLink(String id)
	{
		return new StudyStateInitDependentLink(id)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				onEditLinkActivate(target);
			}
		};
	}
	
	/**
	 * 
	 * @param callback
	 * 		CALLBACK IST NULL!!
	 */
	@Override
	protected WebPage getEditWebPage(final EventHandler callback, ModalWindow window)
	{
		return new EditQuestionEntityWebPage(getDefaultModel(), window);
	}

	/**
	 * 
	 * @param callback
	 * 		CALLBACK IST NULL!!
	 */
	@Override
	protected WindowClosedCallback getEditWindowClosedCallback(final EventHandler callback)
	{
		return new ModalWindow.WindowClosedCallback()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClose(AjaxRequestTarget target)
			{
				onEdit(target);
			}
		};
	}
	
	protected abstract void onEdit(AjaxRequestTarget target);
	
	@Override
	protected void onDelete(AjaxRequestTarget target)
	{
		questionService.deleteQuestion(getQuestion().getId());
	}

	/**
	 * Get the questions, which backs this panel.
	 * 
	 * @return
	 */
	private Question getQuestion()
	{
		return getEntity();
	}

	@Override
	protected String getEntityName()
	{
		Question q = getQuestion();
		Locale l =  q.getConfiguration().getStudy().getLocale();
		
		return q.getName().getValue(l);
	}
	
	@Override
	protected IModel getStudyStateModel()
	{
		return new PropertyModel(getQuestion(), "configuration.study.state");
	}

	@Override
	protected void moveUp(AjaxRequestTarget target)
	{
		List<Question> questions = 
			configurationDao.findById(getQuestion().getConfiguration().getId()).getQuestions();
		
		getQuestion().moveUp(questions);
		
		for (Question question : questions) {
			questionDao.save(question);
		}
		
		moveModuleUp(target);
		
		//target.addComponent(tabbedPanel);
	}

	@Override
	protected void moveDown(AjaxRequestTarget target)
	{
		List<Question> questions = 
			configurationDao.findById(getQuestion().getConfiguration().getId()).getQuestions();
		
		getQuestion().moveDown(questions);
		
		for (Question question : questions) 
		{
			questionDao.save(question);
		}
		
		moveModuleDown(target);
		
		//target.addComponent(tabbedPanel);
	}
	
	protected abstract void moveModuleUp(AjaxRequestTarget target);
	
	protected abstract void moveModuleDown(AjaxRequestTarget target);
}
