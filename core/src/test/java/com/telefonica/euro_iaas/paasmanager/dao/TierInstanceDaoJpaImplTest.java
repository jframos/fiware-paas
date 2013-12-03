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

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.model.Network;
import com.telefonica.euro_iaas.paasmanager.model.NetworkInstance;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;

/**
 * Unit test for TierDaoJpaImplTest
 * 
 * @author Jesus M. Movilla
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/spring-test-db-config.xml", "classpath:/spring-dao-config.xml" })
public class TierInstanceDaoJpaImplTest {


    @Autowired
    private TierInstanceDao tierInstanceDao;
    @Autowired
    private TierDao tierDao;
    @Autowired
    private NetworkInstanceDao networkInstanceDao;

    public final static String TIER_NAME = "TierName";
    public final static String TIER_INSTANCE_NAME = "TierInstanceName";
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
    public void testCreateAndLoad() throws Exception {
        Integer minimum = new Integer(1);
        Integer initial = new Integer(2);
        Integer maximum = new Integer(1);

        NetworkInstance networkInstance = new NetworkInstance ("net");
        networkInstance.setIdNetwork("ID");
        networkInstance = networkInstanceDao.create(networkInstance);

        Tier tier = new Tier(TIER_NAME, maximum, minimum, initial, null, "2", "image", "icono", "keypair", "yes", null);
        tier = tierDao.create(tier);
        TierInstance tierInst = new TierInstance();
        tierInst.setName(TIER_INSTANCE_NAME);
        tierInst.setNumberReplica(2);
        tierInst.addNetworkInstance(networkInstance);
        tierInst.setTier(tier);
        tierInst = tierInstanceDao.create(tierInst);
        assertNotNull(tierInst);
        assertNotNull(tierInst.getId());
        
        tierInst = tierInstanceDao.load(TIER_INSTANCE_NAME);
        assertEquals(tierInst.getName(), TIER_INSTANCE_NAME);
        assertEquals(tierInst.getNumberReplica(), 2); 

    }

    @Test
    public void testUpdate() throws Exception {
        Integer minimum = new Integer(1);
        Integer initial = new Integer(2);
        Integer maximum = new Integer(1);

        NetworkInstance networkInstance = new NetworkInstance ("net");
        networkInstance.setIdNetwork("ID");
        networkInstance = networkInstanceDao.create(networkInstance);

        Tier tier = new Tier(TIER_NAME, maximum, minimum, initial, null, "2", "image", "icono", "keypair", "yes", null);
        tier = tierDao.create(tier);
        TierInstance tierInst = new TierInstance ();
        tierInst.setName(TIER_INSTANCE_NAME);
        tierInst.addNetworkInstance(networkInstance);
        tierInst.setTier(tier);
        tierInst = tierInstanceDao.create (tierInst);
        assertNotNull (tierInst);
        tierInst.setNumberReplica(55);
        tierInst = tierInstanceDao.update(tierInst);
        assertNotNull (tierInst);
        assertEquals (tierInst.getNumberReplica(), 55);

    }
    
    /**
     * Test delete method
     * @throws AlreadyExistsEntityException 
     * @throws InvalidEntityException 
     */
    @Test
    public void testDelete() throws InvalidEntityException, AlreadyExistsEntityException  {
        Integer minimum = new Integer(1);
        Integer initial = new Integer(2);
        Integer maximum = new Integer(1);

        NetworkInstance networkInstance = new NetworkInstance ("net");
        networkInstance.setIdNetwork("ID");
        networkInstance = networkInstanceDao.create(networkInstance);

        Tier tier = new Tier(TIER_NAME, maximum, minimum, initial, null, "2", "image", "icono", "keypair", "yes", null);
        tier = tierDao.create(tier);
        TierInstance tierInst = new TierInstance();
        tierInst.setName(TIER_INSTANCE_NAME);
        tierInst.addNetworkInstance(networkInstance);
        tierInst.setTier(tier);
        tierInst = tierInstanceDao.create(tierInst);
        
        tierInstanceDao.remove(tierInst);
        
     /*   try {
            tierInstanceDao.load(TIER_INSTANCE_NAME);
            fail("Should have thrown an EntityNotFoundException because the tier instance does not exit!");
        } catch (Exception e) {
            assertNotNull(e);
        }*/

    }
   

    public void setNetworkInstanceDao(NetworkInstanceDao networkInstanceDao) {
        this.networkInstanceDao = networkInstanceDao;
    }

    public void setTierDao(TierDao tierDao) {
        this.tierDao = tierDao;
    }
    
    public void setTierInstanceDao(TierInstanceDao tierInstanceDao) {
        this.tierInstanceDao = tierInstanceDao;
    }

}
