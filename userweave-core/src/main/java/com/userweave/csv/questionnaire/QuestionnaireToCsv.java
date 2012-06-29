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

import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.csv.AbstractCsvCell;
import com.userweave.csv.AbstractModuleCsvConverter;
import com.userweave.csv.EmptyCsvCell;
import com.userweave.csv.question.answer.FreeNumberAnswerCsvConverter;
import com.userweave.csv.question.answer.FreeTextAnswerCsvConverter;
import com.userweave.csv.question.answer.MultipleAnswersAnwerCsvConverter;
import com.userweave.csv.question.answer.MultipleDimensionsAnswerCsvConverter;
import com.userweave.csv.question.answer.MultipleRatingAnswerCsvConverter;
import com.userweave.csv.question.answer.SingleAnswerCsvConverter;
import com.userweave.csv.table.CsvColumn;
import com.userweave.csv.table.CsvColumnGroup;
import com.userweave.csv.table.CsvSurveyExecutionColumnGroup;
import com.userweave.csv.table.ICsvColumnGroup;
import com.userweave.csv.table.QuestionnaireCsvColumnGroup;
import com.userweave.csv.table.question.MultipleAnswersCsvColumnGroup;
import com.userweave.csv.table.question.MultipleDimensionsColumnGroup;
import com.userweave.csv.table.question.MultipleRatingColumGroup;
import com.userweave.domain.LocalizedString;
import com.userweave.domain.SurveyExecutionState;
import com.userweave.module.methoden.questionnaire.dao.AnswerDao;
import com.userweave.module.methoden.questionnaire.domain.QuestionnaireConfigurationEntity;
import com.userweave.module.methoden.questionnaire.domain.answer.Answer;
import com.userweave.module.methoden.questionnaire.domain.answer.AnswerToSingleAnswerQuestion;
import com.userweave.module.methoden.questionnaire.domain.answer.FreeNumberAnswer;
import com.userweave.module.methoden.questionnaire.domain.answer.FreeTextAnswer;
import com.userweave.module.methoden.questionnaire.domain.answer.MultipleAnswersAnwer;
import com.userweave.module.methoden.questionnaire.domain.answer.MultipleDimensionsAnswer;
import com.userweave.module.methoden.questionnaire.domain.answer.MultipleRatingAnswer;
import com.userweave.module.methoden.questionnaire.domain.question.DimensionsQuestion;
import com.userweave.module.methoden.questionnaire.domain.question.FreeQuestion;
import com.userweave.module.methoden.questionnaire.domain.question.MultipleAnswersQuestion;
import com.userweave.module.methoden.questionnaire.domain.question.MultipleRatingQuestion;
import com.userweave.module.methoden.questionnaire.domain.question.Question;
import com.userweave.module.methoden.questionnaire.domain.question.SingleAnswerQuestion;

import de.userprompt.utils_userweave.query.model.PropertyCondition;
import de.userprompt.utils_userweave.query.model.QueryObject;
import de.userprompt.utils_userweave.query.template.QueryTemplate;

/**
 * Converter to convert questionnaire results into 
 * columns. Each column group represents a question
 * and each column in a question represents a possible
 * answer, rating, antipode pair, etc.
 * 
 * @author opr
 */
public class QuestionnaireToCsv 
	extends AbstractModuleCsvConverter<QuestionnaireConfigurationEntity>
{
	@SpringBean
	private AnswerDao answerDao;
	
	@Override
	public ICsvColumnGroup convertToCsv(
		QuestionnaireConfigurationEntity entity, 
		CsvSurveyExecutionColumnGroup idGroup,
		Locale locale)
	{
		return convert(entity, idGroup, locale);
	}

	@SuppressWarnings("rawtypes")
	private ICsvColumnGroup convert(
		QuestionnaireConfigurationEntity entity, 
		CsvSurveyExecutionColumnGroup idGroup,
		Locale locale)
	{
		/*
		 * Create a column group for this module,
		 * which contains column groups of questions.
		 */
		QuestionnaireCsvColumnGroup questionnaireGroup = 
			new QuestionnaireCsvColumnGroup(entity.getName());
		
		
		/*
		 * For each question we need a column group. 
		 */
		List<Question> questions = entity.getQuestions();
	
		for(Question question : questions)
		{
			ICsvColumnGroup group = createColGroupForQuestion(question, locale);
			
			if(group == null) { continue; }
			
			
			/*
			 * Iterate over the given answers an order them into rows
			 * in the columns relative to the survey execution id in 
			 * result[0].
			 */
			List<Object[]> answers = getAnswer(question);
			
			/*
			 * Add a column group only once to the questionnire
			 * column group.
			 */
			boolean firstPass = true;
			
			for(Object[] result : answers)
			{
				Answer answer = (Answer)result[1];
				
				answer.getQuestionnaireResult().getExecutionFinished();
				
				/*
				 * Answer is generic. Find specific answer type here.
				 */
				if(answer instanceof AnswerToSingleAnswerQuestion)
				{	
					SingleAnswerCsvConverter.convertToCsvRow(
						//(SingleAnswerCsvColumnGroup) group,
						(CsvColumn)((CsvColumnGroup) group).getColumn(0),
						(AnswerToSingleAnswerQuestion) answer,
						idGroup.findIndexOfId((Integer)result[0]),
						locale, (Date) result[2]);
					
					/*
					 * Add on first pass of this loop the group
					 * to the questionnaire group. Do this here, because
					 * ther MAY be (unlikely) an answer, that is not of
					 * a defined type of answer.
					 */
					
					if(firstPass)
					{
						questionnaireGroup.addColumnGroup(group);
						firstPass = false;
					}
				}
				else if(answer instanceof FreeNumberAnswer)
				{
					FreeNumberAnswerCsvConverter.convertToCsvRow(
						(CsvColumn)((CsvColumnGroup) group).getColumn(0),
						(FreeNumberAnswer) answer,
						idGroup.findIndexOfId((Integer)result[0]),
						(Date) result[2]);
				
					if(firstPass)
					{
						questionnaireGroup.addColumnGroup(group);
						firstPass = false;
					}
				}
				else if(answer instanceof FreeTextAnswer)
				{
					FreeTextAnswerCsvConverter.convertToCsvRow(
							(CsvColumn)((CsvColumnGroup) group).getColumn(0),
							(FreeTextAnswer)answer,
							idGroup.findIndexOfId((Integer)result[0]));
					
					if(firstPass)
					{
						questionnaireGroup.addColumnGroup(group);
						firstPass = false;
					}
					
				}
				else if(answer instanceof MultipleAnswersAnwer)
				{
					MultipleAnswersAnwerCsvConverter.convertToCsvRow(
							(MultipleAnswersCsvColumnGroup) group,
							(MultipleAnswersAnwer) answer,
							idGroup.findIndexOfId((Integer)result[0]),
							locale,
							(Date) result[2]);
				
					if(firstPass)
					{
						questionnaireGroup.addColumnGroup(group);
						firstPass = false;
					}
				}
				else if(answer instanceof MultipleDimensionsAnswer)
				{
					MultipleDimensionsAnswerCsvConverter.convertToCsvRow(
						(MultipleDimensionsColumnGroup) group,
						(MultipleDimensionsAnswer) answer,
						idGroup.findIndexOfId((Integer)result[0]),
						locale,
						(Date) result[2]);
				
					if(firstPass)
					{
						questionnaireGroup.addColumnGroup(group);
						firstPass = false;
					}
				}
				else if(answer instanceof MultipleRatingAnswer)
				{
					int index = idGroup.findIndexOfId((Integer)result[0]);
					
					MultipleRatingAnswerCsvConverter.convertToCsvRow(
						(MultipleRatingColumGroup)group, 
						(MultipleRatingAnswer) answer, 
						index,
						locale,
						(Date) result[2]);
					
					if(firstPass)
					{
						questionnaireGroup.addColumnGroup(group);
						firstPass = false;
					}
				}
			}
		}
		
		return questionnaireGroup;
	}
	
	/**
	 * Retrieve an answer to a specific question for the given
	 * survey execution.
	 * 
	 * @param question
	 * 		Question to retrieve answer for.
	 * @return
	 * 		A list of an array of the survey execution id,
	 * 		the given answer and the finished execution date.
	 * 		The last one ist needed to verify, if a field has
	 * 		to be filled with empty or with 0 cells.
	 */
	@SuppressWarnings("unchecked")
	private List<Object[]> getAnswer(Question question)
	{	
		QueryObject queryObject = answerDao.getQueryObject(question, null);
		
		queryObject.addAndCondition(
				PropertyCondition.greaterOrEqual(
					"se.state", SurveyExecutionState.STARTED));
		
		queryObject.setResult("se.id, a, result.executionFinished");
		
		return new QueryTemplate(queryObject).create(getCurrentSession()).list();
	}
	
	/** 
	 * Factory method to create a column group 
	 * for the given question.
	 * 
	 * @param q
	 * 		Question to create column group for
	 * @param locale
	 * 		Locale to display strings in.
	 */
	private ICsvColumnGroup createColGroupForQuestion(
		final Question q, final Locale locale)
	{
		if(q instanceof MultipleRatingQuestion)
		{
			return new MultipleRatingColumGroup((MultipleRatingQuestion)q, locale);
		}
		
		if(q instanceof DimensionsQuestion)
		{
			return new MultipleDimensionsColumnGroup((DimensionsQuestion) q, locale);
		}
		
		if(q instanceof MultipleAnswersQuestion)
		{
			return new MultipleAnswersCsvColumnGroup((MultipleAnswersQuestion)q, locale);
		}
		
		if(q instanceof FreeQuestion)
		{
//			if(((FreeQuestion)q).getAnswerType() == FreeQuestion.AnswerType.NUMBER)
//			{
				CsvColumnGroup group = 
					new CsvColumnGroup(((FreeQuestion)q).getName().getValue(locale))
				{
					@Override
					public String getHeadlinesAsCsv(String prefix)
					{
						return "\"" + prefix + " | " + getTitle() + "\";";
					}
					
					@Override
					public String getRow(int index)
					{
						// this group consists of only one column,
						// so we need to return the row at the index
						// in this column.
						AbstractCsvCell cell = 
							this.getColumn(0).getCellAtIndex(index);
						
						
						if(cell != null && !(cell instanceof EmptyCsvCell))
						{
							return cell.transformToCsv() + ";";
						}
						else
						{
							return ";";
						}
					}
				};
				
				group.addColumn(new CsvColumn(null));
				
				return group;
//			}
//			else
//			{
//				CsvColumnGroup group = new CsvColumnGroup(((FreeQuestion)q).getName().getValue(locale))
//				{	
//					@Override
//					public String getRow(int index)
//					{
//						return null;
//					}
//					
//					@Override
//					public String getHeadlinesAsCsv(String prefix)
//					{
//						return null;
//					}
//				};
//			}
		}
		
		if(q instanceof SingleAnswerQuestion)
		{
			//return new SingleAnswerCsvColumnGroup((SingleAnswerQuestion)q, locale);
			CsvColumnGroup group = 
				new CsvColumnGroup(q.getName().getValue(locale))
			{
				@Override
				public String getRow(int index)
				{
					// this group consists of only one column,
					// so we need to return the row at the index
					// in this column.
					AbstractCsvCell cell = 
						this.getColumn(0).getCellAtIndex(index);
					
					
					if(cell != null && !(cell instanceof EmptyCsvCell))
					{
						return cell.transformToCsv() + ";";
					}
					else
					{
						return ";";
					}
				}
				
				@Override
				public String getHeadlinesAsCsv(String prefix)
				{
					List<LocalizedString> answers = ((SingleAnswerQuestion)q).getPossibleAnswers();
					
					StringBuilder csvOutput = new StringBuilder();
					
					csvOutput.append("\"" + prefix + " | " + getTitle() + " | ");
					
					boolean isFirst = true;
					
					for(LocalizedString answer : answers)
					{
						Integer pos = answer.getPosition();
						
						if(pos == null)
						{
							pos = -2;
						}
						else
						{
							pos += 1;
						}
						
						if(!isFirst)
						{
							csvOutput.append(" - (" + pos + ") " + answer.getValue(locale));
						}
						else
						{
							isFirst = false;
							csvOutput.append("(" + pos + ") " + answer.getValue(locale));
						}
					}
					
					csvOutput.append("\";");
					
					return csvOutput.toString();
				}
			};
			
			group.addColumn(new CsvColumn(null));
			
			return group;
		}
		
		return null;
	}
}
