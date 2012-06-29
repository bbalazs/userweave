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

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.userweave.domain.service.StudyService;

public class DeleteStudyBatch {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-batch.xml");
		StudyService studyService = (StudyService) context.getBean("studyService");
		if(args.length == 0) {
			purge(studyService, false);
		} else {
			if(args[0].equals("-a")) {
				purge(studyService, true);
			} else {
				List<Integer> ids = new ArrayList<Integer>(args.length);
				for(String arg: args) {
					Integer id = Integer.valueOf(arg);
					ids.add(id);
				}
				purge(studyService, ids);
			}
		}
	}

	private static void purge(StudyService studyService, List<Integer> studies) {
		for(Integer id : studies) {
			try {
				studyService.purge(id);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
	}

	private static void purge(StudyService studyService, boolean deleteAll) {
		List<Integer> studies = null;
		if(deleteAll) {
			studies = studyService.findAll();
		} else {
			studies = studyService.findAgedDeletedStudies();
		}
		purge(studyService, studies);
	}
}
