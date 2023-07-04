package com.virtusa.newsportal.core.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;
import com.virtusa.newsportal.core.services.impl.NPUtilService;

@Component(
		service = Servlet.class
		)
@SlingServletPaths(value = { "/newsportal/recent-articles","/newsportal/featured-articles" })
public class RecentArticleServlet extends SlingAllMethodsServlet {
	
	@Reference
	QueryBuilder queryBuilder;
	
	@Reference
	NPUtilService npUtilService;
	
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {	
		
		
		try(ResourceResolver resolver = npUtilService.getResourceResolver()){
			
			Map<String, String> predicates = new HashMap<>();
			predicates.put("type", "cq:Page");
			predicates.put("path", "/content/newsportal/us/en/articles");
			predicates.put("property", "jcr:content/cq:template");
			predicates.put("property.value", "/conf/newsportal/settings/wcm/templates/article-template");
			predicates.put("orderby", "@jcr:content/jcr:created");
			predicates.put("orderby.sort", "desc");
			predicates.put("p.limit", "5");
			
			Session session = resolver.adaptTo(Session.class);
			
			JsonArrayBuilder articlesJson = Json.createArrayBuilder();
					
			Query query = queryBuilder.createQuery(PredicateGroup.create(predicates), session);
			SearchResult results = query.getResult();
			List<Hit> resultList = results.getHits();
			
			for(Hit hit : resultList) {
				
				Resource resource = hit.getResource();
				Page page = resource.adaptTo(Page.class);
				
				JsonObjectBuilder pageJson = Json.createObjectBuilder();
				pageJson.add("title", page.getTitle());
				pageJson.add("path", page.getPath());
				articlesJson.add(pageJson);
			}
			
			response.getWriter().write(articlesJson.build().toString());
			
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
		
		
		
		/*
		 * try(ResourceResolver resolver = request.getResourceResolver()){ Resource
		 * userResource = resolver.getResource("/content/users"); Iterator<Resource>
		 * childResource = userResource.listChildren(); JsonArrayBuilder usersJsonList =
		 * Json.createArrayBuilder(); // [] while (childResource.hasNext()) { Resource
		 * resource = childResource.next(); ValueMap properties =
		 * resource.getValueMap(); JsonObjectBuilder userJson =
		 * Json.createObjectBuilder(); // {} userJson.add("firstName",
		 * properties.get("firstName", String.class)); userJson.add("lastName",
		 * properties.get("lastName", String.class)); userJson.add("email",
		 * properties.get("email", String.class)); userJson.add("userId",
		 * resource.getName()); usersJsonList.add(userJson); }
		 * response.getWriter().write(usersJsonList.build().toString()); }
		 */		
	}
	
	@Override
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
			
		try(ResourceResolver resolver = request.getResourceResolver()) {
			Resource userResource = resolver.getResource("/content/users");
			Map<String, Object> userProperties = new HashMap<>();
			userProperties.put("firstName", request.getParameter("firstName"));
			userProperties.put("lastName", request.getParameter("lastName"));
			userProperties.put("email", request.getParameter("email"));			
			resolver.create(userResource, request.getParameter("userId"), userProperties);
			resolver.commit();
			response.getWriter().write("Response from path based servlet - POST");
		} 		
	}
	
	@Override
	protected void doPut(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		try(ResourceResolver resolver = request.getResourceResolver()) {
			String userId = request.getParameter("userId"); // hemasai
			Resource userIdResource = resolver.getResource("/content/users/"+userId); // /content/users/hemasai
			if(userIdResource != null) {			
				ModifiableValueMap mProp = userIdResource.adaptTo(ModifiableValueMap.class);
				if(request.getParameter("firstname") != null)
					mProp.put("firstName", request.getParameter("firstname"));
				if(request.getParameter("lastName") != null)
					mProp.put("lastName", request.getParameter("lastName"));
				if(request.getParameter("email") != null)
					mProp.put("email", request.getParameter("email"));
				
				resolver.commit();				
				response.getWriter().write("Response from path based servlet - PUT");
			}
		}
	}
	
	@Override
	protected void doDelete(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		
		try(ResourceResolver resolver = request.getResourceResolver()) {
			String userId = request.getParameter("userId"); // nisheechadra
			Resource userIdResource = resolver.getResource("/content/users/"+userId); // /content/users/nisheechadra
			if(userIdResource != null) {
				resolver.delete(userIdResource);
				resolver.commit();
			}
			response.getWriter().write("Response from path based servlet - Delete");
		}
		
	}

}
