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
package com.userweave.pages.components.slidableajaxtabpanel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * An ajax tabbed panel which is slidable. This means
 * it displays only a specific number of tabs, not all.
 * Tabs, that are not visible are accessed by sliding
 * through the tab list.
 * 
 * This class based on and extends the TabbedPanel.
 * 
 * @author opr
 */
@Deprecated
public class SlidableAjaxTabbedPanel extends Panel
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * id used for child panels.
	 */
	public static final String TAB_PANEL_ID = "panel";

	/**
	 * The list of tabs, which backs this component
	 */
	private final List<ITab> tabs;
	
	/**
	 * List of tabs to display. Will be generated form
	 * the tabs list and has size MAX_DISPLAY_TABS or 
	 * displayTabs from the constructor.
	 */
	private final List<ITab> displayTabList;
	
	/**
	 * The number of tabs to be displayed.
	 */
	protected final int displaySize;
	
	/**
	 * Get the current displaySize of the display tab list.
	 * 
	 * @return
	 */
	public int getDisplaySize()
	{
		return displaySize;
	}
	
	/**
	 * Beginning index of display list.
	 */
	protected int leftIndex;
	
	/**
	 * Display only this amount of tabs if not otherwise
	 * specified.
	 */
	protected static final int MAX_DISPLAY_TABS = 5;
	
	/**
	 * The minimum number of tabs to display.
	 */
	protected static final int MIN_DISPLAY_TABS = 2;
	
	/**
	 * Slide in left direction.
	 */
	protected static final int DIR_LEFT = 0;
	
	/**
	 * Slide in right direction
	 */
	protected static final int DIR_RIGHT = 1;
	
	/**
	 * Default constructor to display displayTabs
	 * tabs.
	 * 
	 * @param id
	 * 		Component id.
	 * @param tabs
	 * 		List of tabs to display.
	 * @param displayTabs
	 * 		Number of tabs to display.
	 */
	public SlidableAjaxTabbedPanel(String id, List<ITab> tabs, int displayTabs)
	{
		super(id, new Model(new Integer(-1)));
		
		leftIndex = 0;
		
		if (tabs == null)
		{
			throw new IllegalArgumentException("argument [tabs] cannot be null");
		}
		
		this.tabs = tabs;
		
		displaySize = (displayTabs <= MIN_DISPLAY_TABS) ? MIN_DISPLAY_TABS : displayTabs;
		
		displayTabList = new ArrayList<ITab>(displaySize);
	
		init();
	}
	
	/**
	 * Constructor to display a tabbed panel with
	 * MAX_DISPLAY_TABS tabs.
	 * 
	 * @param id
	 * 		Component id
	 * @param tabs
	 * 		List of tabs to display.
	 */
	public SlidableAjaxTabbedPanel(String id, List<ITab> tabs)
	{
		super(id, new Model(new Integer(-1)));
		
		leftIndex = 0;
		
		if (tabs == null)
		{
			throw new IllegalArgumentException("argument [tabs] cannot be null");
		}
		
		this.tabs = tabs;
		
		displaySize = MAX_DISPLAY_TABS;
		
		displayTabList = new ArrayList<ITab>(displaySize);
		
		init();
	}
	
	/**
	 * Initializes this panel with the tabs.
	 */
	private void init()
	{	
		int sizeOfTabList = this.tabs.size();
		
		if(sizeOfTabList == 0)
		{
			updateDisplayTabList(tabs, DIR_LEFT);
		}
		else if(sizeOfTabList < displaySize)
		{
			updateDisplayTabList(tabs.subList(leftIndex, sizeOfTabList), DIR_LEFT);	
		}
		else
		{
			updateDisplayTabList(tabs.subList(leftIndex, displaySize), DIR_LEFT);
		}
		
		setOutputMarkupId(true);

		setVersioned(false);
		
		createAndAddSliders();
		
		/*
		 * Container class for the tabs in the markup.
		 */
		WebMarkupContainer tabsContainer = new WebMarkupContainer("tabs-container")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onComponentTag(ComponentTag tag)
			{
				super.onComponentTag(tag);
				tag.put("class", getTabContainerCssClass());
			}
		};
		
		tabsContainer.setOutputMarkupId(true);
		
		add(tabsContainer);
		
		/*
		 * Model for the Loop class which populates this panel.
		 */
		final IModel tabCount = new AbstractReadOnlyModel()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Object getObject()
			{
				return new Integer(SlidableAjaxTabbedPanel.this.displayTabList.size());
			}
		};
		
		/*
		 * Populate this panel with tabs.
		 */
//		tabsContainer.add(getLoop("tabs", tabCount));
	}
	
//	protected Loop getLoop(String title, IModel tabCount)
//	{
//		return new Loop(title, tabCount)
//		{
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			protected void populateItem(LoopItem item)
//			{
//				final int index = item.getIteration();
//				final ITab tab = (SlidableAjaxTabbedPanel.this.displayTabList.get(index));
//
//				final WebMarkupContainer titleLink = newLink("link", index);				
//				
//				titleLink.add(newTitle("title", tab.getTitle(), index));
//
//				item.add(titleLink);
//			}
//
//			@Override
//			protected LoopItem newItem(int iteration)
//			{
//				return newTabContainer(iteration);
//			}
//
//		};
//	}
	
	/**
	 * Add the slidable functionality.
	 */
	private void createAndAddSliders()
	{
		AjaxLink left = new AjaxLink("slide_left")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				shiftLeft(null);
				
				target.addComponent(SlidableAjaxTabbedPanel.this);
			}

			@Override
			public boolean isVisible()
			{
				return leftIndex > 0;
			}
			
		};
		
		AjaxLink right = new AjaxLink("slide_right")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				shiftRight(null);
				
				target.addComponent(SlidableAjaxTabbedPanel.this);
				
			}
			
			@Override
			public boolean isVisible()
			{
				return leftIndex + displaySize < tabs.size();
			}
		};
		
		add(left);
		add(right);
	}
	
	public void shiftLeft(Integer steps)
	{
		if(steps == null)
		{
			leftIndex--;
		}
		else
		{
			leftIndex -= steps;
		}
		
		updateDisplayTabList(tabs.subList(leftIndex, leftIndex + displaySize), DIR_LEFT);
	}
	
	public void shiftRight(Integer steps)
	{
		if(steps == null)
		{
			leftIndex++;
		}
		else
		{
			leftIndex += steps;
		}
		
		updateDisplayTabList(tabs.subList(leftIndex, leftIndex + displaySize), DIR_RIGHT);
	}
	
	/**
	 * Build a new tab list to display, based on the given
	 * list, which is a view of the tab list, which backs
	 * this panel.
	 * 
	 * @param subList
	 * 		The sub list to generate the new display tabs from.
	 */
	protected void updateDisplayTabList(List<ITab> subList, int direction)
	{
		Iterator<ITab> i = subList.iterator();
		
//		int selectedTabIndex = -1;
//		
//		if(getSelectedTab() != -1)
//		{
//			// we are not in the init phase of this panel
//			// so check new setup
//			
//			// get selected Tab of current list
//			// to check, if the new list contains
//			// it. If not, get the index of the tab
//			// for later retrieving.
//			ITab selectedTab = displayTabList.get(getSelectedTab());
//			
//			if(!subList.contains(selectedTab))
//			{
//				// new list does not contain the selected tab
//				// so we have to find it in the tab list
//				selectedTabIndex = tabs.indexOf(selectedTab);
//			}
//		}
		
		// reset display list and fill with 
		// new elements
		displayTabList.clear();
		
		// populate display list
		while(i.hasNext())
		{
			displayTabList.add(i.next());
		}
		
		// add selected tab to first position if 
		// it is not in the new list and if the 
		// shift direction is right.
//		if(selectedTabIndex != -1 && direction == DIR_RIGHT)
//		{
//			displayTabList.set(0, tabs.get(selectedTabIndex));
//		}
//		
//		// add selected tab to last position if 
//		// it is not in the new list and if the 
//		// shift direction is left.
//		if(selectedTabIndex != -1 && direction == DIR_LEFT)
//		{
//			// don't append, replace last element
//			displayTabList.set(subList.size() -1, tabs.get(selectedTabIndex));
//		}
	}
	
//	/**
//	 * Updates the tab list, which backs this panel.
//	 * 
//	 * @param newTabList
//	 * 		New tab list to display.
//	 */
//	public void replaceTabList(List<ITab> newTabList, AjaxRequestTarget target)
//	{
//		tabs = newTabList;
//			
//		if(tabs.size() < leftIndex + displaySize)
//		{
//			updateDisplayTabList(tabs.subList(leftIndex, tabs.size()), DIR_LEFT);
//		}
//		else
//		{
//			updateDisplayTabList(tabs.subList(leftIndex, leftIndex + displaySize), DIR_LEFT);
//		}
//	}
	
	/**
	 * Generates a loop item used to represent a specific tab's <code>li</code> element.
	 * 
	 * @param tabIndex
	 * @return new loop item
	 */
//	protected LoopItem newTabContainer(int tabIndex)
//	{
//		return new LoopItem(tabIndex)
//		{
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			protected void onComponentTag(ComponentTag tag)
//			{
//				super.onComponentTag(tag);
//				String cssClass = (String)tag.getString("class");
//				if (cssClass == null)
//				{
//					cssClass = " ";
//				}
//				cssClass += " tab" + getIteration();
//
//				if (getIteration() == getSelectedTab())
//				{
//					cssClass += " selected";
//				}
//				if (getIteration() == getDisplayTabs().size() - 1)
//				{
//					cssClass += " last";
//				}
//				tag.put("class", cssClass.trim());
//			}
//
//		};
//	}


	// @see org.apache.wicket.Component#onAttach()
	@Override
	protected void onBeforeRender()
	{
		super.onBeforeRender();
		if (!hasBeenRendered() && getSelectedTab() == -1)
		{
			// select the first tab by default
			setSelectedTab(0);
		}
	}

	/**
	 * @return the value of css class attribute that will be added to a div containing the tabs. The
	 *         default value is <code>tab-row</code>
	 */
	protected String getTabContainerCssClass()
	{
		return "tab-row";
	}

	/**
	 * @return list of tabs that can be used by the user to add/remove/reorder tabs in the panel
	 */
	public final List<ITab> getTabs()
	{
		return tabs;
	}

	public final List<ITab> getDisplayTabs()
	{
		return displayTabList;
	}
	
	/**
	 * Factory method for tab titles. Returned component can be anything that can attach to span
	 * tags such as a fragment, panel, or a label
	 * 
	 * @param titleId
	 *            id of title component
	 * @param titleModel
	 *            model containing tab title
	 * @param index
	 *            index of tab
	 * @return title component
	 */
	protected Component newTitle(String titleId, IModel titleModel, int index)
	{
		return new Label(titleId, titleModel);
	}
	
	/**
	 * Factory method for links used to switch between tabs.
	 * 
	 * The created component is attached to the following markup. Label component with id: title
	 * will be added for you by the tabbed panel.
	 * 
	 * <pre>
	 *            &lt;a href=&quot;#&quot; wicket:id=&quot;link&quot;&gt;&lt;span wicket:id=&quot;title&quot;&gt;[[tab title]]&lt;/span&gt;&lt;/a&gt;
	 * </pre>
	 * 
	 * Example implementation:
	 * 
	 * <pre>
	 * protected WebMarkupContainer newLink(String linkId, final int index)
	 * {
	 * 	return new Link(linkId)
	 * 	{
	 * 		private static final long serialVersionUID = 1L;
	 * 
	 * 		public void onClick()
	 * 		{
	 * 			setSelectedTab(index);
	 * 		}
	 * 	};
	 * }
	 * </pre>
	 * 
	 * @param linkId
	 *            component id with which the link should be created
	 * @param index
	 *            index of the tab that should be activated when this link is clicked. See
	 *            {@link #setSelectedTab(int)}.
	 * @return created link component
	 */
	protected WebMarkupContainer newLink(String linkId, final int index)
	{
		return new AjaxFallbackLink(linkId)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				setSelectedTab(index);
				if (target != null)
				{
					target.addComponent(SlidableAjaxTabbedPanel.this);
				}
				onAjaxUpdate(target);
			}
		};
	}
	
	/**
	 * sets the selected tab
	 * 
	 * @param index
	 *            index of the tab to select
	 * 
	 */
	public void setSelectedTab(int index)
	{
		if(displayTabList.size() == 0)
		{
			add(new WebMarkupContainer(TAB_PANEL_ID));
			return;
		}
		if (index < 0 || index >= displayTabList.size())
		{
			throw new IndexOutOfBoundsException();
		}

		setDefaultModelObject(new Integer(index));

		ITab tab = displayTabList.get(index);

//		Panel panel = tab.getPanel(TAB_PANEL_ID);
		Panel panel = null;
		if (panel == null)
		{
			throw new WicketRuntimeException("ITab.getPanel() returned null. TabbedPanel [" +
				getPath() + "] ITab index [" + index + "]");

		}

		if (!panel.getId().equals(TAB_PANEL_ID))
		{
			throw new WicketRuntimeException(
				"ITab.getPanel() returned a panel with invalid id [" +
					panel.getId() +
					"]. You must always return a panel with id equal to the provided panelId parameter. TabbedPanel [" +
					getPath() + "] ITab index [" + index + "]");
		}


		if (get(TAB_PANEL_ID) == null)
		{
			add(panel);
		}
		else
		{
			replace(panel);
		}
	}

	/**
	 * @return index of the selected tab
	 */
	public final int getSelectedTab()
	{
		return ((Integer)getDefaultModelObject()).intValue();
	}
	
	/**
	 * A template method that lets users add additional behavior when ajax update occurs. This
	 * method is called after the current tab has been set so access to it can be obtained via
	 * {@link #getSelectedTab()}.
	 * <p>
	 * <strong>Note</strong> Since an {@link AjaxFallbackLink} is used to back the ajax update the
	 * <code>target</code> argument can be null when the client browser does not support ajax and
	 * the fallback mode is used. See {@link AjaxFallbackLink} for details.
	 * 
	 * @param target
	 *            ajax target used to update this component
	 */
	protected void onAjaxUpdate(AjaxRequestTarget target){}
}
