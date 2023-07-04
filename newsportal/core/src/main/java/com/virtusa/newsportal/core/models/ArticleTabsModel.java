package com.virtusa.newsportal.core.models;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.virtusa.newsportal.core.services.ArticleService;
import com.virtusa.newsportal.core.utils.NewsportalUtils;

@Model(
	adaptables = {Resource.class,SlingHttpServletRequest.class},
	defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ArticleTabsModel {
	
	@ValueMapValue
	@Default(values = "newsportal:featured")
	private String featuredTagStr;
	
	@ValueMapValue
	@Default(values = "newsportal:popular")
	private String popularTagStr;
	
	@OSGiService
	ArticleService articleService;
	
	@SlingObject
	ResourceResolver resolver;
	
	List<ArticleDetailsModel> featuredArticleList;
	List<ArticleDetailsModel> popularArticleList;
	List<ArticleDetailsModel> recentArticleList;
	
	
	@PostConstruct
	public void init() {
		
		TagManager tagManager = resolver.adaptTo(TagManager.class);
		Tag featuredTag = tagManager.resolve(featuredTagStr);
		Tag popularTag = tagManager.resolve(popularTagStr);
		
		featuredArticleList = NewsportalUtils.getArticleDetailsList(featuredTag.find(), resolver);
		popularArticleList = NewsportalUtils.getArticleDetailsList(popularTag.find(), resolver);
		recentArticleList = articleService.recentArticles();
		
	}


	public List<ArticleDetailsModel> getFeaturedArticleList() {
		return featuredArticleList;
	}


	public List<ArticleDetailsModel> getPopularArticleList() {
		return popularArticleList;
	}


	public List<ArticleDetailsModel> getRecentArticleList() {
		return recentArticleList;
	}

	
}
