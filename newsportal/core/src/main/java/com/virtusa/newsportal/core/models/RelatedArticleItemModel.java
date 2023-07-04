package com.virtusa.newsportal.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = { Resource.class })
public class RelatedArticleItemModel {
	
	@ValueMapValue
	private String articleTitle;
	
	@ValueMapValue
	private String articlePath;

	public String getArticleTitle() {
		return articleTitle;
	}

	public String getArticlePath() {
		return articlePath;
	}
}
