/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.environment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telefonica.euro_iaas.paasmanager.dao.ProductReleaseDao;
import com.telefonica.euro_iaas.paasmanager.manager.EnvironmentInstanceManager;
import com.telefonica.euro_iaas.paasmanager.manager.EnvironmentManager;
import com.telefonica.euro_iaas.paasmanager.manager.TierInstanceManager;
import com.telefonica.euro_iaas.paasmanager.manager.TierManager;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.Network;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.Task;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentInstanceDto;
import com.telefonica.euro_iaas.paasmanager.rest.resources.EnvironmentInstanceResource;
import com.telefonica.euro_iaas.paasmanager.rest.resources.EnvironmentResource;
import com.telefonica.euro_iaas.paasmanager.rest.resources.TierInstanceResource;
import com.telefonica.euro_iaas.paasmanager.rest.resources.TierResource;

@RunWith(SpringJUnit4ClassRunner.class)
// ApplicationContext will be loaded from "classpath:/app-config.xml"
@ContextConfiguration(locations = { "classpath:/applicationContextTest.xml" })
@ActiveProfiles("dummy")
public class NetworkinEnvironmenInstanceTest {

    @Autowired
    private EnvironmentResource environmentResource;

    @Autowired
    private EnvironmentInstanceResource environmentInstanceResource;

    @Autowired
    private TierInstanceResource tierInstanceResource;

    @Autowired
    private TierInstanceManager tierInstanceManager;

    @Autowired
    private TierResource tierResource;

    @Autowired
    private ProductReleaseDao productReleaseDao;

    @Autowired
    private EnvironmentManager environmentManager;

    @Autowired
    private EnvironmentInstanceManager environmentInstanceManager;

    @Autowired
    private TierManager tierManager;

    String org = "FIWARE";
    String vdc = "6571e3422ad84f7d828ce2f30373b3d4";

    @Test
    public void testCreateEnvironmentInstanceWithAliasNetwork() throws Exception {

        ProductRelease product = new ProductRelease("tomcat22", "7", "Tomcat server 22", null);

        product = productReleaseDao.create(product);
        assertNotNull(product);
        assertNotNull(product.getId());
        assertEquals(product.getProduct(), "tomcat22");
        assertEquals(product.getVersion(), "7");

        Environment environmentBk = new Environment();
        environmentBk.setName("testCreatedEnvirionmentInstance2");
        environmentBk.setDescription("Description First environment");
        Tier tierbk = new Tier("tierdtotest", new Integer(1), new Integer(1), new Integer(1), null);
        tierbk.setImage("image");
        tierbk.setIcono("icono");
        tierbk.setFlavour("flavour");
        tierbk.setFloatingip("floatingip");
        tierbk.setPayload("");
        tierbk.setKeypair("keypair");
        tierbk.addProductRelease(product);

        Network net = new Network ("network");
        tierbk.addNetwork(net);

        environmentBk.addTier(tierbk);

        environmentResource.insert(org, vdc, environmentBk.toDto());

        Environment env2 = environmentManager.load("testCreatedEnvirionmentInstance2");

        EnvironmentInstanceDto envInst = new EnvironmentInstanceDto();
        envInst.setBlueprintName("blueprintname2");
        envInst.setDescription("description");
        envInst.setEnvironmentDto(environmentBk.toDto());

        Task task = environmentInstanceResource.create(org, vdc, envInst, "");

        Thread.sleep(5000);

        assertEquals(task.getStatus(), Task.TaskStates.RUNNING);

        EnvironmentInstance envInstResult = environmentInstanceManager.load(vdc, "blueprintname2");
        assertNotNull(envInstResult);
        assertEquals(envInstResult.getBlueprintName(), "blueprintname2");
        assertEquals(envInstResult.getEnvironment().getName(), "testCreatedEnvirionmentInstance2");

        TierInstance tierInstance = tierInstanceManager.load("blueprintname2-tierdtotest-1");
        assertEquals(tierInstance.getName(), "blueprintname2-tierdtotest-1");
        assertEquals(tierInstance.getNumberReplica(), 1);
        assertEquals(tierInstance.getTier().getName(), "tierdtotest");

    }



}
