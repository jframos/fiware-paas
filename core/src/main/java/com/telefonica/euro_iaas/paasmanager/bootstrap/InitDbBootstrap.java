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
import com.telefonica.euro_iaas.paasmanager.model.Attribute;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentType;
import com.telefonica.euro_iaas.paasmanager.model.OS;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.ProductType;
import com.telefonica.euro_iaas.paasmanager.model.Service;
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
        		OS so1 = new OS("95", "Debian", "Debian 5", "5");
                OS so2 = new OS("94", "Ubuntu", "Ubuntu 10.04", "10.04");
                so1 = osDao.create(so1);
                so2 = osDao.create(so2);
                List<OS> supportedSSOO1 = Arrays.asList(so1);
                List<OS> supportedSSOO2 = Arrays.asList(so2);
                List<OS> supportedSSOO12 = Arrays.asList(so1,so2);
                
                ProductType productTypeFirewall = new ProductType("Firewall", 
                		"Firewall description");
                productTypeFirewall = productTypeDao.create(productTypeFirewall);
                ProductType productTypeDatabase = new ProductType("Database", 
                		"database description");
                productTypeFirewall = productTypeDao.create(productTypeDatabase);
                ProductType productTypeAWS = new ProductType("ApplicationWebServer", 
                		"Application Web Server description");
                productTypeAWS = productTypeDao.create(productTypeAWS);             
                ProductType productTypeLoadBalancer = new ProductType("LoadBalancer", 
                		"LoadBalancer description");
                productTypeLoadBalancer = productTypeDao.create(productTypeLoadBalancer);             
                
                ArtifactType artifactTypeWar = new ArtifactType("AWSWar", 
                		"AWSWar description", productTypeAWS);
                artifactTypeWar = artifactTypeDao.create(artifactTypeWar);                             
                ArtifactType artifactTypeProperties = new ArtifactType("properties", 
                		"Properties description", productTypeAWS);
                artifactTypeProperties = artifactTypeDao.create(artifactTypeProperties);                             
                ArtifactType artifactTypeEar = new ArtifactType("AWSEar", 
                		"AWSEar description", productTypeAWS);
                artifactTypeEar = artifactTypeDao.create(artifactTypeEar);                             
                ArtifactType artifactTypeSql = new ArtifactType("sql", 
                		"Sql description", productTypeDatabase);
                artifactTypeSql = artifactTypeDao.create(artifactTypeSql);                             
                
                EnvironmentType environmentTypeJavaSpring = new EnvironmentType(
                		"Java-Spring Environment", "Java-Spring Env description");
                environmentTypeJavaSpring 
                	= environmentTypeDao.create(environmentTypeJavaSpring);                             
                List<EnvironmentType> environmentTypeJavaSprings 
                	= Arrays.asList(environmentTypeJavaSpring);
                
                ApplicationType applicationTypeJavaSpring = new ApplicationType(
                		"Java-Spring Application", "Java-Spring Env description",
                		environmentTypeJavaSprings);
                applicationTypeJavaSpring 
            		= applicationTypeDao.create(applicationTypeJavaSpring); 
                
                ProductRelease tomcat7 = new ProductRelease("tomcat", "7.0", "tomcat 7.0 description",
            			null, null, supportedSSOO12, true, productTypeAWS);
                tomcat7 = productReleaseDao.create(tomcat7); 
                ProductRelease mysql5 = new ProductRelease("mysql", "5.0", "mysql 7.0 description",
            			null, null, supportedSSOO12, true, productTypeDatabase);
                mysql5 = productReleaseDao.create(mysql5); 
                ProductRelease firewall1 = new ProductRelease("firewall", "1.0", "firewall 1.0 description",
            			null, null, supportedSSOO12, true, productTypeFirewall);
                firewall1 = productReleaseDao.create(firewall1); 
                ProductRelease haproxy1 = new ProductRelease("haproxy", "1.0", "haproxy 1.0 description",
            			null, null, supportedSSOO12, true, productTypeLoadBalancer);
                haproxy1 = productReleaseDao.create(haproxy1); 
                ProductRelease jbossx = new ProductRelease("jboss", "x.0", "jboss x.0 description",
            			null, null, supportedSSOO12, true, productTypeLoadBalancer);
                jbossx = productReleaseDao.create(jbossx);
                
                //Testing
                Tier tomcat7Tier = new Tier("tomcat7Tier", 1,1,1,null,tomcat7);
                tomcat7Tier = tierDao.create(tomcat7Tier);
                Tier mysql5Tier = new Tier("mysql5Tier", 1,1,1,null,mysql5);
                mysql5Tier = tierDao.create(mysql5Tier);
                
                List<Tier> tiersTesting = new ArrayList<Tier>();
                tiersTesting.add(tomcat7Tier);
                tiersTesting.add(mysql5Tier);
                
                Environment testingEnvironment = new Environment("testing", 
                		environmentTypeJavaSpring,
                		tiersTesting);
                testingEnvironment = environmentDao.create(testingEnvironment);
                
                //Production
                Tier mysql5Tier2 = new Tier("mysql5Tier", 5,1,1,null,mysql5);
                mysql5Tier2 = tierDao.create(mysql5Tier2);
                List<Tier> tiersProduction = new ArrayList<Tier>();
                tiersProduction.add(tomcat7Tier);
                tiersProduction.add(mysql5Tier2);
                
                Environment productionEnvironment = new Environment("production", 
                		environmentTypeJavaSpring,
                		tiersProduction);
                productionEnvironment = environmentDao.create(productionEnvironment);
                
                //Application Release
                 ArtifactType artifactTypeSqlDatabase = new ArtifactType("SqlScript",
                		 "SqlScript Description",
                		 productTypeDatabase);
                 artifactTypeSqlDatabase = artifactTypeDao.create(artifactTypeSqlDatabase); 
                 ArtifactType artifactTypeWarAWS = new ArtifactType("war",
                		 "war Description",
                		 productTypeAWS);
                 artifactTypeWarAWS = artifactTypeDao.create(artifactTypeWarAWS); 
                 List<ArtifactType> artifactTypeSdc = new ArrayList<ArtifactType>();
                 artifactTypeSdc.add(artifactTypeSqlDatabase);
                 artifactTypeSdc.add(artifactTypeWarAWS);
                 
                 Artifact artifactSqlScriptMysql5 = new Artifact("sqlArtifact", 
                		 "/path/To/Sql/Script",
                		 artifactTypeSqlDatabase,
                		 mysql5);
                 artifactSqlScriptMysql5 = artifactDao.create(artifactSqlScriptMysql5);        
                 Artifact artifactWarTomcat7 = new Artifact("SDC War", 
                		 "/path/To/War",
                		 artifactTypeWar,
                		 tomcat7);
                 artifactWarTomcat7 = artifactDao.create(artifactWarTomcat7);
                 
                 ApplicationRelease sdc10 = new ApplicationRelease(
                		 "sdc", "1.0","sdc description",
                		 applicationTypeJavaSpring, null, null,artifactTypeSdc);
                 sdc10 = applicationReleaseDao.create(sdc10);
                 
                 
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

