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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telefonica.euro_iaas.paasmanager.dao.ProductReleaseDao;
import com.telefonica.euro_iaas.paasmanager.manager.EnvironmentManager;
import com.telefonica.euro_iaas.paasmanager.manager.TierManager;
import com.telefonica.euro_iaas.paasmanager.model.Attribute;
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
public class TierTest {

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

        atttomcat.add(new Attribute("ssl_port", "8443", "The ssl listen port"));
        atttomcat.add(new Attribute("port", "8080", "The ssl listen port"));
        atttomcat.add(new Attribute("openports", "8080", "The ssl listen port"));
        atttomcat.add(new Attribute("sdcgroupid", "id_web_server", "The ssl listen port"));

    }

    @Test
    public void testCreateTierOK() throws Exception {
        Environment environment2 = new Environment();
        environment2.setName("create_tier_ok");
        environment2.setDescription("description");

        environmentResource.insert(org, vdc, environment2.toDto());
        ProductRelease tomcat7Att = new ProductRelease("tomcat8", "78", "Tomcat server 8", null);

        tomcat7Att = productReleaseDao.create(tomcat7Att);

        assertNotNull(tomcat7Att);
        assertNotNull(tomcat7Att.getId());
        assertEquals(tomcat7Att.getProduct(), "tomcat8");
        assertEquals(tomcat7Att.getVersion(), "78");

        Tier tierbk = new Tier("tiercreated_ok", new Integer(1), new Integer(1), new Integer(1), null);
        tierbk.setImage("image");
        tierbk.setIcono("icono");
        tierbk.setFlavour("flavour");
        tierbk.setFloatingip("floatingip");
        tierbk.setPayload("");
        tierbk.setKeypair("keypair");
        tierbk.addProductRelease(tomcat7Att);
        tierbk.setRegion("regionOne");

        tierResource.insert(org, vdc, "create_tier_ok", tierbk.toDto());
        TierDto tierDto = tierResource.load(vdc, "create_tier_ok", "tiercreated_ok");
        assertEquals(tierDto.getName(), "tiercreated_ok");
        assertEquals(tierDto.getProductReleaseDtos().size(), 1);
        assertEquals(tierDto.getProductReleaseDtos().get(0).getProductName(), "tomcat8");
        assertEquals("regionOne", tierDto.getRegion());
    }

    @Test(expected = APIException.class)
    public void testCreateTierAlreadyExist() throws Exception {
        Environment environmentBk = new Environment();
        environmentBk.setName("create_tier_already");
        environmentBk.setDescription("description");

        environmentResource.insert(org, vdc, environmentBk.toDto());

        Tier tierbk = new Tier("tiercreatedalready", new Integer(1), new Integer(1), new Integer(1), null);
        tierbk.setImage("image");
        tierbk.setIcono("icono");
        tierbk.setFlavour("flavour");
        tierbk.setFloatingip("floatingip");
        tierbk.setPayload("");
        tierbk.setKeypair("keypair");

        tierResource.insert(org, vdc, "create_tier_already", tierbk.toDto());
        tierResource.insert(org, vdc, "create_tier_already", tierbk.toDto());
    }

    @Test(expected = Exception.class)
    public void testCreateTierNotFound() throws Exception {
        Environment environmentBk = new Environment();
        environmentBk.setName("create_tier");
        environmentBk.setDescription("description");

        environmentResource.insert(org, vdc, environmentBk.toDto());

        TierDto tier = tierResource.load(vdc, "testCreateTierOK", "tiercreated");

    }

    @Test
    public void testdUpdateTier() throws Exception {

        Environment environmentBk = new Environment();
        environmentBk.setName("updatedenvironmenttierv2");
        environmentBk.setDescription("Description Second environment");
        Tier tierbk = new Tier("tierupdatetier22", new Integer(1), new Integer(1), new Integer(1), null);
        tierbk.setImage("image");
        tierbk.setIcono("icono");
        tierbk.setFlavour("flavour");
        tierbk.setFloatingip("floatingip");
        tierbk.setPayload("");
        tierbk.setKeypair("keypair");
        environmentBk.addTier(tierbk);

        environmentResource.insert(org, vdc, environmentBk.toDto());

        Environment env = environmentManager.load("updatedenvironmenttierv2", vdc);

        TierDto tierDto = tierResource.load(vdc, "updatedenvironmenttierv2", "tierupdatetier22");

        tierDto.setFlavour("flavour3");
        tierDto.setIcono("icono2");
        tierResource.update(org, vdc, "updatedenvironmenttierv2", tierDto);
        TierDto tier2Dto = tierResource.load(vdc, "updatedenvironmenttierv2", "tierupdatetier22");
        assertEquals(tier2Dto.getFlavour(), "flavour3");
        assertEquals(tier2Dto.getIcono(), "icono2");

        Environment env3 = environmentManager.load("updatedenvironmenttierv2", vdc);
        assertEquals(env3.getName(), "updatedenvironmenttierv2");
        assertEquals(env3.getTiers().size(), 1);

    }

    @Test
    public void testdUpdateTierSoftware() throws Exception {

        ProductRelease product2 = new ProductRelease("test", "0.1", "test 0.1", null);

        product2 = productReleaseDao.create(product2);
        assertNotNull(product2);
        assertNotNull(product2.getId());
        assertEquals(product2.getProduct(), "test");
        assertEquals(product2.getVersion(), "0.1");

        ProductRelease product3 = new ProductRelease("test2", "0.1", "test2 0.1", null);

        product3 = productReleaseDao.create(product3);
        assertNotNull(product3);
        assertNotNull(product3.getId());
        assertEquals(product3.getProduct(), "test2");
        assertEquals(product3.getVersion(), "0.1");

        Environment environmentBk = new Environment();
        environmentBk.setName("updatedenvironmentsoftwware");
        environmentBk.setDescription("Description Second environment");
        Tier tierbk = new Tier("tiersoftware", new Integer(1), new Integer(1), new Integer(1), null);
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
        assertEquals(env.getName(), "updatedenvironmentsoftwware");
        assertEquals(env.getTiers().size(), 1);

        TierDto tierDto = tierResource.load(vdc, "updatedenvironmentsoftwware", "tiersoftware");
        assertEquals(tierDto.getFlavour(), "flavour");
        assertEquals(tierDto.getName(), "tiersoftware");
        assertEquals(tierDto.getProductReleaseDtos().size(), 1);
        tierDto.setFlavour("flavour3");
        tierDto.setIcono("icono2");
        tierDto.addProductRelease(product3.toDto());
        tierResource.update(org, vdc, "updatedenvironmentsoftwware", tierDto);
        TierDto tier2Dto = tierResource.load(vdc, "updatedenvironmentsoftwware", "tiersoftware");
        assertEquals(tier2Dto.getProductReleaseDtos().size(), 2);
        assertEquals(tier2Dto.getProductReleaseDtos().get(0).getProductName(), "test");
        assertEquals(tier2Dto.getProductReleaseDtos().get(1).getProductName(), "test2");

        Environment env3 = environmentManager.load("updatedenvironmentsoftwware", vdc);
        assertEquals(env3.getName(), "updatedenvironmentsoftwware");
        assertEquals(env3.getTiers().size(), 1);

    }

    @Test
    public void testeDeleteTier() throws Exception {

        Environment environment = new Environment();
        environment.setName("testeDeleteTier2");
        environment.setDescription("Description");

        ProductRelease tomcat7Att = new ProductRelease("tomcat9", "7", "Tomcat server 7", null);

        tomcat7Att = productReleaseDao.create(tomcat7Att);

        Tier tier = new Tier("2tierotro", new Integer(1), new Integer(1), new Integer(1), null);
        tier.setImage("image");
        tier.setIcono("icono");
        tier.setFlavour("flavour");
        tier.setFloatingip("floatingip");
        tier.setPayload("");
        tier.setKeypair("keypair");
        tier.addProductRelease(tomcat7Att);
        Set<Tier> tiers = new HashSet<Tier>();
        tiers.add(tier);
        environment.setTiers(tiers);

        environmentResource.insert(org, vdc, environment.toDto());

        Environment env = environmentManager.load("testeDeleteTier2", vdc);

        assertEquals(env.getName(), "testeDeleteTier2");
        assertEquals(env.getTiers().size(), 1);

        tierResource.delete(org, vdc, "testeDeleteTier2", "2tierotro");

        Environment env2 = environmentManager.load("testeDeleteTier2", vdc);
        assertEquals(env2.getName(), "testeDeleteTier2");
        assertEquals(env2.getTiers().size(), 0);

    }

}
