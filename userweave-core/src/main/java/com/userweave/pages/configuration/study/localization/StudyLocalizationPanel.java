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
package com.userweave.pages.configuration.study.localization;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.components.IToolTipComponent.ToolTipType;
import com.userweave.components.authorization.AuthOnlyAjaxLink;
import com.userweave.components.authorization.AuthOnlyDropDownChoice;
import com.userweave.components.authorization.IAuthOnly;
import com.userweave.components.callback.EventHandler;
import com.userweave.components.customModalWindow.CustomModalWindow;
import com.userweave.components.model.LocaleModel;
import com.userweave.dao.StudyDao;
import com.userweave.domain.Study;
import com.userweave.domain.StudyState;
import com.userweave.pages.configuration.DisableFormComponentVisitor;
import com.userweave.pages.configuration.DisableLinkVisitor;
import com.userweave.pages.configuration.EnabledInStudyState;
import com.userweave.presentation.model.EntityBaseLoadableDetachableModel;

/**
 * Panel to add and configure locales.
 * 
 * @author oma
 * @author opr
 */
public class StudyLocalizationPanel extends Panel 
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Authorized version of a bookmarkable page link. 
	 * Is only enabled,if a user is authorized, that 
	 * is, if he is admin or participant.
	 * 
	 * @author opr
	 */
	private class AuthOnlyBookmarkablePageLink 
		extends BookmarkablePageLink<Void> 
		implements IAuthOnly
	{
		private static final long serialVersionUID = 1L;

		/**
		 * Flag to indicate if a user can use this link.
		 */
		private boolean isAuthorized = false;
		
		/**
		 * {@inheritDoc}
		 */
		public AuthOnlyBookmarkablePageLink(String id, Class<TranslationPage> pageClass,
				PageParameters parameters)
		{
			super(id, pageClass, parameters);
		}

		@Override
		public void setIsAuthorized(boolean isAuthorized)
		{
			this.isAuthorized = isAuthorized;
		}
		
		@Override
		public boolean isEnabled()
		{
			return super.isEnabled() && 
				   isAuthorized &&
				   getStudy().getState() == StudyState.INIT; // @see #1310
		}
		
		@Override
		protected void onBeforeRender()
		{
			if(! isEnabled())
			{
				add(new AttributeModifier(
					"title", 
					new StringResourceModel(
							ToolTipType.PHASE.getStringResourceKey(), 
							new AuthOnlyAjaxLink("dummy")
							{
								private static final long serialVersionUID = 1L;

								@Override
								public void onClick(AjaxRequestTarget target){}
							}, 
							null)));
			}
			super.onBeforeRender();
		}
		
	}
	
	/**
	 * Auth link which is only visible, when the study is in 
	 * init state.
	 * 
	 * @author opr
	 */
	@EnabledInStudyState(states={StudyState.INIT})
	private abstract class StudyStateDependAjaxLink extends AuthOnlyAjaxLink
	{
		private static final long serialVersionUID = 1L;

		/**
		 * {@inheritDoc}
		 */
		public StudyStateDependAjaxLink(String id)
		{
			super(id);
			
			setToolTipType(ToolTipType.PHASE);
		}
	}

	/**
	 * Auth drop down, which is enabeled, when the 
	 * study is in running or init state.
	 * 
	 * @author opr
	 */
	@EnabledInStudyState(states={StudyState.INIT})
	private class StudyStateDependDropDownChoice extends AuthOnlyDropDownChoice
	{
		private static final long serialVersionUID = 1L;

		public StudyStateDependDropDownChoice(
				String id, 
				IModel<Boolean> model,
				List<Boolean> data, 
				IChoiceRenderer<Boolean> renderer)
		{
			super(id, model, data, renderer);
		}
	}
	
	@SpringBean
	private StudyDao studyDao;
	
	/**
	 * Container for a list of configurable locales.
	 */
	private WebMarkupContainer localesContainer;
	
	/**
	 * Model value for the drop down.
	 */
	private boolean isLocaleSelectable;
	
	private Form<Void> form;
	
	/**
	 * Default constructor.
	 * 
	 * @param id
	 * 		Component markup id
	 * @param studyId
	 * 		Id of underlying study.
	 * @param callback
	 * 		Callback to fire on event.
	 */
	public StudyLocalizationPanel(String id, final int studyId, final EventHandler callback) 
	{			
		super(id, new EntityBaseLoadableDetachableModel<Study>(Study.class, studyId));
		
		// only display the default locale.
		add(new Label(
			"default_locale", 
			((Study) getDefaultModelObject()).getLocale().getDisplayLanguage()));
		
		
		localesContainer = getListView(); 
		
		add(localesContainer);
		
		
		final CustomModalWindow addLocaleModal = getAddLocaleModal();
		
		add(addLocaleModal);
		
		
		add(new StudyStateDependAjaxLink("addLocale")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				addLocaleModal.show(target);
			}
		});
		
		addSelectableLocalePanelDropDown();
	}
	
	@Override
	protected void onBeforeRender() 
	{				
		IModel<StudyState> stateModel = 
			new PropertyModel<StudyState>(getDefaultModel(), "state");
	
		// disable link when study not in init-state
		visitChildren(new DisableLinkVisitor(stateModel));
		
		// disable form components, when study not in init-state
		form.visitFormComponents(new DisableFormComponentVisitor<FormComponent<?>>(stateModel));
		
		super.onBeforeRender();
	}
	
	/**
	 * Factory method to create a new list view, which displays 
	 * all configurable locales.
	 * 
	 * @return
	 * 		A web markup container, wrapping a list view.
	 */
	private WebMarkupContainer getListView()
	{
		IModel<List<Locale>> localeChoiceModel = 
			new LoadableDetachableModel<List<Locale>>() 
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected List<Locale> load() 
			{
				return getStudy().getSupportedLocalesWithoutMainLocale();
			}
						
		};
		
		// each list view item has a label for the locale name,
		// a link to his translation page an a delete button.
		ListView<Locale> lv =  new ListView<Locale>("localeList", localeChoiceModel)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<Locale> item)
			{
				item.add(new Label(
					"locale", new LocaleModel(item.getModel(), getLocale())));
				
				PageParameters parameters = new PageParameters();
				
				parameters.set(1, item.getModelObject().toString());
				parameters.set(0, getStudy().getHashCode());
				
				item.add(new AuthOnlyBookmarkablePageLink("open", TranslationPage.class, parameters));
				
				item.add(createDeleteLink(item));
			}
		};
		
		WebMarkupContainer container = new WebMarkupContainer("localesContainer");
		container.add(lv);
		container.setOutputMarkupId(true);
		
		return container;
	}
	
	/**
	 * @param item
	 * @return
	 */
	private AuthOnlyAjaxLink createDeleteLink(final ListItem<Locale> item)
	{
		return new AuthOnlyAjaxLink("delete", ToolTipType.PHASE)
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			public boolean isEnabled()
			{
				return super.isEnabled() && getStudy().getState() == StudyState.INIT;
			}
			
			@Override
			public void onClick(AjaxRequestTarget target)
			{
				Study study = getStudy();
				
				study.getSupportedLocales().remove(item.getModelObject());
				
				studyDao.save(study);
				
				replaceListView(target);
			}
		};
	}
	
	/**
	 * Convenient method to replace the list of locales.
	 * 
	 * @param target
	 * 		Ajax request target.
	 */
	private void replaceListView(AjaxRequestTarget target)
	{
		WebMarkupContainer replacement = getListView();
		
		localesContainer.replaceWith(replacement);
			
		localesContainer = replacement;
		
		target.add(localesContainer);
	}
	
	/**
	 * Factory method to create the dialog to add a new locale.
	 * 
	 * @return
	 * 		A Custom modal winodow.
	 */
	private CustomModalWindow getAddLocaleModal()
	{
		final CustomModalWindow window = new CustomModalWindow("addLocaleModal");
		
		window.setTitle(new StringResourceModel("addLocale", this, null));
		
		window.setPageCreator(new ModalWindow.PageCreator()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page createPage()
			{
				return new AddLocalePage(window, getDefaultModel())
				{
					private static final long serialVersionUID = 1L;

					@Override
					protected IModel<String> getAcceptLabel()
					{
						return window.getTitle();
					}
					
					@Override
					protected void onAddLocale(AjaxRequestTarget target,
							List<Locale> locales)
					{
						Study study = getStudy();
						study.getSupportedLocales().addAll(locales);
						studyDao.save(study);
					}
				};
			}
		});
		
		window.setWindowClosedCallback(new ModalWindow.WindowClosedCallback()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClose(AjaxRequestTarget target)
			{
				replaceListView(target);
			}
		});
		
		return window;
	}
	
	/**
	 * Creates and add a drop down to decide, if the locale panel
	 * at the beginning of a study is visible or if the browser 
	 * locale should be used.
	 */
	private void addSelectableLocalePanelDropDown()
	{
		form = new Form<Void>("form");
		
		add(form);
		
		isLocaleSelectable = getStudy().isLocaleSelectable();
		
		StudyStateDependDropDownChoice choice = 
			new StudyStateDependDropDownChoice(
				"locale_selectable",
				new PropertyModel<Boolean>(StudyLocalizationPanel.this, "isLocaleSelectable"),
				Arrays.asList(new Boolean[] {true, false}),
				new IChoiceRenderer<Boolean>()
				{
					private static final long serialVersionUID = 1L;

					@Override
					public String getIdValue(Boolean object, int index)
					{
						return object.toString() + index;
					}
					
					@Override
					public Object getDisplayValue(Boolean object)
					{
						if(object)
						{
							return new StringResourceModel(
								"manualSelection", 
								StudyLocalizationPanel.this,
								null).getObject();
						}
						
						return new StringResourceModel(
								"automaticSelection", 
								StudyLocalizationPanel.this,
								null).getObject();
					}
				});
		
		choice.add(new AjaxFormComponentUpdatingBehavior("onchange") 
		{	
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) 
			{
				getStudy().setLocaleSelectable(isLocaleSelectable);
				studyDao.save(getStudy());
			}
		});
		
		form.add(choice);
	}
	
	/**
	 * Shortcut to the study object.
	 * 
	 * @return
	 * 		The underlying study.
	 */
	private Study getStudy() 
	{
		return (Study) getDefaultModelObject();
	}
}

