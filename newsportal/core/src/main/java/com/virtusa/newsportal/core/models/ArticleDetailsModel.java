package com.virtusa.newsportal.core.models;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.day.cq.wcm.api.Page;

@Model(
		adaptables = { Resource.class ,SlingHttpServletRequest.class}, 
		defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ArticleDetailsModel {
	
	@ValueMapValue
	@Default(values = "Artice 1 Title")
	private String articleTitle;
	
	@ValueMapValue
	private String articleDesc;
	
	@ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
	private String fileReference;
	
	@ValueMapValue
	//@Optional
	private Date articleExpiry;
	
	@ValueMapValue(name = "sling:resourceType", injectionStrategy = InjectionStrategy.REQUIRED)
	//@Required
	//@Named("sling:resourceType")
	private String slingResourceType;
	
	private boolean articleIsExpired = false;
	
	@ScriptVariable
	Page currentPage;
	
	private String pagePath;
	
	
	@PostConstruct
	public void init() {
		if(articleExpiry != null) {
			Date today = new Date();
			if(articleExpiry.compareTo(today)<0) {
				articleIsExpired = true;
			}
		}		
	}

	public String getArticleTitle() {
		return articleTitle;
	}

	public String getArticleDesc() {
		return articleDesc;
	}	

	public String getFileReference() {
		return fileReference;
	}

	public Date getArticleExpiry() {
		return articleExpiry;
	}

	public String getSlingResourceType() {
		return slingResourceType;
	}

	public boolean isArticleIsExpired() {
		return articleIsExpired;
	}	
	
	public String getPageTitle() {
		String title = currentPage.getPageTitle();
		if(title == null) {
			title = currentPage.getTitle();
		}
		return title;
	}

	public String getPagePath() {
		return pagePath;
	}

	public void setPagePath(String pagePath) {
		this.pagePath = pagePath;
	}	

}
