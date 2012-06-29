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
package com.userweave.module.methoden.iconunderstandability.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.userweave.dao.impl.BaseDaoImpl;
import com.userweave.module.methoden.iconunderstandability.dao.ReactionTimeStatisticsDao;
import com.userweave.module.methoden.iconunderstandability.domain.report.IconTestReactionTimeStatistics;

@Repository
@Transactional
public class ReactionTimeStatisticsDaoImpl extends BaseDaoImpl<IconTestReactionTimeStatistics> implements ReactionTimeStatisticsDao {

	@Override
	public Class<IconTestReactionTimeStatistics> getPersistentClass() {
		return IconTestReactionTimeStatistics.class;
	}

	@Override
	public IconTestReactionTimeStatistics findByIconCount(int iconCount) {
		String queryString = "from  " + getEntityName() + " where iconCount = :iconCount";
		return (IconTestReactionTimeStatistics) getCurrentSession().createQuery(queryString).setParameter("iconCount", iconCount).uniqueResult();
	}
}
