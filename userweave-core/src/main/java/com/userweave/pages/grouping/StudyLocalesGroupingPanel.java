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
package com.userweave.pages.grouping;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.dao.StudyDao;
import com.userweave.dao.StudyLocalesGroupDao;
import com.userweave.domain.Study;
import com.userweave.domain.StudyLocalesGroup;
import com.userweave.module.methoden.questionnaire.page.grouping.GroupAddedCallback;

@SuppressWarnings("serial")
public class StudyLocalesGroupingPanel extends GroupingPanelWithName<StudyLocalesGroup>{

	@SpringBean
	private StudyLocalesGroupDao studyLocalesGroupDao;

	@SpringBean
	private StudyDao studyDao;

	private final Integer studyId;
	
	private final List<Locale> locales = new ArrayList<Locale>();

	public StudyLocalesGroupingPanel(
		String id, Study study, Locale locale, 
		GroupAddedCallback groupAddedCallback) 
	{
		this(id, study, new StudyLocalesGroup(), locale, groupAddedCallback);
	}

	public StudyLocalesGroupingPanel(
		String id, Study study, StudyLocalesGroup group, Locale locale, 
		GroupAddedCallback groupAddedCallback) 
	{
		super(id, group, locale, groupAddedCallback);
		
		studyId = study.getId();

		CheckGroup locales = 
			new CheckGroup("locales", new PropertyModel(this, "locales"));
	    
		add(locales);

		IModel possibleLocalesModel = new LoadableDetachableModel()
		{
			@Override
			protected Object load() 
			{
				Study study = studyDao.findById(studyId);
				return study.getSupportedLocales();
			}
		};

		locales.add(
				new ListView("values", possibleLocalesModel) {
				    @Override
					protected void populateItem(ListItem item) {
				      item.add(new Check("check", item.getModel()));
				      item.add(new Label("content", item.getModelObject().toString()));
				    };
			    }
			);
	}

	@Override
	protected IModel getTitle()
	{
		return new StringResourceModel("mult_pos_locales", this, null);
	}
	
	@Override
	public void submit() 
	{
		StudyLocalesGroup group = getGroup();		
		group.setLocales(locales);
		group.setStudy(studyDao.findById(studyId));

		studyLocalesGroupDao.save(group);
	}

	@Override
	protected boolean isStimulusVisible()
	{
		return false;
	}
}
