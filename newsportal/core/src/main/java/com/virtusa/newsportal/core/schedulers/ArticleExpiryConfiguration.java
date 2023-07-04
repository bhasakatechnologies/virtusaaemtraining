package com.virtusa.newsportal.core.schedulers;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition
public @interface ArticleExpiryConfiguration {
	
	@AttributeDefinition(name = "Cron Expression")
	public String cronExpression() default "*/15 * * ? * *";
	
	
	@AttributeDefinition(name = "Schduler Name")
	public String schedulerName() default "article-expiry";
	
	// enable
	@AttributeDefinition(name = "Enable/Disable Configuration")
	public boolean enalbe() default true;
	
	// articleRootPage
	@AttributeDefinition(name = "Article Root page Path")
	public String articleRootPath() default "/content/newsportal/us/en/articles";
	

}
