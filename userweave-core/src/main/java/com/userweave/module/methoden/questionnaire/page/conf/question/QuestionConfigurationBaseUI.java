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
package com.userweave.module.methoden.questionnaire.page.conf.question;

import org.apache.wicket.ajax.AjaxRequestTarget;

import com.userweave.components.callback.EventHandler;
import com.userweave.components.configuration.ConfigurationBaseUI;
import com.userweave.domain.Study;
import com.userweave.domain.service.QuestionService;
import com.userweave.module.methoden.questionnaire.dao.QuestionDao;
import com.userweave.module.methoden.questionnaire.domain.question.Question;
import com.userweave.pages.components.slidableajaxtabpanel.ChangeTabsCallback;

/**
 * @author Florian Pavkovic
 * 
 * TODO: This calls does not need to extend the configuationbaseui, because
 * 		 there is nothing to update in this component. Child components
 * 		 are handling updates by themselfs.
 */
public abstract class QuestionConfigurationBaseUI<T extends Question> 
	extends ConfigurationBaseUI<T> 
{
	private static final long serialVersionUID = 1L;

	public QuestionConfigurationBaseUI(
			String id, 
			int entityId, 
			EventHandler callback,
			final ChangeTabsCallback changeTabsCallback) 
	{
		super(id, entityId);

//		studyLocale = locale;
	}

//	@Override
//	protected DetailsPanel createDetailsPanel(String id, int entityId, EventHandler callback) {
//		
//		return 
//			new QuestionDetails<T>(id, entityId, getType(), callback, getStudyLocale()) {
//
//				@Override
//				protected QuestionDao getQuestionDao() {
//					return QuestionConfigurationBaseUI.this.getQuestionDao();
//				}
//				
//				@Override
//				protected QuestionService getQuestionService() {
//					return QuestionConfigurationBaseUI.this.getQuestionService();
//				}
//				
//				@Override
//				protected int getConfigurationId() {
//					return getEntity().getConfiguration().getId();
//				}
//		
//				@Override
//				protected boolean deleteIsEnabled() {
//					return studyIsInState(StudyState.INIT);
//				}
//			}
//		;
//	}

	protected abstract QuestionDao getQuestionDao();
	
	@Override
	protected Study getStudy() {
		return getEntity().getConfiguration().getStudy();
	}

	protected abstract QuestionService getQuestionService();
	
//	@Override
//	public List<SimpleContentCreator> getContentCreator()
//	{
//		List<SimpleContentCreator> rv = new ArrayList<SimpleContentCreator>();
//
//		if(studyIsInState(StudyState.RUNNING, StudyState.FINISHED)) { 
//			rv.add(createReportTab());
//		}
//
//		return rv;
//	}
//	
//	protected SimpleContentCreator createReportTab() {
//		IModel resourceModel = new StringResourceModel(studyIsInState(StudyState.FINISHED) ? "eval" : "exec", this, null);
//		return new SimpleContentCreator(resourceModel) {
//				@Override
//				public Component createContent(String id) {
//					return new FilteredQuestionReportPanel(id, getEntity().getConfiguration().getId(), getEntityId());
//				}
//
//			};
//	}
	
	@Override
	public void onUpdate(AjaxRequestTarget target)
	{
		// TODO Auto-generated method stub
		
	}
}

