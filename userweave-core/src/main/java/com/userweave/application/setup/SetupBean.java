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
package com.userweave.application.setup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.userweave.dao.ProjectDao;
import com.userweave.dao.ProjectUserRoleJoinDao;
import com.userweave.dao.RoleDao;
import com.userweave.dao.StudyDao;
import com.userweave.dao.SurveyExecutionDao;
import com.userweave.dao.UserDao;
import com.userweave.domain.Address;
import com.userweave.domain.Callnumber;
import com.userweave.domain.Project;
import com.userweave.domain.ProjectUserRoleJoin;
import com.userweave.domain.Role;
import com.userweave.domain.Study;
import com.userweave.domain.StudyState;
import com.userweave.domain.SurveyExecution;
import com.userweave.domain.User;
import com.userweave.module.methoden.iconunderstandability.dao.ScoreRangeListDao;
import com.userweave.module.methoden.iconunderstandability.domain.ScoreRangeList;
import com.userweave.module.methoden.iconunderstandability.domain.ScoreRangeList.Alignment;
import com.userweave.module.methoden.iconunderstandability.domain.ScoreRangeList.ValueType;
import com.userweave.module.methoden.iconunderstandability.service.ComputeIconTestStatistics;
import com.userweave.utils.LocalizationUtils;

@Transactional
public class SetupBean {
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Resource
	private ScoreRangeListDao valueToScoreListDao;
		
	@Resource
	private UserDao userDao;

	@Resource
	private ComputeIconTestStatistics computeIconTestStatistics;

	@Resource
	private SurveyExecutionDao surveyExecutionDao;

	@Resource
	private ProjectDao projectDao;
	
	@Resource
	private StudyDao studyDao;
	
	@Resource
	private RoleDao roleDao;
	
	@Resource
	private ProjectUserRoleJoinDao purjDao;
	
	/*
	public void saveUserForAllStudies() {
		List<Study> studies = studyDao.findAll();
		User user = userDao.findByEmail("user");
		User tvbuser = userDao.findByEmail("bodo@wannawork.de");
		for(Study study: studies) {
			if(study.getOwner() == null) {
				study.setOwner(user);
				studyDao.save(study);
			}
			if((study.getName() != null) && (study.getName().startsWith("Bed."))) {
				study.setOwner(tvbuser);
				studyDao.save(study);
			}
		}
	}
	*/
	
	public void setupIconTestScoreTables() {
		
		createAndSaveScoreRangeList(
				ValueType.DEVIATION_FROM_MEAN_TIME,
				Alignment.SYMMETRIC,
				new Integer[]{null, -30, -20, -15, -10, -5, 5, 10, 15, 20, 30, null}, 
				true);
		
		createAndSaveScoreRangeList(
				ValueType.MISSING_VALUE, 
				Alignment.NOT_SYMMETRIC,
				new Integer[]{0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 101}, 
				true);
		
		createAndSaveScoreRangeList(
				ValueType.ASSIGNMENT, 
				Alignment.NOT_SYMMETRIC,
				new Integer[]{0, 1, 10, 20, 30, 40, 50, 60, 70, 80, 90, 101}, 
				new Integer[]{0, 1, 2, 3, 4, 5 ,6, 7, 8, 9,  10},
				true);
		
		createAndSaveScoreRangeList(
				ValueType.REACTION_TIME, 
				Alignment.SYMMETRIC,
				new Integer[]{null, -30, -20, -15, -10, -5, 5, 10, 15, 20, 30, null}, 
				true);
		
		createAndSaveScoreRangeList(
				ValueType.HIGHEST_ASSIGNMENT_TO_OTHER_TERM,
				Alignment.NOT_SYMMETRIC,
				new Integer[]{0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 101}, 
				true);
	}

	private void createAndSaveScoreRangeList(ValueType type, Alignment alignment, Integer[] scoreValues, boolean zeroToTen) {
		createAndSaveScoreRangeList(type, alignment, scoreValues, null, zeroToTen);
	}
	
	private void createAndSaveScoreRangeList(ValueType type, Alignment alignment, Integer[] scoreValues, Integer[] rangeValues, boolean zeroToTen) {
		ScoreRangeList deviationFromMeanTime = valueToScoreListDao.findByType(type);
		if(deviationFromMeanTime == null) {
			LOGGER.info("Create new ScoreRangeList for type "+type);
			deviationFromMeanTime = new ScoreRangeList();
		} else {
			LOGGER.info("update existing ScoreRangeList for type "+type);
		}
		createScoreRangeList(type, alignment, scoreValues, rangeValues,
				zeroToTen, deviationFromMeanTime);
		
		valueToScoreListDao.save(deviationFromMeanTime);
	}

	public static ScoreRangeList createScoreRangeList(ValueType type, Alignment alignment,
			Integer[] scoreValues, Integer[] rangeValues, boolean zeroToTen) {
		ScoreRangeList deviationFromMeanTime = new ScoreRangeList();

		createScoreRangeList(type, alignment, scoreValues, rangeValues,
				zeroToTen, deviationFromMeanTime);
		return deviationFromMeanTime;
	}

	private static ScoreRangeList createScoreRangeList(ValueType type, 
			Alignment alignment, Integer[] scoreValues, Integer[] rangeValues,
			boolean zeroToTen, ScoreRangeList deviationFromMeanTime) {
		deviationFromMeanTime.setType(type);
		deviationFromMeanTime.setAlignment(alignment);
		List<Integer> values = Arrays.asList(scoreValues);
	
		for (int count=0; count < values.size(); count++) {
			if (count < values.size()-1) {
				int score;
				if (zeroToTen) {
					score = count;
				} else {
					score = 10 - count;
				}				
				if (rangeValues == null) {
					deviationFromMeanTime.setScore(count, 10-count);
				} else {
					deviationFromMeanTime.setScore(count, rangeValues[count]);
				}
			}
			Integer range = values.get(count);			
			deviationFromMeanTime.setRange(count, range);			
		}
		return deviationFromMeanTime;
	}

	
	public void setupBasicUsers() {
		createAdminUser();
		createStandardUser();
		createUserTVBrowser();
		//saveUserForAllStudies();
	}
	
	@Transactional
	private void createAdminUser() {
		User user = findOcreateUser("admin");
		user.setAdmin(true);
		user.setEmail("admin");
		user.setForename("Vorname");
		user.setSurname("Nachname");
		user.setPasswordMD5("pantone382");
		user.setLocale(LocalizationUtils.getSupportedConfigurationFrontendLocale("de"));
		user.setVerified(true);		
		userDao.save(user);
	}

	@Transactional
	private void createUserTVBrowser() {
		User user = findOcreateUser("bodo@wannawork.de");		
		user.setAdmin(false);
		user.setEmail("bodo@wannawork.de");
		user.setForename("Bodo");
		user.setSurname("Tasche");
		user.setPasswordMD5("tvb?");
		user.setLocale(LocalizationUtils.getSupportedConfigurationFrontendLocale("de"));
		user.setVerified(true);		
		userDao.save(user);
	}

	@Transactional
	private void createStandardUser() {
		User user = findOcreateUser("user");		
		user.setAdmin(false);
		user.setEmail("user");
		user.setForename("Vorname");
		user.setSurname("Nachname");
		user.setPasswordMD5("pantone382");
		user.setLocale(LocalizationUtils.getSupportedConfigurationFrontendLocale("de"));
		user.setVerified(true);		
		userDao.save(user);
	}
	
	private User findOcreateUser(String email) {
		User user = userDao.findByEmail(email);
		if(user == null) {
			LOGGER.info("Create new user: " + email);
			user = new User();
			user.setCallnumber(new Callnumber());
			user.setAddress(new Address());
			userDao.save(user);
		} else {
			LOGGER.info("update existing user: " + email);
		}
		return user;
	}

	public void setupStatistics() {
		computeIconTestStatistics.computeStatisticsAndSave();
	}
	
	public void setupSurveyExecutionStates() {
		List<SurveyExecution> surveys = surveyExecutionDao.findAll();
		LOGGER.info("computing SurveyExecutionStatus for "+surveys.size()+" surveys");
		int i = 0;
		for(SurveyExecution survey : surveys) {
			i++;
			if(survey.setCompletionState()) {
				LOGGER.info("updated survey "+i+" to state "+survey.getState());
				surveyExecutionDao.save(survey);
			}
		}
	}
	
	@Transactional
	public void setupProjects()
	{		
		LOGGER.info("Starting setup of projects");
		
		List<User> userList = userDao.findAllByEmail();
		
		Role projectAdmin = roleDao.findByName(Role.PROJECT_ADMIN);
		
		if(projectAdmin == null)
		{
			projectAdmin = new Role();
			
			projectAdmin.setRoleName("PROJECT_ADMIN");
			
			LOGGER.info("Create role: PROJECT_ADMIN");
			
			roleDao.save(projectAdmin);
		}
		
		
		
		for(User u : userList)
		{
			LOGGER.info("Setup default project of: " + u.getEmail());
			if(u.getHasAlreadyDefaultProject() != null && u.getHasAlreadyDefaultProject() == true)
			{
				LOGGER.info("	" + u.getEmail() + " already has a default project. Skipping.");
				continue;
			}
			
			// create default project for each user
			Project p = new Project();
			
			p.setPrivate(false);
			
			p.setName(u.getForename() + " " + u.getSurname() + " - default project");
			
			p.setParentProject(null);
			
			LOGGER.info("	Create project: " + p.getName());
			
			projectDao.save(p);
			
			
			ArrayList<Study> studies = new ArrayList<Study>();
			
			studies.addAll(studyDao.findByOwnerAndState(u, true, StudyState.INIT));
			
			studies.addAll(studyDao.findByOwnerAndState(u, true, StudyState.RUNNING));
			
			studies.addAll(studyDao.findByOwnerAndState(u, true, StudyState.FINISHED));
			
			LOGGER.info("	Moving studies of " + u.getEmail() + " to " + p.getName());
			
			for(Study s : studies)
			{
				// remap studies of user to users default project
				s.setParentProject(p);
				
				studyDao.save(s);
			}
			
			LOGGER.info("	Moving complete. Setting up references.");
			
			// Create ternary association between user, project and role
			ProjectUserRoleJoin join = purjDao.createJoin(p, u, projectAdmin);
			purjDao.save(join);
			
			u.setHasAlreadyDefaultProject(true);
			userDao.save(u);
			
			LOGGER.info("	References complete.");
		}
		
		LOGGER.info("Creating projects finished.");
	}
}
