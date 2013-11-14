/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.manager;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.dao.NetworkDao;
import com.telefonica.euro_iaas.paasmanager.manager.impl.NetworkManagerImpl;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Network;
import com.telefonica.euro_iaas.paasmanager.model.SubNetwork;



/**
 * Network, SubNetwork and Router Manager.
 * 
 * @author henar
 */
public class NetworkManagerImplTest extends TestCase {

    private static String NETWORK_NAME = "name";
    private static String SUB_NETWORK_NAME = "subname";
    private static String CIDR = "10.100.1.0/24";
    private static String CIDR_ID = "1";

    private NetworkManagerImpl networkManager;
    private NetworkInstanceManager networkInstanceManager;
    private NetworkDao networkDao;
    private SubNetworkManager subNetworkManager = null;
    private ClaudiaData claudiaData = null;

    @Override
    @Before
    public void setUp() throws Exception {

        networkManager = new NetworkManagerImpl();
        networkDao = mock(NetworkDao.class);
        networkManager.setNetworkDao(networkDao);
        subNetworkManager = mock(SubNetworkManager.class);
        networkInstanceManager = mock(NetworkInstanceManager.class);
        networkManager.setSubNetworkManager(subNetworkManager);
        networkManager.setNetworkInstanceManager(networkInstanceManager);
        claudiaData = new ClaudiaData("dd", "dd", "service");
    }

    /**
     * It tests the creation of a network.
     * @throws Exception
     */
    @Test
    public void testCreateNetwork() throws Exception {
        // Given
        Network net = new Network(NETWORK_NAME);
        SubNetwork subNet = new SubNetwork("sub-net-" + NETWORK_NAME + "-1", CIDR_ID);
        
        // When
        when(networkDao.load(any(String.class))).
            thenThrow(new EntityNotFoundException(Network.class, "test", net));
        when(subNetworkManager.create(any(SubNetwork.class))).
        thenReturn(subNet);
        when(networkDao.create(any(Network.class))).thenReturn(net);
        when(networkInstanceManager.getNumberDeployedNetwork(
            any(ClaudiaData.class))).thenReturn(0);

        // Verity
        networkManager.create(claudiaData, net);
        assertEquals(net.getNetworkName(), NETWORK_NAME);
        assertEquals(net.getSubNets().size(), 1);
        
        for (SubNetwork subNet2: net.getSubNets()) {
            assertEquals(subNet2.getName(),  "sub-net-" + NETWORK_NAME + "-1");
            assertEquals(subNet2.getCidr(), CIDR);
        }


    }
    
    /**
     * It tests the creation of a network.
     * @throws Exception
     */
    @Test
    public void testCreateNetworkSubNetSpecified() throws Exception {
        // Given
        Network net = new Network(NETWORK_NAME);
        SubNetwork subNet = new SubNetwork(SUB_NETWORK_NAME, CIDR_ID);
        net.addSubNet(subNet);

        // When
        when(networkDao.load(any(String.class))).
            thenThrow(new EntityNotFoundException(Network.class, "test", net));
        when(subNetworkManager.create(any(SubNetwork.class))).
            thenReturn(subNet);
        when(networkDao.create(any(Network.class))).thenReturn(net);

        // Verity
        networkManager.create(claudiaData,net);
        assertEquals(net.getNetworkName(), NETWORK_NAME);
        assertEquals(net.getSubNets().size(), 1);
        for (SubNetwork subNet2: net.getSubNets()) {
            assertEquals(subNet2.getName(),  SUB_NETWORK_NAME);
            assertEquals(subNet2.getCidr(), CIDR);
        }

    }
    
    /**
     * It tests the creation of a network.
     * @throws Exception
     */
    @Test
    public void testCreateNetworkAlreadyexist() throws Exception {
        // Given
        Network net = new Network(NETWORK_NAME);
        SubNetwork subNet = new SubNetwork(SUB_NETWORK_NAME, CIDR_ID);
        net.addSubNet(subNet);

        // When
        when(networkDao.load(any(String.class))).thenReturn(net);
         
        when(subNetworkManager.create(any(SubNetwork.class))).
            thenReturn(subNet);
        when(networkDao.create(any(Network.class))).thenReturn(net);

        // Verity
        networkManager.create(claudiaData, net);
        assertEquals(net.getNetworkName(), NETWORK_NAME);
        assertEquals(net.getSubNets().size(), 1);
        for (SubNetwork subNet2: net.getSubNets()) {
            assertEquals(subNet2.getName(),  SUB_NETWORK_NAME);
            assertEquals(subNet2.getCidr(), CIDR);
        }

    }

    /**
     * It tests the destruction of a network.
     * @throws Exception
     */
    @Test
    public void testDestroyNetwork() throws Exception {
        // Given
        Network net = new Network(NETWORK_NAME);

        // When
        when(networkDao.load(any(String.class))).
            thenThrow(new EntityNotFoundException(Network.class, "test", net));
        Mockito.doNothing().when(networkDao).remove(any(Network.class));
        when(networkDao.create(any(Network.class))).thenReturn(net);

        // Verity
        networkManager.delete(net);

    }

}
