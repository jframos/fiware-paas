package com.telefonica.euro_iaas.paasmanager.claudia;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.exception.OpenStackException;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Network;

public interface NetworkClient {

    /**
     * Deploy the network in the infrastructure.
     * @param claudiaData
     * @param network
     * @return the id
     */
    String deployNetwork(ClaudiaData claudiaData,
            Network network) throws InfrastructureException;

    /**
     * Destroy the network in the infrastructure.
     * @param claudiaData
     * @param network
     */
    void destroyNetwork(ClaudiaData claudiaData, Network network)
    throws InfrastructureException;

    /**
     * Loads all network associated to a certain vdc
     * @param claudiaData
     * @return List<Network>
     */
    List<Network> loadAllNetwork(ClaudiaData claudiaData) throws OpenStackException ;

    /**
     * Load a Network from OpenStack
     * @param claudiaData
     * @param networkId
     * @return
     * @throws EntityNotFoundException
     */
    String loadSecurityGroup(ClaudiaData claudiaData, String networkId) throws EntityNotFoundException;
}
