package com.virtusa.newsportal.core.models;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.virtusa.newsportal.core.utils.NewsportalUtils;

@Model(adaptables = {Resource.class,SlingHttpServletRequest.class})
public class RelatedNewsModel {
	
	@ScriptVariable
	ValueMap pageProperties;
	
	@SlingObject
	ResourceResolver resolver;
	
	List<ArticleDetailsModel> relatedArticles;
	
	
	@PostConstruct
	public void init() {
		
		String[] tags = pageProperties.get("cq:tags",String[].class);
		String categoryTag = getCategoryTag(tags);
		if(categoryTag != null) {
			relatedArticles = new ArrayList<>();
			TagManager tagManager = resolver.adaptTo(TagManager.class);
			Tag categoryTagObj = tagManager.resolve(categoryTag);
			relatedArticles = NewsportalUtils.getArticleDetailsList(categoryTagObj.find(), resolver);
		}
	}
	
	public List<ArticleDetailsModel> getRelatedArticles() {
		return relatedArticles;
	}



	public String getCategoryTag(String[] tags) {
		for(String tag: tags) {
			if(tag.startsWith("newsportal:categories")) {
				return tag;
			}
		}
		return null;
	}

}
