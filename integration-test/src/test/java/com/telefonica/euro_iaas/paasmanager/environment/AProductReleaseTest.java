package com.telefonica.euro_iaas.paasmanager.environment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import junit.framework.TestCase;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.ContextLoaderListener;

import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.bootstrap.InitDbBootstrap;
import com.telefonica.euro_iaas.paasmanager.dao.ProductReleaseDao;
import com.telefonica.euro_iaas.paasmanager.dao.impl.EnvironmentDaoJpaImpl;
import com.telefonica.euro_iaas.paasmanager.exception.AlreadyExistEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.EnvironmentInstanceNotFoundException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidEnvironmentRequestException;
import com.telefonica.euro_iaas.paasmanager.manager.EnvironmentManager;
import com.telefonica.euro_iaas.paasmanager.manager.impl.EnvironmentManagerImpl;
import com.telefonica.euro_iaas.paasmanager.model.Attribute;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentDto;
import com.telefonica.euro_iaas.paasmanager.rest.resources.EnvironmentResource;
import com.telefonica.euro_iaas.paasmanager.rest.resources.EnvironmentResourceImpl;
import com.telefonica.euro_iaas.paasmanager.rest.resources.TierResource;
import com.telefonica.euro_iaas.paasmanager.rest.util.OVFGeneration;
import com.telefonica.euro_iaas.paasmanager.rest.validation.EnvironmentResourceValidator;
import com.telefonica.euro_iaas.paasmanager.rest.validation.EnvironmentResourceValidatorImpl;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;

@RunWith(SpringJUnit4ClassRunner.class)
// ApplicationContext will be loaded from "classpath:/app-config.xml"
@ContextConfiguration(locations = {"classpath:/applicationContextTest.xml"})
@ActiveProfiles("dummy")

public class AProductReleaseTest{


	
	  @Autowired
	   private ProductReleaseDao productReleaseDao;
	   
	 
	   
	  

	   @Test
	   public void testProductReleasesWithAttributes() throws Exception {
		   
		
		  List<ProductRelease> productReleases = productReleaseDao.findAll();
		  assertNotNull(productReleases);
		  System.out.println ("Number of product releases .." + productReleases);
		  System.out.println ("Number of enviornment .." + productReleases.size());
			  
		  int number = productReleases.size();
		  
		  List<Attribute> attHenar = new ArrayList();
		  attHenar.add(new Attribute("henar", "henar",
					"henar"));
		

		  ProductRelease productHenar = new ProductRelease(
					"henar", "0.1", "henar 0.1",
					attHenar);
		
	
		  productHenar = productReleaseDao.create(productHenar);
		  assertNotNull(productHenar);
		  assertEquals(productHenar.getProduct(), "henar");
		  assertEquals(productHenar.getVersion(), "0.1");
		  
		  productReleases = productReleaseDao.findAll();
		  assertNotNull(productReleases);
		  System.out.println ("Number of product releases .." + productReleases);
		  System.out.println ("Number of product releases  .." + productReleases.size());
		  assertEquals(productReleases.size(), number+1);
		  System.out.println (productReleases.get(0).getProduct() + " " + productReleases.get(0).getVersion() + " " + productReleases.get(0).getName());
		  
		  ProductRelease productRelease = productReleaseDao.load("henar-0.1");
		  assertNotNull(productRelease);
		  assertEquals(productRelease.getProduct(), "henar");
		  assertEquals(productRelease.getVersion(), "0.1");
			
	   }
	   
	   @Test
	   public void testProductReleasesNotAttributes() throws Exception {
		   
		

		  ProductRelease productTomcat= new ProductRelease(
					"mysql", "2", "tomcat 7",
					null);
		
	
		  productTomcat = productReleaseDao.create(productTomcat);
		  assertNotNull(productTomcat);
		  assertEquals(productTomcat.getProduct(), "mysql");
		  assertEquals(productTomcat.getVersion(), "2");
		  
		  List<ProductRelease> productReleases = productReleaseDao.findAll();
		  assertNotNull(productReleases);
		  System.out.println ("Number of product releases .." + productReleases);
		  System.out.println ("Number of product releases  .." + productReleases.size());
		 
		  System.out.println (productReleases.get(0).getProduct() + " " + productReleases.get(0).getVersion() + " " + productReleases.get(0).getName());
		  
		  ProductRelease productRelease = productReleaseDao.load("mysql-2");
		  assertNotNull(productRelease);
		  assertEquals(productRelease.getProduct(), "mysql");
		  assertEquals(productRelease.getVersion(), "2");
				
	   }
	

}
