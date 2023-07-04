package com.virtusa.newsportal.core.listeners;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.replication.ReplicationAction;


@Component(service =  EventHandler.class,
			property = {
				EventConstants.EVENT_TOPIC+"="+ReplicationAction.EVENT_TOPIC,
				EventConstants.EVENT_FILTER+"=(& (type=ACTIVATE) (paths=/content/newsportal/us/en/articles/*) )"
			},
			immediate = true)
public class ArticleActivationListener implements EventHandler{
	
	private static final Logger LOG = LoggerFactory.getLogger(ArticleActivationListener.class);

	@Override
	public void handleEvent(Event event) {
		
		LOG.info("Inside article activation event handler..");
		
		for(String propertyName: event.getPropertyNames()) {
			LOG.info("Property Name - {}, Property Value - {}", propertyName, event.getProperty(propertyName));
		}
		
	}

}
