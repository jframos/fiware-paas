/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.claudia.impl;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.claudia.NetworkClient;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.exception.OpenStackException;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Network;
import com.telefonica.euro_iaas.paasmanager.model.NetworkInstance;
import com.telefonica.euro_iaas.paasmanager.model.RouterInstance;
import com.telefonica.euro_iaas.paasmanager.model.SubNetwork;
import com.telefonica.euro_iaas.paasmanager.model.SubNetworkInstance;

/**
 * @author henar.munoz
 */
public class NetworkClientDummyImpl implements NetworkClient {

    /**
     * It adds the network to the router.
     * 
     * @params claudiaData
     * @params router
     * @params network
     * @throws InfrastructureException
     */

    public void addNetworkToRouter(ClaudiaData claudiaData, RouterInstance router, NetworkInstance network)
    throws InfrastructureException {

    }


    /**
     * Its delete the network from the router.
     * @params claudiaData
     * @params router
     * @params network
     * @throws InfrastructureException
     */
    public void deleteNetworkFromRouter(ClaudiaData claudiaData, RouterInstance router, NetworkInstance network)
        throws InfrastructureException {

    }

    /**
     * The deploy the network in Openstack.
     * 
     * @params claudiaData
     * @params network
     */
    public void deployNetwork(ClaudiaData claudiaData, NetworkInstance network) throws InfrastructureException {
    	network.setIdNetwork("id");
    }

    /**
     * The deploy the network in Openstack.
     * 
     * @params claudiaData
     * @params network
     */
    public void deployRouter(ClaudiaData claudiaData, RouterInstance router) throws InfrastructureException {
        router.setIdRouter("id");

    }

    /**
     * The deploy the subnet in Openstack.
     * 
     * @params claudiaData
     * @params subNet
     */
    public void deploySubNetwork(ClaudiaData claudiaData, SubNetworkInstance subNet) throws InfrastructureException {
    	subNet.setIdNetwork("id");

    }

    /**
     * It destroys the network.
     * 
     * @params claudiaData
     * @params network
     */
    public void destroyNetwork(ClaudiaData claudiaData, NetworkInstance network) throws InfrastructureException {

    }

    /**
     * It destroys the router.
     * 
     * @params claudiaData
     * @params router
     */
    public void destroyRouter(ClaudiaData claudiaData, RouterInstance router) {
        // TODO Auto-generated method stub

    }

    /**
     * It destroys the network.
     * 
     * @params claudiaData
     * @params network
     */
    public void destroySubNetwork(ClaudiaData claudiaData, SubNetworkInstance subnet) throws InfrastructureException {

    }

    /**
     * It loads all networks.
     * 
     * @params claudiaData
     */
    public List<Network> loadAllNetwork(ClaudiaData claudiaData) throws OpenStackException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * It obtains information about the network.
     * 
     * @params claudiaData
     * @params network
     * @return network information
     */
    public String loadNetwork(ClaudiaData claudiaData, NetworkInstance network) throws EntityNotFoundException {
        return "";
    }

    /**
     * It add the network interface to the public router.
     */
    public void addNetworkToPublicRouter(ClaudiaData claudiaData, NetworkInstance net) throws InfrastructureException {
        // TODO Auto-generated method stub
        
    }

}
