package com.virtusa.newsportal.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.via.ResourceSuperType;

import com.adobe.cq.wcm.core.components.models.Title;

@Model(
		adaptables = {Resource.class, SlingHttpServletRequest.class},
		resourceType = "newsportal/components/title",
		adapters = Title.class
		)
public class PageTitleImpl implements Title {

	@ValueMapValue
	private String subTitle;
	
	@Self
	@Via(type = ResourceSuperType.class)
	Title title;
	
	public String getText() {
		return title.getText();
	}
	
	public String getType() {
		return title.getType();
	}
	
	public String getSubTitle() {
		return subTitle;
	}

}
