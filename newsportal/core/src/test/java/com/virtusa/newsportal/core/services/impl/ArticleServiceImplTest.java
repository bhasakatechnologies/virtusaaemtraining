package com.virtusa.newsportal.core.services.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;


@ExtendWith(AemContextExtension.class)
class ArticleServiceImplTest {
	
	AemContext context = new AemContext();
	
	ArticleServiceImpl articleService = new ArticleServiceImpl();
	ArticleConfiguration config;

	@BeforeEach
	void setUp() throws Exception {
		config = mock(ArticleConfiguration.class);
		when(config.clientId()).thenReturn("590325");
		when(config.articleRestAPI()).thenReturn("https://gorest.co.in/public/v2/posts");
		when(config.status()).thenReturn(true);
	}

	@Test
	void testLifeCycleMethod() {
		articleService.activate(config);
		articleService.update(config);
		assertNotNull(articleService.getArticles());
	}

}
