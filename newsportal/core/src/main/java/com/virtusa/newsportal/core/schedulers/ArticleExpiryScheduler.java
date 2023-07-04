package com.virtusa.newsportal.core.schedulers;

import java.util.HashMap;
import java.util.Map;

import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.apache.sling.event.jobs.JobManager;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = Runnable.class,
		immediate = true)
@Designate(ocd=ArticleExpiryConfiguration.class)
public class ArticleExpiryScheduler implements Runnable {

	private static final Logger LOG = LoggerFactory.getLogger(ArticleExpiryScheduler.class);
	
	
	@Reference
	JobManager jobManager;
	
	
	@Reference
	Scheduler scheduler;
	
	private String articleRootPath;
	
	@Activate
	public void activate(ArticleExpiryConfiguration config) {
		schedule(config);
	}
	
	@Modified
	public void update(ArticleExpiryConfiguration config) {
		schedule(config);
	}
	
	public void schedule(ArticleExpiryConfiguration config) {
		this.articleRootPath = config.articleRootPath();
		if(config.enalbe()) {
			ScheduleOptions options =  scheduler.EXPR(config.cronExpression());
			options.name(config.schedulerName());
			options.canRunConcurrently(false);
			scheduler.schedule(this, options);
		} else {
			scheduler.unschedule(config.schedulerName());
		}
	}
	
	@Override
	public void run() {
		LOG.info("Inside article expiry scheduler run method");
		
		Map<String, Object> props = new HashMap<>();
		props.put("articleRootPath", articleRootPath);
		
		jobManager.addJob("com/newsportal/job/article-expiry", props);
		
	}

}
