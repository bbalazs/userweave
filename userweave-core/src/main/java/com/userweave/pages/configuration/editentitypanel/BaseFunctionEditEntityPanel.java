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

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.WindowClosedCallback;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

import com.userweave.application.UserWeaveSession;
import com.userweave.components.authorization.AuthOnlyAjaxLink;
import com.userweave.components.callback.EventHandler;
import com.userweave.components.customModalWindow.CustomModalWindow;
import com.userweave.domain.EntityBase;
import com.userweave.domain.OrderedEntityBase;
import com.userweave.domain.StudyState;
import com.userweave.pages.configuration.DisableEventLinkVisitor;

/**
 * EditEntityPanel with the base functions edit and delete.
 * 
 * @author opr
 *
 */
public abstract class BaseFunctionEditEntityPanel<T extends EntityBase> extends Panel
{
	private static final long serialVersionUID = 1L;
	
	private int positionBeforeDeletion;
	
	/**
	 * Dialogs for basic functions.
	 */
	protected CustomModalWindow editModal, deleteModal;
	
	/**
	 * Default constructor.
	 * 
	 * @param id
	 * 		Component id.
	 * @param entity
	 * 		Entity to back this panel.
	 * @param callback
	 * 		Callback to fire on event.
	 */
	public BaseFunctionEditEntityPanel(String id, IModel<T> entityModel, EventHandler callback)
	{
		super(id, entityModel);
		
		add(editModal = getEditModal(callback));
		
		add(getEditLink("edit"));
		
		
		add(deleteModal = getDeleteModal(callback));
		
		add(getDeleteLink("delete"));
	}
	
	/**
	 * Returns the entity, which backs this panel.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected T getEntity()
	{
		return (T) getDefaultModelObject();
	}
	
	/**
	 * Creates a confirmation dialog for deletion of an entity.
	 * 
	 * @param entity
	 * 		Entity to delete.
	 * @return
	 * 		A CustomModalWindow.
	 */
	private CustomModalWindow getDeleteModal(final EventHandler callback)
	{
		final CustomModalWindow delete = new CustomModalWindow("deleteModal");
		
		delete.setTitle(new StringResourceModel("delete", this, null));
		
		positionBeforeDeletion = -1;
		
		delete.setPageCreator(new ModalWindow.PageCreator()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page createPage()
			{
				return new DeleteEntityPage(delete, getEntityName())
				{
					private static final long serialVersionUID = 1L;

					@Override
					protected void onDelete(AjaxRequestTarget target)
					{
						if(getEntity() instanceof OrderedEntityBase)
						{
							positionBeforeDeletion = ((OrderedEntityBase) getEntity()).getPosition();
						}
						
						BaseFunctionEditEntityPanel.this.onDelete(target);
						
						UserWeaveSession.get().setHasStateToBeChanged(true);
					}
				};
			}	
		});
		
		delete.setWindowClosedCallback(new ModalWindow.WindowClosedCallback()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClose(AjaxRequestTarget target)
			{
				if(UserWeaveSession.get().isHasStateToBeChanged())
				{
					if(positionBeforeDeletion != -1)
					{
						BaseFunctionEditEntityPanel.this.onAfterDelete(target, callback, positionBeforeDeletion);
					}
					else
					{
						BaseFunctionEditEntityPanel.this.onAfterDelete(target, callback, null);
					}
				}
				
				UserWeaveSession.get().setHasStateToBeChanged(false);
			}
		});
		
		return delete;
	}
	
	/**
	 * Returns an ajax link to show the delete dialog.
	 * 
	 * @param id
	 * 		Component id.
	 * @return
	 */
	protected AuthOnlyAjaxLink getDeleteLink(String id)
	{
		StudyStateInitDependentLink link = new StudyStateInitDependentLink(id)
		{	
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				deleteModal.show(target);
			}
			
			@Override
			public boolean isEnabled()
			{
				if(super.isEnabled())
				{
					return deleteLinkIsEnabled();
				}
				
				return false;
			}
		};
		
		link.add(new Label("deleteLabel", new StringResourceModel("delete", this, null)));
		
		return link;
	}
	
	/**
	 * Creates a confirmation dialog to edit an entity.
	 * 
	 * @param entity
	 * 		Entity to edit.
	 * @return
	 * 		A CustomModalWindow.
	 */
	private CustomModalWindow getEditModal(final EventHandler callback)
	{
		final CustomModalWindow edit = new CustomModalWindow("editModal");
		
		edit.setTitle(new StringResourceModel("edit", this, null));
		
		edit.setPageCreator(new ModalWindow.PageCreator()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page createPage()
			{
				return getEditWebPage(callback, edit);
			}
		});
		
		edit.setWindowClosedCallback(getEditWindowClosedCallback(callback));
		
		return edit;
	}
	
	/**
	 * Returns an ajax link to show the edit dialog.
	 * 
	 * @param id
	 * 		Component id.
	 * @return
	 */
	protected AuthOnlyAjaxLink getEditLink(String id)
	{
		return new AuthOnlyAjaxLink(id)
		{	
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				onEditLinkActivate(target);
			}
		};
	}
	
	protected void onEditLinkActivate(AjaxRequestTarget target)
	{
		editModal.show(target);
	}
	
	/**
	 * Callback when the edit modal closes.
	 * 
	 * @return
	 */
	protected WindowClosedCallback getEditWindowClosedCallback(final EventHandler callback)
	{
		return new ModalWindow.WindowClosedCallback()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClose(AjaxRequestTarget target){}	
		};
	}
	
	/**
	 * Disable all annotated links with the given
	 * which not match the given study state.
	 */
	@Override
	protected void onBeforeRender()
	{
		// throw enable check to authorization strategy 
		visitChildren(new IVisitor<Component, Void>()
		{
			public void component(final Component component, final IVisit<Void> visit)
			{
				// calls auth. strategy
				boolean isEnabled = component.isActionAuthorized(ENABLE);
				
				component.setEnabled(isEnabled);
			}
		});
		
		visitChildren(new DisableEventLinkVisitor(getStudyStateModel()));
		
		super.onBeforeRender();
	}
	
	/**
	 * Set the enable state of the delete link. Gets evaluated
	 * only, if the delete link is by default enabled. 
	 * 
	 * @return default true.
	 */
	protected boolean deleteLinkIsEnabled()
	{
		return true;
	}
	
	protected String getEntityName()
	{
		return "Entity";
	}
	
	/**
	 * Model for the study state. Needed, if Links schould be
	 * disabled, if a study is not in a specific sate (for
	 * example, the init state).
	 * 
	 * @return
	 */
	protected abstract IModel<StudyState> getStudyStateModel();
	
	/**
	 * Get a specific WebPage for editing the entity.
	 * 
	 * @return
	 */
	protected abstract WebPage getEditWebPage(final EventHandler callback, ModalWindow window);
	
	/**
	 * Delete an entity here.
	 * 
	 * @param target
	 * 		AjaxRequestTrarget.
	 */
	protected abstract void onDelete(AjaxRequestTarget target);
	
	/**
	 * Called after onDelete has been processed.
	 * 
	 * @param target
	 * 		AjaxRequestTarget
	 */
	protected abstract void onAfterDelete(AjaxRequestTarget target, EventHandler callback, Integer lastPosition);
}
