package com.telefonica.euro_iaas.paasmanager.environment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import junit.framework.TestCase;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.ContextLoaderListener;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.bootstrap.InitDbBootstrap;
import com.telefonica.euro_iaas.paasmanager.claudia.util.ClaudiaUtil;
import com.telefonica.euro_iaas.paasmanager.dao.ProductReleaseDao;
import com.telefonica.euro_iaas.paasmanager.dao.impl.EnvironmentDaoJpaImpl;
import com.telefonica.euro_iaas.paasmanager.exception.AlreadyExistEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.EnvironmentInstanceNotFoundException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidEnvironmentRequestException;
import com.telefonica.euro_iaas.paasmanager.manager.EnvironmentManager;
import com.telefonica.euro_iaas.paasmanager.manager.TierManager;
import com.telefonica.euro_iaas.paasmanager.manager.impl.EnvironmentManagerImpl;
import com.telefonica.euro_iaas.paasmanager.model.Attribute;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierDto;
import com.telefonica.euro_iaas.paasmanager.rest.resources.EnvironmentResource;
import com.telefonica.euro_iaas.paasmanager.rest.resources.EnvironmentResourceImpl;
import com.telefonica.euro_iaas.paasmanager.rest.resources.TierResource;
import com.telefonica.euro_iaas.paasmanager.rest.util.OVFGeneration;
import com.telefonica.euro_iaas.paasmanager.rest.validation.EnvironmentResourceValidator;
import com.telefonica.euro_iaas.paasmanager.rest.validation.EnvironmentResourceValidatorImpl;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;

@RunWith(SpringJUnit4ClassRunner.class)
 //ApplicationContext will be loaded from "classpath:/app-config.xml"
@ContextConfiguration(locations = {"classpath:/applicationContextTest.xml"})
@ActiveProfiles("dummy")

public class TierTest{


	   @Autowired
	   private TierResource tierResource;
	   
	   @Autowired
	   private EnvironmentResource environmentResource;
	   
	   
	   @Autowired
	   private ProductReleaseDao productReleaseDao;
	   
	   @Autowired
	   private EnvironmentManager environmentManager;
	   
	   @Autowired
	   private TierManager tierManager;

	   

	   String org = "FIWARE";
		String vdc = "6571e3422ad84f7d828ce2f30373b3d4";


	   @Before
		public void setUp() throws Exception {
		   List<Attribute> atttomcat = new ArrayList<Attribute>();

			atttomcat.add(new Attribute("ssl_port", "8443",
					"The ssl listen port"));
			atttomcat.add(new Attribute("port", "8080",
					"The ssl listen port"));
			atttomcat.add(new Attribute("openports", "8080",
					"The ssl listen port"));
			atttomcat.add(new Attribute("sdcgroupid", "id_web_server",
					"The ssl listen port"));

			
		}
	   
	   @Test
	   public void testCreateTierOK() throws Exception {
		   System.out.println("testCreateTierOK");
		   Environment environment2 = new Environment();
		   environment2.setName("create_tier_ok");
		   environment2.setDescription("description");
		   
		   environmentResource.insert(org, vdc, environment2.toDto());
		   ProductRelease tomcat7Att = new ProductRelease("tomcat8", "78",
					"Tomcat server 8", null);
		 
		   tomcat7Att = productReleaseDao.create(tomcat7Att);
		 
			assertNotNull(tomcat7Att);
		    assertNotNull(tomcat7Att.getId());
			assertEquals(tomcat7Att.getProduct(), "tomcat8");
			assertEquals(tomcat7Att.getVersion(), "78");
		   
			Tier tierbk = new Tier("tiercreated_ok", new Integer(1), new Integer(1),
						new Integer(1), null);
			tierbk.setImage("image");
			tierbk.setIcono("icono");
			tierbk.setFlavour("flavour");
			tierbk.setFloatingip("floatingip");
			tierbk.setPayload("");
			tierbk.setKeypair("keypair");
			tierbk.addProductRelease(tomcat7Att);

			tierResource.insert(org, vdc, "create_tier_ok", tierbk.toDto());
			TierDto tier = tierResource.load(vdc, "create_tier_ok", "tiercreated_ok");
			assertEquals(tier.getName(),"tiercreated_ok");
			assertEquals(tier.getProductReleaseDtos().size(),1);
			assertEquals(tier.getProductReleaseDtos().get(0).getProductName(),"tomcat8");
	   }
	   
	   @Test(expected=javax.ws.rs.WebApplicationException.class)
	   public void testCreateTierAlreadyExist() throws Exception {
		   System.out.println("testCreateTierAlreadyExist");
		   Environment environmentBk = new Environment();
		   environmentBk.setName("create_tier_already");
		   environmentBk.setDescription("description");
		   
		   environmentResource.insert(org, vdc, environmentBk.toDto());
		   
			Tier tierbk = new Tier("tiercreatedalready", new Integer(1), new Integer(1),
						new Integer(1), null);
			tierbk.setImage("image");
			tierbk.setIcono("icono");
			tierbk.setFlavour("flavour");
			tierbk.setFloatingip("floatingip");
			tierbk.setPayload("");
			tierbk.setKeypair("keypair");

			tierResource.insert(org, vdc, "create_tier_already", tierbk.toDto());
			tierResource.insert(org, vdc, "create_tier_already", tierbk.toDto());
	   }
	   
	   @Test(expected=Exception.class)
	   public void testCreateTierNotFound() throws Exception {
		   System.out.println("testCreateTierOK");
		   Environment environmentBk = new Environment();
		   environmentBk.setName("create_tier");
		   environmentBk.setDescription("description");
		   
		   environmentResource.insert(org, vdc, environmentBk.toDto());

			TierDto tier = tierResource.load(vdc, "testCreateTierOK", "tiercreated");

	   }
	   
	   
	 
	  
	   @Test
	   public void testdUpdateTier() throws Exception {
		   System.out.println("testUpdateTier");
		   
		   Environment environmentBk = new Environment();
			environmentBk.setName("updatedenvironmenttier");
			environmentBk.setDescription("Description Second environment");
			Tier tierbk = new Tier("tierupdatetier", new Integer(1), new Integer(1),
						new Integer(1), null);
			tierbk.setImage("image");
			tierbk.setIcono("icono");
			tierbk.setFlavour("flavour");
			tierbk.setFloatingip("floatingip");
			tierbk.setPayload("");
			tierbk.setKeypair("keypair");
			environmentBk.addTier(tierbk);
			
			environmentResource.insert(org, vdc, environmentBk.toDto());
		  
		   Environment env = environmentManager.load("updatedenvironmenttier", vdc);	 
		   assertEquals(env.getName(),"updatedenvironmenttier");
		   assertEquals(env.getTiers().size(),1);
		   assertEquals(env.getTiers().get(0).getFlavour(),"flavour");
		   assertEquals(env.getTiers().get(0).getIcono(),"icono");
		   
		   TierDto tierDto = tierResource.load(vdc, "updatedenvironmenttier", "tierupdatetier");
		   assertEquals(tierDto.getFlavour(),"flavour");
		   assertEquals(tierDto.getName(),"tierupdatetier");

		   
		   tierDto.setFlavour("flavour3");
		   tierDto.setIcono("icono2");
		   tierResource.update(org, vdc, "updatedenvironmenttier", tierDto);
		   TierDto tier2Dto = tierResource.load(vdc, "updatedenvironmenttier", "tierupdatetier");
		   assertEquals(tier2Dto.getFlavour(),"flavour3");
		  // assertEquals(tier2Dto.getName(),"tiername2");
		   
		   Environment env3 = environmentManager.load("updatedenvironmenttier", vdc);
		   assertEquals(env3.getName(),"updatedenvironmenttier");
		   assertEquals(env3.getTiers().size(),1);
		   assertEquals(env3.getTiers().get(0).getName(),"tierupdatetier");
		   assertEquals(env3.getTiers().get(0).getFlavour(),"flavour3");
		   assertEquals(env3.getTiers().get(0).getIcono(),"icono2");


	   }

	   @Test
	   public void testdUpdateTierSoftware() throws Exception {
		   System.out.println("testdUpdateTierSoftware");
		   
		   ProductRelease product2 = new ProductRelease("test", "0.1",
					"test 0.1", null);
		

		    product2 = productReleaseDao.create(product2);
			assertNotNull(product2);
		    assertNotNull(product2.getId());
			assertEquals(product2.getProduct(), "test");
			assertEquals(product2.getVersion(), "0.1");
			
			ProductRelease product3 = new ProductRelease("test2", "0.1",
					"test2 0.1", null);
		

			product3 = productReleaseDao.create(product3);
			assertNotNull(product3);
		    assertNotNull(product3.getId());
			assertEquals(product3.getProduct(), "test2");
			assertEquals(product3.getVersion(), "0.1");
		   
		   Environment environmentBk = new Environment();
			environmentBk.setName("updatedenvironmentsoftwware");
			environmentBk.setDescription("Description Second environment");
			Tier tierbk = new Tier("tiersoftware", new Integer(1), new Integer(1),
						new Integer(1), null);
			tierbk.addProductRelease(product2);
			tierbk.setImage("image");
			tierbk.setIcono("icono");
			tierbk.setFlavour("flavour");
			tierbk.setFloatingip("floatingip");
			tierbk.setPayload("");
			tierbk.setKeypair("keypair");
			environmentBk.addTier(tierbk);
			
			environmentResource.insert(org, vdc, environmentBk.toDto());
		  
		   Environment env = environmentManager.load("updatedenvironmentsoftwware", vdc);	 
		   assertEquals(env.getName(),"updatedenvironmentsoftwware");
		   assertEquals(env.getTiers().size(),1);
		   assertEquals(env.getTiers().get(0).getFlavour(),"flavour");
		   assertEquals(env.getTiers().get(0).getIcono(),"icono");
		   
		   TierDto tierDto = tierResource.load(vdc, "updatedenvironmentsoftwware", "tiersoftware");
		   assertEquals(tierDto.getFlavour(),"flavour");
		   assertEquals(tierDto.getName(),"tiersoftware");
		   assertEquals(tierDto.getProductReleaseDtos().size(),1);
		   tierDto.setFlavour("flavour3");
		   tierDto.setIcono("icono2");
		   tierDto.addProductRelease(product3.toDto());
		   tierResource.update(org, vdc, "updatedenvironmentsoftwware", tierDto);
		   TierDto tier2Dto = tierResource.load(vdc, "updatedenvironmentsoftwware", "tiersoftware");
		   assertEquals(tier2Dto.getProductReleaseDtos().size(),2);
		   assertEquals(tier2Dto.getProductReleaseDtos().get(0).getProductName(),"test");
		   assertEquals(tier2Dto.getProductReleaseDtos().get(1).getProductName(),"test2");		
		   
		   Environment env3 = environmentManager.load("updatedenvironmentsoftwware", vdc);
		   assertEquals(env3.getName(),"updatedenvironmentsoftwware");
		   assertEquals(env3.getTiers().size(),1);
		   assertEquals(env3.getTiers().get(0).getName(),"tiersoftware");
		   assertEquals(env3.getTiers().get(0).getFlavour(),"flavour3");
		   assertEquals(env3.getTiers().get(0).getIcono(),"icono2");
		   assertEquals(env3.getTiers().get(0).getProductReleases().size(),2);
	   }
	
	   
	   @Test
	   public void testeDeleteTier() throws Exception {
		   
		   Environment environment = new Environment();
			environment.setName("testeDeleteTier2");
			environment.setDescription("Description");

			ProductRelease tomcat7Att = new ProductRelease("tomcat9", "7",
					"Tomcat server 7", null);
		 
		   tomcat7Att = productReleaseDao.create(tomcat7Att);
		 
			
			
			Tier tier = new Tier("2tierotro", new Integer(1), new Integer(1),
					new Integer(1), null);
			tier.setImage("image");
			tier.setIcono("icono");
			tier.setFlavour("flavour");
			tier.setFloatingip("floatingip");
			tier.setPayload("");
			tier.setKeypair("keypair");
			tier.addProductRelease(tomcat7Att);
			List<Tier> tiers = new ArrayList<Tier>();
			tiers.add(tier);
			environment.setTiers(tiers);
			
			environmentResource.insert(org, vdc, environment.toDto());

		   Environment env = environmentManager.load("testeDeleteTier2", vdc);	
		  
		   assertEquals(env.getName(),"testeDeleteTier2");
		   assertEquals(env.getTiers().size(),1);
		   assertEquals(env.getTiers().get(0).getFlavour(),"flavour");
		   assertEquals(env.getTiers().get(0).getIcono(),"icono");
		   
		   tierResource.delete(org, vdc, "testeDeleteTier2", "2tierotro");
		
		   
		   Environment env2 = environmentManager.load("testeDeleteTier2", vdc); 
		   assertEquals(env2.getName(),"testeDeleteTier2");
		   assertEquals(env2.getTiers().size(),0);
		   
		 
	   }
	   
	   @Test
	   public void testeDeleteAndCreatedTier() throws Exception {
		   
		   Environment environment = new Environment();
			environment.setName("envDeleteAndCreatedTier2");
			environment.setDescription("Description");

			
			Tier tier = new Tier("tierdeleteandcreated1", new Integer(1), new Integer(1),
					new Integer(1), null);
			tier.setImage("image");
			tier.setIcono("icono");
			tier.setFlavour("flavour");
			tier.setFloatingip("floatingip");
			tier.setPayload("");
			tier.setKeypair("keypair");

			ProductRelease tomcat7Att = new ProductRelease("tomcat34", "7",
					"Tomcat server 7", null);
		 
		   tomcat7Att = productReleaseDao.create(tomcat7Att);
		    tier.addProductRelease(tomcat7Att);
			List<Tier> tiers = new ArrayList<Tier>();
			tiers.add(tier);
			environment.setTiers(tiers);
			
			environmentResource.insert(org, vdc, environment.toDto());

		   Environment env = environmentManager.load("envDeleteAndCreatedTier2", vdc);	
		  
		   assertEquals(env.getName(),"envDeleteAndCreatedTier2");
		   assertEquals(env.getTiers().size(),1);
		   assertEquals(env.getTiers().get(0).getFlavour(),"flavour");
		   assertEquals(env.getTiers().get(0).getIcono(),"icono");
		   
		   tierResource.delete(org, vdc, "envDeleteAndCreatedTier2", "tierdeleteandcreated1");
		
		   
		   Environment env2 = environmentManager.load("envDeleteAndCreatedTier2", vdc); 
		   assertEquals(env2.getName(),"envDeleteAndCreatedTier2");
		   assertEquals(env2.getTiers().size(),0);
		   
		   tierResource.insert(org, vdc, "envDeleteAndCreatedTier2", tier.toDto());
		   
		   env2 = environmentManager.load("envDeleteAndCreatedTier2", vdc); 
		   assertEquals(env2.getName(),"envDeleteAndCreatedTier2");
		   assertEquals(env2.getTiers().size(),1);
		   assertEquals(env2.getTiers().get(0).getName(),"tierdeleteandcreated1");

	   }
	  

}