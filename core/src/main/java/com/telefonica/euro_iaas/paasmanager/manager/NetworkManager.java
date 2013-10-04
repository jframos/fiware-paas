package com.telefonica.euro_iaas.paasmanager.manager;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Network;

public interface NetworkManager {
    /**
     * Create a network.
     * 
     * @param claudiaData
     * @param network
     * @return the tierInstance created
     */
    Network create(ClaudiaData claudiaData, Network network)
    throws InvalidEntityException;


    /**
     * Delete a Network.
     * @param claudiaData
     * @param network
     * @throws EntityNotFoundException
     * @throws InvalidEntityException
     * @throws InfrastructureException
     */
    void delete(ClaudiaData claudiaData, Network network)
    throws EntityNotFoundException, InvalidEntityException, InfrastructureException;

    /**
     * Retrieve all Network created in the system.
     * 
     * @return the existent networks.
     */
    List<Network> findAll();

    /**
     * Load the network.
     * 
     * @return the network.
     */
    Network load(String name, String vdc, String networkName) throws EntityNotFoundException;

    /**
     * Update a network.
     * @param network
     * @return the tierInstance created
     */
    Network update(Network network) throws InvalidEntityException;

}
