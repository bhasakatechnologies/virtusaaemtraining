package com.virtusa.newsportal.core.listeners;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.PageEvent;
/*import com.day.cq.workflow.WorkflowException;
import com.day.cq.workflow.WorkflowService;
import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkflowData;
import com.day.cq.workflow.model.WorkflowModel;
*/
import com.virtusa.newsportal.core.services.impl.NPUtilService;

@Component(service = EventHandler.class,
			property = {
				EventConstants.EVENT_TOPIC+"="+PageEvent.EVENT_TOPIC
			},
			immediate = true)
public class ArticleCreatedHanlder implements EventHandler {
	
	private static final Logger LOG = LoggerFactory.getLogger(ArticleCreatedHanlder.class);
	
	@Reference
	NPUtilService npUtilService;
	
	/*
	 * @Reference WorkflowService workflowService;
	 */

	@Override
	public void handleEvent(Event event) {
		
		List<Map<String, String>> props = (List<Map<String, String>>) event.getProperty("modifications");		
		for(Map<String, String> mprop : props) {
			if(mprop.get("type").equals("PageCreated")) {
				String pagePath = mprop.get("path"); // -> /content/newsportal/us/en/articles/article-23/jcr:content				
				try(ResourceResolver resolver = npUtilService.getResourceResolver()){
					Resource contentResource = resolver.getResource(pagePath+"/jcr:content");
					ModifiableValueMap mProp = contentResource.adaptTo(ModifiableValueMap.class);
					
					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.DAY_OF_MONTH, 15);
					mProp.put("articleExpiry", cal);
					mProp.put("articleTags", "wknd-shared:activity/cycling");
					resolver.commit();
					
					/* WorkflowSession wfsession = workflowService.getWorkflowSession(session); */
					/*
					 * WorkflowSession wfsession = resolver.adaptTo(WorkflowSession.class);
					 * WorkflowModel model =
					 * wfsession.getModel("/var/workflow/models/newsportal-content-activation");
					 * WorkflowData wfData = wfsession.newWorkflowData("JCR_PATH", pagePath);
					 * wfsession.startWorkflow(model, wfData);
					 */
					//session.save();
					
				} catch (PersistenceException e) {
					LOG.error("Error while adding default properties at page on Created Event , Exception - {}",e.getMessage());
				} /*
					 * catch (WorkflowException e) { // TODO Auto-generated catch block
					 * e.printStackTrace(); }
					 */
			}
		}	
			
	}

}
