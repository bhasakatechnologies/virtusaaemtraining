package com.virtusa.newsportal.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;

@Component(
		service = Servlet.class		
		)
@SlingServletResourceTypes(
		resourceTypes = { "newsportal/components/page","newsportal/servlets/details" },
		extensions = {"json","txt","html"},
		methods = {"GET"},
		selectors = {"recent","print"}
		)
public class FeaturedArticleServlet extends SlingSafeMethodsServlet {

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().write("Response from resource based servlet - GET");
	}
}
