package com.virtusa.newsportal.core.servlets;

import java.io.IOException;
import java.util.Iterator;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMException;

@Component(service = Servlet.class)
@SlingServletPaths(value = { "/newsportal/page-service" })
public class PageServiceServlet extends SlingAllMethodsServlet {
	
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		
		try(ResourceResolver resolver = request.getResourceResolver()) {
			PageManager pageManager = resolver.adaptTo(PageManager.class);
			
			Page articlePage = pageManager.getPage("/content/newsportal/us/en/articles");
			
			JsonArrayBuilder pageJsonList = Json.createArrayBuilder();
			if(articlePage != null) {
				Iterator<Page> childPages = articlePage.listChildren();				
				while (childPages.hasNext()) {
					Page page = childPages.next();
					JsonObjectBuilder pageJson = Json.createObjectBuilder();
					pageJson.add("pageTitle", page.getTitle());
					pageJson.add("pagePath", page.getPath());
					pageJsonList.add(pageJson);
				}
			}
			response.getWriter().write(pageJsonList.build().toString());
		} 
	}
	
	@Override
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		try(ResourceResolver resolver = request.getResourceResolver()) {
			PageManager pageManager = resolver.adaptTo(PageManager.class);
			pageManager.create("/content/newsportal/us/en/articles", 
					request.getParameter("pageName"), 
					"/conf/newsportal/settings/wcm/templates/article-template",
					request.getParameter("pageTitle"));
		} catch (WCMException e) {
			e.printStackTrace();
		}
	}

}
