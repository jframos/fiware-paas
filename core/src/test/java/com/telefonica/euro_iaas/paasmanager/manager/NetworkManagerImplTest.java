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
import com.telefonica.euro_iaas.paasmanager.claudia.NetworkClient;
import com.telefonica.euro_iaas.paasmanager.dao.NetworkDao;
import com.telefonica.euro_iaas.paasmanager.manager.impl.NetworkManagerImpl;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Network;
import com.telefonica.euro_iaas.paasmanager.model.Router;
import com.telefonica.euro_iaas.paasmanager.model.SubNetwork;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;


/**
 * Network, SubNetwork and Router Manager.
 * 
 * @author henar
 */
public class NetworkManagerImplTest extends TestCase {

    private static String NETWORK_NAME = "name";
    private static String SUB_NETWORK_NAME = "subname";
    private static String CIDR = "10.100.1.0/24";

    private NetworkManagerImpl networkManager;
    private NetworkDao networkDao;
    private NetworkClient networkClient = null;
    private SubNetworkManager subNetworkManager = null;
    private RouterManager routerManager = null;
    private SystemPropertiesProvider systemPropertiesProvider = null;

    @Override
    @Before
    public void setUp() throws Exception {

        networkManager = new NetworkManagerImpl();
        networkDao = mock(NetworkDao.class);
        networkManager.setNetworkDao(networkDao);
        systemPropertiesProvider = mock(SystemPropertiesProvider.class);
        networkManager.setSystemPropertiesProvider(systemPropertiesProvider);
        networkClient = mock(NetworkClient.class);

        subNetworkManager = mock(SubNetworkManager.class);
        routerManager = mock(RouterManager.class);
        networkManager.setNetworkClient(networkClient);
        networkManager.setSubNetworkManager(subNetworkManager);
        networkManager.setRouterManager(routerManager);

    }

    /**
     * It tests the creation of a network.
     * @throws Exception
     */
    @Test
    public void testCreateNetwork() throws Exception {
        // Given
        Network net = new Network(NETWORK_NAME);
        ClaudiaData claudiaData = new ClaudiaData("dd", "dd", "service");

        // When
        when(networkDao.load(any(String.class))).thenThrow(new EntityNotFoundException(Network.class, "test", net));
        when(systemPropertiesProvider.getProperty("key")).thenReturn("VALUE");
        Mockito.doNothing().when(networkClient).deployNetwork(any(ClaudiaData.class), any(Network.class));
        Mockito.doNothing().when(subNetworkManager).create(any(ClaudiaData.class), any(SubNetwork.class));
        Mockito.doNothing().when(routerManager).create(any(ClaudiaData.class), any(Router.class), any(Network.class));
        when(networkDao.create(any(Network.class))).thenReturn(net);

        // Verity
        networkManager.create(claudiaData, net);
        assertEquals(net.getNetworkName(), NETWORK_NAME);
        assertEquals(net.getSubNets().size(), 1);
        assertEquals(net.getSubNets().get(0).getName(), "sub-net-" + NETWORK_NAME + "-1");
        assertEquals(net.getSubNets().get(0).getCidr(), CIDR);

    }

    /**
     * It tests the destruction of a network.
     * @throws Exception
     */
    @Test
    public void testDestroyNetwork() throws Exception {
        // Given
        Network net = new Network(NETWORK_NAME);
        ClaudiaData claudiaData = new ClaudiaData("dd", "dd", "service");

        // When
        when(networkDao.load(any(String.class))).thenThrow(new EntityNotFoundException(Network.class, "test", net));
        when(systemPropertiesProvider.getProperty("key")).thenReturn("VALUE");
        Mockito.doNothing().when(networkClient).deployNetwork(any(ClaudiaData.class), any(Network.class));
        Mockito.doNothing().when(subNetworkManager).delete(any(ClaudiaData.class), any(SubNetwork.class));
        Mockito.doNothing().when(networkDao).remove(any(Network.class));
        when(networkDao.create(any(Network.class))).thenReturn(net);

        // Verity
        networkManager.delete(claudiaData, net);

    }

}