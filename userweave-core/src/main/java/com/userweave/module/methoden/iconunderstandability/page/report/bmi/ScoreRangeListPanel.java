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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.Loop;
import org.apache.wicket.markup.html.list.LoopItem;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.module.methoden.iconunderstandability.dao.ScoreRangeListDao;
import com.userweave.module.methoden.iconunderstandability.domain.Interval;
import com.userweave.module.methoden.iconunderstandability.domain.ScoreRangeList;
import com.userweave.module.methoden.iconunderstandability.domain.ScoreRangeList.Alignment;
import com.userweave.module.methoden.iconunderstandability.domain.ScoreRangeList.ValueType;

/**
 * @author oma
 */
public class ScoreRangeListPanel extends Panel 
{
	private static final long serialVersionUID = 1L;

	@SpringBean
	private ScoreRangeListDao scoreRangeListDao;
	
	private final List<Label> intervalLabels = new ArrayList<Label>();
	
	public ScoreRangeListPanel(String id, ValueType type) {
		super(id);
		final ScoreRangeList scoreRangeList = scoreRangeListDao.findByType(type);
		
		setOutputMarkupId(true);
		
		add(new Form("form")
		{
			{
				List<Alignment> ALIGNMENTS = Arrays.asList(new Alignment[] {
						Alignment.NOT_SYMMETRIC, Alignment.SYMMETRIC });

				DropDownChoice alignmentDropDownChoice = new DropDownChoice(
						"alignments", new PropertyModel(scoreRangeList,
								"alignment"), ALIGNMENTS, new ChoiceRenderer()
						{
							@Override
							public Object getDisplayValue(Object object)
							{
								return ((Alignment) object).toString();
							}
						});

				alignmentDropDownChoice
						.add(new AjaxFormComponentUpdatingBehavior("onchange")
						{
							@Override
							protected void onUpdate(AjaxRequestTarget target)
							{
								target.addComponent(ScoreRangeListPanel.this);
							}
						});

				add(alignmentDropDownChoice);

				add(new Loop("scoreRangeList", 11)
				{
					@Override
					protected void populateItem(LoopItem item)
					{

						final int index = item.getIndex();

						addTextField(item, "score", index);
						addTextField(item, "range", index);

						addLabel(item, "fromString",
								scoreRangeList.getScoreInterval(index));
						addLabel(item, "toString",
								scoreRangeList.getScoreInterval(index));
					}

					private void addLabel(LoopItem item, final String id,
							Interval intervalModel)
					{
						Label label = new Label(id, new PropertyModel(
								intervalModel, id));
						label.setOutputMarkupId(true);
						intervalLabels.add(label);
						item.add(label);
					}
				});

				addTextField(this, "range", 11);
			}
	
	
				private void addTextField(WebMarkupContainer container, String id, final int index) {
					TextField textfield = new TextField(id, new PropertyModel(scoreRangeList, id + Integer.toString(index)));
					textfield.add(
						new AjaxFormComponentUpdatingBehavior("onchange") {
	
							@Override
							protected void onUpdate(AjaxRequestTarget target) {
								addLabels(target);
							}
						}
					);
					container.add(textfield);													
				}							
				
				@Override
				protected void onSubmit() {
					scoreRangeListDao.save(scoreRangeList);
				}
			}
		);
	}

	private void addLabels(AjaxRequestTarget target) {
		for (Label label : intervalLabels) 
		{				            		
    		target.add(label);
		}
	}
}

