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
package com.userweave.pages.configuration.study;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

import com.userweave.application.UserWeaveSession;
import com.userweave.components.IToolTipComponent;
import com.userweave.components.ToolTipAjaxLink;
import com.userweave.components.IToolTipComponent.ToolTipType;
import com.userweave.components.authorization.AuthOnlyDropDownChoice;
import com.userweave.components.authorization.AuthOnlyTextField;
import com.userweave.components.customModalWindow.CustomModalWindow;
import com.userweave.dao.BaseDao;
import com.userweave.dao.StudyDao;
import com.userweave.domain.ImageBase;
import com.userweave.domain.Role;
import com.userweave.domain.Study;
import com.userweave.domain.StudyState;
import com.userweave.domain.User;
import com.userweave.domain.util.StudyPaymentInspector;
import com.userweave.pages.configuration.DisableLinkVisitor;
import com.userweave.pages.configuration.EnabledInStudyState;
import com.userweave.pages.configuration.base.AbstractEntityConfigurationPanel;

/**
 * Panel to configure a study.
 * 
 * @author opr
 */
public class StudyConfigurationPanel 
	extends AbstractEntityConfigurationPanel<Study> 
{	
	private static final long serialVersionUID = 1L;

	@AuthorizeAction(
			action = Action.ENABLE, 
			roles = {Role.PROJECT_ADMIN, 
					 Role.PROJECT_PARTICIPANT})
	@EnabledInStudyState(states={StudyState.INIT})
	private class DisableWebMarkupContainer 
		extends WebMarkupContainer
		implements IToolTipComponent
	{
		private static final long serialVersionUID = 1L;

		private ToolTipType type;
		
		public DisableWebMarkupContainer(String id)
		{
			super(id);
		}
		
		@Override
		protected void onBeforeRender()
		{
			if(! isEnabled())
			{
				add(new AttributeModifier(
					"title", 
					new StringResourceModel(
						type.getStringResourceKey(), 
						new ToolTipAjaxLink("dummy")
						{
							private static final long serialVersionUID = 1L;

							@Override
							public void onClick(AjaxRequestTarget target){}
						},
						null)));
			}
			
			super.onBeforeRender();
		}
		
		@Override
		protected void onComponentTag(ComponentTag tag)
		{
			if(! isEnabled())
			{
				tag.setName("span");
			}
			else
			{
				super.onComponentTag(tag);
			}
		}

		@Override
		public void setToolTipType(ToolTipType toolTipType)
		{
			this.type = toolTipType;
		}

		@Override
		public ToolTipType getToolTipType()
		{
			return type;
		}
	}

	@SpringBean
	private StudyDao studyDao;
	
	@Override
	protected BaseDao<Study> getBaseDao()
	{
		return studyDao;
	}
	
	/**
	 * Feedback panel to display error message from icon upload.
	 */
	private FeedbackPanel feedback;
	
	/**
	 * Default constructor
	 * 
	 * @param id
	 * 		Component id.
	 * @param studyId
	 * 		Id of study to configure.
	 */
	public StudyConfigurationPanel(String id, final int studyId) 
	{
		super(id, studyId);
		
		setOutputMarkupId(true);
	}
	
	/**
	 * Disable all disableable components, if neccessary.
	 */
	@Override
	protected void onBeforeRender()
	{	
		IModel<StudyState> stateModel = 
			new PropertyModel<StudyState>(getDefaultModel(), "state");
	
		visitChildren(new DisableLinkVisitor(stateModel));
		
		visitChildren(new IVisitor<Component, Void>()
		{
			@Override
			public void component(Component object, IVisit<Void> visit)
			{
				if(object instanceof DisableWebMarkupContainer)
				{
					DisableWebMarkupContainer container = (DisableWebMarkupContainer) object;
					
					EnabledInStudyState enabledInStudyState = 
						object.getClass().getAnnotation(EnabledInStudyState.class);
					
					if (enabledInStudyState != null) 
					{
						for (StudyState studyState: enabledInStudyState.states()) 
						{
							if (studyState == getEntity().getState()) 
							{
								return;
							} 
						}
						
						container.setEnabled(false);
						container.setToolTipType(ToolTipType.PHASE);
						
					} 
					else 
					{
						if(getEntity().getState() != StudyState.INIT) 
						{
							container.setEnabled(false);
							container.setToolTipType(ToolTipType.PHASE);
						}
					}
				}
			}
		});
		
		super.onBeforeRender();
	}
	
	@Override
	protected AuthOnlyTextField getColorField()
	{
		return new AuthOnlyTextField(
			"backgroundColor", 
			new PropertyModel<String>(getDefaultModel(), "backgroundColor"),
			getUpdateBehavior("onblur"))
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected boolean isEditAllowed()
			{
				return getEntity().getState() == StudyState.INIT;
			}
		};
	}
	
	@Override
	protected WebMarkupContainer getResetColorLink()
	{
		return new DisableWebMarkupContainer("resetColorLink");
	}
	
	@Override
	protected void initComponent(Form<Void> form)
	{		
		form.add(feedback = new FeedbackPanel("feedback"));
		
		feedback.setOutputMarkupId(true);
		
		AuthOnlyTextField finishedUrl = 
			new AuthOnlyTextField(
					"finishedPageUrl", 
					new PropertyModel<String>(getDefaultModel(), "finishedPageUrl"),
					getSavingBehavior("onblur"));
		
		AuthOnlyTextField notAvailableUrl = 
			new AuthOnlyTextField(
					"notAvailableUrl",
					new PropertyModel<String>(getDefaultModel(), "notAvailableUrl"),
					getSavingBehavior("onblur"));
		
		form.add(finishedUrl);
		form.add(notAvailableUrl); 
		
		
		DisableWebMarkupContainer resetFinishedUrlLink = 
			new DisableWebMarkupContainer("resetFinishedUrlLink");
		
		DisableWebMarkupContainer resetNotAvailableUrlLink = 
			new DisableWebMarkupContainer("resetNotAvailableUrlLink");
		
		resetFinishedUrlLink.add(
				new AttributeModifier(
						"title", new StringResourceModel("resetFinishedUrl", this, null)));
		
		resetNotAvailableUrlLink.add(
				new AttributeModifier(
						"title", new StringResourceModel("resetNotAvailableUrl", this, null)));
		
		if(resetFinishedUrlLink.isEnabled())
		{
			resetFinishedUrlLink.add(
					new AttributeModifier(
							"onclick", new Model<String>("resetUrlForFinished();return false;")));
			
			resetNotAvailableUrlLink.add(
					new AttributeModifier(
							"onclick", new Model<String>("resetUrlForNotAvailable();return false;")));
		}
		
		
		form.add(resetFinishedUrlLink);
		form.add(resetNotAvailableUrlLink);
		
		// TODO: Move to SutdyConfigurationTabPanel
		//createAndAddPaymentModalWin(studyId);
	}
	
	@Override
	protected void onBackgroundColorUpdate(AjaxRequestTarget target)
	{
		Study study = getEntity();
		
		study.setFontColor(
			computeBrightnessOfBackgroundAndSetColorValue(
				study.getBackgroundColor()));
		
		studyDao.save(study);
	}
	
	@Override
	protected boolean userIsAuthorized()
	{
		User user = UserWeaveSession.get().getUser();
		
		return (user.hasRole(Role.PROJECT_PARTICIPANT) ||
		 	   user.hasRole(Role.PROJECT_ADMIN)) && 
		 	  getEntity().getState() == StudyState.INIT;
	}
	
	@Override
	protected byte[] getImageData()
	{
		ImageBase logo = getEntity().getLogo();
		
		if (logo != null)
		{
			return logo.getImageData();
		} 
		else
		{
			return null;
		}
	}
	
	@Override
	protected void deleteLogo()
	{
		Study study = getEntity();
		
		ImageBase studyLogo = study.getLogo();
		
		/*
		 * Toggle between usability methods logo and project logo.
		 */
		if(Arrays.equals(
			studyLogo.getImageData(), 
			study.getParentProject().getLogo().getImageData()))
		{
			try
			{
				String path = "res/user_weaver_logo_kl.png";
				
				InputStream stream = 
					getClass().getResourceAsStream(path);
				
				if(stream != null)
				{
					byte[] data = new byte[10000];
				    stream.read(data);
					
				    studyLogo.setImageData(data);
				}
				else
				{
					studyLogo.setImageData(null);
				}
				
			} 
			catch (FileNotFoundException e)
			{
				studyLogo.setImageData(null);
			} 
			catch (IOException e)
			{
				studyLogo.setImageData(null);
			}
		}
		else
		{
			studyLogo.setImageData(study.getParentProject().getLogo().getImageData());
		}
		
		studyDao.save(study);
	}
	
	@Override
	protected ImageBase getEntityLogo()
	{
		return getEntity().getLogo();
	}
	
	@Override
	protected void setEntityLogo(Study entity, ImageBase image)
	{
		entity.setLogo(image);
		
		studyDao.save(entity);
	}
	
	private AjaxFormComponentUpdatingBehavior getSavingBehavior(String event)
	{
		return new AjaxFormComponentUpdatingBehavior(event) 
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) 
			{
				studyDao.save(getEntity());
			}
		};
	}
	
	@Override
	protected AuthOnlyDropDownChoice getFontColorDropDwn()
	{
		AuthOnlyDropDownChoice dropdown = super.getFontColorDropDwn();
		
		dropdown.setEnabled(
			dropdown.isEnabled() && 
			getEntity().getState() == StudyState.INIT);
		
		return dropdown;
	}
	
	/*
	 * 
	 * 
	 * 
	 * 
	 * 			HERE BE DRAGONS!!!
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	
	// TODO: Move to SutdyConfigurationTabPanel
	private final boolean studyPaymentEnabled = true;
	
	// TODO: Move to SutdyConfigurationTabPanel
	private class DisableDefaultConfirmBehavior extends AbstractBehavior
		implements IHeaderContributor
	{
		private static final long serialVersionUID = 1L;
	
		@Override
		public void renderHead(IHeaderResponse response)
		{
			response.renderOnDomReadyJavaScript("Wicket.Window.unloadConfirmation = false");
		}
	}
	
	/**
	 * Creates a payment dialog which opens as long as
	 * a user hasn't done a donation to this awesome piece
	 * of software.
	 * 
	 * @param studyId
	 * 		Id of study to, which this dialog is attached to.
	 */
	private void createAndAddPaymentModalWin(final int studyId)
	{
		final CustomModalWindow paymentModalWin = 
			new CustomModalWindow("paymentModalWindow");
		
		add(paymentModalWin);
		
		paymentModalWin.setInitialHeight(600);
		paymentModalWin.setInitialWidth(555);
		
		paymentModalWin.setPageCreator(new ModalWindow.PageCreator() 
		{
			private static final long serialVersionUID = 1L;

			public Page createPage() 
            {
                return null;
            }
        });

		paymentModalWin.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() 
		{
			private static final long serialVersionUID = 1L;

            public void onClose(AjaxRequestTarget target) {}
        });

		paymentModalWin.setCloseButtonCallback(new ModalWindow.CloseButtonCallback() 
		{
			private static final long serialVersionUID = 1L;

            public boolean onCloseButtonClicked(AjaxRequestTarget target) 
            {
                return true;
            }
        });
		
		paymentModalWin.add(new DisableDefaultConfirmBehavior());
	

		if ( studyPaymentEnabled && !UserWeaveSession.get().isAdmin() 
				&& !(UserWeaveSession.get()).originFromReport()
				&& (StudyState.studyIsInState(getEntity(), StudyState.FINISHED))
				&& !getEntity().getOwner().isSubscription()) 
		{
			if ( !StudyPaymentInspector.isPaid(getEntity()) ) 
			{
				// open payment windows as popup on panel load
				add(new AbstractDefaultAjaxBehavior() 
				{
					private static final long serialVersionUID = 1L;

					boolean isEnabled = true;
					
					@Override
					protected void respond(AjaxRequestTarget target) 
					{
						isEnabled = false;
						paymentModalWin.show(target);
					}
		
					@Override
					public void renderHead(Component component,
							IHeaderResponse response)
					{
						super.renderHead(component, response);
						
						response.renderOnDomReadyJavaScript(getCallbackScript().toString());
					}
		
					@Override
					public boolean isEnabled(Component component)
					{
						return isEnabled;
					}
				});
			}
		}
	}
}

