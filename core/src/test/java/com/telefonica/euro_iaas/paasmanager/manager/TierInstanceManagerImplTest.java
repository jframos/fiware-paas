/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.manager;

import java.util.ArrayList;
import java.util.List;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.dao.TierInstanceDao;
import com.telefonica.euro_iaas.paasmanager.manager.impl.TierInstanceManagerImpl;
import com.telefonica.euro_iaas.paasmanager.model.Attribute;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.paasmanager.model.ProductInstance;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;
import com.telefonica.euro_iaas.paasmanager.model.dto.PaasManagerUser;
import com.telefonica.euro_iaas.paasmanager.model.dto.VM;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.core.GrantedAuthority;


import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author jesus.movilla
 */
public class TierInstanceManagerImplTest extends TestCase {

    private TierInstanceDao tierInstanceDao;
    private InfrastructureManager infrastructureManager;
    private ProductReleaseManager productReleaseManager;
    private ProductInstanceManager productInstanceManager;
    private TierManager tierManager;
    private EnvironmentManager enviromentManager;
    private EnvironmentInstanceManager environmentInstanceManager;

    private Tier tierProductShard = null;
    private Tier tierProductConfig = null;
    private Tier tierProductMongos = null;

    private TierInstance tierInstanceConfig = null;
    private TierInstance tierInstanceShard = null;
    private TierInstance tierInstanceMongos = null;
    private EnvironmentInstance environmentInstance = null;
    private TierInstanceManagerImpl manager = null;
    private Environment envResult = null;

    private PaasManagerUser user;
    private ClaudiaData claudiaData;

    @Override
    @Before
    public void setUp() throws Exception {

        claudiaData = new ClaudiaData("org", "vdc", "service");
        user = new PaasManagerUser("user", "password", new ArrayList<GrantedAuthority>());
        claudiaData.setUser(user);

        tierInstanceDao = mock(TierInstanceDao.class);
        infrastructureManager = mock(InfrastructureManager.class);
        productReleaseManager = mock(ProductReleaseManager.class);
        productInstanceManager = mock(ProductInstanceManager.class);
        environmentInstanceManager = mock(EnvironmentInstanceManager.class);
        tierManager = mock(TierManager.class);
        enviromentManager = mock(EnvironmentManager.class);

        manager = new TierInstanceManagerImpl();

        manager.setInfrastructureManager(infrastructureManager);
        manager.setProductInstanceManager(productInstanceManager);
        manager.setTierInstanceDao(tierInstanceDao);
        manager.setTierManager(tierManager);
        manager.setEnvironmentInstanceManager(environmentInstanceManager);
        manager.setEnvironmentManager(enviromentManager);

        VM host = new VM(null, "hostname", "domain");

        ProductRelease productReleaseConfig = new ProductRelease("config", "2.0");
        List<ProductRelease> productReleasesConfig = new ArrayList<ProductRelease>();
        productReleasesConfig.add(productReleaseConfig);
        tierProductConfig = new Tier();
        tierProductConfig.setInitialNumberInstances(new Integer(1));
        tierProductConfig.setMaximumNumberInstances(new Integer(5));
        tierProductConfig.setMinimumNumberInstances(new Integer(1));
        tierProductConfig.setName("tierconfig");
        tierProductConfig.setProductReleases(productReleasesConfig);

        Attribute att = new Attribute("balancer", "mongos", "description");
        List<Attribute> lAtt = new ArrayList();
        lAtt.add(att);
        ProductRelease productReleaseShard = new ProductRelease("shard", "2.0");
        productReleaseShard.addAttribute(att);
        // ProductRelease productBalancer = new ProductRelease("productbalancer", "2.0");
        List<ProductRelease> productReleasesShards = new ArrayList<ProductRelease>();
        productReleasesShards.add(productReleaseShard);

        tierProductShard = new Tier();
        tierProductShard.setInitialNumberInstances(new Integer(1));
        tierProductShard.setMaximumNumberInstances(new Integer(5));
        tierProductShard.setMinimumNumberInstances(new Integer(1));
        tierProductShard.setName("tiershard");
        tierProductShard.setProductReleases(productReleasesShards);

        List<ProductRelease> productReleaseBalancer = new ArrayList<ProductRelease>();
        ProductRelease productReleaseMongos = new ProductRelease("mongos", "2.0");
        productReleaseBalancer.add(productReleaseMongos);

        tierProductMongos = new Tier();
        tierProductMongos.setInitialNumberInstances(new Integer(1));
        tierProductMongos.setMaximumNumberInstances(new Integer(5));
        tierProductMongos.setMinimumNumberInstances(new Integer(1));
        tierProductMongos.setName("tierNameMongos");
        tierProductMongos.setProductReleases(productReleaseBalancer);

        tierInstanceConfig = new TierInstance(tierProductConfig, "tierInsatnceConfig", "nametierInstance-tier-1", host);
        ProductInstance productInstance = new ProductInstance(productReleaseConfig, Status.INSTALLING, "vdc");
        tierInstanceShard = new TierInstance(tierProductShard, "tierInsatnceShard", "nametierInstance-tier-1", host);
        tierInstanceMongos = new TierInstance(tierProductMongos, "tierInsatnceMongos", "nametierInstance-tier-1", host);
        tierInstanceMongos.setId(new Long(1));
        tierInstanceMongos.addProductInstance(productInstance);
        when(tierManager.load(any(String.class), any(String.class), any(String.class))).thenReturn(tierProductConfig);
        when(tierInstanceDao.create(any(TierInstance.class))).thenReturn(tierInstanceMongos);
        when(tierInstanceDao.update(any(TierInstance.class))).thenReturn(tierInstanceMongos);

        when(tierInstanceDao.load(any(String.class))).thenThrow(
                new EntityNotFoundException(TierInstance.class, "dD", null));

        when(productInstanceManager.create(any(ProductInstance.class))).thenReturn(productInstance);

        List<Tier> tiers = new ArrayList<Tier>();
        tiers.add(tierProductConfig);
        tiers.add(tierProductShard);
        tiers.add(tierProductMongos);

        envResult = new Environment();
        envResult = new Environment();
        envResult.setName("environemntName");

        envResult.setTiers(tiers);

        environmentInstance = new EnvironmentInstance();
        environmentInstance.setEnvironment(envResult);
        environmentInstance.setBlueprintName("blueprintName");
        environmentInstance.setDescription("description");

        List<ProductInstance> lProductInstance = new ArrayList();
        lProductInstance.add(productInstance);

        List<TierInstance> lTierInstance = new ArrayList();
        lTierInstance.add(tierInstanceConfig);
        lTierInstance.add(tierInstanceShard);
        lTierInstance.add(tierInstanceMongos);

        environmentInstance.setTierInstances(lTierInstance);
        when(enviromentManager.load(any(String.class), any(String.class))).thenReturn(envResult);

    }

    @Test
    public void testAddProductInstances() throws Exception {

        VM host = new VM(null, "hostname", "domain");
        TierInstance tierInstance = new TierInstance(tierProductConfig, "tierInsatnce", "nametierInstance-tier-1", host);

        TierInstance tierInstanceCreated = manager.create(claudiaData, "env", tierInstance);

        assertEquals(tierInstanceCreated.getName(), tierInstanceCreated.getName());

        ProductRelease productRelease = new ProductRelease("product", "2.0");

        ProductInstance productInstance = new ProductInstance(productRelease, Status.INSTALLING, "vdc");
        tierInstanceCreated.addProductInstance(productInstance);
        TierInstance tierInstanceUpdate = manager.update(claudiaData, "env", tierInstance);

        verify(tierInstanceDao, times(2)).load(any(String.class));
        verify(tierInstanceDao, times(0)).update(any(TierInstance.class));
        verify(tierInstanceDao, times(2)).create(any(TierInstance.class));
        // assertEquals(tierInstanceUpdate.getProductInstances().size(), 3);
    }

    @Test
    public void testCreateTierInstance() throws Exception {

        VM host = new VM(null, "hostname", "domain");
        TierInstance tierInstance = new TierInstance(tierProductConfig, "tierInsatnce", "nametierInstance-tier-1", host);

        TierInstance tierInstanceCreated = manager.create(claudiaData, "env", tierInstance);
        assertEquals(tierInstanceCreated.getName(), tierInstanceCreated.getName());
        // assertNotNull(environmentCreated.getId());
    }

    @Test
    public void testGetProductNameBalanced() throws Exception {

        String productName = manager.getProductNameBalanced(tierProductShard);
        assertEquals(productName, "mongos");
    }

    @Test
    public void testGetProductNameBalancedError() throws Exception {

        String productName = manager.getProductNameBalanced(tierProductMongos);
        assertNull(productName);
    }

    @Test
    public void testGetTierInstanceToConfigure() throws Exception {

        TierInstance tierfound = manager.getTierInstanceToConfigure(environmentInstance, tierProductShard);
        assertNotNull(tierfound);
        assertEquals(tierfound.getTier().getName(), tierInstanceMongos.getTier().getName());
        assertEquals(tierfound, tierInstanceMongos);
    }

    @Test
    public void testGetTierInstanceWithTier() throws Exception {
        TierInstance tierInstance = manager.getTierInstanceWithTier(environmentInstance, tierProductMongos);
        assertEquals(tierInstance.getTier(), tierProductMongos);
        assertEquals(tierInstance, tierInstanceMongos);
    }

    @Test
    public void testGetTierProductWithName() throws Exception {
        Tier tier = manager.getTierProductWithName(envResult, "mongos");
        assertEquals(tier, tierProductMongos);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testReconfigure() throws Exception {
        Mockito.doNothing().when(productInstanceManager)
                .configure(any(ClaudiaData.class), any(ProductInstance.class), anyList());

        manager.reconfigure(claudiaData, environmentInstance, tierInstanceShard);

    }

    @Test
    public void testRemoveTierInstance() throws Exception {

        VM host = new VM(null, "hostname", "domain");
        TierInstance tierInstance = new TierInstance(tierProductConfig, "tierInsatnce", "nametierInstance-tier-1", host);
        TierInstance tierInstanceCreated = manager.create(claudiaData, "env", tierInstance);
        // when(tierInstanceDao.load(any(String.class))).thenReturn(tierInstance);
        manager.remove(tierInstanceCreated);
    }

    @Test
    public void testScalePaaSInstance() throws Exception {

        TierInstance tierInstanceCreated = manager.create(claudiaData, "env", tierInstanceShard);

        SystemPropertiesProvider propertiesProvider = mock(SystemPropertiesProvider.class);
        when(propertiesProvider.getProperty(any(String.class))).thenReturn("dd");
        // manager.create(claudiaData, tierInstanceCreated, environmentInstance,
        // propertiesProvider);

    }

}
