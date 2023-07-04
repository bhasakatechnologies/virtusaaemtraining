/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.virtusa.newsportal.core.listeners;

import java.util.List;

import org.apache.sling.api.resource.observation.ResourceChange;
import org.apache.sling.api.resource.observation.ResourceChangeListener;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A service to demonstrate how changes in the resource tree
 * can be listened for. 
 * Please note, that apart from EventHandler services,
 * the immediate flag should not be set on a service.
 */
@Component(
		   service = ResourceChangeListener.class,
		   property = {
				   ResourceChangeListener.CHANGES+"="+ResourceChangeListener.CHANGE_ADDED,
				   ResourceChangeListener.CHANGES+"="+ResourceChangeListener.CHANGE_REMOVED,
				   ResourceChangeListener.CHANGES+"="+ResourceChangeListener.CHANGE_CHANGED,
				   ResourceChangeListener.PATHS+"=/content/newsportal/us/en/articles"
		   },
           immediate = true
)
public class ArticleResourceListener implements ResourceChangeListener {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void onChange(List<ResourceChange> changes) {
        
        for(ResourceChange resourceChange: changes) {
        	logger.info("Path - {}, Type - {}",resourceChange.getPath(),resourceChange.getType().name());
        }
    }
}

