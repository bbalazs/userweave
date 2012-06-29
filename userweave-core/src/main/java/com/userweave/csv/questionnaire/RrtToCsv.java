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
package com.userweave.csv.questionnaire;

import java.util.List;
import java.util.Locale;

import org.apache.wicket.spring.injection.annot.SpringBean;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;

import com.userweave.csv.AbstractModuleCsvConverter;
import com.userweave.csv.table.CsvSurveyExecutionColumnGroup;
import com.userweave.csv.table.ICsvColumnGroup;
import com.userweave.csv.table.RrtCsvColumnGroup;
import com.userweave.module.methoden.rrt.dao.RrtResultDao;
import com.userweave.module.methoden.rrt.domain.RrtConfigurationEntity;
import com.userweave.module.methoden.rrt.domain.RrtResult;

import de.userprompt.utils_userweave.query.model.QueryObject;
import de.userprompt.utils_userweave.query.template.QueryTemplate;

public class RrtToCsv extends AbstractModuleCsvConverter<RrtConfigurationEntity>
{
	@SpringBean
	private RrtResultDao rrtResultDao;
	
	@Override
	public ICsvColumnGroup convertToCsv(
		RrtConfigurationEntity entity, 
		CsvSurveyExecutionColumnGroup idGroup, 
		Locale locale)
	{	
		// group for this module
		RrtCsvColumnGroup rrtGroup = 
			new RrtCsvColumnGroup(entity, getRrtResult(entity), idGroup, locale);
		
		return rrtGroup;
	}

	@SuppressWarnings("unchecked")
	private List<Object[]> getRrtResult(RrtConfigurationEntity configuration)
	{
		QueryObject queryObject = rrtResultDao.createQuery(configuration, null);

		queryObject.setResult("{result.*}, se.id as surveyexec_id");
		
		SQLQuery q =  new QueryTemplate(queryObject)
						.createSqlQuery(getCurrentSession());
		
		q.addEntity("result", RrtResult.class);
		q.addScalar("surveyexec_id", Hibernate.INTEGER);
		return q.list();
	}
	
}
