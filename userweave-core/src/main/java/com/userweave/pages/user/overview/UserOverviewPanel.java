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
package com.userweave.pages.user.overview;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.application.auth.AdminOnly;
import com.userweave.dao.UserDao;
import com.userweave.domain.User;

@AdminOnly
@SuppressWarnings("serial")
public abstract class UserOverviewPanel extends Panel {

	@SpringBean
	private UserDao userDao;

	private boolean isOdd = false;
	
	public UserOverviewPanel(String id) {
		super(id);
		initUI();
	}

	private void initUI() {
		LoadableDetachableModel userListModel = new LoadableDetachableModel() {

			@Override
			protected Object load() {
				/*
				 * GroupingComparator comparator = GroupingComparator .build(new
				 * Comparator[] { new StudyStateComparator(), new
				 * EntityBaseComparator() }); Set<Study> sorted = new TreeSet<Study>(comparator);
				 * User user = UserWeaveSession.get().getUser();
				 * if(user.isAdmin()) { sorted.addAll(studyDao.findAll()); }
				 * else { sorted.addAll(studyDao.findByOwner(user)); } return
				 * new ArrayList<Study>(sorted);
				 */
				return userDao.findAllByEmail();
			}
		};

		add(new AjaxLink("create") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				onEditUser(target, null);
			}
		});
		
		add(new ListView("listview", userListModel) {

			@Override
			protected IModel getListItemModel(IModel model, int index) {
				IModel listItemModel = super.getListItemModel(model, index);
				final int userId = ((User) listItemModel.getObject()).getId();

				return new CompoundPropertyModel(new LoadableDetachableModel() {

					@Override
					protected Object load() {
						return userDao.findById(userId);
					}
				});
			}

			@Override
			protected void populateItem(final ListItem item) {
				final int userId = ((User) item.getModelObject()).getId();
				
				item.add(new AjaxLink("edit") {

					@Override
					public void onClick(AjaxRequestTarget target) {
						onEditUser(target, userId);
					}
				}.add(new Label("email")));
				
				item.add(new AjaxCheckBox("subscription") {

					@Override
					protected void onUpdate(AjaxRequestTarget target) {
						User cur = ((User) item.getModelObject());
						userDao.save(cur);
					}
					
					@Override
					public boolean isVisible() {
						return !((User) item.getModelObject()).isAdmin();
					}
				});
				
				item.add(new Label("subscription_label", new StringResourceModel("subscription_label", this, null)) {
					@Override
					public boolean isVisible() {
						return !((User) item.getModelObject()).isAdmin();
					}
				});
				
				if(item.getIndex() == 0) {
					item.add(new SimpleAttributeModifier("class", "even_first"));
				}	
				else if(isOdd)
					item.add(new SimpleAttributeModifier("class", "odd"));
				else
					item.add(new SimpleAttributeModifier("class", "even"));
				
				isOdd = !isOdd;
				
				/*
				 * Link deleteLink = new DeleteLink("delete", item);
				 * 
				 * deleteLink.add(new SimpleAttributeModifier("onclick", "return
				 * confirm('Are you sure?');"));
				 * 
				 * item.add(deleteLink);
				 */
			}
		});
	}
	
	public abstract void onEditUser(AjaxRequestTarget target, Integer userId);

}
