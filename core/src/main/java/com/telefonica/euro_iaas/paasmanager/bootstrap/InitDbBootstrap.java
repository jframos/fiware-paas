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
import com.telefonica.euro_iaas.paasmanager.dao.ApplicationReleaseDao;
import com.telefonica.euro_iaas.paasmanager.dao.ApplicationTypeDao;
import com.telefonica.euro_iaas.paasmanager.dao.ArtifactDao;
import com.telefonica.euro_iaas.paasmanager.dao.ArtifactTypeDao;
import com.telefonica.euro_iaas.paasmanager.dao.EnvironmentDao;
import com.telefonica.euro_iaas.paasmanager.dao.EnvironmentInstanceDao;
import com.telefonica.euro_iaas.paasmanager.dao.EnvironmentTypeDao;
import com.telefonica.euro_iaas.paasmanager.dao.OSDao;
import com.telefonica.euro_iaas.paasmanager.dao.ProductInstanceDao;
import com.telefonica.euro_iaas.paasmanager.dao.ProductReleaseDao;
import com.telefonica.euro_iaas.paasmanager.dao.ProductTypeDao;
import com.telefonica.euro_iaas.paasmanager.dao.ServiceDao;
import com.telefonica.euro_iaas.paasmanager.dao.TierDao;
import com.telefonica.euro_iaas.paasmanager.dao.TierInstanceDao;
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
    public void contextInitialized(ServletContextEvent event) {
    	System.out.println("InitDbBootstrap. START");
    	WebApplicationContext ctx = WebApplicationContextUtils
                .getWebApplicationContext(event.getServletContext());

    	OSDao osDao = (OSDao) ctx.getBean("osDao");
        ProductReleaseDao productReleaseDao = (ProductReleaseDao) ctx.getBean("productReleaseDao");
        ProductTypeDao productTypeDao = (ProductTypeDao) ctx.getBean("productTypeDao");
        ProductInstanceDao productInstanceDao = (ProductInstanceDao) ctx.getBean ("productInstanceDao");
        
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
        EnvironmentInstanceDao environmentInstanceDao = 
        		(EnvironmentInstanceDao) ctx.getBean("environmentInstanceDao");
        
        TierDao tierDao = 
        		(TierDao) ctx.getBean("tierDao");
        TierInstanceDao tierInstanceDao = (TierInstanceDao) ctx.getBean("tierInstanceDao");
        
        ServiceDao serviceDao = 
        		(ServiceDao) ctx.getBean("serviceDao");
        try{
            osDao.load("95");
            osDao.load("94");
        } catch (EntityNotFoundException e) {
        	try{
        		
        		OS so1 = new OS("94", "Ubuntu", "Ubuntu 10.04", "10.04");
                OS so2 = new OS("95", "Debian", "Debian 5", "5");
                OS so3 = new OS("76", "Centos", "Centos 2.9", "2.9");
                
                so1 = osDao.create(so1);
                so2 = osDao.create(so2);
                so3 = osDao.create(so3);

                List<OS> supportedSSOO2 = Arrays.asList(so2);
  
  
                List<OS> supportedSSOO123 = Arrays.asList(so1);
                
                //ProductTypes
                ProductType productTypeFirewall = new ProductType("Firewall", 
                		"Firewall description");
                ProductType productTypeDatabase = new ProductType("Database", 
                		"database description");
                ProductType productTypeAWS = new ProductType("ApplicationWebServer", 
                		"Application Web Server description");
                ProductType productTypeLoadBalancer = new ProductType("LoadBalancer", 
                		"LoadBalancer description");
                ProductType productTypeNEP = new ProductType("NONEXISTENT", 
                		"NONEXISTENT description");
                
                productTypeFirewall = productTypeDao.create(productTypeFirewall);
                productTypeFirewall = productTypeDao.create(productTypeDatabase);
                productTypeAWS = productTypeDao.create(productTypeAWS);             
                productTypeLoadBalancer = productTypeDao.create(productTypeLoadBalancer);             
                productTypeNEP = productTypeDao.create(productTypeNEP);             
                
                //ArtifactTypes
                ArtifactType artifactTypeWar = new ArtifactType("war", 
                		"war description", productTypeAWS);
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
                
                //Non-existent product Release
                ProductRelease nonexistentProduct 
                	= new ProductRelease("nonexistent", "1.0", 
                			"nonexistent 1.0 description",null, null, 
                			supportedSSOO123, true, productTypeNEP);
                nonexistentProduct = productReleaseDao.create(nonexistentProduct); 
                
              //Tomcat Product Releases
              /*  Product tomcat = new Product("tomcat", "tomcat J2EE container");
                tomcat.addAttribute(new Attribute("port", "8080",
                "The listen port"));
                tomcat.addAttribute(new Attribute("ssl_port", "8443",
                "The ssl listen port"));
                tomcat = productDao.create(tomcat);*/

                ProductRelease tomcat7 = new ProductRelease("tomcat",
                        "7", "Tomcat server 7", null, null, 
                        supportedSSOO2, true, productTypeAWS);
                tomcat7 = productReleaseDao.create(tomcat7);
                
                ProductRelease tomcat6 = new ProductRelease("tomcat",
                        "6", "Tomcat server 6", null, null, 
                        supportedSSOO2, true, productTypeAWS);
                tomcat6 = productReleaseDao.create(tomcat6);
                
                ProductRelease tomcat5 = new ProductRelease("tomcat",
                        "5", "Tomcat server 5.5", null, null, 
                        supportedSSOO2, true, productTypeAWS);
                tomcat5 = productReleaseDao.create(tomcat5);
                
                // Jonas
                ProductRelease jonas7 = new ProductRelease("jonas",
                        "7", "Jonas 7", null, null, 
                        supportedSSOO2, true, productTypeAWS);
                jonas7 = productReleaseDao.create(jonas7);
                             
                // Servicemix 7
                ProductRelease servicemix7 = new ProductRelease("servicemix",
                        "7", "Servicemix 7", null, null, 
                        supportedSSOO2, true, productTypeAWS);
                servicemix7 = productReleaseDao.create(servicemix7);
                
                //Postgresql Product Releases
          /*      Product postgresql = new Product("postgresql",
                        "db manager");
                postgresql.addAttribute(new Attribute("username", "postgres",
                        "The administrator usename"));
                postgresql.addAttribute(new Attribute("password", "postgres",
                        "The administrator password"));
                postgresql = productDao.create(postgresql);*/

                ProductRelease postgres84 = new ProductRelease("postgresql",
                        "8.4", "postgresql 8.4", null, null, 
                        supportedSSOO2, true, productTypeDatabase);
                postgres84 = productReleaseDao.create(postgres84);
                
  
                ProductRelease postgres83 = new ProductRelease("postgresql",
                        "8.3", "postgresql 8.3", null, null, 
                        supportedSSOO2, true, productTypeDatabase);
                postgres83 = productReleaseDao.create(postgres83);
                
              

                ProductRelease test01 = new ProductRelease("test",
                        "0.1", "blah blah blah", null,null,supportedSSOO2, true, productTypeAWS);
                test01 = productReleaseDao.create(test01);
                
                // GIt y nodejs
                ProductRelease nodejs = new ProductRelease("nodejs",
                        "0.6.15", "nodejs product", null,null,supportedSSOO2, true, productTypeAWS);
                nodejs = productReleaseDao.create(nodejs);
                
                ProductRelease git = new ProductRelease("git",
                        "1.7", "git product", null,null,supportedSSOO2, true, productTypeAWS);
                git = productReleaseDao.create(git);
                
                //ProductReleases
                
                
                ProductRelease firewall1 = new ProductRelease("firewall", "1.0", "firewall 1.0 description",
            			null, null, supportedSSOO123, true, productTypeFirewall);
                firewall1 = productReleaseDao.create(firewall1); 
                ProductRelease haproxy10 = new ProductRelease("haproxy", "1.0", "haproxy 1.0 description",
            			null, null, supportedSSOO123, true, productTypeLoadBalancer);
                haproxy10 = productReleaseDao.create(haproxy10); 
                ProductRelease jbossx = new ProductRelease("jboss", "x.0", "jboss x.0 description",
            			null, null, supportedSSOO123, true, productTypeLoadBalancer);
                jbossx = productReleaseDao.create(jbossx);
                
                //4Caast Product
                ProductRelease mysql51 = new ProductRelease("mysql", "5.1", "mysql 5.1 description",
            			null, null, supportedSSOO123, true, productTypeDatabase);
                mysql51 = productReleaseDao.create(mysql51); 
                ProductRelease mysqlsql10 = new ProductRelease("mysqlsql", "1.0", "mysqlsql 1.0 description",
            			null, null, supportedSSOO123, true, productTypeDatabase);
                mysqlsql10 = productReleaseDao.create(mysqlsql10);
                ProductRelease JONAS10 = new ProductRelease("JONAS", "1.0", "JONAS 1.0 description",
            			null, null, supportedSSOO123, true, productTypeLoadBalancer);
                JONAS10 = productReleaseDao.create(JONAS10);
                ProductRelease POSTGRES10 = new ProductRelease("POSTGRESQL", "1.0", "postgres 1.0 description",
            			null, null, supportedSSOO123, true, productTypeDatabase);
                POSTGRES10 = productReleaseDao.create(POSTGRES10); 
                
                ProductRelease tomcat_PIC = new ProductRelease("tomcat_PIC", "0.0.3", "tomcat_PIC 0.0.3 description",
            			null, null, supportedSSOO123, true, productTypeAWS);
                tomcat_PIC = productReleaseDao.create(tomcat_PIC); 
                
                ProductRelease postgresql_PIC = new ProductRelease("postgresql_PIC", "0.0.2", "postgresql_PIC 0.0.2 description",
            			null, null, supportedSSOO123, true, productTypeDatabase);
                postgresql_PIC = productReleaseDao.create(postgresql_PIC); 
                
                ProductRelease postgresql_PIC003 = new ProductRelease("postgresql_PIC", "0.0.3", "postgresql_PIC 0.0.3 description",
            			null, null, supportedSSOO123, true, productTypeDatabase);
                postgresql_PIC003 = productReleaseDao.create(postgresql_PIC003); 
                
                ProductRelease JOnAS_PIC = new ProductRelease("JOnAS_PIC", "2.0.0", "JOnAS_PIC 2.0.0 description",
            			null, null, supportedSSOO123, true, productTypeAWS);
                JOnAS_PIC = productReleaseDao.create(JOnAS_PIC); 
                
                //List ProductReleases
                List<ProductRelease> productReleasesTestingTier 
                	= new ArrayList<ProductRelease>();
                
                tomcat7 = productReleaseDao.load("tomcat-7");
                postgres84 = productReleaseDao.load("postgresql-8.4");
                
                productReleasesTestingTier.add(tomcat7);
                productReleasesTestingTier.add(postgres84);
                
                List<ProductRelease> productReleasesTomcat7 = new ArrayList<ProductRelease>();
                productReleasesTomcat7.add(tomcat7);
            
                List<ProductRelease> productReleasesPostgres8 = new ArrayList<ProductRelease>();
                productReleasesPostgres8.add(postgres84);
                
                List<ProductRelease> productReleasesMysql51 = new ArrayList<ProductRelease>();
                productReleasesMysql51.add(mysql51);
            
                List<ProductRelease> productReleasesHaproxy10 = new ArrayList<ProductRelease>();
                productReleasesHaproxy10.add(haproxy10);
                
                //Tiers
                Tier tomcat7postgres8Tier = new Tier("tomcat7postgres8Tier", 1,1,1,productReleasesTestingTier);
                tomcat7postgres8Tier = tierDao.create(tomcat7postgres8Tier);               
                List<Tier> tiersTesting = new ArrayList<Tier>();
                tiersTesting.add(tomcat7postgres8Tier);
                
                Tier tomcat7Tier = new Tier("tomcat7Tier", 1,1,1,productReleasesTomcat7);
                tomcat7Tier = tierDao.create(tomcat7Tier);
                List<Tier> tiersProduction = new ArrayList<Tier>();
                tiersProduction.add(tomcat7Tier);
                
                Tier postgres8Tier = new Tier("postgres8Tier", 1,1,1,productReleasesPostgres8);
                postgres8Tier = tierDao.create(postgres8Tier);               
                List<Tier> tiersProduction2vms = new ArrayList<Tier>();
                tiersProduction2vms.add(tomcat7Tier);
                tiersProduction2vms.add(tomcat7Tier);
                //tiersProduction2vms.add(postgres8Tier);
               
                //4CaastTiers
                Tier mysql51Tier = new Tier("mysql51Tier", 1,1,1,productReleasesMysql51);
                mysql51Tier = tierDao.create(mysql51Tier);               
                
                Tier haproxy10Tier = new Tier("haproxy10Tier", 1,1,1,productReleasesHaproxy10);
                haproxy10Tier = tierDao.create(haproxy10Tier);               
                
                List<Tier> tiers4Caast3VMs = new ArrayList<Tier>();
                tiers4Caast3VMs.add(haproxy10Tier);
                tiers4Caast3VMs.add(mysql51Tier);
                tiers4Caast3VMs.add(tomcat7Tier);               
                
                //ENVIRONMENT
                //Environment=production (1vm= tomcat)
                Environment productionEnvironment = new Environment("production", 
                		environmentTypeJavaSpring,
                		tiersProduction);
                productionEnvironment = environmentDao.create(productionEnvironment);
                
                //Environment=production2vms (2vm; 1 vm tomcat; 1vm postgres8)
                Environment production2vmsEnvironment = new Environment("production2vms", 
                		environmentTypeJavaSpring,
                		tiersProduction2vms);
                production2vmsEnvironment = environmentDao.create(production2vmsEnvironment);
                
                //Environment=testing (1vm = tomcat + postgres8)
                Environment testingEnvironment = new Environment("testing", 
                		environmentTypeJavaSpring,
                		tiersTesting);
                testingEnvironment = environmentDao.create(testingEnvironment);
                
                
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
                
                Artifact artifactWarHelloWorld = new Artifact("war", 
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
                //artifactsHelloWorld.add(artifactDescriptorWarHelloWorld);
                artifactsHelloWorld.add(artifactWarHelloWorld);
                     
                //War Application 4Caast
                ProductRelease warHelloWorld10 = new ProductRelease("application", 
                		"1.0",
                		"War application with no access to database description",
            			null, null, supportedSSOO123, true, productTypeAWS);
                warHelloWorld10 = productReleaseDao.create(warHelloWorld10); 
               
               
                //HelloWorld Artifacts         
                ApplicationRelease appWarHelloWorld10 = new ApplicationRelease(
               		 "HelloWorld", "1.0","War application with no access to database description",
               		 applicationTypeJavaSpring, null, null);
               
                appWarHelloWorld10 = applicationReleaseDao
                		.create(appWarHelloWorld10);
                
                //Provisoning an environmentInstance
        		
                //EnvironmentInstance
                Environment environment4CaastTomcatHaproxyMysql = 
                		new Environment("4CaastTPCW", 
                        		environmentTypeJavaSpring, tiers4Caast3VMs);
                
                environment4CaastTomcatHaproxyMysql = 
                		environmentDao.create(environment4CaastTomcatHaproxyMysql);
                environment4CaastTomcatHaproxyMysql = environmentDao.load("4CaastTPCW");
                
                
        	} catch (AlreadyExistsEntityException e1) {
                throw new RuntimeException(e1);
            } catch (EntityNotFoundException e1) {
                throw new RuntimeException(e1);
            } catch (InvalidEntityException e1) {
                throw new RuntimeException(e1);
            }
        }
        
        System.out.println("InitDbBootstrap. END");
    }

    /** {@inheritDoc} */
    public void contextDestroyed(ServletContextEvent event) {
    	
    }
}

