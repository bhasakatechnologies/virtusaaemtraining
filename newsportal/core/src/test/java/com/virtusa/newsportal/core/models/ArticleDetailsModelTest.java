package com.virtusa.newsportal.core.models;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.day.cq.wcm.api.Page;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;


@ExtendWith(AemContextExtension.class)
class ArticleDetailsModelTest {
	
	AemContext context = new AemContext();
	
	ArticleDetailsModel articleDetails;
	
	ArticleDetailsModel articleDetails1;
	Calendar cal;

	@BeforeEach
	void setUp() throws Exception {
		context.addModelsForClasses(ArticleDetailsModel.class);
		
		context.load().json("/com/models/articledetailsmodel/article-details.json", "/content");
		
		Resource articleResource = context.currentResource("/content/article-details");
		articleDetails = articleResource.adaptTo(ArticleDetailsModel.class);
		
		Resource articleResource1 = context.currentResource("/content/article-details-with-expiry");
		articleDetails1 = articleResource1.adaptTo(ArticleDetailsModel.class);
		
		/*
		 * Map<String, Object> props = new HashMap<>(); props.put("articleTitle",
		 * "Article 1 Title"); props.put("articleDesc", "Article 1 desc");
		 * props.put("articleImage", "/content/dam/newsportal/asset.png");
		 * props.put("sling:resourceType", "newsportal/components/article-details");
		 * 
		 * cal = Calendar.getInstance(); cal.add(Calendar.DAY_OF_MONTH, 15);
		 * props.put("articleExpiry", cal);
		 * 
		 * Resource articleResource =
		 * context.create().resource("/content/articles/artice-details", props);
		 * articleDetails = articleResource.adaptTo(ArticleDetailsModel.class);
		 */	
	}

	@Test
	void testArticleTitle() {
		assertEquals("Jailer Movie Trailer", articleDetails.getArticleTitle());
		assertEquals("Jailer Movie trailer desc...", articleDetails.getArticleDesc());
		//assertEquals("/content/dam/newsportal/asset.jpg", articleDetails.getArticleImage());
		assertEquals("newsportal/components/article-details", articleDetails.getSlingResourceType());
		//assertEquals(cal.getTime(), articleDetails.getArticleExpiry());
		assertEquals(true, articleDetails.isArticleIsExpired());
	}
	
	
	@Test
	void testArticleExpiryTrue() {
		Map<String, Object> props = new HashMap<>();
		props.put("sling:resourceType", "newsportal/components/article-details");
		cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -15);
		props.put("articleExpiry", cal);
		
		Resource articleResource = context.create().resource("/content/articles/artice-details-1", props);
		ArticleDetailsModel articleDetails1 = articleResource.adaptTo(ArticleDetailsModel.class);
		assertEquals(true, articleDetails1.isArticleIsExpired());

	}
	
	/*
	 * @Test void testCurrentPageTitle() { Map<String, Object> properties = new
	 * HashMap<>(); properties.put("jcr:title", "article 1 title");
	 * properties.put("pageTitle", "article 1 page title");
	 * context.create().page("/content/article-1",
	 * "/conf/newsportal/settings/wcm/templates/article-template", properties); Page
	 * currentPage = context.currentPage("/content/article-1"); articleDetails =
	 * context.request().adaptTo(ArticleDetailsModel.class);
	 * 
	 * Map<String, Object> props = new HashMap<>(); props.put("sling:resourceType",
	 * "newsportal/components/article-details");
	 * 
	 * Resource articleResource2 = context.create().resource(currentPage,
	 * "article-details-2", props); ArticleDetailsModel articleDetails1 =
	 * articleResource2.adaptTo(ArticleDetailsModel.class);
	 * 
	 * assertNotEquals("article 1 title",articleDetails1.getPageTitle());
	 * assertEquals("article 1 page title",articleDetails1.getPageTitle()); }
	 */
	
	/*
		 * 
		 * @Test void testArticlePageTitle() { Map<String, Object> pageProps = new
		 * HashMap<>(); pageProps.put("jcr:title", "Articles");
		 * pageProps.put("pageTitle", "Newsportal Articles");
		 * context.create().page("/content/newsportal/articles", "article-template",
		 * pageProps); context.currentPage("/content/newsportal/articles");
		 * articleDetails = context.request().adaptTo(ArticleDetailsModel.class);
		 * 
		 * assertEquals("Newsportal Articles", articleDetails.getPageTitle());
		 * 
		 * }
		 */

}
