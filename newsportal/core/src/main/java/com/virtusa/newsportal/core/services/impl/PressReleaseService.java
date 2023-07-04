package com.virtusa.newsportal.core.services.impl;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.virtusa.newsportal.core.services.ArticleService;

@Component(
		service = PressReleaseService.class,  
		immediate = true)
public class PressReleaseService {
	
	private static final Logger LOG = LoggerFactory.getLogger(PressReleaseService.class);
	
	@Reference
	ArticleService articleService;
	
	@Activate
	public void activate() {
		String articles = articleService.getArticles();
		LOG.info("Inside PreeReleaseService - Activate method");
		LOG.info("Response - getArticles - {}",articles);
	}
	
	@Deactivate
	public void deactivate() {
		LOG.info("Inside PreeReleaseService - Deactivate method");
	}
	
	@Modified
	public void update() {
		LOG.info("Inside PreeReleaseService - Update method");
	}
	
}
