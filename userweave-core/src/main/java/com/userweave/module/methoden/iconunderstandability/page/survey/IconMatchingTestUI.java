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
package com.userweave.module.methoden.iconunderstandability.page.survey;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.markup.repeater.util.ModelIteratorAdapter;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.userweave.application.OnFinishCallback;
import com.userweave.application.images.ImageResource;
import com.userweave.dao.StudyDependendDao;
import com.userweave.domain.LocalizedString;
import com.userweave.domain.TestResultEntityBase;
import com.userweave.module.methoden.iconunderstandability.dao.ITMImageDao;
import com.userweave.module.methoden.iconunderstandability.dao.ITMTestResultDao;
import com.userweave.module.methoden.iconunderstandability.dao.IconTermMappingDao;
import com.userweave.module.methoden.iconunderstandability.dao.IconTermMatchingConfigurationDao;
import com.userweave.module.methoden.iconunderstandability.domain.ITMImage;
import com.userweave.module.methoden.iconunderstandability.domain.ITMTestResult;
import com.userweave.module.methoden.iconunderstandability.domain.IconTermMatchingConfigurationEntity;
import com.userweave.module.methoden.iconunderstandability.domain.ItmTerm;
import com.userweave.pages.test.singleSurveyTestUI.SingleFormSurveyUI;
import com.userweave.utils.ListUtils;
import com.userweave.utils.LocalizationUtils;

@SuppressWarnings("serial")
public abstract class IconMatchingTestUI extends SingleFormSurveyUI<IconTermMatchingConfigurationEntity> implements IHeaderContributor {

	@SpringBean
	protected IconTermMappingDao iconTermMappingDao;

	@SpringBean
	protected ITMTestResultDao itmTestResultDao;

	@SpringBean
	protected ITMImageDao itmImageDao;

	private final Map<String, AbstractDefaultAjaxBehavior> id2Behaviour;

	private static final String IMAGE_ID_PARAMETER = "imageId";
	
	private static final String DROP_COMPONENT_ID_PARAMETER = "dropComponentId";
	
	private final Map<String, String> matchingResult = new HashMap<String, String>();
	
	@SpringBean
	private IconTermMatchingConfigurationDao configurationDao;
	
	@Override
	protected StudyDependendDao<IconTermMatchingConfigurationEntity> getConfigurationDao() {
		return configurationDao;
	}
	
	private Integer testResultId;

	private List<ITMImage> images;

	private List<ItmTerm> terms;

	private final ArrayList<ITMImage> imagesV;

	public ArrayList<ITMImage> getImagesV() {
		return imagesV;
	}

	private ArrayList<ItmTerm> termsV;

	private static int ROW_HEIGHT;

	private static int ROW_WIDTH;
	
	private static int TILE_WIDTH;
	
	private static int TILE_HEIGHT;
	
	private static final int ICON_VERT_PADDIUNG = 12;
	
	private static final int ICON_HOR_PADDING = 10;
	
	private static final int TILE_MARGIN = 3;
	
	private static final int ROW_MAX_WIDTH = 510;
	
	protected int getNumberOfTerms() {
		return terms.size();		
	}
	
	public IconMatchingTestUI(
		String id, int surveyExecutionId, 
		final int configurationId, 
		OnFinishCallback onFinishCallback,
		Locale locale) 
	{
		super(id, configurationId, surveyExecutionId, onFinishCallback, locale);
		
		id2Behaviour = new HashMap<String, AbstractDefaultAjaxBehavior>();
			
		images = getConfiguration().getImages();
		imagesV = new ArrayList<ITMImage>();
		
		initImagesV();
		
		computeDimensionsOfIconContainer();
		
		add(
			new MarkupContainer("contentLeft") {				
				{
					add( 
						new AttributeAppender(
							"style", 
							new Model<String>(
								"height:" + Integer.toString(ROW_HEIGHT)+"px;" + 
								"width:" + Integer.toString(ROW_WIDTH) + "px"),
							" ")
					);	
					
					add(createImagesView());
				}
			}
		);
		
		addTermsView();
	}
	
	@Override
	protected void onSubmit() {
		completeStep();
		
		if(finished()) {
			finish();
		}	
	}
	
	protected abstract void initImagesV();
	
	protected abstract void initTermsV();

	private Component createImagesView() {
		
		return
			new RefreshingView("img_content") {

				@Override
				protected Iterator getItemModels() {
					return new ModelIteratorAdapter(imagesV.iterator()) {
	
						@Override
						protected IModel model(Object object) {
							return new CompoundPropertyModel(object);
						}
						
					};
				}
	
				@Override
				protected void populateItem(Item item) {
					final ITMImage image = (ITMImage) item.getModel().getObject();
					
					WebMarkupContainer imageContainer = 
						new WebMarkupContainer("tileCenterPart");
					
					Dimension d = getConfiguration().getDimiensionOfIcons();
					
					imageContainer.add(
						new AttributeAppender(
								"style", 
								new Model<String>(
									"width:" + d.width + "px;" + 
									"height:" + d.height + "px"), 
								";"));
					
					item.add(imageContainer);
					
					imageContainer.add(ImageResource.createImage("image", image));
					// componentId == index
					// componentId wird bei matching an ajax callback �bertragen
					item.add(new AttributeModifier(IMAGE_ID_PARAMETER, new Model<Integer>(item.getIndex())));
				}	
			};
	}

	private void addTermsView() {
		terms = getConfiguration().getTerms();
		termsV = new ArrayList<ItmTerm>();
		
		initTermsV();
		
		RefreshingView rv;
		
		add( 
			rv = new RefreshingView("term_content") {
	
				@Override
				protected Iterator getItemModels() {
					return new ModelIteratorAdapter(termsV.iterator()) {
	
						@Override
						protected IModel model(Object object) {
							return new CompoundPropertyModel(object);
						}						
					};
				}
	
				@Override
				protected void populateItem(Item item) {
					final String termValue = LocalizationUtils.getValue(getTermValue(item));
	
					
					DropComponent dropComponent = new DropComponent("drop_component");
					
					dropComponent.add( 
							new AttributeAppender(
									"style", 
									new Model("width:" + Integer.toString(TILE_WIDTH + 4) + "px;"),
									" ")
							);
					
					
					dropComponent.add(new WebMarkupContainer("dropFieldCenter"){{
						add(new AttributeAppender(
								"style", 
								new Model("height:" + Integer.toString(TILE_HEIGHT - 13 - 6)+"px;"),
								" "));
					}});
					
					item.add(dropComponent);
	
					AbstractDefaultAjaxBehavior dropCallbackBehavior = new AbstractDefaultAjaxBehavior() 
					{
						@Override
						protected void respond(AjaxRequestTarget target) 
						{	
							IRequestParameters parameters = 
								RequestCycle.get().getRequest().getRequestParameters();
							
//							ValueMap parameterMap = 
//								new ValueMap(RequestCycle.get().getRequest().getParameterMap());
							
							Iterator<String> i = parameters.getParameterNames().iterator();
							
							boolean found = false;
							
							while(i.hasNext())
							{
								if(i.next().equals(IMAGE_ID_PARAMETER))
								{
									int imageIndex = 
										parameters.getParameterValue(IMAGE_ID_PARAMETER).toInt();
									
									if (imageIndex != -1) 
									{
										ITMImage image = imagesV.get(imageIndex);
										String imageId = image.getId().toString();
										matchingResult.put(termValue, imageId);
									} 
									else 
									{
										matchingResult.remove(termValue);
									}
									
									found = true;
									break;
								}								
							}
							
							if(!found)
							{
								matchingResult.remove(termValue);
							}
							
							getSurveyStepTimer().setEndTimeNow();													
						}
					};
					
					dropComponent.add(dropCallbackBehavior);
	
					String dropComponentId = createId(item.getIndex());
					dropComponent.add(new AttributeModifier(
						DROP_COMPONENT_ID_PARAMETER, new Model(dropComponentId)));
	
					// um js-callbacks pro behaviour und dispatch anhand componentId zu erzeugen
					id2Behaviour.put(dropComponentId, dropCallbackBehavior);
	
					WebMarkupContainer wmc;
					
					item.add(wmc = new WebMarkupContainer("table_content") {{
						add(new AttributeAppender(
								"style", 
								new Model<String>("height:" + Integer.toString(TILE_HEIGHT - 2 - 6) + "px;"),
								" "));
					}});
					wmc.add(new Label("text", termValue));
					
					item.add(new WebMarkupContainer("drop_end_center") {{
						add(new AttributeAppender(
								"style", 
								new Model<String>("height:" + Integer.toString(TILE_HEIGHT - 8 - 6) + "px;"),
								" "));
					}});
				}

				private LocalizedString getTermValue(Item item) {
					return ((ItmTerm) item.getModel().getObject()).getValue();
				}
			}
		);
	}
	
	
	protected int getNumberOfImages() {
		return images.size();
	}
	
	@Override
	public IconTermMatchingConfigurationEntity getConfiguration() {
		return (IconTermMatchingConfigurationEntity) getDefaultModelObject();
	}

	public void completeStep() {
		
		if (!getSurveyStepTimer().endTimeIsSet()) {
			getSurveyStepTimer().setEndTimeNow();
		}
		
		completeMethodStep(
				findOrCreateTestResult(), 
				matchingResult, 
				getSurveyStepTimer().getExecutionTime());
		
		getSurveyStepTimer().clear();
	}

	protected abstract boolean finished();
	
	public abstract void completeMethodStep(
			ITMTestResult testResult, 
			Map<String, String> matchingResult, 
			long executionTime);

	private ITMTestResult findOrCreateTestResult() {
		ITMTestResult result = null;

		if(testResultId == null) {
			result = itmTestResultDao.findByConfigurationAndSurveyExecution(getConfiguration(), getSurveyExecution());
		} else {
			result = itmTestResultDao.findById(testResultId);
		}
		if(result == null) {
			result = new ITMTestResult();
			if(saveScopeToResult(result)) {
				itmTestResultDao.save(result);
				testResultId = result.getId();
			}
		}
		return result;
	}

	protected void addIconTermMappingToTestResult(long executionTime, ITMTestResult testResult, LocalizedString term, ITMImage itmImage) {
		IconTermMapping iconTermMapping = new IconTermMapping(itmImage, term);
		
		iconTermMapping.setExecutionTime(executionTime);		
		testResult.addToIconTermMappings(iconTermMapping);
		iconTermMapping.setItmTestResult(testResult);
	}
	
	protected void removeIconTermMappingFromTestResult(ITMTestResult testResult, IconTermMapping mapping) {
		testResult.getIconTermMappings().remove(mapping);
		iconTermMappingDao.delete(mapping);
	}
	
	public Map<String, String> getMatchingResult() {
		return matchingResult;
	}

	private String createId(int index) {
		return Integer.toString(index);
	}
	
	protected List<ItmTerm> getTerms() {
		return Collections.unmodifiableList(terms);
	}

	protected List<ITMImage> getImages() {
		return Collections.unmodifiableList(images);
	}

	protected void setTermV(int position) {
		termsV.clear();
		if (terms.size() > position) {
			termsV.add(terms.get(position));
		}
	}

	protected ItmTerm getTermV(int index) {
		if (termsV.size() > index) {
			return termsV.get(index);
		} else {
			return null;
		}
	}

	protected void shuffleImagesV() {
		imagesV.clear();
		imagesV.addAll(ListUtils.mixListOrder(images));
	}
	
	protected void shuffleImages() {
		images = ListUtils.mixListOrder(images);
	}

	protected void setImageV(int position) {
		imagesV.clear();
		
		if (images.size() > position) {
			imagesV.add(images.get(position));
		}
	}

	protected void shuffleTermsV() {
		termsV.clear();
		termsV.addAll(ListUtils.mixListOrder(terms));
	}
	
	protected void shuffleTerms() {
		terms = ListUtils.mixListOrder(terms);
	}
	
	@Override
	public void renderHead(IHeaderResponse response) 
	{
		super.renderHead(response);
		
		response.renderJavaScriptReference(
			new JavaScriptResourceReference(IconMatchingTestUI.class, "iconmatching.js"));
		
		response.renderCSS(".dropped { margin-top:-" + (TILE_HEIGHT - 6) + "px; }", null);	
		
		String jsCallHeader = "function callWicket(dropTargetComponentId, dropSourceComponentId) { switch (dropTargetComponentId) {";
		String jsCallFooter = "}};";

		String jsCall = jsCallHeader;
		for (String dropComponentId: id2Behaviour.keySet()) {
			AbstractDefaultAjaxBehavior behave = id2Behaviour.get(dropComponentId);
			String caseHead = "case \""+ dropComponentId + "\":";
			String jsCallBody = "var wcall = wicketAjaxGet( '" + behave.getCallbackUrl() + "&" + IMAGE_ID_PARAMETER + "=' + dropSourceComponentId, function() {}, function() {} );";
			String caseFooter = "break;";
			jsCall = jsCall + caseHead +  jsCallBody + caseFooter;
		}

		jsCall = jsCall + "default:alert(\"component not found\");break;";
		jsCall = jsCall + jsCallFooter ;
		response.renderJavaScript(jsCall, null);
	}
	
	protected LocalizedString findTermForString(String term) {
		if(term == null) {
			return null;
		}

		for(ItmTerm itmterm : getTerms()) {
			if(LocalizationUtils.getValue(itmterm.getValue()).equals(term)) {
				return itmterm.getValue();
			}
		}
		return null;
	}


	@Override
	protected ITMTestResult createOrLoadResult() {
		return findOrCreateTestResult();		
	}

	@Override
	protected void finishResult(TestResultEntityBase<IconTermMatchingConfigurationEntity> aResult) {
		ITMTestResult result = (ITMTestResult) aResult;
		itmTestResultDao.save(result);
	}
	
	
	/**
	 * Compute number of rows by simple observation:
	 * 		Icons needed to create a new row:
	 * 			Row 1:  1 Icon,
	 * 			Row 2:  3 Icons,
	 * 			Row 3:  7 Icons,
	 * 			Row 4: 13 Icons,
	 * 			Row 5: 21 Icons
	 * 			...
	 * 
	 * 		Observe now, that between each number of Icons
	 * 		the following expression holds: 1 + x * 2.
	 * 		Now what is x? Between 3 and 7, x is 3.
	 * 		Write the multiples of 2 in a row:
	 * 		0, 1, 3, 6, 10, 15, ...
	 * 		What do we observe here? Exactly, the difference
	 * 		between each number is 1 + difference between
	 * 		preceding numbers, or simpler: 1, 2, 3, 4, 5, ...
	 * 		We found something to iterate about.
	 * 		
	 * 		Now, the number of rows is that i (+1) for which the
	 * 		following expression holds:
	 * 
	 *  		accum <= #icons < accum + (i+1) * 2
	 *  	
	 *  	where accum is 1 + the sum of all i * 2 till the above
	 *  	expression holds.
	 * 
	 * @param numberOfIcons
	 * @return
	 */
	private int computeNumberOfNeededRows(int numberOfIcons)
	{	
		int accum = 1;
		
		int i = 0;
		
		boolean expressionHolds = false;
		
		while(!expressionHolds)
		{
			accum += i * 2;
			
			i += 1;
			
			if(accum <= numberOfIcons && numberOfIcons < accum + i * 2)
			{
				expressionHolds = true;
			}
		}
		
		return i;
	}
	
	/**
	 * Compute icon container's width and height as follows:
	 * 		if #numberOfRows > 3 increase width of container
	 * 		till all images are wrapped by three rows.
	 * 		if to much icons are there, add rows, till all
	 *      icons are wrapped
	 */
	private void computeDimensionsOfIconContainer()
	{
		int rowCount = computeNumberOfNeededRows(imagesV.size());
		
		int iconsPerRow = (int) Math.ceil( (double) imagesV.size() / (double) rowCount );
		
		Dimension d = getConfiguration().getDimiensionOfIcons();
		
		TILE_WIDTH  = (imagesV.isEmpty() ? 0 : d.width)  
					  +  2 * ICON_VERT_PADDIUNG;
		
		TILE_HEIGHT = (imagesV.isEmpty() ? 0 : d.height) 
					  + 2 * ICON_HOR_PADDING;
		
		if(rowCount > 3)
		{
			int additionalImagesPerRow = 
				(int) Math.ceil(iconsPerRow / 3.0 );
			
			int iconSize = iconsPerRow + 
			   			   additionalImagesPerRow +
			   			   4 +
			   			   TILE_MARGIN;
			
			int newRowWidth = iconSize * TILE_WIDTH;
			
			if( newRowWidth < ROW_MAX_WIDTH )
			{
				ROW_WIDTH = newRowWidth;
				iconsPerRow = iconsPerRow + additionalImagesPerRow;
			}
			else
			{
				ROW_WIDTH = ROW_MAX_WIDTH;
				
				iconsPerRow = 0;
				while(iconsPerRow * iconSize < ROW_MAX_WIDTH)
					iconsPerRow++;
				
				if(imagesV.size() > 3 * iconsPerRow)
				{
					rowCount = 4;
					
					int restIcons = imagesV.size() - 3 * iconsPerRow;
					
					while(restIcons > iconsPerRow)
					{
						rowCount++;
					}
				}
			}
		}
		else
		{
			// Tile width + margins for graphics + tile clearance
			ROW_WIDTH = (TILE_WIDTH + 4 + TILE_MARGIN) * iconsPerRow;
		}
		
		ROW_HEIGHT = (TILE_HEIGHT + TILE_MARGIN) * rowCount;
		
	}
}
