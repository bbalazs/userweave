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
package com.userweave.pages.configuration.study.localization;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.application.UserWeaveSession;
import com.userweave.components.model.LocaleModel;
import com.userweave.dao.StudyDao;
import com.userweave.domain.Role;
import com.userweave.domain.Study;
import com.userweave.domain.User;
import com.userweave.utils.LocalizationUtils;

/**
 * @author oma
 */
@Deprecated
public abstract class LanguageActivationPanel extends Panel {
	
	@SpringBean
	private StudyDao studyDao;
		
	public LanguageActivationPanel(String id, IModel model) {
		super(id, model);
		init();
	}
	
	protected void init() {
		
		User user = UserWeaveSession.get().getUser();
		
		final boolean isAuthorized = 
			user.getCurrentProjectRoles().hasRole(Role.PROJECT_ADMIN) || 
			user.getCurrentProjectRoles().hasRole(Role.PROJECT_PARTICIPANT);
		
		Form form = new Form("form");
		
		form.setVisible(isAuthorized);
		
		CheckGroup group = new CheckGroup("group", new PropertyModel(getDefaultModel(), "supportedLocales"));
	    
		group.add(new ListView("checkboxes", getAdditionalLocales()) 
		{
			@Override
			protected void populateItem(ListItem item) 
			{
				item.add(new Check("check", item.getModel()));
				item.add(new Label("locale", new LocaleModel(item.getModel(), getLocale())));
			}

		});
		
		group.add(new AjaxFormChoiceComponentUpdatingBehavior()
        {        
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
            	Study study = getStudy();
				study.getSupportedLocales().add(study.getLocale());
				studyDao.save(study);
				onChange(target);
            }
        });
		
		form.add(group);
		
		// dispay a list of available languages if user is visitor
		ListView langList = new ListView("langList", new PropertyModel(getDefaultModel(), "supportedLocales")) 
		{	
			@Override
			protected void populateItem(ListItem item) 
			{
				Locale locale = (Locale)item.getModelObject();
				
				item.add(new Label("locale", locale.getDisplayLanguage(getLocale())));
			}
			
			@Override
			public boolean isVisible()
			{
				return !isAuthorized;
			}
		};
		
		add(langList);
		add(form);
	}

	private List<Locale> getAdditionalLocales() {
		Locale mainLocale = getStudy().getLocale();	    
		List<Locale> additionalLocales = new ArrayList<Locale>();
		additionalLocales.addAll(LocalizationUtils.getSupportedStudyLocales());
		additionalLocales.remove(mainLocale);
		return additionalLocales;
	}

	protected void onChange(AjaxRequestTarget target) {}

	private Study getStudy() {
		return (Study) getDefaultModelObject();
	};
}

