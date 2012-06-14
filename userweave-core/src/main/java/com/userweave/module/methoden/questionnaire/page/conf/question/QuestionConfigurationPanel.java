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
package com.userweave.module.methoden.questionnaire.page.conf.question;

import java.util.Arrays;
import java.util.Locale;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.ajax.IAjaxUpdater;
import com.userweave.ajax.form.AjaxBehaviorFactory;
import com.userweave.components.authorization.AuthOnlyDropDownChoice;
import com.userweave.components.authorization.AuthOnlyTextArea;
import com.userweave.components.model.LocalizedPropertyModel;
import com.userweave.domain.StudyState;
import com.userweave.domain.service.QuestionService;
import com.userweave.module.methoden.questionnaire.dao.QuestionDao;
import com.userweave.module.methoden.questionnaire.domain.question.FreeQuestion;
import com.userweave.module.methoden.questionnaire.domain.question.Question;
import com.userweave.module.methoden.questionnaire.domain.question.Question.OrderType;
import com.userweave.module.methoden.questionnaire.page.conf.question.feedbackl.CustomFeedbackPanel;
import com.userweave.pages.configuration.DisableComponentIfStudyStateNotInitVisitor;
import com.userweave.pages.configuration.DisableFormComponentVisitor;
import com.userweave.pages.configuration.DisableLinkVisitor;

/**
 * Base class for a question configuration.
 *
 * @important 
 * 		Extending this class means, that the markup 
 * 		for new input fields has to be structured
 * 		as follows:
 *
 * <pre>
 * &lt;tr&gt;
 * 		&lt;td&gt; &lt;wicket:message key="conf_key"/&gt; &lt;/td&gt;
 * 		&lt;td&gt; &lt;input/&gt; &lt;/td&gt;
 * 		&lt;td&gt; &lt;wicket:message key="conf_key_legend"/&gt; &lt;/td&gt;
 * &lt;/tr&gt;
 * </pre>
 * @author oma
 */
public abstract class QuestionConfigurationPanel<T extends Question> 
	extends Panel
	implements IAjaxUpdater
{
	private static final long serialVersionUID = 1L;

	@SpringBean
	private QuestionDao questionDao;
	
	@SpringBean
	private QuestionService questionService;
	
	private final Integer questionId;

	private final Form questionForm;

	private final int configurationId;

	private final String questionType;
	
	private final Locale locale;
	
	protected Locale getStudyLocale() 
	{
		return locale;
	}
	
	/**
	 * Shortcut method to test the study state.
	 * 
	 * @param state
	 * 		State to compare to study state
	 * @return
	 * 		True, if state equals study state.
	 */
	protected boolean studyIsInState(StudyState state) 
	{
		return getQuestion().getConfiguration().getStudy().getState() == state;
	}

	/**
	 * Default constructor.
	 * 
	 * @param id
	 * 		Component markup id.
	 * @param configurationId
	 * 		Id of configuration the question belongs to.
	 * @param theQuestionId
	 * 		Id of question.
	 * @param questionType
	 * 		Type of question.
	 * @param locale
	 * 		Locale to display strings in.
	 */
	public QuestionConfigurationPanel(
		final String id, 
		final int configurationId, 
		Integer theQuestionId, 
		String questionType, 
		final Locale locale) 
	{
		super(id);

		this.configurationId = configurationId;
		
		this.questionType = questionType;
		
		if (theQuestionId == null) 
		{
			Question question = createNewQuestion();
			theQuestionId = question.getId();
		}

		this.questionId = theQuestionId;
				
		final LoadableDetachableModel questionModel = 
			new LoadableDetachableModel() 
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected Object load() 
				{
					return questionDao.findById(questionId);
				}					
		};
		
		setDefaultModel(new CompoundPropertyModel(questionModel));

		this.locale = locale;
		
		final AuthOnlyTextArea textarea = 
			new AuthOnlyTextArea(
					"text", 
					new LocalizedPropertyModel(questionModel, "text", locale),
					AjaxBehaviorFactory.getUpdateBehavior("onblur", QuestionConfigurationPanel.this),
					null, 6, 46)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected boolean isEditAllowed()
			{
				return studyIsInState(StudyState.INIT);
			}
		};
		
		textarea.setRequired(true);
		
		questionForm = new Form("form") 
		{
			private static final long serialVersionUID = 1L;
			{							
				add(textarea);	
			}		
			
			@Override
			protected void onSubmit()
			{
				addQuestionToConfigurationAndSave(configurationId);
			}
			
			@Override
			protected void onBeforeRender() 
			{
				IModel model = new PropertyModel(
					QuestionConfigurationPanel.this.getDefaultModel(), "configuration.study.state");
				
				visitChildren(new DisableLinkVisitor(model));
				
				visitFormComponents(new DisableFormComponentVisitor(model));
				
				super.onBeforeRender();
			}
		};
		
		add(questionForm);
		
		
		AuthOnlyDropDownChoice choices = new AuthOnlyDropDownChoice(
				"orderType",
				new PropertyModel(getDefaultModel(), "orderType"),
				Arrays.asList(OrderType.values()),
				new LocalizedAnswerTypeChoiceRenderer(this)
		);
		
		questionForm.add(choices);
		
		choices.setRequired(true);		
		choices.add(AjaxBehaviorFactory.getUpdateBehavior("onchange", QuestionConfigurationPanel.this));
		choices.setVisible(!(getQuestion() instanceof FreeQuestion));
		
		
		add(new CustomFeedbackPanel("feedbackPanel").setOutputMarkupId(true));

		
		questionForm.add(new Label("questionType", getTypeModel()));
	}
	

	@Override
	protected void onBeforeRender() 
	{
		visitChildren(
			new DisableComponentIfStudyStateNotInitVisitor(
				new PropertyModel(getDefaultModel(), "configuration.study.state"))
		);
		super.onBeforeRender();
	}
	
	/**
	 * Get the form to add new components to this panel.
	 * 
	 * @return
	 * 		The form to configure the question.
	 */
	protected Form getQuestionForm() 
	{
		return questionForm;
	}
	
	protected abstract QuestionDao getQuestionDao();
	
	/**
	 * Convenient method to get the underlying question object.
	 * 
	 * @return
	 * 		The question for this configuration view.
	 */
	@SuppressWarnings("unchecked")
	protected T getQuestion() 
	{
		return (T) getDefaultModelObject();
	}
	
	/**
	 * Shortcut for saving a question.
	 * 
	 * @param configurationId
	 * 		Id of configuration the underlying question
	 * 		belongs to.
	 */
	protected void addQuestionToConfigurationAndSave( final int configurationId) 
	{
		Question question = getQuestion();
	
		questionService.saveQuestion(configurationId, question);
	}
	
	/**
	 * Factory method to create a new question on demand.
	 * 
	 * @return
	 * 		A new question.
	 */
	@SuppressWarnings("unchecked")
	protected T createNewQuestion() 
	{
		 return (T) questionService.createQuestion(configurationId, "", questionType);
	};

	/**
	 * Translator for order type values into loaclized strings.
	 * 
	 * @author opr
	 */
	private class LocalizedAnswerTypeChoiceRenderer implements IChoiceRenderer 
	{
		private static final long serialVersionUID = 1L;
		
		/**
		 * Parent class to retrive loaclized string from.
		 */
		QuestionConfigurationPanel<T> parent;
		
		/**
		 * Default constructor.
		 * 
		 * @param parent
		 * 		Parent class to retrieve loaclized string from.
		 */
		public LocalizedAnswerTypeChoiceRenderer(QuestionConfigurationPanel<T> parent)
		{
			this.parent = parent;
		}
		
		@Override
		public Object getDisplayValue(Object object) 
		{
			StringResourceModel srm = 
				new StringResourceModel(((OrderType) object).name(), parent, null);
			
			return srm.getObject();
		}

		@Override
		public String getIdValue(Object object, int index) 
		{
			return ((OrderType) object).toString();
		}
		
	}
	
	/**
	 * Needed to save From components like textarea and textfields.
	 * 
	 * @usage: component.add(getUpdateBehavior(event))
	 * 
	 * @param event 
	 * 		The javascript event trigger (mostly 'onblur' or 'onchange')
	 * @return
	 * 		Update behavior for ajax form components.
	 */
//	protected AjaxFormComponentUpdatingBehavior getUpdateBehavior(String event)
//	{
//		return new AjaxFormComponentUpdatingBehavior(event) 
//		{	
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			protected void onUpdate(AjaxRequestTarget target) 
//			{
//				target.addComponent(QuestionConfigurationPanel.this.get("feedbackPanel"));				
//			
//				addQuestionToConfigurationAndSave(configurationId);
//			}
//		};
//	}
	
	@Override
	public void onUpdate(AjaxRequestTarget target)
	{
		target.addComponent(QuestionConfigurationPanel.this.get("feedbackPanel"));				
		
		addQuestionToConfigurationAndSave(configurationId);
	}
	
	/**
	 * Creates an ajax form updating behavior for choices.
	 * 
	 * @return
	 * 		Update behavior for ajax form choice components.
	 */
	protected AjaxFormChoiceComponentUpdatingBehavior getChoiceUpdateBehavior()
	{
		return new AjaxFormChoiceComponentUpdatingBehavior()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				target.addComponent(QuestionConfigurationPanel.this.get("feedbackPanel"));				
				
				addQuestionToConfigurationAndSave(configurationId);
			}
			
			@Override
			protected void onBind()
			{
				try
				{
					super.onBind();
				}
				catch (Exception e) 
				{
					// This behavior can only be attached to Choices.
					// If we bind it to another element, the method
					// throws a runtime exception. Since we don't want
					// that, catch the exception here.
				}
			}
		};
	}
	
	protected abstract IModel getTypeModel();
}

