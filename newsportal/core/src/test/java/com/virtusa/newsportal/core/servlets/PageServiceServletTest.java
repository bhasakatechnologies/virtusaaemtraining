package com.virtusa.newsportal.core.servlets;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.sling.servlethelpers.MockSlingHttpServletRequest;
import org.apache.sling.servlethelpers.MockSlingHttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.day.cq.wcm.api.Page;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class PageServiceServletTest {
	
	AemContext context = new AemContext();
	
	PageServiceServlet pageServlet = new PageServiceServlet();
	
	MockSlingHttpServletRequest request;
	MockSlingHttpServletResponse response;

	@BeforeEach
	void setUp() throws Exception {
		request = context.request();
		response = context.response();
	}

	@Test
	void testDoGet() throws ServletException, IOException {
		
		Map<String, Object> pageProps = new HashMap<>();
		pageProps.put("jcr:title", "Articles");
		Page articlePage = context.create().page("/content/newsportal/us/en/articles", "article-template", pageProps);
		context.create().page(articlePage, "article-1");
		context.create().page(articlePage, "article-2");
		context.create().page(articlePage, "article-3");
		
		pageServlet.doGet(request, response);
		assertNotNull(response.getStatus());
	}
	
	@Test
	void testDoPost() throws ServletException, IOException {
		request.addRequestParameter("pageName", "article-4");
		request.addRequestParameter("pageTitle", "Article 4");
		pageServlet.doPost(request, response);
		assertNotNull(response.getStatus());
	}

}
