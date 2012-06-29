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
package com.userweave.components.configuration;

import java.util.Locale;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;

import com.userweave.ajax.IAjaxUpdater;
import com.userweave.application.UserWeaveSession;
import com.userweave.dao.BaseDao;
import com.userweave.domain.EntityBase;
import com.userweave.domain.Study;
import com.userweave.domain.StudyState;

/**
 * Base class for configuration of methods/modules.
 * This component gives a form to configure the
 * underlying entity.
 * 
 * Important: Use addFormComponent to add new
 * component to this panel.
 * 
 * @author opr
 */
public abstract class ConfigurationBaseUI<T extends EntityBase> 
	extends Panel
	implements IAjaxUpdater
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Id of entity, which backs this configuration.
	 */
	private final int entityId;
	
	/**
	 * Getter for the entity id.
	 */
	protected int getEntityId() 
	{
		return entityId;
	}
	
	/**
	 * Get the entity from the model.
	 * 
	 * @return
	 * 		entity which backs this panel.
	 */
	@SuppressWarnings("unchecked")
	protected T getEntity() 
	{
		return (T) getDefaultModelObject();
	}
	
	/**
	 * Form to configure entity.
	 */
	private Form<Void> form;
	
	/**
	 * Default constructor.
	 * 
	 * @param id
	 * 		Component id
	 * @param entityId
	 * 		Id of entity to load
	 * @param callback
	 * 		Callback to fire for triggered events.
	 */
	public ConfigurationBaseUI(String id, final int entityId) 
	{
		super(id);

		this.entityId = entityId;

		setDefaultModel(
			new CompoundPropertyModel<T>( 
				new LoadableDetachableModel<T>() 
				{
					private static final long serialVersionUID = 1L;

					@Override
					protected T load() 
					{
						return getBaseDao().findById(entityId);
					}
				}
			)
		);
		
		add(form = new Form<Void>("form"));
	}
	
	/**
	 * Adds a component to this form.
	 * 
	 * @param formComponent
	 * 		Component to add to this form.
	 */
	protected void addFormComponent(Component formComponent)
	{
		form.add(formComponent);
	}
	
	/**
	 * BaseDao to load entity from.
	 * 
	 * @return
	 * 		The BaseDao.
	 */
	protected abstract BaseDao<T> getBaseDao();
	
	/**
	 * Get the study this configuration depends on.
	 * 
	 * @return
	 * 		Study.
	 */
	protected abstract Study getStudy();

	/**
	 * this function provides either the study locale OR - 
	 * if coming from ReportLink - the locale of the user.
	 * 
	 * @return
	 */
	public Locale getStudyLocale()
	{
		if(showOnConfigurationButNotOnReport()) 
		{
			return getStudy().getLocale();
		} 
		else 
		{
			return getLocale();
		}
	}
	
	public final boolean showOnConfigurationButNotOnReport() 
	{
		return ! UserWeaveSession.get().originFromReport();
	}
	
	/**
	 * Check, if the underlying study is in one of
	 * the given states.
	 * 
	 * @param studyStates
	 * 		List of study states.
	 * @return
	 * 		true, if study is in one of the given states.
	 */
	protected boolean studyIsInState(StudyState ... studyStates) 
	{
		return StudyState.studyIsInState(getStudy().getState(), studyStates);
	}
}

