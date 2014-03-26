/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.model.Network;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.Tier;

/**
 * Unit test for TierDaoJpaImplTest
 * 
 * @author Jesus M. Movilla
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/spring-test-db-config.xml", "classpath:/spring-dao-config.xml" })
public class TierDaoJpaImplTest {

    @Autowired
    private ProductReleaseDao productReleaseDao;
    @Autowired
    private NetworkDao networkDao;
    @Autowired
    private TierDao tierDao;

    public final static String TIER_NAME = "TierName";
    public final static String PRODUCT_NAME = "Product";
    public final static String NETWORK_NAME = "NETWORK";
    public final static String VDC = "vdc";
    public final static String ENV = "env";
    public final static String PRODUCT_VERSION = "version";
    public final static Integer MAXIMUM_INSTANCES = 8;
    public final static Integer MINIMUM_INSTANCES = 1;
    public final static Integer INITIAL_INSTANCES = 1;

    /**
     * Test the create method
     */
    @Test
    public void testCreate1() throws Exception {

        List<ProductRelease> productReleases = new ArrayList<ProductRelease>();
        ProductRelease prodRelease = new ProductRelease(PRODUCT_NAME, PRODUCT_VERSION);
        prodRelease = productReleaseDao.create(prodRelease);
        productReleases.add(prodRelease);

        Tier tier = new Tier(TIER_NAME, MAXIMUM_INSTANCES, MINIMUM_INSTANCES, INITIAL_INSTANCES, productReleases);

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
        ProductRelease prodRelease = new ProductRelease(PRODUCT_NAME, PRODUCT_VERSION);
        prodRelease = productReleaseDao.create(prodRelease);
        productReleases.add(prodRelease);

        Tier tier = new Tier(TIER_NAME+2, MAXIMUM_INSTANCES, MINIMUM_INSTANCES, INITIAL_INSTANCES, productReleases);
        tier.setVdc(VDC);
        tier.setEnviromentName(ENV);

        tier = tierDao.create(tier);

        tier = tierDao.load(TIER_NAME+2, VDC, ENV); 
        assertNotNull(tier); 
        assertNotNull(tier.getName(), TIER_NAME+2); 
        assertNotNull(tier.getVdc(), VDC);
        

    }

    /**
     * Test the update
     */
    @Test
    public void testUpdate() throws Exception {

        List<ProductRelease> productReleases = new ArrayList<ProductRelease>();
        ProductRelease prodRelease = new ProductRelease(PRODUCT_NAME, PRODUCT_VERSION);
        prodRelease = productReleaseDao.create(prodRelease);
        productReleases.add(prodRelease);

        Tier tier = new Tier(TIER_NAME, MAXIMUM_INSTANCES, MINIMUM_INSTANCES, INITIAL_INSTANCES, productReleases);
        tier.setVdc(VDC);
        tier.setEnviromentName(ENV);
        tier.setFloatingip("false");

        tier = tierDao.create(tier);
        tier.setFloatingip("true");
        tier = tierDao.update(tier);
        assertNotNull(tier);
        assertNotNull(tier.getName(), TIER_NAME);
        assertNotNull(tier.getFloatingip(), "true");

    }

    /**
     * Test the create and load method
     */
    @Test(expected = EntityNotFoundException.class)
    public void testDeleted() throws Exception {

        List<ProductRelease> productReleases = new ArrayList<ProductRelease>();
        ProductRelease prodRelease = new ProductRelease(PRODUCT_NAME, PRODUCT_VERSION);
        prodRelease = productReleaseDao.create(prodRelease);
        productReleases.add(prodRelease);

        Tier tier = new Tier(TIER_NAME, MAXIMUM_INSTANCES, MINIMUM_INSTANCES, INITIAL_INSTANCES, productReleases);
        tier.setVdc(VDC);
        tier.setEnviromentName(ENV);

        tier = tierDao.create(tier);
        tierDao.remove(tier);

        tier = tierDao.load(TIER_NAME, "vdc", "environmentName");

    }
    
    /**
     * Test the loas tier with networks
     */
    @Test
    public void testLoadTierWithNetworks() throws Exception {

        List<ProductRelease> productReleases = new ArrayList<ProductRelease>();
        ProductRelease prodRelease = new ProductRelease(PRODUCT_NAME, PRODUCT_VERSION);
        prodRelease = productReleaseDao.create(prodRelease);
        productReleases.add(prodRelease);
        
        Set<Network> networks = new HashSet<Network>();
        Network network = new Network(NETWORK_NAME, "VDC");
        network = networkDao.create(network);
        networks.add(network);
        Network network2 = new Network(NETWORK_NAME+2, "VDC");
        network2 = networkDao.create(network2);
        networks.add(network2);

        Tier tier = new Tier(TIER_NAME, MAXIMUM_INSTANCES, MINIMUM_INSTANCES, INITIAL_INSTANCES, productReleases);
        tier.setVdc(VDC);
        tier.setNetworks(networks);
        tier.setEnviromentName(ENV);

        tier = tierDao.create(tier);
        
        Tier tier3 = tierDao.load(TIER_NAME, VDC, ENV);
        assertNotNull(tier3);
        assertEquals(tier3.getName(), TIER_NAME);
        assertEquals(tier3.getNetworks().size(), 2);
        
        
        Tier tier2 = tierDao.loadTierWithNetworks(TIER_NAME, VDC, ENV);
        assertNotNull(tier2);
        assertEquals(tier2.getName(), TIER_NAME);
        assertEquals(tier2.getNetworks().size(), 2);
    }
    
    @Test
    public void testFindAllWithNetwork () throws Exception {
    	ProductRelease prodRelease = new ProductRelease(PRODUCT_NAME, PRODUCT_VERSION);
    	List<ProductRelease> productReleases = new ArrayList<ProductRelease>();
        prodRelease = productReleaseDao.create(prodRelease);
        productReleases.add(prodRelease);
    	String net ="NETFIND";
    	Network network = new Network(net, "VDC");
        network = networkDao.create(network);
        
        assertEquals(tierDao.findAllWithNetwork(net).size(),0);
        
        Tier tier = new Tier(TIER_NAME, MAXIMUM_INSTANCES, MINIMUM_INSTANCES, INITIAL_INSTANCES, productReleases);
        tier.setVdc(VDC);
        tier.addNetwork(network);
        tierDao.create(tier);
        
        assertEquals(tierDao.findAllWithNetwork(net).size(),1);
        
        tier = new Tier(TIER_NAME+2, MAXIMUM_INSTANCES, MINIMUM_INSTANCES, INITIAL_INSTANCES, productReleases);
        tier.setVdc(VDC);
        tier.addNetwork(network);
        tierDao.create(tier);
        
        assertEquals(tierDao.findAllWithNetwork(net).size(),2);
        
        tier = new Tier(TIER_NAME+3, MAXIMUM_INSTANCES, MINIMUM_INSTANCES, INITIAL_INSTANCES, productReleases);
        tier.setVdc(VDC);
        tierDao.create(tier);
        
        assertEquals(tierDao.findAllWithNetwork(net).size(),2);
     

    }

    
   

    public void setProductReleaseDao(ProductReleaseDao productReleaseDao) {
        this.productReleaseDao = productReleaseDao;
    }

    public void setTierDao(TierDao tierDao) {
        this.tierDao = tierDao;
    }

}
