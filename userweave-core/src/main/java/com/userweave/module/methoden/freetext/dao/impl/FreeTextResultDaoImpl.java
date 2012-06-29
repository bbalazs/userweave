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
package com.userweave.module.methoden.freetext.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.userweave.dao.impl.TestResultDaoImpl;
import com.userweave.module.methoden.freetext.dao.FreeTextResultDao;
import com.userweave.module.methoden.freetext.domain.FreeTextConfigurationEntity;
import com.userweave.module.methoden.freetext.domain.FreeTextResult;

@Repository
@Transactional
public class FreeTextResultDaoImpl extends TestResultDaoImpl<FreeTextResult, FreeTextConfigurationEntity> implements FreeTextResultDao {

	@Override
	public Class<FreeTextResult> getPersistentClass() {
		return FreeTextResult.class;
	}
	
	@Override
	public String getEntityResultName()
	{
		return "freetext_result";
	}
}
