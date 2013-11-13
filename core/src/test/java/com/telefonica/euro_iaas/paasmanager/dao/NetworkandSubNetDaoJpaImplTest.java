/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */
package com.telefonica.euro_iaas.paasmanager.dao;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.model.Network;
import com.telefonica.euro_iaas.paasmanager.model.SubNetwork;



/**
 * @author jesus.movilla
 *
 */
public class NetworkandSubNetDaoJpaImplTest extends AbstractJpaDaoTest {

    private NetworkDao networkDao;
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
    public void testDestroyNetworkNoSubNet() throws Exception {

        Network network = new Network(NETWORK_NAME);

        network = networkDao.create(network);
        assertNotNull(network);
        network = networkDao.load(NETWORK_NAME);
        assertNotNull(network);
        networkDao.remove(network);
    }

    @Test
    public void testNetworkWithSubNets() throws Exception {

        List<Network> networks = networkDao.findAll();
        assertNotNull(networks);

        int number = networks.size();
        SubNetwork subNet = new SubNetwork(SUB_NETWORK_NAME, "1");
        subNet = subNetworkDao.create(subNet);
        assertNotNull(subNet);
        assertEquals(subNet.getName(), SUB_NETWORK_NAME);


        Set<SubNetwork> subNets = new HashSet<SubNetwork>();
        subNets.add(subNet);
        Network network = new Network(NETWORK_NAME);
        network.setSubNets(subNets);
 

        network = networkDao.create(network);
        assertNotNull(network);
        assertEquals(network.getNetworkName(), NETWORK_NAME);
        assertEquals(network.getSubNets().size(), 1);


        networks = networkDao.findAll();
        assertNotNull(networks);
        assertEquals(networks.size(), number + 1);

        Network networkOut = networkDao.load(NETWORK_NAME);
        assertNotNull(networkOut);
        assertEquals(networkOut.getNetworkName(), NETWORK_NAME);
        assertEquals(networkOut.getSubNets().size(), 1);
        
        for (SubNetwork subNet2: networkOut.getSubNets()) {
        	assertEquals(subNet2.getName(), SUB_NETWORK_NAME);
        }
    }
    
    @Test(expected = EntityNotFoundException.class)
    public void testDeleteNetworkWithSubNets() throws InvalidEntityException, AlreadyExistsEntityException, EntityNotFoundException  {


        SubNetwork subNet = new SubNetwork(SUB_NETWORK_NAME, "1");
        subNet = subNetworkDao.create(subNet);
        assertNotNull(subNet);
      
        Set<SubNetwork> subNets = new HashSet<SubNetwork>();
        subNets.add(subNet);
        Network network = new Network(NETWORK_NAME);
        network.setSubNets(subNets);
 
        network = networkDao.create(network);
        assertNotNull(network);
        
        network = networkDao.load(NETWORK_NAME);
        assertNotNull(network);
        assertEquals(network.getSubNets().size(), 1);
        Set<SubNetwork> subNetAux = network.cloneSubNets();

        network.setSubNets(null);
        for (SubNetwork subNet2: subNetAux) {
        	subNetworkDao.remove(subNet2);
        }
        
        networkDao.remove(network);
        

    }
     
    /**
     * @param productReleaseDao
     *            the productReleaseDao to set
     */
    public void setNetworkDao(NetworkDao networkDao) {
        this.networkDao = networkDao;
    }
    
    public void setSubNetworkDao(SubNetworkDao subNetworkDao) {
        this.subNetworkDao = subNetworkDao;
    }
}
