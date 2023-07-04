package com.virtusa.newsportal.core.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.jcr.query.Query;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import com.day.cq.wcm.api.Page;
import com.virtusa.newsportal.core.utils.NewsportalUtils;

@Model(adaptables = { Resource.class, SlingHttpServletRequest.class })
public class InThisCategoryModel {

	@ScriptVariable
	ValueMap pageProperties;
	
	@SlingObject
	ResourceResolver resolver;
	
	@ScriptVariable
	Page currentPage;
	
	List<ArticleDetailsModel> categoryArticles;
	
	@PostConstruct
	public void init() {
		
		String[] tags = pageProperties.get("cq:tags", String[].class);
		if(tags != null && tags.length > 0) {
			String categoryTag = getCategoryTag(tags);
			if(categoryTag != null) {
				categoryArticles = new ArrayList();
				String query = "SELECT * FROM [cq:PageContent] AS s WHERE ISDESCENDANTNODE([/content/newsportal]) and s.[cq:tags] like '"+categoryTag+"/%\' order by s.[jcr:content/jcr:created] desc option(limit 5)";
				Iterator<Resource> result = resolver.findResources(query, Query.JCR_SQL2);
				categoryArticles = NewsportalUtils.getArticleDetailsList(result, resolver);
			}
		}
	}
	
	public String getCategoryTag(String[] tags) {
		for(String tag: tags) {
			if(tag.startsWith("newsportal:categories")) {
				String[] tagItems = tag.split("/");
				if(tagItems.length>=2) {
					return tagItems[0]+"/"+tagItems[1];
				}
			}
		}
		return null;
		
	}

	public List<ArticleDetailsModel> getCategoryArticles() {
		return categoryArticles;
	}
	
	
	
}
