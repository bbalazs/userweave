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

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.WindowClosedCallback;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.application.UserWeaveSession;
import com.userweave.components.authorization.AuthOnlyAjaxLink;
import com.userweave.components.callback.EntityEvent;
import com.userweave.components.callback.EventHandler;
import com.userweave.components.csvexportlink.CsvExportAdminLink;
import com.userweave.components.customModalWindow.CustomModalWindow;
import com.userweave.components.navigation.breadcrumb.StateChangeTrigger;
import com.userweave.dao.StudyDao;
import com.userweave.domain.Project;
import com.userweave.domain.Role;
import com.userweave.domain.Study;
import com.userweave.domain.StudyState;
import com.userweave.domain.service.StudyService;
import com.userweave.module.ModuleConfiguration;
import com.userweave.pages.configuration.EnabledInStudyState;
import com.userweave.pages.configuration.base.IConfigReportStateChanger.UiState;
import com.userweave.pages.configuration.editentitypanel.copydialog.BrowseEntityWebPage;
import com.userweave.pages.configuration.study.FilterOverviewPage;
import com.userweave.pages.configuration.study.details.AreYouSurePage;
import com.userweave.pages.configuration.study.selection.EditStudyPage;
import com.userweave.pages.test.DisplaySurveyUI;

/**
 * Base class for study functionality.
 * 
 * IMPORTANT: This class overrides the base makrup!
 * 
 * @author opr
 */
public abstract class EditStudyEntityPanel extends CopyEntityPanel<Study> 
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
	private StudyService studyService;
	
	@SpringBean
	private StudyDao studyDao;
	
	/**
	 * Dialog to confirm, that the study state
	 * is to be changed.
	 */
	private final CustomModalWindow changeStateModalWindow, notRegiteredModalWindow; 
	
	private final EventHandler callback;
	
	/**
	 * Default constructor
	 * 
	 * @param id
	 * 		Component markup id.
	 * @param entity
	 * 		Study entity.
	 * @param callback
	 * 		Callback to fire on study event.
	 */
	public EditStudyEntityPanel(String id, IModel<Study> entityModel,
			EventHandler callback, StateChangeTrigger trigger)
	{
		super(id, entityModel, callback);
		
		this.callback = callback;
		
		add(getChangeStateLink());
		add(getResetLink(callback));
		add(getPreviewLink(callback));
		
		add(changeStateModalWindow = getChangeStateModal(callback));
		add(notRegiteredModalWindow = getNotRegisteredModal("notRegisteredModal", callback));
		
		createFilter(trigger.getState());
		
		addChangeViewStateDropDown(trigger, callback);
		
		add(new CsvExportAdminLink("csvExportLink", entityModel));
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
	 * A study can be in the state INIT, RUNNING, FINISH. This
	 * Link triggers the state transition.
	 * 
	 * @return
	 * 		An abstract link.
	 */
	private AbstractLink getChangeStateLink()
	{	
		AjaxLink<Void> changeState = new AjaxLink<Void>("changeState")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				// see #974 for workflow.
				if(studyService.isAtLeastOneAdminRegistered(
						getStudy().getParentProject()))
				{
					changeStateModalWindow.show(target);
				}
				else
				{
					notRegiteredModalWindow.show(target);
				}
			}

			@Override
			public boolean isEnabled()
			{
				return UserWeaveSession.get().getUser().hasRole(Role.PROJECT_ADMIN) &&
					   getStudy().getState() != StudyState.FINISHED;
			}
		};
		
		IModel<String> labelModel;
		
		if(getStudy().getState() != StudyState.INIT)
		{
			labelModel = new StringResourceModel("deactivate_study", this, null);
		}
		else
		{
			labelModel = new StringResourceModel("activate_study", this, null);
		}
		
		changeState.add(new Label("action", labelModel));
		
		return changeState;
	}
	
	/**
	 * Displays a link to reset the study state to predecessor state.
	 * 
	 * @important Link is only visible, if user is portal admin.
	 * 
	 * @param callback
	 * 		Callback to fire on study event.
	 * 
	 * @return
	 * 		An ajax link.
	 */
	private AjaxLink<Void> getResetLink(final EventHandler callback)
	{	
		AjaxLink<Void> resetLink = new AjaxLink<Void>("resetState")
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick(AjaxRequestTarget target)
			{
				Study study = getStudy();
				
				resetState(study);
				
				// Fire purged event, because this event deletes
    			// nothing and redirects to select project panel
    			EntityEvent.Purged(target, study).fire(callback);
			}
			
			@Override
			public boolean isVisible()
			{
				return UserWeaveSession.get().isAdmin() && 
					   getStudy().getState() != StudyState.INIT;
			}
		};
		
		IModel<String> resetModel;
		
		if(getStudy().getState() != StudyState.RUNNING)
		{
			resetModel = new StringResourceModel("activate_study", this, null);
		}
		else
		{
			resetModel = new StringResourceModel("init_study", this, null);
		}
		
		resetLink.add(new Label("resetStateLabel", resetModel));
		
		return resetLink;
	}
	
	/**
	 * Returns a link to preview the study.
	 * 
	 * @return
	 * 		An abstract link component, depending on registered admins.
	 */
	private AbstractLink getPreviewLink(final EventHandler callback)
	{
		PageParameters parameters = new PageParameters();
		
		AbstractLink link;
		
		IModel<String> linkLabelModel = 
			new StringResourceModel("studylink", EditStudyEntityPanel.this, null);
		
		// see #974 for workflow.
		if(studyService.isAtLeastOneAdminRegistered(getStudy().getParentProject()))
		{
			parameters.set(0, getStudy().getHashCode());
			
			if(getStudy().getState() == StudyState.INIT)
			{				
				linkLabelModel = 
					new StringResourceModel("preview", EditStudyEntityPanel.this, null);
			}
 			
			link = new BookmarkablePageLink<Void>("preview", DisplaySurveyUI.class, parameters);
		}
		else
		{	
			linkLabelModel = 
				new StringResourceModel("preview", EditStudyEntityPanel.this, null);
			
			link = new AjaxLink<Void>("preview")
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick(AjaxRequestTarget target)
				{
					notRegiteredModalWindow.show(target);
				}
			};
		}
		
		link.add(new Label("previewLabel", linkLabelModel));
		
		link.setEnabled(
			getStudy().getState() != StudyState.FINISHED &&
			! getStudy().isDeleted());
		
		return link; 
	}
	
	/**
	 * Factory method to create a dialog for displaying the 
	 * "not registered" message.
	 * 
	 * @param id
	 * 		Component markup id.
	 * @param callback
	 * 		Callback to fire on study event.
	 * 
	 * @return
	 * 		A modal window.
	 */
	private CustomModalWindow getNotRegisteredModal(String id, final EventHandler callback)
	{	
		final CustomModalWindow window = new CustomModalWindow(id);
		
		window.setPageCreator(new ModalWindow.PageCreator()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page createPage()
			{
				return new RedirectOnNotRegisteredWebPage(window)
				{
					private static final long serialVersionUID = 1L;

					@Override
					protected void onLinkClicked(AjaxRequestTarget target)
					{
						UserWeaveSession.get().setHasStateToBeChanged(true);

						window.close(target);
					}
				};
			}
		});
		
		window.setWindowClosedCallback(new ModalWindow.WindowClosedCallback()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClose(AjaxRequestTarget target)
			{
				if(UserWeaveSession.get().isHasStateToBeChanged())
				{
					UserWeaveSession.get().setHasStateToBeChanged(false);
					
					EntityEvent.Selected(target, UserWeaveSession.get().getUser()).fire(callback);
				}
			}
		});
		
		return window;
	}
	
	/**
	 * Returns a dialog for state change confirmation.
	 * 
	 * @param callback
	 * 		Callback to fire on study event.
	 * @return
	 * 		A modal window.
	 */
	private CustomModalWindow getChangeStateModal(final EventHandler callback)
	{
		final CustomModalWindow changeStateModal = 
			new CustomModalWindow("changeStateModal");
		
		final StudyState state = getStudy().getState();
		
		changeStateModal.setPageCreator(new ModalWindow.PageCreator() 
		{
			private static final long serialVersionUID = 1L;

			public Page createPage() 
            {
                return new AreYouSurePage(changeStateModal, state) 
                {
                	private static final long serialVersionUID = 1L;
                	
					@Override
					protected void onOk(AjaxRequestTarget target) 
					{
						UserWeaveSession.get().setHasStateToBeChanged(true);
					}
					
					@Override
					protected IModel<String> getAcceptLabel()
					{
						if(state == StudyState.INIT)
						{
							return new StringResourceModel("yes_activate",EditStudyEntityPanel.this,null);
						}
						
						return new StringResourceModel("yes_close",EditStudyEntityPanel.this,null);
					}
					
					@Override
					protected IModel<String> getDeclineLabel()
					{
						if(state == StudyState.INIT)
						{
							return new StringResourceModel("no_activate",EditStudyEntityPanel.this,null);
						}
						
						return new StringResourceModel("no_close",EditStudyEntityPanel.this,null);
					}
                };
            }
        });

		changeStateModal.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() 
		{
			private static final long serialVersionUID = 1L;

            public void onClose(AjaxRequestTarget target) 
            {	
            	boolean hasStateToBeChanged = UserWeaveSession.get().isHasStateToBeChanged();
            	
        		if (hasStateToBeChanged == true) 
        		{
        			Study study = getStudy();
        			changeState(study);
        			UserWeaveSession.get().setHasStateToBeChanged(false);
        			
        			// Fire selected event to reload configuration
        			// page of study, since we want to stay
        			EntityEvent.Selected(target, study).fire(callback);
        		}
            }
        });

		changeStateModal.setCloseButtonCallback(new ModalWindow.CloseButtonCallback() 
		{
			private static final long serialVersionUID = 1L;

            public boolean onCloseButtonClicked(AjaxRequestTarget target) 
            {
                return true;
            }
        });
		
		return changeStateModal;
	}
	
	/**
	 * A component can be in config or report state. This drop down
	 * triggers the event to change the view accordingly.
	 * 
	 * @param trigger
	 * 		StateChangeTrigger to delegate event to registered components.
	 * @param studyCallback
	 * 		Callback to fire on study event,
	 */
	private void addChangeViewStateDropDown(
		final StateChangeTrigger trigger, final EventHandler studyCallback)
	{
		List<UiState> states = new ArrayList<UiState>();
		
		states.add(UiState.CONFIG);
		states.add(UiState.REPORT);
		
		IModel<UiState> preSelect = new Model<UiState>(trigger.getState());
		
		final DropDownChoice<UiState> dropdown = 
			new DropDownChoice<UiState>(
					"changeView", 
					preSelect, 
					states, 
					new ChoiceRenderer()
					{
						private static final long serialVersionUID = 1L;

						@Override
						public Object getDisplayValue(Object object)
						{
							UiState state = (UiState) object;
							
							if(state == UiState.CONFIG)
							{
								return new StringResourceModel(
									"ui_state_config", 
									EditStudyEntityPanel.this, 
									null).getString();
							}
							else
							{
								return new StringResourceModel(
										"ui_state_report", 
										EditStudyEntityPanel.this, 
										null).getString();
							}
						}
						
						@Override
						public String getIdValue(Object object, int index)
						{
							return ((UiState) object).toString();
						}
					});
		
		dropdown.setEnabled(getStudy().getState() != StudyState.INIT);
		
		dropdown.add(new AjaxFormComponentUpdatingBehavior("onchange") 
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) 
			{
				if(getStudy().getState() != StudyState.INIT)
				{
					UiState state = dropdown.getModelObject();
					
					trigger.triggerChange(state, target, studyCallback);
				}
			}
		});
		
		add(dropdown);
		
	}
	
	/**
	 * Shortcut to the model object.
	 * 
	 * @return
	 * 		The underlying study.
	 */
	private Study getStudy() 
	{
		return getEntity();
	}
	
	/**
	 * Changes the study state to next state. 
	 * (preperation -> active, active -> finished).
	 * 
	 * @param study
	 * 		Study which state should be changed
	 */
	private void changeState(Study study)
	{
		if(study.getState() == StudyState.INIT)
		{
			study.setRunning();
			studyDao.save(study);
		}
		else if(study.getState() == StudyState.RUNNING)
		{
			study.setFinished();
			studyDao.save(study);
			studyService.generateStudyResults(study.getId());
		}
	}
	
	private void resetState(Study study)
	{
		if(study.getState() == StudyState.FINISHED)
		{
			study.setRunning();
		}
		else if(study.getState() == StudyState.RUNNING)
		{
			study.setState(StudyState.INIT);
		}
		
		studyDao.save(study);
	}
	
	@Override
	protected IModel<String> getCopyModalTitle()
	{
		return new StringResourceModel("copyStudy", this, null);
	}
	
	@Override
	protected WebPage getCopyWebPage(EventHandler callback,
			ModalWindow window)
	{
		String newName = new StringResourceModel("copy_of", this, null).getString() + 
						 "_" + getStudy().getName();
		
		return new BrowseEntityWebPage(newName, window, 1)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onCopy(String copyName, Project destinyProject,
					Study destinyStudy, ModuleConfiguration configuration)
			{
				studyService.copy(getStudy(), copyName, destinyProject);
			}
		};
	}

	@Override
	protected WebPage getEditWebPage(final EventHandler callback,
			ModalWindow window)
	{
		return new EditStudyPage(window, getStudy())
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onFinish(AjaxRequestTarget target, Study study) 
			{
//				studyDao.save(study);
//				EntityEvent.Updated(target, study).fire(callback);
			}
			
			@Override
			protected IModel<String> getAcceptLabel()
			{
				return new StringResourceModel("save", EditStudyEntityPanel.this, null);
			}
		};
	}

	@Override
	protected WindowClosedCallback getEditWindowClosedCallback(final EventHandler callback)
	{
		return new ModalWindow.WindowClosedCallback()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClose(AjaxRequestTarget target)
			{
				studyDao.save(getStudy());
				EntityEvent.Updated(target, getStudy()).fire(callback);
			}
		};
	}
	
	@Override
	protected void onDelete(AjaxRequestTarget target)
	{
		studyService.delete((Study) getDefaultModelObject());
	}

	@Override
	protected IModel<StudyState> getStudyStateModel()
	{
		return new PropertyModel<StudyState>(getDefaultModelObject(), "state");
	}

	@Override
	protected String getEntityName()
	{
		return getStudy().getName();
	}
	
	@Override
	protected AuthOnlyAjaxLink getDeleteLink(String id)
	{
		if(getStudy().isDeleted())
		{
			AuthOnlyAjaxLink link = new AuthOnlyAjaxLink(id)
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick(AjaxRequestTarget target)
				{
					Study study = getStudy();
					study.setDeletedAt(null);
					
					studyDao.save(study);
					
					onAfterDelete(target, callback, null);
				}
			};
			
			link.add(new Label("deleteLabel", new StringResourceModel("undelete", this, null)));
			
			return link;
		}
		else
		{
			return super.getDeleteLink(id);
		}
	}
	
	/**
	 * @param positionBeforeDeletion
	 * 		Ignore this param
	 */
	@Override
	protected void onAfterDelete(AjaxRequestTarget target, EventHandler callback, Integer positionBeforeDeletion)
	{
		EntityEvent.Purged(target, getStudy()).fire(callback);
	}
	
	/**
	 * Creates a filter modal window to display the list of
	 * selected filters and a link to display this window.
	 */
	private void createFilter(final UiState uiState)
	{
		final CustomModalWindow filterOverviewModal = 
			new CustomModalWindow("filterOverviewModal");
		
		add(new StudyStateFinishedDependendLink("filterOverview")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				filterOverviewModal.show(target);
			}
			
			@Override
			public boolean isEnabled()
			{
				return UserWeaveSession.get().getUser().hasRole(Role.PROJECT_ADMIN) &&
					   getStudy().getState() == StudyState.FINISHED &&
					   uiState == UiState.REPORT;
			}
			
			@Override
			protected void onBeforeRender()
			{
				if(! isEnabled() && uiState != UiState.REPORT)
				{
					setToolTipType(ToolTipType.VIEW);
				}
				
				super.onBeforeRender();
			}
		});
		
		add(filterOverviewModal); 
		
		filterOverviewModal.setPageCreator(new ModalWindow.PageCreator()
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			public Page createPage()
			{
				return new FilterOverviewPage(filterOverviewModal, getStudy().getId());
			}
		});
		
		filterOverviewModal.setWindowClosedCallback(new ModalWindow.WindowClosedCallback()
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClose(AjaxRequestTarget target)
			{
				if(UserWeaveSession.get().isHasStateToBeChanged())
				{
					UserWeaveSession.get().setHasStateToBeChanged(false);
					onFilterWindowClose(target);
				}
			}
		});
	}
	
	protected abstract void onFilterWindowClose(AjaxRequestTarget target);
}
