/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */
package com.telefonica.euro_iaas.paasmanager.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.model.NetworkInstance;
import com.telefonica.euro_iaas.paasmanager.model.SubNetworkInstance;

/**
 * @author jesus.movilla
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/spring-test-db-config.xml", "classpath:/spring-dao-config.xml" })
public class NetworkInstandSubNetInstDaoJpaImplTest {

    @Autowired
    private NetworkInstanceDao networkInstanceDao;
    @Autowired
    private SubNetworkInstanceDao subNetworkInstanceDao;
    public static String NETWORK_NAME = "network_name";
    public static String SUB_NETWORK_NAME = "subnetwork_name";

    @Test
    public void testNetworkNoSubNet() throws Exception {

        NetworkInstance network = new NetworkInstance(NETWORK_NAME);

        network = networkInstanceDao.create(network);
        assertNotNull(network);
        assertEquals(network.getNetworkName(), NETWORK_NAME);
        assertEquals(network.getSubNets().size(), 0);

        List<NetworkInstance> networks = networkInstanceDao.findAll();
        assertNotNull(networks);

        NetworkInstance networkOut = networkInstanceDao.load(NETWORK_NAME);
        assertNotNull(networkOut);
        assertEquals(networkOut.getNetworkName(), NETWORK_NAME);
        assertEquals(networkOut.getSubNets().size(), 0);

    }

    @Test
    public void testNetworkWithSubNets() throws Exception {

        List<NetworkInstance> networks = networkInstanceDao.findAll();
        assertNotNull(networks);

        int number = networks.size();
        SubNetworkInstance subNet = new SubNetworkInstance(SUB_NETWORK_NAME, "1");
        subNet = subNetworkInstanceDao.create(subNet);
        assertNotNull(subNet);
        assertEquals(subNet.getName(), SUB_NETWORK_NAME);

        Set<SubNetworkInstance> subNets = new HashSet<SubNetworkInstance>();
        subNets.add(subNet);
        NetworkInstance network = new NetworkInstance(NETWORK_NAME);
        network.setSubNets(subNets);

        network = networkInstanceDao.create(network);
        assertNotNull(network);
        assertEquals(network.getNetworkName(), NETWORK_NAME);
        assertEquals(network.getSubNets().size(), 1);

        networks = networkInstanceDao.findAll();
        assertNotNull(networks);
        assertEquals(networks.size(), number + 1);

        NetworkInstance networkOut = networkInstanceDao.load(NETWORK_NAME);
        assertNotNull(networkOut);
        assertEquals(networkOut.getNetworkName(), NETWORK_NAME);
        assertEquals(networkOut.getSubNets().size(), 1);

        for (SubNetworkInstance subNet2 : networkOut.getSubNets()) {
            assertEquals(subNet2.getName(), SUB_NETWORK_NAME);
        }

    }

    @Test
    public void testDestroyNetworkInstNoSubNetInst() throws Exception {

        NetworkInstance network = new NetworkInstance(NETWORK_NAME);
        network = networkInstanceDao.create(network);
        networkInstanceDao.remove(network);
        networkInstanceDao.load(NETWORK_NAME);

    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteNetworkIInstanceWithSubNets() throws Exception {

        SubNetworkInstance subNet = new SubNetworkInstance(SUB_NETWORK_NAME, "1");
        subNet = subNetworkInstanceDao.create(subNet);
        Set<SubNetworkInstance> subNets = new HashSet<SubNetworkInstance>();
        subNets.add(subNet);
        NetworkInstance network = new NetworkInstance(NETWORK_NAME + "kk");
        network.setSubNets(subNets);
        network = networkInstanceDao.create(network);
        assertNotNull(network);
        Set<SubNetworkInstance> subNetOut = network.cloneSubNets();
        network.getSubNets().clear();

        networkInstanceDao.remove(network);
        for (SubNetworkInstance subNet2 : subNetOut) {
            subNetworkInstanceDao.remove(subNet2);
        }
        networkInstanceDao.load(NETWORK_NAME + "kk");

    }

    @Test(expected = com.telefonica.euro_iaas.commons.dao.EntityNotFoundException.class)
    public void testDeleteSubNet() throws Exception {

        SubNetworkInstance subNet = new SubNetworkInstance(SUB_NETWORK_NAME, "1");
        subNet = subNetworkInstanceDao.create(subNet);
        subNetworkInstanceDao.remove(subNet);
        subNetworkInstanceDao.load(SUB_NETWORK_NAME);
    }

    /**
     * @param productReleaseDao
     *            the productReleaseDao to set
     */
    public void setNetworkInstanceDao(NetworkInstanceDao networkInstanceDao) {
        this.networkInstanceDao = networkInstanceDao;
    }

    public void setSubNetworkInstanceDao(SubNetworkInstanceDao subNetworkInstanceDao) {
        this.subNetworkInstanceDao = subNetworkInstanceDao;
    }
}
