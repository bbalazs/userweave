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

import org.apache.wicket.Page;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.PageCreator;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

import com.userweave.components.bar.BarListPanel;
import com.userweave.components.customModalWindow.CustomModalWindow;
import com.userweave.module.methoden.iconunderstandability.domain.report.TermReport;

/**
 * @author oma
 */
public class IndicatorPanel extends Panel 
{
	private static final long serialVersionUID = 1L;

	private CustomModalWindow indicatorModalWindow;
	
	private CustomModalWindow barListModalWindow;
	
	private WebMarkupContainer indicatorContainer; 
	
	private WebMarkupContainer scaleContainer; 
	
	public IndicatorPanel(String id, final TermReport termReport) {
		super(id);	
		
		final double indicator = termReport.getIndicator();
		
		//add(new IndicatorLegendPanel("indicator_legend"));
		
		createAndAddIndicatorContainer();
		
		createAndAddScaleContainer();
		
		addIndicatorModalWindow(indicator);
		
		addBarListModalWindow(termReport);
		
		indicatorContainer.add(new Label("indicator", Double.toString(indicator)));
		
		List<Integer> values = Arrays.asList(
			new Integer[] {
				termReport.getMissingValueRating(),
				termReport.getDeviationFromMeanProcessingTimeRating() 
			}
		);
			
			
		List<String> cssClassNames = Arrays.asList(
			new String[] {
				"bar_indicator_first", 
				"bar_indicator_second"
			}
		);

		scaleContainer.add(new BarListPanel("barListPanel", values, 10, cssClassNames));
	}
	
	private void createAndAddScaleContainer() 
	{
		scaleContainer = new WebMarkupContainer("scale_container");
		
		scaleContainer.setOutputMarkupId(true);
		
//		scaleContainer.add(new AjaxEventBehavior("onclick") {	
//			@Override
//			protected void onEvent(AjaxRequestTarget target) 
//			{
//				barListModalWindow.show(target);
//			}
//		});

		add(scaleContainer);
		
	}

	private void createAndAddIndicatorContainer()
	{
		indicatorContainer = new WebMarkupContainer("indicator_container");
				
		indicatorContainer.setOutputMarkupId(true);
		
//		indicatorContainer.add(new AjaxEventBehavior("onclick") {	
//			@Override
//			protected void onEvent(AjaxRequestTarget target) 
//			{
//				indicatorModalWindow.show(target);
//			}
//		});

		add(indicatorContainer);
	}
	
	private void addIndicatorModalWindow(final double indicator)
	{
		indicatorModalWindow = new CustomModalWindow("indicatorExplanationPopup");
		
		indicatorModalWindow.setPageCreator(new PageCreator() {

			private static final long serialVersionUID = 1L;

			@Override
			public Page createPage() {
				return new NoteExplanationPage() {
					
					@Override
					protected IModel getNoteExplanation() {
						if (indicator < 4) {
							return new StringResourceModel(NoteExplanationPage.RATING1, this, null);
						} else if (indicator < 7) {
							return new StringResourceModel(NoteExplanationPage.RATING2, this, null);
						} else {
							return new StringResourceModel(NoteExplanationPage.RATING3, this, null);
						}
					}
				};
			};
		});
		
		indicatorModalWindow.setInitialWidth(316);
		
		indicatorModalWindow.setInitialHeight(355);
	
		add(indicatorModalWindow);
	}
	
	private void addBarListModalWindow(final TermReport termReport)
	{
		barListModalWindow = new CustomModalWindow("barListPopup");
		
		barListModalWindow.setPageCreator(new PageCreator() 
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page createPage() {
				return new IndicatorBarListExplanationPage(termReport);
			};
		});
		
		barListModalWindow.setInitialWidth(342);

		barListModalWindow.setInitialHeight(424);
		
		scaleContainer.add(barListModalWindow);
	}
}

