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
package com.userweave.module.methoden.iconunderstandability.page.report.bmi;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.PageCreator;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;

import com.userweave.application.images.ImageResource;
import com.userweave.components.bar.BarListPanel;
import com.userweave.components.bar.BarPanelWithMessage;
import com.userweave.components.customModalWindow.CustomModalWindow;
import com.userweave.domain.ImageBase;
import com.userweave.module.methoden.iconunderstandability.domain.report.IconReport;
import com.userweave.module.methoden.iconunderstandability.domain.report.TermAssignment;

/**
 * @author oma
 */
public abstract class IconReportPanel extends Panel 
{
	private static final long serialVersionUID = 1L;

	private class ExplanationFragment extends Fragment
	{
		private static final long serialVersionUID = 1L;

		public ExplanationFragment(String id)
		{
			super(id, "explanationFragment", IconReportPanel.this);
		}
	}
	
	private final Locale studyLocale;
	
	private CustomModalWindow noteExplanationModalWindow;
	
	private CustomModalWindow barListModalWindow;
	
	public IconReportPanel(
			String id, IconReport iconReport, Locale studyLocale, int index) 
	{
		super(id);
		
		this.studyLocale = studyLocale;
	
		final ImageBase image = iconReport.getImage();
		
		final CustomModalWindow iconOrderModalWindow = createAddModalWindow(iconReport);

		add(iconOrderModalWindow);
		
		add(new AjaxLink("openIconOrderLink") 
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) 
			{
				iconOrderModalWindow.show(target);					
			}
		});
		
		add(ImageResource.createImage("image", image));
		
		addNoteModalWindow(iconReport);
		
		addBarListPopupAndPanel(iconReport);
	}

	private void addNoteModalWindow(IconReport iconReport) 
	{	
		final double rating = iconReport.getTotalRating();
		
		WebMarkupContainer ratingContainer = new WebMarkupContainer("rating");
		
		ratingContainer.setOutputMarkupId(true);
		
		add(ratingContainer);
		
//		ratingContainer.add(new AjaxEventBehavior("onclick") 
//		{		
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			protected void onEvent(AjaxRequestTarget target) 
//			{
//				noteExplanationModalWindow.show(target);
//			}
//		});
		
		ratingContainer.add(new Label("totalRating", Double.toString(rating)));
		
		noteExplanationModalWindow = new CustomModalWindow("totalRatingExplanationPopup");
		
		noteExplanationModalWindow.setPageCreator(new PageCreator() 
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page createPage() 
			{
				return new NoteExplanationPage() 
				{
					@Override
					protected IModel getNoteExplanation()
					{
						if (rating < 4) {
							return new StringResourceModel(NoteExplanationPage.RATING1, this, null);
						} else if (rating < 7) {
							return new StringResourceModel(NoteExplanationPage.RATING2, this, null);
						} else {
							return new StringResourceModel(NoteExplanationPage.RATING3, this, null);
						}
					}
				};
			}	
		});
		
		noteExplanationModalWindow.setInitialWidth(316);
		
		noteExplanationModalWindow.setInitialHeight(323);
		
		add(noteExplanationModalWindow);
	}

	private void addBarListPopupAndPanel(final IconReport iconReport) 
	{
		WebMarkupContainer scaleContainer = new WebMarkupContainer("scale_container");
		
		scaleContainer.setOutputMarkupId(true);
		
//		scaleContainer.add(new AjaxEventBehavior("onclick") 
//		{	
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			protected void onEvent(AjaxRequestTarget target) 
//			{
//				barListModalWindow.show(target);
//			}
//		});
		
		scaleContainer.add(barListModalWindow = new CustomModalWindow("barListPopup"));
		
		barListModalWindow.setPageCreator(new PageCreator() 
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page createPage() {
				return new IconBarListExplanationPage(iconReport);
			}
			
		});
		
		barListModalWindow.setInitialWidth(342);
		
		barListModalWindow.setInitialHeight(605);
		
		int assignmentRating = iconReport.getAssignmentRating();
		if (assignmentRating > 0) {
			List<Integer> barValues = Arrays.asList(new Integer[] {
					assignmentRating, 
					iconReport.getHighestAssignmentToOtherTermRating(), 
					iconReport.getReactionTimeRating()});
			
			List<String> cssClassNames = Arrays.asList(new String[] {
					"bar_first", 
					"bar_second", 
					"bar_third"});
			
			scaleContainer.add(new BarListPanel("barListPanel", barValues, 10, cssClassNames));
		} else {
			scaleContainer.add(new BarPanelWithMessage("barListPanel", 0, 10, new ResourceModel("icon_not_assigned")));
		}
		
		add(scaleContainer);
	}
	
	private CustomModalWindow createAddModalWindow(IconReport iconReport) 
	{
		final CustomModalWindow modalWindow = new CustomModalWindow("iconOrderModalWindow");

		final List<TermAssignment> termAssignments = iconReport.getTermAssignments();
		
        modalWindow.setPageCreator(new ModalWindow.PageCreator()
        {
        	private static final long serialVersionUID = 1L;

			public Page createPage() 
			{
				return new IconOrderPage(modalWindow, termAssignments, studyLocale);
            }
        });
        
        modalWindow.setWindowClosedCallback(new ModalWindow.WindowClosedCallback()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClose(AjaxRequestTarget target)
			{
				onModalWindowClose(target);
			}
		});

      return modalWindow;
	}
	
	protected abstract void onModalWindowClose(AjaxRequestTarget target);
}

