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
package com.userweave.domain.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.userweave.application.UserWeaveSession;
import com.userweave.dao.ConfigurationDao;
import com.userweave.dao.ProjectUserRoleJoinDao;
import com.userweave.dao.StudyDao;
import com.userweave.dao.SurveyExecutionDao;
import com.userweave.domain.ImageBase;
import com.userweave.domain.LocalizedString;
import com.userweave.domain.ModuleConfigurationEntityBase;
import com.userweave.domain.Project;
import com.userweave.domain.Study;
import com.userweave.domain.StudyState;
import com.userweave.domain.SurveyExecution;
import com.userweave.domain.SurveyExecutionState;
import com.userweave.domain.User;
import com.userweave.domain.service.ModuleService;
import com.userweave.domain.service.QuestionService;
import com.userweave.domain.service.StudyService;
import com.userweave.domain.util.HashProvider;
import com.userweave.module.ModuleConfiguration;
import com.userweave.module.ModuleConfigurationState;
import com.userweave.module.methoden.freetext.FreeTextMethod;
import com.userweave.module.methoden.freetext.dao.FreeTextConfigurationDao;
import com.userweave.module.methoden.freetext.domain.FreeTextConfigurationEntity;
import com.userweave.module.methoden.mockup.MockupMethod;
import com.userweave.module.methoden.mockup.dao.MockupConfigurationDao;
import com.userweave.module.methoden.mockup.domain.MockupConfigurationEntity;
import com.userweave.module.methoden.questionnaire.QuestionnaireMethod;
import com.userweave.module.methoden.questionnaire.domain.QuestionnaireConfigurationEntity;
import com.userweave.module.methoden.questionnaire.domain.question.AntipodePair;
import com.userweave.module.methoden.questionnaire.domain.question.FreeQuestion;
import com.userweave.module.methoden.questionnaire.domain.question.MultipleRatingQuestion;
import com.userweave.module.methoden.questionnaire.domain.question.Question;
import com.userweave.module.methoden.questionnaire.domain.question.FreeQuestion.AnswerType;
import com.userweave.utils.LocalizationUtils;

@Service(value="studyService")
public class StudyServiceImpl implements StudyService {
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	@Resource
    private SessionFactory sessionFactory;

    protected Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}
	
	@Resource
	private StudyDao studyDao;
	
	@Resource
	private ProjectUserRoleJoinDao purjDao;
	
	@Resource
	private SurveyExecutionDao surveyExecutionDao;
	
	@Resource
	private ModuleService moduleService;
	
	@Resource
	private ConfigurationDao moduleConfigurationDao;

	@Resource
	private FreeTextConfigurationDao freeTextConfigurationDao;
	
	@Resource
	private MockupConfigurationDao mockupConfigurationDao;

	@Resource
	private QuestionService questionService;
	
	@Transactional
	public Study finalizeStudy(Study study) {
		study.setOwner(UserWeaveSession.get().getUser()); // creator of study
		study.setState(StudyState.INIT);
		
		if(study.getLocale() != null) {
			// store default locale also in supportedlocales 
			study.getSupportedLocales().add(study.getLocale());
		}
		study.setHashCode(HashProvider.uniqueUUID());
		study.setReportCode(HashProvider.uniqueUUID());
		study.setCreationDate(new DateTime());

		studyDao.save(study);
		
		createFreeTextConfiguration(study, "Goodbye", "");
		createFreeTextConfiguration(study, "Welcome", "");

		return study;
	}
	
	private void createFreeTextConfiguration(Study study, String name, String text) {
		FreeTextConfigurationEntity freeText = (FreeTextConfigurationEntity) moduleService.createNewConfigurationInStudyForModule(FreeTextMethod.moduleId, study);
		freeText.setName(name);
		freeTextConfigurationDao.save(freeText);
		freeText.setFreeText(new LocalizedString(text, Locale.GERMAN));
		freeText.setState(ModuleConfigurationState.ACTIVE);
	}
	
	private void createFreeTextConfiguration(Study study, String name, LocalizedString text, LocalizedString description) {
		FreeTextConfigurationEntity freeText = (FreeTextConfigurationEntity) moduleService.createNewConfigurationInStudyForModule(FreeTextMethod.moduleId, study);
		freeText.setName(name);
		freeText.setDescription(description);
		freeTextConfigurationDao.save(freeText);
		freeText.setFreeText(text);
		freeText.setState(ModuleConfigurationState.ACTIVE);
	}

	@Transactional
	public SurveyExecution createSurveyExecution(Study study, String hashCode) {
		SurveyExecution surveyExecution = getSurveyExecution(hashCode);
		
		if(surveyExecution == null && study != null) {
			surveyExecution = new SurveyExecution();
			surveyExecution.setStudy(study);
			surveyExecution.setStudyState(study.getState());
			if(surveyExecution.executedWhileStudyInRunningState()) {
				surveyExecution.setState(SurveyExecutionState.NOT_STARTED);
			} else {
				surveyExecution.setState(SurveyExecutionState.INVALID);
			}
			surveyExecution.setModulesExecuted(0);
			surveyExecutionDao.save(surveyExecution);
			surveyExecution.createHashCode();
			surveyExecutionDao.save(surveyExecution);
		} 
		
		return surveyExecution;
	}
	
	private SurveyExecution getSurveyExecution(String hashCode) {
		if(hashCode == null) {
			return null;
		}
		return surveyExecutionDao.findByHashcode(hashCode);
	}

	
	@Override
	@Transactional
	public void purge(Integer studyId) {
		purge(studyDao.findById(studyId));
	} 
	
	@Override
	@Transactional
	public void purge(Study study) {
		if(study == null) {
			return;
		}
		LOGGER.info("start purge study with id "+study.getId() +" ("+study.getName()+")");
		List<ModuleConfiguration> configurations = moduleService.getModuleConfigurationsForStudy(study);
		for (ModuleConfiguration moduleConfiguration : configurations) {
			LOGGER.info("purge moduleConfiguration with id "+moduleConfiguration.getId() +" ("+moduleConfiguration.getName()+")");
			moduleConfigurationDao.delete((ModuleConfigurationEntityBase) moduleConfiguration);
		}
		LOGGER.info("purge study with id "+study.getId() +" ("+study.getName()+")");
		studyDao.delete(study);
		LOGGER.info("done purge study with id "+study.getId() +" ("+study.getName()+")");

	}

	public void sort(List<Study> studies) {
		Collections.sort(studies, 
			new Comparator<Study>() {
				public int compare(Study study1, Study study2) {
					
					int comparedState = study1.getState().compareTo(study2.getState());
					if (comparedState == 0) {
						String name1 = study1.getName();
						String name2 = study2.getName();
						if ((name1 != null) && (name2 != null)) {
							return name1.compareTo(name2);
						} else {
							return 0;
						}
					} else {
						return comparedState;
					}
				}			
			}
		);	
	}

//	@Override
//	public List<Study> findByOwner(User user) {
//		return findByOwner(user, false);
//	}
//
//	@Override
//	public List<Study> findByOwner(User user, boolean alsoDeleted) {
//		return findByOwnerAndState(user, alsoDeleted, null);
//	}
//
//	@Override
//	public List<Study> findByOwnerAndState(User user, StudyState state) {
//		return findByOwnerAndState(user, false, state);
//	}
//
//	@Override
//	public List<Study> findByOwnerAndState(User user, boolean alsoDeleted, StudyState state) {
//		return studyDao.findByOwnerAndState(user, alsoDeleted, state);
//	}

	@Override
	public List<Study> findDeletedStudies(User user)
	{
		return studyDao.findDeletedStudies(user);
	}
	
	@Override
	public void delete(Study study) {
		study.setDeletedAt(new DateTime());
		studyDao.save(study);
	}

	@Override
	public void restore(Study study) {
		study.setDeletedAt(null);
		studyDao.save(study);
	}
	
	@Override
	@Transactional
	public Study copy(Study study, String name, Project parentProject) {
		Study studyClone = study.copy();
		// FIXME find a better way to handle the name of a copied study
		studyClone.setName(name);
		studyClone.setParentProject(parentProject);
		studyClone.setCreationDate(new DateTime());
		studyClone.setState(StudyState.INIT);
		studyClone.setHashCode(HashProvider.uniqueUUID());
		studyDao.save(studyClone);
		
		for(ModuleConfiguration moduleConfiguration : study.getModuleConfigurations(moduleService.getModules())) {

			ModuleConfiguration moduleConfigurationCopy = moduleConfiguration.copy();
			moduleConfigurationCopy.setStudy(studyClone);
			moduleConfigurationCopy.save();
			
		} 
		return studyClone;
	}

	@Override
	@Transactional
	public List<Integer> findAgedDeletedStudies() {
		List<Study> studies = studyDao.findAgedDeletedStudies();
		List<Integer> rv = new ArrayList<Integer>();
		for(Study study : studies) {
			rv.add(study.getId());
		}
		return rv;
	}

	@Override
	@Transactional
	public List<Integer> findAll() {
		List<Study> studies = studyDao.findAll();
		List<Integer> rv = new ArrayList<Integer>();
		for(Study study : studies) {
			rv.add(study.getId());
		}
		return rv;
	}

	@Override
	public Study load(int studyId) {
		return studyDao.findById(studyId);
	}

//	@Override
//	@Transactional
//	public void purge(User user) {
//		List<Study> studies = findByOwner(user,true);
//		for(Study study : studies) {
//			purge(study);
//		}
//	}

	@Override
	public void purge(Project project)
	{
		List<Study> studies = findByProject(project,true);
		for(Study study : studies) {
			purge(study);
		}
		
	}

	private void createPresentationConfiguration(Study study, String name, LocalizedString text, LocalizedString url, int durationInSec) {
		MockupConfigurationEntity mockup = (MockupConfigurationEntity) moduleService.createNewConfigurationInStudyForModule(MockupMethod.moduleId, study);
		mockup.setName(name);
		mockup.setLocaleUrl(url);
		mockup.setTime(durationInSec);
		mockup.setTimeVisible(true);
		mockupConfigurationDao.save(mockup);
		mockup.setFreeText(text);
		mockup.setState(ModuleConfigurationState.ACTIVE);
	}
	
	@Override
	@Transactional
	public Study createPreConfiguredPresentationStudy(LocalizedString imgUrl, Locale locale, String studyName){
		Study newStudy = new Study();
		
		if (locale == null) {
			newStudy.setLocale(LocalizationUtils.getDefaultLocale());
		}
		else {
			newStudy.setLocale(locale);
		}
		// store default locale also in supportedlocales 
		newStudy.getSupportedLocales().add(newStudy.getLocale());
		
		if (!locale.equals(Locale.GERMAN)) {
			newStudy.getSupportedLocales().add(Locale.GERMAN);
		}
		if (!locale.equals(Locale.ENGLISH)) {
			newStudy.getSupportedLocales().add(Locale.ENGLISH);
		}
		
		if(studyName.equals(""))
		{
			newStudy.setName("pidoco initialised study");
		}
		else
		{
			newStudy.setName(studyName);
		}
		
		LocalizedString headline = new LocalizedString("How is your impression?", Locale.ENGLISH);
		headline.setValueDE("Wie ist Ihr Eindruck?");
		newStudy.setHeadline( headline );
		LocalizedString description = new LocalizedString("Please take a close look at our prototype and tell us how you like it!", Locale.ENGLISH); 
		description.setValueDE("Bitte schauen Sie sich unseren Prototypen genau an und sagen Sie uns, wie er Ihnen gefällt!");
		newStudy.setDescription( description );
		
		newStudy.setOwner(UserWeaveSession.get().getUser());
		newStudy.setState(StudyState.INIT);
		newStudy.setHashCode(HashProvider.uniqueUUID());
		newStudy.setReportCode(HashProvider.uniqueUUID());
		newStudy.setCreationDate(new DateTime());
		
		studyDao.save(newStudy);
		
		LocalizedString goobyeText = new LocalizedString("Thank you very much - your feedback helps us a lot", Locale.ENGLISH);
		goobyeText.setValueDE("Vielen Dank - Ihre Meinung hilft uns sehr weiter!");
		LocalizedString goobyeDescription = new LocalizedString("Thank you!", Locale.ENGLISH);
		goobyeDescription.setValueDE("Danke!");
		createFreeTextConfiguration(newStudy, "Goodbye", goobyeText, goobyeDescription);
		
		LocalizedString welcomeText = new LocalizedString("On the following page we will show you a prototype for 20 seconds. After that we will ask you a couple of questions concerning your impression.<br /><br />"
				+"Please click \"Next\" in lower right to show the prototype."
				, Locale.ENGLISH);
		welcomeText.setValueDE("Auf der folgenden Seite zeigen wir Ihnen für 20 Sekunden einen Prototypen. Danach haben wir ein paar wenige Fragen zu Ihrem Eindruck.<br /><br />"
				+ "Bitte klicken Sie unten rechts auf \"Weiter\" um den Prototypen anzuzeigen.");
		LocalizedString welcomeDescription = new LocalizedString("Welcome!", Locale.ENGLISH);
		welcomeDescription.setValueDE("Willkommen!");
		createFreeTextConfiguration(newStudy, "Welcome", welcomeText, welcomeDescription);
		
		
		LocalizedString mockupText = new LocalizedString("Please do not click - you will be forwarded automatically", Locale.ENGLISH);
		mockupText.setValueDE("Bitte klicken sie nicht - Sie werden automatisch weitergeleitet");
		
		createPresentationConfiguration(newStudy, "Presentation", mockupText, imgUrl, 20);
		
		/*
		 * create question module
		 */
		QuestionnaireConfigurationEntity questionConf = (QuestionnaireConfigurationEntity) moduleService.createNewConfigurationInStudyForModule(QuestionnaireMethod.moduleId, newStudy);
		questionConf.setName("Question");
		LocalizedString questDes = new LocalizedString("Evaluation of the prototype", Locale.ENGLISH);
		questDes.setValueDE("Ihre Bewertung des Prototypen");
		questionConf.setDescription( questDes );
		questionConf.setQuestions(new ArrayList<Question>());
		
		
		/*
		 * create evalution question
		 */
		MultipleRatingQuestion multiRatingQuestion = new MultipleRatingQuestion();
		Question curQuestion = questionService.createQuestion(questionConf.getId(), "Evaluation", MultipleRatingQuestion.TYPE);
		multiRatingQuestion = (MultipleRatingQuestion) curQuestion;
		
		LocalizedString multiRatingQuestionName = new LocalizedString("Evaluation", Locale.ENGLISH);
		multiRatingQuestionName.setValueDE("Auswertung");
		multiRatingQuestion.setName(multiRatingQuestionName);
		
		LocalizedString stimulusMultiRating = new LocalizedString("Please rate how much you agree to the following statements about the prototype you just saw!", Locale.ENGLISH);
		stimulusMultiRating.setValueDE("Bitte bewerten Sie, wie weit Sie den folgenden Aussagen über den gerade gezeigten Prototypen zustimmen!");
		multiRatingQuestion.setText(stimulusMultiRating);
		
		
		LocalizedString antipode1 = new LocalizedString("Fully agree", Locale.ENGLISH);
		antipode1.setValueDE("Stimme voll zu");
		LocalizedString antipode2 = new LocalizedString("Fully disagree", Locale.ENGLISH);
		antipode2.setValueDE("Stimme gar nicht zu");
	
		AntipodePair antipodePair = new AntipodePair();
		antipodePair.setAntipode1(antipode1);
		antipodePair.setAntipode2(antipode2);
		multiRatingQuestion.setAntipodePair(antipodePair);

		LocalizedString ans1 = new LocalizedString("The prototype is clearly arranged.", Locale.ENGLISH);
		ans1.setValueDE("Der Prototyp ist übersichtlich.");
		multiRatingQuestion.addToRatingTerms(ans1);
		
		LocalizedString ans2 = new LocalizedString("I can immediately see what the prototype offers.", Locale.ENGLISH);
		ans2.setValueDE("Es ist sofort verständlich was der Prototyp bietet.");
		multiRatingQuestion.addToRatingTerms(ans2);
		
		LocalizedString ans3 = new LocalizedString("The prototype seems to contain all functions important to me.", Locale.ENGLISH);
		ans3.setValueDE("Der Prototyp scheint alle mir wichtigen Funktionen zu beinhalten.");
		multiRatingQuestion.addToRatingTerms(ans3);
		
		questionConf.addToQuestions(multiRatingQuestion);
		
		/*
 		 * 
		 */
		FreeQuestion freeQuestion = new FreeQuestion();
		curQuestion = questionService.createQuestion(questionConf.getId(), "Free answer", FreeQuestion.TYPE);
		freeQuestion = (FreeQuestion) curQuestion;
		
		freeQuestion.setAnswerType(AnswerType.LONG_TEXT);
		LocalizedString freeQuestionName = new LocalizedString("Annotations to the prototype?", Locale.ENGLISH);
		freeQuestionName.setValueDE("Anmerkungen zum Protoypen");
		freeQuestion.setName(freeQuestionName);
		
		LocalizedString stimulusFree = new LocalizedString("Do you have any annotations to the prototype?", Locale.ENGLISH);
		stimulusFree.setValueDE("Haben Sie Anmerkungen zum Protoypen?");
		freeQuestion.setText(stimulusFree);
		
		questionConf.addToQuestions(freeQuestion);
	
		return newStudy;
	}

	@Override
	public Study preConfigureStudy(Project parentProject)
	{
		Study study = new Study();
		
		ImageBase logo = parentProject.getLogo();
		
		study.setBackgroundColor(parentProject.getBackgroundColor());
		study.setFontColor(parentProject.getFontColor());
		study.setLogo(logo != null ? logo.copy() : null);
        study.setFinishedPageUrl(Study.DEFAULT_FINISHED_URL);
        study.setNotAvailableUrl(Study.DEFAULT_FINISHED_URL);
        study.setParentProject(parentProject);
        study.setLocale(LocalizationUtils.getDefaultLocale());
        
        return study;
	}
	
	@Override
	public List<Study> findByProject(Project project)
	{
		return findByProject(project, false);
	}
	
	@Override
	public List<Study> findByProject(Project project, boolean alsoDeleted)
	{
		return findByProjectAndState(project, alsoDeleted, null);
	}

	@Override
	public List<Study> findByProjectAndState(Project project, StudyState state)
	{
		return findByProjectAndState(project, false, state);
	}

	@Override
	public List<Study> findByProjectAndState(Project project,
			boolean alsoDeleted, StudyState state)
	{
		return studyDao.findByProjectAndState(project, alsoDeleted, state);
	}
	
	@Override
	public List<Study> findByProjectIdAndState(int projectId,
			boolean alsoDeleted, StudyState state)
	{
		return studyDao.findByProjectIdAndState(projectId, alsoDeleted, state);
	}

	@Override
	public List<Study> findDeletedStudies(Project project)
	{
		return studyDao.findDeletedStudies(project);
	}
	
	@Override
	public boolean isAtLeastOneAdminRegistered(Project project)
	{
		// see #935
//		boolean isRegistered = false;
//		
//		List<ProjectUserRoleJoin> projectAdmins = 
//			purjDao.getProjectAdmins(project);
//		
//		for(ProjectUserRoleJoin projectAdmin : projectAdmins)
//		{
//			if(projectAdmin.getUser().isRegistered())
//			{
//				isRegistered = true;
//				break;
//			}
//		}
//		
//		return isRegistered;
		
		return true;
	}
	
	@Override
	public void generateStudyResults(Integer studyId)
	{
		Session session = getCurrentSession();
		
		// create queries
		String freetextQuery = insertFreeTextAnswer();
		String freenumberQuery = insertFreeNumberAnswer();
		String multipleanswerQuery = insertMultipleAnswer();
		String dimensionQuery = insertSingleDimension();
		String singleanswerQuery = insertSingleAnswer();
		String ratingQuery = insertSingleRating();
		
		// create query objects.
		SQLQuery freetext = session.createSQLQuery(freetextQuery);
		SQLQuery freenumber = session.createSQLQuery(freenumberQuery);
		SQLQuery multipleanswer = session.createSQLQuery(multipleanswerQuery);
		SQLQuery dimension = session.createSQLQuery(dimensionQuery);
		SQLQuery singleanswer = session.createSQLQuery(singleanswerQuery);
		SQLQuery rating = session.createSQLQuery(ratingQuery);
		
		// set study id
		freetext.setParameter("studyId", studyId);
		freenumber.setParameter("studyId", studyId);
		multipleanswer.setParameter("studyId", studyId);
		dimension.setParameter("studyId", studyId);
		singleanswer.setParameter("studyId", studyId);
		rating.setParameter("studyId", studyId);
		
		freetext.executeUpdate();
		freenumber.executeUpdate();
		multipleanswer.executeUpdate();
		dimension.executeUpdate();
		singleanswer.executeUpdate();
		rating.executeUpdate();
	}
	
	private String insertFreeTextAnswer()
	{
		String results_freetextanswer = 
			"INSERT INTO results_freetextanswer(" +
				"freetextanswer_id, surveyexecution_id, question_id, \"text\") " +
			"SELECT qa.id, se.id, qa.question_id, qa.text " +
			"FROM questionnaire_answer AS qa " +
			"LEFT JOIN questionnaire_result AS qr ON (qa.result_id = qr.id) " +
			"LEFT JOIN surveyexecution AS se ON (se.id = qr.surveyexecution_id)" +
			"WHERE qa.dtype = 'FreeTextAnswer' AND " +
				  "qr.executionstarted is not null AND " +
				  "se.state >= 2 AND " +
				  "se.study_id = :studyId AND " +
				  "NOT EXISTS (" +
				  	"SELECT r_fta.freetextanswer_id, " +
				  		   "r_fta.surveyexecution_id, " +
				  		   "r_fta.question_id, " +
				  		   "r_fta.text " +
				  	"FROM results_freetextanswer r_fta " +
				  	"WHERE  r_fta.freetextanswer_id = qa.id AND " +
				  		   "r_fta.surveyexecution_id = se.id AND " +
				  		   "r_fta.question_id = qa.question_id AND " +
				  		   "r_fta.text = qa.text);";
		
		return results_freetextanswer;
	}
	
	private String insertFreeNumberAnswer()
	{
		String results_freenumberanswer = 
			"INSERT INTO results_freenumberanswer(" +
				"freenumberanswer_id, surveyexecution_id, question_id, \"number\") " +
			"SELECT qa.id, se.id, qa.question_id, qa.number " +
			"FROM questionnaire_answer AS qa " +
			"LEFT JOIN questionnaire_result as qr ON (qa.result_id = qr.id) " +
			"LEFT JOIN surveyexecution AS se ON(se.id = qr.surveyexecution_id)" +
			"WHERE qa.dtype = 'FreeNumberAnswer' AND " +
				  "qr.executionstarted is not null AND " +
				  "se.state >= 2 AND " +
				  "se.study_id = :studyId AND " +
				  "NOT EXISTS (" +
				  	"SELECT r_fna.freenumberanswer_id, " +
				  		   "r_fna.surveyexecution_id, " +
				  		   "r_fna.question_id, " +
				  		   "r_fna.number " +
				  	"FROM results_freenumberanswer r_fna " +
				  	"WHERE  r_fna.freenumberanswer_id = qa.id AND " +
				  		   "r_fna.surveyexecution_id = se.id AND " +
				  		   "r_fna.question_id = qa.question_id AND " +
				  		   "r_fna.number = qa.number);";
		
		return results_freenumberanswer;
	}
	
	private String insertMultipleAnswer()
	{
		String results_multipleanswer = 
			"INSERT INTO results_multipleanswer(" +
				"multipleanswer_id, surveyexecution_id, question_id) " +
			"SELECT qa.id, se.id, qa.question_id " +
			"FROM questionnaire_answer AS qa " +
			"LEFT JOIN questionnaire_result as qr ON (qa.result_id = qr.id) " +
			"LEFT JOIN surveyexecution AS se ON(se.id = qr.surveyexecution_id)" +
			"WHERE qa.dtype = 'MultipleAnswersAnwer' AND " +
				  "qr.executionstarted is not null AND " +
				  "se.state >= 2 AND " +
				  "se.study_id = :studyId AND " +
				  "NOT EXISTS (" +
				  	"SELECT r_ma.multipleanswer_id, " +
				  		   "r_ma.surveyexecution_id, " +
				  		   "r_ma.question_id " +
				  	"FROM results_multipleanswer r_ma " +
				  	"WHERE  r_ma.multipleanswer_id = qa.id AND " +
				  		   "r_ma.surveyexecution_id = se.id AND " +
				  		   "r_ma.question_id = qa.question_id);";
		
		return results_multipleanswer;
	}
	
	private String insertSingleDimension()
	{
		String results_singledimension = 
			"INSERT INTO results_singledimensionanswer(" +
				"singledimensionanswer_id, surveyexecution_id, question_id, antipodepair_id) " +
			"SELECT sda.id, se.id, qa.question_id, app.id " +
			"FROM questionnaire_singledimensionanswer AS sda " +
			"LEFT JOIN questionnaire_answer AS qa ON (qa.id = sda.multipledimensionsanswer_id) " +
			"LEFT JOIN questionnaire_result as qr ON (qa.result_id = qr.id) " +
			"LEFT JOIN surveyexecution AS se ON(se.id = qr.surveyexecution_id) " +
			"LEFT JOIN antipodepair AS app ON(app.id = sda.antipodepair_id) " +
			"WHERE qr.executionstarted is not null AND " +
			"se.state >= 2 AND " +
			"se.study_id = :studyId AND " +
			"NOT EXISTS (" +
			  	"SELECT r_sda.singledimensionanswer_id, " +
			  		   "r_sda.surveyexecution_id, " +
			  		   "r_sda.question_id," +
			  		   "r_sda.antipodepair_id " +
			  	"FROM results_singledimensionanswer r_sda " +
			  	"WHERE  r_sda.singledimensionanswer_id = sda.id AND " +
			  		   "r_sda.surveyexecution_id = se.id AND " +
			  		   "r_sda.question_id = qa.question_id AND " +
			  		   "r_sda.antipodepair_id = app.id);";
		
		return results_singledimension;
	}
	
	private String insertSingleAnswer()
	{
		String results_singleanswer = 
			"INSERT INTO results_singleanswer(" +
				"singleanswer_id, surveyexecution_id, question_id, answer_id) " +
			"SELECT qa.id, se.id, qa.question_id, qa.answer_id " +
			"FROM questionnaire_answer AS qa " +
			"LEFT JOIN questionnaire_result AS qr ON (qa.result_id = qr.id) " +
			"LEFT JOIN surveyexecution AS se ON (se.id = qr.surveyexecution_id)" +
			"WHERE qa.dtype = 'AnswerToSingleAnswerQuestion' AND " +
				  "qr.executionstarted is not null AND " +
				  "se.state >= 2 AND " +
				  "se.study_id = :studyId AND " +
				  "NOT EXISTS (" +
				  	"SELECT r_sa.singleanswer_id, " +
				  		   "r_sa.surveyexecution_id, " +
				  		   "r_sa.question_id, " +
				  		   "r_sa.answer_id " +
				  	"FROM results_singleanswer r_sa " +
				  	"WHERE  r_sa.singleanswer_id = qa.id AND " +
				  		   "r_sa.surveyexecution_id = se.id AND " +
				  		   "r_sa.question_id = qa.question_id AND " +
				  		   "r_sa.answer_id = qa.answer_id);";
		
		return results_singleanswer;
	}
	
	private String insertSingleRating()
	{
		String results_singlerating = 
			"INSERT INTO results_singleratinganswer(" +
				"singleratinganswer_id, surveyexecution_id, question_id, ratingterm_id) " +
			"SELECT sra.id, se.id, qa.question_id, rt.id " +
			"FROM questionnaire_singleratinganswer AS sra " +
			"LEFT JOIN questionnaire_answer AS qa ON (qa.id = sra.multipleratinganswer_id) " +
			"LEFT JOIN questionnaire_result as qr ON (qa.result_id = qr.id) " +
			"LEFT JOIN surveyexecution AS se ON(se.id = qr.surveyexecution_id) " +
			"LEFT JOIN questionnaire_ratingterm AS rt ON(rt.id = sra.ratingterm_id) " +
			"WHERE qr.executionstarted is not null AND " +
			"se.state >= 2 AND " +
			"se.study_id = :studyId AND " +
			"NOT EXISTS (" +
			  	"SELECT r_sra.singleratinganswer_id, " +
			  		   "r_sra.surveyexecution_id, " +
			  		   "r_sra.question_id," +
			  		   "r_sra.ratingterm_id " +
			  	"FROM results_singleratinganswer r_sra " +
			  	"WHERE  r_sra.singleratinganswer_id = sra.id AND " +
			  		   "r_sra.surveyexecution_id = se.id AND " +
			  		   "r_sra.question_id = qa.question_id AND " +
			  		   "r_sra.ratingterm_id = rt.id);";
		
		return results_singlerating;
	}
}

