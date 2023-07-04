package com.virtusa.newsportal.core.schedulers;

import java.util.Date;
import java.util.Iterator;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.ValueFormatException;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.version.VersionException;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.Replicator;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.virtusa.newsportal.core.services.impl.NPUtilService;

@Component(
		service = JobConsumer.class,
		property = {
			JobConsumer.PROPERTY_TOPICS+"=com/newsportal/job/article-expiry"	
		},
		immediate = true)
public class ArticleExpiryJob implements JobConsumer {

	@Reference
	NPUtilService npUtilService;
	
	@Reference
	Replicator replicator;
	
	@Override
	public JobResult process(Job job) {
		try(ResourceResolver resolver = npUtilService.getResourceResolver()){
			String articleRootPath = job.getProperty("articleRootPath").toString();
			PageManager pageManager = resolver.adaptTo(PageManager.class);
			Page articlePage = pageManager.getPage(articleRootPath);
			if(articlePage != null) {
				Iterator<Page> childPages = articlePage.listChildren();
				while (childPages.hasNext()) {
					Page page = childPages.next();
					ValueMap properties = page.getProperties();
					Date articleExpiry = properties.get("articleExpiry",Date.class);
					Date today = new Date();
					if(articleExpiry != null && articleExpiry.compareTo(today)<0) {
						
						replicator.replicate(
								resolver.adaptTo(Session.class), 
								ReplicationActionType.DEACTIVATE, 
								page.getPath());
						
						Session session = resolver.adaptTo(Session.class);
						// Node contentNodeObj = session.getNode(page.getPath()+"/jcr:content");
						
						Resource contentResource = page.getContentResource();
						Node contentNode = contentResource.adaptTo(Node.class);	
						contentNode.setProperty("pageDeactivated", true);
						contentNode.getSession().save();
						
					}					
				}
			} else {
				return JobResult.FAILED;
			}
			
		} catch (ReplicationException e) {
			e.printStackTrace();
			return JobResult.FAILED;
		} catch (ValueFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (VersionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LockException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ConstraintViolationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return JobResult.OK;
	}

}
