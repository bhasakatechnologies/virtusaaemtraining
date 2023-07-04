package com.virtusa.newsportal.core.services.impl;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition
public @interface ArticleConfiguration {
	
	@AttributeDefinition(name = "Article REST API")
	public String articleRestAPI() default "https://gorest.co.in/public/v2/posts";
	
	@AttributeDefinition(name = "Enable/Disable")
	public boolean status() default true;
	
	@AttributeDefinition(name = "Client Id")
	public String clientId() default "590325";
	

}
