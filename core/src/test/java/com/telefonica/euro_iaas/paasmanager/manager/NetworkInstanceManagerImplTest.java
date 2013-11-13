/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.manager;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;


import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.claudia.NetworkClient;
import com.telefonica.euro_iaas.paasmanager.dao.NetworkInstanceDao;
import com.telefonica.euro_iaas.paasmanager.manager.impl.NetworkInstanceManagerImpl;
import com.telefonica.euro_iaas.paasmanager.manager.impl.NetworkManagerImpl;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Network;
import com.telefonica.euro_iaas.paasmanager.model.NetworkInstance;
import com.telefonica.euro_iaas.paasmanager.model.RouterInstance;
import com.telefonica.euro_iaas.paasmanager.model.SubNetwork;
import com.telefonica.euro_iaas.paasmanager.model.SubNetworkInstance;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;


/**
 * Network, SubNetwork and Router Manager.
 * 
 * @author henar
 */
public class NetworkInstanceManagerImplTest {

    private static String NETWORK_NAME = "name";
    private static String SUB_NETWORK_NAME = "subname";
    private static String CIDR = "10.100.1.0/24";

    private NetworkInstanceManagerImpl networkInstanceManager;
    private NetworkInstanceDao networkInstanceDao;
    private NetworkClient networkClient = null;
    private SubNetworkInstanceManager subNetworkInstanceManager = null;
    private RouterManager routerManager = null;
    private SystemPropertiesProvider systemPropertiesProvider = null;


    @Before
    public void setUp() throws Exception {

    	networkInstanceManager = new NetworkInstanceManagerImpl();
    	networkInstanceDao = mock(NetworkInstanceDao.class);
        networkInstanceManager.setNetworkInstanceDao(networkInstanceDao);
        systemPropertiesProvider = mock(SystemPropertiesProvider.class);
        networkInstanceManager.setSystemPropertiesProvider(systemPropertiesProvider);
        networkClient = mock(NetworkClient.class);

        subNetworkInstanceManager = mock(SubNetworkInstanceManager.class);
        routerManager = mock(RouterManager.class);
        networkInstanceManager.setNetworkClient(networkClient);
        networkInstanceManager.setSubNetworkInstanceManager(subNetworkInstanceManager);
        networkInstanceManager.setRouterManager(routerManager);

    }

    /**
     * It tests the creation of a network.
     * @throws Exception
     */
    @Test
    public void testCreateNetwork() throws Exception {
        // Given
        Network net = new Network(NETWORK_NAME);
        SubNetwork subNet = new SubNetwork(SUB_NETWORK_NAME);
        net.addSubNet(subNet);
        NetworkInstance netInst = net.toNetworkInstance();
        netInst.setIdNetwork("ID");
        ClaudiaData claudiaData = new ClaudiaData("dd", "dd", "service");

        // When
        when(networkInstanceDao.load(any(String.class))).thenThrow(new EntityNotFoundException(Network.class, "test", net));
        when(systemPropertiesProvider.getProperty("key")).thenReturn("VALUE");
        Mockito.doNothing().when(networkClient).deployNetwork(any(ClaudiaData.class), any(NetworkInstance.class));
        Mockito.doNothing().when(networkClient).addNetworkToPublicRouter(any(ClaudiaData.class), any(NetworkInstance.class));
        when(subNetworkInstanceManager.create(any(ClaudiaData.class),
            	any(SubNetworkInstance.class))).thenReturn(subNet.toInstance());
        Mockito.doNothing().when(routerManager).create(any(ClaudiaData.class), any(RouterInstance.class), any(NetworkInstance.class));
        when(networkInstanceDao.create(any(NetworkInstance.class))).thenReturn(netInst);

        // Verify
        NetworkInstance netInstOut = networkInstanceManager.create(claudiaData, netInst);
        assertEquals(netInstOut.getNetworkName(), NETWORK_NAME);
        assertEquals(netInstOut.getSubNets().size(), 1);
        assertEquals(netInstOut.getSubNets().get(0).getName(), SUB_NETWORK_NAME);
    }
    
    /**
     * It tests the creation of a network.
     * @throws Exception
     */
    @Test
    public void testCreateNetworkAlreadyExist() throws Exception {
        // Given
        Network net = new Network(NETWORK_NAME);
        SubNetwork subNet = new SubNetwork(SUB_NETWORK_NAME);
        net.addSubNet(subNet);
        NetworkInstance netInst = net.toNetworkInstance();
        ClaudiaData claudiaData = new ClaudiaData("dd", "dd", "service");

        // When
        when(networkInstanceDao.load(any(String.class))).thenReturn(netInst);
        when(systemPropertiesProvider.getProperty("key")).thenReturn("VALUE");
        Mockito.doNothing().when(networkClient).
            deployNetwork(any(ClaudiaData.class), any(NetworkInstance.class));
        when(subNetworkInstanceManager.create(any(ClaudiaData.class),
        	any(SubNetworkInstance.class))).thenReturn(subNet.toInstance());
        Mockito.doNothing().when(routerManager).
            create(any(ClaudiaData.class), any(RouterInstance.class), any(NetworkInstance.class));
        when(networkInstanceDao.create(any(NetworkInstance.class))).
            thenReturn(netInst);

        NetworkInstance netInstOut =
            networkInstanceManager.create(claudiaData, netInst);
        
        // Verify
        assertEquals(netInstOut.getNetworkName(), NETWORK_NAME);
        assertEquals(netInstOut.getSubNets().size(), 1);

    }

    /**
     * It tests the destruction of a network.
     * @throws Exception
     */
    @Test
    public void testDestroyNetwork() throws Exception {
        // Given
        NetworkInstance net = new NetworkInstance(NETWORK_NAME);
        ClaudiaData claudiaData = new ClaudiaData("dd", "dd", "service");

        // When
        when(networkInstanceDao.load(any(String.class))).thenThrow(new EntityNotFoundException(Network.class, "test", net));
        when(systemPropertiesProvider.getProperty("key")).thenReturn("VALUE");
        Mockito.doNothing().when(networkClient).deployNetwork(any(ClaudiaData.class), any(NetworkInstance.class));
        Mockito.doNothing().when(subNetworkInstanceManager).delete(any(ClaudiaData.class), any(SubNetworkInstance.class));
        Mockito.doNothing().when(networkInstanceDao).remove(any(NetworkInstance.class));


        // Verify
        networkInstanceManager.delete(claudiaData, net);
        verify(networkInstanceDao).remove(any(NetworkInstance.class));


    }

}
