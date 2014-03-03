/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.environment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

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
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.Task;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentInstanceDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierInstanceDto;
import com.telefonica.euro_iaas.paasmanager.rest.exception.APIException;
import com.telefonica.euro_iaas.paasmanager.rest.resources.EnvironmentInstanceResource;
import com.telefonica.euro_iaas.paasmanager.rest.resources.EnvironmentResource;
import com.telefonica.euro_iaas.paasmanager.rest.resources.TierInstanceResource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/applicationContextTest.xml" })
@ActiveProfiles("dummy")
public class EnvironmenInstanceITest {

    @Autowired
    private EnvironmentResource environmentResource;

    @Autowired
    private EnvironmentInstanceResource environmentInstanceResource;

    @Autowired
    private TierInstanceResource tierInstanceResource;

    @Autowired
    private TierInstanceManager tierInstanceManager;

    @Autowired
    private ProductReleaseDao productReleaseDao;

    @Autowired
    private EnvironmentManager environmentManager;

    @Autowired
    private EnvironmentInstanceManager environmentInstanceManager;

    String org = "FIWARE";
    String vdc = "6571e3422ad84f7d828ce2f30373b3d4";

    @Test
    public void testCreateEnvironmentInstance() throws Exception {

        ProductRelease product = new ProductRelease("tomcat21", "7", "Tomcat server 21", null);

        product = productReleaseDao.create(product);
        assertNotNull(product);
        assertNotNull(product.getId());
        assertEquals(product.getProduct(), "tomcat21");
        assertEquals(product.getVersion(), "7");

        Environment environmentBk = new Environment();
        environmentBk.setName("tCEI");
        environmentBk.setDescription("Description First environment");
        Tier tierbk = new Tier("tierdtotest", new Integer(1), new Integer(1), new Integer(1), null);
        tierbk.setImage("image");
        tierbk.setIcono("icono");
        tierbk.setFlavour("flavour");
        tierbk.setFloatingip("floatingip");
        tierbk.setPayload("");
        tierbk.setKeypair("keypair");
        tierbk.addProductRelease(product);
        environmentBk.addTier(tierbk);

        environmentResource.insert(org, vdc, environmentBk.toDto());

        Environment env2 = environmentManager.load("tCEI");

        EnvironmentInstanceDto envInst = new EnvironmentInstanceDto();
        envInst.setBlueprintName("blueprintname");
        envInst.setDescription("description");
        envInst.setEnvironmentDto(environmentBk.toDto());
        List<TierInstanceDto> tierInstanceDtos = new ArrayList(2);
        TierInstanceDto tierInstanceDto = new TierInstanceDto();
        TierDto tierDto = new TierDto();
        tierDto.setInitialNumberInstances(1);
        tierDto.setFloatingip("true");
        tierInstanceDto.setTierDto(tierDto);
        tierInstanceDtos.add(tierInstanceDto);
        envInst.setTierInstances(tierInstanceDtos);

        Task task = environmentInstanceResource.create(org, vdc, envInst, "");

        Thread.sleep(5000);

        assertEquals(Task.TaskStates.RUNNING, task.getStatus());

        EnvironmentInstance envInstResult = environmentInstanceManager.load(vdc, "blueprintname");
        assertNotNull(envInstResult);
        assertEquals(envInstResult.getBlueprintName(), "blueprintname");
        assertEquals(envInstResult.getEnvironment().getName(), "tCEI");

        TierInstance tierInstance = tierInstanceManager.load("blueprintname-tierdtotest-1");
        assertEquals(tierInstance.getName(), "blueprintname-tierdtotest-1");
        assertEquals(tierInstance.getNumberReplica(), 1);
        assertEquals(tierInstance.getTier().getName(), "tierdtotest");

    }

    @Test
    public void testCreateEnvironmentInstanceNoEnvironment() throws Exception {

        EnvironmentInstanceDto envInst = new EnvironmentInstanceDto();
        envInst.setBlueprintName("blueprintname");
        envInst.setDescription("description");

        try {
            environmentInstanceResource.create(org, vdc, envInst, "");

        } catch (APIException e) {
            String message = e.getMessage();
            int result = message.indexOf("The environment to be deployed is null");

            assertTrue(result != -1);
        }

    }

    @Test
    public void testCreateEnvironmentInstanceNoDescription() throws Exception {

        EnvironmentInstanceDto envInst = new EnvironmentInstanceDto();
        envInst.setBlueprintName("blueprintname2");
        try {
            environmentInstanceResource.create(org, vdc, envInst, "");

        } catch (APIException e) {
            String message = e.getMessage();
            int result = message.indexOf("is null");

            assertTrue(result != -1);
        }
    }

    @Test
    public void testCreateEnvironmentInstanceNoBlueprintName() throws Exception {

        EnvironmentInstanceDto envInst = new EnvironmentInstanceDto();
        envInst.setDescription("description");
        try {
            environmentInstanceResource.create(org, vdc, envInst, "");

        } catch (APIException e) {
            String message = e.getMessage();
            int result = message.indexOf(" is null");

            assertTrue(result != -1);
        }

    }

    @Test
    public void testCreateEnvironmentInstanceCreateTierInstance() throws Exception {

        ProductRelease product = new ProductRelease("tomcat25", "7", "Tomcat server 21", null);

        product = productReleaseDao.create(product);
        assertNotNull(product);
        assertNotNull(product.getId());
        assertEquals(product.getProduct(), "tomcat25");
        assertEquals(product.getVersion(), "7");

        Environment environmentBk = new Environment();
        environmentBk.setName("tcETierInstance");
        environmentBk.setDescription("Description First environment");
        Tier tierbk = new Tier("tierdAddTierInstancr", new Integer(2), new Integer(1), new Integer(1), null);
        tierbk.setImage("image");
        tierbk.setIcono("icono");
        tierbk.setFlavour("flavour");
        tierbk.setFloatingip("floatingip");
        tierbk.setPayload("");
        tierbk.setKeypair("keypair");
        tierbk.addProductRelease(product);
        environmentBk.addTier(tierbk);

        environmentResource.insert(org, vdc, environmentBk.toDto());

        Environment env2 = environmentManager.load("tcETierInstance");
        assertNotNull(env2);
        assertEquals(env2.getName(), "tcETierInstance");
        assertEquals(env2.getDescription(), "Description First environment");
        assertEquals(env2.getVdc(), vdc);
        assertEquals(env2.getOrg(), org);
        assertEquals(env2.getTiers().size(), 1);

        EnvironmentInstanceDto envInst = new EnvironmentInstanceDto();
        envInst.setBlueprintName("blueprintnameTierInstance");
        envInst.setDescription("description");
        envInst.setEnvironmentDto(environmentBk.toDto());
        List<TierInstanceDto> tierInstanceDtos = new ArrayList(2);
        TierInstanceDto tierInstanceDto = new TierInstanceDto();
        TierDto tierDto = new TierDto();
        tierDto.setInitialNumberInstances(1);
        tierDto.setFloatingip("true");
        tierInstanceDto.setTierDto(tierDto);
        tierInstanceDtos.add(tierInstanceDto);
        envInst.setTierInstances(tierInstanceDtos);

        Task task = environmentInstanceResource.create(org, vdc, envInst, "");
        Thread.sleep(20000);
        assertEquals(task.getStatus(), Task.TaskStates.RUNNING);

        EnvironmentInstance envInstResult = environmentInstanceManager.load(vdc, "blueprintnameTierInstance");
        assertNotNull(envInstResult);
        assertEquals(envInstResult.getBlueprintName(), "blueprintnameTierInstance");
        assertEquals(envInstResult.getEnvironment().getName(), "tcETierInstance");
        assertEquals(envInstResult.getTierInstances().size(), 1);

        TierInstance tierInstance = tierInstanceManager.load("blueprintnameTierInstance-tierdAddTierInstancr-1");
        assertEquals(tierInstance.getName(), "blueprintnameTierInstance-tierdAddTierInstancr-1");
        assertEquals(tierInstance.getNumberReplica(), 1);

        task = tierInstanceResource.insert(org, vdc, "blueprintnameTierInstance", tierbk.toDto(), "");
        Thread.sleep(5000);
        assertEquals(task.getStatus(), Task.TaskStates.RUNNING);
        tierInstance = tierInstanceManager.load("blueprintnameTierInstance-tierdAddTierInstancr-2");
        assertEquals(tierInstance.getName(), "blueprintnameTierInstance-tierdAddTierInstancr-2");
        assertEquals(tierInstance.getNumberReplica(), 2);

    }

    @Test
    public void testCreateEnvironmentInstanceDeleteTierInstance() throws Exception {

        ProductRelease product = new ProductRelease("tomcat31", "7", "Tomcat server 21", null);

        product = productReleaseDao.create(product);
        assertNotNull(product);
        assertNotNull(product.getId());
        assertEquals(product.getProduct(), "tomcat31");
        assertEquals(product.getVersion(), "7");

        Environment environmentBk = new Environment();
        environmentBk.setName("tcEDeleteTierInstance");
        environmentBk.setDescription("Description First environment");
        Tier tierbk = new Tier("tierdAddDeleteTierInstancr", new Integer(2), new Integer(0), new Integer(1), null);
        tierbk.setImage("image");
        tierbk.setIcono("icono");
        tierbk.setFlavour("flavour");
        tierbk.setFloatingip("floatingip");
        tierbk.setPayload("");
        tierbk.setKeypair("keypair");
        tierbk.addProductRelease(product);
        environmentBk.addTier(tierbk);

        environmentResource.insert(org, vdc, environmentBk.toDto());
        Environment env = environmentManager.load("tcEDeleteTierInstance", vdc);

        assertNotNull(env);
        assertEquals(env.getName(), "tcEDeleteTierInstance");
        assertEquals(env.getDescription(), "Description First environment");
        assertEquals(env.getTiers().size(), 1);

        EnvironmentInstanceDto envInst = new EnvironmentInstanceDto();
        envInst.setBlueprintName("btid");
        envInst.setDescription("description");
        envInst.setEnvironmentDto(environmentBk.toDto());
        List<TierInstanceDto> tierInstanceDtos = new ArrayList(2);
        TierInstanceDto tierInstanceDto = new TierInstanceDto();
        TierDto tierDto = new TierDto();
        tierDto.setInitialNumberInstances(1);
        tierDto.setFloatingip("true");
        tierInstanceDto.setTierDto(tierDto);
        tierInstanceDtos.add(tierInstanceDto);
        envInst.setTierInstances(tierInstanceDtos);

        Task task = environmentInstanceResource.create(org, vdc, envInst, "");
        Thread.sleep(5000);
        assertEquals(task.getStatus(), Task.TaskStates.RUNNING);

        EnvironmentInstance envInstResult = environmentInstanceManager.load(vdc, "btid");
        assertNotNull(envInstResult);
        assertEquals(envInstResult.getBlueprintName(), "btid");
        assertEquals(envInstResult.getEnvironment().getName(), "tcEDeleteTierInstance");
        assertEquals(envInstResult.getTierInstances().size(), 1);
        String name = envInstResult.getTierInstances().get(0).getName();

        tierInstanceResource.removeTierInstance(org, vdc, "btid", name, "");

        envInstResult = environmentInstanceManager.load(vdc, "btid");
        assertNotNull(envInstResult);
        assertEquals(envInstResult.getBlueprintName(), "btid");
        assertEquals(envInstResult.getEnvironment().getName(), "tcEDeleteTierInstance");

        assertEquals(envInstResult.getTierInstances().size(), 0);

    }

    @Test
    public void testCreateDeleteEnvironmentInstance() throws Exception {

        ProductRelease product = new ProductRelease("tomcat26", "7", "Tomcat server 21", null);

        product = productReleaseDao.create(product);
        assertNotNull(product);
        assertNotNull(product.getId());
        assertEquals(product.getProduct(), "tomcat26");
        assertEquals(product.getVersion(), "7");

        Environment environmentBk = new Environment();
        environmentBk.setName("tdei");
        environmentBk.setDescription("Description First environment");
        Tier tierbk = new Tier("tierDeleteEnvInst", new Integer(1), new Integer(1), new Integer(1), null);
        tierbk.setImage("image");
        tierbk.setIcono("icono");
        tierbk.setFlavour("flavour");
        tierbk.setFloatingip("floatingip");
        tierbk.setPayload("");
        tierbk.setKeypair("keypair");
        tierbk.addProductRelease(product);
        environmentBk.addTier(tierbk);

        environmentResource.insert(org, vdc, environmentBk.toDto());
        Environment env = environmentManager.load("tdei", vdc);
        assertNotNull(env);
        assertEquals(env.getName(), "tdei");
        assertEquals(env.getDescription(), "Description First environment");
        assertEquals(env.getTiers().size(), 1);

        EnvironmentInstanceDto envInst = new EnvironmentInstanceDto();
        envInst.setBlueprintName("blueprintnameDeleteEnvInstn");
        envInst.setDescription("description");
        envInst.setEnvironmentDto(environmentBk.toDto());
        List<TierInstanceDto> tierInstanceDtos = new ArrayList(2);
        TierInstanceDto tierInstanceDto = new TierInstanceDto();
        TierDto tierDto = new TierDto();
        tierDto.setInitialNumberInstances(1);
        tierDto.setFloatingip("true");
        tierInstanceDto.setTierDto(tierDto);
        tierInstanceDtos.add(tierInstanceDto);
        envInst.setTierInstances(tierInstanceDtos);

        Task task = environmentInstanceResource.create(org, vdc, envInst, "");
        Thread.sleep(5000);

        assertEquals(task.getStatus(), Task.TaskStates.RUNNING);

        EnvironmentInstance envInstResult = environmentInstanceManager.load(vdc, "blueprintnameDeleteEnvInstn");
        assertNotNull(envInstResult);
        assertEquals(envInstResult.getBlueprintName(), "blueprintnameDeleteEnvInstn");
        assertEquals(envInstResult.getEnvironment().getName(), "tdei");
        assertEquals(envInstResult.getTierInstances().size(), 1);

        environmentInstanceResource.destroy(org, vdc, "blueprintnameDeleteEnvInstn", "");
        // environmentInstanceManager.load(vdc, "blueprintnameDeleteEnvInstn");

    }

}
