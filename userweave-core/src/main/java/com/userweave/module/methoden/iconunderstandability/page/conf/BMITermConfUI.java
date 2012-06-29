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
package com.userweave.module.methoden.iconunderstandability.page.conf;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.components.configuration.ConfigurationBaseUI;
import com.userweave.domain.Study;
import com.userweave.module.methoden.iconunderstandability.dao.ItmTermDao;
import com.userweave.module.methoden.iconunderstandability.domain.ItmTerm;

@SuppressWarnings("serial")
@Deprecated
public class BMITermConfUI extends ConfigurationBaseUI<ItmTerm> {

	@SpringBean
	private ItmTermDao termDao;

	public BMITermConfUI(String id, int termId) {
		super(id, termId);
	}

//	@Override
//	protected Panel createDetailsPanel(String id, int entityId, EventHandler callback) {
//		return new BMITermDetailsPanel(id, getEntityId(), callback);
//	}

//	protected SimpleContentCreator createReportTab(boolean showDetails) {
//		IModel resourceModel = new StringResourceModel(studyIsInState(StudyState.FINISHED) ? "eval" : "exec", this, null);
//		return new SimpleContentCreator(resourceModel) {
//
//			@Override
//			public Component createContent(String id) {
//				return new BMITermReportPanel(id, getEntity().getConfiguration(), getEntity().getId(), getStudyLocale());
//			}
//		};
//	}

	@Override
	public ItmTermDao getBaseDao() {
		return termDao;
	}

//	@Override
//	protected List<SimpleContentCreator> getContentCreator() {
//		return Collections.singletonList(createReportTab(true));
//	}

	@Override
	protected Study getStudy() {
		return getEntity().getConfiguration().getStudy();
	}

	@Override
	public void onUpdate(AjaxRequestTarget target)
	{
		// TODO Auto-generated method stub
		
	}

//	@Override
//	protected IModel getType() {
//		// FIXME: StringResourceModel hier
//		return new Model("IconTerm");
//	}

}
