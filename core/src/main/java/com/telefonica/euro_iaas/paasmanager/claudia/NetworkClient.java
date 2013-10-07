package com.telefonica.euro_iaas.paasmanager.claudia;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.exception.OpenStackException;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Network;
import com.telefonica.euro_iaas.paasmanager.model.Router;
import com.telefonica.euro_iaas.paasmanager.model.SubNetwork;

/**
 * @author henar
 * 
 */
public interface NetworkClient {

    /**
     * Add the network to the router.
     * @param claudiaData
     * @param router
     * @param network
     */
    void addNetworkToRouter(ClaudiaData claudiaData, Router router, Network network) throws InfrastructureException;

    /**
     * Deploy the network in the infrastructure.
     * @param claudiaData
     * @param network
     */
    void deployNetwork(ClaudiaData claudiaData,
            Network network) throws InfrastructureException;

    /**
     * Deploy the router.
     * @param claudiaData
     * @param router
     * @return
     * @throws InfrastructureException
     */
    void deployRouter(ClaudiaData claudiaData, Router router) throws InfrastructureException;

    /**
     * Deploy the subnetwork in the infrastructure.
     * @param claudiaData
     * @param subNet
     */
    void deploySubNetwork(ClaudiaData claudiaData, SubNetwork subNet) throws InfrastructureException;

    /**
     * Destroy the network in the infrastructure.
     * @param claudiaData
     * @param network
     */
    void destroyNetwork(ClaudiaData claudiaData, Network network) throws InfrastructureException;

    void destroyRouter(ClaudiaData claudiaData, Router router);


    /**
     * Destroy a subnet in OpenStack.
     * @param claudiaData
     * @param subnet
     * @return
     * @throws EntityNotFoundException
     */
    void destroySubNetwork(ClaudiaData claudiaData, SubNetwork subnet) throws InfrastructureException;

    /**
     * Loads all network associated to a certain vdc.
     * @param claudiaData
     * @return List<Network>
     */
    List<Network> loadAllNetwork(ClaudiaData claudiaData) throws OpenStackException;

    /**
     * Load a Network from OpenStack.
     * @param claudiaData
     * @param networkId
     * @return
     * @throws EntityNotFoundException
     */
    String loadNetwork(ClaudiaData claudiaData, Network network) throws EntityNotFoundException;


}
