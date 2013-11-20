/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.claudia;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.exception.OpenStackException;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Network;
import com.telefonica.euro_iaas.paasmanager.model.NetworkInstance;
import com.telefonica.euro_iaas.paasmanager.model.RouterInstance;
import com.telefonica.euro_iaas.paasmanager.model.SubNetwork;
import com.telefonica.euro_iaas.paasmanager.model.SubNetworkInstance;

/**
 * @author henar
 */
public interface NetworkClient {


    /**
     * Add the network to the public router.
     * 
     * @param network
     */
    void addNetworkToPublicRouter(ClaudiaData claudiaData,NetworkInstance net) throws InfrastructureException;
    
    /**
     * Add the network to the router.
     * 
     * @param claudiaData
     * @param routerId
     * @param network
     */

    void addNetworkToRouter(ClaudiaData claudiaData, RouterInstance router, NetworkInstance network) throws InfrastructureException;


    /**
     * Remove the network from the router.
     * 
     * @param claudiaData
     * @param router
     * @param idNet
     */

    void deleteNetworkFromRouter(ClaudiaData claudiaData, RouterInstance router, NetworkInstance idNet)
    throws InfrastructureException;

    /**
     * Deploy the network in the infrastructure.
     * 
     * @param claudiaData
     * @param network
     */
    void deployNetwork(ClaudiaData claudiaData, NetworkInstance network) throws InfrastructureException;

    /**
     * Deploy the router.
     * 
     * @param claudiaData
     * @param router
     * @return
     * @throws InfrastructureException
     */
    void deployRouter(ClaudiaData claudiaData, RouterInstance router) throws InfrastructureException;

    /**
     * Deploy the subnetwork in the infrastructure.
     * 
     * @param claudiaData
     * @param subNet
     */
    void deploySubNetwork(ClaudiaData claudiaData, SubNetworkInstance subNet) throws InfrastructureException;

    /**
     * Destroy the network in the infrastructure.
     * 
     * @param claudiaData
     * @param networkInstance
     */
    void destroyNetwork(ClaudiaData claudiaData, NetworkInstance networkInstance) throws InfrastructureException;



    /**
     * It delete a router in Openstack.
     * @param claudiaData
     * @param router
     * @throws InfrastructureException
     */
    void destroyRouter(ClaudiaData claudiaData, RouterInstance router) throws InfrastructureException;

    /**
     * Destroy a subnet in OpenStack.
     * 
     * @param claudiaData
     * @param subnet
     * @return
     * @throws EntityNotFoundException
     */
    void destroySubNetwork(ClaudiaData claudiaData, SubNetworkInstance subnet) throws InfrastructureException;

    /**
     * Loads all network associated to a certain vdc.
     * 
     * @param claudiaData
     * @return List<Network>
     * @throws InfrastructureException 
     */
    List<NetworkInstance> loadAllNetwork(ClaudiaData claudiaData) throws InfrastructureException;

    /**
     * Load a Network from OpenStack.
     * 
     * @param claudiaData
     * @param networkId
     * @return
     * @throws EntityNotFoundException
     */
    String loadNetwork(ClaudiaData claudiaData, NetworkInstance network) throws EntityNotFoundException;
    
    /**
     * Load a subNet from Openstack.
     * @param claudiaData
     * @param subNet
     * @return
     * @throws EntityNotFoundException
     */
    String loadSubNetwork(ClaudiaData claudiaData, SubNetworkInstance subNet) throws EntityNotFoundException;

    /**
     * It deletes the interface in the public router.
     * @param claudiaData
     * @param networkInstance
     * @throws InfrastructureException
     */
	void deleteNetworkToPublicRouter(ClaudiaData claudiaData,
			NetworkInstance networkInstance) throws InfrastructureException;

}
