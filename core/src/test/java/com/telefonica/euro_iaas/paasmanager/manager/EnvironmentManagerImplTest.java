/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.manager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.dao.EnvironmentDao;
import com.telefonica.euro_iaas.paasmanager.dao.ProductReleaseDao;
import com.telefonica.euro_iaas.paasmanager.manager.impl.EnvironmentManagerImpl;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;


import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author jesus.movilla
 */
public class EnvironmentManagerImplTest extends TestCase {

    private ProductReleaseDao productReleaseDao;
    private EnvironmentDao environmentDao;

    // private TierDao tierDao;
    private TierManager tierManager;

    private ProductRelease productRelease;
    private Tier tier;
    private Set<Tier> tiers;

    private ProductReleaseManager productReleaseManager;

    private ClaudiaData data;

    @Override
    @Before
    public void setUp() throws Exception {

        productRelease = new ProductRelease("product", "2.0");
        /*
         * ProductType productType = new ProductType("Generic", "Generic"); productRelease.setProductType(productType);
         * OS os = new OS ("94", "ip", "hostname", "domain"); List<OS> oss = new ArrayList<OS>(); oss.add(os);
         * productRelease.setSupportedOOSS(oss);
         */

        List<ProductRelease> productReleases = new ArrayList<ProductRelease>();
        productReleases.add(productRelease);

        tier = new Tier();
        tier.setInitialNumberInstances(new Integer(1));
        tier.setMaximumNumberInstances(new Integer(5));
        tier.setMinimumNumberInstances(new Integer(1));
        tier.setName("tierName");
        tier.setProductReleases(productReleases);

        tiers = new HashSet<Tier>();
        tiers.add(tier);

        productReleaseDao = mock(ProductReleaseDao.class);
        when(productReleaseDao.load(any(String.class))).thenReturn(productRelease);
        productReleaseManager = mock(ProductReleaseManager.class);
        when(productReleaseManager.load(any(String.class))).thenReturn(productRelease);

        tierManager = mock(TierManager.class);
        when(tierManager.load(any(String.class), any(String.class), any(String.class))).thenReturn(tier);
        when(tierManager.create(any(ClaudiaData.class), any(String.class), any(Tier.class))).thenReturn(tier);

        /*
         * tierDao = mock(TierDao.class); when(tierDao.load(any(Long.class))).thenReturn(tier);
         * when(tierDao.create(any(Tier.class))).thenReturn(tier);
         */
        Environment envResult = new Environment();
        envResult = new Environment();
        envResult.setName("environemntName");
        envResult.setTiers(tiers);

        environmentDao = mock(EnvironmentDao.class);
        Mockito.doThrow(new EntityNotFoundException(Environment.class, "test", envResult)).when(environmentDao)
                .load(any(String.class));

        when(environmentDao.create(any(Environment.class))).thenReturn(envResult);

        data = new ClaudiaData("dd", "dd", "service");

    }

    @Test
    public void testCreateEnvironment() throws Exception {
        EnvironmentManagerImpl manager = new EnvironmentManagerImpl();

        manager.setEnvironmentDao(environmentDao);
        manager.setTierManager(tierManager);
        // manager.setProductReleaseManager(productReleaseManager);

        Environment environment = new Environment();

        environment.setName("environemntName");

        environment.setTiers(tiers);

        Environment environmentCreated = manager.create(data, environment);
        assertEquals(environmentCreated.getName(), environment.getName());
        // assertNotNull(environmentCreated.getId());
    }

    @Test
    public void testCreateEnvironmentNoTiers() throws Exception {
        EnvironmentManagerImpl manager = new EnvironmentManagerImpl();

        manager.setEnvironmentDao(environmentDao);
        manager.setTierManager(tierManager);
        // manager.setProductReleaseManager(productReleaseManager);

        Environment environment = new Environment();

        environment.setName("environemntName");
        environment.setDescription("description");

        Environment environmentCreated = manager.create(data, environment);
        assertEquals(environmentCreated.getName(), environment.getName());
        // assertNotNull(environmentCreated.getId());
    }

    @Test
    public void testRemoveEnvironment() throws Exception {
        EnvironmentManagerImpl manager = new EnvironmentManagerImpl();

        manager.setEnvironmentDao(environmentDao);
        manager.setTierManager(tierManager);

        Environment environment = new Environment();
        environment.setName("environemntName");
        environment.setOvf("DD");
        environment.setTiers(tiers);
        manager.destroy(data, environment);

    }
}
