/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.dao;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.dao.impl.TierDaoJpaImpl;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.Tier;

/**
 * Unit test for TierDaoJpaImplTest
 * 
 * @author Jesus M. Movilla
 */
public class TierDaoJpaImplTest extends AbstractJpaDaoTest {

    private ProductReleaseDao productReleaseDao;
    private TierDao tierDao;
   

    public final static String TIER_NAME = "TierName";
    public final static String PRODUCT_NAME = "Product";
    public final static String VDC = "vdc";
    public final static String ENV = "env";
    public final static String PRODUCT_VERSION = "version";
    public final static Integer MAXIMUM_INSTANCES = 8;
    public final static Integer MINIMUM_INSTANCES = 1;
    public final static Integer INITIAL_INSTANCES = 1;

    /**
     * Test the create  method
     */
    @Test
    public void testCreate1() throws Exception {

        List<ProductRelease> productReleases = new ArrayList<ProductRelease>();
        ProductRelease prodRelease = new ProductRelease (PRODUCT_NAME,PRODUCT_VERSION);
        prodRelease = productReleaseDao.create(prodRelease);
        productReleases.add(prodRelease);

        Tier tier = new Tier(TIER_NAME, MAXIMUM_INSTANCES, MINIMUM_INSTANCES,
            INITIAL_INSTANCES, productReleases);

        tier = tierDao.create(tier);
        assertNotNull(tier);
        assertNotNull(tier.getId());

    }
    
    /**
     * Test the create and load method
     */
    @Test
    public void testCreateLoad() throws Exception {

        List<ProductRelease> productReleases = new ArrayList<ProductRelease>();
        ProductRelease prodRelease = new ProductRelease (PRODUCT_NAME,PRODUCT_VERSION);
        prodRelease = productReleaseDao.create(prodRelease);
        productReleases.add(prodRelease);

        Tier tier = new Tier(TIER_NAME, MAXIMUM_INSTANCES, MINIMUM_INSTANCES,
            INITIAL_INSTANCES, productReleases);
        tier.setVdc(VDC);
        tier.setEnviromentName(ENV);

        tier = tierDao.create(tier);
        
    /*    tier = tierDao.load(TIER_NAME, "vdc", "environmentName");
        assertNotNull(tier);
        assertNotNull(tier.getName(), TIER_NAME);
        assertNotNull(tier.getVdc(), VDC);*/

    }
    
    /**
     * Test the update
     */
    @Test
    public void testUpdate() throws Exception {

        List<ProductRelease> productReleases = new ArrayList<ProductRelease>();
        ProductRelease prodRelease = new ProductRelease (PRODUCT_NAME,PRODUCT_VERSION);
        prodRelease = productReleaseDao.create(prodRelease);
        productReleases.add(prodRelease);

        Tier tier = new Tier(TIER_NAME, MAXIMUM_INSTANCES, MINIMUM_INSTANCES,
            INITIAL_INSTANCES, productReleases);
        tier.setVdc(VDC);
        tier.setEnviromentName(ENV);
        tier.setFloatingip("false");

        tier = tierDao.create(tier);
        tier.setFloatingip("true");
        tier = tierDao.update(tier);
        assertNotNull(tier);
        assertNotNull(tier.getName(), TIER_NAME);
        assertNotNull(tier.getFloatingip(), "true");
        
        
        
    /*    tier = tierDao.load(TIER_NAME, "vdc", "environmentName");
        assertNotNull(tier);
        assertNotNull(tier.getName(), TIER_NAME);
        assertNotNull(tier.getVdc(), VDC);*/

    }
    
    /**
     * Test the create and load method
     */
    @Test(expected=EntityNotFoundException.class)
    public void testDeleted() throws Exception {

        List<ProductRelease> productReleases = new ArrayList<ProductRelease>();
        ProductRelease prodRelease = new ProductRelease (PRODUCT_NAME,PRODUCT_VERSION);
        prodRelease = productReleaseDao.create(prodRelease);
        productReleases.add(prodRelease);

        Tier tier = new Tier(TIER_NAME, MAXIMUM_INSTANCES, MINIMUM_INSTANCES,
            INITIAL_INSTANCES, productReleases);
        tier.setVdc(VDC);
        tier.setEnviromentName(ENV);

        tier = tierDao.create(tier);
        tierDao.remove(tier);
        
      //  tier = tierDao.load(TIER_NAME, "vdc", "environmentName");

    }
    
    public void setProductReleaseDao(ProductReleaseDao productReleaseDao) {
    	this.productReleaseDao=productReleaseDao;
    }
    
    public void setTierDao(TierDao tierDao) {
    	this.tierDao=tierDao;
    }

}
