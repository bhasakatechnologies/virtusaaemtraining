package com.virtusa.newsportal.core.servlets;

import java.io.IOException;
import java.util.Iterator;

import javax.jcr.query.Query;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;

import com.day.cq.wcm.api.Page;

@Component(service = Servlet.class,immediate = true)
@SlingServletPaths(value = { "/newsportal/article-search" })
public class ArticleSearchServlet  extends SlingSafeMethodsServlet {

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		
		String searchTerm = request.getParameter("q");
		
		String query = "SELECT * FROM [cq:Page] AS s WHERE ISDESCENDANTNODE([/content/wknd]) and CONTAINS(s.*, '"+searchTerm+"')";

		try(ResourceResolver resolver = request.getResourceResolver()){
			
			   JsonArrayBuilder articlesJson = Json.createArrayBuilder();

			   Iterator<Resource> results = resolver.findResources(query, Query.JCR_SQL2);
			   while (results.hasNext()) {
				 Resource resource = results.next();
				 Page page = resource.adaptTo(Page.class);
				 JsonObjectBuilder pageJson = Json.createObjectBuilder();
				 pageJson.add("title", page.getTitle());
				 pageJson.add("path", page.getPath());
				 articlesJson.add(pageJson);
				
			   }
			   
			   response.getWriter().write(articlesJson.build().toString());
			}
	}
}
