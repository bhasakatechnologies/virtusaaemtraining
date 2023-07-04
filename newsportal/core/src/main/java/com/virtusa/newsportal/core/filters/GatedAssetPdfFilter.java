package com.virtusa.newsportal.core.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.servlets.annotations.SlingServletFilter;
import org.apache.sling.servlets.annotations.SlingServletFilterScope;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;

@Component(service = Filter.class, immediate = true,configurationPolicy = ConfigurationPolicy.REQUIRE)
@SlingServletFilter(
		scope = SlingServletFilterScope.REQUEST,
		pattern = "/content/dam/newsportal/*.pdf"
		)
public class GatedAssetPdfFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		SlingHttpServletRequest slingRequest = (SlingHttpServletRequest)request;
		SlingHttpServletResponse slingResponse = (SlingHttpServletResponse) response;
		String userName = slingRequest.getUserPrincipal().getName();
		if(userName.endsWith("anonymous")) {
			slingResponse.sendError(403);
			chain.doFilter(slingRequest, slingResponse);
		} else {
			slingRequest.getUserPrincipal();
		}
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
