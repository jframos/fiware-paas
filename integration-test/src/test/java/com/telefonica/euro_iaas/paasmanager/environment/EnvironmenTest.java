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

package com.telefonica.euro_iaas.paasmanager.environment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telefonica.euro_iaas.paasmanager.dao.ProductReleaseDao;
import com.telefonica.euro_iaas.paasmanager.manager.EnvironmentManager;
import com.telefonica.euro_iaas.paasmanager.manager.TierManager;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierDto;
import com.telefonica.euro_iaas.paasmanager.rest.exception.APIException;
import com.telefonica.euro_iaas.paasmanager.rest.resources.EnvironmentResource;
import com.telefonica.euro_iaas.paasmanager.rest.resources.TierResource;

@RunWith(SpringJUnit4ClassRunner.class)
// ApplicationContext will be loaded from "classpath:/app-config.xml"
@ContextConfiguration(locations = { "classpath:/applicationContextTest.xml" })
@ActiveProfiles("dummy")
public class EnvironmenTest {

    @Autowired
    private EnvironmentResource environmentResource;
    @Autowired
    private TierResource tierResource;

    @Autowired
    private ProductReleaseDao productReleaseDao;

    @Autowired
    private EnvironmentManager environmentManager;

    @Autowired
    private TierManager tierManager;

    // private ProductRelease tomcat7Att;
    // private ProductRelease product2;

    // private List<ProductRelease> productRelease;

    private Tier tier;
    String org = "FIWARE";
    String vdc = "6571e3422ad84f7d828ce2f30373b3d4";

    /*
     * @Before public void setUp() throws Exception { List<Attribute> atttomcat = new ArrayList<Attribute>();
     * atttomcat.add(new Attribute("ssl_port", "8443", "The ssl listen port")); atttomcat.add(new Attribute("port",
     * "8080", "The ssl listen port")); atttomcat.add(new Attribute("openports", "8080", "The ssl listen port"));
     * atttomcat.add(new Attribute("sdcgroupid", "id_web_server", "The ssl listen port")); tomcat7Att = new
     * ProductRelease("tomcat", "7", "Tomcat server 7", atttomcat); tomcat7Att = productReleaseDao.create(tomcat7Att);
     * assertNotNull(tomcat7Att); assertNotNull(tomcat7Att.getId()); assertEquals(tomcat7Att.getProduct(), "tomcat");
     * assertEquals(tomcat7Att.getVersion(), "7"); }
     */

    @Test
    public void testEnvironmentDtos() throws Exception {
        ProductRelease tomcat7Att = new ProductRelease("tomcat1", "7", "Tomcat server 7", null);

        tomcat7Att = productReleaseDao.create(tomcat7Att);
        assertNotNull(tomcat7Att);
        assertNotNull(tomcat7Att.getId());
        assertEquals(tomcat7Att.getProduct(), "tomcat1");
        assertEquals(tomcat7Att.getVersion(), "7");

        Environment environmentBk = new Environment();
        environmentBk.setName("envtest");
        environmentBk.setDescription("Description First environment");
        Tier tierbk = new Tier("tierdto2", new Integer(1), new Integer(1), new Integer(1), null);
        tierbk.setImage("image");
        tierbk.setIcono("icono");
        tierbk.setFlavour("flavour");
        tierbk.setFloatingip("floatingip");
        tierbk.setPayload("");
        tierbk.setKeypair("keypair");
        tierbk.addProductRelease(tomcat7Att);
        environmentBk.addTier(tierbk);

        environmentResource.insert(org, vdc, environmentBk.toDto());

        Environment env2 = environmentManager.load("envtest", vdc);
        assertNotNull(env2);
        assertEquals(env2.getName(), "envtest");
        assertEquals(env2.getDescription(), "Description First environment");
        assertEquals(env2.getVdc(), vdc);
        assertEquals(env2.getOrg(), org);
        assertEquals(env2.getTiers().size(), 1);

        TierDto tierDto = tierResource.load(vdc, "envtest", "tierdto2");
        assertNotNull(tierDto);
        assertEquals(tierDto.getName(), "tierdto2");
        assertEquals(tierDto.getFlavour(), "flavour");

        List<Environment> environment2 = environmentManager.findAll();
        for (Environment envDto : environment2) {
            assertNotNull(envDto);
            assertNotNull(envDto.getTiers());
            // assertNotNull(envDto.getTiers().size());

        }

    }

    @Test
    public void testCloneEnviroment() throws Exception {

        ProductRelease tomcat7Att = new ProductRelease("tomcat50", "7", "Tomcat server 7", null);

        tomcat7Att = productReleaseDao.create(tomcat7Att);
        assertNotNull(tomcat7Att);
        assertNotNull(tomcat7Att.getId());
        assertEquals(tomcat7Att.getProduct(), "tomcat50");
        assertEquals(tomcat7Att.getVersion(), "7");

        Environment environmentBk = new Environment();
        environmentBk.setName("envinitial");
        environmentBk.setDescription("Description First environment");
        Tier tierbk = new Tier("tierinitial", new Integer(1), new Integer(1), new Integer(1), null);
        tierbk.setImage("image");
        tierbk.setIcono("icono");
        tierbk.setFlavour("flavour");
        tierbk.setFloatingip("floatingip");
        tierbk.setPayload("");
        tierbk.setKeypair("keypair");
        tierbk.addProductRelease(tomcat7Att);
        environmentBk.addTier(tierbk);

        environmentResource.insert(org, vdc, environmentBk.toDto());
        Environment env = environmentManager.load("envinitial", vdc);
        assertNotNull(env);
        assertEquals(env.getName(), "envinitial");
        assertEquals(env.getTiers().size(), 1);

        Environment environmentBk2 = new Environment();
        environmentBk2.setName("envclone1");
        environmentBk2.setDescription("Description First environment");

        environmentBk2.addTier(tierbk);

        environmentResource.insert(org, vdc, environmentBk2.toDto());
        env = environmentManager.load("envclone1", vdc);
        assertNotNull(env);
        assertEquals(env.getName(), "envclone1");
        assertEquals(env.getTiers().size(), 1);

    }

    @Test
    public void testCreateEnvironment3Tiers() throws Exception {
        ProductRelease tomcat7Att = new ProductRelease("tomcat2", "7", "Tomcat server 7", null);

        tomcat7Att = productReleaseDao.create(tomcat7Att);
        assertNotNull(tomcat7Att);
        assertNotNull(tomcat7Att.getId());
        assertEquals(tomcat7Att.getProduct(), "tomcat2");
        assertEquals(tomcat7Att.getVersion(), "7");

        List<ProductRelease> productRelease = new ArrayList();
        productRelease.add(tomcat7Att);
        Environment environmentBk = new Environment();
        environmentBk.setName("envthreetiers");
        environmentBk.setDescription("Description environment");
        Tier tierbk1 = new Tier("tier1", new Integer(1), new Integer(1), new Integer(1), productRelease);
        tierbk1.setImage("image");
        tierbk1.setIcono("icono");
        tierbk1.setFlavour("flavour");
        tierbk1.setFloatingip("floatingip");
        tierbk1.setPayload("");
        tierbk1.setKeypair("keypair");
        environmentBk.addTier(tierbk1);

        Tier tierbk2 = new Tier("tier2", new Integer(1), new Integer(1), new Integer(1), productRelease);
        tierbk2.setImage("image");
        tierbk2.setIcono("icono");
        tierbk2.setFlavour("flavour");
        tierbk2.setFloatingip("floatingip");
        tierbk2.setPayload("");
        tierbk2.setKeypair("keypair");
        environmentBk.addTier(tierbk2);

        Tier tierbk3 = new Tier("tier3", new Integer(1), new Integer(1), new Integer(1), productRelease);
        tierbk3.setImage("image");
        tierbk3.setIcono("icono");
        tierbk3.setFlavour("flavour");
        tierbk3.setFloatingip("floatingip");
        tierbk3.setPayload("");
        tierbk3.setKeypair("keypair");
        environmentBk.addTier(tierbk3);

        environmentResource.insert(org, vdc, environmentBk.toDto());

        Environment env2 = environmentManager.load("envthreetiers", vdc);
        assertNotNull(env2);
        assertEquals(env2.getName(), "envthreetiers");
        assertEquals(env2.getDescription(), "Description environment");
        assertEquals(env2.getVdc(), vdc);
        assertEquals(env2.getOrg(), org);
        assertEquals(env2.getTiers().size(), 3);

        TierDto tierDto = tierResource.load(vdc, "envthreetiers", "tier1");
        assertNotNull(tierDto);
        assertEquals(tierDto.getName(), "tier1");
        assertEquals(tierDto.getFlavour(), "flavour");

        List<Environment> environment2 = environmentManager.findAll();
        for (Environment envDto : environment2) {
            assertNotNull(envDto);
            assertNotNull(envDto.getTiers());
            // assertNotNull(envDto.getTiers().size());

        }

    }

    @Test
    public void testFirstCreateEnvironmentOK() throws Exception {

        List<Environment> environments = environmentManager.findAll();
        int numberEnvs = environments.size();

        ProductRelease tomcat7Att = new ProductRelease("tomcat3", "7", "Tomcat server 7", null);

        tomcat7Att = productReleaseDao.create(tomcat7Att);
        assertNotNull(tomcat7Att);
        assertNotNull(tomcat7Att.getId());
        assertEquals(tomcat7Att.getProduct(), "tomcat3");
        assertEquals(tomcat7Att.getVersion(), "7");

        Environment environmentBk = new Environment();
        environmentBk.setName("firstenvironment");
        environmentBk.setDescription("Description First environment");
        Tier tierbk = new Tier("tierFirst", new Integer(1), new Integer(1), new Integer(1), null);
        tierbk.setImage("image");
        tierbk.setIcono("icono");
        tierbk.setFlavour("flavour");
        tierbk.setFloatingip("floatingip");
        tierbk.setPayload("");
        tierbk.setKeypair("keypair");
        tierbk.addProductRelease(tomcat7Att);
        environmentBk.addTier(tierbk);

        environmentResource.insert(org, vdc, environmentBk.toDto());

        List<Environment> environment2 = environmentManager.findAll();
        assertEquals(environment2.size(), numberEnvs + 1);

        Environment env = environmentManager.load("firstenvironment", vdc);
        assertNotNull(env);
        assertEquals(env.getName(), env.getName());

    }

    @Test
    public void shouldReturnErrorWhenSecondCreation() throws Exception {
        Environment environmentBk = new Environment();
        environmentBk.setName("alreadyexistsenvironment");
        environmentBk.setDescription("Description First environment");
        Tier tierbk = new Tier("tierSecond", new Integer(1), new Integer(1), new Integer(1), null);
        tierbk.setImage("image");
        tierbk.setIcono("icono");
        tierbk.setFlavour("flavour");
        tierbk.setFloatingip("floatingip");
        tierbk.setPayload("");
        tierbk.setKeypair("keypair");
        environmentBk.addTier(tierbk);

        environmentResource.insert(org, vdc, environmentBk.toDto());
        try {
            environmentResource.insert(org, vdc, environmentBk.toDto());
            fail();
        } catch (APIException e) {
            String message = e.getMessage();
            assertTrue(message.matches(".*The environment .* already exists"));
        }

    }

    @Test
    public void testCreateEnvironmentRepiteDifferntVdc() throws Exception {
        Environment environmentBk = new Environment();
        environmentBk.setName("envDifferentvdc");
        environmentBk.setDescription("Description First environment");
        Tier tierbk = new Tier("tierDiffVdc", new Integer(1), new Integer(1), new Integer(1), null);
        tierbk.setImage("image");
        tierbk.setIcono("icono");
        tierbk.setFlavour("flavour");
        tierbk.setFloatingip("floatingip");
        tierbk.setPayload("");
        tierbk.setKeypair("keypair");
        environmentBk.addTier(tierbk);

        environmentResource.insert(org, vdc, environmentBk.toDto());
        environmentResource.insert(org, "dd", environmentBk.toDto());

    }

    @Test
    public void testGetSameEnvironmentDifferntVdc() throws Exception {
        Environment environmentBk = new Environment();
        environmentBk.setName("sameEnvDifferentvdc");
        environmentBk.setDescription("Description First environment");
        Tier tierbk = new Tier("tiersameEnvDiffVdc", new Integer(1), new Integer(1), new Integer(1), null);
        tierbk.setImage("image");
        tierbk.setIcono("icono");
        tierbk.setFlavour("flavour");
        tierbk.setFloatingip("floatingip");
        tierbk.setPayload("");
        tierbk.setKeypair("keypair");
        environmentBk.addTier(tierbk);

        environmentResource.insert(org, vdc, environmentBk.toDto());
        environmentResource.insert(org, "dd", environmentBk.toDto());

        Environment env1 = environmentManager.load("sameEnvDifferentvdc", vdc);
        Environment env2 = environmentManager.load("sameEnvDifferentvdc", "dd");
        assertEquals(env1.getName(), "sameEnvDifferentvdc");
        assertEquals(env2.getName(), "sameEnvDifferentvdc");
        assertEquals(env1.getVdc(), vdc);
        assertEquals(env2.getVdc(), "dd");

    }

    @Test
    public void testc3SecondCreateEnvironmentRepiteTierOK() throws Exception {
        Environment environmentBk = new Environment();
        environmentBk.setName("alreadyexiststierenvironment");
        environmentBk.setDescription("Description Second environment");
        Tier tierbk = new Tier("tierThird", new Integer(1), new Integer(1), new Integer(1), null);
        tierbk.setImage("image");
        tierbk.setIcono("icono");
        tierbk.setFlavour("flavour");
        tierbk.setFloatingip("floatingip");
        tierbk.setPayload("");
        tierbk.setKeypair("keypair");
        environmentBk.addTier(tierbk);

        environmentResource.insert(org, vdc, environmentBk.toDto());

        Environment env = new Environment();
        env.setName("Name2");
        env.setDescription("Description");

        Set<Tier> tiers = new HashSet<Tier>();
        tiers.add(tierbk);
        env.setTiers(tiers);

        environmentResource.insert(org, vdc, env.toDto());

    }

    @Test
    public void shouldFailWithEnvironmentWithoutDescription() throws Exception {

        Environment environmentBk = new Environment();
        environmentBk.setName("EnvNodescription");

        Tier tierbk = new Tier("new", new Integer(1), new Integer(1), new Integer(1), null);
        tierbk.setImage("image");
        tierbk.setIcono("icono");
        tierbk.setFlavour("flavour");
        tierbk.setFloatingip("floatingip");
        tierbk.setPayload("");
        tierbk.setKeypair("keypair");
        environmentBk.addTier(tierbk);
        try {
            environmentResource.insert(org, vdc, environmentBk.toDto());
            fail();

        } catch (APIException e) {
            String message = e.getMessage();
           
        }

    }

    @Test
    public void testCreateEnvironmentNoTiers() throws Exception {

        Environment environmentBk = new Environment();
        environmentBk.setName("EnvNoTiers");
        environmentBk.setDescription("No tiers");

        environmentResource.insert(org, vdc, environmentBk.toDto());
        Environment env = environmentManager.load("EnvNoTiers", vdc);
        assertEquals(env.getName(), "EnvNoTiers");
        assertNotNull(env.getTiers());
        assertEquals(env.getTiers().size(), 0);

    }

    @Test
    public void testdCreateTwoEnvironmentsIqualTier() throws Exception {
        Environment environmentBk = new Environment();
        environmentBk.setName("twoenvironmenttier");
        environmentBk.setDescription("Description Second environment");
        Tier tierbk = new Tier("tiertwoenvironment", new Integer(1), new Integer(1), new Integer(1), null);
        tierbk.setImage("image");
        tierbk.setIcono("icono");
        tierbk.setFlavour("flavour");
        tierbk.setFloatingip("floatingip");
        tierbk.setPayload("");
        tierbk.setKeypair("keypair");
        environmentBk.addTier(tierbk);

        environmentResource.insert(org, vdc, environmentBk.toDto());

        Environment environmentBk2 = new Environment();
        environmentBk2.setName("twoenvironmenttier2");
        environmentBk2.setDescription("Description Second environment");

        environmentBk2.addTier(tierbk);

        environmentResource.insert(org, vdc, environmentBk2.toDto());
    }

    @Test
    public void testeDeleteAndCreatedEnv() throws Exception {

        Environment environment = new Environment();
        environment.setName("testeDeleteAndCreatedEnv1");
        environment.setDescription("Description");

        Tier tier = new Tier("tierdeletecreatedenv", new Integer(1), new Integer(1), new Integer(1), null);
        tier.setImage("image");
        tier.setIcono("icono");
        tier.setFlavour("flavour");
        tier.setFloatingip("floatingip");
        tier.setPayload("");
        tier.setKeypair("keypair");
        Set<Tier> tiers = new HashSet<Tier>();
        tiers.add(tier);
        environment.setTiers(tiers);

        environmentResource.insert(org, vdc, environment.toDto());

        Environment env = environmentManager.load("testeDeleteAndCreatedEnv1", vdc);

        assertEquals(env.getName(), "testeDeleteAndCreatedEnv1");
        assertEquals(env.getTiers().size(), 1);

        environmentResource.delete(org, vdc, "testeDeleteAndCreatedEnv1");

        environmentResource.insert(org, vdc, environment.toDto());

        Environment env2 = environmentManager.load("testeDeleteAndCreatedEnv1", vdc);
        assertEquals(env2.getName(), "testeDeleteAndCreatedEnv1");
        assertEquals(env2.getTiers().size(), 1);

    }
    
    @Test
    public void testFindAll() throws APIException  {
        int number= environmentResource.findAll(org, vdc, null, null, null, null).size();
        Environment environment= new Environment();
        environment.setName("nassme");
        environment.setDescription("Description First environment");
        Tier tier = new Tier("sss", new Integer(1), new Integer(1), new Integer(1), null);
        tier.setImage("image");
        tier.setIcono("icono");
        tier.setFlavour("flavour");
        tier.setFloatingip("floatingip");
        tier.setKeypair("keypair");
       
        environment.addTier(tier);
        environmentResource.insert(org, vdc, environment.toDto());  
        
        assertEquals (environmentResource.findAll(org, vdc, null, null, null, null).size(), number+1);
  
    }

}
