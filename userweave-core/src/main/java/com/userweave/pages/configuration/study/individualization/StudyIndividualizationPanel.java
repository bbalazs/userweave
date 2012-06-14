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
package com.userweave.pages.configuration.study.individualization;


import java.awt.Image;

import javax.swing.ImageIcon;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.model.StringResourceModel;

import com.userweave.ajax.form.AjaxBehaviorFactory;
import com.userweave.components.authorization.AuthOnlyTextArea;
import com.userweave.components.authorization.AuthOnlyTextField;
import com.userweave.components.imageUpload.IconUploadPanel;
import com.userweave.components.model.LocalizedPropertyModel;
import com.userweave.domain.ImageBase;
import com.userweave.domain.Study;
import com.userweave.domain.StudyState;
import com.userweave.module.methoden.iconunderstandability.page.conf.images.ImageUploadCallback;
import com.userweave.module.methoden.questionnaire.page.conf.question.feedbackl.CustomFeedbackPanel;
import com.userweave.pages.components.servicePanel.ServicePanel;
import com.userweave.pages.components.servicePanel.ServicePanel.ServicePanelType;
import com.userweave.pages.configuration.study.StudyConfigurationFormPanelBase;

/**
 * @author oma
 */
@SuppressWarnings("serial")
@Deprecated
public class StudyIndividualizationPanel extends StudyConfigurationFormPanelBase {

	private final int MAX_WIDTH = 170;
	private final int MAX_HEIGHT = 40;
	private CustomFeedbackPanel feedback;
	private final StringResourceModel logoCutAcrossMaxSizeMessage;
	
	public StudyIndividualizationPanel(String id, int studyId) {
		super(id, studyId);
		
		StudyState state = getStudy().getState();
		
		if(state == StudyState.INIT)
			add(new ServicePanel("servicePanel", ServicePanelType.SAVE_TIME));
		else if(state == StudyState.RUNNING)
			add(new ServicePanel("servicePanel", ServicePanelType.TEST_MEMBER));
		else
			add(new ServicePanel("servicePanel", ServicePanelType.NOT_SURE_INTERPRETATION));
			
		AuthOnlyTextField headline = 
			new AuthOnlyTextField(
					"headline", 
					new LocalizedPropertyModel(getStudyModel(), "headline", getStudyLocale()),
					AjaxBehaviorFactory.getUpdateBehavior("onblur", StudyIndividualizationPanel.this));
		
		AuthOnlyTextArea descr = 
			new AuthOnlyTextArea(
					"description",
					new LocalizedPropertyModel(getStudyModel(), "description", getStudyLocale()),
					AjaxBehaviorFactory.getUpdateBehavior("onblur", StudyIndividualizationPanel.this),
					null,
					2,
					40);
		
		
		
		
		
		
		
		addFormComponent(headline);
		addFormComponent(descr);
		
		
		addFormComponent( new Label(
				"individualLogoLegend", 
				new StringResourceModel("individual_logo_legend", this, null, new Object[] {MAX_WIDTH, MAX_HEIGHT}) ));
		
		logoCutAcrossMaxSizeMessage = new StringResourceModel("individual_logo_cut_across_max_size", this, null, new Object[] { MAX_WIDTH, MAX_HEIGHT});
		
		addFormComponent(
			new IconUploadPanel("logoUpload", 
				new ImageUploadCallback() {

					public byte[] getImageData() {
						ImageBase logo = getStudy().getLogo();
						if (logo != null) { 
							return logo.getImageData();
						} else {
							return null;
						}
					}

					public void remove() {
						Study study = getStudy();
						study.setLogo(null);
						save(study);						
					}

					public void saveFileUpload(FileUpload fileUpload) {
						Study study = getStudy();
						ImageBase logo = study.getLogo();
						if (logo == null) {
							logo = new ImageBase();
							study.setLogo(logo);
						}
						
						
						Image inImage = new ImageIcon(fileUpload.getBytes()).getImage();
						
				        if (inImage.getWidth(null) < 1 || inImage.getWidth(null) > MAX_WIDTH 
				        		|| inImage.getHeight(null) < 1 || inImage.getHeight(null) > MAX_HEIGHT)
				        {
				    		warn( logoCutAcrossMaxSizeMessage.getString() );
				        }
				        else 
				        {
							logo.setImageData(fileUpload.getBytes());
							logo.incrementVersion();
							save();	
				        }
					}			
				}
			)
		);
		
		add(feedback = new CustomFeedbackPanel("feedback"));
		feedback.setOutputMarkupId(true);

	}
}

