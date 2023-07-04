package com.virtusa.newsportal.core.services.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = NPUtilService.class)
public class NPUtilService {
	
	@Reference
	ResourceResolverFactory factory;
	
	public ResourceResolver getResourceResolver() {
		ResourceResolver resolver = null;
		try {
			Map<String, Object> props = new HashMap<>();
			props.put(ResourceResolverFactory.SUBSERVICE, "npsubservice");			
			resolver = factory.getServiceResourceResolver(props);
		} catch (LoginException e) {
			e.printStackTrace();
		}		
		return resolver;
	}

}
