/**
 * Copyright 2014 Telefonica Investigación y Desarrollo, S.A.U <br>
 * This file is part of FI-WARE project.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 * </p>
 * <p>
 * You may obtain a copy of the License at:<br>
 * <br>
 * http://www.apache.org/licenses/LICENSE-2.0
 * </p>
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * </p>
 * <p>
 * See the License for the specific language governing permissions and limitations under the License.
 * </p>
 * <p>
 * For those usages not covered by the Apache version 2.0 License please contact with opensource@tid.es
 * </p>
 */

package com.telefonica.euro_iaas.paasmanager.bootstrap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.dao.ApplicationReleaseDao;
import com.telefonica.euro_iaas.paasmanager.dao.ApplicationTypeDao;
import com.telefonica.euro_iaas.paasmanager.dao.ArtifactDao;
import com.telefonica.euro_iaas.paasmanager.dao.ArtifactTypeDao;
import com.telefonica.euro_iaas.paasmanager.dao.AttributeDao;
import com.telefonica.euro_iaas.paasmanager.dao.EnvironmentDao;
import com.telefonica.euro_iaas.paasmanager.dao.EnvironmentInstanceDao;
import com.telefonica.euro_iaas.paasmanager.dao.OSDao;
import com.telefonica.euro_iaas.paasmanager.dao.ProductInstanceDao;
import com.telefonica.euro_iaas.paasmanager.dao.ProductReleaseDao;
import com.telefonica.euro_iaas.paasmanager.dao.ProductTypeDao;
import com.telefonica.euro_iaas.paasmanager.dao.ServiceDao;
import com.telefonica.euro_iaas.paasmanager.dao.TierDao;
import com.telefonica.euro_iaas.paasmanager.dao.TierInstanceDao;
import com.telefonica.euro_iaas.paasmanager.dao.sdc.ProductReleaseSdcDao;
import com.telefonica.euro_iaas.paasmanager.exception.SdcException;
import com.telefonica.euro_iaas.paasmanager.model.ApplicationRelease;
import com.telefonica.euro_iaas.paasmanager.model.ApplicationType;
import com.telefonica.euro_iaas.paasmanager.model.Artifact;
import com.telefonica.euro_iaas.paasmanager.model.ArtifactType;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.OS;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.ProductType;
import com.telefonica.euro_iaas.paasmanager.model.Tier;

/**
 * Populates data base with synthetic data to emulate the preconditions of paas manager
 * 
 * @author Jesus M. Movilla
 * @version $Id: $
 */
// TODO delete this class when the preconditions are done.
public class InitDbBootstrap implements ServletContextListener {

    private static Logger log = Logger.getLogger(InitDbBootstrap.class);

    /** {@inheritDoc} */
    public void contextDestroyed(ServletContextEvent event) {

    }

    /** {@inheritDoc} */
    public void contextInitialized(ServletContextEvent event) {
        log.debug("InitDbBootstrap. START");
        WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());

        OSDao osDao = (OSDao) ctx.getBean("osDao");
        ProductReleaseDao productReleaseDao = (ProductReleaseDao) ctx.getBean("productReleaseDao");
        ProductTypeDao productTypeDao = (ProductTypeDao) ctx.getBean("productTypeDao");
        ProductInstanceDao productInstanceDao = (ProductInstanceDao) ctx.getBean("productInstanceDao");
        AttributeDao attributeDao = (AttributeDao) ctx.getBean("attributeDao");

        ApplicationReleaseDao applicationReleaseDao = (ApplicationReleaseDao) ctx.getBean("applicationReleaseDao");
        ApplicationTypeDao applicationTypeDao = (ApplicationTypeDao) ctx.getBean("applicationTypeDao");

        ArtifactTypeDao artifactTypeDao = (ArtifactTypeDao) ctx.getBean("artifactTypeDao");
        ArtifactDao artifactDao = (ArtifactDao) ctx.getBean("artifactDao");

        EnvironmentDao environmentDao = (EnvironmentDao) ctx.getBean("environmentDao");
        EnvironmentInstanceDao environmentInstanceDao = (EnvironmentInstanceDao) ctx.getBean("environmentInstanceDao");

        TierDao tierDao = (TierDao) ctx.getBean("tierDao");
        TierInstanceDao tierInstanceDao = (TierInstanceDao) ctx.getBean("tierInstanceDao");

        ServiceDao serviceDao = (ServiceDao) ctx.getBean("serviceDao");

        ProductReleaseSdcDao productReleaseSdcDao = (ProductReleaseSdcDao) ctx.getBean("productReleaseSdcDao");


       /* List<ProductRelease> productReleases = new ArrayList<ProductRelease>();
=======
     /*   List<ProductRelease> productReleases = new ArrayList<ProductRelease>();
>>>>>>> 6b6090e4bc049aedcdc17e08d97dc30e5da4729a
        try {
            productReleases = productReleaseSdcDao.findAll();

            for (int i = 0; i < productReleases.size(); i++) {
                ProductRelease pRelease = productReleases.get(i);
                List<OS> ssoo = new ArrayList<OS>();
                ssoo = pRelease.getSupportedOOSS();

                if (ssoo != null) {
                    List<OS> supportedOOSS = new ArrayList<OS>();
                    for (int j = 0; j < ssoo.size(); j++) {

                        OS so = null;
                        try {
                            so = osDao.load(ssoo.get(j).getOsType());
                        } catch (EntityNotFoundException e1) {
                            so = osDao.create(ssoo.get(j));
                        }
                        supportedOOSS.add(so);
                    }
                    pRelease.setSupportedOOSS(supportedOOSS);
                }

                ProductRelease productRelease = null;
                try {
                    productRelease = productReleaseDao.load(pRelease.getName());
                    productRelease = productReleaseDao.update(productRelease);
                } catch (EntityNotFoundException ex4) {
                    productRelease = productReleaseDao.create(pRelease);
                }
            }
            // Taking the ProductRelease from SDC *******************
        } catch (SdcException ex1) {
            String msg = " Impossible to recover ProductReleases from SDC. "
                    + "Either connection problem or Product Release Resource problem ";
            log.error(msg + ex1);
        } catch (AlreadyExistsEntityException ex2) {
            log.error("alread exists " + ex2);
        } catch (Exception ex4) {
            log.error("generic exception " + ex4);
<<<<<<< HEAD
        }
*/

     /*   try {
            osDao.load("95");
            osDao.load("94");
        } catch (EntityNotFoundException e) {
            try {

                OS so1 = new OS("94", "Ubuntu", "Ubuntu 10.04", "10.04");
                OS so2 = new OS("95", "Debian", "Debian 5", "5");
                OS so3 = new OS("76", "Centos", "Centos 2.9", "2.9");

                so1 = osDao.create(so1);
                so2 = osDao.create(so2);
                so3 = osDao.create(so3);

                List<OS> supportedSSOO2 = Arrays.asList(so2);

                List<OS> supportedSSOO123 = Arrays.asList(so1);

                // ProductTypes
                ProductType productTypeFirewall = new ProductType("Firewall", "Firewall description");
                ProductType productTypeDatabase = new ProductType("Database", "database description");
                ProductType productTypeAWS = new ProductType("ApplicationWebServer",
                        "Application Web Server description");
                ProductType productTypeLoadBalancer = new ProductType("LoadBalancer", "LoadBalancer description");
                ProductType productTypeNEP = new ProductType("NONEXISTENT", "NONEXISTENT description");

                productTypeFirewall = productTypeDao.create(productTypeFirewall);
                productTypeFirewall = productTypeDao.create(productTypeDatabase);
                productTypeAWS = productTypeDao.create(productTypeAWS);
                productTypeLoadBalancer = productTypeDao.create(productTypeLoadBalancer);
                productTypeNEP = productTypeDao.create(productTypeNEP);

                // ArtifactTypes
                ArtifactType artifactTypeWar = new ArtifactType("war", "war description", productTypeAWS);
                ArtifactType artifactTypeDescriptorWar = new ArtifactType("AWSDescriptor", "AWS XML Descriptor",
                        productTypeAWS);
                ArtifactType artifactTypeProperties = new ArtifactType("properties", "Properties description",
                        productTypeAWS);
                ArtifactType artifactTypeEar = new ArtifactType("AWSEar", "AWSEar description", productTypeAWS);
                ArtifactType artifactTypeSql = new ArtifactType("sql", "Sql description", productTypeDatabase);

                artifactTypeWar = artifactTypeDao.create(artifactTypeWar);
                artifactTypeDescriptorWar = artifactTypeDao.create(artifactTypeDescriptorWar);
                artifactTypeProperties = artifactTypeDao.create(artifactTypeProperties);
                artifactTypeEar = artifactTypeDao.create(artifactTypeEar);
                artifactTypeSql = artifactTypeDao.create(artifactTypeSql);

                ProductRelease tomcat6 = null;
                ProductRelease postgres84 = null;
                ProductRelease haproxy10 = null;
                ProductRelease mysqlsql10 = null;
                ProductRelease nodejs0615 = null;
                ProductRelease mongodbconfig223 = null;
                ProductRelease mongos223 = null;
                ProductRelease contextbroker100 = null;
                ProductRelease mongodbshard223 = null;

                try {
                    tomcat6 = productReleaseDao.load("tomcat-6");
                    postgres84 = productReleaseDao.load("postgresql-8.4");
                    haproxy10 = productReleaseDao.load("haproxy-1.0");
                    mongodbconfig223 = productReleaseDao.load("mongodbconfig-2.2.3");
                    mongos223 = productReleaseDao.load("mongos-2.2.3");
                    contextbroker100 = productReleaseDao.load("contextbroker-1.0.0");
                    mongodbshard223 = productReleaseDao.load("mongodbshard-2.2.3");
                    nodejs0615 = productReleaseDao.load("nodejs-0.6.15");
                } catch (EntityNotFoundException e1) {
                    String msg = " Impossible to recover ProductReleases from PaasManager Database. "
                            + "Either connection problem or Product Release Resource problem";
                    log.error(msg);
                    throw new RuntimeException(e1);
                }

                // PoductReleases List
                List<ProductRelease> productReleasesTomcat6 = new ArrayList<ProductRelease>();
                productReleasesTomcat6.add(tomcat6);

                List<ProductRelease> productReleasesPostgres8 = new ArrayList<ProductRelease>();
                productReleasesPostgres8.add(postgres84);

                // List<ProductRelease> productReleasesMysql51 = new
                // ArrayList<ProductRelease>();
                // productReleasesMysql51.add(mysql51);

                List<ProductRelease> productReleasesHaproxy10 = new ArrayList<ProductRelease>();
                productReleasesHaproxy10.add(haproxy10);

                List<ProductRelease> productReleasesTestingTier = new ArrayList<ProductRelease>();
                // productReleasesTestingTier.add(tomcat7);
                productReleasesTestingTier.add(tomcat6);
                productReleasesTestingTier.add(postgres84);

                // Tiers
                Tier tomcat7postgres8Tier = new Tier("tomcat7postgres8Tiertest", 1, 1, 1, productReleasesTestingTier);
                tomcat7postgres8Tier.setImage("44dcdba3-a75d-46a3-b209-5e9035d2435e");
                tomcat7postgres8Tier.setIcono("icono");
                tomcat7postgres8Tier.setFlavour("1");
                tomcat7postgres8Tier.setFloatingip("yes");
                tomcat7postgres8Tier.setKeypair("testpaas");

                tomcat7postgres8Tier = tierDao.create(tomcat7postgres8Tier);

                Set<Tier> tiersTesting = new HashSet<Tier>();
                tiersTesting.add(tomcat7postgres8Tier);

                /*
                 * Tier tomcat7Tier = new Tier("tomcat7Tier", 1, 1, 1, productReleasesTomcat7);
                 * tomcat7Tier.setImage("44dcdba3-a75d-46a3-b209-5e9035d2435e"); tomcat7Tier.setIcono("icono");
                 * tomcat7Tier.setFlavour("1"); tomcat7Tier.setFloatingip("yes"); tomcat7Tier.setKeypair("testpaas");
                 * tomcat7Tier = tierDao.create(tomcat7Tier);
                 */

       /*         Tier tomcat6Tier = new Tier("tomcat6Tier", 1, 1, 1, productReleasesTomcat6);
                tomcat6Tier.setImage("44dcdba3-a75d-46a3-b209-5e9035d2435e");
                tomcat6Tier.setIcono("icono");
                tomcat6Tier.setFlavour("1");
                tomcat6Tier.setFloatingip("yes");
                tomcat6Tier.setKeypair("testpaas");

                tomcat6Tier = tierDao.create(tomcat6Tier);

                Set<Tier> tiersProduction = new HashSet<Tier>();
                // tiersProduction.add(tomcat7Tier);
                tiersProduction.add(tomcat6Tier);

                Tier postgres8Tier = new Tier("postgres8Tier", 1, 1, 1, productReleasesPostgres8);
                postgres8Tier = tierDao.create(postgres8Tier);
                Set<Tier> tiersProduction2vms = new HashSet<Tier>();
                // tiersProduction2vms.add(tomcat7Tier);
                // tiersProduction2vms.add(tomcat7Tier);
                tiersProduction2vms.add(tomcat6Tier);
                tiersProduction2vms.add(tomcat6Tier);

                // 4CaastTiers
                /*
                 * Tier mysql51Tier = new Tier("mysql51Tier", 1, 1, 1, productReleasesMysql51); mysql51Tier =
                 * tierDao.create(mysql51Tier);
                 */

    /*            Tier haproxy10Tier = new Tier("haproxy10Tier", 1, 1, 1, productReleasesHaproxy10);
                haproxy10Tier.setImage("sdc-template-paas");
                haproxy10Tier.setIcono("icono");
                haproxy10Tier.setFlavour("1");
                haproxy10Tier.setFloatingip("yes");
                haproxy10Tier.setKeypair("testpaas");

                haproxy10Tier = tierDao.create(haproxy10Tier);

                Set<Tier> tiers4Caast3VMs = new HashSet<Tier>();
                tiers4Caast3VMs.add(haproxy10Tier);
                // tiers4Caast3VMs.add(mysql51Tier);
                // tiers4Caast3VMs.add(tomcat7Tier);
                tiers4Caast3VMs.add(tomcat6Tier);

                // ENVIRONMENT
                // Environment=production (1vm= tomcat)
                String org = "FIWARE";
                String vdc = "6571e3422ad84f7d828ce2f30373b3d4";
                Environment productionEnvironment = new Environment("production", tiersProduction,
                        "Environment for production", org, vdc);
                productionEnvironment = environmentDao.create(productionEnvironment);

                // Environment=production2vms (2vm; 1 vm tomcat; 1vm postgres8)
                Environment production2vmsEnvironment = new Environment("production2vms", tiersProduction2vms,
                        "Environment for production 2 vms", org, vdc);
                production2vmsEnvironment = environmentDao.create(production2vmsEnvironment);

                // Environment=testing (1vm = tomcat + postgres8)
                Environment testingEnvironment = new Environment("testing", tiersTesting, "Environment for testing ",
                        org, vdc);
                testingEnvironment = environmentDao.create(testingEnvironment);

                Tier abstractTier = new Tier("tomcat7postgres8Tier", 1, 1, 1, productReleasesTestingTier);
                abstractTier.setImage("44dcdba3-a75d-46a3-b209-5e9035d2435e");
                abstractTier.setIcono("icono");
                abstractTier.setFlavour("2");

                abstractTier = tierDao.create(abstractTier);
                Set<Tier> tiersTesting2 = new HashSet<Tier>();
                tiersTesting2.add(abstractTier);

                Environment abstractEnvironment = new Environment("2testing", tiersTesting2,
                        "abstractEnvironment for testing ");
                abstractEnvironment.setOrg("FIWARE");
                abstractEnvironment = environmentDao.create(abstractEnvironment);

                List<ProductRelease> productReleasesNodejs = new ArrayList<ProductRelease>();
                productReleasesNodejs.add(nodejs0615);

                Tier nodeTier = new Tier("Nodejstier", 2, 1, 1, productReleasesNodejs);
                nodeTier.setImage("44dcdba3-a75d-46a3-b209-5e9035d2435e");
                nodeTier.setIcono("http://nodejs-cloud.com/img/64px/nodejs.png");
                nodeTier.setFlavour("2");
                nodeTier = tierDao.create(nodeTier);

                List<ProductRelease> productReleasesMysql = new ArrayList<ProductRelease>();
                productReleasesMysql.add(mysqlsql10);

                Tier mysqlTier = new Tier("mysqlTier", 1, 1, 1, productReleasesMysql);
                mysqlTier.setImage("44dcdba3-a75d-46a3-b209-5e9035d2435e");
                mysqlTier.setIcono("http://erlycoder.com/themes/site4fast/images/mysql.png");
                mysqlTier.setFlavour("2");
                mysqlTier = tierDao.create(mysqlTier);

                Set<Tier> tiersNodeMysql = new HashSet<Tier>();
                tiersNodeMysql.add(mysqlTier);
                tiersNodeMysql.add(nodeTier);
                Environment nodemysql = new Environment("NodejsMysql", tiersNodeMysql, "Environment Nodejs Mysql ");
                nodemysql.setOrg("FIWARE");
                nodemysql = environmentDao.create(nodemysql);

                List<ProductRelease> productReleasesMongoConfig = new ArrayList<ProductRelease>();
                productReleasesMongoConfig.add(mongodbconfig223);

                Tier tierConfig = new Tier("mongoconfig", 1, 1, 1, productReleasesMongoConfig);
                tierConfig.setImage("44dcdba3-a75d-46a3-b209-5e9035d2435e");
                tierConfig.setIcono("http://www.bloggure.info/wp-content/uploads/2012/02/mongo.png");
                tierConfig.setFlavour("2");
                tierConfig = tierDao.create(tierConfig);

                List<ProductRelease> productReleasesMongoShard = new ArrayList<ProductRelease>();
                productReleasesMongoShard.add(mongodbshard223);
                Tier tierShard = new Tier("mongoshard", 3, 1, 1, productReleasesMongoShard);
                tierShard.setImage("44dcdba3-a75d-46a3-b209-5e9035d2435e");
                tierShard.setIcono("http://www.bloggure.info/wp-content/uploads/2012/02/mongo.png");
                tierShard.setFlavour("2");
                tierShard = tierDao.create(tierShard);

                List<ProductRelease> productReleasesMongoContext = new ArrayList<ProductRelease>();
                productReleasesMongoContext.add(mongos223);
                productReleasesMongoContext.add(contextbroker100);
                Tier tierContextBroker = new Tier("contextbrokr", 1, 1, 1, productReleasesMongoContext);
                tierContextBroker.setImage("44dcdba3-a75d-46a3-b209-5e9035d2435e");
                tierContextBroker
                        .setIcono("http://catalogue.fi-ware.eu/sites/default/files/styles/enabler_icon_large/public/orion.png");
                tierContextBroker.setFlavour("2");
                tierContextBroker = tierDao.create(tierContextBroker);

                Set<Tier> tiersContextbrokertemplate = new HashSet<Tier>();
                tiersContextbrokertemplate.add(tierConfig);
                tiersContextbrokertemplate.add(tierShard);
                tiersContextbrokertemplate.add(tierContextBroker);

                Environment contextbrokertemplate = new Environment("contextbroker-enabler",
                        tiersContextbrokertemplate, "Environment contextbroker-enabler");
                contextbrokertemplate.setOrg("FIWARE");
                contextbrokertemplate = environmentDao.create(contextbrokertemplate);

                Tier tierConfig2 = new Tier("mongodbconfig", 1, 1, 1, productReleasesMongoConfig);
                tierConfig2.setImage("44dcdba3-a75d-46a3-b209-5e9035d2435e");
                tierConfig2.setIcono("http://www.bloggure.info/wp-content/uploads/2012/02/mongo.png");
                tierConfig2.setFlavour("2");
                tierConfig2.setVdc("cd593e796acb4ac5821e208ff26802bd");
                tierConfig2.setKeypair("passmanager-fermin");
                tierConfig2.setFloatingip("true");
                tierConfig2.setEnviromentName("smartcity-lights");
                tierConfig2 = tierDao.create(tierConfig2);

                Tier tierShard2 = new Tier("mongodbshard", 3, 1, 1, productReleasesMongoShard);
                tierShard2.setImage("44dcdba3-a75d-46a3-b209-5e9035d2435e");
                tierShard2.setIcono("http://www.bloggure.info/wp-content/uploads/2012/02/mongo.png");
                tierShard2.setFlavour("2");
                tierShard2.setVdc("cd593e796acb4ac5821e208ff26802bd");
                tierShard2.setKeypair("passmanager-fermin");
                tierShard2.setFloatingip("true");
                tierShard2.setEnviromentName("smartcity-lights");
                tierShard2 = tierDao.create(tierShard2);

                Tier tierContextBroker2 = new Tier("mongoscontextbrokr", 1, 1, 1, productReleasesMongoContext);
                tierContextBroker2.setImage("44dcdba3-a75d-46a3-b209-5e9035d2435e");
                tierContextBroker2
                        .setIcono("http://catalogue.fi-ware.eu/sites/default/files/styles/enabler_icon_large/public/orion.png");
                tierContextBroker2.setFlavour("2");
                tierContextBroker2.setVdc("cd593e796acb4ac5821e208ff26802bd");
                tierContextBroker2.setKeypair("passmanager-fermin");
                tierContextBroker2.setFloatingip("true");
                tierContextBroker2.setEnviromentName("smartcity-lights");
                tierContextBroker2 = tierDao.create(tierContextBroker2);

                Tier tierApp2 = new Tier("cbapp", 1, 1, 1, null);
                tierApp2.setImage("44dcdba3-a75d-46a3-b209-5e9035d2435e");
                tierApp2.setIcono("http://blogs.sfweekly.com/thesnitch/PackageIcon.png");
                tierApp2.setFlavour("2");
                tierApp2.setVdc("cd593e796acb4ac5821e208ff26802bd");
                tierApp2.setKeypair("passmanager-fermin");
                tierApp2.setFloatingip("true");
                tierApp2.setEnviromentName("smartcity-lights");
                tierApp2 = tierDao.create(tierApp2);

                Set<Tier> tiersSmartcitylights = new HashSet<Tier>();
                tiersSmartcitylights.add(tierConfig2);
                tiersSmartcitylights.add(tierShard2);
                tiersSmartcitylights.add(tierContextBroker2);
                tiersSmartcitylights.add(tierApp2);

                Environment contextSmartcitylights = new Environment("smartcity-lights", tiersSmartcitylights,
                        "Environment smartcity-lights");
                contextSmartcitylights.setOrg("FIWARE");
                contextSmartcitylights.setVdc("cd593e796acb4ac5821e208ff26802bd");
                contextSmartcitylights = environmentDao.create(contextSmartcitylights);

                // AppicationType
                ApplicationType applicationTypeJavaSpring = new ApplicationType("Java-Spring Application",
                        "Java-Spring Env description");
                applicationTypeJavaSpring = applicationTypeDao.create(applicationTypeJavaSpring);

                // *********Application Release*************
                // War Application
                // Two artifacts: war file (WarFile Artifact) and
                // descriptor file (descriptor File Artifact)
                // Ambos asociados a un productTypeAWS

                Artifact artifactWarHelloWorld = new Artifact("war",
                        "/opt/war/releases/80f5053b166c69d81697ba21113c673f8372aca0.war", tomcat6);
                Artifact artifactDescriptorWarHelloWorld = new Artifact("HelloWorldAWSDescriptor",
                        "/opt/war/releases/80f5053b166c69d81697ba21113c673f8372aca0.xml", tomcat6);

                artifactWarHelloWorld = artifactDao.create(artifactWarHelloWorld);
                artifactDescriptorWarHelloWorld = artifactDao.create(artifactDescriptorWarHelloWorld);

                List<Artifact> artifactsHelloWorld = new ArrayList<Artifact>();
                // artifactsHelloWorld.add(artifactDescriptorWarHelloWorld);
                artifactsHelloWorld.add(artifactWarHelloWorld);

                // War Application 4Caast
                ProductRelease warHelloWorld10 = new ProductRelease("application", "1.0",
                        "4Caast War application with no access to database description", null, null, supportedSSOO123,
                        true, productTypeAWS);
                warHelloWorld10 = productReleaseDao.create(warHelloWorld10);

                // HelloWorld Artifacts
                ApplicationRelease appWarHelloWorld10 = new ApplicationRelease("HelloWorld", "1.0",
                        "War application with no access to database description", applicationTypeJavaSpring, null, null);

                appWarHelloWorld10 = applicationReleaseDao.create(appWarHelloWorld10);

                // Provisoning an environmentInstance

            } catch (AlreadyExistsEntityException e1) {
                log.error("alread exists " + e1);
            }
        }*/

        log.trace("InitDbBootstrap. END");
    }
}
