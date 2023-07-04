package com.virtusa.newsportal.core.services.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.jcr.query.Query;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.Page;
import com.virtusa.newsportal.core.models.ArticleDetailsModel;
import com.virtusa.newsportal.core.services.ArticleService;
import com.virtusa.newsportal.core.utils.NewsportalUtils;

@Component(
		service = ArticleService.class, 
		configurationPolicy = ConfigurationPolicy.REQUIRE,
		immediate = true)
@Designate(ocd = ArticleConfiguration.class)
public class ArticleServiceImpl implements ArticleService {

	private static final Logger LOG = LoggerFactory.getLogger(ArticleServiceImpl.class);
	
	@Reference
	NPUtilService npUtilService;
	
	private String clientId;
	
	private String articleRestApi;
	
	@Activate
	public void activate(ArticleConfiguration config) {
		LOG.info("Inside ArticleServiceImpl - Activate Method");
		this.clientId = config.clientId();
		this.articleRestApi = config.articleRestAPI();
	}
	
	@Modified
	public void update(ArticleConfiguration config) {
		LOG.info("Inside ArticleServiceImpl - Modified Method");
		this.clientId = config.clientId();
		this.articleRestApi = config.articleRestAPI();
		LOG.info("Rest URL - {}, Client Id - {}, Status - {}",articleRestApi,clientId,config.status());
		//getArticles();
	}
	
	@Override
	public String getArticles() {		
        HttpGet request = new HttpGet(articleRestApi);
        String result = null;
        try(CloseableHttpClient httpClient = HttpClients.createDefault()) {
			CloseableHttpResponse response = httpClient.execute(request);
			HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity);
            }
            response.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
        LOG.info("Response - {}",result);
		return result;
	}

	@Override
	public List<ArticleDetailsModel> recentArticles() {
		
		List<ArticleDetailsModel> articleList = new ArrayList<>();		
		String query = "SELECT * FROM [cq:PageContent] AS s WHERE ISDESCENDANTNODE([/content/newsportal]) AND s.[cq:template]=\"/conf/newsportal/settings/wcm/templates/article-template\" order by s.[jcr:content/jcr:created] desc option(limit 5)";
		try(ResourceResolver resolver = npUtilService.getResourceResolver()){			
		   Iterator<Resource> results = resolver.findResources(query, Query.JCR_SQL2);
		   articleList = NewsportalUtils.getArticleDetailsList(results, resolver);		   
		}
		return articleList;
	}
}
