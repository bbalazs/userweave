package com.userweave.pages.user.verification;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.request.resource.PackageResourceReference;

import com.userweave.pages.base.BaseUserWeavePage;



public class TermsOfUsePage extends WebPage {

	public TermsOfUsePage(final ModalWindow window)
	{
		add(new ExternalLink("imprintPrompt", "http://www.user-prompt.com/impressum.html"));
		
		add(new AjaxLink("close")
		{

			@Override
			public void onClick(AjaxRequestTarget target) 
			{
				window.close(target);
			}
			
		});
	}
	
	@Override
	public void renderHead(IHeaderResponse response)
	{
		response.renderCSSReference(new PackageResourceReference(
				BaseUserWeavePage.class, "configBase.css"));
	}
}
