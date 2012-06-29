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
import com.userweave.module.methoden.iconunderstandability.dao.ScoreRangeListDao;
import com.userweave.module.methoden.iconunderstandability.domain.ScoreRangeList;
import com.userweave.module.methoden.iconunderstandability.domain.ScoreRangeList.ValueType;

@Repository
@Transactional
public class ScoreRangeListDaoImpl extends BaseDaoImpl<ScoreRangeList> implements ScoreRangeListDao {

	@Override
	public Class<ScoreRangeList> getPersistentClass() {
		return ScoreRangeList.class;
	}

	public ScoreRangeList findByType(ValueType type) {
		return (ScoreRangeList) getCurrentSession()
			.createQuery("from " + getEntityName() + " where type = :type")
			.setParameter("type", type)
			.uniqueResult();
	}
	
	public ScoreRangeList getScoreRangeListForDeviationFromMeanReactionTime() {	
		return findByType(ValueType.DEVIATION_FROM_MEAN_TIME);
	}

	public ScoreRangeList getScoreRangeListForAssignment() {
		return findByType(ValueType.ASSIGNMENT);
	}

	public ScoreRangeList getScoreRangeListForHighestAssignmentToOtherTerm() {
		return findByType(ValueType.HIGHEST_ASSIGNMENT_TO_OTHER_TERM);
	}

	public ScoreRangeList getScoreRangeListForMissingValue() {
		return findByType(ValueType.MISSING_VALUE);
	}

	public ScoreRangeList getScoreRangeListForReactionTime() {
		return findByType(ValueType.REACTION_TIME);
	}
}
