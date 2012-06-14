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
package com.userweave.module.methoden.iconunderstandability.page.conf.terms;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.dao.StudyDependendDao;
import com.userweave.domain.LocalizedString;
import com.userweave.domain.OrderedEntityBase;
import com.userweave.domain.Role;
import com.userweave.domain.StudyState;
import com.userweave.module.methoden.iconunderstandability.dao.IconTermMatchingConfigurationDao;
import com.userweave.module.methoden.iconunderstandability.dao.ItmTermDao;
import com.userweave.module.methoden.iconunderstandability.domain.IconTermMatchingConfigurationEntity;
import com.userweave.module.methoden.iconunderstandability.domain.ItmTerm;
import com.userweave.pages.components.button.AddLink;
import com.userweave.pages.components.reorderable.LocalizedStringReorderableListPanel;
import com.userweave.pages.configuration.DisableComponentIfStudyStateNotInitVisitor;
import com.userweave.pages.configuration.module.ModuleConfigurationPanelBase;

/**
 * @author oma
 */
@SuppressWarnings("serial")
public class ITMTermsConfigurationPanel extends ModuleConfigurationPanelBase<IconTermMatchingConfigurationEntity> { 
	
	@AuthorizeAction(action = Action.RENDER, roles = Role.PROJECT_ADMIN)
	private class AuthButton extends Button
	{
		public AuthButton(String id) {
			super(id);
		}
	};
	
	@SpringBean
	private IconTermMatchingConfigurationDao dao;
	
	@SpringBean
	private ItmTermDao itmTermDao;
	
	private final Form<Void> form;
	
	public ITMTermsConfigurationPanel(String id, final Integer configurationId, Locale studyLocale) {
		super(id, configurationId, studyLocale);
		
		form = new Form<Void>("form") 
		{ 
			ITMTermsUpload itmTermspload;

			{					
				add(itmTermspload = new ITMTermsUpload("upload"));		
				add(new AuthButton("submit"));
			}
		
			
			@Override
			protected void onSubmit() {
				List<String> input = itmTermspload.getUploadedTerms();
				
				if (input != null && input.size() > 0 ) {

					if (input.size() == 1) {
						warn(new StringResourceModel("oneTermInFile", this, null).getString());
					}
					else {
						warn(new StringResourceModel("severalTermsInFile", this, null, 
								new Object[]{ input.size() }).getString());
					}
					
					for (int i = 0; i < input.size(); i++) {
						ItmTerm term = new ItmTerm(); 
						term.setValue(new LocalizedString(input.get(i), getLocale()));
						term.setConfiguration(getConfiguration());
						getConfiguration().addToTerms(term);
						itmTermDao.save(term);
					}
				}
				else {
					warn(new StringResourceModel("noTermsInFile", this, null).getString());
				}
			}
		};
		
		form.add(
			new LocalizedStringReorderableListPanel(
					"valueListPanel", 
					studyIsInState(StudyState.INIT), 
					studyLocale, 
					AddLink.ADD_TERM) 
			{

			@Override
			protected void delete(LocalizedString objectToDelete, List<LocalizedString> objects) {
				ItmTerm term = findTerm(objectToDelete);
				

				getConfiguration().removeFromTerms(term);
				// #429: we must set LocalizedString to null
				// as it may be already referenced by a 
				// preview survey
				term.setValue(null);
				itmTermDao.save(term);
				itmTermDao.delete(term);

				// also change display objects
				objects.remove(objectToDelete);
			}

			@Override
			protected List<LocalizedString> getDisplayObjects() {
				List<LocalizedString> rv = new ArrayList<LocalizedString>();
			    
				List<ItmTerm> terms = getConfiguration().getTerms();
				
				// terms may be null, because on after module
				// creation, the var is not initialized
				if(terms != null && !terms.isEmpty())
				{
					for (ItmTerm term: terms) {
				    	term.getValue().setPosition(term.getPosition());
				    	rv.add(term.getValue());
				    }	
				}
				
			    return rv;
			}

			@Override
			protected void moveDown(LocalizedString orderedObject, List<LocalizedString> objects) {
				
				ItmTerm itmTerm = findTerm(orderedObject);
				List<ItmTerm> itmTerms = getConfiguration().getTerms();
				OrderedEntityBase.moveDown(itmTerms, itmTerm);
				itmTermDao.save(itmTerms);
				
				// also change display objects
				OrderedEntityBase.moveDown(objects, orderedObject);
			}

			@Override
			protected void moveUp(LocalizedString orderedObject, List<LocalizedString> objects) {
				ItmTerm itmTerm = findTerm(orderedObject);
				List<ItmTerm> itmTerms = getConfiguration().getTerms();
				OrderedEntityBase.moveUp(itmTerms, itmTerm);
				itmTermDao.save(itmTerms);
				
				// also change display objects
				OrderedEntityBase.moveUp(objects, orderedObject);
			}

			@Override
			protected void append(LocalizedString localizedString) {
				ItmTerm term = new ItmTerm(); 
				term.setValue(localizedString);
				term.setConfiguration(getConfiguration());
				getConfiguration().addToTerms(term);
				itmTermDao.save(term);

			}
		});
		
		add(form);
	}
	
	@Override
	protected void onBeforeRender() {
		visitChildren(
			new DisableComponentIfStudyStateNotInitVisitor(
				new PropertyModel(getDefaultModel(), "study.state")
			)
		);
		super.onBeforeRender();
	}
	
	@Override
	protected StudyDependendDao<IconTermMatchingConfigurationEntity> getConfigurationDao() {
		return dao;
	}
	
	private ItmTerm findTerm(LocalizedString termValue) {
		if(termValue == null) return null;
		
		for (ItmTerm term: getConfiguration().getTerms()) {
			if ((term.getValue() != null) && term.getValue().equals(termValue)) {
				return term;
			}
		}
		
		return null;
	}
	
}

