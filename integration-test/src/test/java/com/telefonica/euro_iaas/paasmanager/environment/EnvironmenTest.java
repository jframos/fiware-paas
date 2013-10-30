/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.environment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telefonica.euro_iaas.paasmanager.dao.ProductReleaseDao;
import com.telefonica.euro_iaas.paasmanager.exception.AlreadyExistEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidEnvironmentRequestException;
import com.telefonica.euro_iaas.paasmanager.manager.EnvironmentManager;
import com.telefonica.euro_iaas.paasmanager.manager.TierManager;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierDto;
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
        assertEquals(env2.getTiers().get(0).getName(), "tierdto2");

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
        assertEquals(env2.getTiers().get(0).getName(), "tier1");
        assertEquals(env2.getTiers().get(1).getName(), "tier2");
        assertEquals(env2.getTiers().get(2).getName(), "tier3");

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

    @Test(expected = AlreadyExistEntityException.class)
    public void testbSecondCreateEnvironmentRepiteOK() throws Exception {
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
        environmentResource.insert(org, vdc, environmentBk.toDto());

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

        List<Tier> tiers = new ArrayList<Tier>();
        tiers.add(tierbk);
        env.setTiers(tiers);

        environmentResource.insert(org, vdc, env.toDto());

    }

    @Test(expected = InvalidEnvironmentRequestException.class)
    public void testCreateEnvironmentNoDescription() throws Exception {

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

        environmentResource.insert(org, vdc, environmentBk.toDto());

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
        List<Tier> tiers = new ArrayList<Tier>();
        tiers.add(tier);
        environment.setTiers(tiers);

        environmentResource.insert(org, vdc, environment.toDto());

        Environment env = environmentManager.load("testeDeleteAndCreatedEnv1", vdc);

        assertEquals(env.getName(), "testeDeleteAndCreatedEnv1");
        assertEquals(env.getTiers().size(), 1);
        assertEquals(env.getTiers().get(0).getFlavour(), "flavour");
        assertEquals(env.getTiers().get(0).getIcono(), "icono");

        environmentResource.delete(org, vdc, "testeDeleteAndCreatedEnv1");

        environmentResource.insert(org, vdc, environment.toDto());

        Environment env2 = environmentManager.load("testeDeleteAndCreatedEnv1", vdc);
        assertEquals(env2.getName(), "testeDeleteAndCreatedEnv1");
        assertEquals(env2.getTiers().size(), 1);
        assertEquals(env2.getTiers().get(0).getName(), "tierdeletecreatedenv");

    }

}
