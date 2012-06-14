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
package com.userweave.module.methoden.mockup.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.userweave.dao.impl.TestResultDaoImpl;
import com.userweave.module.methoden.mockup.dao.MockupResultDao;
import com.userweave.module.methoden.mockup.domain.MockupConfigurationEntity;
import com.userweave.module.methoden.mockup.domain.MockupResult;

@Repository
@Transactional
public class MockupResultDaoImpl extends TestResultDaoImpl<MockupResult, MockupConfigurationEntity> implements MockupResultDao {

	@Override
	public Class<MockupResult> getPersistentClass() {
		return MockupResult.class;
	}
	
	@Override
	public String getEntityResultName()
	{
		return "mockup_result";
	}
	
}
