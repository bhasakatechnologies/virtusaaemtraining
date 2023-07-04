package com.virtusa.newsportal.core.utils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import com.virtusa.newsportal.core.models.ArticleDetailsModel;

public class NewsportalUtils {
	
	public static List<ArticleDetailsModel> getArticleDetailsList(Iterator<Resource> list, ResourceResolver resolver){
		List<ArticleDetailsModel> articleList = new ArrayList<>();
		while (list.hasNext()) {
			Resource resource = list.next();			
			Resource articleResource = resolver.getResource(resource.getPath()+"/root/container/article_grid/left-container/article_details");
			if(articleResource != null) {
				ArticleDetailsModel articleDetails = articleResource.adaptTo(ArticleDetailsModel.class);
				if(articleDetails != null) {
					String pagePath = resource.getPath();					
					articleDetails.setPagePath(pagePath.split("/jcr:content")[0]);
					articleList.add(articleDetails);
				}
			}
		}
		return articleList;
	}

}
