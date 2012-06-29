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
package com.userweave.batch;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;

import com.userweave.dao.LocalizedStringDao;
import com.userweave.dao.StudyDao;
import com.userweave.domain.LocalizedString;
import com.userweave.domain.Study;
import com.userweave.module.Module;
import com.userweave.module.ModuleConfiguration;

@Transactional
public class AdjustLocaleInLocalizedStrings {

	@Resource(name = "studyDaoImpl")
	private StudyDao studyDao;
	
	@Resource(name = "localizedStringDaoImpl")
	private LocalizedStringDao localizedStringDao;

	@Resource(name="modules")
	private List<Module<?>> modules;	
	
	public List<Study> getStudies() {
		return studyDao.findAll();	
	}
	
	public List<LocalizedString> getLocalizedStrings(int studyId) {
		List<LocalizedString> rv = new ArrayList<LocalizedString>();
		Study study = studyDao.findById(studyId);
		rv.addAll(study.getLocalizedStrings());
		for(ModuleConfiguration<?> module : study.getModuleConfigurations(modules)) {
			rv.addAll(module.getLocalizedStrings());
		}
		return rv;
	}
	
	public void save(LocalizedString localizedString) {
		localizedStringDao.save(localizedString);
		
	}
	


}
