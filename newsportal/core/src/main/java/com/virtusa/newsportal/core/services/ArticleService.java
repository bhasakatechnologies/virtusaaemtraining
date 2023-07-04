package com.virtusa.newsportal.core.services;

import java.util.List;

import com.virtusa.newsportal.core.models.ArticleDetailsModel;

public interface ArticleService {
	
	public String getArticles();
	
	public List<ArticleDetailsModel> recentArticles();
	
}
