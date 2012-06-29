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
package com.userweave.module.methoden.rrt.page.survey;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.application.OnFinishCallback;
import com.userweave.components.model.LocalizedPropertyModel;
import com.userweave.dao.StudyDependendDao;
import com.userweave.domain.TestResultEntityBase;
import com.userweave.module.methoden.rrt.dao.RrtConfigurationDao;
import com.userweave.module.methoden.rrt.dao.RrtResultDao;
import com.userweave.module.methoden.rrt.domain.OrderedTerm;
import com.userweave.module.methoden.rrt.domain.RrtConfigurationEntity;
import com.userweave.module.methoden.rrt.domain.RrtResult;
import com.userweave.module.methoden.rrt.domain.RrtTerm;
import com.userweave.pages.test.NextButton;
import com.userweave.pages.test.SurveyUI;
import com.userweave.utils.ListUtils;
import com.userweave.utils.LocalizationUtils;
import com.userweave.utils.StringUtils;

@SuppressWarnings("serial")
public class RrtSurveyUI extends SurveyUI<RrtConfigurationEntity> {
	
	@SpringBean
	private RrtResultDao rrtResultDao;

	private String rrtChanged = null;
	
	private final Map<Integer, String> resultMapPosition2TermId = new HashMap<Integer, String>();

	String tmp = "INIT_VALUE";

	String testval = "testVAL";

	public RrtSurveyUI(
		String id, int surveyExecutionId, 
		final int configurationId, 
		OnFinishCallback onFinishCallback,
		Locale locale) 
	{
		super(id, configurationId, surveyExecutionId, onFinishCallback, locale);

		add(new Label("prefix", new LocalizedPropertyModel(getDefaultModel(), "prefix", getLocale())));
		add(new Label("postfix", new LocalizedPropertyModel(getDefaultModel(), "postfix", getLocale())));

		IModel termsModel = new LoadableDetachableModel() {

			private final List<Integer> order = new ArrayList<Integer>();

			@Override
			protected Object load() {
				RrtConfigurationEntity configuration = getConfiguration();
				if (configuration != null) {
					return ListUtils.mixListOrder(configuration.getTerms(), order);
				} else {
					return Collections.emptyList();
				}
			}
		};

		addCounterList(termsModel);
		addDescriptionList(termsModel);
		addTermsWithTermId(termsModel);
		addSubmitForm(termsModel);
	}

	private void addDescriptionList(IModel termsModel) {
		final List<RrtTerm> terms = (List<RrtTerm>) termsModel.getObject();

		// this is a hack!!
		add(
			new ListView("scale", Collections.singletonList(terms.size()+"")) {
	
				@Override
				protected void populateItem(ListItem item) {
					String height= (String) item.getModelObject();
					item.add(new StyleHeightAttributeModifier(true, new Model(height), "height"));
				}
			}
		);
		
		// this is a hack, as above!!
		// Beschreibung Start
		add(
			new ListView("desc_box",Collections.singletonList(terms.size()+"")) {
	
				@Override
				protected void populateItem(ListItem item) {
					String height= (String) item.getModelObject();
					item.add(new StyleHeightAttributeModifier(true, new Model(height), "min-height"));
					RepeatingView termsRepeatingView = new RepeatingView("termValueAndDescription");
					int count=0;
					for (RrtTerm term : terms) {
	
						MarkupContainer markupContainer = new WebMarkupContainer(Integer.toString(count));
						termsRepeatingView.add(markupContainer);
						markupContainer.add(new AttributeModifier("id", true, new Model("destermid_" + term.getId().toString())));
						markupContainer.setEscapeModelStrings(false);
	
	
						markupContainer.add(new Label("value", new LocalizedPropertyModel(term, "value", RrtSurveyUI.this.getLocale())));
						markupContainer.add(new Label("description", new LocalizedPropertyModel(term, "description", RrtSurveyUI.this.getLocale())));
	
						count++;
					}
					item.add(termsRepeatingView);
				}
			}
		);
	}

	private void addCounterList(IModel termsModel) {
		add( new ListView("counterList", termsModel) {
				@Override
				protected void populateItem(ListItem item) {
					WebMarkupContainer counter = new WebMarkupContainer("counter");
					item.add(counter);

					counter.add(new Label("value", Integer.toString(item.getIndex()+1) ));
				}
		});
	}

	private void addSubmitForm(final IModel termsModel) {
		Form form = new Form("return") {
			{
				add(new NextButton("nextButton"){
					@Override
					protected String getImageColor()
					{
						return getColorNextButton();
					}
				});
				
				// add hidden field to hold return values
				RepeatingView positionRepeatingView = new RepeatingView("position");
				int positionenCount=0;

				for (RrtTerm term : (List<RrtTerm>) termsModel.getObject()) {

					final Integer thisCount = positionenCount;

					String countString = Integer.toString(positionenCount);

					HiddenField resultField =
						new HiddenField(countString,

								// connect hidden-fields to result-map
								new Model<String>() {

									@Override
									public void setObject(String object) {
										setResult(thisCount, object);
										
									}

								});

					positionRepeatingView.add(resultField);

					positionenCount++;
				}
				add(positionRepeatingView);
				
				add(new HiddenField("rrtChanged",

							// connect hidden-fields to result-map
							new Model<String>() {

								@Override
								public void setObject(String object) {
									setRrtChanged(object);
								}

							}));
			}

			@Override
			public void onSubmit() {
				finish();
			}
		};

		add(form);
	}

	private void setRrtChanged(String string) {
		rrtChanged = string;
	}

	private void setResult(Integer thisCount, String object) {
		resultMapPosition2TermId.put(thisCount, object);
	}

	
	private void addTermsWithTermId(IModel termsModel) {
		RepeatingView termsRepeatingView = new RepeatingView("term");
		int count=0;
		for (RrtTerm term : (List<RrtTerm>) termsModel.getObject()) {

			MarkupContainer markupContainer = new WebMarkupContainer(Integer.toString(count));
			termsRepeatingView.add(markupContainer);
			markupContainer.add(new AttributeModifier("id", new Model("termid_" + term.getId().toString())));
			markupContainer.setEscapeModelStrings(false);

			Label value = new Label("value", new Model( StringUtils.shorten(LocalizationUtils.getValue(term.getValue(), getLocale()), 25)));
			markupContainer.add(value);

			count++;
		}
		add(termsRepeatingView);
	}

	private static class StyleHeightAttributeModifier extends AttributeModifier {

		private final String styleAttrName;
		
		public StyleHeightAttributeModifier(boolean addAttributeIfNotPresent, Model model, String usedHeight) {
			super("style",addAttributeIfNotPresent,model);
			styleAttrName = usedHeight;
		}

		@Override
		protected String newValue(final String currentValue, final String replacementValue) {
			Integer curReplacementValInt = Integer.decode(replacementValue);
			if(currentValue == null) {
				String newVal = newValue(null,curReplacementValInt);
				return newVal == null ? null : styleAttrName+": "+newVal+"px;";
			}

			Pattern p = Pattern.compile("[0-9][0-9]*");
			Matcher m = p.matcher(currentValue);
			if(!m.find()) {
				String newVal = newValue(null,curReplacementValInt);
				return newVal == null ? null : styleAttrName+": "+newVal+"px;";
			}
			Integer curValInt = Integer.decode(m.group());
			String newVal = newValue(curValInt,curReplacementValInt);
			return newVal == null ? null : styleAttrName+": "+newVal+"px;";
		}

		protected String newValue(Integer currentValue, Integer replacementValue) {
				if(currentValue == null) {
					currentValue=42;
				}
				return replacementValue == null ? null : ""+(currentValue*replacementValue);
		}
	}

	@SpringBean
	private RrtConfigurationDao configurationDao;

	@Override
	protected StudyDependendDao<RrtConfigurationEntity> getConfigurationDao() {
		return configurationDao;
	}

	@Override
	protected RrtResult createOrLoadResult() {
		RrtResult result = rrtResultDao.findByConfigurationAndSurveyExecution(getConfiguration(), getSurveyExecution());
		if(result == null) {
			result = new RrtResult();
		}
		if(saveScopeToResult(result)) {
			rrtResultDao.save(result);
		}
		
		return result;
	}

	@Override
	protected void finishResult(TestResultEntityBase<RrtConfigurationEntity> aResult) {
		RrtResult result = (RrtResult) aResult;

		if(rrtChanged != null && rrtChanged.equals("true")) {
			for (Integer position: resultMapPosition2TermId.keySet()) {

				String termId = resultMapPosition2TermId.get(position);

				// decode format termid[]=666
				String id = termId.substring(termId.indexOf("=")+1);
			
				RrtTerm term = getConfiguration().getTermById(id);

				OrderedTerm orderedTerm = result.getOrderedTerm(term);
				if(orderedTerm == null) {
					orderedTerm = new OrderedTerm();
					orderedTerm.setTerm(term);
				} else {
					// remove if already in list of terms to track position
					result.getOrderedTerms().remove(orderedTerm);
				}
				orderedTerm.setPosition(position);
			
				result.addToOrderedTerms(orderedTerm);
			}
		}
		
		rrtResultDao.save(result);
	}

	@Override
	public void renderHead(IHeaderResponse response)
	{
		super.renderHead(response);
		
		response.renderJavaScriptReference(new JavaScriptResourceReference(RrtSurveyUI.class, "rrt.js"));
	}
	
}