/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.manager;

import java.util.ArrayList;
import java.util.List;

import com.telefonica.euro_iaas.paasmanager.dao.ProductInstanceDao;
import com.telefonica.euro_iaas.paasmanager.installator.ProductInstallator;
import com.telefonica.euro_iaas.paasmanager.manager.impl.ProductInstanceManagerImpl;
import com.telefonica.euro_iaas.paasmanager.model.Attribute;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.paasmanager.model.ProductInstance;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;
import com.telefonica.euro_iaas.paasmanager.model.dto.VM;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;


import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author jesus.movilla
 */
public class ProductInstanceImplTest extends TestCase {

    private ProductInstanceDao productInstanceDao;

    private ProductReleaseManager productReleaseManager;
    private ProductInstallator productInstallator;

    private ProductInstanceManagerImpl manager = null;

    private ProductRelease productRelease = null;
    private TierInstance tierInstance = null;

    @Override
    @Before
    public void setUp() throws Exception {

        productInstanceDao = mock(ProductInstanceDao.class);

        productReleaseManager = mock(ProductReleaseManager.class);
        productInstallator = mock(ProductInstallator.class);

        manager = new ProductInstanceManagerImpl();

        manager.setProductInstallator(productInstallator);
        manager.setProductInstanceDao(productInstanceDao);
        manager.setProductReleaseManager(productReleaseManager);

        productRelease = new ProductRelease("product", "2.0");

        List<ProductRelease> productReleases = new ArrayList<ProductRelease>();
        productReleases.add(productRelease);

        VM host = new VM(null, "hostname", "domain");

        Tier tier = new Tier();
        tier.setInitialNumberInstances(new Integer(1));
        tier.setMaximumNumberInstances(new Integer(5));
        tier.setMinimumNumberInstances(new Integer(1));
        tier.setName("tierName");
        tier.setProductReleases(productReleases);

        tierInstance = new TierInstance(tier, "tierInsatnce", "nametierInstance", host);
        ProductInstance productInstance = new ProductInstance(productRelease, Status.INSTALLING, "vdc");

        when(productReleaseManager.load(any(String.class))).thenReturn(productRelease);
        when(productInstanceDao.load(any(String.class))).thenReturn(productInstance);
        when(
                productInstallator.install(any(ClaudiaData.class), any(String.class), any(TierInstance.class),
                        any(ProductRelease.class), Matchers.<List<Attribute>> any())).thenReturn(productInstance);
    }

    @Test
    public void testCreateProductInstance() throws Exception {

        ProductInstance productInstance = new ProductInstance(productRelease, Status.INSTALLING, "vdc");
        ProductInstance productInstanceCreated = manager.create(productInstance);
        assertEquals(productInstance.getName(), productInstanceCreated.getName());

    }

    @Test
    public void testInstallProductInstanceNoAttributes() throws Exception {

        ProductInstance productInstance = new ProductInstance(productRelease, Status.INSTALLING, "vdc");
        ClaudiaData data = new ClaudiaData("ogr", "vdc", "");
        ProductInstance productInstanceCreated = manager.install(tierInstance, data, " ", productRelease, null);
        assertEquals(productInstance.getName(), productInstanceCreated.getName());

    }

}
