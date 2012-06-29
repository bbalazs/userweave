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
package com.userweave.pages.components.reorderable;

import java.util.Collections;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.StringResourceModel;

import com.userweave.components.authorization.AuthOnlyAjaxLink;
import com.userweave.components.customModalWindow.CustomModalWindow;
import com.userweave.domain.util.Ordered;
import com.userweave.domain.util.OrderedComparator;
import com.userweave.pages.components.button.AddLink;
import com.userweave.pages.components.button.AuthOnlyAddLink;
import com.userweave.pages.configuration.study.details.AssurancePage;

/**
 * A list which is reorderable.
 * 
 * @author oma
 * @author opr
 */
public abstract class ReorderableListPanel<T extends Ordered<?>> extends Panel 
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * The list view for the reorderable items.
	 */
	private PropertyListView<T> configurationsListView;

	/**
	 * Dialog, that pops up, when an item should be removed.
	 */
	private CustomModalWindow deleteModalWindow;

	/**
	 * Declare this component as editable or not.
	 */
	private final boolean editable;
	
	protected boolean isEditable() 
	{
		return editable;
	}
	
	/**
	 * Default constructor.
	 * 
	 * @param id
	 * 		Component markup id.
	 * @param editable
	 * 		Flag to declare this component as editable or not.
	 * @param addButtonRowResourceString
	 * 		String to display on the add button.
	 */
	public ReorderableListPanel(String id, boolean editable, String addButtonRowResourceString) 
	{
		super(id);

		this.editable = editable;

		setOutputMarkupId(true);

		addDeleteItemModalWindow();
		
		add(configurationsListView = 
			new PropertyListView<T>("configurations", getListModel()) 
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<T> item) 
			{
				final T orderedObject = item.getModel().getObject();

				// get position of current item
				final int position = item.getIndex();
				
				ReorderableListPanel.this.populateItem(item);

				AuthOnlyAjaxLink moveUp = createMovableLink(
					"moveup", moveUpIsVisible(getIndex(orderedObject)), true, position);

				AuthOnlyAjaxLink moveDown = createMovableLink(
					"movedown", moveDownIsVisible(getIndex(orderedObject)), false, position);
				
				item.add(moveUp);
				item.add(moveDown);
				
				
				item.add(new AuthOnlyAjaxLink("delete") 
				{
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) 
					{
						deleteItemPosition = position;
						
						if(askBeforeDelete()) 
						{
							deleteModalWindow.show(target);
						} 
						else 
						{
							reallyDeleteItem = true;
							deleteItem(target);
						}
					}

					@Override
					public boolean isEnabled() 
					{
						return deleteIsEnabled(getIndex(orderedObject));
					}
				});
			}
		});
		
		configurationsListView.setOutputMarkupId(true);
		
		String addButtonRowId = "addButtonRow";
		Component addButtonRow = null;
		if(addIsVisible()) {
			addButtonRow = getAddButtonRow(addButtonRowId, addButtonRowResourceString);
		}
		if (addButtonRow == null) {
			addButtonRow = new WebMarkupContainer(addButtonRowId);
			addButtonRow.setEnabled(false);
		}
		add(addButtonRow);
				

		// create no item available label with empty list message as default
		add(new Label("emptyListMessage", new StringResourceModel("empty_list_message", this, null)) 
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible() {
				return getModelDisplayObjects()!= null && 
					   getModelDisplayObjects().isEmpty();
			}
		});
	}

	/**
	 * Creates a model for the list view.
	 * 
	 * @return
	 */
	private LoadableDetachableModel<List<T>> getListModel()
	{
		return new LoadableDetachableModel<List<T>>() 
{
		private static final long serialVersionUID = 1L;
		
		@Override
		protected List<T> load() {
			return sortDisplayObjects(getDisplayObjects());
		}
};
	}

	/**
	 * Factory method to create a link, that moves an item
	 * up or down.
	 * 
	 * @param componentId
	 * 		Component markup id.
	 * @param isEnabled
	 * 		Declare this link as enabled or not.
	 * @param moveUp
	 * 		Move item up in list or down.
	 * @param position
	 * 		Position of item in list.
	 * @return
	 * 		An auth only ajax link.
	 */
	private AuthOnlyAjaxLink createMovableLink(
		String componentId, final boolean isEnabled, 
		final boolean moveUp, final int position)
	{
		AuthOnlyAjaxLink link = new AuthOnlyAjaxLink(componentId)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled() {
				return super.isEnabled() && isEnabled;
			}

			@Override
			public void onClick(AjaxRequestTarget target) 
			{
				T object = getObjectAtPosition(position);
				
				if(moveUp)
				{
					moveUp(object, getModelDisplayObjects());
				}
				else
				{
					moveDown(object, getModelDisplayObjects());
				}
				
				sortDisplayObjects(getModelDisplayObjects());
				onOrderChanged(object, target);
				target.add(ReorderableListPanel.this);
			}
		};
		
		return link;
	}
	
	private T getObjectAtPosition(int index) 
	{
		return getModelDisplayObjects().get(index);
	}

	private boolean isFirst(int index) {
		return (index == 0);
	}

	private boolean isLast(int index) {
		return (index == getModelDisplayObjects().size() - 1);
	}

	private int getIndex(final T orderedObject) {
		return getModelDisplayObjects().indexOf(orderedObject);
	}

	private List<T> sortDisplayObjects(List<T> objects) {
		if (objects != null) {
			Collections.sort(objects, OrderedComparator.INSTANCE);
		}
		return objects;
	}

	protected abstract void moveUp(final T orderedObject, List<T> objects);

	protected abstract void moveDown(final T orderedObject, List<T> objects);

	protected abstract void delete(final T objectToDelete, List<T> objects);

	private boolean moveUpIsVisible(int index) {
		return editable && !isFirst(index);
	}

	private boolean moveDownIsVisible(int index) {
		return editable && !isLast(index);
	}

	/**
	 * Check if delete is enabled, here return always true
	 * 
	 * @return
	 */
	private boolean deleteIsEnabled(final int index) {
		return editable;
	}
	
	protected boolean askBeforeDelete() {
		return true;
	}

	public WebMarkupContainer wrapper;

	private List<T> getModelDisplayObjects() {
		return configurationsListView.getModelObject();
	}

	protected void clearModelDisplayObjects() {
		configurationsListView.detachModels();
	}

	protected abstract List<T> getDisplayObjects();

	protected abstract void populateItem(ListItem<T> item);

	protected void onOrderChanged(T object, AjaxRequestTarget target) { }
	
	protected void onDelete(T object, AjaxRequestTarget target) { }

	private Integer deleteItemPosition = null;
	
	private boolean reallyDeleteItem = false;

	/**
	 * Creates and adds a DeleteItemModalWindow (Deletion 
	 * reconfirmation Modal Dialog). If the user confirms 
	 * the deletion of an item the item will be deleted 
	 * with the abstract delete() method.
	 */
	private void addDeleteItemModalWindow() {

		deleteModalWindow = new CustomModalWindow("deleteItemModalWindow");
		add(deleteModalWindow);

		deleteModalWindow.setInitialHeight(150);
		deleteModalWindow.setInitialWidth(350);

		// use AssurancePage for delete reconfirmation dialog
		deleteModalWindow.setPageCreator(new ModalWindow.PageCreator() 
		{
			private static final long serialVersionUID = 1L;

			public Page createPage() {
				return new AssurancePage(deleteModalWindow) 
				{
					private static final long serialVersionUID = 1L;

					@Override
					protected void onOk(AjaxRequestTarget target) {
						reallyDeleteItem = true;
					}
				};
			}
		});

		// reset delete item on closing
		deleteModalWindow
				.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() 
				{
					private static final long serialVersionUID = 1L;

					public void onClose(AjaxRequestTarget target) 
					{
						deleteItem(target);
					}
				});

		//
		deleteModalWindow
				.setCloseButtonCallback(new ModalWindow.CloseButtonCallback() 
				{
					private static final long serialVersionUID = 1L;

					public boolean onCloseButtonClicked(AjaxRequestTarget target) {
						return true;
					}
				});
	}
		
	protected void deleteItem(AjaxRequestTarget target) {
		if(reallyDeleteItem && deleteItemPosition != null) {
			List<T> objects = getModelDisplayObjects();
			T deleteObject = objects.get(deleteItemPosition);
			objects.remove(deleteItemPosition);
			clearModelDisplayObjects();
			
			delete(deleteObject, objects);
			onDelete(deleteObject, target);
		}

		target.add(ReorderableListPanel.this);
		reallyDeleteItem = false;
		deleteItemPosition = null;		
	}

	protected Component getAddButtonRow(String markupId, String addButtonRowResourceString) {
		
		if(addButtonRowResourceString != null)
		{
			return new AuthOnlyAddLink(markupId, addButtonRowResourceString) 
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onClick(AjaxRequestTarget target) {
					target.add(ReorderableListPanel.this);
					append();
					ReorderableListPanel.this.onAdd(target);
					
				}
			};
		}
		else
		{
			return new AuthOnlyAddLink(markupId, AddLink.ADD_METHOD) 
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onClick(AjaxRequestTarget target) {
					target.add(ReorderableListPanel.this);
					append();
					ReorderableListPanel.this.onAdd(target);
					
				}
			};
		}
	}

	protected boolean addIsVisible() {
		return isEditable();
	}

	protected void onAdd(AjaxRequestTarget target) { }

	protected T append() { return null; }

}
