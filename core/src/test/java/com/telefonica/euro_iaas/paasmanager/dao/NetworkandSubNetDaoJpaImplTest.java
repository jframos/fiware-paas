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
import com.telefonica.euro_iaas.paasmanager.model.SubNetwork;

/**
 * @author jesus.movilla
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/spring-test-db-config.xml", "classpath:/spring-dao-config.xml" })
public class NetworkandSubNetDaoJpaImplTest {

    @Autowired
    private NetworkDao networkDao;
    @Autowired
    private SubNetworkDao subNetworkDao;
    public static String NETWORK_NAME = "network_name";
    public static String SUB_NETWORK_NAME = "subnetwork_name";

    @Test
    public void testNetworkNoSubNet() throws Exception {

        Network network = new Network(NETWORK_NAME);

        network = networkDao.create(network);
        assertNotNull(network);
        assertEquals(network.getNetworkName(), NETWORK_NAME);
        assertEquals(network.getSubNets().size(), 0);

        List<Network> networks = networkDao.findAll();
        assertNotNull(networks);

        Network networkOut = networkDao.load(NETWORK_NAME);
        assertNotNull(networkOut);
        assertEquals(networkOut.getNetworkName(), NETWORK_NAME);
        assertEquals(networkOut.getSubNets().size(), 0);

    }

    @Test
    public void testDestroyNetworkNoSubNet() throws InvalidEntityException, AlreadyExistsEntityException {

        Network network = new Network(NETWORK_NAME);

        network = networkDao.create(network);
        networkDao.remove(network);
        try {
            networkDao.load(NETWORK_NAME);
            fail("Should have thrown an EntityNotFoundException because the network does not exit!");
        } catch (EntityNotFoundException e) {
            assertNotNull(e);
        } catch (Exception e) {
            System.out.println (e.getMessage());
        }

    }

    @Test
    public void testDestroySubNet() throws AlreadyExistsEntityException, InvalidEntityException {

        SubNetwork subNet = new SubNetwork(SUB_NETWORK_NAME, "1");
        subNet = subNetworkDao.create(subNet);
        subNetworkDao.remove(subNet);

        try {
            subNetworkDao.load(SUB_NETWORK_NAME);
            fail("Should have thrown an EntityNotFoundException because the subnet does not exit!");
        } catch (EntityNotFoundException e) {
            assertNotNull(e);
        }

    }

    @Test
    public void testNetworkWithSubNets() throws Exception {

        List<Network> networks = networkDao.findAll();
        assertNotNull(networks);

        int number = networks.size();
        SubNetwork subNet = new SubNetwork(SUB_NETWORK_NAME, "1");
        subNet = subNetworkDao.create(subNet);

        Set<SubNetwork> subNets = new HashSet<SubNetwork>();
        subNets.add(subNet);
        Network network = new Network(NETWORK_NAME + "aa");
        network.setSubNets(subNets);

        networkDao.create(network);

        networks = networkDao.findAll();
        assertNotNull(networks);
        assertEquals(networks.size(), number + 1);

        Network networkOut = networkDao.load(NETWORK_NAME + "aa");
        assertNotNull(networkOut);
        assertEquals(networkOut.getNetworkName(), NETWORK_NAME + "aa");
        assertEquals(networkOut.getSubNets().size(), 1);

        for (SubNetwork subNet2 : networkOut.getSubNets()) {
            assertEquals(subNet2.getName(), SUB_NETWORK_NAME);
        }
    }

    @Test
    public void testDeleteNetworkWithSubNets() throws InvalidEntityException, AlreadyExistsEntityException {

        // Given
        SubNetwork subNet = new SubNetwork(SUB_NETWORK_NAME, "1");
        subNet = subNetworkDao.create(subNet);
        Set<SubNetwork> subNets = new HashSet<SubNetwork>();
        subNets.add(subNet);
        Network network = new Network(NETWORK_NAME);
        network.setSubNets(subNets);

        network = networkDao.create(network);
        Set<SubNetwork> subNetAux = network.cloneSubNets();

        networkDao.remove(network);
        // When
        for (SubNetwork subNet2 : subNetAux) {
            subNetworkDao.remove(subNet2);
        }

        // then
        try {
            networkDao.load(NETWORK_NAME);
            fail("Should have thrown an EntityNotFoundException because the network does not exit!");
        } catch (EntityNotFoundException e) {
            assertNotNull(e);
        }

    }

    /**
     * the productReleaseDao to set
     */
    public void setNetworkDao(NetworkDao networkDao) {
        this.networkDao = networkDao;
    }

    public void setSubNetworkDao(SubNetworkDao subNetworkDao) {
        this.subNetworkDao = subNetworkDao;
    }
}
