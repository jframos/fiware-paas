/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

*/
package com.telefonica.euro_iaas.paasmanager.bootstrap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.dao.ApplicationTypeDao;
import com.telefonica.euro_iaas.paasmanager.dao.ApplicationReleaseDao;
import com.telefonica.euro_iaas.paasmanager.dao.OSDao;
import com.telefonica.euro_iaas.paasmanager.dao.ProductTypeDao;
import com.telefonica.euro_iaas.paasmanager.dao.ProductReleaseDao;
import com.telefonica.euro_iaas.paasmanager.dao.ArtifactTypeDao;
import com.telefonica.euro_iaas.paasmanager.dao.ArtifactDao;
import com.telefonica.euro_iaas.paasmanager.dao.EnvironmentTypeDao;
import com.telefonica.euro_iaas.paasmanager.dao.EnvironmentDao;
import com.telefonica.euro_iaas.paasmanager.dao.ServiceDao;
import com.telefonica.euro_iaas.paasmanager.dao.TierDao;
import com.telefonica.euro_iaas.paasmanager.model.ApplicationRelease;
import com.telefonica.euro_iaas.paasmanager.model.ApplicationType;
import com.telefonica.euro_iaas.paasmanager.model.Artifact;
import com.telefonica.euro_iaas.paasmanager.model.ArtifactType;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentType;
import com.telefonica.euro_iaas.paasmanager.model.OS;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.ProductType;
import com.telefonica.euro_iaas.paasmanager.model.Tier;

/**
 * Populates data base with synthetic data to emulate the preconditions of paas
 * manager
 * 
 * @author Jesus M. Movilla
 * @version $Id: $
 */
// TODO delete this class when the preconditions are done.
public class InitDbBootstrap implements ServletContextListener {

    /** {@inheritDoc} */
    @Override
    public void contextInitialized(ServletContextEvent event) {
    	System.out.println("InitDbBootstrap. START");
    	WebApplicationContext ctx = WebApplicationContextUtils
                .getWebApplicationContext(event.getServletContext());

    	OSDao osDao = (OSDao) ctx.getBean("osDao");
        ProductReleaseDao productReleaseDao = (ProductReleaseDao) ctx.getBean("productReleaseDao");
        ProductTypeDao productTypeDao = (ProductTypeDao) ctx.getBean("productTypeDao");
     
        ApplicationReleaseDao applicationReleaseDao =
            (ApplicationReleaseDao) ctx.getBean("applicationReleaseDao");
        ApplicationTypeDao applicationTypeDao 
        	= (ApplicationTypeDao) ctx.getBean("applicationTypeDao");
        
        ArtifactTypeDao artifactTypeDao = 
        		(ArtifactTypeDao) ctx.getBean("artifactTypeDao");
        ArtifactDao artifactDao = 
        		(ArtifactDao) ctx.getBean("artifactDao");
       
        EnvironmentTypeDao environmentTypeDao = 
        		(EnvironmentTypeDao) ctx.getBean("environmentTypeDao");
        EnvironmentDao environmentDao = 
        		(EnvironmentDao) ctx.getBean("environmentDao");
        
        TierDao tierDao = 
        		(TierDao) ctx.getBean("tierDao");
        ServiceDao serviceDao = 
        		(ServiceDao) ctx.getBean("serviceDao");
        
        try{
            osDao.load("95");
            osDao.load("94");
        } catch (EntityNotFoundException e) {
        	try{
        		OS so1 = new OS("94", "Ubuntu", "Ubuntu 10.04", "10.04");
                OS so2 = new OS("95", "Debian", "Debian 5", "5");
                so1 = osDao.create(so1);
                so2 = osDao.create(so2);
                List<OS> supportedSSOO1 = Arrays.asList(so1);
                List<OS> supportedSSOO2 = Arrays.asList(so2);
                List<OS> supportedSSOO12 = Arrays.asList(so1,so2);
                
                //ProductTypes
                ProductType productTypeFirewall = new ProductType("Firewall", 
                		"Firewall description");
                ProductType productTypeDatabase = new ProductType("Database", 
                		"database description");
                ProductType productTypeAWS = new ProductType("ApplicationWebServer", 
                		"Application Web Server description");
                ProductType productTypeLoadBalancer = new ProductType("LoadBalancer", 
                		"LoadBalancer description");
                
                productTypeFirewall = productTypeDao.create(productTypeFirewall);
                productTypeFirewall = productTypeDao.create(productTypeDatabase);
                productTypeAWS = productTypeDao.create(productTypeAWS);             
                productTypeLoadBalancer = productTypeDao.create(productTypeLoadBalancer);             
                
                //ArtifactTypes
                ArtifactType artifactTypeWar = new ArtifactType("AWSWar", 
                		"AWSWar description", productTypeAWS);
                ArtifactType artifactTypeDescriptorWar = new ArtifactType("AWSDescriptor", 
                		"AWS XML Descriptor", productTypeAWS);
                ArtifactType artifactTypeProperties = new ArtifactType("properties", 
                		"Properties description", productTypeAWS);
                ArtifactType artifactTypeEar = new ArtifactType("AWSEar", 
                		"AWSEar description", productTypeAWS);
                ArtifactType artifactTypeSql = new ArtifactType("sql", 
                		"Sql description", productTypeDatabase);
                
                artifactTypeWar = artifactTypeDao.create(artifactTypeWar);                             
                artifactTypeDescriptorWar = artifactTypeDao.create(artifactTypeDescriptorWar);                             
                artifactTypeProperties = artifactTypeDao.create(artifactTypeProperties);                             
                artifactTypeEar = artifactTypeDao.create(artifactTypeEar);                             
                artifactTypeSql = artifactTypeDao.create(artifactTypeSql);                             
                
                //EnvironmentType
                EnvironmentType environmentTypeJavaSpring = new EnvironmentType(
                		"Java-Spring Environment", "Java-Spring Env description");
                environmentTypeJavaSpring 
                	= environmentTypeDao.create(environmentTypeJavaSpring);                             
                List<EnvironmentType> environmentTypeJavaSprings 
                	= Arrays.asList(environmentTypeJavaSpring);
                
                //ProductReleases
                ProductRelease tomcat7 = new ProductRelease("tomcat", "7", "tomcat 7.0 description",
            			null, null, supportedSSOO12, true, productTypeAWS);
                tomcat7 = productReleaseDao.create(tomcat7); 
                ProductRelease postgres84 = new ProductRelease("postgres", "8.4", "postgres 8.4 description",
            			null, null, supportedSSOO12, true, productTypeDatabase);
                postgres84 = productReleaseDao.create(postgres84); 
                ProductRelease firewall1 = new ProductRelease("firewall", "1.0", "firewall 1.0 description",
            			null, null, supportedSSOO12, true, productTypeFirewall);
                firewall1 = productReleaseDao.create(firewall1); 
                ProductRelease haproxy1 = new ProductRelease("haproxy", "1.0", "haproxy 1.0 description",
            			null, null, supportedSSOO12, true, productTypeLoadBalancer);
                haproxy1 = productReleaseDao.create(haproxy1); 
                ProductRelease jbossx = new ProductRelease("jboss", "x.0", "jboss x.0 description",
            			null, null, supportedSSOO12, true, productTypeLoadBalancer);
                jbossx = productReleaseDao.create(jbossx);
                
                List<ProductRelease> productReleasesTestingTier 
                	= new ArrayList<ProductRelease>();
                
                productReleasesTestingTier.add(tomcat7);
                productReleasesTestingTier.add(postgres84);
                
                //Tiers
                Tier tomcat7postgres8Tier = new Tier("tomcat7postgres8Tier", 1,1,1,productReleasesTestingTier);
                tomcat7postgres8Tier = tierDao.create(tomcat7postgres8Tier);
                
                List<Tier> tiersTesting = new ArrayList<Tier>();
                tiersTesting.add(tomcat7postgres8Tier);
                
                Environment testingEnvironment = new Environment("testing", 
                		environmentTypeJavaSpring,
                		tiersTesting);
                testingEnvironment = environmentDao.create(testingEnvironment);
                
                //Production
                List<ProductRelease> productReleasesTomcat7 = new ArrayList<ProductRelease>();
                productReleasesTomcat7.add(tomcat7);
            
                List<ProductRelease> productReleasesPostgres8 = new ArrayList<ProductRelease>();
                productReleasesPostgres8.add(postgres84);
                        
                Tier tomcat7Tier = new Tier("tomcat7Tier", 1,1,1,productReleasesTomcat7);
                tomcat7Tier = tierDao.create(tomcat7Tier);
                Tier postgres8Tier = new Tier("postgres8Tier", 1,1,1,productReleasesPostgres8);
                postgres8Tier = tierDao.create(postgres8Tier);               
                       
                List<Tier> tiersProduction = new ArrayList<Tier>();
                tiersProduction.add(tomcat7Tier);
                //tiersProduction.add(postgres8Tier);
                
                
                //Environment
                Environment productionEnvironment = new Environment("production", 
                		environmentTypeJavaSpring,
                		tiersProduction);
                productionEnvironment = environmentDao.create(productionEnvironment);
                
                //AppicationType
                ApplicationType applicationTypeJavaSpring = new ApplicationType(
                		"Java-Spring Application", "Java-Spring Env description",
                		environmentTypeJavaSprings);
                applicationTypeJavaSpring 
            		= applicationTypeDao.create(applicationTypeJavaSpring); 
                
                //*********Application Release*************
                //War Application
                //Two artifacts: war file (WarFile Artifact) and 
                //descriptor file (descriptor File Artifact)
                //Ambos asociados a un productTypeAWS
                
                //War Application
               
                //HelloWorld Artifacts
                Artifact artifactWarHelloWorld = new Artifact("HelloWorldAWSWar", 
               		 "/opt/war/releases/80f5053b166c69d81697ba21113c673f8372aca0.war",
               		 artifactTypeWar,
               		 tomcat7);
                Artifact artifactDescriptorWarHelloWorld = new Artifact("HelloWorldAWSDescriptor", 
                		 "/opt/war/releases/80f5053b166c69d81697ba21113c673f8372aca0.xml",
                  		 artifactTypeDescriptorWar,
                  		 tomcat7);
                
                artifactWarHelloWorld = artifactDao.create(artifactWarHelloWorld);
                artifactDescriptorWarHelloWorld = 
                		artifactDao.create(artifactDescriptorWarHelloWorld);
                
                List<Artifact> artifactsHelloWorld = new ArrayList<Artifact>();
                artifactsHelloWorld.add(artifactDescriptorWarHelloWorld);
                artifactsHelloWorld.add(artifactWarHelloWorld);
                           
                ApplicationRelease warHelloWorld10 = new ApplicationRelease(
               		 "HelloWorld", "1.0","War application with no access to database description",
               		 applicationTypeJavaSpring, null, null,artifactsHelloWorld);
               
                warHelloWorld10 = applicationReleaseDao.create(warHelloWorld10);
                                 
        	} catch (AlreadyExistsEntityException e1) {
                throw new RuntimeException(e1);
            } catch (InvalidEntityException e1) {
                throw new RuntimeException(e1);
            }
        }        
        System.out.println("InitDbBootstrap. END");
    }

    /** {@inheritDoc} */
    @Override
    public void contextDestroyed(ServletContextEvent event) {
    	
    }

}

