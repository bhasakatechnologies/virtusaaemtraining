package com.virtusa.newsportal.core.models;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.adobe.granite.asset.api.AssetManager;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;

@Model(adaptables = { Resource.class,SlingHttpServletRequest.class })
public class CategoriesModel {
	
	@ValueMapValue
	@Default(values = "newsportal:categories")
	private String categoryTag;
	
	
	@SlingObject
	ResourceResolver resolver;
	
	Map<String, Long> tagsData;
	
	@PostConstruct
	public void init() {
		
		AssetManager assetManager = resolver.adaptTo(AssetManager.class);
		
		tagsData = new HashMap<>();
		TagManager tagManager = resolver.adaptTo(TagManager.class);
		Tag categoryTagObj = tagManager.resolve(categoryTag);
		Iterator<Tag> childTags = categoryTagObj.listChildren();
		while (childTags.hasNext()) {
			Tag tag = childTags.next();
			tagsData.put(tag.getTitle(), tag.getCount());			
		}
	}

	public Map<String, Long> getTagsData() {
		return tagsData;
	}	

}
